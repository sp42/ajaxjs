<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "日历控件");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<h4>日历控件</h4>
	<section class="center">
		<div class="calendar">
			<div class="selectYearMonth">
				<a href="#" class="preYear">&lt;</a> <select>
					<option value="1">一月</option>
					<option value="2">二月</option>
					<option value="3">三月</option>
					<option value="4">四月</option>
					<option value="5">五月</option>
					<option value="6">六月</option>
					<option value="7">七月</option>
					<option value="8">八月</option>
					<option value="9">九月</option>
					<option value="10">十月</option>
					<option value="11">十一月</option>
					<option value="12">十二月</option>
				</select> <a href="#" class="nextYear">&gt;</a>
			</div>
			<div class="showCurrentYearMonth">
				<span class="showYear"></span>/<span class="showMonth"></span>
			</div>
			<table>
				<thead>
					<tr>
						<td>日</td>
						<td>一</td>
						<td>二</td>
						<td>三</td>
						<td>四</td>
						<td>五</td>
						<td>六</td>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>


		<div class="slider">
			<button>A</button>
		</div>
		<script src="${pageContext.request.contextPath}/asset/common/js/component/calendar.js"></script>
	</section>
	<%@include file="../public/footer.jsp"%>
</body>
</html>