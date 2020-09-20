package com.ecar.eoc.entity.vo.response;

import java.util.List;

public class TripResPageVO {
	private Integer pageIndex;
	private Integer pageNum;
	private Integer pageSize;
	private Integer totalCount;
	private List<TripReportVO> list;
	
	public Integer getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public List<TripReportVO> getList() {
		return list;
	}
	public void setList(List<TripReportVO> list) {
		this.list = list;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
}
