<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="c" 		uri="/ajaxjs" %>
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="adminUI"  tagdir="/WEB-INF/tags/common/admin"%>
<adminUI:edit type="main">
		<div class="row">
			<div>
				<label>
					${uiName}名称：
					<input placeholder="请填写${uiName}名称" size="40" required="required" name="name" value="${info.name}" type="text" class="my-inputField" />
				</label>
				<label>
					${uiName}地址（<a href="${info.url}" target="_blank">打开</a>）：
						<input placeholder="请填写${uiName}名称" size="50" required="required" name="url" value="${info.url}" type="text" class="my-inputField" />
				</label>
			</div>
		</div>
		<div class="row">
			<div>
				<label>
					所属门户：<select name="portalId">
						<c:foreach items="${portals}" var="current">
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
				<label>
					抓取分类：
					<select name="crawlerType">
						<option value="0" ${info.crawlerType == 0 ? 'selected' : ''}>视频</option>
						<option value="1" ${info.crawlerType == 1 ? 'selected' : ''}>音频</option>
						<option value="2" ${info.crawlerType == 2 ? 'selected' : ''}>文章</option>
					</select>
				</label>
			</div>
		</div>
		<div class="row">
			<div>
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
					 <button class="my-btn-3" onclick="updateAlbumInfo(${info.id}, this);return false;"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/updateStatus.gif" /> 立刻更新${uiName}信息 </button>
				</label>
				<label>
					 <button class="my-btn-3" onclick="getAlbumList(${info.id}, this);return false;"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/add.gif" /> 手工添加抓取</button>
				</label>
				<label><span class="msgReport"></span></label>
			</div>
		</div>
		<div class="row">
			<div>
				<label>
					更新日期：
					<input value="${viewUtils.formatDateShorter(info.lastUpdate)}" name="lastUpdate" type="text" class="my-inputField" size="20" />
					 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</label>
				<label>
					集数：<input value="${info.length == 0 ? '' : info.length}" name="length" type="text" class="my-inputField" size="10" />
				</label>
				是否覆盖现在内容？
				<label>
					<input type="radio" name="isOverride" value="true" /> 是 
				</label>
				<label>
					<input type="radio" name="isOverride" value="false" checked />否
				</label>

			</div>
		</div>
		
		<div class="row brief">
			<div>
				<label>${uiName}简介：
					<textarea rows="20" cols="20" class="my-inputField" name="intro" style="width:65%;">${info.intro}</textarea>
				 </label>
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