<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="c" 		uri="/ajaxjs" %>
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="adminUI"  tagdir="/WEB-INF/tags/common/admin"%>
<adminUI:edit type="main">
	<div class="row">
		<div>
			
		</div>
	</div>
	<div class="row">
		<div>
			<label>
				所属门户：<select name="portalId">
					<c:foreach items="${all_portals}" var="current">
						<c:choose>
							<c:when test="${info.portalId == current.id}">
								<option value="${current.id}" selected>${current.name}</option>
							</c:when>
							<c:otherwise>
								<option value="${current.id}">${current.name}</option>
							</c:otherwise>
						</c:choose>
					</c:foreach>
					</select>
			</label>
			<label>
				内容分类：
				<select name="catalog">
					<c:foreach items="${catalogs}" var="current">
						<c:choose>
							<c:when test="${info.catalog == current.id}">
								<option value="${current.id}" selected>${current.name}</option>
							</c:when>
							<c:otherwise>
								<option value="${current.id}">${current.name}</option>
							</c:otherwise>
						</c:choose>
					</c:foreach>
				</select>
			</label>
		</div>
	</div>
	<div class="row">
		<label>
			抓取分类：
			<select name="crawlerType">
				<option value="0" ${info.crawlerType == 0 ? 'selected' : ''}>视频</option>
				<option value="1" ${info.crawlerType == 1 ? 'selected' : ''}>音频</option>
				<option value="2" ${info.crawlerType == 2 ? 'selected' : ''}>文章</option>
			</select>
		</label>
		<label>
			爬虫 id：&nbsp;&nbsp;
			<select name="crawlerId">
				<option value="1" ${info.crawlerId == 1 ? 'selected' : ''}>荔枝 fm：1</option>
				<option value="2" ${info.crawlerId == 2 ? 'selected' : ''}>喜马拉雅fm：2</option>
				<option value="3" ${info.crawlerId == 3 ? 'selected' : ''}>蜻蜓 fm：3</option>
				<option value="4" ${info.crawlerId == 4 ? 'selected' : ''}>考拉 fm：4</option>
				<option value="5" ${info.crawlerId == 5 ? 'selected' : ''}>网易电台 ：5</option>
				<option value="6" ${info.crawlerId == 6 ? 'selected' : ''}>荔枝台 ：6</option>
			</select>
		</label>
		<label>
			 <button class="my-btn-3" onclick="updateAlbumInfo(${info.id}, this);return false;"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/updateStatus.gif" />获取${uiName}信息</button>
		</label>
		覆盖现有内容？
		<label>
			<input type="radio" name="isOverride" value="true" checked  /> 是 
		</label>
		<label>
			<input type="radio" name="isOverride" value="false" />否
		</label>
	</div>

	<div class="row">
		<div>
			<label>
				上次更新：
				<input value="${viewUtils.formatDateShorter(info.lastUpdate)}" name="lastUpdate" type="text" class="my-inputField" size="27" />
			</label>
			<label>
				&nbsp;上次更新记录标题：
				<input value="${info.lastUpdateRecordName}" name="lastUpdateRecordName" type="text" class="my-inputField" size="40" />
				
			</label>
		</div>
	</div>
	<div class="row">
		<label>
			集数总计：<input value="${info.length == 0 ? '' : info.length}" name="length" type="text" class="my-inputField" size="4" style="margin-left: 0px;" />
		</label>
		<label>
			&nbsp;&nbsp;播放量：<input value="${info.playHit == 0 ? '' : info.playHit}" name="playHit" type="text" class="my-inputField" size="5" />
		</label>
		<label>
			&nbsp;&nbsp;订阅量：<input value="${info.subscriptionAmount}" name="subscriptionAmount" type="text" class="my-inputField" size="5" />
		</label>
		<label>
			&nbsp;&nbsp;标签：<input value="${info.tags}" name="tags" type="text" class="my-inputField" size="40" />
		</label>
	</div>
	<div class="row">
		<label>
			 <button class="my-btn-3" onclick="getAlbumList(${info.id}, this);return false;" style="margin:0;margin-left: 78px;"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/add.gif" />手工添加抓取</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			 <button class="my-btn-3" onclick=";return false;" style="margin:0;">获取更新</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			 <span style="font-size:.9rem;color:gray;">更新信息是采集记录与本地记录之间能够同步的重要依据。</span> 
		</label>
		<label><span class="msgReport"></span></label>
	</div>
	<div class="row brief">
		<div>
			<label>${uiName}简介：
				<textarea rows="20" cols="20" class="my-inputField" name="intro" style="width:65%;">${info.intro}</textarea>
			 </label>
		</div>
	</div>
	
	
	<div class="row">
		<div>
			<table>
				<Tr>
					<td valign="top">封面图： 
					<a href="#"><img width="200" name="cover" src="${info.cover}" /></a>
					<br />
					<br />
					<input type="text" class="my-inputField" name="cover" value="${info.cover}" size="60" />
					</td>
					<td width="20"></td>
					<td valign="top">实时截图：<a href="#"><img
						src="../../../images/screencut.gif" width="150" height="200" /></a>
					</td>
					<td width="20"></td>
					<td valign="top">
					</td>
				</Tr>
			</table>
		</div>
	</div>
 
	<script>
		var url = 'http://192.168.1.141:3000/';
		var msgReport = document.querySelector('.msgReport');

		function updateAlbumInfo(id, btnEl) {
			if(!id) {
				alert('未创建专辑');
			}else {
				
				msgReport.innerHTML = ' :查询中……';
				var agrs = {
					id : id,
					action : 'updateAlbumInfo',
					url : encodeURIComponent(document.querySelector('*[name=url]').value),
					crawlerId : document.querySelector('*[name=crawlerId]').value
				};
				xhr.get(url, agrs, function(json){
					if(json.isOk){
						msgReport.innerHTML = '查询成功！';
						dataBinding(json.data);
					}else{
						msgReport.innerHTML = '查询失败！';
					}
					console.log(json);
				});
			}
		}	
	
		function dataBinding(json){
			var formEl = document.querySelector('form');
			var isOverride = document.querySelector('input[name=isOverride]:checked').value == 'true';
			//alert(isOverride)
			for(var i in json) {
				var el = formEl.querySelector('*[name= ' + i + ']');
				if (el){
					if('value' in el) {
						if(!el.value || isOverride){
							el.value = json[i];
						}
					}else if('src' in el){//debugger
						// img.src 即使为空也有默认值！！
						var input = formEl.querySelector('input[name= ' + i + ']');
						if(!input.value || isOverride) {
							el.src = json[i];
							// 再搜一次 input的
							input.value = json[i];
						}
					}else if('innerHTML' in el){
						el.innerHTML = json[i];
					}
				}
					
			}
		}
		
		function getAlbumList(id) {
			var crawlerId = document.querySelector('*[name=crawlerId]').value;
			if(!crawlerId) {
				alert('没有爬虫 id');
			}else {
				var agrs = {
					id : id,
					name : 		document.querySelector('*[name=name]').value,
					action : 'getAlbumList',
					url : encodeURIComponent(document.querySelector('*[name=url]').value),
					portalId : 	document.querySelector('*[name=portalId]').value,
					albumId : 	id,
					catalog : 	document.querySelector('*[name=catalog]').value,
					crawlerId : document.querySelector('*[name=crawlerId]').value
				};
				xhr.get(url, agrs, function(json) {
					if(json.isOk){
						alert('抓取成功！\n' + json.msg);
					}else{
						alert('抓取失败！' );
					}
					console.log(json);
				});
			}
		}
	</script>
	<style>
		.label{
			display:inline-block;
			width:8%;
		}
	</style>
</adminUI:edit>