<%@ page pageEncoding="UTF-8"%>
		<menu>
			<ul>
				<li><a href="${ctx}/user/center/home/">概览</a></li>
				<li><a href="${ctx}/user/center/info/">会员信息</a></li>
				<li><a href="${ctx}/user/center/loginInfo/">账户安全</a></li>
				<li><a href="${ctx}/user/center/feedback/">留言反馈</a></li>
				<li><a href="${ctx}/user/login/logout/">退出登录</a></li>
			</ul>
		</menu>
		<script>
			// find current hightlight
			var arr = document.location.href.split('/');
			if('' === arr[arr.length -1]) {
				arr.pop();
			}
			
			var last = arr.pop(), path = arr.pop() + '/' + last; 
			var a = document.querySelectorAll('menu ul li a');
			
			for(var i = 0, j = a.length; i < j; i++) {
				if(document.location.href.indexOf(a[i].href) != -1) {
					a[i].parentNode.classList.add('selected');
					break;
				}
			}
		</script>