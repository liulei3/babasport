<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>babasport-list</title>
<script type="text/javascript">
	// 三种方法进行修改触发事件
	var flag;
	function updateSku(skuId){
		
		// 方法一 原理:根据唯一id,区别每个input框,再将不可操作改为可操作
		 /* $("#m"+skuId).attr("disabled",false);
		 $("#p"+skuId).attr("disabled",false);
		 $("#s"+skuId).attr("disabled",false);
		 $("#l"+skuId).attr("disabled",false);
		 $("#f"+skuId).attr("disabled",false); */
		
		// 方法二  原理:当鼠标进入可以修改,离开不可以修改
		/*$("#m"+skuId).hover(
		  function () {
		    $(this).attr("disabled",false);
		  },
		  function () {
		    $(this).attr("disabled",true);
		  }
		); */
		
		// 方法三 原理:通过赋值旗标,触发不同事件
		 if(flag!=null){
			 $("#m"+flag).attr("disabled",true);
			 $("#p"+flag).attr("disabled",true);
			 $("#s"+flag).attr("disabled",true);
			 $("#l"+flag).attr("disabled",true);
			 $("#f"+flag).attr("disabled",true);
		 }
		 flag=skuId;
		 $("#m"+skuId).attr("disabled",false);
		 $("#p"+skuId).attr("disabled",false);
		 $("#s"+skuId).attr("disabled",false);
		 $("#l"+skuId).attr("disabled",false);
		 $("#f"+skuId).attr("disabled",false);
		
	}
	// 保证属性
	function addSku(skuId){
		var m = $("#m"+skuId).attr("disabled",true).val();
		var p = $("#p"+skuId).attr("disabled",true).val();
		var s = $("#s"+skuId).attr("disabled",true).val();
		var l = $("#l"+skuId).attr("disabled",true).val();
		var f = $("#f"+skuId).attr("disabled",true).val();
		
		/* var url = "/sku/addSku.do";
		// json k-v格式
		var params = {"marketPrice":m,"price":p,"stock":s,"upperLimit":l,"deliveFee":f,"id":skuId};
		$.post(url,params,function(data){
			// 保存后的提示信息
			alert(data.message);
		},"json"); */
		
		// json 字符串,因为是字符串,所以需要通过ajax进行请求 
		var params = '{"marketPrice":'+m+',"price":'+p+',"stock":'+s+',"upperLimit":'+l+',"deliveFee":'+f+',"id":'+skuId+'}';
		// 发生json格式数据,需要后台通过@RequestParams注解接收
		$.ajax({
			data : params,
			dataType:"json",
			contentType : "application/json;charset=utf-8",
			type : "post",
			url : "/sku/addSku.do",
			success : function(data){
				alert(data.message);
			}
		});
		
	}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 库存管理 - 列表</div>
	<div class="clear"></div>
</div>
<div class="body-box">
<form method="post" id="tableForm">
<table cellspacing="1" cellpadding="0" border="0" width="100%" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="Pn.checkbox('ids',this.checked)"/></th>
			<th>商品编号</th>
			<th>商品颜色</th>
			<th>商品尺码</th>
			<th>市场价格</th>
			<th>销售价格</th>
			<th>库       存</th>
			<th>购买限制</th>
			<th>运       费</th>
			<th>是否赠品</th>
			<th>操       作</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
		<c:forEach items="${skus}" var="sku">
			<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
				<td><input type="checkbox" name="ids" value="${sku.id}"/></td>
				<td>${sku.productId}</td>
				<td align="center">${sku.color.name}</td>
				<td align="center">${sku.size}</td>
				<td align="center"><input type="text" id="m${sku.id}" value="${sku.marketPrice}" disabled="disabled" size="10"/></td>
				<td align="center"><input type="text" id="p${sku.id}" value="${sku.price}" disabled="disabled" size="10"/></td>
				<td align="center"><input type="text" id="s${sku.id}" value="${sku.stock}" disabled="disabled" size="10"/></td>
				<td align="center"><input type="text" id="l${sku.id}" value="${sku.upperLimit}" disabled="disabled" size="10"/></td>
				<td align="center"><input type="text" id="f${sku.id}" value="${sku.deliveFee}" disabled="disabled" size="10"/></td>
				<td align="center">不是</td>
				<td align="center"><a href="javascript:updateSku('${sku.id}')" class="pn-opt">修改</a> | <a href="javascript:addSku('${sku.id}')" class="pn-opt">保存</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</form>
</div>
</body>
</html>