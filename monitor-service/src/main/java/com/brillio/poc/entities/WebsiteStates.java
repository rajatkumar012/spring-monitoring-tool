package com.brillio.poc.entities;

import com.brillio.poc.entities.model.StateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "websites_states")
public class WebsiteStates {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "web_states_id")
    private long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "website_state")
    private StateStatus status;

    @Column(name = "up_time")
    private Long upTime;

    @Column(name = "down_time")
    private Long downTime;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;



}
