package com.nitin.payment.transaction.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.nitin.payment.common.CommonConstants.*;

@Configuration
public class KafkaConfig {
    @Bean
    NewTopic transactionCreatedTopic() {
        return TopicBuilder.name(TRANSACTION_CREATED_TOPIC).partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic transactionStatusUpdatedTopic() {
        return TopicBuilder.name(TRANSACTION_STATUS_UPDATED_TOPIC).partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic fraudAlertTopic() {
        return TopicBuilder.name(FRAUD_ALERT_TOPIC).partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic reconciliationCompletedTopic() {
        return TopicBuilder.name(RECONCILIATION_COMPLETED_TOPIC).partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic notificationTopic() {
        return TopicBuilder.name(NOTIFICATION_TOPIC).partitions(3).replicas(1).build();
    }
}
