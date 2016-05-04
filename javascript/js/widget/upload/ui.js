;(function(){
	// 获取选中
	bf_uploadDialog = {};
    bf_uploadDialog.getSelected = function(){
        var m_selecteds = [];
        
        this.dataviewEl.query('.m_selected')(function(m_selected){
            m_selecteds.push(m_selected);
        });
        return m_selecteds;
    };
 
 	// 本地预览
    bf_uploadDialog.perviewImg = function(filepacker, perviewImg){
	    filepacker.on('change', loadPerview.delegate(null, perviewImg));
    }

    // 扩展名的检测，看看是否为图片
    bf_uploadDialog.checkIfImgByExtName = function(filename){
        var extFileName = filename.split('.').pop();
        
        if(!/png|gif|jpg/i.test(extFileName)){
        	$.alert('亲~你选择的不是图片哦~');
        	return false;
        }else return true;
    }

    // 上传按钮事件
    bf_uploadDialog.onUploadBtnClk = function (e){
		e.preventDefault();

		var btn = e.target, imgUpload = btn.parentNode,
			inputFileEl = imgUpload.querySelector('input[type=file]'),
			perviewEl 	= imgUpload.querySelector('.perview');

        if(!inputFileEl.value){
        	$.alert('你未选择图片！');
        	return;
        }

        if(!bf_uploadDialog.checkIfImgByExtName(inputFileEl.value))return;
    
		// 插入新的
		var newInputFileEl = inputFileEl.cloneNode(true);
		
		bf_uploadDialog.perviewImg(newInputFileEl, imgUpload.querySelector('.perview img')/* 还是那个 imgEl */);
		
		// imgUpload.insertBefore(newInputFileEl, perviewEl);
		imgUpload.replaceChild(newInputFileEl, inputFileEl);

        var uploadIframe = document.querySelector('.uploadIframe');
        if(!uploadIframe)throw 'HTML uploadIframe 未准备好！';

        // 清除旧有的 文件上传域
        var inputFileEl_old = uploadIframe.querySelector('input[type=file]');	
        inputFileEl_old && uploadIframe.removeChild(inputFileEl_old);
        
        // debugger;
        if(!inputFileEl.name)inputFileEl.name="dd";

        // for 指向上传图片的字段域 <input type="hidden" /> 被安排在 按钮中
        var _for = btn.getAttribute('for');
        uploadIframe.action = uploadIframe.action.replace(/for=.+/, 'for=' + _for);

        uploadIframe.appendChild(inputFileEl);	// 移动 真正的 文件上传域 到 隐藏的 iframe 中
        uploadIframe.submit();
	}

    function loadPerview(e, perviewImg){
        var filepacker = e.target;

        if(!bf_uploadDialog.checkIfImgByExtName(filepacker.value))return;

       	if (filepacker.files && filepacker.files[0]){
		    var reader = new FileReader();
		    reader.onload = function(evt){
		    	var base64_str = evt.target.result, isImg = false;

		    	for(var i in loadPerview_filters){
		    		if(~base64_str.indexOf(loadPerview_filters[i])){ // 文件头判别
		    			isImg = true;
		    			break;
		    		}
		    	}

		    	if(!isImg){
		    		$.alert('亲，改了扩展名我还能认得你不是图片哦');
		    		return;
		    	}else{
		    		perviewImg.src = base64_str;
		    	}
		    }
		    reader.readAsDataURL(filepacker.files[0]);
		}else{
			// ie@todo
		}
	}

    var loadPerview_filters = {
        "jpeg"  : "/9j/4",
        "gif"   : "R0lGOD",
        "png"   : "iVBORw"
    };

    // @todo 拖放文件
    // http://blog.csdn.net/bighead1026/article/details/12993145

	// http://blog.csdn.net/nhconch/article/details/7295456
	function previewImage(file, perviewHolder){
	    var MAXWIDTH  = 100, MAXHEIGHT = 100;
	    var div = perviewHolder || document.getElementById('perviewHolder');
	    if (file.files && file.files[0]){
	        var img = document.createElement('img');
	        div.innerHTML = '';
	        div.appendChild(img);

	    // img.onload = function(e){
	    //   var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
	    //   img.width = rect.width;
	    //   img.height = rect.height;
	    //   // img.style.marginLeft = rect.left+'px';
	    //   // img.style.marginTop = rect.top+'px';
	    // }
	    var reader = new FileReader();
	    reader.onload = function(evt){img.src = evt.target.result;}
	    reader.readAsDataURL(file.files[0]);
	  }else{ // ie
	    var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
	    file.select();
	    var src = document.selection.createRange().text;
	    div.innerHTML = '<img id="imghead">';
	    var img = document.getElementById('imghead');
	    img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
	    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
	    status =('rect:' + rect.top + ',' + rect.left +','+rect.width+','+rect.height);
	    div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;margin-left:"+rect.left+"px;"+sFilter+src+"\"'></div>";
	  }
	}

	function clacImgZoomParam(maxWidth, maxHeight, width, height){
	    var param = {
	        top : 0, left : 0, width : width, height : height
	    };
	    if (width > maxWidth || height > maxHeight){
	        rateWidth = width / maxWidth;
	        rateHeight = height / maxHeight;
	        
	        if(rateWidth > rateHeight){
	            param.width =  maxWidth;
	            param.height = Math.round(height / rateWidth);
	        }else{
	            param.width = Math.round(width / rateHeight);
	            param.height = maxHeight;
	        }
	    }
	    
	    param.left = Math.round((maxWidth -  param.width)  / 2);
	    param.top  = Math.round((maxHeight - param.height) / 2);
	    return param;
	}
 
})();