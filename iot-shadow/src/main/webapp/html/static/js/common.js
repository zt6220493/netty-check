function setCookie(c_name, value, expiredays){
　　　　var exdate=new Date();
 　　　　exdate.setDate(exdate.getDate() + expiredays);
 　　　　document.cookie=c_name+ "=" + escape(value) + ((expiredays==null) ? "" : ";expires="+exdate.toGMTString()) + ";path=/";
}
function getCookie(c_name){
　　　　if (document.cookie.length>0){　　// 先查询cookie是否为空，为空就return ""
　　　　　　c_start=document.cookie.indexOf(c_name + "=")　　// 通过String对象的indexOf()来检查这个cookie是否存在，不存在就为
														// -1
　　　　　　if (c_start!=-1){ 
　　　　　　　　c_start=c_start + c_name.length+1　　// 最后这个+1其实就是表示"="号啦，这样就获取到了cookie值的开始位置
　　　　　　　　c_end=document.cookie.indexOf(";",c_start)　　// 其实我刚看见indexOf()第二个参数的时候猛然有点晕，后来想起来表示指定的开始索引的位置...这句是为了得到值的结束位置。因为需要考虑是否是最后一项，所以通过";"号是否存在来判断
　　　　　　　　if (c_end==-1) c_end=document.cookie.length　　
　　　　　　　　return unescape(document.cookie.substring(c_start,c_end))　　// 通过substring()得到了值。想了解unescape()得先知道escape()是做什么的，都是很重要的基础，想了解的可以搜索下，在文章结尾处也会进行讲解cookie编码细节
　　　　　　} 
　　　　}
　　　　return null
}


// 公用JS
// 获取URL前缀
var URL = window.location + "";
var prefix_url = URL.substring(0, URL.indexOf("/eoc_ms/eoc"));
// 获取URL参数的方法 request.QueryString("参数名称");
var request = {
	QueryString : function(val) {
		var uri = window.location.search;
		var re = new RegExp("" + val + "=([^\&\?]*)", "ig");
		var res = ((uri.match(re)) ? (uri.match(re)[0].substr(val.length + 1))
				: null);
		if(res == null)
			return getCookie(val);
		else
			return res
	}
}

// 日期类型转换
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}

/** 
*转换long值为日期字符串 
* @param l long值 
* @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss 
* @return 符合要求的日期字符串 
*/ 
function getFormatDateByLong(l, pattern) { 
	return getFormatDate(new Date(l), pattern); 
} 

/**
 * IE8下不支持new Date(dateString) 格式为 YYYY-MM-DD Parses string formatted as
 * YYYY-MM-DD to a Date object. If the supplied string does not match the
 * format, an invalid Date (value NaN) is returned.
 * 
 * @param {string}
 *            dateStringInRange format YYYY-MM-DD, with year in range of
 *            0000-9999, inclusive.
 * @return {Date} Date object representing the string.
 */
function dateFormat(dateStringInRange) {
	var isoExp = /^\s*(\d{4})-(\d\d)-(\d\d)\s*$/, date = new Date(NaN), month, parts = isoExp
			.exec(dateStringInRange);

	if (parts) {
		month = +parts[2];
		date.setFullYear(parts[1], month - 1, parts[3]);
		if (month != date.getMonth() + 1) {
			date.setTime(NaN);
		}
	}
	return date;
}
/** *******************图片操作开始****************************** */

// 图片上传控件
// formID: formID
// imgID： 上传图片后展示的ID地址
function subimtBtn(formID, imgID) {
	var form = $("#" + formID);
	form.ajaxSubmit( {
		dataType : "text",
		success : function(data) {
			var result = JSON.parse(data);
			if (result.isSucceed == 1) {
				alert("上传出错");
			} else {
				var resultJson = result.data;
				$("#" + imgID).attr("src", resultJson);
			}

		},
		error : function() {
			alert("错误了");
		}
	});
}

function delImg(imgID) {
	$("#" + imgID).attr("src", "");
}

/** *******************图片操作结束****************************** */

/** *******************字符串转换操作结束****************************** */

function changContextStyle(theString) {
	var str = "";
	str = replaceAll(theString, ">", "&gt;");
	str = replaceAll(str, "<", "&lt;");
	str = replaceAll(str, " ", "&nbsp;");
	str = replaceAll(str, "\"", "&quot;");
	str = replaceAll(str, "\'", "&#39;");
	str = replaceAll(str, "\n", "\\n");
	str = replaceAll(str, "\r", "\\r");
	str = replaceAll(str, "\′", "&acute");
	str = replaceAll(str, ":", "&#58");
	return str;
}

function replaceAll(str, sptr, sptr1) {
	while (str.indexOf(sptr) >= 0) {
		str = str.replace(sptr, sptr1);
	}
	return str;
}

// 显示，隐藏div
function showDiv(obj) {
	$(obj).find("div").show();
}
function hiddenDiv(obj) {
	$(obj).find("div").hide();
}

function getStrCounts(str, cons) {
	return (str.split(cons)).length - 1;
}

function showMessage(message) {
	bootbox.dialog( {
		message : "<span class='bigger-110'>" + message + "</span>",
		buttons : {
			"button" : {
				"label" : "确定",
				"className" : "btn-sm"
			}
		}
	});
}
function getFormatDate(dateobj,format)
{
	if (dateobj == null || dateobj ==''){
		return '';
	}
	return new Date(dateobj).format(format)
}


// 显示加载等待框
function showWaitDiv()
{
	$("#waitDIV").css("display","block");
}	
// 隐藏加载等待框
function hideWaitDiv()
{
	$("#waitDIV").css("display","none");
}

function  dis()   
	{   
        var a = document.getElementsByTagName("input");   
        for(var i=0; i<a.length; i++)   
        {   
        	if(a[i].type=="checkbox" || a[i].type=="radio")
            {
            	a[i].disabled=true;  
            }
        	if(a[i].type=="text")
            {
            	a[i].readOnly=true;  
            }
        }   
        var b = document.getElementsByTagName("select");   
        for (var i=0; i<b.length; i++)   
        {   
        	b[i].disabled=true;   
        } 
        var t = document.getElementsByTagName("textarea");   
        for (var i=0; i<t.length; i++)   
        {   
        	t[i].readOnly=true;   
        } 
	}   
	
	function  show()   
	{   
        var a = document.getElementsByTagName("input");   
        for(var i=0; i<a.length; i++)   
        {   
        	if(a[i].type=="checkbox" || a[i].type=="radio")
            {
            	a[i].disabled=false;  
            }
        	if(a[i].type=="text")
            {
            	a[i].readOnly=false;  
            }
        }   
        var b = document.getElementsByTagName("select");   
        for (var i=0; i<b.length; i++)   
        {   
        	b[i].disabled=false;   
        } 
        var t = document.getElementsByTagName("textarea");   
        for (var i=0; i<t.length; i++)   
        {   
        	t[i].readOnly=false;   
        } 
	}
	
	Array.prototype.remove=function(dx) 
	{ 
	    if(isNaN(dx)||dx>this.length){return false;} 
	    for(var i=0,n=0;i<this.length;i++) 
	    { 
	        if(this[i]!=this[dx]) 
	        { 
	            this[n++]=this[i] 
	        } 
	    } 
	    this.length-=1 
	} 
	
	
	//setCookiePb: 设置缓存
	//c_nam:缓存名称;（命名规则[根据文件目录名称]：模块名称_页面名称_html页面名称_方法名称，如：sim_siminfo_init_loadContentInfo）
	//value:缓存值;
	//millisecond:毫秒(缓存失效时间)
	function setCookiePb(c_name,value,millisecond)
	{
		var exdate=new Date();
		exdate.setTime(exdate.getTime()+parseInt(millisecond==null ? 0 : millisecond));
		document.cookie = c_name + "=" + escape(value)+ ((millisecond == null) ? "" : "^;expires=" + exdate.toGMTString());  
	}
	
	//获取缓存
	function getCookiePb(c_name)
	{
		if (document.cookie.length>0)
		{
		  c_start=document.cookie.indexOf(c_name + "=")
		  if (c_start!=-1)
		  { 
		    c_start=c_start + c_name.length+1 
		    c_end=document.cookie.indexOf(";",c_start)
		    if (c_end==-1) c_end=document.cookie.length
		    return unescape(document.cookie.substring(c_start,c_end))
		  } 
		}
		return null;
	}
	
	
	