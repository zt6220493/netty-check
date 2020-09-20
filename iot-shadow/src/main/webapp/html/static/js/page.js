/*****************分页栏公共函数***************/
var nowpage = 1;//记下当前页面       
var maxPageMid=5;//中间的页数
var maxPageNum=10;//显示的总页数
var totalPage = 0;

var rowsNum = 10;//一页展示多少条数据

//var searchlose = 0;//加载时判断是否获查询条件加载后台数据



/*以字符串的形式传入方法名 最后一页*/
function nextDouble(method) {
		nowpage=totalPage;
		window[method](nowpage, rowsNum); 
}
/*以字符串的形式传入方法名 */
function next(method) {
	if(nowpage<totalPage){
		nowpage=nowpage+1;
		window[method](nowpage, rowsNum); 
	}
}

function getPage() {
	var num;
	pageObj=null;
	//var pageObj = parent.parent.pageQuene.get(location.href);
	if(pageObj != null){
		num = pageObj.split(",")[0];
		rowsNum = Number(pageObj.split(",")[1]);
		return Number(num);
	}else
		return null;
}
function closeWindow()
{
	if(parent.parent.pageQuene.containsKey(location.href))
	{
		parent.parent.pageQuene.remove(location.href)
	}
	history.back();
}
//第一页
function previousLeft(method) {
	nowpage=1;
	window[method](nowpage, rowsNum); 
}
//上一页
function previous(method) {
	if(nowpage>1){
		nowpage=nowpage-1;
		window[method](nowpage, rowsNum); 
	}
}
//跳转
function gotoPage(method,pageInputID) {
	var topageNum = parseInt($("#"+pageInputID).val());
	if(!isNaN(topageNum) && topageNum<=totalPage)
	{
	  nowpage=topageNum;
	  $("#"+pageInputID).val('')
	}
	
	if(topageNum>0 && topageNum<=totalPage){
		window[method](nowpage, rowsNum); 
	}
}

//点击事件
function ym_click(id) {
	nowpage=id;
	var p = document.getElementById("ym" + id);
	for ( var u = 1; u < 9; u++) {
		$("#ym" + u).removeAttr("style");
	}
    $("#pageNum").val("");
	loadContentInfo(id, rowsNum);
}

/*增加分页栏
obj:页面div对象
method：点击页码触发的函数名
pageNum：页数
totalPage：总页数
id:页码的id
*/
function appendPage(obj,method,pageNum,totalPage,id,rows)
{
	var fartherDiv = $(obj).parent().parent().parent();
	var showpagenoticeDiv = $(($(fartherDiv).children("div"))[0]).children("div");
	
	$(showpagenoticeDiv).html('');
	$(showpagenoticeDiv).append("总页数："+totalPage+" | 总行数："+rows);
	
    obj.html("");
    
    if(totalPage>10){
    	obj.append('<li class="prev" id="prev_left'+id+'li"><a href="javascript:void(0)" onclick="previousLeft(\'loadContentInfo\')"><i class="fa fa-angle-double-left"></i></a></li>') ;
	}
	obj.append('<li class="prev" id="prev'+id+'li"><a href="javascript:void(0)" onclick="previous(\'loadContentInfo\')"><i class="fa fa-angle-left"></i></a></li>') ;
	
	
	if(totalPage<=maxPageNum){//总页数小于最大页数的时候，总是显示所有的页数
	   	for (var u = 1; u<=totalPage; u++) 
		{ 
		   obj.append('<li class="" id="'+id+u+'"><a href="javascript:void(0)" onclick="'+method+'('+u+')"'+'>'+u+'</a></li>');
		} 
	}
	else 
	{
		 if(pageNum>maxPageMid)//当前页大于中间的页数
		 {
		    if(pageNum<=totalPage-10){//当前页左右各显示两页
				for (var u = pageNum-5; u <=pageNum+4 && u<=totalPage; u++) 
				{ 
					 obj.append('<li class="" id="'+id+u+'"><a href="javascript:void(0)" onclick="'+method+'('+u+')"'+'>'+u+'</a></li>');
				} 
				//obj.append('<li class="" id="'+id+(pageNum+5)+'"><a href="javascript:void(0)" onclick="'+method+'('+(pageNum+5)+')"'+'>'+(pageNum+5)+'</a></li>');
			}
			else//显示最后5页
			{
			    for (var u = totalPage-9; u <=totalPage; u++) 
				{ 
			    	 obj.append('<li class="" class="active" id="'+id+u+'"><a href="javascript:void(0)" onclick="'+method+'('+u+')"'+'>'+u+'</a></li>');
				} 
			}
		 }
		 else{//显示开始5页
		   	for (var u = 1; u <=maxPageNum; u++) 
			{ 
			   obj.append('<li class="" id="'+id+u+'"><a href="javascript:void(0)" onclick="'+method+'('+u+')"'+'>'+u+'</a></li>');
			} 
		}
	}

       //给选中的页码增加样式
	obj.append('<li class="next" id="next'+id+'li"><a href="javascript:void(0)" onclick="next(\'loadContentInfo\')"><i class="fa fa-angle-right"></i></a></li>') ;
	if(totalPage>10){
		obj.append('<li class="next" id="next_double'+id+'li"><a href="javascript:void(0)" onclick="nextDouble(\'loadContentInfo\')"><i class="fa fa-angle-double-right"></i></a></li>') ;
	}	
	$("#"+(id+pageNum)).attr("class","active");
	if(pageNum==1){
		$("#prev"+id+"li").attr("class","prev disabled");
		if(totalPage>10){
			$("#prev_left"+id+"li").attr("class","prev disabled");
		}
	}else if(pageNum==totalPage || totalPage==1){
		$("#next"+id+"li").attr("class","next disabled");
		if(totalPage>10){
			$("#next_double"+id+"li").attr("class","next disabled");
		}
	}
	try{
		if(parent.parent.pageQuene.containsKey(location.href))
		{
			parent.parent.pageQuene.remove(location.href);
		}
		parent.parent.pageQuene.put(location.href,(pageNum+","+rowsNum));
	}
	catch (e) {
	
	}
}

/*****************分页栏公共函数***************/

/*****************查询模块公共函数**************/
var searchMapping='';
$(function() {
     $("#btnsearch").bind("click",search); 
     $("#search").bind('keyup', function(event){
         if (event.keyCode == "13") {
             if (typeof($('#btnsearch')) != "undefined")
             {
            	  $('#btnsearch').click();
             }
             
             if (typeof($('#gzpzsearchbtn')) != "undefined")
             {
            	 $('#gzpzsearchbtn').click();
             }
         }
     });
     
 });
 
function lose(mes) {
    if ($.trim($("#serach").attr("value")) == "") {//去空格
	     $("#serach").css('color','#e4e4e4');
	     $("#serach").val(mes);
	     searchlose=1;
    }
}
    
function search(){
	nowpage = 1;
	searchMapping=$.trim($("#search").val());
	loadContentInfo(1,  rowsNum);//数据展示
}
 /***************查询模块公共函数**************/
    