<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "简易选项卡 Tab"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
		<br />
		<div class="p">
	    	<h3>简易选项卡 Tab</h3>
    	</div>
    	<div class="p">
    	 <ul>
    	 	<li>设有水平（常见）和垂直 Tab</li>
    	 	<li>切换原理为控制元素的 display:block/none;，原理比较简单</li>
    	 	<li>使用 display:block/none; 附带的好处是高度自适应</li>
    	 	<li>要将 Tab 边框样式无缝结合，需要 CSS 的一定技巧性</li>
    	 	<li>提供 beforeSwitch、afterSwitch、afterRender(i, btn, showTab)（只是在第一次触发）事件</li>
    	 </ul>
    	</div>
		<div class="p">
	    	<h3>垂直 Tab</h3>
    	</div>
    	<div class="center" style="padding:5px;">
			<div class="tab2">
				<ul>
					<li>公司简介</li>
					<li>资质证照</li>
					<li>荣誉证书</li>
				</ul>
				<div class="content">
					<div>
						自古以来，中华民族就善于运用契约进行交往。从杨国桢在《明清土地契约文书研究》一书中期待“中国契约学”研究以来，传统契约的论著、编著就不绝如缕。张传玺的煌煌三大册《中国历代契约粹编》，把中国的契约史追索到尚无无文字记载的时代;而他的《契约史买地券研究》，更系统地梳理了中国古代契约的源流关系和分类形式，从中可见，在周代就存在“万民约”与“邦国约”的分野。契约无论在官方，还是在民间，都获得过广泛的关注。至于王旭的《契纸千年——中国传统契约的形式与演变》，则把笔触落脚到近千年来我国丰富多样的契约之流变研究上。最近十年来，有关传统契约研究的论著可谓空前繁荣!
					</div>
					<div>
						之所以如此，端在于中国传统中存在着发达的契约。予谓不信，可看看早年由福建师范大学内部印行的两册本《明清福建经济契约文书选集》，中国社会科学出版社出版的《自贡盐业契约档案选集》，花山文艺出版社出版的共二十册的《徽州千年契约文书》;再看看近些年由安徽师范大学出版社出版的十卷本的《千年徽州契约文书集萃》，广西师范大学出版社出版的四辑共四十册《徽州文书》、三辑共三十册的《清水江文书》，民族出版社出版的多卷本《贵州清水江流域明清契约文书》，凤凰出版社出版的《敦煌契约文书辑校》，天津古籍出版社出版的《清代宁波契约文书辑校》，浙江大学出版社出版的《清代浙东契约文书辑选》……不难发现，中国传统契约文书的整理出版，完全可称为如雨后春笋般在迅速成长!
					</div>
					<div>
						笔者出于个人兴趣，在关注这些已经整理出版的、卷帙浩繁的契约文献的同时，也游走各地，或亲自搜集各类契约文书，或到一些地方档案馆查看其业已编辑成册、内部印行的传统契约文书，如在台湾宜兰、高雄，山东青岛、威海，贵州锦屏、从江等地档案机构或民间都见到过相关契约文献。记忆尤深的一次，是我和几位学生共游山东浮来山。在一处值班室里，居然发现有人以清代契约文本粘糊墙壁!足见只要我们稍加留意，在这个文明发展历经数千年的国度，不时可以发现一些令人称心的古代契约文献。
					</div>
				</div>
			</div>
			<div class="p">
		    	<h3>垂直 Tab</h3>
	    	</div>
			<div class="tab3">
				<ul>
					<li>找&nbsp;&nbsp;专&nbsp;&nbsp;线</li>
					<li>找&nbsp;&nbsp;货&nbsp;&nbsp;源</li>
					<li>物流追踪</li>
					<li>快递追踪</li>
				</ul>
				<div class="content">
					<div>
						指出我国传统契约之发达，是要藉此解构一个流传已久的观念：传统中国是个缺乏权利精神的国度。这种人云亦云的先入之间，让读书人沉沦于预设的结论，而对如此发达的契约，以及这些契约文书中明显透出的与义务相对的权利主张和事实，或者视而不见、充耳不闻，或者纵有见闻，也囿于既有成见而嗤之以鼻、不屑一顾。这种对传统契约的傲慢态度，实际上反映了一些学人的“反智”情结(尽管自以为很智)，其固执于某种所谓“先进”理念，而对一个国家的人民长期以来所形成的生活智慧、日用传统不但漠视，而且鄙弃。这里仍盛行的是某种“英雄史观”和精英传统，而不是现代国家所孜孜以求的民主史观和大众传统。
					</div>
					<div>
						以此来衡量，则关注传统契约，就是关注长期以来人民的日常生活方式，关注他们如何把日常生活的安排化成了一种精神产品——契约，关注在这种精神产品中所蕴含的权利和义务关系(如锦屏契约中不断出现的“地主”和“栽手”、卖主和买主之间的权利义务关系)。因此，权利义务关系之间有没有本位?如果有，究竟何者为本位?这些问题，绝非思想家高级精神生活中的苦思冥想;中国传统社会中的人民有没有权利精神和追求?这也绝非思想家高级精神生活中的指点江山。脱离开社会史和日常生活史、而专注于思想史的学术论调，终究属于精英、属于人文、属于高级精神生活，而不属于大众、不属于社科、不属于日常生活。
					</div>
					<div>
						本期刊出的三篇论文，来自同一课题组的成员在不同视角对《清水江文书整理与研究》这一课题在不同视角的论述。其中李亚的论文透过清水江习惯法，关注林业生产管理关系、特别是林权关系问题的研究;郑姝的论文透过对某农家收藏的800余份契约的分析，关注清水江流域林权关系变动的研究;而简丽的论文透过清水江流域当代乡规民约的研究，关注当地当代社会秩序制度化运行的事实。这些文章，虽都说不上理论的创新和深奥，但通过这些文字，展示了和我国既有法理学论述完全有别的古人或今人的权利观念和权利精神。
					</div>
					<div>
						本文将刊于《原生态民族文化学刊》2015年第3期。
					</div>
				</div>
			</div>
    	</div>
	
<script src="${bigfoot}/js/widget/tab.js"></script>
<script>
	new SimpleTab(document.querySelector('.tab2')).jump(0);
	new SimpleTab(document.querySelector('.tab3')).jump(0);
</script>
    	<br />

	<div style="clear:both;"></div>
	<div class="p">
		<h3>依赖 js：</h3>
		<ul>
			<li>/js/widget/tab.js</li>
		</ul>
	</div>
	<div class="p">
		<h3>注意事项：</h3>
		<ul>
			<li>垂直 Tab 的内容区域应该设置 min-height；或者固定高度</li>
			<li>创建实例后需要调用 jump(0); 加载第一个 tab 的内容，当前还不宜省去这一步</li>
		</ul>
	</div>
	<div class="p">
		<h3>代码如下：</h3>
	</div>
 
	<pre class="prettyprint">
&lt;div class=&quot;tab2&quot;&gt;
	&lt;ul&gt;
		&lt;li class=&quot;selected&quot;&gt;公司简介&lt;/li&gt;
		&lt;li&gt;资质证照&lt;/li&gt;
		&lt;li&gt;荣誉证书&lt;/li&gt;
	&lt;/ul&gt;
	&lt;div class=&quot;content&quot;&gt;
		&lt;div&gt;
			自古以来，……
		&lt;/div&gt;
		&lt;div&gt;
			之所以如此，……
		&lt;/div&gt;
		&lt;div&gt;
			笔者出于个人兴趣，……
		&lt;/div&gt;
	&lt;/div&gt;
&lt;/div&gt;

&lt;script src=&quot;${bigfoot}/js/widget/tab.js&quot;&gt;&lt;/script&gt;
&lt;script&gt;
	new SimpleTab(document.querySelector(&#x27;.tab2&#x27;));
&lt;/script&gt;
	   </pre>
    </body>
</html>