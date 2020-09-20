package com.ecar.eoc.service;

import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.*;

public class TestService {

    private static Map<String, BlockingQueue<MsgDatagram>> map = new ConcurrentHashMap<String, BlockingQueue<MsgDatagram>>();

    // 从队例中获取数据的超时时间(毫秒)
    private static final int READ_OUT_TIME = 8000;

    // 往队例中增加数据超时时间(毫秒)
    private static final int OFFER_OUT_TIME = 2000;

    // 等待socket数据时间
    public static final int AWAITUNINTERRUPTIBLY_TIME = 5;


    public static void put (MsgDatagram ram) {
        BlockingQueue<MsgDatagram> queue = map.get("50");
        if (queue != null) {
            try {
                boolean iSucced = queue.offer(ram, OFFER_OUT_TIME, TimeUnit.MILLISECONDS);
                if (!iSucced) {
                    System.out.println("SocketBlockHelper.setMsgDatagram offer failed!");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static MsgDatagram get(String token, String serialNo , Integer waitTime) {
        // 防止上次的脏数据
        map.remove(token);
        BlockingQueue<MsgDatagram> queue = new ArrayBlockingQueue<MsgDatagram>(1);
        map.put(token, queue);

        MsgDatagram ram = null;
        try {
            Long startTime = System.currentTimeMillis();
            ram = queue.poll(waitTime == null ? READ_OUT_TIME : waitTime.intValue(), TimeUnit.MILLISECONDS);
            Long endTime = System.currentTimeMillis();
            //判断流水号是否一致
            System.out.println("SocketBlockHelper.get queue result:" + JSON.toJSONString(ram) + ",serialNo=" + serialNo + ",时长：" + waitTime);
            if (ram != null && !StringUtils.isEmpty(serialNo) && !serialNo.equals(String.valueOf(ram.getSerialNo()))) {
                System.out.println("SocketBlockHelper.get data:" + JSON.toJSONString(ram));
                //流水号不一致，过滤该信息重新获取
                Long castTime = endTime - startTime;
                if (waitTime > castTime.intValue()) {
                    waitTime = waitTime - castTime.intValue();
                    System.out.println("SocketBlockHelper.get serialNo:" + serialNo + ",响应流水号：" + ram.getSerialNo() + "不一致，剩余时间：" + waitTime);
                    get(token, serialNo, waitTime);
                } else {
                    System.out.println("SocketBlockHelper.get serialNo:" + serialNo + ",响应流水号：" + ram.getSerialNo() + "不一致，直接返回");
                    return null;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //取完以后删除
        map.remove(token);
        return ram;
    }

    public static void main(String[] args) {
        final String sn = System.currentTimeMillis() + "";
        new Thread(){
            public void run(){
                System.out.println("监听数据：");
                get("50", sn, READ_OUT_TIME);
            }
        }.run();
        System.out.println(1111);
        new Thread(){
            public void run(){
                try {
                    System.out.println("添加数据：");
                    for (int i = 0; i < 3; i++) {
                        MsgDatagram datagram = new MsgDatagram();
                        datagram.setSerialNo(11223300 + i * 100);
                        datagram.setToken("50");
                        put(datagram);
                        System.out.println("第" + i +"次响应：" + JSON.toJSONString(datagram));
                        Thread.sleep(2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.run();
    }
}
