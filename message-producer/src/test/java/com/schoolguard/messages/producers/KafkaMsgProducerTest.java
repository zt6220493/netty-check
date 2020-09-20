package com.schoolguard.messages.producers;

import com.schoolguard.messages.producers.messenger.NoticeMessenger;
import com.schoolguard.messages.producers.messenger.ReceiverType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class KafkaMsgProducerTest {
    private KafkaMsgProducer producer;

    @org.junit.Before
    public void setUp() throws Exception {
        producer = new KafkaMsgProducer("127.0.0.1:9092");
    }

    @org.junit.After
    public void tearDown() throws Exception {
        producer.shutdown();
    }

    @Test
    public void testSend() throws Exception {
        Set<Integer> receivers = new HashSet<Integer>(Arrays.asList(1, 4, 10));
        NoticeMessenger messenger = new NoticeMessenger(1, 1, "第一小学",  1, "Mr.teacher1","http://bt.ly/p1.jpg",
                receivers, ReceiverType.CLASS, true, "张老师", null, new Date(), "五一劳动节放假三天");

//        producer.send(KafkaTopic.NOTICE_TOPIC, messenger);

    }
}