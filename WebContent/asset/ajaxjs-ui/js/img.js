

	perviewImg.onload = function(e) {
		var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, perviewImg.offsetWidth, perviewImg.offsetHeight);
		perviewImg.width = rect.width;
		perviewImg.height = rect.height;
		// img.style.marginLeft = rect.left+'px';
		// img.style.marginTop = rect.top+'px';
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
