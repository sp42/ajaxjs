<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<!DOCTYPE html>
<html>
	<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
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
			
			.admin-entry-form > form div.label {
				min-width: 140px;
			}
			.admin-entry-form > form input {
				width: 300px!important;
			}
		</style>
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
				<div class="admin-entry-form">
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
		<script>
			Date.prototype.format = function (fmt) {
			    var o = {
			        "M+": this.getMonth() + 1, //月份
			        "d+": this.getDate(), //日
			        "h+": this.getHours(), //小时
			        "m+": this.getMinutes(), //分
			        "s+": this.getSeconds(), //秒
			        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
			        "S": this.getMilliseconds() //毫秒
			    };
			    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
			    for (var k in o)
			        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
			    return fmt;
			}
		
			new Vue({
				el: aj('.main table'),
				data: {
					arr:[]
				},
				mounted() {
					aj.xhr.get('getTimeTrendRpt/', json => {
						this.arr = json.body.data[0].result.items[1];
					});
				}
			});
			
			aj.xhr.get('getCommonTrackRpt/', json => {
				var data = json.body.data[0].result;
				
				for(var i in data) {				
					new Vue({
						el: aj('.' + i),
						data: { arr: data[i].items}
					});
				}
			});
			
			var now = new Date(), last7days = new Date(), yesterday = new Date();
			last7days.setDate(now.getDate() - 8);
			yesterday.setDate(now.getDate() - 1);
			
			new Vue({
				el: '.chart',
				data: {
					startDate: last7days.format("yyyy-MM-dd"),
					endDate: yesterday.format("yyyy-MM-dd"),
					values: [{ name: 'Page A', uv: 4000, pv: 2400, amt: 2400 }]
				},
				mounted(){ 
					this.query();
				},
				methods: {
					query() {
						var startDate = this.$children[0].date, endDate = this.$children[1].date;
						aj.xhr.get('getTrend', json => {
							// 转换格式
							var arr = json.body.data[0].result.items, days = arr[0].reverse(), value = arr[1].reverse();
							var newArr = [];
							
							for(var i = 0, j = days.length; i < j; i++)
								newArr.push({
									name: days[i], pv: value[i][0], uv: value[i][1]
								});
							
							this.values = newArr;
						}, {
							start_date: startDate,
							end_date: endDate
						});
					}
				}
			});
			
			settings = new Vue({
				el: '.settings',
				mounted(){
					ajaxjs.xhr.form(this.$el.$('form'));
				}
			});
			function showSetting(){
				settings.$children[0].show();
			}
		</script>
	</body>
</html>