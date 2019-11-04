<%@ page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:account-center>
	<style>
	
		form{
			width:460px;
		}
		.form-1 dl dt {
			width:30%;
		}
		.form-1 dl dd {
			width:70%;
		}
		
		.area select{
			width:100px;
		}
	</style>
	<script src="${ajaxjs_ui_output}/lib/China_AREA_full.js"></script>
	<h3>用户信息</h3>

	<div style="text-align: right;padding-right: 5%;">
		<a href="#">更多个人信息</a>
	</div>
	<form class="aj-form form-1" action="${ctx}/user/add_information" method="post">
		<div class="formBox">
			<dl>

				<label>
					<dt>用户名</dt>
					<dd>
						<input type="text" name="name" value="${userName}" disabled="true"
							placeholder="用户名当前为空" /> <a href="${ctx}/user/">修改</a>
					</dd>
				</label>

				<label>
					<dt>手机</dt>
					<dd>
						<input type="text" name="email" value="${userPhone}"
							disabled="true" placeholder="请输入电子邮件"
							pattern="/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/" />
						<a href="${ctx}/user/">修改</a>
					</dd>
				</label>
				<label>
					<dt>电子邮件</dt>
					<dd>
						<input type="text" name="email" value="${user.email}"
							disabled="true" placeholder="电子邮件当前为空"
							pattern="/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/" />
						<a href="${ctx}/user/">修改</a>
					</dd>
				</label>

				<label>
					<dt>姓名</dt>
					<dd>
						<input type="text" name="name" value="${user.name}"
							placeholder="请输入您的真实姓名" />
					</dd>
				</label>

				<dt>性别</dt>
				<dd>
					<label><input type="radio" name="sex" value=""
						checked="checked"> 男</label> <label><input type="radio"
						name="sex" value=""> 女</label> <label><input type="radio"
						name="sex" value=""> 未知</label>
				</dd>

				<label>
					<dt>出生日期</dt>
					<dd class="calendar">
						<aj-form-calendar-input field-name="birthday"
							field-value="${user.birthday}" :position-fixed="true"></aj-form-calendar-input>
						<div class="note">请输入您的生日</div>
					</dd>
				</label>

				<label>
					<dt>地区</dt>
					<dd class="area">
						<aj-china-area></aj-china-area>
						<br />
						<input type="text" size="40" name="name" value="${user.name}" required placeholder="请输入地区" />
						<div class="note">请输入地区名称</div>
					</dd>
				</label>

			</dl>
		</div>
		<dl>
			<dt></dt>
			<dd>
				<button>提交</button>
			</dd>
		</dl>
	</form>
	<p class="note">为提供更好的服务，请完善以上信息。我们保证不会外泄个人私隐信息。</p>
	<script>
		new Vue({
			el : '.calendar'
		});
		new Vue({
			el : '.area'
		});
		var add_information = document.querySelector('form.add_information');
		// AJAX 表单提交
		ajaxjs.xhr.form(add_information, ajaxjs.xhr.defaultCallBack);
	</script>
</tags:account-center>