<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib prefix="c" uri="/ajaxjs" %>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/less/admin.less" title="${isCreate ? '新建' : '编辑:'}${uiName}${info.name}" />
	<body>
<commonTag:adminUI type="editor">
	<div class="row">
		<div>
			<table>
				<tr>
					<td>
			<label>
				<div class="label">标 题：</div>
				<input placeholder="请填写更新标题名称" size="60" required="required" name="name" value="${info.name}" type="text" class="my-inputField" />
			</label>
					</td>
					<td>
			<label>
				<div class="label"> &nbsp;版 本：</div>
				<input placeholder="请填写版本号" size="30" required="required" name="version" value="${info.name}" type="text" class="my-inputField" />
			</label>
					</td>
				</tr>
			</table>
		</div>
	</div>
			
	<div class="row">
		<div>
			<table>
				<tr>
					<td>
			<label>
				<div class="label">门 户：</div>
				<select name="portalId">
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
					
					</td>
					<td>
			<label>
				<div class="label"> &nbsp;渠 道：</div>
				<select name="channelId">
					<c:foreach items="${channels}" var="current">
						<c:choose>
							<c:when test="${info.channelId == current.channel}">
								<option value="${current.channel}" selected>${current.name}</option>
							</c:when>
							<c:otherwise>
								<option value="${current.channel}">${current.name}</option>
							</c:otherwise>
						</c:choose>
					</c:foreach>
				</select>
			 </label>
					
					</td>
					<td>
						 &nbsp;&nbsp;是否强制更新？&nbsp;&nbsp;
						<label>
							<input type="radio" name="forceUpdate" value="yes" ${not empty info.forceUpdate && info.forceUpdate ? 'checked' : ''} /> 是
						</label>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<label>
							<input type="radio" name="forceUpdate" value="no"  ${!info.forceUpdate ? 'checked' : ''}/> 否
						</label>
					</td>
				</tr>
			</table>
		</div>
	</div>
	
	<div class="row">
		<div>
			<label>
				<div class="label" style="vertical-align: top;">简 介：</div>
				<commonUI:htmlEditor name="content" basePath="../">${info.content}</commonUI:htmlEditor>
			</label>
		</div>
	</div>
	<div class="row">
		<div>
			<label>
				<div class="label">下 载：</div>
				<input type="text" name="downloadUrl"  placeholder="请上传 apk 包" size="60" required="required" />
				
			</label>
			<a href="javascript:Upload_Panel.show();">上传安装包</a>
		</div>
	</div>
	
	<!-- 上传控件 -->
<textarea class="hide msgboxTpl">	
					<div class="msgbox imgUpload">
						<h1>安装版维护</h1>
						<div class="topCloseBtn closeAction">×</div>
						<div class="inner">
<!-- Tab -->
<div class="tab3">
	<ul>
		<li>上传文件</li>
		<li>安装包列表</li>
	</ul>
	<div class="content">
		<div>
			<div class="upload-btns">
				<form action="../uploadImg/do.do" method="post" enctype="multipart/form-data" target="upframe">
					<!-- 隐藏的 input 上传控件 -->
					<input name="fileInput" type="file" />
					<!-- 隐藏的 iframe，为了无刷新上传，对应 form 的 target -->
					<iframe name="upframe" style="display:none;"></iframe>
					
					<!-- 点击按钮选择文件 -->
					
					<table>
						<tr>
							<td>
					<label for="input_file_molding"> 
						<div>
							<div>+</div>
							点击选择安装包
						</div>
					</label>
							</td>
							<td width="20"></td>
							<td>
					<span class="fileName"></span>
					<br />
					<button class="my-btn-2">上传文件</button>
							
							</td>
						</tr>
					</table>
				</form> 
			</div>
		</div>
		<div class="gallery">
			<!-- 图片列表 -->
			<ul onclick="Upload_Panel.insertImg.apply(Upload_Panel, arguments);">
				<li>
					<img src="../../../images/1.jpg" />
				</li>
			</ul>
		</div>
	</div>
</div>
<!-- //Tab -->
			
						</div>
						<div class="btn">
					 		<div class="closeAction">关闭</div>
						</div>
					</div>
					<div class="msgbox_mask"></div>
				</textarea>		
	<script>
		// 正文
		Upload_Panel = {
			show : function() {
				new ajaxjs.Popup({
					onShow : this.initTab
				}).show();
			},
			
			initTab : function() {
				var tab = new ajaxjs.SimpleTab(document.querySelector('.tab3'));
				tab.afterRender = function(index, btn, showTab) { 
					switch(index) {
					case 0:
						
						qs('input[name=fileInput]').addEventListener('change', function(event) {
							var fileName = this.value.split('\\').pop();
							var ext = fileName.split('.').pop();
							if(ext != 'apk') {
								alert('请选择 Android APK 格式文件');
							} else {
								qs('.fileName').innerHTML = fileName;
							}
						});
						qs('iframe[name=upframe]').onload = iframe_callback.delegate(null, Upload_Panel);
						
					break;
					case 1:
						//getAllPic (showTab);
					}
				}
				tab.jump(0);
			}
		};
	</script>
</commonTag:adminUI>
</body>
</html>