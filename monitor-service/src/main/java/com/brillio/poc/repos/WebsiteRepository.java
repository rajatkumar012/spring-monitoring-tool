package com.brillio.poc.repos;

import com.brillio.poc.entities.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Long> {

}
