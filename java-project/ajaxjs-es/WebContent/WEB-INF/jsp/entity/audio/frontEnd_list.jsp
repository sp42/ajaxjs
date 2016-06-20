<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="ui"   tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<tags:content isSecondLevel="true">
	<jsp:attribute name="body">
	
	
	<!-- 过滤器 -->
	<style>
		.cityChooser{
			clear:left;
			width:100%;
		}
		.cityChooser td{
			padding: 1%;
		}
		.cityChooser td li {
			padding:0 1%;
			cursor: pointer;
			float:left;
			list-style:none;
		}
		.cityChooser .left{
			background-color:gray;
			width:15%;
			background-color: lightgray;
			width: 10%;
			text-align: center;
		}
	</style>
	<table cellpadding="0" class="cityChooser">
		<tr>
			<td class="left">门户</td>
			<td class="right ">
<ul class="filter">
	<li><a href="?">全部</a></li>
<c:foreach var="current" items="${all_portals_audio}">
	<c:if test="${current.name.indexOf('fm') != -1}">
	<li>
		<a href="javascript:void('portalId_${current.id}');" onclick="goByFilter(this);" data-filterField="portalId" data-filterValue="${current.id}" 
			${param.filterValue == current.id && param.filterField == 'portalId' ? ' style="font-size:1.2rem;font-weight:bold;"' : ''}>
		 ${current.name}(<span style="color:#ca4225;">${current.totalRecordCount}</span>)
		</a>
	</li>
	</c:if>
</c:foreach>
</ul>
			</td>
		</tr>
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

	</table>

	<div style="clear:left;margin-bottom:20px;"></div>

		<div class="filterTitle">类型：</div>
		<ul class="filter">
			<li><a href="?">全部</a></li>		
			<c:foreach var="current" items="${all_catalogs}">
				<c:if test="${current.name.indexOf('fm') == -1}">
				<li>
					<a href="?filterField=catalog&filterValue=${current.id}" 
					${param.filterValue == current.id && param.filterField == 'catalog' ? ' style="font-size:1.2rem;font-weight:bold;"' : ''}>
					 ${current.name}(${current.totalRecordCount })
					</a>
				</li>
				</c:if>
			</c:foreach>
		</ul>
	
	<script src="ChinaCity.js"></script>
	<script src="js.js"></script>
<!-- // 过滤器 -->


		<div class="filterTitle">门户：</div>


	<br />
	<div style="clear:left;margin-bottom:10px;"></div>
	
	<div class="filterTitle">类型：</div>
	<ul class="filter">
		<li><a href="?">全部</a></li>			
	<c:foreach var="current" items="${all_catalogs_audio}">
		<li>
			<a href="javascript:void('catalogId_${current.id}');" onclick="goByFilter(this);" data-filterField="catalog" data-filterValue="${current.id}"
			 ${param.filterValue == current.id && param.filterField == 'catalog' ? ' style="font-size:1.1rem;font-weight:bold;"' : ''}>
			 ${current.name}(${current.totalRecordCount })
			</a>
		</li>
	</c:foreach>
	</ul>
	<div style="clear:left;"></div>
		<!-- 搜索输入框 -->
		<div style="float:right;margin-top:20px;margin-right: 10px;">
			<form>
				<input type="hidden" name="searchField" value="name" />
				<input type="text"   name="searchValue" placeholder="搜索标题" value="${viewUtils.urlChinese(param.searchValue)}" />
				<button> 搜 索 </button>
			</form>
		</div>
		<!-- // 搜索输入框 -->
		<h1>列表</h1>
		<ul class="title">
			<li class="col_id">#ID</li>
			<li class="col_name">名称</li>
			<li class="col_createDate">推出時間|来源</li>
			<li class="col_meta">门户|类型|专辑</li>
			<li class="col_action" style="text-align:center;">操作</li>
		</ul>
		<ul class="list">
			<c:foreach var="current" items="${PageResult.rows}">
				<li>
					<div class="col_id">${current.id}</div>
					<div class="col_name" title="${current.name}">
						<a href="${current.id}">${current.name}</a>
					</div>
					<div class="col_createDate">
						${empty current.publishDate ? 'n/a' : current.publishDate} |
						<a href="${current.ownerUrl}" target="_blank">来源</a>
					</div>
					<div class="col_meta">
						${all_portals_audio_map[current.portalId]['name']} | ${all_catalogs_audio_map[current.catalog]['name']} | 
						<a href="?filterField=albumId&filterValue=${current.albumId}">
						${empty album_map[current.albumId] ? 'n/a' : album_map[current.albumId]['name']}
						</a>
					</div>
					<div class="col_action">
						<button class="b1" onclick="location.assign('../info.jsp?id=${current.id}');">详情</button>
						<button class="b2" onclick="playVideo('${current.sourceUrl}');">播放</button>
						<button class="b3" onmouseover="showCover(arguments[0], this);" onmouseout="hideCover(arguments[0]);" data-img="${current.cover}">封面</button>
					</div>
				</li>
			</c:foreach>
		</ul>
		<div align="center">
		
		<ui:page type="page" pageInfo="${PageResult}" />

		<div class="cover_img">
			<img src="" />
		</div>
		<script>
			var cover_img = document.querySelector('.cover_img');
			function showCover(e, el){
				cover_img.addCls('show');
				if(el.getAttribute('data-img')){
					cover_img.querySelector('img').src = el.getAttribute('data-img');
				}
				cover_img.style.left = e.clientX + 'px';
				cover_img.style.top = e.clientY + 'px';
			}
			
			function hideCover(e){
				cover_img.querySelector('img').src = '';
				cover_img.removeCls('show');
			}
			 
			
			function playAudio(sourceUrl){
				var audio = document.querySelector('audio');
				audio.src = sourceUrl;
				audio.play();
			}
			
			function goByFilter(el) {
				var filterField = el.getAttribute('data-filterField'), filterValue = el.getAttribute('data-filterValue');
				var search = location.search;
				
				if (search.indexOf(filterField) != -1) {
					var arr = search.split('filterField=');
					for(var i = 0, j = arr.length; i < j; i++) {
						if(arr[i].indexOf(filterField) != -1) {
							arr[i] = arr[i].replace(/filterValue=[^&]+/, 'filterValue=' + filterValue);
						}
					}
					search = arr.join('filterField=');
					// search = search.reaplce(new RegExp('filterValue=' + filterField) + '=[^&]+', filterField + '=' + filterValue);
				} else {
					// 没有这个参数，加上！
					if(search.indexOf('?') != -1) {
						search += '&filterField={0}&filterValue={1}'.format(filterField, filterValue);
					} else {
						search = '?filterField={0}&filterValue={1}'.format(filterField, filterValue);
					}
				}
				
				location.assign(search);
				//debugger;
			}
		</script>
		<br />
		<audio src="" controls></audio>
		</div>

	</jsp:attribute>
</tags:content>



