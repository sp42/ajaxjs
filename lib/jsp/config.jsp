<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%-- <jsp:useBean id="bean" class="com.ajaxjs.framework.javascript.JSON_Saver" /> --%>
<%
// 	if (request.getParameter("getConfig") != null) {
// 		String filePath = new RequestHelper(request).Mappath("/META-INF/site_config.js");
// 		out.println(Base.text.readFile(filePath));
// 		return;
// 	} else if (request.getMethod().equals("POST")) {
// 		//bean.doAction(request, response);
// 		return;
// 	}
%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/ajaxjs-ui/less/admin.less" title="网站配置" />
<body>
	<style>
		body{
			background-color: #f4f4f4;
			padding:2%!important;
		}
		h4{
			font-size:1.2rem;	 		
		}
		
		.v-m{
			overflow: hidden;
			vertical-align: middle;
			display: table-cell;
			height: 100px;
			line-height: 100px;
		}
		
		.textarea{
			width:100%!important;height:100px!important;resize:none;
		}
	</style>
	<div class="panel">
		<h4>请输入网址配置</h4>
		<hr class="ajaxjs-hr" />
		<form action="?" method="POST" class="ajaxjs-form">
			<dl>
			
			
			<input type="hidden" name="jsonFile" value="site_config.js" />
			<input type="hidden" name="topVarName" value="bf_Config" />

				<label>
					<dt>公司全称<span class="required-note">*</span></dt>
					<dd>
						<input type="text" placeholder="公司全称" name="bf_Config.clientFullName" data-note2="请输入你公司的全称" value="${all_config.clientFullName}" />
					</dd>
				</label>
				<label>
					<dt>公司简称</dt>
					<dd>
						<input type="text" placeholder="公司简称" name="bf_Config.clientShortName" data-note2="请输入你公司的简称"  value="${all_config.clientShortName}" />
					</dd>
				</label>
				<label>
					<dt>网站标题前缀<span class="required-note">*</span></dt>
					<dd>
						<input type="text" placeholder="用户名" name="bf_Config.site.titlePrefix" required data-regexp="Username" data-note="网站全局的标题，在 title 标签中显示" value="${all_config.site.titlePrefix}"/>
					</dd>
				</label>
				<label>
					<!-- 用于 SEO 关键字优化，以便于搜索引擎机器人查找、分类。多个关键字之间用英文 , 逗号分开 -->
					<dt class="v-m">
						搜索关键字
					</dt>
					<dd>
						<textarea name="bf_Config.site.keywords" rows="10" cols="50" class="textarea" data-note2="用于 SEO 关键字优化，以便于搜索引擎机器人查找、分类。<br />多个关键字之间用英文 , 逗号分开">${all_config.site_keywords}</textarea>
					</dd>
				</label>
			</dl>
						
			<label>
				<div class="heightLabel">
					<div>网站描述：</div>
					<div class="sub">网站的说明文本。通常是一段话介绍网站即可。</div>
				</div>
				<textarea name="bf_Config.site.description" rows="10">${all_config.site_description}</textarea>
			</label>
 
			<div class="labelRow">
				<div class="heightLabel">&nbsp;&nbsp;是否启动网站？</div>
				<div class="input"> 
				    <label>
				        <input type="checkbox" class="siteStatus_pc" checked value="${command.siteStatus_pc}" /> PC 版
				    </label>
				    <label>
				        <input type="checkbox" class="siteStatus_wap" checked /> 手机版
				    </label>
				    <label>
				        <input type="checkbox" class="siteStatus_weixin" checked value="on" /> 微信版
				    </label>
				    <span style="color:gray;font-size:.8rem;">请慎重选择是否关闭网站！</span>
				</div>
			</div>
	 
			<div> 
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="保存" class="my-btn-3" /> &nbsp;&nbsp;&nbsp;&nbsp;
			</div>
	</form>
	</div>
 
	<script>
	/*
		new bf_form(document.querySelector('form'), {
			isCommonAfterSubmit : true
		});
		*/
		
		if (!this.FormData) {
			FormData = function () { }
		}
		;(function () {
			// 遍历每个函数
			function getList(formEl, fn) {
				var list = [];
				function add(el) {
					list.push(el);
				}
				var forEach = [].forEach;
				forEach.call(formEl.querySelectorAll('input[type=text]'), add);
				forEach.call(formEl.querySelectorAll('input[type=password]'), add);
				forEach.call(formEl.querySelectorAll('textarea'), add);

				list.forEach(fn);
			}

			function createNoteEl(className, tipsNote, input, isToLeft) {
				var tipsNoteEl = document.createElement('div');
				tipsNoteEl.classList.add(className);
				tipsNoteEl.innerHTML = '<div class="aj-arrow ' + (isToLeft ? 'toLeft' : 'toTop') + '"></div><span>' + tipsNote + '</span>';
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
					tipsNoteEl.style.left = el.offsetLeft + el.offsetWidth + 20 + 'px';
					tipsNoteEl.style.top = el.offsetTop  + 'px';
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
						require: /.+/,
						username: /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/,
						email: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
						url: /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,
						integer: /^[-\+]?\d+$/,
						double: /^[-\+]?\d+(\.\d+)?$/,
						english: /^[A-Za-z]+$/,
						chinese: /^[\u0391-\uFFE5]+$/
					};
					var regexp = validatorReg[el.getAttribute('data-regexp')];
					if (!regexp) regexp = new RegExp(regexp); // 预设没有的，自定义
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
				var tipsEl = tipsNote && createNoteEl('tipsNote', tipsNote, input);

				//var data-note-direction = input.getAttribute('data-note-direction');
				//return;

				// float panel
				var tipsNote2 = input.getAttribute('data-note2');
				tipsNote2 && createNoteEl('tipsNote2', tipsNote2, input, true).classList.add('hide');

				// 表单验证的状态图标
				var iconEl = document.createElement('div');
				iconEl.classList.add('icon');
				iconEl.classList.add('hide');
				input.parentNode.appendChild(iconEl);
				input.onfocus = onfocus;
				input.onblur = onBlur;
			}

			FormData.initTips = function (formEl) {
				getList(typeof formEl == 'string' ? document.querySelector(formEl) : formEl, everyInput);
			}
		})();
		
		FormData.initTips('.ajaxjs-form');
	</script>
</body>
</html>