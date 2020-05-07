<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="/ajaxjs" prefix="c" %>
<%@attribute name="type" required="true" type="String" description="标签类型"%>

<c:if test="${type == 'profile'}">	
	<script src="${ajaxjs_ui_output}/lib/China_AREA_full.js"></script>
	<style>
		.aj-china-area span{
			clear: left;
		    display: block;
		    float: left;
		    width: 60px;
		}	
	</style>
	<form class="aj-form fixed-width" action="${ctx}/user/profile/" method="put">
		<input type="hidden" name="id" value="${info.id}" />
		<dl>
			<label>
				<dt>姓名</dt>
				<dd>
					<input type="text" name="username" value="${info.username}" placeholder="请输入您的真实姓名" />
				</dd>
			</label>
		</dl>
		<dl>
			<dt>性别</dt>
			<dd>
				<label><input type="radio" name="sex" value="1" ${info.sex == 1 ? 'checked' : '' }> 男</label> 
				<label><input type="radio" name="sex" value="2" ${info.sex == 2 ? 'checked' : '' }> 女</label> 
				<label><input type="radio" name="sex" value="0" ${info.sex == 0 ? 'checked' : '' }> 未知</label>
			</dd>
		</dl>
		<dl>
			<dt>出生日期</dt>
			<dd class="calendar">
				<aj-form-calendar-input field-name="birthday" field-value="${info.birthday}" :position-fixed="true"></aj-form-calendar-input>
			</dd>
		</dl>
		<dl>
			<dt>居住地</dt>
			<dd class="area">
				<aj-china-area province-code="${info.locationProvince}" city-code="${info.locationCity}" district-code="${info.locationDistrict}">
				</aj-china-area>
				<input type="text" name="address" placeholder="填写地址，不用再填省市区" pattern="^[\u4e00-\u9fa5\w]{4,15}$" value="${info.address}" size="32" />
			</dd>
		</dl>
		<dl>
			<label>
				<dt>工作职务</dt>
				<dd>
					<input type="text" name="jobTitle" value="${info.jobTitle}" placeholder="工作职务" />
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>身份证号码</dt>
				<dd>
					<input type="text" name="idCardNo" value="${info.idCardNo}" placeholder="中国大陆身份证号码" pattern="^[1-9]\d{5}(18|19|20|(3\d))\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$" />
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>个人简介</dt>
				<dd>
					<textarea rows="6" cols="35" name="content" placeholder="200字符内" class="aj-input">${info.content}</textarea>
				</dd>
			</label>
		</dl>
		<dl>
			<dt></dt>
			<dd>
				<button>保存</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <button class="returnBtn">返回</button>
			</dd>
		</dl>
	</form>
	<script>
		new Vue({
			el : '.calendar'
		});
		new Vue({
			el : '.area'
		});
 
		// AJAX 表单提交
		ajaxjs.xhr.form('.aj-form');
	</script>
</c:if>

<c:if test="${type == 'avatar'}">
	<script src="${ajaxjs_ui_output}/lib/exif.min.js"></script>
	
	<form method="put" action="${ctx}/user/profile/" class="avatar" style="width:600px;margin:0 auto;text-align: center;">
		<input type="hidden" name="id" value="${userId}" />
		
		<aj-xhr-upload action="${ctx}/user/profile/avatar/${userUid}/?id=${userId}" :is-img-upload="true" 
			hidden-field="avatar" 
			hidden-field-value="${userAvatar}" 
			img-place="${empty userAvatar ? commonAsset.concat('/images/imgBg.png') : userAvatar}">
		</aj-xhr-upload>
		
		<p class="aj-note">
			先选择图片然后点击上传，然后请记得按下保存方能生效。<br />
			支持jpg、png、jpeg、bmp，图片大小5M以内 </p>
			
		<br />
		<br />
		<br />
		<div class="aj-btnsHolder">
			<button>保存</button> <button onclick="history.back();" class="aj-btn">返回</button>
		</div>
	</form>

	<script>
		new Vue({
			el : 'form.avatar'
		});
		
		aj.xhr.form('form.avatar', json => {
			aj.xhr.get('${ctx}/user/profile/avatar/updateAvatar/', json=>{
				aj.msg.show("修改头像成功");
				aj('.avatar img').src = "${ctx}/" + aj("input[name=avatar]").value;
				
			}, {
				avatar: aj("input[name=avatar]").value
			});
		});
	</script>
</c:if>

<script src="${ajaxjs_ui_output}/lib/md5.min.js"></script>
<c:if test="${type == 'account'}">	
	<ul class="safe">
		<li>	
		<c:if test="${aj_allConfig.user.login.canModiflyUserName}">
			<a href="${ctx}/user/account/safe/?action=modiflyUserName">修改登录名</a>
		</c:if>
			<div class="${empty userName ? 'fail' : 'ok' }">登录名</div>
			<div>${empty userName ? '未绑定邮箱' : userName} 登录名即用户名</div>
		</li>
		<li>	
			<a href="${ctx}/user/account/safe/?action=modiflyPhone">更换手机</a>
			<div class="${empty userInfo.phone ? 'fail' : 'ok' }">绑定手机</div><div>${empty userPhone ? '未绑定手机' : userPhone}</div>
		</li>
		<li>	
			<a href="${ctx}/user/account/safe/?action=modiflyEmail">设置邮箱</a>
			<div class="${empty userInfo.email ? 'fail' : 'ok' }">绑定邮箱</div><div>${empty userInfo.email ? '未绑定邮箱' : userInfo.email} 
				&nbsp;${empty userInfo.email ? '' : (isEmailVerified ? '已验证' :'未验证 &nbsp;<a href="#">点击验证</a>')}</div>
		</li>
		<li>	
			<a href="${ctx}/user/account/safe/?action=resetPsw">修改密码</a>
			<div class="ok">设置密码</div><div>已设置</div>
		</li>
		<li>	
			<a href="${ctx}/user/account/log-history/">查看登录历史</a>
			<div style="padding-left:3%;">最近登录</div><div>
				<c:dateFormatter value="${lastUserLoginedInfo.createDate}"></c:dateFormatter>
			</div>
		</li>
	<c:if test="${aj_allConfig.user.login.thridOauth}">
		<li>	
			<a href="oauth/">管理绑定</a>
			<div style="padding-left:3%;">第三方登录</div><div>通过微博、微信、QQ等第三方登录的绑定</div>
		</li>
	</c:if>
		<li>	
			<div style="padding-left:3%;">用户组</div><div>${UserGroups[userGroupId].name}</div>
		</li>
		<li>	
			<a href="delete-account/">账号注销</a>
			<div style="padding-left:3%;">删除帐号</div><div>删除该帐号以及所有该帐号关联的信息</div>
		</li>
	</ul>
</c:if>

<c:if test="${type == 'modiflyUserName'}">
	<div class="test-pl">
		<aj-process-line ref="processLine" :items="['填写登录名信息', '检查是否可用', '完成']"></aj-process-line>
	</div>
	
	<form class="aj-form fixed-width" action="${ctx}/user/info/modiflyUserName" method="POST">
		<dl>
			<label>
				<dt>用户名</dt>
				<dd>
					<input type="text" name="userName" value="${userName}" placeholder="请输入用户名" required pattern=".{4,10}"/>
					<div class="note">请输入您的由4~10位由中文26个英文字母的用户名</div>
				</dd>
			</label>
		</dl>
		<dl>
			<dt></dt>
			<dd>

				<button>提交</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <button class="returnBtn">返回</button>

			</dd>
		</dl>
	</form>
	
	<script>
		ProcessLine = new Vue({el : '.test-pl'});
		
		aj.xhr.form('.aj-form', json => {
			if (json) {
				if (json.isOk) {
					ajaxjs.alert.show(json.msg || '操作成功！');
					ProcessLine.$refs.processLine.go(1);
				} else {
					ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
				}
			} else {
				ajaxjs.alert.show('ServerSide Error!');
			}
		});
	</script>
</c:if>

<c:if test="${type == 'modiflyPhone'}">
	<div class="test-pl">
		<aj-process-line ref="processLine" :items="['输入新号码', '审核手机号码','完成']"></aj-process-line>
	</div>
	
	<form class="aj-form fixed-width" action="${ctx}/user/info/modiflyPhone" method="POST">
		<dl>
			<label>
				<dt>手机</dt>
				<dd>
					<input type="text" name="phone" placeholder="请输入手机" required />
				</dd>
			</label>
		</dl>
		<dl>
			<dt></dt>
			<dd>
				<button>提交</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <button class="returnBtn">返回</button>
			</dd>
		</dl>
	</form>
	
	<script>
		ProcessLine = new Vue({el : '.test-pl'});
		
		aj.xhr.form('.aj-form', json => {
			if (json) {
				if (json.isOk) {
					ajaxjs.alert.show(json.msg || '操作成功！');
					ProcessLine.$refs.processLine.go(1);
				} else {
					ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
				}
			} else {
				ajaxjs.alert.show('ServerSide Error!');
			}
		});
	</script>
</c:if>

<c:if test="${type == 'modiflyEmail'}">
	<div class="test-pl">
		<aj-process-line ref="processLine" :items="['填写新邮箱', '邮箱审核', '完成']"></aj-process-line>
	</div>
	
	<form class="aj-form fixed-width" action="${ctx}/user/info/modiflyEmail" method="POST">
		<dl>
			<label>
				<dt>邮箱</dt>
				<dd>
					<input type="email" name="email" value="${user.email}" placeholder="请输入邮箱" required pattern="[^@]+@[^@]+\.[a-zA-Z]{2,6}" /> 
				</dd>
			</label>
		</dl>
		<dl>
			<dt></dt>
			<dd>
				<button>提交</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <button class="returnBtn">返回</button>
			</dd>
		</dl>
	</form>
	
	<script>
		ProcessLine = new Vue({el : '.test-pl'});
		
		aj.xhr.form('.aj-form', json => {
			if (json) {
				if (json.isOk) {
					ajaxjs.alert.show(json.msg || '操作成功！');
					ProcessLine.$refs.processLine.go(1);
				} else {
					ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
				}
			} else {
				ajaxjs.alert.show('ServerSide Error!');
			}
		});
	</script>
</c:if>

<c:if test="${type == 'resetPsw'}">
	<div class="test-pl">
		<aj-process-line ref="processLine" :items="['填写信息', '重置密码', '完成']"></aj-process-line>
	</div>
	
	<form class="aj-form fixed-width" action="resetPassword" method="POST">
		<dl>
			<label>
				<dt>原密码</dt>
				<dd>
					<input type="password" name="password"  placeholder="请输入原来的密码" required pattern="[0-9A-Za-z]{6,10}" />
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>新密码</dt>
				<dd>
					<input type="password" name="new_password" placeholder="6~10位的数字或英文字母" required  pattern="[0-9A-Za-z]{6,10}"  />
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>重复新密码</dt>
				<dd>
					<input type="password" name="new_password2" placeholder="请输入重复新密码" required pattern="[0-9A-Za-z]{6,10}" />
				</dd>
			</label>
		</dl>
		<dl>
			<dt></dt>
			<dd class="aj-btnsHolder">
				<br />
				<button>提交</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <button class="returnBtn">返回</button>
			</dd>
		</dl>
	</form>
	
	<script>
		ProcessLine = new Vue({el : '.test-pl'});
		
		aj.xhr.form('.aj-form', json => {
			if (json) {
				if (json.isOk) {
					ajaxjs.alert.show(json.msg || '操作成功！');
					ProcessLine.$refs.processLine.go(2);
				} else {
					ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
				}
			} else {
				ajaxjs.alert.show('ServerSide Error!');
			}
		}, {
 			beforeSubmit (form, json) {
 				ProcessLine.$refs.processLine.go(1);
					json.password = md5(json.password);
					json.new_password = md5(json.new_password);
					
					delete json.new_password2;
				}
		});
	</script>
</c:if>	