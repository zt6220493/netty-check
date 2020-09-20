package com.schoolguard.messages.producers;

import com.schoolguard.messages.producers.messenger.Transformer;
import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * Created by Rogers on 15-5-22.
 */
public class KafkaMsgProducer extends BasicKafkaProducer{
    protected KafkaProducer<String, byte[]> producer;

    public KafkaMsgProducer(String bootstrapServers) {
        super(bootstrapServers);
    }

    public void send(Topic topic, Transformer transformer){
        this.send(topic, transformer.toBytes());
    }
}
