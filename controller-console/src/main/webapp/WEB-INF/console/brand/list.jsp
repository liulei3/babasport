<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>babasport-list</title>
<script type="text/javascript">
	// 批量选中,即为选中的input框设置为选中
	function checkBox(name,checked){
		// 获取那么为指定内容的input标签
		$("input[name='"+ name +"']").attr("checked",checked);
	}
	
	// 批量删除 通过点击事件进行表单提交,所以加上dForm表单
	function optDelete(name,isDisplay,pageNo){
		var checked = $("input[name='ids']:checked").size();
		// 进行提交验证 空选提交进行提示
		if(checked==0){
			alert("别瞎点!!!");
			return;
		}
		
		// 删除进行确认
		if(!confirm("真的不爽一把?")){
			return;
		}
		
		
		// 需要设置提交路径和提交方式 删除时带条件,保证页面不会跳转
		$("#dForm").attr("action","/brand/deletes.do?name="+name+"&isDisplay="+isDisplay+"&pageNo="+pageNo);
		$("#dForm").attr("method","post");
		$("#dForm").submit();
	}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 品牌管理 - 列表</div>
	<form class="ropt">
		<input class="add" type="button" value="添加" onclick="javascript:window.location.href='add.jsp'"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<form action="/brand/list.do" method="post" style="padding-top:5px;">
品牌名称: <input type="text" name="name" value="${name}"/>
	<select name="isDisplay">
		<option value="1" <c:if test="${isDisplay == 1}">selected="selected"</c:if>>是</option>
		<option value="0" <c:if test="${isDisplay == 0}">selected="selected"</c:if>>否</option>
	</select>
	<input type="submit" class="query" value="查询"/>
</form>
<form id="dForm">
	<table cellspacing="1" cellpadding="0" border="0" width="100%" class="pn-ltable">
		<thead class="pn-lthead">
			<tr>
				<th width="20"><input type="checkbox" onclick="checkBox('ids',this.checked)"/></th>
				<th>品牌ID</th>
				<th>品牌名称</th>
				<th>品牌图片</th>
				<th>品牌描述</th>
				<th>排序</th>
				<th>是否可用</th>
				<th>操作选项</th>
			</tr>
		</thead>
		<tbody class="pn-ltbody">
			<c:forEach items="${pagination.list}" var="brand">
				<tr bgcolor="#ffffff" onmouseout="this.bgColor='#ffffff'" onmouseover="this.bgColor='#eeeeee'">
					<td><input type="checkbox" value="${brand.id}" name="ids"/></td>
					<td align="center">${brand.id}</td>
					<td align="center">${brand.name}</td>
					<td align="center"><img width="40" height="40" src="${brand.imgUrl}"/></td>
					<td align="center">${brand.description}</td>
					<td align="center">${brand.sort}</td>
					<td align="center">
						<c:if test="${brand.isDisplay == 1}">是</c:if>
						<c:if test="${brand.isDisplay == 0}">否</c:if>
					</td>
					<td align="center">
					<a class="pn-opt" href="/brand/toEdit.do?id=${brand.id}">修改</a> | <a class="pn-opt" onclick="if(!confirm('您确定删除吗？')) {return false;}" href="#">删除</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</form>
<div class="page pb15">
	<span class="r inb_a page_b">
		<c:forEach items="${pagination.pageView}" var="page">
			${page}
		</c:forEach>
	</span>
</div>
<div style="margin-top:15px;"><input class="del-button" type="button" value="删除" onclick="optDelete('${name}','${isDisplay}','${pagination.pageNo}');"/></div>
</div>
</body>
</html>