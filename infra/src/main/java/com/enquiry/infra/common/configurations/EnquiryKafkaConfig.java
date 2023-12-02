package com.enquiry.infra.common.configurations;


import java.util.Collections;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

import com.enquiry.domain.enquiry.EnquiryConstants.ENQUIRY_EVENTS;

import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

@Configuration
@SuppressWarnings("removal")
public class EnquiryKafkaConfig {

	@Bean
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate(
            KafkaProperties properties) {
        Map<String, Object> props = properties.buildProducerProperties();
        return new ReactiveKafkaProducerTemplate<String, String>(SenderOptions.create(props));
    }
    
    /* ----------------------payment status consumer------------------------------*/
	@Bean(name="NewEnquiryOptions")
    public ReceiverOptions<String, String> kafkaPaymentStatusReceiverOptions(KafkaProperties kafkaProperties) {
		Map<String, Object> props = kafkaProperties.buildConsumerProperties();
		/*this is kafka's default consumer group , create new in production*/		
		props.put(ConsumerConfig.GROUP_ID_CONFIG , "test-consumer-group");
		ReceiverOptions<String, String> basicReceiverOptions = ReceiverOptions.create(props);
        return basicReceiverOptions.subscription(Collections.singletonList(ENQUIRY_EVENTS.NEW_ENQUIRY.name()));
    }
	
    @Bean(name="NewEnquiryKafkaTemplate")
    public ReactiveKafkaConsumerTemplate<String, String> reactivePaymentStatusConsumerTemplate(@Qualifier("NewEnquiryOptions") ReceiverOptions<String, String> kafkaPaymentStatusReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<String, String>(kafkaPaymentStatusReceiverOptions);
    }
}