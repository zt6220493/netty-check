var URL = window.location + "";
// var prefix_url = URL.substring(0,URL.indexOf("/eoc/html"));
var page = 1
$(function() {
	// 第一次加载
	// $("#pageNum").val("");//防止缓存数据
	loadLoginInfo();
	var pageNum = getPage();
	if (pageNum != null) {
		page = pageNum ;
	}
	// loadContentInfo(page, rowsNum);// 数据展示

});

// 跳转加载事件
/**
function loadContentInfo(pageNum, rowsNum) {
	// 后台请求
	parent.showWaitDiv();
	$.ajax( {
		type : "post",
		// url : "../../../../service/siminfo/simchange/pageList",
		url:'/',
		data : {
			'page' : pageNum, // 第几页
			'rows' : rowsNum, // 一页展示多少条数据
			'ticketId' : TicketUtil.ticketId,
			'searchContent' : $("#searchContent").val()
		// 过滤掉末端销售商
		},
		dataType : "text",
		success : function(data) {
			fillData(data, pageNum, rowsNum);
			parent.hideWaitDiv();
		},
		error : function() {
			parent.hideWaitDiv();
			showErrorMessage('出错了，请稍后再试11');
		}
	});

}

 **/

/**
 * 提交查询模块的表单
 * 
 * @return
 */
function submitQueryForm() {
	
	loadContentInfo(1,rowsNum);
}

/**
 * 填充列表的值
 * 
 * @param data
 * @param pageNum
 * @param rowsNum
 * @return
 */
function fillData(data, pageNum, rowsNum) {
	var resultJsonMessage = JSON.parse(data);
	var resultJson = resultJsonMessage.data;

	var rows = resultJson.total; // 总记录数

	$("#contentTbody").html("");

	if (0 == rows) {
		$("#ymdiv").html('');// 如果返回值為空，頁碼至空
		$("#pagedivid").hide();// 隐藏分页模块
		if (typeof (nowpage) != "undefined") { // 当前页设为0
			nowpage = 1;
		}

		return false;// 如果后台没有数据
	} else {
		$("#pagedivid").show();
	}

	totalPage = Math.ceil(rows / rowsNum);// 总页数
	// 分页样式的加载
	var pageObj = $("#ymdiv");
	appendPage(pageObj, 'ym_click', pageNum, totalPage, 'ym', rows);

	var rowData = resultJson.rows;

	$('#tableTemplate').tmpl(rowData).appendTo('#contentTbody');
}

function changeStatus(oldIccid,newIccid)
{
	showConfirm("确定要执行删除操作吗！", function(result) {
		if (result) 
		{
			$.ajax( {
				type : "post",
				// url : "../../../../service/siminfo/simchange/deleteChangeSimInfo",
				url : "/",
				data : {
					'optUser' : TicketUtil.userVO.loginName,
					'newIccid' : newIccid,
					'oldIccid':oldIccid,
					'ticketId' : TicketUtil.ticketId
				},
				dataType : "text",
				success : function(data) {
					loadContentInfo(nowpage, rowsNum)
					showSuccessMessage('删除成功！');
				},
				error : function() {
					showErrorMessage('出错了，请稍后再试22');
				}
			});
		}

	});
}

function exportExcel(){
	var param = "";	
	var id = request.QueryString("ticketId");
	var businesssId = TicketUtil.userVO.businessId;
	if(null != id && id != "")
	{
		param += "ticketId="+id+"&";
	}
	if(null != $("#searchContent").val()&& $("#searchContent").val() != "")
	{
		param += "searchContent="+$("#searchContent").val()+"&";
	}
	
	if(param.length > 0){
		param = param.substring(0,param.length-1);
	}
	
    var timestamp = new Date().getTime(); 
	var form=$("<form>");//定义一个form表单
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","post");
	form.attr("action","../../../../service/siminfo/querySimChangeExport?"+timestamp+"&"+param);
	var input1=$("<input>");
	input1.attr("type","hidden");
	input1.attr("name","exportData");
	input1.attr("value",(new Date()).getMilliseconds());
	$("body").append(form);//将表单放置在web中
	form.append(input1);
	form.submit();//表单提交 	
}
