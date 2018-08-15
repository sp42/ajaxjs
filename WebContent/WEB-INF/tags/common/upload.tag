<%@tag pageEncoding="UTF-8" description="上传组件"%>
<textarea class="hide uploadBoxTpl">
	<!-- Tab -->
	<div class="simpleTab_hoz uploadBox" style="width:700px;">
		<h4>{title}</h4>	
		<ul>
			<li>上传本地图片</li>
			<li>图片库</li>
		</ul>
		<div class="content">
			<div>
				<img style="float:left;margin-bottom:20px;max-width:180px;max-height:160px;" class="upload_img_perview" src="${commonImage}imgBg.png" />
				
				<div class="upload-btns">
					<form action="${ctx}/admin/attachmentPicture/upload/${info.uid}/" method="POST" enctype="multipart/form-data" target="upframe">
						<!-- 隐藏的 input 上传控件 -->
						<input name="fileInput" id="input_file_molding" type="file" class="hide" />
						<!-- 隐藏的 iframe，为了无刷新上传，对应 form 的 target -->
						<iframe name="upframe" class="hide"></iframe>
						
						<!-- 点击按钮选择文件 -->
						<label for="input_file_molding"> 
							<div>
								<div>+</div>
								点击选择图片
							</div>
						</label>
						
						<br />
						<button class="ajaxjs-btn">上传图片</button> <button class="ajaxjs-btn insertAfterUplaod">选择图片</button>
					</form> 
				</div>
			</div>
			<div class="gallery">
				<!-- 图片列表 -->
				<ul onclick="Upload_Panel.insertImg.apply(Upload_Panel, arguments);">
					<li>
	<!-- 					<img src="../../../images/1.jpg" /> -->
					</li>
				</ul>
			</div>
		</div>
	</div>
	<!-- //Tab -->
</textarea>		

<script>
	// 上传后成功的提示
	function iframe_callback(e, obj) { 
		var json = e.target.contentDocument.body.innerText;
		
		if(json[0] == '{') {
			json = JSON.parse(json);
			
			if(json.isOk) {
				ajaxjs.msg('上传成功！');
				obj.lastUploadedImg = json.imgUrl;
				loadPic_AJAX();
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
			
			showTab.querySelector('ul').innerHTML = html;
		});
	}
	
	// 正文
	Upload_Panel = {
		htmlEditor : htmlEditor,
		show : function() {
			ajaxjs.layer('.uploadBoxTpl');
			this.initTab();
		},
		
		// 上次上传的图片
		lastUploadedImg :  null,
		
		initTab : function() {
			var tab = new ajaxjs.SimpleTab(document.querySelector('.simpleTab_hoz'));
			tab.afterRender = function(index, btn, showTab) { 
				switch(index) {
					case 0:
						showTab.up('div').up('div').querySelector('h4').innerHTML = '正文图片';
						new ajaxjs_Upload_perview(showTab.querySelector('.upload_img_perview'), showTab.querySelector('input[name=fileInput]'));

						showTab.querySelector('iframe[name=upframe]').onload = iframe_callback.delegate(null, Upload_Panel);
						
						// 插入到 html 编辑器
						showTab.querySelector('button.insertAfterUplaod').onclick = function(e) {
							e.preventDefault();
							
							var filename = '${ctx}' + Upload_Panel.lastUploadedImg;
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
		// htmlEditor : htmlEditor,
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
 
	