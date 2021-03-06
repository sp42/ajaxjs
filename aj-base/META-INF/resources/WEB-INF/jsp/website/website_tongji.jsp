<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
		<style>
			body{
				padding:2%;
			}
			.main table {
				width: 900px;
			}
			.main table td{
				border-right: 1px solid lightgray;
				text-align:right;
				padding:2% 3%;
			}
			.box{
				padding:1%;
				background-color:#f9f9f9;
				border: 1px solid lightgray;
				box-sizing: border-box;
				border-top-color: white;
			}
			.box h3{
				margin:1% 0;
			}
			.link{
				float: right;
			}
			.t-1 {
				width:100%;
			}
			.t-1 td{
				text-align:center;
				padding:5px;
			}
			.chart {
				height:780px;
			}
			
			.aj-form-row-holder > form div.label {
				min-width: 140px;
			}
			.aj-form-row-holder > form input {
				width: 300px!important;
			}
		</style>
		
		<!-- Admin 公共前端资源 -->
		<link rel="stylesheet" href="${aj_static_resource}/dist/css/admin/admin.css" />
		<script src="${aj_static_resource}/dist/admin/admin.js"></script>
		<script src=//unpkg.com/laue></script>
	</head>
	<body>
		<div class="box main" style="border-top-color: lightgray;">
			<div class="link">
				<a href="https://tongji.baidu.com" target="_blank">访问百度统计网站99</a> | <a href="javascript:showSetting();" >统计参数设定</a>
			</div>
			<h3>流量概要</h3>		
			<table>
			  <tr>
			    <th></th><th>浏览量(PV)</th><th>访客数(UV)</th><th>IP数</th><th>跳出率</th><th>平均访问时长</th>	
			  </tr>
			  <tr v-for="(item, i) in arr">
			    <td>{{ i==1 ? '今天' : ''}}{{ i==0 ? '昨天' : ''}}</td><td>{{item[0]}}</td><td>{{item[1]}}</td><td>{{item[2]}}</td>
			    <td>{{item[3]}}%</td><td>{{Math.round(item[4]/60)}}分</td>
			  </tr>
			</table>
		</div> 
		
		<div class="box chart" style="float:left; width: 70%;border-right-color: white;">
			<h3>趋势分析</h3>		
			起始时间：<aj-form-calendar-input field-name="startDate" :field-value="startDate" :date-only="true" :position-fixed="true"></aj-form-calendar-input>
			截至时间：<aj-form-calendar-input field-name="endDate" :field-value="endDate" :date-only="true" :position-fixed="true"></aj-form-calendar-input>
			<button class="aj-btn" @click="query()">查询</button>
			<br />
			<br />
			<div style="background-color:white;">
			  <la-cartesian :bound="[0, n => n + 100]" :data="values" :width="960" :height="500">
			    <la-line dot curve prop="pv"></la-line>
			    <la-line dot curve prop="uv"></la-line>
			    <la-line dot curve prop="amt"></la-line>
			    <la-x-axis prop="name"></la-x-axis>
			    <la-y-axis></la-y-axis>
			    <la-tooltip></la-tooltip>
			  </la-cartesian>
			</div>
			<div style="text-align: center;margin:5%;">
				<span style="background-color:#6be6c1;color:white;padding:5px 10px">PV</span>
				<span style="background-color:#3fb1e3;color:white;padding:5px 10px">UV</span>
			</div>
		</div>
		
		<div class="box word" style="float:left; width: 30%;">
			<h3>Top10搜索词</h3>	

			<table class="t-1">
			  <tr>
			    <th style="text-align:left;">搜索词</th><th>浏览量(PV)</th><th>占比</th>
			  </tr>
			  <tr v-for="(item, i) in arr">
			    <td style="text-align:left;">{{item[0]}}</td><td>{{item[1]}}</td><td>{{item[2]}}%</td>
			  </tr>
			</table>
		</div>
		
		<div class="box sourceSite" style="float:right; width: 30%;">
			<h3>Top10来源网站</h3>	

			<table class="t-1">
			  <tr>
			    <th style="text-align:left;">来源网站</th><th>浏览量(PV)</th><th>占比</th>
			  </tr>
			  <tr v-for="(item, i) in arr">
			    <td style="text-align:left;">{{item[0]}}</td><td>{{item[1]}}</td><td>{{item[2]}}%</td>
			  </tr>
			</table>
		</div>
		
		<div style="clear:both;"></div>
		
		<div class="box landingPage" style="float:left; width: 50%;border-right-color: white;">
			<h3>Top10入口页面</h3>	

			<table class="t-1">
			  <tr>
			    <th style="text-align:left;">入口页面</th><th>浏览量(PV)</th><th>占比</th>
			  </tr>
			  <tr v-for="(item, i) in arr">
			    <td style="text-align:left;"><a target="_blank" :href="item[0]">{{item[0]}}</a></td><td>{{item[1]}}</td><td>{{item[2]}}%</td>
			  </tr>
			</table>
		</div>
		
		<div class="box visitPage" style="float:left; width: 50%;">
			<h3>Top10受访页面</h3>	

			<table class="t-1">
			  <tr>
			    <th style="text-align:left;">受访页面</th><th>浏览量(PV)</th><th>占比</th>
			  </tr>
			  <tr v-for="(item, i) in arr">
			    <td style="text-align:left;"><a target="_blank" :href="item[0]">{{item[0]}}</a></td><td>{{item[1]}}</td><td>{{item[2]}}%</td>
			  </tr>
			</table>
		</div>
		
		
		<div class="settings">
			<aj-layer>
				<div class="aj-form-row-holder">
					<form method="POST" action="${ctx}/admin/config/site/" class="configForm" style="width:500px;">
						<div>
							<label>
								<div class="label">站点 id</div> 
								<input type="text" name="baidu_tongji.siteId" value="${aj_allConfig.baidu_tongji.siteId}" />
							</label> 
						</div>
						<div>
							<label>
								<div class="label">百度统计账号名</div> 
								<input type="text" name="baidu_tongji.api_username" value="${aj_allConfig.baidu_tongji.api_username}" />
							</label> 
						</div>
						<div>
							<label>
								<div class="label">百度统计账号密码</div> 
								<input type="text" name="baidu_tongji.api_password" value="${aj_allConfig.baidu_tongji.api_password}" />
							</label> 
						</div>
						<div>
							<label>
								<div class="label">API 密钥</div> 
								<input type="text" name="baidu_tongji.api_token" value="${aj_allConfig.baidu_tongji.api_token}" />
							</label> 
						</div>
						<div>
							<label>
								<div class="label">脚本 id</div> 
								<input type="text" name="baidu_tongji.api_token" value="${aj_allConfig.baidu_tongji.scriptId}" />
							</label> 
						</div>
				
						<section class="aj-btnsHolder">
							<button class="aj-btn">
								<img src="${commonAssetIcon}/save.gif" /> 修改
							</button>
							<button class="aj-btn" onclick="this.up('form').reset();return false;">复 位</button>
						</section>
					</form>
				</div>
			</aj-layer>
		</div>
		<script src="${aj_static_resource}/dist/admin/Website/tongji.js"></script>
	</body>
</html>