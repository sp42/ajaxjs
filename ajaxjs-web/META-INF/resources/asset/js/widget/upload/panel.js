;(function(){
	bf_section = function(){
		UserEvent.call(this);
		this.sections = [];
		this.addEvents('beforeSelect', 'select', 'selectAll');
	};
	bf_section.prototype = {
		select : function(toSelectData){
			if(this.fireEvent('beforeSelect', toSelectData) !== false){
				this.sections.push(toSelectData);
				this.fireEvent('select', toSelectData);
			}
		}
	};
	
	
    bf_uploadDialog.init = function(_el) {
    	this.sectionObj = new bf_section();
    	var dialog = new bf_dialog(document.querySelector('.upload_DialogTpl'));
//        dialog.onClose = this.onClose;
        dialog.show();

        this.el = dialog.el;
        var  el = dialog.el, innerPanel = el.querySelector('.innerPanel');        
        innerPanel.style.height = (innerPanel.parentNode.offsetHeight - 100) + 'px';

        return;
        // 单击选择、反选
        this.dataviewEl = el.$('.dataview');
        this.dataviewEl.$(onItemClick);
        // 全选按钮
        el.$('button.selectAll').$(selectAll.bind(this));
        // 删除按钮
        el.$('button.del').$(del.bind(this));
        // 刷新按钮
        el.$('button.refreash').$(this.refreashImgList.bind(this));
        // 插入按钮
        el.$click('button.insert2article', function(e){
            var el = e.target, selected = this.getSelected();

            if(!selected.length){
                $.alert('你未选择图片哦~');
                return;
            }

            var fileName = selected[0].parentNode.querySelector('h2').innerHTML;
            bf.htmlEditor.insert2article(fileName);
        }.bind(this));
        
        el.$click('button.insert2article_gallery', function(e){
        	var el = e.target, selected = this.getSelected();
        	
        	if(!selected.length){
        		$.alert('你未选择图片哦~');
        		return;
        	}
        	
        	var fileName = selected[0].parentNode.querySelector('h2').innerHTML;
        	bf.htmlEditor.insert2article('../public/gallery/' + fileName);
        }.bind(this));
        
        // 设置封面
        el.$click('button.cover', setCover.bind(this));

        // 预览
        ui.perviewImg(el.$('.preview input[type=file]'), el.$('.preview .perviewHolder img'));          

        if(this.baseParams)setParams(this.baseParams, el); // 设置 FORM Action 的 GET 参数。

        // 获取 上传的 委托 iframe 元素
        var formEl = el.querySelector('form'), targetiframe = formEl.target,
            iframeEl = document.querySelector('iframe[name=' + targetiframe + ']');
        
		// 接口在同一个 jsp 中
        // 因为 admin 外加一层 shell 的关系，因此不能用改？，否则 ？指向 shell所在的页面
		// 应该要显式声明 的url，即完整的 url
		if(/^\?/.test(this.createUrl)){
			formEl.action = location.origin + location.pathname + this.createUrl;
		}else{
			formEl.action = this.createUrl;
		}
        
        iframeEl.onload = afterUpload.bind(this);
        // why load 会注册多次？
        // iframeEl.on('load', afterUpload.bind(this)); // 该 iframe 返回 json，登记 load 事件获取 JSON 数据。
    }
    
    // 读取图片列表。url 接口地址，params 是参数对象
    bf_uploadDialog.loadImgs = function(url, params){
        params = params || {};
        for(var i in this.baseParams)params[i] = this.baseParams[i];

        this.lastRequest = { // 上次请求参数，都保存起来
            url : url,
            params : params,
            callback : loadImgs.bind(this)
        };
        
        XMLHttpRequest.get(url, params, this.lastRequest.callback);
    }
    
    // 刷新 图片列表
    bf_uploadDialog.refreashImgList = function(){
    	console.log('刷新 图片列表 中……');
    	$.get(this.lastRequest.url, this.lastRequest.callback, this.lastRequest.params);
    }
    
    function onItemClick(e){
        var el = e.target;
        if(~el.className.indexOf('m_selected')){
            e.preventDefault();
            el.parentNode.removeChild(el); // 不选
        }
        if(~el.className.indexOf('m_checkbox')){// 选中
            e.preventDefault();
            var selectedIcon = document.createElement('div');
            selectedIcon.className = 'm_selected';
            el.parentNode.appendChild(selectedIcon);    
        }
    }
    // 全选
    function selectAll(e){
        this.dataviewEl.$('.imgContainer', function(imgContainer){
            var hasSelected = imgContainer.$('.m_selected');
            if(!hasSelected){
                var selectedIcon = document.createElement('div');
                selectedIcon.className = 'm_selected';
                imgContainer.appendChild(selectedIcon);                            
            }
        });
    }

    function setCover(e){
      	var el = e.target, selected = this.getSelected();
    	
    	if(!selected.length){
    		$.alert('你未选择图片哦~');
    		return;
    	}
    	
    	var fileName = selected[0].parentNode.querySelector('h2').innerHTML;
    	
    	if(!this.setCoverUrl)throw '未设置封面 url！';
    	
    	var id = getId(selected[0]);
    	bf.data.put(this.setCoverUrl,{
    		cover : id // 修改字段
    	}, function(json){
    		if(json.isOk){debugger;
    			$.msg("已设置{0}为封面。".format(fileName));
    		}else{
    			$.alet("设置封面失败！");
    		}
    	});
    }
    
    /**
     * 获取保存在 DOM 中的图片 id
     */
    function getId(el){
    	return el.parentNode.className.match(/_(\d+)/).pop();
    }
    function del(e){
        var el = e.target, selected = this.getSelected();

        if(!selected.length){
            $.alert('你未选择图片哦~');
            return;
        }

        var _request = require('bigfoot/dhtml/request');
        for(var i = 0, j = selected.length; i < j; i++){
            var li = selected[i].up('li');

            var id = li.$('.imgContainer');
            id = id.className.match(/imgId_([^ ]*)/i);
            id = id[1];

            
			var xhr = Object.create(_request.xhr);
			
			xhr.url = this.delUrl + '/' + id;
			xhr.httpMethod = 'DELETE';
			xhr.onSuccuess = function(json, b, c, li){
                if(json.isOk){
                	li.parentNode.removeChild(li);
                }else{
                	$.msg('删除图片失败!');
                }
            }.delegate(null, null, null, li);
			xhr.request();
			
//            var params = {};
//            params['id'] = id;
//            $.get(this.delUrl, function(json, li){
//                // $.msg('删除图片成功!');
//                console.log(json);
//                li.parentNode.removeChild(li);
//            }.delegate(null, li), params);
        }
    }
    function upload_Callback(data){
        if(!data.isOk){
            alert('上传失败！原因：' + data.msg);
            return;
        }
        var ul = $.call(this.dataviewEl, 'ul'),
            li = document.createElement('LI'),
            filename = data.result, filename = filename[0], 
            filename = filename.split('\\'), filename = filename.pop();

            li.innerHTML = '<div class="imgContainer">\
                <img src="/mmc/upload/{0}">\
                <h2>{0}</h2>\
                <div class="m_checkbox"></div>\
            </div>'.format(filename);

            ul.appendChild(li);
    }

    // 设置 上传 POST 的参数。因为 上传 的缘故，所以不能 POST 字段，但可以 GET 的方式提交参数
    function setParams(param, dialogEl){
        var formEl = $.call(dialogEl, 'form');
        var arr = [];
        for(var i in param)
            arr.push(i + '=' + param[i]);

        var params = ~formEl.action.indexOf('?') ? arr.join('&') : '?' + arr.join('&');
        formEl.action += params;
    }

    var imgEl_tpl =     
        '<tpl><li>\
            <div class="imgContainer imgId_{id}">\
                <img src="{0}/{fileName}">\
                <h2>{fileName}</h2>\
                <div class="m_checkbox"></div>\
            </div>\
        </li></tpl>';

    // the callback after xhr
    function loadImgs(json){
        if(json.total == 0){
            console.log('上传数据为零');
        }else{
        	bf_list(url, el, args, config);
            $.databinding_List(imgEl_tpl.format(this.urlPrefix), json.result, this.el.$('.dataview>ul'));
        }
    }

    function afterUpload(e){
		// https://bugzilla.mozilla.org/show_bug.cgi?id=674186
    	// FF 带标签 sucks
        var json = navigator.isFF ? e.target.contentDocument.body.textContent 
        						  : e.target.contentDocument.body.innerHTML;
		
        if(json.indexOf('<pre') == 0){
			json = json.replace(/<pre[^>]*>/i,'').replace(/<\/pre>/i, '');
		}
        json = JSON.parse(json);
        
        if(json.isOk){
            $.alert(json.fileName + '上传成功');
            // 加入 一张 图片
            var ul = this.el.$('.dataview>ul'),
            	li = document.createElement('li');
            
            json.id = json.newlyId;
            
            li.innerHTML = imgEl_tpl.replace(/<tpl>|<\/tpl>|<li>|<\/li>/ig, '').format(json).format(
            		this.urlPrefix
            );
            ul.appendChild(li);
        }else $.alert('上传失败！');
    }
})();