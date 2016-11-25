<%@tag pageEncoding="UTF-8" description="JS模拟滚动条"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="robots" content="all" />
<meta name="author" content="Tencent-CP" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>JS模拟滚动条</title>
<style type="text/css">
/*reset*/
body {
	margin: 0;
	font: 12px/1.5 tahoma, '\5b8b\4f53', sans-serif;
	color: #333;
	background: #fff;
}

h1,h2,h3,h5,p,ul,ol,dl,dd {
	margin: 0;
}

button {
	padding: 0;
}

ul,ol {
	padding-left: 0;
	list-style-type: none;
}

a {
	text-decoration: none;
	color: #333;
}

a img {
	border: 0;
}

h1 {
	font-size: 16px;
}

h2 {
	font-size: 14px;
}

h3 {
	font-size: 100%;
}

em {
	font-style: normal;
	color: #0069c1;
}

.box {
	background:
		url(http://henryzp.com/wp-content/uploads/2011/11/box_bg.jpg)
		no-repeat;
	width: 426px;
	height: 294px;
	margin: 100px 0 0 100px;
	position: relative;
	overflow-y: hidden;
}

.box_main {
	width: 400px;
	height: 255px;
	padding-left: 26px;
	position: relative;
	overflow-y: hidden;
	margin-top: 20px;
}

.box_scroll {
	width: 24px;
	height: 250px;
	position: absolute;
	top: 4px;
	right: 10px;
}

.box_scroll .srcollbtn {
	background:
		url(http://henryzp.com/wp-content/uploads/2011/11/scrollimg.png)
		no-repeat;
	width: 24px;
	height: 23px;
	position: absolute;
	top: 0px;
	right: 0px;
	text-indent: -9999em;
}

.content {
	width: 340px;
	position: absolute;
}
</style>
</head>
<body>
	<div class="box">
		<div class="box_main">
			<div class="box_scroll">
				<a class="srcollbtn" id="mbutton" title="滚动条">滚动条</a>
			</div>
			<div id="clist" class="content" style="top: 0;">
				<p>
					<strong>活动规则说明：</strong> <br /> 活动时间：每个月15号10：00-22号10：00 <span
						class="c_text2">到点醒我：</span> QQ提醒
				</p>
				<p>如何获得抽奖资格？ 购买右边的不同欢乐主题道具，就可以获得不同的抽奖资格，还能赠送欢乐豆。</p>
				<p>
					如何拯救黑桃A？ <br />
					点开始抽奖，如果中了黑桃A（iphone4），会弹出女巫的魔盒，提示输入前一个中iphone4的玩家设置的密码（0~22中的任意一个数字），猜对了奖品暂时归您所有，到10.15上午10点页面将公布最终的iphone4获奖名单，我们的工作人员会与您联系，记录您的收货地址，15个工作日内发放奖品。
				</p>
				<p>
					黑桃A奖品，别人设置的密码我猜错了，还有机会继续竞猜么？ <br /> 可以的，有两种方式： <br />
					1、右边奖品实时播报下方会显示当前黑桃A奖品持有者，点击右边的按钮，会再次出现魔盒，您可以继续竞猜，此时每猜一次需消耗一次页面的抽奖机会。
					<br /> 2、您可以继续抽奖，如果抽中黑桃A奖品，可免费竞猜一次别人设置的密码。
				</p>
				<p>
					获奖了该怎么领取？ <br />
					实物奖品会提示您填写个人资料，奖品将会在15个工作日内发放，如果填写收货地址错误或未填写，我们不予发放奖品，损失由用户自行承担。其他虚拟奖品都是即时发放，请您留意系统提示。
				</p>
				<p>
					挑战资格本月没用完，下月还可以继续使用吗？ <br /> 不可以。当月的挑战资格请务必在当月22号10：00前使用完毕，过期无效。
				</p>
				<p>
					举办方有权利在法律允许的范围内修改和优化。 <br /> 本活动最终解释权归腾讯公司所有。
				</p>
				<p>腾讯官方游戏客服专线：0755-86013666</p>
			</div>
		</div>
	</div>
	<!-- 引入脚本 -->
	<script type="text/javascript"
		src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.1/jquery.min.js"></script>
	<script type="text/javascript">
		(function($) {
			var c = $('#clist'), mb = $('#mbutton'), mbc = mb.parent(), cmin = 0, cmax = 0, bmax = 0, isMove = false;
			//scroll move
			cmax = c.height() - c.parent().height();
			bmax = mbc.height() - mb.outerHeight();
			//var log = function(m){$('#log').html(m);};
			var eventDo = {
				start : 0,
				current : 0,
				top : 0,
				timeOut : 0,
				initY : 0,
				mouseUp : function(e) {
					isMove = false;
					$(document).unbind('mousemove.s').unbind('mouseup.s');
				},
				mouseMove : function(e) {
					if (!isMove) {
						return false;
					}
					eventDo.current = e.pageY;
					var moved = eventDo.current - eventDo.start;
					var t = eventDo.top + moved;
					t > bmax && (t = bmax);
					t < 0 && (t = 0);
					(!eventDo.timeOut) && setTimeout(function() {
						mb.css('top', t + 'px');
						//content move too,same percent
						var percent = t / bmax;
						c.css('top', -1 * cmax * percent + cmin);
					}, 10);
				}
			};
			eventDo.initY = mb.offset().top + mb.outerHeight();
			mb.bind('mousedown', function(e) {
				isMove = true;
				eventDo.start = e.pageY;
				eventDo.top = mb.position().top;
				$(document).bind('mousemove.s', eventDo.mouseMove).bind(
						'mouseup.s', eventDo.mouseUp);
				window.getSelection().removeAllRanges();
				document.selection.empty();
			});
			mbc.bind('click', function(e) {
				var moved = e.pageY - eventDo.initY;
				moved > bmax && (moved = bmax);
				moved < 0 && (moved = 0);
				mb.css('top', moved + 'px');
				//content move too,same percent
				var percent = moved / bmax;
				c.css('top', -1 * cmax * percent + cmin);
			});
		})(jQuery);
	</script>
</body>
</html>