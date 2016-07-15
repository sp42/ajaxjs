<%@tag pageEncoding="UTF-8" description="管理界面的公用 header"%>
<%@attribute name="pageTitle" 	fragment="false" required="false" description="页面标题"%> 
<%@attribute name="maxWin" 	fragment="false" type="Boolean"	required="false" description="是否需要'最大化窗体'"%> 
		<header>
			<div>
				<div class="memberNav">
					<jsp:doBody />
					  
				 	<%if(maxWin == null || maxWin == true){ %>
				 	<a href="javascript:maxWin();" >最大化窗体</a> |
				 	<%}%>
				 	
				 	<a href="#" target="_blank">新窗口打开</a>
				</div>
				<span>${pageTitle}</span>
			</div>
		</header>
		<script>
			function maxWin() {
				var panel = document.querySelector('.panel');
				if (panel) {
					panel.style.width = '95%';
				}
			}
		</script>