<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="ui"   tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<tags:content isSecondLevel="true">
	<jsp:attribute name="body">
<!-- 过滤器 -->
	<div class="filterTitle">门户：</div>
		<ul class="filter">
			<li><a href="?">全部</a></li>
		<c:foreach var="current" items="${all_portals_album}">
			<li>
				<a href="?filterField=portalId&filterValue=${current.id}" 
				${param.filterValue == current.id && param.filterField == 'portalId' ? ' style="font-size:1.2rem;font-weight:bold;"' : ''}>
				 ${current.name}(${current.totalRecordCount })
				</a>
			</li>
		</c:foreach>
		</ul>

	<br />
	<div style="clear:left;margin-bottom:20px;"></div>
	
	<div class="filterTitle">类型：</div>
	<ul class="filter">
		<li><a href="?">全部</a></li>		
		<c:foreach var="current" items="${all_catalogs_album}">
			<li>
				<a href="?filterField=catalog&filterValue=${current.id}" 
				${param.filterValue == current.id && param.filterField == 'catalog' ? ' style="font-size:1.2rem;font-weight:bold;"' : ''}>
				 ${current.name}(${current.totalRecordCount })
				</a>
			</li>
		</c:foreach>
	</ul>
			
	<div style="clear:left;margin-bottom:10px;"></div>
	
	<div class="filterTitle">分类：</div>
	<ul class="filter">
		<li><a href="?">全部</a></li>		
		<c:foreach var="current" items="${crawlerTypes}">
			<li>
				<a href="?filterField=crawlerType&filterValue=${currentIndex}" 
				${param.filterValue == currentIndex && param.filterField == 'crawlerType' ? ' style="font-size:1.2rem;font-weight:bold;"' : ''}
				>
				${crawlerTypes[currentIndex]}
			</a>
			</li>		
		</c:foreach>	
	</ul>
	
	<div style="clear:left;"></div>
<!-- // 过滤器 -->

		<h1>列表</h1>
		<ul class="title">
			<li class="col_id">#ID</li>
			<li class="col_name">名称</li>
			<li class="col_createDate">上次更新|集数|来源</li>
			<li class="col_meta">分类|门户|类型</li>
			<li class="col_action" style="text-align:center;">操作</li>
		</ul>
		<ul class="list">
			<c:foreach var="current" items="${PageResult.rows}">
				<li>
					<div class="col_id">${current.id}</div>
					<div class="col_name" title="${current.name}">
						<a href="${current.id}">${current.name}</a>
					</div>
					<div class="col_createDate" >
						${viewUtils.formatDate(current.lastUpdate, 'yyyy-MM-dd')} | ${current.length} | 
						<a href="${current.url}" target="_blank">来源</a>
					</div>
					<div class="col_meta" title="${all_portals_map[current.portalId]['name']} | ${catalogs_map[current.catalog]['name']}  | ${album_map[current.albumId]['name']}">
						${crawlerTypes[current.crawlerType]} | 
						${all_portals_album_map[current.portalId]['name']} | 
						${all_catalogs_album_map[current.catalog]['name']} 
					</div>
					<div class="col_action">
						<button class="b1" onclick="location.assign('${current.id}');">详情</button>
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
			 
		</script>
		</div>
	</jsp:attribute>
</tags:content>



