<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "滑动杆");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<h4>滑动杆控件</h4>
	<section class="center">
		<div class="slider">
			<button>A</button>
		</div>
		<script src="${pageContext.request.contextPath}/asset/common/js/component/slider.js"></script>
	</section>
	<%@include file="../public/footer.jsp"%>
</body>
</html>