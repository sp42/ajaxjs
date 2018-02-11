<%@tag pageEncoding="UTF-8" import="com.ajaxjs.simpleApp.Constant" description="上传组件"%>
	<textarea class="hide msgboxTpl">	
					<div class="msgbox imgUpload">
						<h1>title</h1>
						<div class="topCloseBtn closeAction">×</div>
						<div class="inner">
<!-- Tab -->
<div class="tab3">
	<ul>
		<li>上传本地图片</li>
		<li>图片库</li>
	</ul>
	<div class="content">
		<div>
			<img style="float:left;margin-bottom:20px;" class="upload_img_perview" src="${pageContext.request.contextPath}/asset/images/imgBg.png" />
			
			<div class="upload-btns">
				<form action="../uploadImg/do.do" method="post" enctype="multipart/form-data" target="upframe">
					<!-- 隐藏的 input 上传控件 -->
					<input name="fileInput" type="file" />
					<!-- 隐藏的 iframe，为了无刷新上传，对应 form 的 target -->
					<iframe name="upframe" style="display:none;"></iframe>
					
					<!-- 点击按钮选择文件 -->
					<label for="input_file_molding"> 
						<div>
							<div>+</div>
							点击选择图片
						</div>
					</label>
					
					<br />
					<button class="my-btn-2">上传图片</button> <button class="my-btn-2 insertAfterUplaod">选择图片</button>
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
				
				
				<script type="text/javascript" src="${pageContext.request.contextPath}/asset/js/libs/dd.js"></script>
				<script type="text/javascript" src="${pageContext.request.contextPath}/asset/js/widget/tab.js"></script>
				
				<script type="text/javascript" src="http://118.193.132.50:8080/framework/dom.js"></script>
				<script type="text/javascript" src="http://118.193.132.50:8080/framework/widget.js"></script>
				<script>
					// 上传后成功的提示
					function iframe_callback(e, obj) { 
						var json = e.target.contentDocument.body.innerText;
						
						if(json[0] == '{') {
							json = JSON.parse(json);
							
							if(json.isOk) {
								alert('上传成功！');
								obj.lastUploadedImg = json.url;
							}
						} else {
							
						}
					}
						
					// 加载图片
					function getAllPic (showTab) {						
						ajaxjs.xhr.get('../gallery/list.do', function(arr) {
							var html = '', tpl = '<li><img src="../../../images/{0}" /></li>';
							for(var i = 0, j = arr.length; i < j; i++)
								html += tpl.replace('{0}', arr[i]); 
							
							showTab.qs('ul').innerHTML = html;
						});
					}
					
					// 正文
					Upload_Panel = {
						htmlEditor : htmlEditor,
						show : function() {
							new ajaxjs.Popup({
								title : '正文图片',
								onShow : this.initTab
							}).show();
						},
						
						// 上次上传的图片
						lastUploadedImg :  null,
						
						initTab : function() {
							var tab = new SimpleTab(document.querySelector('.tab3'));
							tab.afterRender = function(index, btn, showTab) { 
								switch(index) {
								case 0:
									new ajaxjs.Upload_perview(qs('.upload_img_perview'), qs('input[name=fileInput]'));
	
									qs('iframe[name=upframe]').onload = iframe_callback.delegate(null, Upload_Panel);
									
									// 插入到 html 编辑器
									qs('button.insertAfterUplaod').onclick = function(e) {
										e.preventDefault();
										
										var filename = '../images/' + Upload_Panel.lastUploadedImg.split('/').pop();
										Upload_Panel.htmlEditor.format("insertImage", filename);
									}
								break;
								case 1:
									getAllPic (showTab);
								}
							}
							tab.jump(0);
						},
						insertImg : function(e) {
							var img = e.target, filename = '../images/' + img.src.split('/').pop();
							this.htmlEditor.format("insertImage", filename);
						}
					};
					
					// 封面
					Upload_Cover_Panel = {
						htmlEditor : htmlEditor,
						show : function() {
							new ajaxjs.Popup({
								title : '封面图片',
								onShow : this.initTab
							}).show();
						},
						
						// 上次上传的图片
						lastUploadedImg :  null,
						
						initTab : function() {
							var tab = new SimpleTab(document.querySelector('.tab3'));
							tab.afterRender = function(index, btn, showTab) { 
								switch(index) {
								case 0:
									new ajaxjs.Upload_perview(qs('.upload_img_perview'), qs('input[name=fileInput]'));

									// 上传后成功的提示
									qs('iframe[name=upframe]').onload = iframe_callback.delegate(null, Upload_Cover_Panel);
									// 插入到 html 编辑器
									qs('button.insertAfterUplaod').onclick = function(e) {
										e.preventDefault();
										
										var filename = '../images/' + Upload_Panel.lastUploadedImg.split('/').pop();
										Upload_Panel.htmlEditor.format("insertImage", filename);
									}
								break;
								case 1:
									getAllPic (showTab);
								}
							}
							tab.jump(0);
						},
						insertImg : function(e) {
							var img = e.target, filename = '../images/' + img.src.split('/').pop();
							this.htmlEditor.format("insertImage", filename);
						}
					};
				</script>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<button class="my-btn-2" onclick="Upload_Panel.show();return false;">
					<img src="${pageContext.request.contextPath}/<%=Constant.commonIcon%>add.gif" />上传正文图片
				</button>
				
				<button class="my-btn-2" onclick="Upload_Cover_Panel.show();return false;">
					<img src="${pageContext.request.contextPath}/<%=Constant.commonIcon%>add.gif" />上传封面图片
				</button>
 
	