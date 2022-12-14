package com.brillio.poc.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "websites")
public class Website {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "website_id")
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "website_name")
    private String websiteName;

    @JoinColumn(name = "website_id", referencedColumnName = "website_id")
    @OneToOne(cascade = CascadeType.ALL)
    private WebsiteStates websiteStates;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "frequency")
    private int frequency ;

    @Column(name = "is_active")
    private boolean isActive = true;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false, updatable = true)
    private Date updatedAt;
}
