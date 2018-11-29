<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="c" uri="/ajaxjs" %>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/less/admin.less" title="${isCreate ? '新建' : '编辑:'}${uiName}${info.name}">		
		<style>
			.form-1 input[type=text], .form-1 input[type=password], .form-1 textarea {
			    width: initial;
			}
		</style>
	</commonTag:head>
	<body class="admin-entry-form">
		<header class="top">
			<div class="right">
				<c:if test="${empty isCreate}">
					<a href="../create.do">新建${uiName}</a> | 
				</c:if>
				
				<a href="${isCreate ? '' : '../'}list/do.do?start=0&limit=9">${uiName}列表</a> |  
				<a href="#" target="_blank">新窗口打开</a>
			</div>
			<h3>${isCreate ? '新建' : '编辑'}${uiName} ${not empty info.id ? '#': ''}${info.id}</h3>
		</header>

		<form action="." class="form-1" method="put" style="width:90%;margin:0 auto;">
			<!-- 传送 id 参数 -->
			<input type="hidden" name="id" value="${info.id}" />
		<dl>
		<label>
				<dt>
					账号名称：<span class="required-note">*</span>
				</dt>
				<dd>
					<input type="text" name="name" value="${info.name}" placeholder="用户名" required/>
					<label>
						用户姓名：<input type="text" name="username" value="${info.username}" placeholder="用户名" required />
					</label>
				</dd>
			</label>
			<label>
				<dt>
					电子邮件：
				</dt>
				<dd>
					<input type="text" name="email" placeholder="请输入电子邮件" value="${info.email}" />
					<label>
						手机：<input type="text" name="username" value="${info.phone}" placeholder="用户名" required />
					</label>
				</dd>
			</label>
			<label>
				<dt>
					出生日期：
				</dt>
				<dd>
					<input type="text" name="birthday"  value="${info.birthday}" />
					<label>
						身份证号码：<input type="text" name="idCardNo" value="${info.idCardNo}" placeholder="身份证号码" required />
					</label>
				</dd>
			</label>

			<label>
				<dt>
					用户头像：<span class="required-note">*</span>
				</dt>
				<dd>
					<img id="preview" /> <input type="file" name="avatar"
						onchange="onAvatartUploadChange(this);" placeholder="请输入电子邮件"
						data-regexp="email" data-note="邮件可用于登录和查找密码等；不能与现有的邮件相同；注册后可以修改；"
						data-note2="推荐使用国内邮箱，如 QQ、163 等的邮箱" />
				</dd>
			</label>



			<label>
				<dt style="overflow: hidden; vertical-align: middle; display: table-cell; height: 100px; line-height: 100px;">个人简介
					:</dt>
				<dd>
					<textarea style="width: 100%; height: 100px; resize: none;" name="content">${info.content}</textarea>
				</dd>
			</label>

			<dt>性别：</dt>
			<dd>
				<label><input type="radio" value="1" name="sex" checked="checked" /> 男</label> 
				<label><input type="radio" value="0" name="sex" /> 女</label>
			</dd> 
		</dl>	
	
		<div style="text-align: center;">
			<button class="ajaxjs-btn">保存</button>
			<button class="ajaxjs-btn">修改密码</button>
			<button class="ajaxjs-btn" onclick="this.up('form').reset();return false;">复 位</button>
			<button class="ajaxjs-btn" onclick="del();return false;">
				<img src="${commonAssetIcon}/delete.gif" /> 删除${uiName}
			</button>
		</div>
	</form>
		
		<script>
			var form = document.querySelector('form');
			new ajaxjs.formValid(form);

			ajaxjs.xhr.form(form, function(json) {
				if(json)
					aj.alert.show(json.msg);
			});
			
			function del() {
				if (confirm('确定删除${uiName} \n${info.name}？'))
					ajaxjs.xhr.dele('.', function(json) {
						if (json && json.isOk) {
							aj.alert.show(json.msg);
							location.assign('../list/');
						}
					});
			}
		</script>
	</body>
</html>
