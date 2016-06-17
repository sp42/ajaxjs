<%@tag pageEncoding="UTF-8" description="对话框"%>
<%@attribute name="id" required="false" type="String" description="id"%> 
<%@attribute name="title" required="false" type="String" description="标题"%> 
<%@attribute name="msgBody" required="false" type="String" description="内容"%> 
<%@attribute name="msgBox_type" required="false" type="Integer" description="对话框种类"%> 
<script>
	msgBox = {};
</script>
<%
if(msgBox_type == null)msgBox_type = 0; // 默认值
// System.out.println("dfd" + msgBox_type);
if(msgBox_type == 0){
%>
<fieldset class="warningMsgBox">
	<legend>${title}</legend>
	<p>
		${msgBody}<jsp:doBody />
	</p>
	<div align="center">
		<a href="#" onclick="var _p = this.parentNode.parentNode;_p.style.display = 'none';_p.style.left='100%';_p.removeCls('fadeInRight');" 
		style="font-size:14px;" class="btn exit">关&nbsp;&nbsp;闭</a>
	</div>
</fieldset>

<script>
	var isOldBrowser = !('webkitAnimation' in document.body.style) || /android\s?2\.2/i.test(navigator.userAgent);
	// 显示对话框
	function alertBox(msg){
		var msgBox = document.querySelector('.warningMsgBox');
		msgBox.style.display= 'block';
		
		if(msg)msgBox.querySelector('p').innerHTML = msg;
		
		if(isOldBrowser){
			msgBox.style.left = '8%';
		}else{
			msgBox.addCls('fadeInRight');
		}
	}
</script>
<%}else if(msgBox_type == 1){
	// 全屏幕遮罩
%>
	<div class="fadeFullScreen_Panel ${id}">
		${msgBody}<jsp:doBody />
		<button class="x-btn close">
			&nbsp;&nbsp;&nbsp;&nbsp;关 闭 close&nbsp;&nbsp;&nbsp;&nbsp;
		</button>
	</div>
	<script>
		msgBox.fadeFullScreen_Panel = function(showBtn, panel_el){
			this.panel_el = panel_el ||document.querySelector('.fadeFullScreen_Panel');
			showBtn.addEventListener('click', this.show.bind(this));
			
			var closeBtn = this.panel_el.querySelector('.close');
			if (closeBtn){
				closeBtn.addEventListener('click', this.close.bind(this));
			}
		}
		msgBox.fadeFullScreen_Panel.prototype = {
			show : function(){
				var panel_el = this.panel_el;
				panel_el.style.display = 'block';
				setTimeout(function(){
					panel_el.addCls('show');
				}, 200);
			},
			// 关闭按钮
			close : function(){
				var panel_el = this.panel_el;
				panel_el.removeCls('show');
				setTimeout(function(){
					panel_el.style.display = 'none';
				}, 300);
			}
		};
	</script>
<%}%>