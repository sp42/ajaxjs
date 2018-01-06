<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "高级表单元素");
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

	<h4>Tag 选择器</h4>


	<section class="center">
		关键字：<input value="${info.keywords}" name="keywords" type="text"
			class="my-inputField" size="40" />
	</section>
	<style>
input {
	height: 30px;
}

label {
	position: relative;
}

.keywords_item {
	position: absolute;
	top: 2px;
	border: 1px solid red;
	border-radius: 2px;
	background-color: lightgray;
	padding-left: 4px;
	padding-right: 15px;
	text-decoration: none !important;
}

.keywords_item .close {
	/* 		 	float:right; */
	border-radius: 8px;
	background-color: white;
	height: 16px;
	width: 16px;
	text-align: center;
	position: absolute;
	right: -8px;
	top: -8px;
	border: 1px solid red;
	color: red;
	line-height: 16px;
	font-weight: bold;
}
</style>
	<script>
		var input = document.querySelector('input[name=keywords]'), label = input.parentNode;
		label.style.position = "relative";
		var span = document.createElement('span');
		span.innerHTML = label.childNodes[0].nodeValue;
		document.body.appendChild(span);

		var a = document.createElement('a');
		a.className = 'keywords_item';
		a.href = "#"
		a.innerHTML = "dasdsa" + '<div class="close">×<div>';
		a.querySelector('.close').onclick = closeKeyword;

		function closeKeyword(e) {
			var el = e.target;
			while (el.tagName != 'LABEL') {
				el = el.parentNode;

				if (!el || el.tagName == 'LABEL') {
					break;
				}
			}
			el && el.removeChild(el.querySelector('a'));
			return false;
		}

		label.appendChild(a);

		var textWidth = span.offsetWidth;

		a.style.left = (textWidth + 5) + 'px';
		a.style.top = '2px';
		//alert(span.offsetWidth);
		document.body.removeChild(span);
	</script>

	<h4>自定义文件上传按钮</h4>
	<p>通过 label 标签 for 属性关联具体的 input[type=file] 触发本地 file picker。input[type=file] 本身隐藏。</p>
	<div class="centerLimitWidth" style="width: 100px;">
		<input id="input_file_molding" type="file" class="hide" name="uploadfile" /> 
		<label for="input_file_molding">
			<div
				style="border: 1px solid lightgray; border-radius: 3px; text-align: center; color: gray; cursor: pointer; font-size: .8rem; padding: 10px;">
				<div style="font-size: 2rem;">✚</div>
				点击选择图片
			</div>
		</label>
	</div>
	<h4>图片上传预览</h4>
	
	<input type="file" name="uploadfile" /> 
	<script src="${pageContext.request.contextPath}/asset/common/js/component/upload.js"></script>
	<%@include file="../public/footer.jsp"%>
</body>
</html>