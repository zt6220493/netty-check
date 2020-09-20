package com.schoolguard.messages.producers;

import com.schoolguard.messages.producers.messenger.Transformer;
import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * Created by Rogers on 15-5-22.
 */
public class KafkaMsgProducer2 extends BasicKafkaProducer{
    protected KafkaProducer<String, byte[]> producer;

    public KafkaMsgProducer2(String bootstrapServers) {
        super(bootstrapServers);
    }

}
