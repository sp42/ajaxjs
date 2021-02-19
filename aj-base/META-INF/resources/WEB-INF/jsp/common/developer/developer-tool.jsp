<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="title" value="实用工具" />
	</jsp:include>
	<style>
		.backup span {
			font-size: 9pt;
			color: gray;
		}
		
		.backup li {
			list-style: none;
			margin: 5%;
		}
		
		.backup button {
			padding: 3% 10%;
			width: 100%;
		}
		
		.backup td {
			padding: 3%;
			vertical-align: top;
		}
		
		.tab .content > div{
			padding:2%;
		}
		
		.codeGen form>div {
			margin: 1%
		}
		
		.codeGen form>div>div {
			display:inline-block;
			min-width:10%;
		}
	</style>
	
	<!-- Admin 公共前端资源 -->
	<link rel="stylesheet" href="${aj_static_resource}/dist/css/admin/admin.css" />
	<script src="${aj_static_resource}/dist/admin/admin.js"></script>
</head>
<body class="admin-entry-form">
	<div>
		<aj-admin-header> 
			<template slot="title">实用工具</template>
		</aj-admin-header>
	</div>

	<div class="aj-simple-tab-horizontal tab" style="padding: 1% 5%;">
		<ul>
			<li :class="{'selected': 0 === selected}" @click="selected = 0">备份数据</li>
			<li :class="{'selected': 1 === selected}" @click="selected = 1">代码生成器</li>
			<li :class="{'selected': 2 === selected}" @click="selected = 2">后台日志浏览</li>
			<li :class="{'selected': 3 === selected}" @click="selected = 3">统计代码行数</li>
		</ul>
		<div class="content">
			<div :class="{'selected': 0 === selected}">			
<a href="#" download id="downLink"></a>
<table style="width: 629px;" border="0" class="backup">
	<tbody>
		<tr>
			<td>备份图片<span>（/images 目录）</span></td>
			<td><button class="aj-btn" onclick="backupImages();">下载</button>
				<br></td>
		</tr>
		<tr>
			<td style="width: 333px;">备份网站 <span>若需以下目录请勾选</span><br>
				<ul>
					<li><label><input class="isWithImage" type="checkbox">
							包括备份 /images 图片目录</label></li>
					<li><label><input class="isWithClasses"
							type="checkbox"> 包括备份 /WEB-INF/classes 的 Java 类目录</label></li>
					<li><label><input class=isWithLib type="checkbox">
							包括备份 /WEB-INF/lib 的 JAR包 目录</label></li>
				</ul>
			</td>
			<td style="width: 100px;"><button class="aj-btn" onclick="backupSite();">下载</button></td>
		</tr>
		<tr>
			<td>备份数据库<span>（打包所有SQL为一个zip文件）</span></td>
			<td><button class="aj-btn" onclick="backupDB();">下载</button></td>
		</tr>
		<tr>
			<td>清空下载目录。 <span>注意下载后请一定要清理，否则公众可以下载重要数据。</span></td>
			<td><button class="aj-btn" onclick="aj.xhr.dele('backup/', j => aj.msg.show(j.msg));">清理</button></td>
		</tr>
	</tbody>
</table>
	</div>
			
	<div :class="{'selected': 1 === selected, codeGen:true}">
					<!-- TAB 内容 -->

	<!-- http://localhost:8080/ajaxjs-tools/CodeGenerators?getTable=user_admin_reosurces&isMap=true&beanName=UserRoleResources -->
	<form action="${ctx}/admin/CodeGenerators/" method="post" class="aj-form">
		<div>
			<div>SQL表名：</div><input type="text" name="getTable" />
		</div>
		<div>
			<div>BeanName:</div><input type="text" name="beanName" /> 通常用于类名，例如 UserRoleResources
		</div>
		<div>
			<div>isMap: </div><input type="radio" name="isMap" value="true"> true <input type="radio" name="isMap" value="false" checked /> false   当 isMap=false 时不生成 Bean 类，id使用 int
		</div>
		<div> 
			<div>saveFolder: </div><input type="text" name="saveFolder" placeholder="C:\project\temp\CodeGenerators\" value="C:\\temp" size="80" /> 保存目录
		</div>
		<div>
			<div>packageName: </div><input type="text" name="packageName" value="com.ajaxjs.user.role"  size="80" /> 包名
		</div>
		<div>
			<div>dbUrl: </div><input type="text" name="dbUrl" value="${conn.url}" size="80" /> 数据库配置
		</div>
		<div>
			<div>dbUser: </div><input type="text" name="dbUser" value="${conn.username}" /> 
		</div>
		<div>
			<div>dbPassword: </div><input type="password" name="dbPassword" value="${conn.password}" /> 
		</div>
		<div>
			<button>生 成 单 表</button> &nbsp;&nbsp;&nbsp; <button>生成所有表的（速度较慢）</button><br>注意：仅支持 MySQL 数据库  
		</div>
	</form>
	<br />
		<!-- // TAB 内容 -->
		</div>
		<div :class="{'selected': 2 === selected}">
			<!-- TAB 内容 -->
			<p>在这里可以实时浏览 Tomcat 日志</p>
					<br />
<div id="log-container" style="height: 450px; overflow-y: scroll; background: #333; color: #aaa; padding: 10px; margin: 0 auto;">
	<div></div>
</div>


					<!-- // TAB 内容 -->
				</div>
				<div :class="{'selected': 3 === selected}">
					<form action="${ctx}/admin/CodeGenerators/CodeCount" method="get" class="aj-form">
						<div>
							<div>文件夹名：</div>
							<br />
							<input type="text" name="folder" size="100" />
						</div>
						<br />
						<div>
							<button>统计代码行数</button>
						</div>
					</form>
				</div>
			</div>
		</div>

	<script>
		TAB = new Vue({
			el : '.tab',
			data:{
				selected:0
			}
		});

		new Vue({
			el : 'body>div'
		});
		
		down = file => {			
			var link = document.body.$('#downLink');
			link.href = '${ctx}/temp/' + file;
			// 前端实现文件自动下载 不知为何第一次下载不行，要等好久，其实文件已经在服务端上了
			setTimeout(() => {
				link.click();
			}, 5500);
		}
		
		backupImages = () => {
			aj.xhr.get('backup/images/', () => {
				down('images.zip');
			});
		}
		
		backupSite = () => {
			var i = 0;
			
			if(document.body.$('.isWithImage').checked)
				i += 1;
			if(document.body.$('.isWithClasses').checked)
				i += 2;
			if(document.body.$('.isWithLib').checked)
				i += 4;
			
			aj.xhr.get('backup/site/?i=' + i, () => {
				down('site.zip');
			});
		}
		
		backupDB = () => {
			aj.xhr.get('backup/db/', json => {
				down(json.zipFile);
			});
		}
	
	</script>
	<script src="//cdn.bootcss.com/jquery/2.1.4/jquery.js"></script>
	<script>
		$(document).ready(function() {
			// 指定websocket路径
			var websocket = new WebSocket('ws://' + document.location.host + '/${ctx}/tomcat_log');
			websocket.onmessage = function(event) {
				// 接收服务端的实时日志并添加到HTML页面中
				$("#log-container div").append(event.data);
				// 滚动条滚动到最低部
				$("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
			};
		});
	</script>	
</body>
</html>