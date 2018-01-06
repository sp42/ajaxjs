<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "跑马灯");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>

	<h4>Banner 控件</h4>
	
	<p>Banner</p>
	<ul class="list center">
		<li>继承自 Tab 控件</li>
		<li>可支持手势切换、点击小圆点可以切换</li>
		<li>可自定义 indicator 样式</li>
		<li>该控件须引用 list.js</li>
	</ul>

	<section class="banner center">
		<div>
			<div>
				<a href="topic/?id=283"> <img
					src="http://imgu.3gtv.net:9090/_file/section/20150906113629683.jpg">
				</a>
			</div>

			<div>
				<a href="topic/?id=341"> <img
					src="http://imgu.3gtv.net:9090/_file/section/20150828090747132.jpg">
				</a>
			</div>

			<div>
				<a href="live/?id=3"> <img
					src="http://imgu.3gtv.net:9090/_file/section/20150906125934797.png">
				</a>
			</div>

			<div>
				<a href="album/?id=4805"> <img
					src="http://imgu.3gtv.net:9090/_file/section/20150826162352938.jpg">
				</a>
			</div>
		</div>
		<ol class="indicator">
			<li>0</li>
			<li>1</li>
			<li>2</li>
			<li>3</li>
		</ol>
	</section>

	<script>
		var banner = Object.create(ajaxjs.Banner);
		banner.el = document.querySelector('.banner');
		banner.init();
		banner.loop();
		banner.initIndicator();
	</script>

	<p>代码如下：</p>
	<pre class="prettyprint">
&lt;section class=&quot;banner&quot;&gt;
	&lt;div&gt;
		&lt;div&gt;
			&lt;a href=&quot;topic/?id=283&quot;&gt;
				&lt;img src=&quot;http://imgu.3gtv.net:9090/_file/section/20150906113629683.jpg&quot;&gt;
			&lt;/a&gt;
		&lt;/div&gt;
	
		&lt;div&gt;
			&lt;a href=&quot;topic/?id=341&quot;&gt;
				&lt;img src=&quot;http://imgu.3gtv.net:9090/_file/section/20150828090747132.jpg&quot;&gt;
			&lt;/a&gt;
		&lt;/div&gt;
	
		&lt;div&gt;
			&lt;a href=&quot;live/?id=3&quot;&gt;
				&lt;img src=&quot;http://imgu.3gtv.net:9090/_file/section/20150906125934797.png&quot;&gt;
			&lt;/a&gt;
		&lt;/div&gt;
	
		&lt;div&gt;
			&lt;a href=&quot;album/?id=4805&quot;&gt;
				&lt;img src=&quot;http://imgu.3gtv.net:9090/_file/section/20150826162352938.jpg&quot;&gt;
			&lt;/a&gt;
		&lt;/div&gt;
	&lt;/div&gt;
    &lt;ol class=&quot;indicator&quot;&gt; 
		&lt;li&gt;0&lt;/li&gt;
		&lt;li&gt;1&lt;/li&gt;
		&lt;li&gt;2&lt;/li&gt;
		&lt;li&gt;3&lt;/li&gt;
    &lt;/ol&gt;
&lt;/section&gt;

&lt;script&gt;
	var banner = Object.create(bf_banner);
	banner.el = document.querySelector(&#x27;.banner&#x27;);
	banner.init();
	banner.loop();
	banner.initIndicator();
&lt;/script&gt;
	   </pre>
	<p>样式如下：</p>
	<pre class="prettyprint">
.banner{
	.tab;
	position:relative;
	margin-bottom: 15px;
	min-height: 140px;
	width:98%;
	.centerLimitWidth();
	max-height:280px; // 防止加载中扯高容器
	&>div>div img{
		width:100%;
	}
	ol{
		background:rgba(0, 0, 0, .5);
		width:100%;
		margin:0;
		padding:0;
		padding-right:60px;
		height:18px;
		line-height:25px;
		position:absolute;
		bottom:0;
		left:0;
		text-align:right;
		li{
			list-style-type:none;
			height:5px;
			width:5px;
			display:inline-block;
			margin:6px 3px;
			color:white;
			text-indent:99em;
			background:rgba(255, 255, 255, .5);
			cursor: pointer;
			&:last-child{
				margin-right: 20px;
			}
			&.active{
				background:white;
			}
		}
	}
}
	    </pre>
	    
	<h4>Lightbox</h4>
	<p>TODO</p>
	    
	<div class="imgList_1">
		<ul class="imgList"></ul>
		<center>
			<button class="loadMoreBtn">加载更多</button>
		</center>
	</div>

	<br />
	<br />

	<div class="imgList_2">
		<ul class="imgList"></ul>
		<center>
			<button class="loadMoreBtn">加载更多</button>
		</center>
	</div>

	<script>
		function call_lightbox(galleryId, imgList_el) {
			var baseUrl = "http://u1.3gtv.net:2080/pms-service";
			var imgList_holder = document.querySelector('.' + imgList_el), imgList = imgList_holder
					.querySelector('.imgList');

			// http://u1.3gtv.net:2080/pms-service/gallery/gallery_pic_list?galleryId=4804&portalId=5&start=0&limit=5&callBack=$$_jsonp.globalMethod_191585
			ajaxjs.List(baseUrl + '/gallery/gallery_pic_list', imgList, {
				portalId : 5,
				galleryId : galleryId,
				start : 0,
				limit : 5
			}, {
				loadMoreBtn : imgList_holder.querySelector('.loadMoreBtn'),
				isNoAutoHeight : false, // 海报 col3 需要等高
				tpl : document.querySelector('.indexTab_ranking_tpl').value,
				pager : true, // 是否需要分页
				pageSize : 6
			// 海报 col3 读9条

			});

			var lightboxTpl = '<div class="lightbox_Container">\
    			    <ul></ul>\
    			</div>';
			var galleryTitle = '', galleryId = 9;

			function initImgs(el, frag) {
				var ul = frag.querySelector('ul');

				// collect imgs
				el.up('ul').eachChild('img', function(img, i, j) {
					var li = document.createElement('li');
					//    					var img = img.cloneNode();
					//    					img.src = img.src.replace('?w=250', '');
					//    					li.appendChild(img.cloneNode());
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
			function getIndex_In_Dom(imgEl) {
				// 这里跟 dom 结构有关系
				var li = imgEl.parentNode.parentNode.parentNode, children = imgEl
						.up('ul').children;

				for (var index = 0, j = children.length; index < j; index++) {
					if (li == children[index])
						break;
				}

				return index;
			}

			imgList.onclick = function(e) {
				var el = e.target;
				if (el.tagName != 'IMG')
					return;

				var frag = document.createElement('div');
				frag.innerHTML = lightboxTpl;

				var ul = initImgs(el, frag);
				var carouselEl = frag.querySelector('div');
				document.body.appendChild(carouselEl);

				var carouselEl = document.querySelector('.lightbox_Container');
				var obj = Object.create(ajaxjs.Tab.Lightbox);
				//obj.isDirectShow = false;
				obj.el = carouselEl;

				var index = getIndex_In_Dom(el);
				obj.init();
				obj.go(index);
			}
		}
	</script>

	<textarea style="display: none;" class="indexTab_ranking_tpl">
			<li>
				<div class="box">
					<div class="imgHolder">
							<img data-src="[:=filePath:]?w=150" />
					</div>
				</div>
			</li>
		</textarea>
	<script>
		call_lightbox(4804, 'imgList_1');
		call_lightbox(4805, 'imgList_2');
	</script>
	<%@include file="../public/footer.jsp"%>
</body>
</html>