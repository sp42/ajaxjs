<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "折叠菜单");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>

	<h4>折叠菜单 expander</h4>
	<hr class="ajaxjs-hr" />
	<p>常用于展开正文详情。提供过渡效果的图层，如果不需要把 div class="mask" 去掉即可。注意加入容器的 padding 会导致关闭时高度异常。</p>

	<div class="min p centerLimitWidth" style="width:65%;">
		<p>
			母亲的早逝令灰姑娘辛德瑞拉再也感受不到母爱的温暖，失去了公主般的童年。后母和她的两个女儿，无时无刻不在想方设法折磨仙德瑞拉，给她糟糕的住处、做不完的家务。幸好得道多助、失道寡助，心地善良的辛德瑞拉结识了一大班朋友，老鼠、鸟儿、小狗都是守护在她周围的老友记。王子要在全城寻找王妃，仙德瑞拉用破旧零碎的布料赶做了一件礼服，当然这远远比不上心花怒放的姐姐们穿得艳丽。然而，灰姑娘得到了魔法相助，在神奇的法术中她美得倾城，坐着南瓜车来到王子的宫殿，与王子翩翩起舞。她知道，法术会在12点之前统统消失，大钟敲响的那一刻，她慌忙逃离王子，情陷已深的王子拿着灰姑娘不小心遗下的玻璃鞋，誓要从百万女子中找到一生中的最爱辛德瑞拉。
		</p>
		<p>
			在母亲离世后，艾拉的父亲再娶了一位继母。为了支持深爱的父亲，艾拉热情欢迎继母和她的两位女儿进入自己的家庭。然而当父亲也意外去世后，她才发现自己身陷嫉妒和折磨。艾拉被继母一家当作女仆对待，还被恶意称作“灰姑娘”。尽管遭受残忍的折磨，艾拉还是决心坚持母亲的遗言，“勇敢而善良地活下去”，因此她不向绝望投降，也不仇恨虐待她的人。后来，艾拉在树林里遇见了潇洒迷人的陌生人基特，她以为他是王宫的随从，却不知他竟是王子。随着王宫向全国少女发出舞会邀请，艾拉也希望自己能借机再见基特。但是当然，继母不允许她参加舞会，还撕碎了她的裙子。可就如所有美好童话故事里描绘的一样……神仙教母翩然而至，借用一只南瓜和一群小老鼠，永远改变了灰姑娘的人生。
		</p>
		<div class="mask"></div>
	</div>
	<div class="p centerLimitWidth" style="text-align: center;">
		<a href="###" class="moreBtn">更多</a>
	</div>

	<script>
		function collapse(e, contentEl, maskEl, minHeight) {
			var maskStyle = maskEl.style;
			if (maskStyle.display == '' || maskStyle.display == 'block') {
				maskStyle.display = 'none';
				contentEl.style.height = contentEl.scrollHeight + 'px';
				e.target.innerHTML = "收起";
			} else {
				maskStyle.display = 'block';
				contentEl.style.height = minHeight + 'px';
				e.target.innerHTML = "更多";
			}
		}

		function Expander(moreBtn) {
			// 注意结构：按钮与 面板 .min 在 dom 是同级，
			var panel = moreBtn.parentNode.parentNode.querySelector('.min');
			var mask = panel.querySelector('.mask');

			setTimeout(function() {
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

.mask { /* 过渡效果 */
	position: absolute;
	bottom: 0;
	left: 0;
	height: 23px;
	width: 100%;
	background: -webkit-gradient(linear, center top, center bottom, from(transparent), to(white));
}

.p p {
	text-align: justify;
}
</style>


	<h4>Accordion Menu 折叠菜单</h4>
	<hr class="ajaxjs-hr" />
	<p>折叠菜单的特点是同一时间只展开一个区域，其他区域则处于闭合状态。</p>
 
	<ul class="centerLimitWidth leftSidebar" style="width:80%;">
		<li class="siteMgr">
			<h3>网站管理</h3>
			<ul>
				<li><a target="iframepage"
					href="website/profile/changePassword.jsp">帐号信息</a></li>
				<li><a target="iframepage"
					href="website/profile/companyInfo.jsp">公司信息</a></li>
				<li><a target="iframepage"
					href="website/gallery/companyAlbum.jsp">相 册</a></li>
				<li><a target="iframepage" href="website/pageEditor/index.jsp">页面维护</a>
				</li>
				<li><a target="iframepage" href="website/profile/global.jsp">全局信息</a>
				</li>
			</ul>
		</li>

		<li class="news">
			<h3>新闻中心管理</h3>
			<ul>
				<li><a target="iframepage" href="entry/list/news">新闻管理</a></li>
			</ul>
		</li>
		<li class="levelPoints">
			<h3>积分系统</h3>
			<ul>
				<li><a target="iframepage" href="levelPoints/memberTree.jsp">会员下线一览</a>
				</li>
				<li><a target="iframepage"
					href="levelPoints/showUserPoints.jsp">显示会员积分</a></li>
			</ul>
		</li>
		<li>
			<h3>其他信息</h3>
			<ul>
				<li><a target="iframepage" href="../job/job.jsp">留言管理</a></li>
				<li><a target="iframepage" href="../job/job.jsp">招聘管理</a></li>
			</ul>
		</li>
	</ul>
	<script>
		// 初始化菜单
		new ajaxjs.AccordionMenu(document.querySelector('.leftSidebar'));
	</script>


	<p>JS 代码如下：</p>

	<pre class="prettyprint">
new ajaxjs.AccordionMenu(document.querySelector('.leftSidebar'));</pre>


	<p>LESS 样式如下：</p>

	<pre class="prettyprint">
.leftSidebar {
	.acMenu ();
	width: 20%;
	margin: 3% auto;
	overflow: hidden;
	& > li {
		border-top: 1px solid white;
		border-bottom: 1px solid lightgray;
		&.pressed {
			border-top: 0;
			border-bottom: 1px solid lightgray;
			h3 {
				// .hozGradient_3 (transparent , darken(@mainColor, 10));
			}
		}

		ul {
			li {
				list-style-type: disc;
				padding-left: 10%;
				a {
					width: 100%;
					display: block;
				}

				&.selected {
					// background-color: lighten(@hightColor, 33);
					a {
						color: black;
					}
				}
			}
		}

		h3 , li {
			padding: 5px 0 5px 15px;
			letter-spacing: 2px;
			line-height: 20px;
			color: #939da8;
			font-size: 12px;
			&:hover , a:hover {
				color: black;
			}
		}
	}
}
	    </pre>

	<%@include file="../public/footer.jsp"%>
</body>
</html>