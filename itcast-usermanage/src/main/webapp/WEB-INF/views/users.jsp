<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员管理系统</title>
<link rel="stylesheet" type="text/css" href="/js/jquery-easyui-1.4/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="/js/jquery-easyui-1.4/themes/icon.css" />
<script type="text/javascript" src="/js/jquery-easyui-1.4/jquery.min.js"></script>
<script type="text/javascript" src="/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
</head>
<body>
	<div>
    <table class="easyui-datagrid" id="userList" title="会员列表" 
	       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/rest/user/list',method:'get',pageSize:5,toolbar:toolbar,pageList:[2,5,10]">
	    <thead>
	        <tr>
	        	<th data-options="field:'ck',checkbox:true"></th>
	        	<th data-options="field:'id',width:60">ID</th>
	            <th data-options="field:'userName',width:200">用户名</th>
	            <th data-options="field:'name',width:100">姓名</th>
	            <th data-options="field:'age',width:100">年龄</th>
	            <th data-options="field:'sex',width:80,align:'right',formatter:formatSet">性别</th>
	            <th data-options="field:'birthday',width:80,align:'right',formatter:formatBirthday">出生日期</th>
	            <th data-options="field:'created',width:130,align:'center',formatter:formatDate">创建日期</th>
	            <th data-options="field:'updated',width:130,align:'center',formatter:formatDate">更新日期</th>
	        </tr>
	    </thead>
	</table>
	</div>
<div id="userAdd" class="easyui-window" title="新增会员" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/rest/page/user-add'" style="width:800px;height:600px;padding:10px;">
        The window content.
</div>
<script type="text/javascript">
function formatDate(val,row){
	var now = new Date(val);
	return now.format("yyyy-MM-dd hh:mm:ss");
}
function formatBirthday(val,row){
	var now = new Date(val);
	return now.format("yyyy-MM-dd");
}
function formatSet(val,row){
	if(val == 1){
		return "男";
	}else if(val == 2){
		return "女";
	}else{
		return "未知";
	}
}
function getSelectionsIds(){
	var userList = $("#userList");
	var sels = userList.datagrid("getSelections");
	var ids = [];
	for(var i in sels){
		ids.push(sels[i].id);
	}
	ids = ids.join(",");
	return ids;
}
var toolbar = [{
    text:'新增',
    iconCls:'icon-add',
    handler:function(){
    	$('#userAdd').window('open');
    }
},{
    text:'编辑',
    iconCls:'icon-edit',
    handler:function(){
    	$.messager.alert('提示','该功能由学员自己实现!');
    }
},{
    text:'删除',
    iconCls:'icon-cancel',
    handler:function(){
    	var ids = getSelectionsIds();
    	if(ids.length == 0){
    		$.messager.alert('提示','未选中用户!');
    		return ;
    	}
    	$.messager.confirm('确认','确定删除ID为 '+ids+' 的会员吗？',function(r){
    	    if (r){
                //提交到后台的RESTful
                $.ajax({
                    type: "POST",
                    url: "/rest/user/delete",
                    data: {'ids':ids},
                    statusCode :{
                        204 : function(){
                            $.messager.alert('提示','删除会员成功!',undefined,function(){
                                $("#userList").datagrid("reload");
                            });
                        },
                        500 : function(){
                            $.messager.alert('提示','删除用户失败!');
                        }
                    }
                });
    	    }
    	});
    }
},'-',{
    text:'导出',
    iconCls:'icon-remove',
    handler:function(){
    	var optins = $("#userList").datagrid("getPager").data("pagination").options;
    	var page = optins.pageNumber;
    	var rows = optins.pageSize;
    	$("<form>").attr({
    		"action":"/user/export/excel",
    		"method":"POST"
    	}).append("<input type='text' name='page' value='"+page+"'/>")
    	.append("<input type='text' name='rows' value='"+rows+"'/>").submit();
    }
}];
</script>
</body>
</html>