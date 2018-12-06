<%@page pageEncoding="UTF-8"%>
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
	<div>
		<input class="ajaxjs-inputField" type="text" value="文本输入框" /> <br />
		<br /> <input class="ajaxjs-inputField ajaxjs-disable" type="text"
			value="文本输入框（禁止状态）" />
	</div>

	<h4>文本框 Textarea</h4>
	<p>class="ajaxjs-select"</p>
	<div>
		<textarea class="ajaxjs-inputField" rows="15" cols="20"
			style="resize: none; padding-top: 5px; height: 100px; width: 80%;-webkit-overflow-scrolling : touch"
			name="intro">“北上广深同比涨幅高达20%，26个主要城市涨幅超10%，69个城市房价上涨……”2013年全国房价涨声一片，这与年初各地制定的房价控制目标相差甚远。 经济学者马光远[微博]表示，房价调控目标是政府对于百姓的“承诺”，没有完成目标而“爽约”，蒙混过关只会丧失公信力。这与年初各地制定的房价控制目标相差甚远。 经济学者马光远[微博]表示，房价调控目标是政府对于百姓的“承诺”，没有完成目标而“爽约”，蒙混过关只会丧失公信力。</textarea>
	</div>

	<h4>下拉列表 DropdownList</h4>
	<p>class="ajaxjs-select"</p>
	<select name="catalog" class="ajaxjs-select" style="width:250px;">
		<option value="1">公司新闻</option>
		<option value="2" selected="selected">行业动态</option>
		<option value="3">媒体报道</option>
	</select>
	<br/>
	<br/>
	<select class="ajaxjs-select" multiple style="width:250px;height:100px;">
			<option>多项选择</option>
			<option>1</option>
			<option>2</option>
		</select>
	
	<h4>自定义文件上传按钮</h4>
	<p>通过 label 标签 for 属性关联具体的 input[type=file] 触发本地 file picker。input[type=file] 本身隐藏。</p>
	<div style="width: 100px;">
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
		<form action="../../admin/news/imgUpload/do.do" method="POST" enctype="multipart/form-data">
			<input type="file"	 name="uploadfile" /> 
			<input type="submit" value="upload" /> 
		</form>

	<script src="${pageContext.request.contextPath}/asset/common/js/component/upload.js"></script>
	
	<h4>HTML 编辑器</h4>
	<p>
		依赖 iframe 指向空白的页面 <a href="htmleditor_blankpage.jsp">htmleditor_blankpage.jsp</a>
	</p>
	<section class="center">
		<!-- HTML 编辑器控件 -->
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
		
		<script src="${pageContext.request.contextPath}/asset/common/js/component/htmlEditor.js"></script>	
		<script>
			var htmlEditor = new ajaxjs_HtmlEditor(document.querySelector('.htmlEditor'));
			htmlEditor.setValue('Hello world');
		</script>
		<!-- // HTML 编辑器控件 -->

	</section>
	<p>用法如下：</p>
	<pre class="prettyprint">
// 须引入 component/htmlEditor.js
var htmlEditor = new ajaxjs_HtmlEditor(document.querySelector('.htmlEditor'));
htmlEditor.setValue('Hello world');</pre>

	<h4>日历控件</h4>
	<p>TODO</p>
	<br />
	<section class="center">
		<div class="calendar" style="margin:0 auto;">
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
		<script src="${pageContext.request.contextPath}/asset/common/js/component/calendar.js"></script>
	</section>
	