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
			required: false
		},
		
		filedId : {
	      type: Number,
	      required: false
	    },
	    
	    limitSize : Number,
	    
	    limitFileType: String
	},
	template : 
		'<div class="ajaxjs-file-upload">\
			<div class="pseudoFilePicker">\
				<input type="hidden" v-if="fieldName" :name="fieldName" :value="newlyId || filedId" />\
				<label for="input_file_molding"><div><div>+</div>点击选择文件</div></label>\
			</div>\
			<div v-if="!isFileSize || !isExtName">{{errMsg}}</div>\
			<div v-if="isFileSize && isExtName">\
				<button @click.prevent="doUpload($event);">上传</button>\
			</div>\
		</div>',
	methods : {
		onUploadInputChange : function(e) {
			var fileInput = e.target;
			var ext = fileInput.value.split('.').pop(); // 扩展名
			var size = fileInput.files[0].size;
			
			if(this.limitSize)
				this.isFileSize = size < this.limitSize;
			else
				this.isFileSize = true;
			
			if(this.limitFileType)
				this.isExtName = new RegExp(this.limitFileType, 'i').test(ext);
			else
				this.isExtName = true;
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
	}
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
	    },
	    accpectFileType : { // 可以上传类型
	    	type : String
	    }
	},
	template : // 隐藏的 input 上传控件为了无刷新上传，对应 form 的 target 
		'<form :action="uploadUrl" method="POST" enctype="multipart/form-data" :target="\'upframe_\' + radomId">\
			<input name="fileInput" :id="labelId" type="file" multiple="multiple" class="hide" @change="fireUploadFileSelected($event)" :accept="accpectFileType" />\
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
						var imgUrl = json.imgUrl || json.visitPath;
						this.uploadOk_callback(imgUrl, json);
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

//polyfill JavaScript-Canvas-to-Blob 解决了 HTMLCanvasElement.toBlob 的兼容性
//https://github.com/blueimp/JavaScript-Canvas-to-Blob
if (!HTMLCanvasElement.prototype.toBlob) {
	Object.defineProperty(HTMLCanvasElement.prototype, 'toBlob', {
		value : function(callback, type, quality) {
			var binStr = atob(this.toDataURL(type, quality).split(',')[1]), len = binStr.length, arr = new Uint8Array(len);

			for (var i = 0; i < len; i++) {
				arr[i] = binStr.charCodeAt(i);
			}

			callback(new Blob([ arr ], {
				type : type || 'image/png'
			}));
		}
	});
}

Vue.component('aj-xhr-upload', {
	data : function() {
		return {
			isFileSize : false,			// 文件大小检查
			isExtName : false,			// 文件扩展名检查
			isImgSize : false, 			// 图片分辨率大小检查
			errMsg : null,				// 错误信息
			newlyId : null,				// 成功上传之后的文件 id
			radomId : Math.round(Math.random() * 1000),		// 不重复的 id
			uploadOk_callback: ajaxjs.xhr.defaultCallBack,				// 回调函数
	    	imgBase64Str : null,			// 图片的 base64 形式，用于预览
	    	progress : 0
		};
	},
	props : {
		action : {
			type : String, // 上传路径
			required: true
		}, 		
		fieldName : String, 	// input name 字段名
	    limitSize : Number,
	    limitFileType: String,
	    accpectFileType: String,// 可以上传类型
	    isImgUpload : Boolean, 	// 是否图片上传
	    imgPlace : String,		// 图片占位符，用户没有选定图片时候使用的图片
	    imgMaxWidth : {type : Number, default : 1920},
	    imgMaxHeight: {type : Number, default : 1680}
	},
	template : 
		'<div class="aj-xhr-upload">\
			<div v-if="isImgUpload">\
				<a :href="imgPlace" target="_blank">\
					<img class="upload_img_perview" :src="(isFileSize && isExtName && imgBase64Str) ? imgBase64Str : imgPlace" />\
				</a>\
			</div>\
			<div class="pseudoFilePicker">\
				<label :for="\'uploadInput_\' + radomId"><div><div>+</div>点击选择{{isImgUpload ? \'图片\': \'文件\'}}</div></label>\
			</div>\
			<input type="file" :name="fieldName" class="hide" :id="\'uploadInput_\' + radomId" @change="onUploadInputChange($event)" :accept="isImgUpload ? \'image/*\' : accpectFileType" />\
			<div v-if="!isFileSize || !isExtName">{{errMsg}}</div>\
			<div v-if="isFileSize && isExtName">\
				<button @click.prevent="doUpload();" style="min-width:110px;">{{progress && progress !== 100 ? \'上传中 \' + progress + \'%\': \'上传\'}}</button>\
			</div>\
		</div>',
	methods : {
		onUploadInputChange : function(e) {
			var fileInput = e.target;
			var ext = fileInput.value.split('.').pop(); // 扩展名
			
			if(!fileInput.files || !fileInput.files[0]) return;
			
			this.$fileObj = fileInput.files[0]; // 保留引用
			
			this.$fileName = this.$fileObj.name;
			this.$fileType = this.$fileObj.type;
			
			var size = this.$fileObj.size;
			
			if(this.limitSize) {
				this.isFileSize = size < this.limitSize;
				this.errMsg = "要上传的文件容量过大，请压缩到 " + this.limitSize + "kb 以下";
			} else
				this.isFileSize = true;
			
			if(this.limitFileType) {
				this.isExtName = new RegExp(this.limitFileType, 'i').test(ext);
				this.errMsg = '根据文件后缀名判断，此文件不能上传';				
			} else
				this.isExtName = true;
			
		
			this.readBase64(fileInput.files[0]);
			
			if(self.isImgUpload) {
				var imgEl = new Image();
				imgEl.onload = function() {
					if (imgEl.width > self.imgMaxWidth || imgEl.height > self.imgMaxHeight) {
						cfg.isImgSize = false;
						self.errMsg = '图片大小尺寸不符合要求哦，请重新图片吧~';
					} else {
						cfg.isImgSize = true;
					}
				}
				
			}
		},

		readBase64 : function(file) {
			var reader = new FileReader(), self = this;
			
			reader.onload = function(e) {
				var imgBase64Str = e.target.result;
				self.imgBase64Str = imgBase64Str;
				
				if(self.isImgUpload) {
					var imgEl = new Image();
					imgEl.onload = function() {
						if(file.size > 300 * 1024) { // 大于 300k 才压缩
							self.compressImg(imgEl);
						}
						
						if (imgEl.width > self.maxWidth || imgEl.height > self.maxHeight) {
							self.isImgSize = false;
							self.errMsg = '图片大小尺寸不符合要求哦，请重新图片吧~';
						} else {
							self.isImgSize = true;
						}
					}
					
					imgEl.src = imgBase64Str;
					
					
					// 文件头判别，看看是否为图片
					var imgHeader = {
						"jpeg" : "/9j/4",
						"gif" : "R0lGOD",
						"png" : "iVBORw"
					};
					
					for ( var i in imgHeader) {
						if (~imgBase64Str.indexOf(imgHeader[i])) {
							self.isExtName = true;
							return;
						}
					}
					
					self.errMsg = "亲，改了扩展名我还能认得你不是图片哦";
				}
			}
			
			reader.readAsDataURL(file);
		},
		
		doUpload : function() {
			var fd = new FormData();
			if(this.$blob){
				fd.append("file", this.$blob, this.$fileName);
			} else 
				fd.append("file", this.$fileObj);
		
			var xhr = new XMLHttpRequest(), self = this;
	        xhr.onreadystatechange = ajaxjs.xhr.callback.delegate(null, this.uploadOk_callback, 'json');
	        xhr.open("POST", this.action, true);
	        xhr.onprogress = function(evt) {
	            var progress = 0;
	            var p = ~~(evt.loaded * 1000 / evt.total);
	            p = p / 10;
	            
	            if(progress !== p) {
	            	progress = p;
	            	console.log('progress: ', p);
	            }
	            self.progress = progress;
	        };
	        xhr.send(fd);
		},
		
		// 图片压缩
		compressImg: function(imgObj) {
			var self = this;
			var maxWidth = 1000, maxHeight = 1500;
			var fitSizeObj = this.fitSize(imgObj.width, imgObj.height, maxWidth, maxHeight);
			var targetWidth = fitSizeObj.targetWidth, targetHeight = fitSizeObj.targetHeight;
			
			var orient = this.getPhotoOrientation(imgObj);// 获取照片的拍摄方向
			
			if (orient == 6) {
				targetWidth = fitSizeObj.targetHeight;
				targetHeight = fitSizeObj.targetWidth;
			}
			
			var comp = new Image();
			comp.onload = function() {
				var canvas = document.createElement('canvas');
				canvas.width = targetWidth;
				canvas.height = targetHeight;
				canvas.getContext('2d').drawImage(this, 0, 0, targetWidth, targetHeight); // 图片压缩
				
				// canvas转为blob并上传
				canvas.toBlob(function(blob) {
					self.$blob = blob;
				}, self.$fileType || 'image/jpeg');
			}
			
			comp.src = this.rotate(imgObj, orient);
		},
		fitSize: function (originWidth, originHeight, maxWidth, maxHeight) {
		    // 目标尺寸
		    var targetWidth = originWidth, targetHeight = originHeight;
		    
		    // 图片尺寸超过400x400的限制
		    if (originWidth > maxWidth || originHeight > maxHeight) {
		        if (originWidth / originHeight > maxWidth / maxHeight) {
		            // 更宽，按照宽度限定尺寸
		            targetWidth = maxWidth;
		            targetHeight = Math.round(maxWidth * (originHeight / originWidth));
		        } else {
		            targetHeight = maxHeight;
		            targetWidth = Math.round(maxHeight * (originWidth / originHeight));
		        }
		    }
		    
		    return {
		    	targetWidth : targetWidth, targetHeight : targetHeight
		    };
		},
		getPhotoOrientation : function (img) {
		    var orient;
		    EXIF.getData(img, function () {
		        orient = EXIF.getTag(this, 'Orientation');
		    });
		    
		    return orient;
		},
		rotate : function (img, orient) {
			var width = img.width, height = img.height, 
			canvas = document.createElement('canvas'), ctx = canvas.getContext("2d");
			
			// set proper canvas dimensions before transform & export
			if ([ 5, 6, 7, 8 ].indexOf(orient) > -1) {
				canvas.width = height;
				canvas.height = width;
			} else {
				canvas.width = width;
				canvas.height = height;
			}
			
			// transform context before drawing image
			switch (orient) {
			case 2:
				ctx.transform(-1, 0, 0, 1, width, 0);
				break;
			case 3:
				ctx.transform(-1, 0, 0, -1, width, height);
				break;
			case 4:
				ctx.transform(1, 0, 0, -1, 0, height);
				break;
			case 5:
				ctx.transform(0, 1, 1, 0, 0, 0);
				break;
			case 6:
				ctx.transform(0, 1, -1, 0, height, 0);
				break;
			case 7:
				ctx.transform(0, -1, -1, 0, height, width);
				break;
			case 8:
				ctx.transform(0, -1, 1, 0, 0, width);
				break;
			default:
				ctx.transform(1, 0, 0, 1, 0, 0);
			}

			ctx.drawImage(img, 0, 0);
			
			return canvas.toDataURL('image/jpeg');
		}
	}
});

// 相册列表
Vue.component('attachment-picture-list', {
	props : {
		picCtx : String,
		uploadUrl : String,
		blankBg : String,
		delImgUrl : String,
		loadListUrl: String
	},
	data : function() {
		return {
			pics: []
		};
	},
	template : '<table width="100%"><tr><td>\
				<div class="label">相册图：</div>\
				<ul>\
					<li v-for="pic in pics" style="float:left;margin-right:1%;text-align:center;">\
						<a href="picCtx + pic.path" target="_blank"><img :src="picCtx + pic.path" style="max-width: 180px;max-height: 160px;" /></a><br />\
						<a href="#" @click="delPic(pic.id);">删 除</a>\
					</li>\
				</ul>\
			</td><td>\
			<aj-xhr-upload ref="attachmentPictureUpload" :action="uploadUrl" :is-img-upload="true" :img-place="blankBg"></aj-xhr-upload>\
			</td></tr></table>',
	mounted: function() {
		this.loadAttachmentPictures();
		this.$refs.attachmentPictureUpload.uploadOk_callback = this.loadAttachmentPictures;
	},
	methods : {
		loadAttachmentPictures : function() {
			var self = this;
			aj.xhr.get(this.loadListUrl, function(json) {
				self.pics = json.result;
			});
		},
		delPic: function(picId) {
			var self = this;
			aj.xhr.dele(this.delImgUrl + picId, function(json) {
				if(json.isOk) {
					self.loadAttachmentPictures();
				} else {
					
				}
			});
		}
	}
});