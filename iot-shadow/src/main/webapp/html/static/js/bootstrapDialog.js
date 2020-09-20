$(function()
{
	css_onload(prefix_url+'/static/css/bootstrapDialog.css');
})
function css_onload(url)
{
	var head=document.getElementsByTagName("head");
	var es=document.createElement("link");
	es.href=url;
	es.rel="stylesheet";
	es.type="text/css";
	head[0].appendChild(es);
}

function showSuccessMessage(message,falg){
	if(null == message)
	{
		message='操作成功!';
	}
	showMessages(message,"success",falg);
}
function showErrorMessage(message,falg){
	if(null == message)
	{
		message='操作失败!';
	}
	showMessages(message,"error",falg);
}

function showWarnMessage(message,falg){
	if(null == message)
	{
		message='提醒您!';
	}
	showMessages(message,"warn",falg);
}
function showInfoMessage(message,falg){
	if(null == message)
	{
		message='提醒您!';
	}
	showMessages(message,"info",falg);
}

function showMessages(message,type,falg){
	
	var title='<span style="font-size: 1.17em;">温馨提示</span>';
	var info = '<div style="text-align: center;"><div style="width:400px;"><div style="width:100px; float:left;"><i id="dialogType" class="icon_msg '+type+' "></i></div>'
		+'<div style="width:300px;text-align: left; "><span class="bigger-130">'+message+'</span></div></div></div>';
	bootbox.dialog({
		title:title,
		message: info,
		buttons: 			
		{
			"success" :
			{
			   "label" : "确定",
			   "className" : "btn-sm btn-primary"
			   
			}
		}
	}).find("div.modal-dialog").attr("id","msgDialogId");
	$("#msgDialogId").find("div.modal-content").addClass("confirmWidth");
	$("#msgDialogId").find("h4.modal-title").remove();
	$("#msgDialogId").find("div.modal-header").append(title);
	$("#msgDialogId").parent().removeClass("bootbox modal fade in").addClass("dialog_wrps").css("z-index","99999");
	if (falg == null )
	{
		$(".dialog_wrps").css("top",window.screen.height/3+$(window.parent.document).scrollTop());
	}
}

var _shconfirm = {};
function showConfirm(message,callback,falg)
{
	if (message == null || message =='')
	{
		message = '您真的确定要保存吗？';
	}
	_shconfirm.shconfirmCallBack = callback;
	  
	var title = '<span style="font-size: 1.17em;">温馨提示</span>';
	var info = '<div style="text-align: center;"><div style="width:400px;"><div style="width:100px; float:left;"><i id="dialogType" class="icon_msg info "></i></div>'
	+'<div style="width:300px;text-align: left; "><span class="bigger-130">'+message+'</span></div></div></div>';
    bootbox.confirm({
		title:title,
		message: info, 
		buttons: {
		  confirm: {
			 label: "确认",
			 className: "btn-primary btn-sm "
			
		  },
		  cancel: {
			 label: "取消",
			 className: "btn-sm",
		  }
		}, 
		callback: function(result) { 
			if (result){
				 _shconfirm.shconfirmCallBack(true);
			}else{
				 _shconfirm.shconfirmCallBack(false);
			}
			
		}
	  }).find("div.modal-dialog").attr("id","msgConfirmId");
	$("#msgConfirmId").find("div.modal-content").addClass("confirmWidth");
	$("#msgConfirmId").find("h4.modal-title").remove();
	$("#msgConfirmId").find("div.modal-header").append(title);
	$("#msgConfirmId").parent().removeClass("bootbox modal fade in").addClass("dialog_wrps");
	if (falg == null)
	{
		$(".dialog_wrps").css("top",window.screen.height/3+$(window.parent.document).scrollTop());
	}
	

	
}
// 例子
function ShConfirm() {
	showConfirm("确定要这么做吗！", function (result) {
        if (result) {
        	// 确定要执行的方法
            alert("点击了确定");
        } else {
            alert("点击了取消");
        }
    });
	alert(parent.parent.$("body").attr("id"))
}