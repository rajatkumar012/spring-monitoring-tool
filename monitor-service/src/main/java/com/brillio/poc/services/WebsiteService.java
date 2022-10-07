package com.brillio.poc.services;

import com.brillio.poc.entities.Website;
import com.brillio.poc.entities.WebsiteStates;
import com.brillio.poc.entities.model.StateStatus;
import com.brillio.poc.repos.WebsiteRepository;
import com.brillio.poc.response.WebsiteResponse;
import com.brillio.poc.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WebsiteService {
    private final WebsiteRepository repository;
    private final ScheduledThreadPoolExecutor scheduledExecutorService;
    private final RestTemplate restTemplate;

    private HashMap<Long, ScheduledFuture> hashMapForScheduledTask = new HashMap<>();

    @Autowired
    public WebsiteService( WebsiteRepository repository,
                          ScheduledThreadPoolExecutor scheduledExecutorService,
                          RestTemplate restTemplate) {
        this.repository = repository;
        this.scheduledExecutorService = scheduledExecutorService;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        initiateScheduler();
    }
    public ResponseEntity<WebsiteResponse> save(Website website) {
        Website response = this.repository.save(website);
        if(!website.isActive()) {
            cancelScheduleJobByKey(website.getId());
        } else {
            runScheduler(website);
        }
        return  ResponseEntity.ok(new WebsiteResponse(null, response , "Successfully save the data"));
    }

    public Website getById(long id) {
       return this.repository.findById(id)
                .orElse(null);
    }

    public ResponseEntity<WebsiteResponse> delete(long id) {
        if ( this.repository.existsById(id) ) {
            this.repository.deleteById(id);
            cancelScheduleJobByKey(id);
            return ResponseEntity.ok(new WebsiteResponse(null, null, "Deleted monitoring check"));
        }
        return ResponseEntity.ok(new WebsiteResponse("Invalid Id to remove monitoring check", null, null));
    }

    public List<Website> getAllActiveWebsiteCheck() {
        List<Website> websiteList = this.repository.findByIsActive(true);
        return websiteList;
    }

    public void initiateScheduler() {
        getAllActiveWebsiteCheck().stream().forEach(website ->
            runScheduler(website)
        );
    }
    public void runScheduler (Website website) {
        Runnable task  = new Runnable() {
            @Override
            public void run() {
                checkWebsiteStatus(website);
            }
        };
        long frequency = Utils.getFreqBasedOnMinAndHour(website.getFrequency());
        TimeUnit timeUnit = Utils.getTimeUnit(website.getFrequency());
        log.info("run scheduler  === " +website.getFrequency() + " converted freq "+ frequency + "timeunit "+timeUnit);
        ScheduledFuture scheduledFutureJob = scheduledExecutorService.scheduleAtFixedRate(task,0, frequency, timeUnit);
        hashMapForScheduledTask.put(website.getId(), scheduledFutureJob);
    }

    private void cancelAllJobScheduler() {
        hashMapForScheduledTask.keySet().stream().forEach(key -> {
           ScheduledFuture job =  hashMapForScheduledTask.get(key);
           job.cancel(true);
        });
        scheduledExecutorService.shutdownNow();
    }

    private boolean cancelScheduleJobByKey(long key) {
        ScheduledFuture job =  hashMapForScheduledTask.get(key);
        job.cancel(true);
        return job.isCancelled();
    }
    private void checkWebsiteStatus(Website website) {
        log.info("Started task check for "+ website.getWebsiteName());
        WebsiteStates websiteStates = getWebStates(website);
        website.setWebsiteStates(websiteStates);
        this.repository.save(website);
        if(website.getWebsiteStates() != null && website.getWebsiteStates().getDownCount() >= 3) {
            log.info("consecutive failed checks for " +website.getWebsiteName());
        }
    }

    public static boolean pingURL(String url, int timeout) {
        url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }

    public WebsiteStates getWebStates(Website website) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean response = pingURL(website.getWebsiteUrl(), 1000);
        stopWatch.stop();
        log.debug("Response time is [{}]", stopWatch.getTotalTimeSeconds());
        WebsiteStates websiteStates = new WebsiteStates();
        websiteStates.setId(website.getId());
        websiteStates.setResponseTime(stopWatch.getTotalTimeSeconds());
         if (response) {
             websiteStates.setStatus(StateStatus.UP);
             websiteStates.setUpTime(stopWatch.getTotalTimeSeconds());
             websiteStates.setDownCount(0);

         } else {
            websiteStates.setStatus(StateStatus.DOWN);
             websiteStates.setUpTime(0);
             websiteStates.setDownTime(stopWatch.getTotalTimeSeconds());
             websiteStates.setDownCount(website.getWebsiteStates() == null ? 1 : website.getWebsiteStates().getDownCount() + 1);
         }
        return websiteStates;
    }
}
