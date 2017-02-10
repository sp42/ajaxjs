<%@tag pageEncoding="UTF-8" description="HTML 编辑器控件"%>
<%@attribute name="name"     required="false" type="String" description="表单 name，字段名"%>
<%@attribute name="basePath" required="false" type="String" description="指定 iframe src 静态资源的路径"%>

<!-- HTML 编辑器控件 --> 
<script src="${pageContext.request.contextPath}/asset/js/form/htmlEditor.js"></script>
<div class="htmlEditor">
	<ul class="toolbar">
		<li class="dorpdown">
			<img title="字体" src="${pageContext.request.contextPath}/asset/images/htmlEditor/4.gif" />
			<div class="fontfamilyChoser">
				<a href="javascript:;" style="font-family: '宋体'">宋体</a>
				<a href="javascript:;" style="font-family: '黑体'">黑体</a>
				<a href="javascript:;" style="font-family: '楷体'">楷体</a>
				<a href="javascript:;" style="font-family: '隶书'">隶书</a>
				<a href="javascript:;" style="font-family: '幼圆'">幼圆</a>
				<a href="javascript:;" style="font-family: 'Microsoft YaHei'">Microsoft YaHei</a>
				<a href="javascript:;" style="font-family: Arial">Arial</a>
				<a href="javascript:;" style="font-family: 'Arial Narrow'">Arial Narrow</a>
				<a href="javascript:;" style="font-family: 'Arial Black'">Arial Black</a>
				<a href="javascript:;" style="font-family: 'Comic Sans MS'">Comic Sans MS</a>
				<a href="javascript:;" style="font-family: Courier">Courier</a>
				<a href="javascript:;" style="font-family: System">System</a>
				<a href="javascript:;" style="font-family: 'Times New Roman'">Times New Roman</a>
				<a href="javascript:;" style="font-family: Verdana">Verdana</a>
			</div>
		</li>		
		<li class="dorpdown">
			<img title="字号" src="${pageContext.request.contextPath}/asset/images/htmlEditor/5.gif" />
			<div class="fontsizeChoser">
				<a href="javascript:;" style="font-size: xx-small; line-height: 120%">极小</a>
				<a href="javascript:;" style="font-size: x-small;  line-height: 120%">特小</a>
				<a href="javascript:;" style="font-size: small;    line-height: 120%">小</a>
				<a href="javascript:;" style="font-size: medium;   line-height: 120%">中</a>
				<a href="javascript:;" style="font-size: large;    line-height: 120%">大</a>
				<a href="javascript:;" style="font-size: x-large;  line-height: 120%">特大</a>
				<a href="javascript:;" style="font-size: xx-large; line-height: 140%">极大</a>
			</div>
		</li>		
	
		<li title="加粗"><img class="bold" src="${pageContext.request.contextPath}/asset/images/htmlEditor/6.gif" /></li>		
		<li><img title="斜体"   class="italic" src="${pageContext.request.contextPath}/asset/images/htmlEditor/7.gif" /></li>		
		<li><img title="下划线"  class="underline" src="${pageContext.request.contextPath}/asset/images/htmlEditor/8.gif" /></li>
		<li><img title="左对齐"  class="justifyleft" src="${pageContext.request.contextPath}/asset/images/htmlEditor/9.gif" /></li>
		<li><img title="中间对齐" class="justifycenter" src="${pageContext.request.contextPath}/asset/images/htmlEditor/10.gif" /></li>
		<li><img title="右对齐"  class="justifyright" src="${pageContext.request.contextPath}/asset/images/htmlEditor/11.gif" /></li>
		<li><img title="数字编号" class="insertorderedlist" src="${pageContext.request.contextPath}/asset/images/htmlEditor/12.gif" /></li>
		<li><img title="项目编号" class="insertunorderedlist" src="${pageContext.request.contextPath}/asset/images/htmlEditor/13.gif" /></li>
		<li><img title="增加缩进" class="outdent" src="${pageContext.request.contextPath}/asset/images/htmlEditor/14.gif" /></li>
		<li><img title="减少缩进" class="indent" src="${pageContext.request.contextPath}/asset/images/htmlEditor/15.gif" /></li>
		<li class="dorpdown">
			<img title="字体颜色" src="${pageContext.request.contextPath}/asset/images/htmlEditor/16.gif" />
			<div class="fontColor colorPicker">
				<script>
					document.write(bf_EditHTML.createColorPickerHTML());
				</script>
			</div>
		</li>
		<li class="dorpdown">
			<img title="背景颜色" class="backColor" src="${pageContext.request.contextPath}/asset/images/htmlEditor/17.gif" />
			<div class="bgColor colorPicker">
				<script>
					document.write(bf_EditHTML.createColorPickerHTML());
				</script>
			</div>
		</li>
		<li>
			<img title="增加链接" class="createLink" src="${pageContext.request.contextPath}/asset/images/htmlEditor/18.gif" />
		</li>
		<li>
			<img title="增加图片" class="insertImage" src="${pageContext.request.contextPath}/asset/images/htmlEditor/19.gif" />
		</li>
		<li>
			<span class="switchMode">HTML</span>
		</li>
	</ul>

	<div class="editorBody">
<!-- 	<iframe src="about:blank"></iframe> -->
<!--  	<iframe src="${pageContext.request.contextPath}/util_service/htmleditor_iframe?basePath=${basePath}"></iframe>-->	
		<iframe src="${pageContext.request.contextPath}/common_jsp/htmleditor_iframe.jsp?basePath=${basePath}"></iframe>
		<textarea class="hide" name="${name}"><jsp:doBody /></textarea>
	</div>
</div>

<script>
	var htmlEditor = new bf_EditHTML(document.querySelector('.htmlEditor'));
	// htmlEditor.setValue('dfdfdf'); 
</script>
<!-- // HTML 编辑器控件 -->

<%--
保存为 blankpage.jsp 文件放在同级目录下
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8">
		<title>New Document</title>
		<style type="text/css">
			body {
				font-size: 12px;
				padding-top: 3px;
				padding-left: 3px;
				padding-right: 0px;
				margin: 0px
			}
			
			p {
				margin: 0;
				padding: 0;
			}
		</style>
		<base href="${param.basePath}/" />
	</head>
	<body></body>
</html>
--%>