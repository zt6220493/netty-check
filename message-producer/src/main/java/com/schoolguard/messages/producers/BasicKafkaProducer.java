package com.schoolguard.messages.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by Rogers on 15-5-22.
 */
public abstract class BasicKafkaProducer {
    protected KafkaProducer<String, byte[]> producer;

    /**
     * 初始化KafkaProducer
     *
     * @param bootstrapServers 逗号分隔的host:port字符串：<code>host1:port1,host2:port2,...</code>
     */
    public BasicKafkaProducer(String bootstrapServers) {
        Properties config = setConfig();
        config.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        this.producer = new KafkaProducer<>(config);
    }

    public void send(Topic topic, byte[] msgBytes){
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic.toString(), msgBytes);
        producer.send(record);
    }

    public void shutdown(){
        producer.close();
    }

    private Properties setConfig(){
        Properties config = new Properties();
        config.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        config.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        config.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        config.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        config.setProperty(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, "30000"); //如果断开连接，则每隔30重试一次
        config.setProperty(ProducerConfig.RETRIES_CONFIG, "1"); //失败后重试
        config.setProperty(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "10000"); //失败后10s后重试

        return config;
    }
}
