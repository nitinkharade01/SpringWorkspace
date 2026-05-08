package com.nitin.payment.notification.entity;

import com.nitin.payment.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class NotificationLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipient;
    private String type;
    private String subject;
    @Column(length = 1000)
    private String message;
    private boolean read;
}
