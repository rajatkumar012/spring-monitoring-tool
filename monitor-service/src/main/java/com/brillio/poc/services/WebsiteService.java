package com.brillio.poc.services;

import com.brillio.poc.entities.Website;
import com.brillio.poc.repos.WebsiteRepository;
import com.brillio.poc.response.WebsiteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebsiteService {
    private final WebsiteRepository repository;

    @Autowired
    public WebsiteService(WebsiteRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<WebsiteResponse> save(Website website) {
        return  ResponseEntity.ok(new WebsiteResponse(null, this.repository.save(website), "Successfully save the data"));
    }

    public Website getById(long id) {
       return this.repository.findById(id)
                .orElse(null);
    }

    public ResponseEntity<WebsiteResponse> delete(long id) {
        if( this.repository.existsById(id) ) {
            this.repository.deleteById(id);
            return ResponseEntity.ok(new WebsiteResponse(null, null, "Deleted monitoring check"));
        }
        return ResponseEntity.ok(new WebsiteResponse("Invalid Id to remove monitoring check", null, null));

    }
}
