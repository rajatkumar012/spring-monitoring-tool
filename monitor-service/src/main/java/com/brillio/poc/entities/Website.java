package com.brillio.poc.entities;

import com.brillio.poc.entities.model.StateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @OneToMany(targetEntity =WebsiteStates.class ,cascade = CascadeType.ALL)
    @JoinColumn(name = "website_fk_id", referencedColumnName = "website_id")
    private List<WebsiteStates> websiteStates;
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
