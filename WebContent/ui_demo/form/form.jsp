<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "表单提示及验证");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<h4>表单布局</h4>

	<h4>表单布局</h4>
	<hr class="ajaxjs-hr" />
	<script>
		if (!this.FormData) {
			FormData = function() {
			}
		};
		(function() {
			// 遍历每个函数
			function getList(formEl, fn) {
				var list = [];
				function add(el) {
					list.push(el);
				}
				var forEach = [].forEach;
				forEach.call(formEl.querySelectorAll('input[type=text]'), add);
				forEach.call(formEl.querySelectorAll('input[type=password]'),
						add);

				list.forEach(fn);
			}

			function createNoteEl(className, tipsNote, input, isToLeft) {
				var tipsNoteEl = document.createElement('div');
				tipsNoteEl.classList.add(className);
				tipsNoteEl.innerHTML = '<div class="aj-arrow '
						+ (isToLeft ? 'toLeft' : 'toTop') + '"></div><span>'
						+ tipsNote + '</span>';
				input.parentNode.appendChild(tipsNoteEl); // 添加兄弟节点

				return tipsNoteEl;
			}

			function onfocus(e) {
				var el = e.currentTarget;

				var tipsNote = el.getAttribute('data-note2');
				if (tipsNote) {
					var tipsNoteEl = el.parentNode.querySelector('.tipsNote2');
					tipsNoteEl.querySelector('span').innerHTML = tipsNote;
					//					var xy = el.getBoundingClientRect();
					//					tipsNoteEl.style.left = xy.right + 20 + 'px';
					tipsNoteEl.style.left = el.offsetLeft + el.offsetWidth + 20
							+ 'px';
					tipsNoteEl.style.top = el.offsetTop + 'px';
					tipsNoteEl.classList.remove('hide');
					tipsNoteEl.classList.add('active');
				}
			}

			function onBlur(e) {
				var el = e.currentTarget;

				// 同一时间只有一个 float panel
				var tipsNote = document.querySelector('.tipsNote2.active');
				tipsNote && tipsNote.classList.add('hide');
				tipsNote && tipsNote.classList.remove('active');

				// check
				var iconEl = el.parentNode.querySelector('.icon');
				var xy = el.getBoundingClientRect();
				iconEl.style.left = xy.right + 5 + 'px';
				iconEl.style.top = xy.top + 6 + 'px';

				if (el.hasAttribute('required')) {
					if (el.value) {
						iconEl.classList.remove('hide');
						iconEl.classList.remove('error');
						iconEl.classList.add('pass');
					} else {
						iconEl.classList.remove('hide');
						iconEl.classList.remove('pass');
						iconEl.classList.add('error');
					}
				}

				if (el.hasAttribute('data-regexp')) {
					var validatorReg = {
						require : /.+/,
						username : /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/,
						email : /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
						url : /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,
						integer : /^[-\+]?\d+$/,
						double : /^[-\+]?\d+(\.\d+)?$/,
						english : /^[A-Za-z]+$/,
						chinese : /^[\u0391-\uFFE5]+$/
					};
					var regexp = validatorReg[el.getAttribute('data-regexp')];
					if (!regexp)
						regexp = new RegExp(regexp); // 预设没有的，自定义
					if (regexp && regexp.test(el.value)) {
						iconEl.classList.remove('hide');
						iconEl.classList.remove('error');
						iconEl.classList.add('pass');
					} else {
						iconEl.classList.remove('hide');
						iconEl.classList.remove('pass');
						iconEl.classList.add('error');
					}
				}
			}

			function everyInput(input) {
				// 说明
				var tipsNote = input.getAttribute('data-note');
				var tipsEl = tipsNote
						&& createNoteEl('tipsNote', tipsNote, input);

				//var data-note-direction = input.getAttribute('data-note-direction');
				//return;

				// float panel
				var tipsNote2 = input.getAttribute('data-note2');
				tipsNote2
						&& createNoteEl('tipsNote2', tipsNote2, input, true).classList
								.add('hide');

				// 表单验证的状态图标
				var iconEl = document.createElement('div');
				iconEl.classList.add('icon');
				iconEl.classList.add('hide');
				input.parentNode.appendChild(iconEl);
				input.onfocus = onfocus;
				//input.onblur = onBlur;
			}

			FormData.initTips = function(formEl) {
				getList(typeof formEl == 'string' ? document
						.querySelector(formEl) : formEl, everyInput);
			}
		})();
	</script>


	<form action="?" method="POST" class="form-1 ajaxjs-form">

		<dl>
			<label>
				<dt>
					用户名：<span class="required-note">*</span>
				</dt>
				<dd>
					<input type="text" name="username" placeholder="用户名" required
						data-regexp="Username" data-note="用户名等于账号名；不能与现有的账号名相同；注册后不能修改；"
						data-note2="用户名22等于账号名；不能与现有的账号名相同；注册后不能修改；" />
				</dd>
			</label>


			<label>
				<dt>
					电子邮件：<span class="required-note">*</span>
				</dt>
				<dd>
					<input type="text" name="username" placeholder="请输入电子邮件"
						data-regexp="email" data-note="邮件可用于登录和查找密码等；不能与现有的邮件相同；注册后可以修改；"
						data-note2="推荐使用国内邮箱，如 QQ、163 等的邮箱" />
				</dd>
			</label>



			<label>
				<dt
					style="overflow: hidden; vertical-align: middle; display: table-cell; height: 100px; line-height: 100px;">个人简介
					:</dt>
				<dd>
					<textarea style="width: 100%; height: 100px; resize: none;"></textarea>
				</dd>
			</label>


			<dt>性别：</dt>
			<dd>
				<label><input type="radio" value="" name="sex"
					checked="checked" /> 男</label> <label><input type="radio" value=""
					name="sex" /> 女</label>
			</dd>

			<dt>爱好：</dt>
			<dd>
				<label><input type="checkbox" /> 篮球</label> <label><input
					type="checkbox" /> 足球</label> <label><input type="checkbox" /> 游泳</label>
			</dd>

			<dt>城市</dt>
			<dd>
				<select>
					<option>北京</option>
					<option>上海</option>
					<option>南京</option>
				</select>
			</dd>

			<label>
				<dt>
					<span class="required-note">*</span>登录密码 :
				</dt>
				<dd>
					<input type="password" name="password" />
				</dd>
			</label>




			<label>
				<dt>
					<span class="required-note">*</span>确认密码 :
				</dt>
				<dd>
					<input type="password" name="password" />
				</dd>
			</label>

			<!-- 验证码 -->
			<label class="captchaCode">
				<dt>
					<span class="required-note">*</span>验证码：
				</dt>
				<dd>
					<input type="text" data-regexp="integer" required />
				</dd>
			</label>
			<label>
				<dt>&nbsp;</dt>
				<dd>
					<input name="agree" type="checkbox" /> 我已仔细阅读并接受<a
						href="misc/registration-policy.jsp" target="_blank">注册条款</a>
				</dd>
			</label>
			<dt>&nbsp;</dt>
			<dd>
				<input type="submit" value="注 册" /> &nbsp;&nbsp;&nbsp;&nbsp;<a
					href="?">现有账号登录</a>
			</dd>
		</dl>


	</form>

	<div class="single_tips hide">
		<div class="aj-arrow toLeft"></div>
		<span></span>
	</div>
	<script>
		FormData.initTips('.form-1');
	</script>

	<div style="clear: both;"></div>

	<h4>日历选择控件</h4>
	<hr class="ajaxjs-hr" />
	<!-- 日历控件 -->
	<div class="CalendarHolder">
		<input class="aj-input" placeholder="" name="valid" type="text"
			noSelectedBeforeDate="true" />
		<div class="icon">◲</div>
		<div class="Calendar id_01">
			<div style="position: relative; padding-bottom: 40px;">
				<div class="idCalendarPre">⬅</div>
				<div class="idCalendarNext">➡</div>
				<span class="idCalendarYear"></span>年 <span class="idCalendarMonth"></span>月
				<table cellspacing="0">
					<thead>
						<tr>
							<td>日</td>
							<td>一</td>
							<td>二</td>
							<td>三</td>
							<td>四</td>
							<td>五</td>
							<td>六</td>
						</tr>
					</thead>
					<tbody></tbody>
				</table>

				<div class="btns">
					<input type="button" value="上一年" class="idCalendarPreYear" /> <input
						type="button" value="当前月" class="idCalendarNow" /> <input
						type="button" value="下一年" class="idCalendarNextYear" />
				</div>
			</div>
		</div>
	</div>
	<script src="calendar.js"></script>

	<script>
		var calendar = Object.create(ajaxjs_calendar);
		calendar.el = document.querySelector('.Calendar.id_01');
		calendar.onSelect = function(date) {
			var input = this.el.parentNode.querySelector('input[type=text]');
			// 时间要有限制，不能选择当前日期之前的时间
			var noSelectedBeforeDate = input
					.getAttribute('noSelectedBeforeDate');

			// 2012-07-08
			// firefox中解析 new date(2012/12-23) 不兼容，提示invalid date 无效的日期
			// Chrome下可以直接将其格式化成日期格式，且时分秒默认为零
			var arr = date.split('-'), now = new Date(arr[0], arr[1] - 1,
					arr[2], " ", " ", " ");
			if (noSelectedBeforeDate == "true" && now < new Date) {
				alert('不能选择当前日期之前的时间');
			} else {
				input.value = date;
			}
		};
		calendar.init();
	</script>
	<!-- // 日历控件 -->


	<h4>滑动杆控件</h4>
	<hr class="ajaxjs-hr" />


	<h4>Tag 选择器</h4>
	<hr class="ajaxjs-hr" />

	<label> &nbsp;&nbsp;关键字：<input value="${info.keywords}"
		name="keywords" type="text" class="my-inputField" size="40" />
	</label>
	<style>
input {
	height: 30px;
}

label {
	position: relative;
}

.keywords_item {
	position: absolute;
	top: 2px;
	border: 1px solid red;
	border-radius: 2px;
	background-color: lightgray;
	padding-left: 4px;
	padding-right: 15px;
	text-decoration: none !important;
}

.keywords_item .close {
	/* 		 	float:right; */
	border-radius: 8px;
	background-color: white;
	height: 16px;
	width: 16px;
	text-align: center;
	position: absolute;
	right: -8px;
	top: -8px;
	border: 1px solid red;
	color: red;
	line-height: 16px;
	font-weight: bold;
}
</style>
	<script>
		var input = document.querySelector('input[name=keywords]'), label = input.parentNode;
		label.style.position = "relative";
		var span = document.createElement('span');
		span.innerHTML = label.childNodes[0].nodeValue;
		document.body.appendChild(span);

		var a = document.createElement('a');
		a.className = 'keywords_item';
		a.href = "#"
		a.innerHTML = "dasdsa" + '<div class="close">×<div>';
		a.querySelector('.close').onclick = closeKeyword;

		function closeKeyword(e) {
			var el = e.target;
			while (el.tagName != 'LABEL') {
				el = el.parentNode;

				if (!el || el.tagName == 'LABEL') {
					break;
				}
			}
			el && el.removeChild(el.querySelector('a'));
			return false;
		}

		label.appendChild(a);

		var textWidth = span.offsetWidth;

		a.style.left = (textWidth + 5) + 'px';
		a.style.top = '2px';
		//alert(span.offsetWidth);
		document.body.removeChild(span);
	</script>
</body>
</html>



