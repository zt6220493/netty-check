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
import org.springframework.util.StringUtils;

import com.ecar.eoc.entity.report.UbiOneTrip;

@Component
public class TripReportMongoDAO {
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public List<UbiOneTrip> queryPageList(int pageSize,int index,Integer terminalId,String startTime){
		Criteria criteria = new Criteria();
		criteria.and("terminalId").is(terminalId);
		criteria.and("startTime").gte(startTime);
		criteria.and("delete").exists(false);
		Query query = new Query(criteria);
		query.with(new Sort(Direction.DESC, "_id"));
		query.skip(index*pageSize);
		query.limit(pageSize);
		return mongoTemplate.find(query, UbiOneTrip.class, "ubiOneTrip");
	}
	
	public int countPageList(Integer terminalId,String startTime) {
		Criteria criteria = new Criteria();
		criteria.and("terminalId").is(terminalId);
		criteria.and("startTime").gte(startTime);
		criteria.and("delete").exists(false);
		Query query = new Query(criteria);
		long count = mongoTemplate.count(query, "ubiOneTrip");
		return (int)count;
	}
	
	/**
	 * 更新行程记录为删除状态
	 * @param tripCode
	 * @param terminalId
	 */
	public void updateUbiOneTripForDelete(String tripCode) {
		Query query = Query.query(Criteria.where("tripCode").is(tripCode));
	    Update update = Update.update("delete", 1);
	    mongoTemplate.updateFirst(query, update,"ubiOneTrip");
	}
	
	/**
	 * 根据tripCode查询里程报告数据
	 * @param tripCode
	 * @return
	 */
	public UbiOneTrip queryByTripCode(String tripCode) {
		Query query = Query.query(Criteria.where("tripCode").is(tripCode));
		return mongoTemplate.findOne(query, UbiOneTrip.class, "ubiOneTrip");
	}
	
	/**
	 * 查询参与行程报告排名的数量
	 * @return
	 */
	public long getRankingTotalCount(Integer cityCode,String startTime,String endTime) {
		Criteria criteria = new Criteria();
		criteria.and("startTime").gte(startTime).lte(endTime);
		criteria.and("cityCode").is(cityCode);
		Query query = new Query(criteria);
		return mongoTemplate.count(query, "ubiOneTrip");
	}
	
	/**
	 * 
	 * @param terminalId
	 * @param startTime
	 * @param endTime
	 * @param ranking
	 * @return
	 */
	public long getCurrentRanking(Integer terminalId,String startTime,String endTime,int ranking) {
		Criteria criteria = new Criteria();
		criteria.and("terminalId").is(terminalId);
		criteria.and("startTime").gte(startTime).lte(endTime);
		criteria.and("ranking").gt(ranking);
		Query query = new Query(criteria);
		return mongoTemplate.count(query, "ubiOneTrip");
	}
	
	/**
	 * 
	 * @param tripCode
	 * @param city
	 * @param cityCode
	 * @param preRanking
	 */
	public void updateRanking(String tripCode,String city,String cityCode,String startAddress,String endAddress,Integer preRanking) {
		Query query = Query.query(Criteria.where("tripCode").is(tripCode));
		Update update = new Update();
		if(!StringUtils.isEmpty(city)) {
			update.set("rankingCity", city);
		}
		if(!StringUtils.isEmpty(cityCode)) {
			update.set("cityCode", cityCode);
		}
		if(!StringUtils.isEmpty(startAddress)) {
			update.set("startAdress", startAddress);
		}
		if(!StringUtils.isEmpty(endAddress)) {
			update.set("endAdress", endAddress);
		}
		update.set("preRanking", preRanking);
		mongoTemplate.updateFirst(query, update,"ubiOneTrip");
	}
}
