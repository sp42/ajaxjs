<%@page pageEncoding="UTF-8"%>
	<h4>Tab(with CSS3)</h4>
	<ul class="list">
		<li>CSS3 动画加速</li>
		<li>自动适应宽度（使用百分比单位）</li>
		<li>可强制绝对单位（使用像素单位）</li>
		<li>自动计算 tab 内容高度</li>
		<li>TODO：SimpleTab 有 afterRender（只是在第一次触发）事件，但这个 tab 却没有</li>
		<li>该控件须引用 <a href="js/ajaxjs-list.js">ajaxjs-list.js</a></li>
	</ul>
	<div class="tab">
		<header>
			<ul>
				<li>Tab 1</li>
				<li>Tab 2</li>
				<li>Tab 3</li>
			</ul>
		</header>
		<div>
			<div>
				「真相達文西‧天才之作特展」因男孩意外撞壞畫作引起國際關注。不過義大利「共和報」報導，被壓壞的畫作是出自努濟之手，價值約3萬歐元（約111萬台幣），並非身價上百萬的波爾波拉作品。<br />
				<br /> 台灣小男孩手持飲料罐逛畫展，不小心跌倒撞壞17世紀畫作的新聞，引起義大利高度關注。<br /> <br />
				多數義大利媒體引述主辦單位說法，指出被壓壞的作品「花」是拿波里畫家波爾波拉（Paolo Porpora）的作品，價值達150萬美金。<br />
				<br /> 不過，義大利「共和報」在網站上公布的影片和照片中指出，毀損的畫作是努濟（Mario
				Nuzzi）在1660年的作品，價值約3萬歐元。<br />
			</div>
			<div>
				<div>
					義大利網路媒體「郵報」（Il Post）也指出，努濟才是畫作「花」的作者，而至今主辦單位尚未解釋，為何一開始錯標畫家名字。<br />
					<br /> 努濟1603年出生於羅馬，1673年逝世，以花卉繪畫見長，因此也被稱為「花的馬力歐」（Mario
					de’Fiori），有專家認為，1617年出生在拿波里的波爾波拉，也受到這位前輩的影響。<br /> <br />
					除了花卉，波爾波拉也擅長描繪動物，青蛙、魚和雞都是他展現寫實和華麗裝飾手法的靜物畫對象。
				</div>
			</div>
			<div>如今我们的网站、页面更加需要注重细节，不论是字体的样式、还是图片的分辨率清晰度都会影响到用户的访问体验和PV，以及用户以后是否会回访我们的网站/博客。如果有时间的时候，老左也会浏览和阅读相关的前端网站和积累一些不错的前端资源，在"8款设计师常用漂亮的HTML
				CSS表格样式"中展示了几款不错的价格列表，在这篇文章中整理6个用户留言HTML CSS样式。</div>
		</div>
	</div>

	<script>
		var tab = Object.create(ajaxjs.Tab);
		tab.el = document.querySelector('.tab');
		tab.init();
		tab.initFirstTab(); // 点击第一个 tab，否则不会高亮 tab 1 按钮和自动计算高度
	</script>
	
	
	<h4>Banner</h4>
	<ul class="list">
		<li>继承自 Tab 控件</li>
		<li>可支持手势切换、点击小圆点可以切换</li>
		<li>可 resize 容器的时候调整大小</li>
		<li>可自定义 indicator 样式</li>
		<li>该控件须引用 <a href="js/ajaxjs-list.js">ajaxjs-list.js</a></li>
	</ul>

	<section class="banner">
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
	
	<h4>LightBox</h4>
	<p>TODO</p>