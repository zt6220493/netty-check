package com.ecar.eoc.mapper.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.ecar.eoc.entity.report.UbiSendLimit;

@Component
public class UbiSendLimitDAO {
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public UbiSendLimit queryOne(Integer terminalId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("terminalId").is(terminalId));
		query.with(new Sort(Direction.DESC,"time"));
		query.limit(1);
		return mongoTemplate.findOne(query, UbiSendLimit.class,"ubiSendLimit");
	}
	
	public boolean saveLimit(UbiSendLimit ubiSendLimit) {
		if(null==ubiSendLimit.getTerminalId() ||
				null==ubiSendLimit.getLimit()) {
			return false;
		}
		mongoTemplate.save(ubiSendLimit, "ubiSendLimit");
		return true;
	}
	
	public boolean upsert(UbiSendLimit ubiSendLimit) {
		if(null==ubiSendLimit.getTerminalId() ||
				null==ubiSendLimit.getLimit()) {
			return false;
		}
		boolean flag = false;
		Query query = new Query();
		query.addCriteria(Criteria.where("terminalId").is(ubiSendLimit.getTerminalId()));
		Update update = new Update();
		update.set("limit", ubiSendLimit.getLimit());
		mongoTemplate.upsert(query, update, "ubiSendLimit");
		return flag; 
		
	}
}
