<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="common"    tagdir="/WEB-INF/tags/common"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<common:head lessFile="/asset/less/main.less" title="容祯自留地 ${info.name}">
		<style>
			.catalog{
				float:right;
				width:165px;
				padding-right:5%;
				font-size:.8rem;
				line-height:23px;
				text-align:right;
			}
			.catalog select{
				width:110px;
				height:23px;
			}
		</style>
	</common:head>
<body>
	<header>
		<nav>
			<div class="catalog"><common:page type="catalog_dropdownlist" /></div>
			<a href="${pageContext.request.contextPath == '' ? '/' : pageContext.request.contextPath}/index">
				<h1>容祯的自留地</h1>
			</a>
		</nav>
	</header>
	<div class="contentBody">
		<c:choose>
			<c:when test="${not empty info}">
	<style>
		article p {
		    color: #555;
		    padding: 0 2%;
		    line-height: 140%;
		    margin: 20px 0;
		    letter-spacing: 1px;
		    text-align: justify;
		    text-indent: 2em;
		}
		
	</style>
		<article>
			<h2>${info.name}</h2>
			<h3 class="createDate">
 			分类： ${info.catalogName} 
			创建日期：${(info.createDate)}</h3>
			<div>${info.content.replace('src="', 'src="images/')}</div>
			<div style="margin-top:5%;font-size:.8rem;padding-left:2%;">
			<c:if test="${not empty neighbor.perRecord.id}">
				<div>
					<a href="?id=${neighbor.perRecord.id}">上则记录：${neighbor.perRecord.name}</a>
				</div>
			</c:if>
			<c:if test="${not empty neighbor.nextRecord.id}">
				<div>
					<a href="?id=${neighbor.nextRecord.id}">下则记录：${neighbor.nextRecord.name}</a>
				</div>
			</c:if>
			</div>
		</article>
		<div style="overflow: hidden;">
			<table align="center">
				<tr>
					<td align="middle">
						<common:widget type="share" />
					</td>
					<td align="middle">
						<common:widget type="adjustFontSize" />
					</td>
				</tr>
			</table>
		</div>
 
		<!-- UY BEGIN -->
		<div id="uyan_frame" class="list" style="padding:2%;"></div>
		<script type="text/javascript" src="http://v2.uyan.cc/code/uyan.js?uid=1952510"></script>
		<!-- UY END -->
				
		
			</c:when>
			<c:otherwise>

				<!-- 列表 -->
				<ul class="list">
				<c:foreach items="${list}" var="current">
					<li>
							<a href="detail?id=${current.id}">
								<div>
									<h3>${current.name}</h3>
									<p>${current.brief}</p>
								</div>
							</a>
							<div align="right" class="misc">
								${(current.createDate)}  分类：${current.catalogName}
							</div>
					</li>
				</c:foreach>
				</ul>
				<!-- 列表 -->
				
				<div class="list">
					<common:page type="page" />
				</div>

			</c:otherwise>
		</c:choose>
	</div>
	<footer>
		<div align="center">© 2005-2017, 容祯</div>
	</footer>
	
	<!-- CNZZ 统计 -->
	<div class="hide">
	<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256910326'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s95.cnzz.com/z_stat.php%3Fid%3D1256910326' type='text/javascript'%3E%3C/script%3E"));</script>
	</div>
	<!-- // CNZZ 统计 -->

</body>
</html>