<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
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
</head>
<body class="admin-entry-form">
	<div>
		<ajaxjs-admin-header> 
			<template slot="title">实用工具</template>
		</ajaxjs-admin-header>
	</div>
	

		<div class="aj-simple-tab-horizontal tab" style="padding: 1% 5%;">
				<ul>
					<li :class="{'selected': 0 === selected}" @click="selected = 0">备份数据</li>
					<li :class="{'selected': 1 === selected}" @click="selected = 1">代码生成器</li>
					<li :class="{'selected': 2 === selected}" @click="selected = 2">后台日志浏览</li>
					<li :class="{'selected': 3 === selected}" @click="selected = 3">统计代码行数</li>
					<li :class="{'selected': 4 === selected}" @click="selected = 4">前端代码打包</li>
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
			<td><button class="aj-btn" onclick="aj.xhr.dele('${ctx}/admin/backup/', j=>{aj.msg.show(j.msg)})">清理</button></td>
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
			<div>saveFolder: </div><input type="text" name="saveFolder" placeholder="C:\project\temp\CodeGenerators\" value="C:\\temp" size="80" /> 保存目录6565
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
			<div>dbPassword: </div><input type="text" name="dbPassword" value="${conn.password}" /> 
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
				<div :class="{'selected': 4 === selected}">	
					<p>1、仅在开发模式下（Windows）执行，且在部署之前执行。2、按实际情况改为你的工程目录。</p>
					<div>
						<input class="jsSave aj-input" style="height:32px;width:400px;margin-right:30px;" value="${aj_allConfig.System.project_folder.replace('\\',  '/')}" /> <button class="aj-btn" onclick="packageJs();return false;">打包 JavaScript</button>
						<div>最终保存在 /asset/js/all.js 中。<br />**请确保浏览器可以访问谷歌 GAE 站点，原因是压缩 JS 采用了谷歌的接口。推荐 GHelper 浏览器插件。** </div>
					</div>
<%-- 					<div>
						<input class="jsSave aj-input" style="height:32px;width:400px;margin-right:30px;" value="${aj_allConfig.System.project_folder.replace('\\',  '/')}" /> <button class="aj-btn" onclick="window.open('/ajaxjs-js/JsController?output=' + aj('.jsSave').value);return false;">打包 JavaScript</button>
						<div>最终保存在 /asset/js/all.js 中。 </div>
					</div> --%>
					<div>
						<input class="cssSave aj-input" style="height:32px;width:400px;margin-right:30px;" value="${aj_allConfig.System.project_folder.replace('\\',  '/')}" /> <button class="aj-btn" onclick="window.open('${ctx}/?css=true&output=' + aj('.cssSave').value);return false;">打包网站 CSS</button>
						<div>最终保存在 /asset/css/main.css 中。 </div>
					</div>
					<div>
						<input class="adminSave aj-input" style="height:32px;width:400px;margin-right:30px;" value="${aj_allConfig.System.project_folder.replace('\\',  '/')}" /> <button class="aj-btn" onclick="window.open('${ctx}/admin/?css=true&file=admin.css&output=' + aj('.adminSave').value);return false;">打包后台 CSS</button>
						<div>最终保存在 /asset/css/admin.css 中。 </div>
					</div>
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
	</script>
	

	<script>
		new Vue({
			el : 'body>div'
		});
		
		down = (file) =>{			
			var link = aj('#downLink');
			link.href = '${ctx}/temp/' + file;
			// 前端实现文件自动下载 不知为何第一次下载不行，要等好久，其实文件已经在服务端上了
			setTimeout(()=>{
				link.click();
			}, 5500);
		}
		
		backupImages = () => {
			aj.xhr.get('${ctx}/admin/backup/images/', () => {
				down('images.zip');
			});
		}
		
		backupSite = () => {
			var i = 0;
			if(aj('.isWithImage').checked)
				i+=1;
			if(aj('.isWithClasses').checked)
				i+=2;
			if(aj('.isWithLib').checked)
				i+=4;
			aj.xhr.get('${ctx}/admin/backup/site/?i=' + i, () => {
				down('site.zip');
			});
		}
		
		backupDB = () => {
			aj.xhr.get('${ctx}/admin/backup/db/', json => {
				down(json.zipFile);
			});
		}
		
		function packageJs() {
			aj.xhr.get("/ajaxjs-js/JsController", js => {
				aj.xhr.post('https://closure-compiler.appspot.com/compile', compressedJs => {
					aj.xhr.post("/ajaxjs-js/JsController", j => {
						console.log(j)
						aj.alert(JSON.parse(j).isOk ? '打包 js 成功！' : '打包 js 失败！')
					}, {
						js: encodeURIComponent(compressedJs),
						saveFolder: aj('.jsSave').value
					});
				}, {
					js_code: encodeURIComponent(js),
					compilation_level: 'WHITESPACE_ONLY',
					output_format: 'text',
				    output_info: 'compiled_code'
				}, {
					  
				});
			}, null, {
				parseContentType: 'text'
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