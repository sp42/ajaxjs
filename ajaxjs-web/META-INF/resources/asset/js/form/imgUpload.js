;
(function() {
	// 

	/**
	 * 扩展名的检测，看看是否为图片
	 * @return false = 你选择的不是图片哦
	 */
	function checkImgByExtName(filename) {
		var ext = filename.split('.').pop();
		return !/png|gif|jpg/i.test(ext);
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
	

	function sizeLimit(img, maxSize) {
		if (img.width > maxSize.width || img.height > maxSize.height) {
			alert('图片大小尺寸不符合要求哦，请重新图片吧~');
			return false;
		}

		return true;
	}

	checkImgByBin.header = {
		"jpeg" : "/9j/4",
		"gif" : "R0lGOD",
		"png" : "iVBORw"
	};

	function loadPerview(e, perviewImg) {
		var filepacker = e.target;

		if (!checkImgByExtName(filepacker.value))
			return;

		if (filepacker.files && filepacker.files[0]) {
			var reader = new FileReader();
			reader.onload = function(evt) {
				if (!checkImgByBin(evt.target.result)) {
					alert('亲，改了扩展名我还能认得你不是图片哦');
					return;
				}
				perviewImg.src = evt.target.result;
			}
			reader.readAsDataURL(filepacker.files[0]);
		} else {
			// ie@todo
		}
	}
	filepacker.on('change', loadPerview.delegate(null, perviewImg));
})();