/**
 * 上传组件
 */



// 文件选择器和校验
Vue.component('ajaxjs-file-upload', {
	data : function() {
		return {
			isFileSize : false,			// 文件大小检查
			isExtName : false,			// 文件扩展名检查
			errMsg : null,				// 错误信息
			newlyId : null				// 成功上传之后的文件 id
		};
	},
	props : {
		fieldName : {
			type: String,
			required: true
		},
		filedId : {
	      type: Number,
	      required: false
	    },
	},
	template : 
		'<div class="ajaxjs-img-upload-perview">\
			<div class="pseudoFilePicker">\
				<input type="hidden" :name="fieldName" :value="newlyId || filedId" />\
				<label for="input_file_molding"><div><div>+</div>点击选择图片</div></label>\
			</div>\
			<div v-if="!isFileSize || !isExtName">{{errMsg}}</div>\
			<div v-if="isFileSize && isExtName">\
				<button onclick="aj(\'form[target=upframe]\').submit();return false;">上传</button>\
			</div>\
		</div>'
});

// 图片选择器、预览和校验
Vue.component('ajaxjs-img-upload-perview', {
	data : function() {
		return {
			imgBase64Str : null, 		// 图片的 base64 形式，用于预览
			isFileSize : false,			// 文件大小检查
			isExtName : false,			// 文件扩展名检查
			isImgSize : false, 			// 图片分辨率大小检查
			isFileTypeCheck : false, 	// 图片二进制的类型检查
			errMsg : null,				// 错误信息
			imgNewlyId : null			// 成功上传之后的图片 id
		};
	},
	props : {
		imgPlace : String, // 图片占位符，用户没有选定图片时候使用的图片
		imgName : {
			type: String,
			required: true
		},
		imgId : {
	      type: Number,
	      required: false
	    }
	},
	template : 
		'<div class="ajaxjs-img-upload-perview">\
			<div>\
				<img class="upload_img_perview" :src="(isFileSize && isExtName && isImgSize && isFileTypeCheck && imgBase64Str) ? imgBase64Str : imgPlace" />\
				<input type="hidden" :name="imgName" :value="imgNewlyId || imgId" />\
			</div>\
			<div class="pseudoFilePicker">\
				<label for="input_file_molding"><div><div>+</div>点击选择图片</div></label>\
			</div>\
			<div v-if="isShowErrMessage()">{{errMsg}}</div>\
			<div v-if="isFileSize && isExtName && isImgSize && isFileTypeCheck && imgBase64Str">\
				<button onclick="aj(\'form[target=upframe]\').submit();return false;">上传</button>\
			</div>\
		</div>',
	methods : (function () {
		var defaultLimit = {
			maxSize : 600,
			fileExt: /png|gif|jpg|jpeg/i,
			maxWidth: 120,
			maxHeight:1680
		};

		// 文件头判别，看看是否为图片
		var imgHeader = {
			"jpeg" : "/9j/4",
			"gif" : "R0lGOD",
			"png" : "iVBORw"
		};
		
		return {
			onUploadInputChange : function(e) {
				var fileInput = e.target;
				
				for(var i = 0, j = fileInput.files.length; i < j; i++) {
					var reader = new FileReader(), fileObj = fileInput.files[i];
					reader.onload = this.afterLoad.delegate(null, fileObj, this);
					reader.readAsDataURL(fileObj);
				}
			},
			afterLoad : function (e, fileObj, self) {
				var imgBase64Str = e.target.result;
				var isOk = self.checkFile(fileObj, imgBase64Str, self);
				
				self.imgBase64Str = imgBase64Str;
			},
			isShowErrMessage : function() {
				return !this.isFileSize || !this.isExtName || !this.isImgSize || !this.isFileTypeCheck;
			},
			checkFile : function (fileObj, imgBase64Str, cfg) {
				if(fileObj.size > defaultLimit.maxSize * 1024) {  // 文件的大小，单位为字节B
					cfg.isFileSize = false;
					cfg.errMsg = "要上传的文件容量过大，请压缩到 " + defaultLimit.maxSize + "kb 以下";
					return;
				} else {
					cfg.isFileSize = true;
				}
				
				var ext = fileObj.name.split('.').pop();
				if (!defaultLimit.fileExt.test(ext)) {
					cfg.isExtName = false;
					cfg.errMsg = '根据文件后缀名判断，此文件不是图片'; 
					return;
				} else {
					cfg.isExtName = true;
				}
				
				var imgEl = new Image();
				imgEl.onload = function() {
					if (imgEl.width > cfg.maxWidth || imgEl.height > cfg.maxHeight) {
						cfg.isImgSize = false;
						cfg.errMsg = '图片大小尺寸不符合要求哦，请重新图片吧~';
					} else {
						cfg.isImgSize = true;
					}
				}
				
				imgEl.src = imgBase64Str;
				
				cfg.isFileTypeCheck = false;
				for ( var i in imgHeader) {
					if (~imgBase64Str.indexOf(imgHeader[i])){
						cfg.isFileTypeCheck = true;
						return;
					}
				}
				
				cfg.errMsg = "亲，改了扩展名我还能认得你不是图片哦";
			}
		};
	})()
});

// 通过 iframe 实现无刷新文件上传
Vue.component('ajaxjs-fileupload-iframe', {
	props : {
		uploadUrl : {
			type: String,
			required: true
		}
	},
	template : // 隐藏的 input 上传控件为了无刷新上传，对应 form 的 target 
		'<form :action="uploadUrl" method="POST" enctype="multipart/form-data" target="upframe">\
			<input name="fileInput" id="input_file_molding" type="file" multiple="multiple" class="hide" onchange="UploadController.$children[0].onUploadInputChange(event);" />\
			<iframe name="upframe" class="hide" @load="iframe_callback($event);"></iframe>\
		</form>',
	methods : {
		// 上传后成功的提示
		iframe_callback: function (e) { 
			var json = e.target.contentDocument.body.innerText;
			
			if(json[0] == '{') {
				json = JSON.parse(json);
				
				if(json.isOk) {
					ajaxjs.msg('上传成功！');
					// UploadController.imgNewlyId = json.newlyId;
				}
			}
		}
	}
});