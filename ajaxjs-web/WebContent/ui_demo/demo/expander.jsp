<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "折叠菜单"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
   
    	<div class="p">
	    	<h3>折叠菜单</h3>
	    	<p>折叠菜单的特点是同一时间只展开一个区域，其他区域则处于闭合状态。常用于展开正文详情。</p>
	    	 <ul>
	    	 	<li>控件代码仅 23 行</li>
	    	 	<li>提供过渡效果的图层，如果不需要把 div class="mask" 去掉即可</li>
	    	 	<li>代码在本页面寻找，没有整合到 js 库中</li>
	    	 </ul>
    	</div>
	    <div class="p">
		    <h3>例子</h3>
		</div>
 
		<div class="story min p">
			<p>
				母亲的早逝令灰姑娘辛德瑞拉再也感受不到母爱的温暖，失去了公主般的童年。后母和她的两个女儿，无时无刻不在想方设法折磨仙德瑞拉，给她糟糕的住处、做不完的家务。幸好得道多助、失道寡助，心地善良的辛德瑞拉结识了一大班朋友，老鼠、鸟儿、小狗都是守护在她周围的老友记。王子要在全城寻找王妃，仙德瑞拉用破旧零碎的布料赶做了一件礼服，当然这远远比不上心花怒放的姐姐们穿得艳丽。然而，灰姑娘得到了魔法相助，在神奇的法术中她美得倾城，坐着南瓜车来到王子的宫殿，与王子翩翩起舞。她知道，法术会在12点之前统统消失，大钟敲响的那一刻，她慌忙逃离王子，情陷已深的王子拿着灰姑娘不小心遗下的玻璃鞋，誓要从百万女子中找到一生中的最爱辛德瑞拉。
			</p>
			<p>
			在母亲离世后，艾拉的父亲再娶了一位继母。为了支持深爱的父亲，艾拉热情欢迎继母和她的两位女儿进入自己的家庭。然而当父亲也意外去世后，她才发现自己身陷嫉妒和折磨。艾拉被继母一家当作女仆对待，还被恶意称作“灰姑娘”。尽管遭受残忍的折磨，艾拉还是决心坚持母亲的遗言，“勇敢而善良地活下去”，因此她不向绝望投降，也不仇恨虐待她的人。后来，艾拉在树林里遇见了潇洒迷人的陌生人基特，她以为他是王宫的随从，却不知他竟是王子。随着王宫向全国少女发出舞会邀请，艾拉也希望自己能借机再见基特。但是当然，继母不允许她参加舞会，还撕碎了她的裙子。可就如所有美好童话故事里描绘的一样……神仙教母翩然而至，借用一只南瓜和一群小老鼠，永远改变了灰姑娘的人生。
			</p>
			<div class="mask"></div>
		</div>
		<div class="p">
			<a href="###" class="moreBtn">更多</a>
		</div>
		
		<script>
			function collapse(e, contentEl, maskEl, minHeight){
				var maskStyle = maskEl.style;
				if (maskStyle.display == '' || maskStyle.display == 'block'){
					maskStyle.display = 'none';
					contentEl.style.height = contentEl.scrollHeight + 'px';
					e.target.innerHTML = "收起";
				}else{
					maskStyle.display = 'block';
					contentEl.style.height = minHeight + 'px';
					e.target.innerHTML = "更多";
				}
			}
			
			function Expander(moreBtn){
				// 注意结构：按钮与 面板 .min 在 dom 是同级，
				var panel = moreBtn.parentNode.parentNode.querySelector('.min');
				var mask = panel.querySelector('.mask');
				
				setTimeout(function(){
					moreBtn.onclick = collapse.delegate(null, panel, mask, panel.clientHeight);
				}, 1000);
			}
			
			new Expander(document.querySelector('.moreBtn'));
		</script>

		<style>
			
			.min {
			 	height: 200px; /* 收缩高度，可指定 */
				overflow: hidden;
				-webkit-transition: height 200ms;
				transition: height 200ms;
				position: relative;
			}
			.mask {/* 过渡效果 */
				position: absolute;
				bottom: 0;
				left: 0;
				height: 23px;
				width: 100%;
				background: -webkit-gradient(linear, center top, center bottom, from(transparent), to(white));
			} 
			
			.p p{
				text-align: justify;
			}
		</style>
		

 
		<div class="p">
			<h3>代码如下：</h3>
		</div>
		<pre class="prettyprint">
function collapse(e, contentEl, maskEl, minHeight) {
	var maskStyle = maskEl.style;
	if (maskStyle.display == '' || maskStyle.display == 'block') {
		maskStyle.display = 'none';
		contentEl.style.height = contentEl.scrollHeight + 'px';
		e.target.innerHTML = "收起";
	}else{
		maskStyle.display = 'block';
		contentEl.style.height = minHeight + 'px';
		e.target.innerHTML = "更多";
	}
}

function Expander(moreBtn){
	// 注意结构：按钮与 面板 .min 在 dom 是同级，所以要注意按钮所在位置，需要定位 parentNode
	var panel = moreBtn.parentNode.parentNode.querySelector('.min');
	var mask = panel.querySelector('.mask');
	
	setTimeout(function(){
		moreBtn.onclick = collapse.delegate(null, panel, mask, panel.clientHeight);
	}, 500);
}

new Expander(document.querySelector('.moreBtn'));
		</pre>	
		<div class="p">
			<h3>样式如下：</h3>
		</div>
		<pre class="prettyprint">
.min {
 	height: 200px; /* 收缩高度，可指定 */
	overflow: hidden;
	-webkit-transition: height 200ms;
	transition: height 200ms;
	position: relative;
}

.mask {/* 过渡效果 */
	position: absolute;
	bottom: 0;
	left: 0;
	height: 23px;
	width: 100%;
	background: -webkit-gradient(linear, center top, center bottom, from(transparent), to(white));
} 
	    </pre>
	    <%@include file="../public/footer.jsp" %>
    </body>
</html>