<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "HTML 在线编辑器");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<h4>HTML 在线编辑器</h4>
	<p>
		依赖 iframe 指向空白的页面 <a href="htmleditor_blankpage.jsp">htmleditor_blankpage.jsp</a>
	</p>
	<section class="center">
		<!-- HTML 编辑器控件 -->
		<script src="${pageContext.request.contextPath}/asset/common/js/component/htmlEditor.js"></script>
		
		<div class="htmlEditor">
			<ul class="toolbar">
				<li class="dorpdown"><span title="字体" class="bg-4"></span>
					<div class="fontfamilyChoser">
						<a href="javascript:;" style="font-family: '宋体'">宋体</a> <a
							href="javascript:;" style="font-family: '黑体'">黑体</a> <a
							href="javascript:;" style="font-family: '楷体'">楷体</a> <a
							href="javascript:;" style="font-family: '隶书'">隶书</a> <a
							href="javascript:;" style="font-family: '幼圆'">幼圆</a> <a
							href="javascript:;" style="font-family: 'Microsoft YaHei'">Microsoft
							YaHei</a> <a href="javascript:;" style="font-family: Arial">Arial</a>
						<a href="javascript:;" style="font-family: 'Arial Narrow'">Arial
							Narrow</a> <a href="javascript:;" style="font-family: 'Arial Black'">Arial
							Black</a> <a href="javascript:;" style="font-family: 'Comic Sans MS'">Comic
							Sans MS</a> <a href="javascript:;" style="font-family: Courier">Courier</a>
						<a href="javascript:;" style="font-family: System">System</a> <a
							href="javascript:;" style="font-family: 'Times New Roman'">Times
							New Roman</a> <a href="javascript:;" style="font-family: Verdana">Verdana</a>
					</div></li>
				<li class="dorpdown"><span title="字号" class="bg-5"></span>

					<div class="fontsizeChoser">
						<a href="javascript:;"
							style="font-size: xx-small; line-height: 120%">极小</a> <a
							href="javascript:;" style="font-size: x-small; line-height: 120%">特小</a>
						<a href="javascript:;" style="font-size: small; line-height: 120%">小</a>
						<a href="javascript:;"
							style="font-size: medium; line-height: 120%">中</a> <a
							href="javascript:;" style="font-size: large; line-height: 120%">大</a>
						<a href="javascript:;"
							style="font-size: x-large; line-height: 120%">特大</a> <a
							href="javascript:;"
							style="font-size: xx-large; line-height: 140%">极大</a>
					</div></li>

				<li><span title="加粗" class="bold bg-6"></span></li>
				<li><span title="斜体" class="italic bg-7"></span></li>
				<li><span title="下划线" class="underline bg-8"></span></li>
				<li><span title="左对齐" class="justifyleft bg-9"></span></li>
				<li><span title="中间对齐" class="justifycenter bg-10"></span></li>
				<li><span title="右对齐" class="justifyright bg-11"></span></li>
				<li><span title="数字编号" class="insertorderedlist bg-12"></span></li>
				<li><span title="项目编号" class="insertunorderedlist bg-13"></span></li>
				<li><span title="增加缩进" class="outdent bg-14"></span></li>
				<li><span title="减少缩进" class="indent bg-15"></span></li>

				<li class="dorpdown"><span title="字体颜色" class="bg-16"></span>
					<div class="fontColor colorPicker">
						<script>
							document.write(ajaxjs_HtmlEditor.createColorPickerHTML());
						</script>
					</div></li>
				<li class="dorpdown"><span title="背景颜色" class="backColor bg-17"></span>
					<div class="bgColor colorPicker">
						<script>
							document.write(ajaxjs_HtmlEditor.createColorPickerHTML());
						</script>
					</div></li>
				<li><span title="增加链接" class="createLink bg-18"></span></li>
				<li><span title="增加图片" class="insertImage bg-19"></span></li>
				<li><span class="switchMode noBg">HTML</span></li>
			</ul>

			<div class="editorBody">
				<iframe src="htmleditor_blankpage.jsp"></iframe>
				<textarea class="hide" name="${name}"></textarea>
			</div>
		</div>
		
		
		<script>
			var htmlEditor = new ajaxjs_HtmlEditor(document.querySelector('.htmlEditor'));
			htmlEditor.setValue('Hello world');
		</script>
		<!-- // HTML 编辑器控件 -->

	</section>
	<p>代码如下：</p>
	<pre class="prettyprint">
// 须引入 component/htmlEditor.js
var htmlEditor = new ajaxjs_HtmlEditor(document.querySelector('.htmlEditor'));
htmlEditor.setValue('Hello world');</pre>
	<%@include file="../public/footer.jsp"%>
</body>
</html>