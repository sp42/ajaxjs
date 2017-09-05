<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "省市区三级选择器");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>

	<style>
.cityChooser table .left {
	width: 135px;
	background-color: #eaeaea;
	color: #424242;
	text-align: center;
	letter-spacing: 5px;
	min-height: 30px;
	border-bottom: 1px dotted #d3cece;
}

.cityChooser table td {
	font-size: .8 .rem;
	padding: 2px 10px;
}

.cityChooser table span:hover {
	color: red;
}

.cityChooser table span {
	padding: 0 2px;
	display: inline-block;
	letter-spacing: 2px;
	line-height: 20px;
	text-align: center;
	cursor: pointer;
}

.cityChooser table .right {
	color: #f27b03;
	font-size: 9pt;
}
</style>

	<h4>省市区三级选择器</h4>
	<p>实质是一种三级联动机制。注意当前只提供 PC 版。</p>

	<div class="cityChooser body" style="margin: 0 auto; width: 800px;">
		<table cellpadding="0">
			<tr>
				<td class="left">去往省市</td>
				<td class="right provinceSel"></td>
			</tr>
			<tr>
				<td class="left">去往城市</td>
				<td class="right areaSel"></td>
			</tr>
			<tr>
				<td class="left">去往县区</td>
				<td class="right countySel"></td>
			</tr>
			<tr>
				<td class="left">货物类型</td>
				<td class="right filter filter_goodsType"><span class="noLimit">不限</span><span>货</span><span>轻货</span><span>重货</span>
				</td>
			</tr>
			<tr>
				<td class="left">运输方式</td>
				<td class="right filter filter_trafficType"><span
					class="noLimit">不限</span><span>物流公司</span><span>整车配货</span><span>零担配货</span>
				</td>
			</tr>
		</table>
	</div>
	<script
		src="${pageContext.request.contextPath}/asset/common/js/component/ChinaCity.js"></script>
	<script>
		var panel = Object.create(ChinaCity), table = document
				.querySelector('.cityChooser table');
		//adapter.levelCount = panel.levelCount;
		//panel.onItemClick = adapter.afterSelect.bind(adapter).after(function(currentValue, levelIndex, lastSelectId) {
		//// 接口查询
		//setUrl('destinationID', lastSelectId);
		//});
		// 撤销事件
		panel.onGotoParent = function(arr, levelIndex) {
			//if(levelIndex == 0){
			//	 		removeHash('destinationID');
			//}else{
			//	 		var parentValue = this.selectData[levelIndex - 1];
			//	 		var parentData  = adapter.afterSelect(parentValue, levelIndex - 1);
			//
			//	 		setUrl('destinationID', parentData[2]);
			//}
		}

		panel.selectUIs = [ table.querySelector('.provinceSel'),
				table.querySelector('.areaSel'),
				table.querySelector('.countySel') ];

		//var params = url2json(window.location.hash);
		panel.selectData = [];
		//if (params.toProvince) panel.selectData[0] = params.toProvince;
		//if (params.toArea) panel.selectData[1] = params.toArea;
		//if (params.toCounty) panel.selectData[2] = params.toCounty;
		//panel.selectData = ["广东", "广州市", "海珠区"];
		panel.init();
		// setInterval(function(){
		//	 	 	console.log(panel.selectData);
		// }, 2000);
		//
	</script>

	<!-- 选择省市区控件 -->
	<div class="newForm cityChoser tab2"
		style="margin: 0 auto; width: 300px;">
		<ul>
			<li>省&nbsp;<span class="arrow"></span></li>
			<li>市&nbsp;<span class="arrow"></span></li>
			<li>区县&nbsp;<span class="arrow"></span></li>
		</ul>
		<div class="content">
			<div class="provinceSel">6</div>
			<div class="areaSel">6</div>
			<div class="countySel">67</div>
		</div>
	</div>
	<!-- // 选择省市区控件 -->
	<style>
.cityChoser .content>div {
	overflow: hidden;
	zoom: 1;
	padding: 10px;
	text-align: left;
}

.cityChoser .content>div>span {
	margin: 3px 0;
	padding-left: 3px;
	width: 55px;
	display: block;
	float: left;
	cursor: pointer;
	font-size: 9pt;
	text-align: center;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	letter-spacing: 2px;
}
</style>
	<script>
		var tab = new ajaxjs.SimpleTab(document.querySelector('.tab2'));

		var panel = Object.create(ChinaCity), table = document
				.querySelector('.cityChoser .content');
		panel.showAllChoice = true;
		panel.selectUIs = [ table.querySelector('.provinceSel'),
				table.querySelector('.areaSel'),
				table.querySelector('.countySel') ];

		panel.onItemClick = function() {
			// 切换 tab
			// tab.jump(i);
		}
		panel.init();
	</script>

	<br />



	<%@include file="../public/footer.jsp"%>
</body>
</html>