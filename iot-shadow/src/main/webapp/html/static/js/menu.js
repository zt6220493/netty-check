var $height = window.screen.height+100;
function loadMeanu(username, businessName,ticketId,type) {
	
	var url = prefix_url + "/eoc_ms/service/basic/findMenu";
	$.ajax( {
		type : "post",
		url : url,
		data : {
			'ticketId' : ticketId
		},
		async : false,
		success : function(data) {

			if (data.isSucceed == 1) {
				return;
			}
			showRootMenu(data.data, ticketId,'head',type);
			showRootMenu(data.data, ticketId,'',type);
			$("#loginNameDiv").html(username);
			$("#businessNameDiv").html(businessName);
			
			$("#loginNameDiv").html(username);
			$("#contentObject").css("height", $height+ "px");
			returnHome();
		},
		error : function(data) {
			alert('出错了')
		}
	});
}
var activeLiId = null;
function changeClass(id) {
	pageQuene.clear();
	
	if (id.indexOf('head') >=0 ){
		var parentObj = $("#"+id).parent().parent();
		leftMenu(parentObj.attr("id"),"head");
		id  = id.replace("head",'');
	}
	
	$("#" + activeLiId).removeClass("active");
	$("#" + id).attr("class", "active");
	var url = $($("#" + id).children("a")[0]).attr("href");
	var msg = '<object id="contentObject" onload="stateChange()" type="text/html"  data="'
			+ url
			+ '"'
			+ 'style="width: 100%;height: '
			+ $height
			+ "px"
			+ '"></object>';
	$("#contentDiv").html(msg);
	activeLiId = id;
	// 滚动条置顶
	window.scrollTo(0, 0);

}
function stateChange() {
	setTimeout(
			function() {
				var body$ = parent.parent.document
						.getElementById("contentObject").contentDocument.body;
				var height = $height;
				if (body$ != null){
					height = body$.offsetHeight > $height ? body$.offsetHeight: $height;
				}
				$("#contentObject").css("height", height + "px");
				$(body$).css("background-color", "#ffffff");
			}, 50)
}
var headLastId = null;
function leftMenu(id,falg)
{
	if (headLastId != null)
	{
		$("#"+headLastId).removeClass("active open");
	}
	$("#"+id).addClass("active open");
	showLeftMenu();
	var leftId  = id.replace("head",'');
	$("#"+leftId).siblings().hide();
	$("#"+leftId).show();
	if (falg == null){
		$("#"+leftId+" li ").eq(0).click();
	}
	headLastId = id;
}


var index = 1;
var hasLL = false;
var isFlag = 0;

function showRootMenu( data, ticketId,flag,type) {
	index = 1;
	var clazz,id,eventObj='';
	id = flag+'_left_menu_ul_1_0';
	if (flag == 'head')
	{
		clazz = 'hover hsub';
	}
	else
	{
		clazz = 'active open';
	}
	
	// 增加菜单栏现在在head的样式
	$("#sidebar").addClass(" h-sidebar navbar-collapse collapse ");
	
	for ( var i = 0; i < data.length; i++) {

		if (data[i].level == 1) {  //第一级别
			var hrefUrl = "#";
			if (($.trim(data[i].urlInfo) || "").length > 0) {
				hrefUrl = data[i].urlInfo;
			}
			if (flag == 'head'){
				var urlId = "'"+flag+"_left_menu_li_" + data[i].id+"'";
				eventObj = 'onclick="leftMenu('+urlId+')"'; 
			}
			if(data[i].urlName=="流量池")
			{
				if(type==6||type==9||type==10)
				{
//					$("#home").remove();
					hasLL = true;
					isFlag=1;
				}
				if(type==2)
				{
					isFlag=1;
				}
			}
			
			var msg = '<li  class="'+clazz+'" id="'+flag+'_left_menu_li_' + data[i].id
					+ '">' + '<a '+eventObj+' href="' + hrefUrl
					+ '" class="dropdown-toggle"> '
					+ '<i class="menu-icon fa fa-'+index+'"></i> '
					+ '<span class="menu-text" > ' + data[i].urlName
					+ ' </span> <b class="arrow fa fa-angle-down"></b> </a>'
					+ '<b class="arrow"></b>' + '</li>'
			$("#"+id).append($(msg));
			index++;
		}
	}
	showChildrenMenu(data, ticketId,flag);
}
function showChildrenMenu(data, ticketId,flag) {
	for ( var i = 0; i < data.length; i++) {
		if (data[i].level != 1) {
			var hrefUrl = "#";
			if (($.trim(data[i].urlInfo) || "").length > 0) {
				hrefUrl = data[i].urlInfo;
			}
			var msg = '';
			if ($("#"+flag+"_left_menu_ul_" + data[i].level + "_" + data[i].fatherId).length > 0) {

				msg = ''
						+ '<li onclick="changeClass(this.id)" id="'+flag+'_left_menu_li_'
						+ data[i].id + '" >' + ' <a href="' + hrefUrl
						+ '" onclick="return false"  >'
					    + '<i class="menu-icon fa fa-caret-right"></i>'
						+ data[i].urlName + ' </a>' + ' <b class="arrow"></b>'
						+ '</li>';
				$("#"+flag+"_left_menu_ul_" + data[i].level + "_" + data[i].fatherId)
						.append($(msg));
			} else {

				msg = '<ul class="submenu" id="'+flag+'_left_menu_ul_'
						+ data[i].level
						+ "_"
						+ data[i].fatherId
						+ '">'
						+ '<li onclick="changeClass(this.id)" id="'+flag+'_left_menu_li_'
						+ data[i].id + '" >' + ' <a href="' + hrefUrl
						+ '" onclick="return false"  >'
						+ '<i class="menu-icon fa fa-caret-right"></i>'
						+ data[i].urlName + ' </a>' + ' <b class="arrow"></b>'
						+ '</li></ul>';
				$("#"+flag+"_left_menu_li_" + data[i].fatherId).append($(msg));
				if ($("#"+flag+"_left_menu_li_" + data[i].fatherId).find("a:eq(0)").attr("class") != 'dropdown-toggle'){
					
					$("#"+flag+"_left_menu_li_" + data[i].fatherId).find("a:eq(0)").addClass("dropdown-toggle");
					$("#"+flag+"_left_menu_li_" + data[i].fatherId).find("a:eq(0)").append('<b class="arrow fa fa-angle-down"></b>');
					$("#"+flag+"_left_menu_li_" + data[i].fatherId).attr('onclick','return false');
				}
			}

		}
	}
}



function hideLeftMenu()
{
	//sidebar responsive
	$("#sidebar2").removeClass();
	$("#sidebar2").hide();
}
function showLeftMenu()
{
	//sidebar responsive
	$("#sidebar2").addClass("sidebar responsive");
	$("#sidebar2").show();
}
