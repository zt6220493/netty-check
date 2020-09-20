package com.ecar.eoc.mapper.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import com.ecar.eoc.entity.report.PushWeekData;

@Component
public class WeekReportMongoDAO {
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public List<PushWeekData> queryPageList(int pageSize,int index,Integer terminalId,String startdate,String enddate){
		Criteria criteria = new Criteria();
		criteria.and("terminalId").is(terminalId);
		criteria.and("startdate").gte(startdate).lt(enddate);
		criteria.and("delete").exists(false);
		Query query = new Query(criteria);
		query.with(new Sort(Direction.DESC, "_id"));
		query.skip(index*pageSize);
		query.limit(pageSize);
		return mongoTemplate.find(query, PushWeekData.class, "pushWeekData");
	}
	
	public int countPageList(Integer terminalId,String startdate,String enddate) {
		Criteria criteria = new Criteria();
		criteria.and("terminalId").is(terminalId);
		criteria.and("startdate").gte(startdate).lt(enddate);
		criteria.and("delete").exists(false);
		Query query = new Query(criteria);
		long count = mongoTemplate.count(query, "pushWeekData");
		return (int)count;
	}
	
	public void updateWeekReportForDelete(Integer terminalId,String startDate) {
		Query query = Query.query(Criteria.where("terminalId").is(terminalId)
				.andOperator(Criteria.where("startDate").is(startDate)));
	    Update update = Update.update("delete", 1);
	    mongoTemplate.updateFirst(query, update,"pushWeekData");
	}
	
	public PushWeekData queryByTerDate(Integer terminalId,String startdate) {
		Criteria criteria = new Criteria();
		criteria.and("terminalId").is(terminalId);
		criteria.and("startdate").gte(startdate);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, PushWeekData.class, "pushWeekData");
	}
}
