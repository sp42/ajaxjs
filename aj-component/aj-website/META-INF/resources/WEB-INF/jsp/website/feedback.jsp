		<style>
			.aj-form {
				margin:0 auto;
			}
			.aj-form td{
				padding:2% 0;
			
			}
			.c{
				text-align:center;
			}
		</style>
		<form class="aj-form feedback" method="POST" action="${ctx}/feedback/">
			<table width="80%" align="center">
				<tr>
					<td width="10%"><tags:i18n zh="名字" eng="Your Name" /></td>
					<td><input type="text" name="name" required value="${userName}" />
						<input type="hidden" name="userId" value="${userId}" />
					</td>
					
					<td rowspan="6" valign="top" width="65%" style="padding-left:8%;">
						<tags:i18n zh="留言信息" eng="Message" /><br /><br />
						<textarea name="content" rows="8" cols="45" required></textarea>
					</td>
				</tr>
				<tr>
					<td><tags:i18n zh="联系方式" eng="Contact" /></td>
					<td><input type="text" name="contact" placeholder="请输入你的手机或电子邮箱"  value="${userPhone}" required /></td>
				</tr>
			</table>
	
			<div class="aj-btnsHolder">
				<button>提交</button>
			</div>
		</form>
		
		<script>
			aj.xhr.form('.aj-form', j => {
				if(j && j.isOk) {
					aj.alert('留言成功！');
				} else
					aj.alert(j.msg)
			}, {
				googleReCAPTCHA: '${aj_allConfig.security.GoogleReCAPTCHA.siteId}'
			});
			
			new Vue({el: '.captcha'});
		</script>