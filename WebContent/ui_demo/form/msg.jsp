<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "消息提示框");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<h4>消息提示框</h4>
	<p>
		该控件的效果是从页面右上方徐徐显示出一个消息框，如下图所示。然后暂时停留若干时间，完毕后自动收缩。 <a href="http://blog.csdn.net/zhangxin09/article/details/79156982">教程</a>
	</p>
	<section class="center">
	
	<div class="topMsg">暮從碧山下，山月随人歸。卻顧所來徑，蒼蒼橫翠微。相攜及田家，童稚開荊扉。綠竹入幽徑，青蘿拂行衣。</div>
	<button class="ajaxjs-btn" onclick="show();return false;">show</button>
	<button class="ajaxjs-btn" onclick="show('保存成功！');return false;">show with text</button>
	<script>
	function show(text, showTime){
		var el = document.querySelector('.topMsg');
		if(text)
			el.innerHTML = text;
		el.classList.remove('fadeOut');
		el.classList.add('fadeIn');
		
		setTimeout(function(){
			el.classList.remove('fadeIn');
			el.classList.add('fadeOut');
		}, showTime || 3000)
	}
	</script>
	</section>
	<%@include file="../public/footer.jsp"%>
</body>
</html>