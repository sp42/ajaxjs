<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
	</head>
	<body>
		<div class="admin-entry-form">
			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">${isCreate?'新建':'编辑'}${uiName}</template>
				<template slot="btns">
				<c:if test="${!isCreate}">
					<a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | 
				</c:if>
					<a :href="ajResources.ctx + '/admin/${shortName}/list/'">${uiName}列表</a> | 
				</template>
			</aj-admin-header>

			<form action="." method="${isCreate ? 'POST' : 'PUT'}">
			<c:if test="${!isCreate}">
				<input type="hidden" name="id" value="${info.id}" /><!-- 传送 id 参数 -->
			</c:if>
			
				<div>
					<label>
						<div class="label">${uiName}名称：</div> 
						<input placeholder="请填写${uiName}名称" size="20" required="required" name="name" value="${info.name}" type="text" />
						<span style="color:red;">*</span>
					</label> 
					<label>
 						<div class="label">创建日期：</div>  
						<aj-form-calendar-input field-name="createDate" field-value="<c:dateFormatter value="${info.createDate}" />"></aj-form-calendar-input> 
 					</label> 
				</div>
				
				<div>
					<label>
						<div class="label">店长姓名：</div> 
						<input size="10" required="required" name="sellerMaster" value="${info.sellerMaster}" type="text" />
						<span style="color:red;">*</span>
					</label> 
					<label>
						<div class="label">电话：</div> 
						<input size="20" required="required" name="tel" value="${info.tel}" type="text" />
						<span style="color:red;">*</span>
					</label> 
					<label>
						<div class="label">地址：</div> 
						<input size="40" name="address" value="${info.address}" type="text" />
					</label> 
				</div>
				<div>
					<label>
						<div class="label">位置-纬度（latitude）：</div> 
						<input size="20" required="required" name="latitude" value="${info.latitude}" type="text" />
						<span style="color:red;">*</span>
					</label> 
					<label>
						<div class="label">位置-纬度（longitude）：</div> 
						<input size="20" required="required" name="longitude" value="${info.longitude}" type="text" />
						<span style="color:red;">*</span>
					</label> 
					
					<a href="http://www.gpsspg.com/maps.htm">获取经纬度</a>
				</div>
				<div>
					<label>
						<div class="label">商家简介：</div> 
						<textarea rows="5" cols="80" name="content" class="aj-input">${info.content}</textarea>
					</label>
				</div>
				<div>
					<table >
						<tr>
							<td><div class="label" style="float: left;">${uiName} LOGO：</div></td>
							<td>
								<c:choose>
									<c:when test="${isCreate}">
											<span>请保存记录后再上传图片。</span>
									</c:when>
									<c:otherwise>
											<!-- 图片上传 -->
											<aj-xhr-upload action="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catalog=2" 
												:is-img-upload="true" 
												hidden-field="cover" 
												hidden-field-value="${info.cover}" 
												img-place="${empty info.cover ? commonAsset.concat('/images/imgBg.png') : aj_allConfig.uploadFile.imgPerfix.concat(info.cover)}">
											</aj-xhr-upload>
									</c:otherwise>
								</c:choose>		
							</td>	
						</tr>
					</table>
				</div>
				<aj-admin-state :checked="${empty info.stat ? 9 : info.stat}"></aj-admin-state>
				<div>
					<!--按钮 -->
					<aj-admin-info-btns :is-create="${isCreate}"></aj-admin-info-btns>
				</div>
			</form>
		</div>
		<script charset="utf-8" src="https://map.qq.com/api/js?v=2.exp&libraries=convertor&key=GPHBZ-WBV6I-NFKG7-5BZ3O-WBEDH-WRBXV"> </script>
		

		<script>
		
		var json = [
			{
			content: "鸿海",
			cover: null,
			createDate: "2019-03-21 23:31:21",
			extractData: null,
			id: 1,
			latitude: 113.342435,
			longitude: 23.095495,
			name: "鸿海",
			status: null,
			uid: 558432489819865100,
			updateDate: "2019-03-22 00:25:12"
			},
			{
			content: "惠康",
			cover: null,
			createDate: "2019-03-22 00:26:35",
			extractData: null,
			id: 3,
			latitude: 113.728502,
			longitude: 24.25758,
			name: "惠康",
			status: null,
			uid: 558446390154362900,
			updateDate: "2019-03-22 01:01:18"
			},
			{
			content: "新力",
			cover: null,
			createDate: "2019-03-22 01:05:28",
			extractData: null,
			id: 5,
			latitude: 113.641469,
			longitude: 24.144377,
			name: "新力",
			status: null,
			uid: 558456177260757000,
			updateDate: "2019-03-22 01:05:28"
			}
		]
		
		// 如何获取两个经纬度之间的距离
		function getDistance( lat1, lng1, lat2, lng2) {
		    var radLat1 = lat1*Math.PI / 180.0;
		    var radLat2 = lat2*Math.PI / 180.0;
		    var a = radLat1 - radLat2;
		    var  b = lng1*Math.PI / 180.0 - lng2*Math.PI / 180.0;
		    var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
		    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		    s = s *6378.137 ;// EARTH_RADIUS;
		    s = Math.round(s * 10000) / 10000;
		    return s;
		}
		
		function getNear(arr, latitude, longitude) {
			var min = {
				distance : Math.min()
			};
			
			json.forEach(i => {
				i.distance = getDistance(latitude, longitude, i.latitude, i.longitude);
				if(i.distance < min.distance) {
					min = i;
				}
			});
			
			return min;
		}
		
		var latitude = 113.265423, longitude = 23.143608;
		console.log(getNear(json, latitude, longitude));
		
		/*
			// 获取本机经纬度 
			if (navigator.geolocation) {
			   navigator.geolocation.getCurrentPosition(
				(position) => {
				 
					var lat = position.coords.latitude; //纬度
					var lag = position.coords.longitude; //经度
			 
					console.log(position);
					console.log('纬度:'+ lat + ', 77经度:' + lag);
					
					geocoder = new qq.maps.Geocoder({
					    complete:function(result){
					        alert('成功：'+result.detail.address);
					    }
					});
					
 					geocoder.getAddress(new qq.maps.LatLng(lag, lat));
					
				}, 
				(error) => {
					switch(error.code) {
						case error.PERMISSION_DENIED:
						  alert("定位失败,用户拒绝请求地理定位");
						  break;
						case error.POSITION_UNAVAILABLE:
						  alert("定位失败,位置信息是不可用");
						  break;
						case error.TIMEOUT:
						  alert("定位失败,请求获取用户位置超时");
						  break;
						case error.UNKNOWN_ERROR:
						  alert("定位失败,定位系统失效");
						  break;
					}
				});
			} else {
				alert("浏览器不支持地理定位");
			}
		*/
 

			App = new Vue({
				el: '.admin-entry-form',
				data : {
	
				},
				
				mounted(){
					// 表单提交
					aj.xhr.form('.admin-entry-form form', json => {
						if(json && json.msg) {
							aj.alert(json.msg);
							${isCreate} && json && json.isOk && setTimeout(()=>location.assign(json.newlyId + "/"), 2000);
						}
					});
				},
				methods: {

				}
			});		
			
		</script>
		<c:if test="${isCreate}">
			<script>
				window.isCreate = true;
			</script>
		</c:if>
	</body>
</html>