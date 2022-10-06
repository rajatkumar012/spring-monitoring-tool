package com.brillio.poc.controllers;

import com.brillio.poc.entities.Scheduler;
import com.brillio.poc.services.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduler")
@Slf4j
public class SchedulerController {
    @Autowired
    private SchedulerService schedulerService;

    @PostMapping
    public Scheduler save(@RequestBody Scheduler scheduler) {
        return schedulerService.save(scheduler);
    }

}
