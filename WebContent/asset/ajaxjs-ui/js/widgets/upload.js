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



