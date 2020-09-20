package com.ecar.eoc.mapper.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.ecar.eoc.entity.report.PushDayData;

@Component
public class DayDataMongoDAO {
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public List<PushDayData> queryPageList(Integer terminalId,long tsStart,long tsEnd){
		Criteria criteria = new Criteria();
		criteria.and("ts").gte(tsStart).lte(tsEnd);
		criteria.and("terminalId").is(terminalId);
		Query query = new Query(criteria);
		query.with(new Sort(Direction.ASC, "_id"));
		return mongoTemplate.find(query, PushDayData.class, "pushDayData");
	}
}
