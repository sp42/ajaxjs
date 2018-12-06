<%@page pageEncoding="UTF-8"%>
<h4>消息框</h4>
<!-- 对话框 -->
<div class="modal hide" onclick="ajaxjs.modal(arguments[0]);">
	<div style="text-align:center;">
		花間一壺酒，獨酌無相親。<br/>
		舉杯邀明月，對影成三人。<br/>
		月既不解飲，影徒隨我身。<br/>
		暫伴月將影，行樂須及春。<br/>
		我歌月徘徊，我舞影零亂。<br/>
		醒時同交歡，醉后各分散。<br/>
		永結無情游，相期邈云漢。<br/>
	</div>
</div>
<!-- // 对话框 -->
<p>简单的弹出层。</p>
<button onclick="ajaxjs.modal();">Modal 对话框</button>

<p>
	该控件的效果是从页面右上方徐徐显示出一个消息框，然后暂时停留若干时间，完毕后自动收缩。<a href="http://blog.csdn.net/zhangxin09/article/details/79156982">教程</a>
</p>
<!-- 顶部消息提示框 -->
<div class="topMsg">暮從碧山下，山月随人歸。<br/>卻顧所來徑，蒼蒼橫翠微。</div>
<button onclick="aj.alert.show();">顶部消息提示框 </button>
<br />
<button onclick="aj.alert.show('保存成功！');">顶部消息提示框（方法传内容）</button>
<!-- // 顶部消息提示框 -->