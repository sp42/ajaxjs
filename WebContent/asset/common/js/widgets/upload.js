/*
 * --------------------------------------------------------
 * 图片上传预览
 * --------------------------------------------------------
 */
ajaxjs_Upload_perview = function(perviewImg, uploadInput, maxSize) {
	uploadInput.addEventListener('change', function(perviewImg_event) {
		var filepacker = perviewImg_event.target;

		if (!checkImgByExtName(filepacker.value)) {
			alert('根据文件后缀名判断，此文件不是图片');
			return;
		}

		if (uploadInput.files && uploadInput.files[0]) {
			var reader = new FileReader();
			reader.onload = function(evt) {
				if (!checkImgByBin(evt.target.result)) {
					alert('亲，改了扩展名我还能认得你不是图片哦');
					uploadInput.value = '';
					return;
				}
				perviewImg.src = evt.target.result;
			}

			var file = filepacker.files[0], fileSize = file.size; // 文件的大小，单位为字节B

			var maxSize = 600;

			if (fileSize > maxSize * 1024) {
				alert("提示信息\n上传图片过大，请将图片压缩到 " + maxSize + "kb 以下");
				uploadInput.value = '';
				return;
			}

			reader.readAsDataURL(file);
		} else {
			// ie@todo
		}
	});

	var MAXWIDTH = 300, MAXHEIGHT = 150;

	perviewImg.onload = function(e) {
		var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, perviewImg.offsetWidth, perviewImg.offsetHeight);
		perviewImg.width = rect.width;
		perviewImg.height = rect.height;
		// img.style.marginLeft = rect.left+'px';
		// img.style.marginTop = rect.top+'px';
	}

	/**
	 * 扩展名的检测，看看是否为图片
	 * 
	 * @return false = 你选择的不是图片哦
	 */
	function checkImgByExtName(filename) {
		var ext = filename.split('.').pop();
		return /png|gif|jpg|jpeg/i.test(ext);
	}

	/**
	 * 文件头判别，看看是否为图片
	 */
	function checkImgByBin(base64_str) {
		for ( var i in checkImgByBin.header) {
			if (~base64_str.indexOf(checkImgByBin.header[i]))
				return true;
		}
		return false;
	}

	checkImgByBin.header = {
		"jpeg" : "/9j/4",
		"gif" : "R0lGOD",
		"png" : "iVBORw"
	};

	function sizeLimit(img, maxSize) {
		if (img.width > maxSize.width || img.height > maxSize.height) {
			alert('图片大小尺寸不符合要求哦，请重新图片吧~');
			return false;
		}

		return true;
	}

	/*
	 * 实现本地图片等比例缩放预览
	 * 
	 */
	function clacImgZoomParam(maxWidth, maxHeight, width, height) {
		var param = {
			top : 0,
			left : 0,
			width : width,
			height : height
		};
		if (width > maxWidth || height > maxHeight) {
			rateWidth = width / maxWidth;
			rateHeight = height / maxHeight;

			if (rateWidth > rateHeight) {
				param.width = maxWidth;
				param.height = Math.round(height / rateWidth);
			} else {
				param.width = Math.round(width / rateHeight);
				param.height = maxHeight;
			}
		}

		param.left = Math.round((maxWidth - param.width) / 2);
		param.top = Math.round((maxHeight - param.height) / 2);
		return param;
	}
}