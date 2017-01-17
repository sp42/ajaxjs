function call_lightbox(galleryId, imgList_el){
	var baseUrl = "http://u1.3gtv.net:2080/pms-service";
	var imgList_holder = document.querySelector('.' + imgList_el),
		imgList = imgList_holder.querySelector('.imgList');
	
	// http://u1.3gtv.net:2080/pms-service/gallery/gallery_pic_list?galleryId=4804&portalId=5&start=0&limit=5&callBack=$$_jsonp.globalMethod_191585
	bf_list(baseUrl + '/gallery/gallery_pic_list', 
			imgList, 
			{
				portalId:  5,
				galleryId: galleryId,
				start:0,
				limit:5
			},
			{
				loadMoreBtn : imgList_holder.querySelector('.loadMoreBtn'),
				isNoAutoHeight : false, // 海报 col3 需要等高
				tpl : document.querySelector('.indexTab_ranking_tpl').value,
				pager : true, // 是否需要分页
				pageSize : 6 // 海报 col3 读9条
				
			}
		);
	var lightboxTpl =  
		'<div class="lightbox_Container">\
		    <ul></ul>\
		</div>';
	var galleryTitle = '', galleryId = 9;
	function initImgs(el, frag){
		var ul = frag.querySelector('ul');
		
		// collect imgs
		el.up('ul').eachChild('img', function(img, i, j){
			var li = document.createElement('li');
//				var img = img.cloneNode();
//				img.src = img.src.replace('?w=250', '');
//				li.appendChild(img.cloneNode());
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
		
	imgList.onclick = function(e){
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
}