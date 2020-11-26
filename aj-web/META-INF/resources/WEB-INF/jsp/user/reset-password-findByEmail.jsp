<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="c" 	uri="/ajaxjs"%>

<tags:content bannerImg="${ctx}/images/banner.jpg">
	<fieldset class="aj-fieldset" style="margin:5% auto;max-width:500px;">
		<legend>重置密码</legend>
		<h4 class="aj-center-title">账号： ${param.email}</h4>
		<form class="aj-form fixed-width" action="verify/" method="POST">
			<dl>
				<label>
					<dt>新密码</dt>
					<dd>
						<input type="password" name="password" placeholder="6~10位的数字或英文字母" required  pattern="[0-9A-Za-z]{6,10}"  />
					</dd>
				</label>
			</dl>
			<dl>
				<label>
					<dt>重复新密码</dt>
					<dd>
						<input type="password" name="password2" placeholder="请输入重复新密码" required pattern="[0-9A-Za-z]{6,10}" />
						<input type="hidden" name="token" value="${param.token}" />
						<input type="hidden" name="email" value="${param.email}" />
					</dd>
				</label>
			</dl>
			<dl>
				<dt></dt>
				<dd class="aj-btnsHolder" style="text-align: left;width:300px;">
					<button>提 交</button> <button @click.prevent="closePopup($event);">返 回</button>
				</dd>
			</dl>
		</form>
		<script src="http://static.ajaxjs.com//lib/md5.min.js"></script>
		<script>
			aj.xhr.form('.aj-form', json => {
				if (json && json.isOk) {
					aj.alert.show(json.msg || '操作成功！');
					setTimeout("location.assign('/Leidong/user/')", 3000);
				} else 
					aj.alert.show(json ? json.msg : '执行失败！原因未知！');
			},
			{
				beforeSubmit(f, json) {
					if (json.passowrd != json.passowrd2) {
						ajaxjs.alert.show('两次密码输入不一致！');
						return false;
					}
					
					json.password =  md5(json.password);
					delete json.passowrd2;
				
				}
			});
		</script>
	</fieldset>
</tags:content>