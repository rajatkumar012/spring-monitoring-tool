package com.brillio.poc.repos;

import com.brillio.poc.entities.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Long> {

    @Transactional
    List<Website> findByIsActive(boolean isActive);
}
