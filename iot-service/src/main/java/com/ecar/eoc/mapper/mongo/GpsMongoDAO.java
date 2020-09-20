package com.ecar.eoc.mapper.mongo;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.ecar.eoc.entity.trip.EocGps;

@Component
public class GpsMongoDAO {
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public EocGps queryLast(Integer terminalId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("terminalId").is(terminalId));
		query.with(new Sort(Direction.DESC,"time"));
		query.limit(1);
		return mongoTemplate.findOne(query, EocGps.class,"eocGps");
	}
	
	public List<EocGps> queryLastGpses(Integer terminalId,int count,String collectionName){
		Query query = new Query();
		query.addCriteria(Criteria.where("terminalId").is(terminalId));
		query.with(new Sort(Direction.DESC,"time"));
		query.limit(5);
		return mongoTemplate.find(query, EocGps.class,collectionName);
	}
	
	public List<EocGps> queryGpsByTerTime(Integer terminalId,Date startTime,Date endTime,String collectionName){
		Criteria criteria = new Criteria();
		criteria.and("terminalId").is(terminalId);
		criteria.and("time").gte(startTime).lte(endTime);
		Query query = new Query(criteria);
		query.with(new Sort(Direction.ASC,"time"));
		return mongoTemplate.find(query, EocGps.class,collectionName);
	}

	public List<EocGps> queryGpsByTripTime(Integer terminalId,Date startTime,Date endTime,String collectionName){
		Criteria criteria = new Criteria();
		criteria.and("terminalId").is(terminalId);
		if (startTime != null) {
			criteria.and("time").gte(startTime);
		}
		if (endTime != null) {
			criteria.and("time").lte(endTime);
		}
		Query query = new Query(criteria);
		query.with(new Sort(Direction.DESC,"time"));
		query.limit(6);
		return mongoTemplate.find(query, EocGps.class,collectionName);
	}
}
