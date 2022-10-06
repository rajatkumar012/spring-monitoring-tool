package com.brillio.poc.services;

import com.brillio.poc.entities.Scheduler;
import com.brillio.poc.repos.SchedulerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulerService {
    private final SchedulerRepository repository;

    @Autowired
    public SchedulerService(SchedulerRepository repository) {
        this.repository = repository;
    }

    public Scheduler save(Scheduler scheduler) {
        return this.repository.save(scheduler);
    }

    public Scheduler getById(long id) {
       return this.repository.findById(id)
                .orElse(null);
    }
}
