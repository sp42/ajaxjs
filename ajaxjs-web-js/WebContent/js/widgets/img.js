aj.img = (function() {
	function dataURLtoBlob(dataurl) {
		var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1], bstr = atob(arr[1]), len = bstr.length, u8arr = new Uint8Array(len);
		while (len--)
			u8arr[len] = bstr.charCodeAt(len);
		
		return new Blob([ u8arr ], {
			type : mime
		});
	}
	
	return {	
		/**
		 * image转canvas：图片地址
		 */
		imageToCanvas(imgUrl, cb, isCovernt2DataUrl) {
			var img = new Image();
			img.onload = () => {
				var canvas = document.createElement('canvas');
				canvas.width = img.width;
				canvas.height = img.height;
				canvas.getContext('2d').drawImage(img, 0, 0);
				
				if(isCovernt2DataUrl) {
					cb(canvas.toDataURL(isCovernt2DataUrl.format || 'image/jpeg', isCovernt2DataUrl.quality || .9));
				} else {
					cb(canvas);
				}
			}
			img.src = imgUrl;
		},
		
		imageElToBlob(imgUrl, cb) {
			this.imageToCanvas(imgUrl, (canvas) => {
				cb(dataURLtoBlob(canvas));
			}, true)
		},

		/**
		 * 改变blob图片的质量，为考虑兼容性
		 * 
		 * @param blob 图片对象
		 * @param callback 转换成功回调，接收一个新的blob对象作为参数
		 * @param format  目标格式，mime格式
		 * @param quality 介于0-1之间的数字，用于控制输出图片质量，仅当格式为jpg和webp时才支持质量，png时quality参数无效
		 */
		changeBlobImageQuality (blob, callback, format, quality) {
			format = format || 'image/jpeg';
			quality = quality || 0.9; // 经测试0.9最合适
			var fr = new FileReader();
			
			fr.onload = (e) => {
				var dataURL = e.target.result;
				var img = new Image();
				img.onload = () => {
					var canvas = document.createElement('canvas');
					var ctx = canvas.getContext('2d');
					canvas.width = img.width;
					canvas.height = img.height;
					ctx.drawImage(img, 0, 0);
					
					var newDataURL = canvas.toDataURL(format, quality);
					callback && callback(dataURLtoBlob(newDataURL));
					canvas = null;
				};
				
				img.src = dataURL;
			};
			
			fr.readAsDataURL(blob); // blob 转 dataURL
		}
	};
})();