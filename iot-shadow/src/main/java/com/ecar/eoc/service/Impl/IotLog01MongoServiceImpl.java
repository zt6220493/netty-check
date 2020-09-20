package com.ecar.eoc.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ecar.eoc.common.DateUtil;
import com.ecar.eoc.entity.mongo.DeviceStatus;
import com.ecar.eoc.service.IotLog01MongoService;


/**
 * 将 kafka 消费的数据存到 mongo 中
 */
@Service
public class IotLog01MongoServiceImpl implements IotLog01MongoService {

    @Resource
    private MongoTemplate mongoTemplate;
    @Autowired
    private Environment env; 

    /**
     * 插入数据到 mongo
     */
    public void save(DeviceStatus mongoDevice) {
    	//测试环境
    	if("dev".equals(env.getProperty("spring.profiles.active"))) {
    		mongoTemplate.save(mongoDevice, "iotlog");
    	}else {
    		 // 表名为 iotLog + 当前系统时间
            mongoTemplate.save(mongoDevice, "iotlog_" + DateUtil.formatMongo(new Date(),"yyyyMMdd"));
    	}
    }
    
    public void save(List<DeviceStatus> list,String collectionName) {
    	//测试环境
    	if("dev".equals(env.getProperty("spring.profiles.active"))) {
    		mongoTemplate.insert(list, "iotlog");
    	}else {
    		mongoTemplate.insert(list, collectionName);
    	}
    }

    /**
     * 根据设备 id 查询一段时间的连接状态数据
     * @param terminalId 设备 ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<DeviceStatus> queryByTerminalIdAndTime(String terminalId, Date startTime, Date endTime) {
    	List<DeviceStatus> totalMongoDeviceList = new ArrayList<>();
    	if("dev".equals(env.getProperty("spring.profiles.active"))) {
    		Criteria criteria = new Criteria();
            Query query = new Query(criteria);
            criteria.andOperator(Criteria.where("terminalId").is(terminalId),
                    // 时间查询，要转为时间戳
                    Criteria.where("time").gte(startTime).lte(endTime));
            // 按照消息 Unix 时间戳降序排列
            query.with(new Sort(Sort.Direction.ASC, "time"));
            totalMongoDeviceList = mongoTemplate.find(query, DeviceStatus.class,"iotlog" + DateUtil.formatMongo(endTime,"yyyyMMdd"));
    	}else {
    		Criteria criteria = new Criteria();
            Query query = new Query(criteria);
            criteria.andOperator(Criteria.where("terminalId").is(terminalId),
                    // 时间查询，要转为时间戳
                    Criteria.where("time").gte(startTime).lte(endTime));
            // 按照消息 Unix 时间戳降序排列
            query.with(new Sort(Sort.Direction.ASC, "time"));
            // 为同一天
            if (DateUtil.isSameDay(startTime, endTime)) {
                totalMongoDeviceList = mongoTemplate.find(query, DeviceStatus.class,
                        "iotlog_" + DateUtil.formatMongo(endTime,"yyyyMMdd"));
            } else {
                // 跨天查询
                totalMongoDeviceList.addAll(mongoTemplate.find(query, DeviceStatus.class,
                        "iotlog_" + DateUtil.formatMongo(startTime,"yyyyMMdd")));
                totalMongoDeviceList.addAll(mongoTemplate.find(query, DeviceStatus.class,
                        "iotlog_" + DateUtil.formatMongo(endTime,"yyyyMMdd")));
            }
    	}
        return totalMongoDeviceList;
    }
}