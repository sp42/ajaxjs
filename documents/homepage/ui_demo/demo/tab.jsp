<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "tab"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
    	<script src="${bigfoot}/js/libs/touch.js"></script>
    	<script src="${bigfoot}/js/widget/tab.js"></script>
    	<br />

    	<div class="p">
    	 Tab 控件, 支持以下特性：
    	 <ul>
    	 	<li>CSS3 动画加速</li>
    	 	<li>自动适应宽度（使用百分比单位）</li>
    	 	<li>可强制绝对单位（使用像素单位）</li>
    	 	<li>自动计算 tab 内容高度</li>
    	 	<li>TODO：SimpleTab 有 afterRender（只是在第一次触发）事件，但这个 tab 却没有</li>
    	 </ul>
    	</div>
    	<br />
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
					<br /> 台灣小男孩手持飲料罐逛畫展，不小心跌倒撞壞17世紀畫作的新聞，引起義大利高度關注。<br />
					<br /> 多數義大利媒體引述主辦單位說法，指出被壓壞的作品「花」是拿波里畫家波爾波拉（Paolo
					Porpora）的作品，價值達150萬美金。<br />
					<br /> 不過，義大利「共和報」在網站上公布的影片和照片中指出，毀損的畫作是努濟（Mario
					Nuzzi）在1660年的作品，價值約3萬歐元。<br />
				</div>
				<div>
					<div>
						義大利網路媒體「郵報」（Il Post）也指出，努濟才是畫作「花」的作者，而至今主辦單位尚未解釋，為何一開始錯標畫家名字。<br />
						<br /> 努濟1603年出生於羅馬，1673年逝世，以花卉繪畫見長，因此也被稱為「花的馬力歐」（Mario
						de’Fiori），有專家認為，1617年出生在拿波里的波爾波拉，也受到這位前輩的影響。<br />
						<br /> 除了花卉，波爾波拉也擅長描繪動物，青蛙、魚和雞都是他展現寫實和華麗裝飾手法的靜物畫對象。
	    			</div>
				</div>
				<div>
					aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
				</div>
    		</div>
    	</div>

    	<script>
			var tab = Object.create(bf_tab);
			tab.el = document.querySelector('.tab');
			tab.init();
			tab.initFirstTab(); // 点击第一个 tab，否则不会高亮 tab 1 按钮和自动计算高度
    	</script>
    	<br />
	<div class="p">
		<h3>依赖 js：</h3>
		<ul>
			<li>/js/widget/tab.js</li>
			<li>/js/libs/touch.js 如果需要触控的话</li>
		</ul>
	</div>
	<div class="p">
		<h3>注意事项：</h3>
		<ul>
			<li> </li>
		</ul>
	</div>
	<div class="p">
		<h3>代码如下：</h3>
	</div>
 
	<pre class="prettyprint">
&lt;div class=&quot;tab&quot;&gt;
	&lt;header&gt;
		&lt;ul&gt;
			&lt;li&gt;Tab 1&lt;/li&gt;
			&lt;li&gt;Tab 2&lt;/li&gt;
			&lt;li&gt;Tab 3&lt;/li&gt;
		&lt;/ul&gt;
	&lt;/header&gt;
	&lt;div&gt;
		&lt;div&gt;
			Tab1
		&lt;/div&gt;
		&lt;div&gt;
			Tab2
		&lt;/div&gt;
		&lt;div&gt;
			Tab3
		&lt;/div&gt;
	&lt;/div&gt;
&lt;/div&gt;

&lt;script&gt;
	var tab = Object.create(bf_tab);
	tab.el = document.querySelector(&#x27;.tab&#x27;);
	tab.init();
	tab.initFirstTab(); // 点击第一个 tab，否则不会高亮 tab 1 按钮和自动计算高度
&lt;/script&gt;
	   </pre>
    </body>
</html>