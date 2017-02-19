
<%@page pageEncoding="UTF-8" %>
<style>
.hide{
	
	display: none;
}
.htmlEditor {
	/* 工具条 */
	/* 颜色选择器 */
	
}

.htmlEditor ul.toolbar {
	border: 1px solid #C5C5C5;
	border-bottom: 0;
	background-color: #E8E7E4;
	width: 100%;
	height: 30px;
	margin: 0;
	box-sizing: border-box;
	padding: 3px 4px;
}

.htmlEditor ul.toolbar>li {
	list-style: none;
	float: left;
	cursor: pointer;
}

.htmlEditor ul.toolbar>li img {
	border: 1px solid transparent;
}

.htmlEditor ul.toolbar>li img:hover {
	border-right-color: #aaa;
	border-bottom-color: #aaa;
	border-top-color: #fff;
	border-left-color: #fff;
}

.htmlEditor ul.toolbar>li img:active {
	border-right-color: #f3f8fc;
	border-bottom-color: #f3f8fc;
	border-top-color: #ccc;
	border-left-color: #ccc;
}

.htmlEditor ul.toolbar>li ul li {
	padding: 3px;
	cursor: pointer;
}

.htmlEditor ul.toolbar>li ul li:hover {
	background-color: lightgray;
}

.htmlEditor .editorBody iframe, .htmlEditor .editorBody textarea {
	border: 1px solid #C5C5C5;
	border-top-width: 0;
	box-sizing: border-box;
	background-color: white;
	height: 283px;
	width: 100%;
}

.htmlEditor .editorBody textarea {
	resize: none;
}

.htmlEditor .colorPicker {
	width: 210px;
	border: 1px solid #D3D3D3;
	position: absolute;
}

.htmlEditor .colorPicker table {
	border-collapse: collapse;
}

.htmlEditor .colorPicker .colorhead {
	height: 23px;
	line-height: 23px;
	font-weight: bold;
	width: 100%;
}

.htmlEditor .colorPicker .colortitle {
	margin-left: 6px;
	font-size: 12px;
}

.htmlEditor .colorPicker .colorpanel td {
	border: 1px solid #000;
	height: 10px;
	width: 10px;
	overflow: hidden;
	font-size: 1px;
	cursor: pointer;
}

.dorpdown {
	position: relative;
}

.dorpdown:hover .fontfamilyChoser, .dorpdown .fontfamilyChoser:hover,
	.dorpdown:hover .fontsizeChoser, .dorpdown .fontsizeChoser:hover,
	.dorpdown:hover .colorPicker, .dorpdown .colorPicker:hover {
	display: block;
}

.dorpdown>div {
	display: none;
	position: absolute;
	top: 24px;
	top: 22px;
	left: 0;
	background-color: #f5f5f5;
	border: 1px solid lightgray;
	border-top: 0;
	padding: 5px;
	width: 230px;
}

.dorpdown .fontsizeChoser {
	top: inherit;
	right: 0;
}

.fontfamilyChoser a, .fontsizeChoser a {
	padding-right: 2px;
	display: block;
	padding-left: 2px;
	padding-bottom: 2px;
	color: #000000;
	line-height: 16px;
	padding-top: 2px;
	text-decoration: none;
}

.fontfamilyChoser a:hover, .fontsizeChoser a:hover {
	background: #e5e5e5;
}
</style>
<!-- HTML 编辑器控件 --> 
<script src="${bigfoot}/js/form/htmlEditor.js"></script>
<div class="htmlEditor">
	<ul class="toolbar">
		<li class="dorpdown">
			<img title="字体" src="${bigfoot}/asset/htmlEditor/4.gif" />
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
			<img title="字号" src="${bigfoot}/asset/htmlEditor/5.gif" />
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
	
		<li title="加粗"><img class="bold" src="${bigfoot}/asset/htmlEditor/6.gif" /></li>		
		<li><img title="斜体"   class="italic" src="${bigfoot}/asset/htmlEditor/7.gif" /></li>		
		<li><img title="下划线"  class="underline" src="${bigfoot}/asset/htmlEditor/8.gif" /></li>
		<li><img title="左对齐"  class="justifyleft" src="${bigfoot}/asset/htmlEditor/9.gif" /></li>
		<li><img title="中间对齐" class="justifycenter" src="${bigfoot}/asset/htmlEditor/10.gif" /></li>
		<li><img title="右对齐"  class="justifyright" src="${bigfoot}/asset/htmlEditor/11.gif" /></li>
		<li><img title="数字编号" class="insertorderedlist" src="${bigfoot}/asset/htmlEditor/12.gif" /></li>
		<li><img title="项目编号" class="insertunorderedlist" src="${bigfoot}/asset/htmlEditor/13.gif" /></li>
		<li><img title="增加缩进" class="outdent" src="${bigfoot}/asset/htmlEditor/14.gif" /></li>
		<li><img title="减少缩进" class="indent" src="${bigfoot}/asset/htmlEditor/15.gif" /></li>
		<li class="dorpdown">
			<img title="字体颜色" src="${bigfoot}/asset/htmlEditor/16.gif" />
			<div class="fontColor colorPicker">
				<script>
					document.write(bf_EditHTML.createColorPickerHTML());
				</script>
			</div>
		</li>
		<li class="dorpdown">
			<img title="背景颜色" class="backColor" src="${bigfoot}/asset/htmlEditor/17.gif" />
			<div class="bgColor colorPicker">
				<script>
					document.write(bf_EditHTML.createColorPickerHTML());
				</script>
			</div>
		</li>
		<li>
			<img title="增加链接" class="createLink" src="${bigfoot}/asset/htmlEditor/18.gif" />
		</li>
		<li>
			<img title="增加图片" class="insertImage" src="${bigfoot}/asset/htmlEditor/19.gif" />
		</li>
		<li>
			<span class="switchMode">HTML</span>
		</li>
	</ul>

	<div class="editorBody">
<!-- 	<iframe src="about:blank"></iframe> -->
		<iframe src="blankpage.jsp?basePath=${basePath}"></iframe>
		<textarea class="hide">${contentBody}</textarea>
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