
var $height = null;
$(function() {
	loadLoginInfos();
});
	

function headFloat(businessId) {
	$("#businessId").val(businessId);
	var str = "http://eoc.e-car.cn//eoc_ms/eoc/html/login/account.html";
	$.get(str,function(data, status) {
		if (businessId != null)
			$('li.light-blue').webuiPopover( {
				title : '账户余额',
				type : 'html',
				width : 165,
				content : data,
				placement : 'bottom-left',
				trigger : 'hover',
				arrow : false
			});
		else
			$('li.light-blue').webuiPopover( {
				type : 'html',
				width : 165,
				content : data,
				placement : 'bottom-left',
				trigger : 'hover',
				arrow : false
			});

	});
}

function loadLoginInfos() {
	var url = prefix_url + "http://eoc.e-car.cn//eoc_ms/service/basic/enFindInfo";
	$.ajax( {
				type : "post",
				url : url,
				data : {
					'ticketId' : getCookie('ticketId')
				},
				async : false,
				success : function(data) {
					if (data.isSucceed == 1) {
						window.parent.location.href = 'http://eoc.e-car.cn//eoc_ms/eoc/html/login/error.html';
					} else {
						loadMeanu(data.data.username, data.data.businessName==null?'':data.data.businessName,data.data.ticketId,data.data.type)
						var businessId = data.data.userVO.businessId;
//						var url = "/eoc_ms/eoc/html/login/home.html";
						if(hasLL)
						{
							url="http://eoc.e-car.cn/eoc_ms/eoc/html/login/pool_home.html";
						}
						else
						{
							url="http://eoc.e-car.cn/eoc_ms/eoc/html/login/home.html";
						}
						if (businessId != null && businessId != '') {
							url = url + "?businessId=" + businessId;
						}
						$("#contentObject").attr("data", url)
						headFloat(businessId)
					}
				}
			});
}

function logoutBtn() {
	var url = prefix_url + "http://eoc.e-car.cn/eoc_ms/service/basic/logout";
	$
			.ajax( {
				type : "post",
				url : url,
				data : {
					'ticketId' : getCookie('ticketId')
				},
				success : function(data) {
					window.parent.location.href = 'http://eoc.e-car.cn/eoc_ms/eoc/html/login/login.html';
				},
				error : function(data) {
					alert('出错了')
				}
			});
}
function returnHome() {
	hideLeftMenu();
	if (headLastId != null)
	{
		$("#"+headLastId).removeClass("active open");
	}
	var url =null;
	if(hasLL)
	{
		url="http://eoc.e-car.cn/eoc_ms/eoc/html/login/pool_home.html";
	}
	else
	{
		url="http://eoc.e-car.cn/eoc_ms/eoc/html/login/home.html";
	}
	//this.hasLL=hasLL;
		
	var msg = '<object id="contentObject" onload="stateChange()" type="text/html"  data="'
			+ url
			+ '"'
			+ 'style="width: 100%;height: '
			+ $height
			+ "px"
			+ '"></object>';
	$("#contentDiv").html(msg);
}
function userInfoClick() {
	hideLeftMenu();
	if (headLastId != null)
	{
		$("#"+headLastId).removeClass("active open");
	}
	var url = "http://eoc.e-car.cn/eoc_ms/eoc/html/setting/userinfo.html"
	var msg = '<object id="contentObject" onload="stateChange()" type="text/html"  data="'
			+ url
			+ '"'
			+ 'style="width: 100%;height: '
			+ $height
			+ "px"
			+ '"></object>';
	$("#contentDiv").html(msg);

}
