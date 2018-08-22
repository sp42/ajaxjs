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

//图片选择器、预览和校验
//建议每次创建实例时声明 ref="uploadControl" 以便对应组件
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
			required: false // false 表示不随表单设置值
		},
		imgId : {
	      type: Number,
	      required: false
	    },
	    limit : {
	    	type : Object,
	    	required : false,
	    	default : function(){
	    		return { // 上传限制
					maxSize : 600,
					fileExt: /png|gif|jpg|jpeg/i,
					maxWidth: 1200,
					maxHeight:1680
				};
	    	}
	    },
	    labelId : {
	    	type : String,
	    	required : false,
	    	default : 'input_file_molding'
	    }
	},
	template : 
		'<div class="ajaxjs-img-upload-perview">\
			<div>\
				<img class="upload_img_perview" :src="(isFileSize && isExtName && isImgSize && isFileTypeCheck && imgBase64Str) ? imgBase64Str : imgPlace" />\
				<input v-if="imgName" type="hidden" :name="imgName" :value="imgNewlyId || imgId" />\
			</div>\
			<div class="pseudoFilePicker">\
				<label :for="labelId"><div><div>+</div>点击选择图片</div></label>\
			</div>\
			<div v-if="isShowErrMessage()">{{errMsg}}</div>\
			<div v-if="isFileSize && isExtName && isImgSize && isFileTypeCheck && imgBase64Str">\
				<button @click.prevent="doUpload($event);" style="padding: .4em 1.3em; width: 80px;">上传</button>\
			</div>\
		</div>',
		
	created : function(){
		this.BUS.$on('upload-file-selected', this.onUploadInputChange);
	},

	methods : (function () {
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
				var defaultLimit = cfg.limit;
				
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
			},
			doUpload : function(e) {

				// 先周围找下 form，没有的话找全局的
				var form = this.$parent.$refs.uploadIframe && this.$parent.$refs.uploadIframe.$el;
				if(!form) {
					//form = aj('form[target=upframe]');
					form = this.$parent.$el.$('form');
				}
				
				form && form.submit();
				
				e.preventDefault();
				return false;
			}
		};
	})()
});

//通过 iframe 实现无刷新文件上传
//这里包含一个 form元素，form元素不能嵌套在 form 里面，故独立出来一个组件
Vue.component('ajaxjs-fileupload-iframe', {
	data : function() {
		return {
			radomId : Math.round(Math.random() * 1000),
			uploadOk_callback: function(){}
		};
	},
	props : {
		uploadUrl : {
			type: String,
			required: true
		},
	    labelId : {
	    	type : String,
	    	required : false,
	    	default : 'input_file_molding'
	    }
	},
	template : // 隐藏的 input 上传控件为了无刷新上传，对应 form 的 target 
		'<form :action="uploadUrl" method="POST" enctype="multipart/form-data" :target="\'upframe_\' + radomId">\
			<input name="fileInput" :id="labelId" type="file" multiple="multiple" class="hide" @change="fireUploadFileSelected($event)" />\
			<iframe :name="\'upframe_\' + radomId" class="hide" @load="iframe_callback($event);"></iframe>\
		</form>',
	methods : {
		// 上传后成功的提示
		iframe_callback: function (e) { 
			var json = e.target.contentDocument.body.innerText;
			
			if(json[0] == '{') {
				json = JSON.parse(json);
				
				if(json.isOk) {
					aj.msg.show('上传成功！');
					
					if(this.uploadOk_callback && typeof this.uploadOk_callback == 'function') {
						var imgUrl = json.imgUrl;
						this.uploadOk_callback(imgUrl);
					}
				}
			}
		},
		fireUploadFileSelected : function(e) {// 在附近查找 上传组件：就近原则
			var p = this.$parent;
			while(p && p.$refs && !p.$refs.uploadControl) {
				p = p.$parent;
			}
			
			p.$refs.uploadControl.onUploadInputChange(e);
		}
	}
});



/**
* 将上传控件嵌入到一个浮出层中
*/
Vue.component('aj-popup-upload', {
	data : function() {
		return {
			text : {}
		};
	},
	props : {
		uploadUrl : {// 上传接口地址
			type: String,
			required: true
		},
		imgName : {
			type: String,
			required: false
		},
		imgId : {
	      type: Number,
	      required: false
	    },
		imgPlace : String // 图片占位符，用户没有选定图片时候使用的图片
	},
	template : 
		'<aj-layer>\
			<h3>图片上传</h3>\
			<ajaxjs-img-upload-perview style="width:420px;" :img-name="imgName" :img-id="imgId" :img-place="imgPlace" label-id="foo1" ref="uploadControl"></ajaxjs-img-upload-perview>\
			<ajaxjs-fileupload-iframe :upload-url="uploadUrl" label-id="foo1" ref="uploadIframe"></ajaxjs-fileupload-iframe>\
			<div>上传限制：{{text.maxSize}}kb 或以下，分辨率：{{text.maxHeight}}x{{text.maxWidth}}</div>\
		</aj-layer>',
	mounted: function() {
		// todo 是否不需要对象，而是逐个列出
		this.text = this.$refs.uploadControl.$options.props.limit.default();
	},
	methods : {
		/**
		 * 显示上传控件
		 * 
		 * @paran {Function} callback 上传成功之后的回调函数
		 */
		show : function(callback) {
			if(callback)
				this.$refs.uploadIframe.uploadOk_callback = callback;
			
			this.$children[0].show();
		}
	}
});



