<%@page pageEncoding="UTF-8" import="com.ajaxjs.web.upload.*"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "表单元素");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<h4>按钮 Button</h4>
	
	
	<p>class="ajaxjs-btn" 或 class="ajaxjs-btn-1"，class="ajaxjs-btn-1
		ajaxjs-disable"</p>
	<div class="centerLimitWidth">

		<button class="ajaxjs-btn">按钮</button>

		<button class="ajaxjs-btn-1">按钮</button>
		<br />
		<button class="ajaxjs-btn-1 ajaxjs-disable">按钮（禁止状态）</button>
	</div>


	<h4>输入框 Input Field</h4>
	
	<p>class="ajaxjs-inputField"、class="ajaxjs-inputField
		ajaxjs-disable"</p>
	<div class="centerLimitWidth" style="padding-left: 2%;">
		<input class="ajaxjs-inputField" type="text" value="文本输入框" /> <br />
		<br /> <input class="ajaxjs-inputField ajaxjs-disable" type="text"
			value="文本输入框（禁止状态）" />

	</div>

	<h4>文本框 Textarea</h4>
	
	<p>class="ajaxjs-select"</p>
	<div class="centerLimitWidth" style="padding-left: 2%;">
		<textarea class="ajaxjs-inputField" rows="15" cols="20"
			style="resize: none; padding-top: 5px; height: 100px; width: 80%;-webkit-overflow-scrolling : touch"
			name="intro">“北上广深同比涨幅高达20%，26个主要城市涨幅超10%，69个城市房价上涨……”2013年全国房价涨声一片，这与年初各地制定的房价控制目标相差甚远。 经济学者马光远[微博]表示，房价调控目标是政府对于百姓的“承诺”，没有完成目标而“爽约”，蒙混过关只会丧失公信力。这与年初各地制定的房价控制目标相差甚远。 经济学者马光远[微博]表示，房价调控目标是政府对于百姓的“承诺”，没有完成目标而“爽约”，蒙混过关只会丧失公信力。</textarea>
	</div>

	<h4>下拉列表 DropdownList</h4>
	
	<p>class="ajaxjs-select"</p>
	<div class="centerLimitWidth" style="padding-left: 2%;">
		<select name="catalog" class="ajaxjs-select" style="width:250px;">
			<option value="1">公司新闻</option>
			<option value="2" selected="selected">行业动态</option>
			<option value="3">媒体报道</option>
		</select>
	</div>
	<p>class="ajaxjs-select[multiple]"</p>
	<div class="centerLimitWidth" style="padding-left: 2%;">
	<select class="ajaxjs-select" multiple style="width:250px;height:100px;">
			<option>多项选择</option>
			<option>1</option>
			<option>2</option>
		</select>
	</div>
	
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
<%
if(request.getMethod().equals("POST")) {
	//UploadRequest r = new UploadRequest();
	//r.setRequest(request);
	//new Upload().upload(r);
	//if(r.isOk()) {
UploadRequest r = new UploadRequest(request);
if(r.upload().isOk){
%>
upload ok
<% 
	}
}
%>
	<div class="center">
		<form action="../../admin/news/imgUpload/do.do" method="POST" enctype="multipart/form-data">
			<input type="file"	 name="uploadfile" /> 
			<input type="submit" value="upload" /> 
		</form>
	</div>
	<script src="${pageContext.request.contextPath}/asset/common/js/component/upload.js"></script>
	<%@include file="../public/footer.jsp"%>
</body>
</html>