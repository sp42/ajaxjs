<%@page pageEncoding="UTF-8"%>
<h4>全屏幕弹窗</h4>

<div id="alert" class="hide">
	花間一壺酒，獨酌無相親。<br/>
	舉杯邀明月，對影成三人。<br/>
	月既不解飲，影徒隨我身。<br/>
	暫伴月將影，行樂須及春。<br/>
	我歌月徘徊，我舞影零亂。<br/>
	醒時同交歡，醉后各分散。<br/>
	永結無情游，相期邈云漢。<br/>
</div>


<p>全屏幕弹窗，居中显示文字。其中 cfg.afterClose : Function 为关闭弹窗后的回调，可选的。</p>
<p>该方法无须使用标签，而是 JS 方法调用。</p>

<p>例子：</p>
<pre class="prettyprint">aj.alert.show(text:String, cfg:Object);  // 弹窗  
aj.alert.close();                        // 关闭</pre>


<p>演示：</p>
<button class="ajaxjs-btn" onclick="aj.alert.show(aj('#alert').innerHTML);">Modal 对话框</button>

<p>例子：</p>
<pre class="prettyprint">aj.alert.show('Hello World!');
aj.showOk('Hello World!', () => { alert('点击了确定按钮');}); // 显示“确定”按钮，第二个参数为回调，可选
aj.showConfirm("Hello World!", () => {alert('点击了 yes 按钮')}); // 显示“是、否”的选择按钮，第二个参数为点击“是”时候的回调，可选</pre>

<p>所有回调函数执行之后都会关闭弹出的窗体。</p>

<p>演示：</p>
<button class="ajaxjs-btn" onclick="aj.showOk('Hello World!', () => { alert('点击了确定按钮');});">回调测试</button>
<button class="ajaxjs-btn" onclick="aj.showConfirm('Hello World!', (v) => {alert('点击了 yes 按钮' + v)});">选择按钮</button>




<h4>顶部消息提示框 </h4>
<p>
	顶部出现，用于后台提示信息多。该控件的效果是从页面右上方徐徐显示出一个消息框，然后暂时停留若干时间，完毕后自动收缩。原理参见详细的<a href="http://blog.csdn.net/zhangxin09/article/details/79156982">教程</a>。
</p>
<p>该方法无须使用标签，而是 JS 方法调用。</p>

<p>演示：</p>
<button class="ajaxjs-btn" onclick="aj.msg.show('暮從碧山下，山月随人歸。<br>卻顧所來徑，蒼蒼橫翠微。');">顶部消息提示框 </button>

<p>例子：</p>
<pre class="prettyprint">aj.msg.show('暮從碧山下，山月随人歸。&lt;br /&gt; 卻顧所來徑，蒼蒼橫翠微。');// 定时器隐藏，无须手动 close</pre>

<h4>简单的弹出层</h4>
<p>浮層組件，通常要復用這個組件</p>
<p>例子：</p>
<pre class="prettyprint"><xmp><aj-layer></aj-layer></xmp></pre>