<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "简易选项卡 Tab"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
		<br />
		<div class="p">
	    	<h3>简易选项卡 Tab</h3>
    	</div>
    	<div class="p">
    	 <ul>
    	 	<li>设有水平（常见）和垂直 Tab</li>
    	 	<li>切换原理为控制元素的 display:block/none;，原理比较简单</li>
    	 	<li>使用 display:block/none; 附带的好处是高度自适应</li>
    	 	<li>要将 Tab 边框样式无缝结合，需要 CSS 的一定技巧性</li>
    	 	<li>提供 beforeSwitch、afterSwitch、afterRender(i, btn, showTab)（只是在第一次触发）事件</li>
    	 </ul>
    	</div>
		<div class="p">
	    	<h3>垂直 Tab</h3>
    	</div>
    	<div class="center" style="padding:5px;">
    	
			<!-- HTML 编辑器控件 --> 
<div class="htmlEditor">
	<ul class="toolbar">
		<li class="dorpdown">
			<span  title="字体" class="bg-4"></span>
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
		<span  title="字号" class="bg-5" ></span>
			
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
	
		<li><span title="加粗"    class="bold bg-6"></span></li>		
		<li><span title="斜体"    class="italic bg-7"></span></li>		
		<li><span title="下划线"   class="underline bg-8"></span></li>
		<li><span title="左对齐"   class="justifyleft bg-9"></span></li>
		<li><span title="中间对齐" class="justifycenter bg-10"></span></li>
		<li><span title="右对齐"   class="justifyright bg-11"></span></li>
		<li><span title="数字编号" class="insertorderedlist bg-12"></span></li>
		<li><span title="项目编号" class="insertunorderedlist bg-13"></span></li>
		<li><span title="增加缩进" class="outdent bg-14"></span></li>
		<li><span title="减少缩进" class="indent bg-15"></span></li>

		<li class="dorpdown">
			<span title="字体颜色"  class="bg-16"></span>
			<div class="fontColor colorPicker">
				<script>
					document.write(ajaxjs.HtmlEditor.createColorPickerHTML());
				</script>
			</div>
		</li>
		<li class="dorpdown">
			<span title="背景颜色" class="backColor bg-17" ></span>
			<div class="bgColor colorPicker">
				<script>
					document.write(ajaxjs.HtmlEditor.createColorPickerHTML());
				</script>
			</div>
		</li>
		<li>
			<span title="增加链接" class="createLink bg-18" ></span>
		</li>
		<li>
			<span title="增加图片" class="insertImage bg-19" ></span>
		</li>
		<li>
			<span class="switchMode noBg">HTML</span>
		</li>
	</ul>

	<div class="editorBody">	
		<iframe src="htmleditor_blankpage.jsp"></iframe>
		<textarea class="hide" name="${name}"></textarea>
	</div>
</div>
<script>
	var htmlEditor = new ajaxjs.HtmlEditor(document.querySelector('.htmlEditor'));
	htmlEditor.setValue('Hello world'); 
</script>
<!-- // HTML 编辑器控件 -->


    	</div>
	

    	<br />

	<div style="clear:both;"></div>
	<div class="p">
		<h3>注意事项：</h3>
		<ul>
			<li>垂直 Tab 的内容区域应该设置 min-height；或者固定高度</li>
			<li>创建实例后需要调用 jump(0); 加载第一个 tab 的内容，当前还不宜省去这一步</li>
		</ul>
	</div>
	<div class="p">
		<h3>代码如下：</h3>
	</div>
 
	<pre class="prettyprint">
&lt;div class=&quot;tab2&quot;&gt;
	&lt;ul&gt;
		&lt;li class=&quot;selected&quot;&gt;公司简介&lt;/li&gt;
		&lt;li&gt;资质证照&lt;/li&gt;
		&lt;li&gt;荣誉证书&lt;/li&gt;
	&lt;/ul&gt;
	&lt;div class=&quot;content&quot;&gt;
		&lt;div&gt;
			自古以来，……
		&lt;/div&gt;
		&lt;div&gt;
			之所以如此，……
		&lt;/div&gt;
		&lt;div&gt;
			笔者出于个人兴趣，……
		&lt;/div&gt;
	&lt;/div&gt;
&lt;/div&gt;

&lt;script src=&quot;${bigfoot}/js/widget/tab.js&quot;&gt;&lt;/script&gt;
&lt;script&gt;
	new SimpleTab(document.querySelector(&#x27;.tab2&#x27;));
&lt;/script&gt;
	   </pre>
    </body>
</html>