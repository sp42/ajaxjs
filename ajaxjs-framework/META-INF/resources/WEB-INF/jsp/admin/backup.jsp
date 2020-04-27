<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
	<jsp:param name="title" value="全局配置" />
</jsp:include>
<style>
span {
	font-size: 9pt;
	color: gray;
}

li {
	list-style: none;
	margin: 5%;
}

button {
	padding: 3% 10%;
	width: 100%;
}

td {
	padding: 3%;
	vertical-align: top;
}
</style>
</head>
<body class="admin-entry-form">
	<div>
		<ajaxjs-admin-header> <template slot="title">备份</template>
		</ajaxjs-admin-header>
	</div>
	<a href="#" download id="downLink"></a>
	
	<table style="width: 629px;" border="0">
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
			aj.xhr.get('${ctx}/admin/backup/db/', () => {
				down('db.zip');
			});
		}
	</script>
</body>
</html>