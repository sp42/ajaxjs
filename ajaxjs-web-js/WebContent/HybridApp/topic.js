cross = {
	iPhoneIframe : false,
	map : {
		video	: '1',
		album	: '2',
		topic	: '3',
		live	: '4',
		article : '5',
		gallery : '6',
		ugc		: '7'
	},
	contentType : {
		1 : '视频',
		2 : '专辑',
		3 : '专题',
		4 : '直播',
		5 : '文章',
		6 : '图集',
		7 : '原创视频',
		11: 'url链接'
	}
};

cross.ClientType = {
	app : 0x10,	
	browser : 0x11
};

/**
 * 判断当前环境是哪种类型。
 * @return {String}
 */
function getClientType() {
	var ua =  navigator.userAgent,
		// SMC_Client 是所有客户端SMC_Client
		reg = /yuetv|yitv|tiyuhui|gdtv|SMC_Client|SMCClient/i; 
	
	// 提供一个额外的入口，方便调试
    if ((location.search.indexOf('isClient=true') != -1) || ua.indexOf('mode=client') != -1 || reg.test(ua)) {
        return cross.ClientType.app;
    } else {
        return cross.ClientType.browser;
    }
}

function appOpen(id, contentType, feeFlag) {
	if(!id || !contentType){
		alert('参数不足！');
		return;
	}
	
	if(!feeFlag)feeFlag = 0;
		
	try{
		if(navigator.userAgent.indexOf('iPhone') != -1) {
			if(!cross.iPhoneIframe){
				var iframe = document.createElement('iframe');
				iframe.style.display = 'none;';
				document.body.appendChild(iframe);

				cross.iPhoneIframe = iframe;
			}

			var json = {
				method : 'play', // 方法名称
				id : id,   // 实体 id
				contentType : contentType, // 实体类型 
				feeFlag: feeFlag // 是否免费
			};
			cross.iPhoneIframe.src = "smc://" + encodeURIComponent(JSON.stringify(json));
		}else{
			SmcAndroid.play(Number(contentType), Number(id), Number(feeFlag));
		}
	}catch(e){
		alert(e);
	}
}

function browserOpen(id, contentType, feeFlag) {
	if(!id || !contentType){
		alert('参数不足！');
		return;
	}
	
	if(!feeFlag)feeFlag = 0;
	
	switch(contentType){
		case 1:
			window.location.assign('../vod?id=' + id); 
		break;
		case 11: // 广告
			window.location.assign(data.advertUrl);
		break;
		default:
			console.log('id:' + id + ' contentType:' + contentType + ' feeFlag:' + feeFlag)
	}
}

/**
 * 分发到哪的跳转函数。
 * @type {Function}
 */
cross.handler = ({
	0x10 : appOpen,
	0x11 : browserOpen
})[getClientType()];


//--------------------------view-------------------
var global_config = {
	baseUrl : 'http://u1.3gtv.net:2080/pms-service',
	portalId : 45
};
global_config.url = global_config.baseUrl + '/section/content_list';

var forEach = [].forEach;
var lists = document.querySelectorAll('ul.list');

function renderList(list){
	// 由于使用了 百分比 单位，图片尺寸是会拉伸的，而且同时元素是不可见的（例如 tab 里面的时候，是隐藏的），
	// 隐藏的元素如果是百分比的话，是没有最终尺寸的。除非让其显示
	// 于是综上，应该尽量规避这个问题，否则图片尺寸会错乱。
	var isAutoload = list.hasAttribute('autoload') && list.getAttribute('autoload') == 'false';
	if(isAutoload) return;
	
	var args = {
		id: null,
		start : 0,
		limit : 5,
		portalId : global_config.portalId
	},cfg = {
		tpl : null,
		renderer : function rendererItem(data){
			data.createTime = data.createTime.substring(0, 10);
			return data;
		}
	};
	
	/**
	 * 输入参数，还原它的primitive值。有点类似 eval() 的作用，却不使用 eval()。
	 * @param  {Mixed} v
	 * @return {Mixed}
	 */
	function getPrimitives(v){
		if(v){
			if(v == 'true' ) return true;
			if(v == 'false') return false;
			if(v.toString() == (new Date(v)).toString()) return new Date(v); // v is a date but in Stirng Type
	      	if(v == String(Number(v))) return Number(v);
		}
	  	return v;
	}
	
	// 读取属性
	forEach.call(list.attributes, function(item) {
		var name = item.name, v = item.value;
		 console.log(item.name + ': '+ item.value);
		if(name == 'id' || name == 'style') {
			return;
		} else if(name == 'class'){
			if(~v.indexOf('col_0')){
				cfg.tpl = '<li>\
					<a href="javascript:handler({id:{id},contentType:{contentType}});">{name}<div class="createTime">{createTime}</div></a>\
				</li>';
				list.addCls('simpleList'); // 添加样式 
			} else if(~v.indexOf('col_1')){
				cfg.tpl =   '<li>\
								<a href="javascript:cross.handler({id}, {contentType}, {feeFlag});">\
									<div class="imgHolder noImg">\
										<img data-src="{horizontalPic}" onload="this.addCls(\'tran\')" />\
										<div class="icon {icon}"></div>\
									</div>\
									<h4>{name}</h4>\
									<p>{createTime}</p>\
								</a>\
							</li>';
				list.addCls('simpleList1'); // 添加样式
			} else if(~v.indexOf('col_2') || ~v.indexOf('col_3')) {
				cfg.tpl = '<li>\
					<a href="javascript:cross.handler({id}, {contentType}, {feeFlag});">\
					<div class="box">\
						<div class="imgHolder">\
							<img data-src="{horizontalPic}?w=150" onload="this.addCls(\'tran\')" />\
						</div>\
						<h4>{name}</h4>\
					</div>\
					</a>\
				</li>'; 
				list.addCls(~v.indexOf('col_2') ? 'simpleList2' : 'simpleList3'); // 添加样式
			} else if(~v.indexOf('gallery')){ // 相册
				cfg.tpl = '<li>\
					<div class="box">\
						<div class="imgHolder">\
							<img data-src="{filePath}?w=150" onload="this.addCls(\'tran\')" />\
						</div>\
					</div>\
				</li>';
				list.addCls('simpleList2'); // 添加样式
			}
		} else if(name == 'sid') {
			args.id = v;
		} else if(name == 'pagesize') {
			args.limit = v;
			cfg.pageSize = v;
		} else if(name == 'args'){
			// todo 
		} else {
			// cfg
			if(name == 'pager' && v == 'true'){
				cfg.pager = true;
				// 在 ul 旁边插入按钮
				var pageBtn = document.createElement('button');
				pageBtn.addCls('longBtn center');
				pageBtn.innerHTML = '下一页';
				list.insertAfter(pageBtn);
				
				cfg.loadMoreBtn = pageBtn;
			}else if(name == 'is_append'){
				cfg.isAppend = getPrimitives(v);
			}else{
				cfg[name] = v;
			}
		}
	});
	if(list.className.indexOf('gallery') != -1){
		args.galleryId = args.id;
		bf_list(global_config.baseUrl + '/gallery/gallery_pic_list', list, args, cfg);
	}else{
		bf_list(global_config.url, list, args, cfg);
	}
}
forEach.call(lists, renderList);
 
// tab
forEach.call(document.querySelectorAll('div.staticTab'), function(staticTab){
	var tab = new SimpleTab(staticTab);
	tab.afterRender = function(i, btn, tab){
		var ul = tab.querySelector('ul[autoload=false]');
		if (ul) {
			ul.setAttribute('autoload', 'true');
			renderList(ul);
		}
	}
	tab.jump(0);
});

//-------------------Gallery------------------------------
var lightboxTpl =  
	'<div class="lightbox_Container">\
	    <ul></ul>\
	</div>';
var galleryTitle = '', galleryId = 9;
function initImgs(el, frag){
	var ul = frag.querySelector('ul');
	
	// collect imgs
	el.up('ul').$('img', function(img, i, j){
		var li = document.createElement('li');
//			var img = img.cloneNode();
//			img.src = img.src.replace('?w=250', '');
//			li.appendChild(img.cloneNode());
		var newlyImg = document.createElement('img');
		newlyImg.src = img.src.replace(/\?w=\d+/, '');
		li.appendChild(newlyImg);
		var title = document.createElement('h2');
		title.innerHTML = '图集 » ' + galleryTitle;
		li.appendChild(title);
		
		var picNo = document.createElement('div');
		picNo.className = 'picNo';
		picNo.innerHTML = (i + 1) + "/" + j.length;
		li.appendChild(picNo);
		ul.appendChild(li);
	});
	
	return ul;
}

// 获取 选中的 图片在 DOM 中的  index
function getIndex_In_Dom(imgEl){
	// 这里跟 dom 结构有关系
	var li = imgEl.parentNode.parentNode.parentNode, children = imgEl.up('ul').children;
	
	for(var index = 0, j = children.length; index < j; index++){
		if(li == children[index])break;
	}
	
	return index;
}

forEach.call(document.querySelectorAll('ul.gallery'), function(galleryEl){
	galleryEl.onclick = function(e){
		var el = e.target;
		if(el.tagName != 'IMG')return;
		
		var frag = document.createElement('div');
		frag.innerHTML = lightboxTpl;

		var ul = initImgs(el, frag);
		var carouselEl = frag.querySelector('div');
		document.body.appendChild(carouselEl);
		
		var carouselEl = document.querySelector('.lightbox_Container');
		var obj = Object.create(bf_lightbox);
		//obj.isDirectShow = false;
		obj.el = carouselEl;
	    
		var index = getIndex_In_Dom(el);
		obj.init();
		obj.go(index);
	}
});