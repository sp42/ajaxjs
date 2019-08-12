<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tagfiles" tagdir="/WEB-INF/tags/"%>
<tagfiles:common type="content" banner="../../images/contact.jpg" showPageHelper="true">
	<jsp:attribute name="body">
			<form method="POST" action="../saveFeedback" class="feedback">
				<dl>
					<label>
						<dt>名称：<span class="required-note">*</span>
						</dt>
						<dd>
							<input type="text" name="name" placeholder="用户名" required
								data-regexp="Username" data-note="用户名等于账号名；不能与现有的账号名相同；注册后不能修改；"
								data-note2="用户名22等于账号名；不能与现有的账号名相同；注册后不能修改；" />
						</dd>
					</label>
				</dl>
				<dl>
			
				<label>
					<dt>电子邮件：</dt>
					<dd>				
						<input type="text" name="email" placeholder="请输入电子邮件" pattern="[^@]+@[^@]+\.[a-zA-Z]{2,6}"
								data-regexp="email" data-note="邮件可用于登录和查找密码等；不能与现有的邮件相同；注册后可以修改；"
								data-note2="推荐使用国内邮箱，如 QQ、163 等的邮箱" />
					</dd>
				</label>
				</dl>
				<dl>
				<label>
					<dt>手机：</dt>
					<dd>				
						<input type="text" name="phone" placeholder="请输入手机" pattern="^(0|86|17951)?1[0-9]{10}" 
								data-regexp="email" data-note="邮件可用于登录和查找密码等；不能与现有的邮件相同；注册后可以修改；"
								data-note2="推荐使用国内邮箱，如 QQ、163 等的邮箱" />
					</dd>
				</label>
				</dl>
				<dl>
					<label>
						<dt>留言：<span class="required-note">*</span>
						</dt>
						<dd>	
							<textarea name="content" rows="5" cols="20" placeholder="请输入留言" style="height:150px;"></textarea>
						</dd>
					</label>
				</dl>
				<dl>
					<label>
						<dt>验证码：<span class="required-note">*</span>
						</dt>
						<dd>	
							<commonTag:form type="captcha" />
						</dd>
					</label>
				</dl>
				<dl>
					<label>
						<dt></dt>
						<dd>	
							<button>提交</button>
						</dd>
					</label>
				</dl>
			</form>
	
			<script>
				new ajaxjs.formValid(document.querySelector('form.feedback'));
			</script>
			

			<button onclick="msgbox();">dfsf</button>
			<div class="modal hide" onclick="msgbox(arguments[0]);">
				<div>
					kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkdsds
				</div>
		</div>
	</jsp:attribute>
</tagfiles:common>
