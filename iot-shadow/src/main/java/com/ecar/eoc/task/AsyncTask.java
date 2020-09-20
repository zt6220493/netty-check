/**
 * Project Name:eoc-sim
 * File Name:AsyncTask.java
 * Package Name:com.ecar.eoc.task
 * Date:2018年7月27日上午9:57:33
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.task;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * ClassName:AsyncTask <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2018年7月27日 上午9:57:33 <br/>
 * @author   zhongying	 
 */
@Component
public class AsyncTask {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public static Random random = new Random();

    @Async("taskExecutor")
    public void doTaskOne() throws Exception {
        log.info("开始做任务一");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        log.info("完成任务一，耗时：" + (end - start) + "毫秒");
    }

    @Async("taskExecutor")
    public void doTaskTwo() throws Exception {
        log.info("开始做任务二");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        log.info("完成任务二，耗时：" + (end - start) + "毫秒");
    }

    @Async("taskExecutor")
    public void doTaskThree() throws Exception {
        log.info("开始做任务三");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        log.info("完成任务三，耗时：" + (end - start) + "毫秒");
    }

}

