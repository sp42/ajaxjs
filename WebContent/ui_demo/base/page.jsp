<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "页面制作");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<h4>细线表格 Table</h4>
	<hr class="ajaxjs-hr" />
	<p>快速制作 1px 表格边框，为需要设置的 table 元素 class 即可。 class="ajaxjs-borderTable"</p>
	<div class="centerLimitWidth">
		<table class="ajaxjs-borderTable"  width="80%" align="center">
			<tr>
				<th>姓名</th>
				<td>Bill Gates</td>
			</tr>
			<tr>
				<th rowspan="2">电话</th>
				<td>555 77 854</td>
			</tr>
			<tr>
				<td>555 77 855</td>
			</tr>
		</table>
	</div>
	<p>class="ajaxjs-niceTable"</p>
	<div class="centerLimitWidth">
		<table class="ajaxjs-niceTable" width="80%" align="center">
			<thead>
				<tr>
					<th>Month</th>
					<th>Savings</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>January</td>
					<td>$100</td>
				</tr>
				<tr>
					<td>February</td>
					<td>$80</td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td>Sum</td>
					<td>$180</td>
				</tr>
			</tfoot>
		</table>
	</div>
	<h4>分页</h4>
	<hr class="ajaxjs-hr" />
	<div class="centerLimitWidth" style="width: 98%;">
		<div class="pager">
			<section class="pageInfo">
				<a href="?start=0&amp;limit=2">上一页</a> <a href="#"
					style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>
				<a href="?start=4&amp;limit=2">下一页</a>
				<div class="info" style="vertical-align: bottom;">
					页数：2/6 记录数：2/11
					<form style="display: inline-block; vertical-align: bottom;"
						method="GET">
						每页记录数： <input size="4" title="输入一个数字确定每页记录数" type="text"
							name="limit" value="2"
							style="text-align: center; width: 40px; height: 22px; float: none;">
						<!-- 其他参数 -->
						<input type="hidden" name="start" value="2">
					</form>
					跳转： <select style="text-align: center; width: 40px; height: 22px;"
						class="select_1">
						<option value="0">1</option>
						<option value="2">2</option>
						<option value="4">3</option>
						<option value="6">4</option>
						<option value="8">5</option>
						<option value="10">6</option>
					</select>
				</div>
			</section>
		</div>
	</div>

	<h4>网站所在位置</h4>
	<hr class="ajaxjs-hr" />
	<div class="centerLimitWidth">
		<nav class="anchor">
			您在位置 ：<a href="#">首页 </a> » <a href="/grand/aboutus/">走进公司</a> » <a
				href="/grand/aboutus/intro/">公司简介</a>
		</nav>
	</div>

	<h4>网站版权</h4>
	<hr class="ajaxjs-hr" />
	<p class="centerLimitWidth">Copyright © 2014-2016 版权所有 All Rights
		Reserved 粤ICP备10007080号-1</p>

	<h4>分隔样式</h4>
	<hr class="ajaxjs-hr" />
	<p>利用 &lt;fieldset&gt; 制作如下样式。</p>
	<div class="centerLimitWidth" style="width: 98%; padding-left: 1%;">
		<fieldset
			style="border-bottom: 0; border-left: 0; border-right: 0; border-top: 1px solid lightgray; width: 90%;">
			<legend align="center" style="text-align: center;">
				<img src="../../asset/common/images/gs.png" />
			</legend>
		</fieldset>
	</div>
	<%@include file="../public/footer.jsp"%>
</body>
</html>