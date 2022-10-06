package com.brillio.poc.controllers;

import com.brillio.poc.entities.Website;
import com.brillio.poc.response.WebsiteResponse;
import com.brillio.poc.services.WebsiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/website")
@Slf4j
public class WebsiteController {
    @Autowired
    private WebsiteService websiteService;

    @PostMapping("/add")
    public ResponseEntity<WebsiteResponse> save(@RequestBody Website website, @RequestHeader("userId") String id) {
        website.setUserId(Long.valueOf(id));
        return websiteService.save(website);
    }

    @PutMapping("/update")
    public ResponseEntity<WebsiteResponse> update( @RequestParam("id") Long id , @RequestParam boolean isActive) {
        Website website = websiteService.getById(id);
        if( website != null) {
            website.setActive(isActive);
            return  websiteService.save(website);
        } else {
            return  ResponseEntity.ok(new WebsiteResponse("Bad Request", null, "Invalid Id"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<WebsiteResponse> deleteCheck(@RequestParam long id) {
        return websiteService.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebsiteResponse> getById(@PathVariable long id) {
       Website website =  websiteService.getById(id);
        if (website != null) {
            return ResponseEntity.ok(new WebsiteResponse(null, website, "Successfully retrieve data"));
        }
        return ResponseEntity.ok(new WebsiteResponse("Bad Request", null, "Invalid Id to remove monitoring check"));
    }

}
