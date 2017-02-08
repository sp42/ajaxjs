<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
	<commonTag:head lessFile="/asset/bigfoot/asset/less/user.less" title="登录信息" />
<body>
	<commonUI:adminHeader pageTitle="登录信息" />
	<style>
		body{
				background-color: #f4f4f4;
		
		}
		fieldset{
			background-color: white;
		}
	</style>
	<div class="center">
		<fieldset>
			<legend>修改密码</legend>
			<form class="aj">
				<label>
					原密码：
					<input type="text" name="username" placeholder="请输入旧密码"
					data-note="虽已登录了，但为安全起见，请先输入原密码方可修改之"
					 />
				</label>
				<label>
					新密码：
					<input type="password" name="password" placeholder="请输入新的密码" 
					data-note="密码要求 6 位长度，混合英文和数字" />
				</label>
				<label>
					新密码二次确认：
					<input type="password" name="password" placeholder="请重复刚才输入的新密码" />
				</label>
				<div> 
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="修改密码" /> &nbsp;&nbsp;&nbsp;&nbsp;
				</div>
			</form>
			
			<script src="${bigfoot}/js/libs/md5.js"></script>
			<script>
				var form = new bf_form(document.querySelector('form'), {
					isCommonAfterSubmit : true
				});
				form.on('beforeSubmit', function(){
		            var oldPassword = this.formEl.querySelector('input[name=oldPassword]');
		            var password  = this.formEl.querySelector('input[name=password]'),
		                password2 = this.formEl.querySelector('input[name=newPassword2]');
		
		            if(!oldPassword.value || !password.value || !password2.value){
		            	alert('请输入密码！');
		            	return false;
		            }
		
		            if(password.value != password2.value){
		         	   alert('两次输入的密码不一致哦~');
		         	   return false;
		            }
		            
		            // MD5
		            oldPassword.value = md5(oldPassword.value);
		            password.value = password2.value = md5(password.value);     
		        });
			</script>
		</fieldset>
		
		<style>
	
		</style>
		<fieldset class="openAuth">
			<legend>第三方认证账号绑定</legend>
		 	<p>高亮图标表示已绑定账户，灰色未绑定。</p>
		 	
		 	<ul>
		 		<li>
		 			<img src="../../bigfoot/asset/images/user/weibo.png" />
		 			<h4>解除绑定</h4>
		 		</li>
		 		<li>
		 			<img src="../../bigfoot/asset/images/user/qq_un.png" />
		 			<h4>添加绑定</h4>
		 		</li>
		 	</ul>
		</fieldset>
	</div>
	
	<script src="../user/form.js"></script>
</body>
</html>