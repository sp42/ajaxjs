<%@page pageEncoding="UTF-8"%>


	<h4>图片验证码</h4>
	<p>点击刷新图片，并发送时间戳参数请求图片（不缓存图片）。</p>

	<p>属性说明：</p>
	<table class="ajaxjs-borderTable">
		<thead>
			<tr>
				<th>属性</th>
				<th>含义</th>
				<th>类型</th>
				<th>是否必填，默认值</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>imgSrc</td>
				<td>生成图片验证码地址</td>
				<td>String</td>
				<td>y</td>
			</tr>
			<tr>
				<td>fieldName</td>
				<td>提交的字段名</td>
				<td>String</td>
				<td>n，captcha</td>
			</tr>
		</tbody>
	</table>
	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-page-captcha img-src="${pageContext.request.contextPath}/Captcha/"></aj-page-captcha></xmp></pre>
	
	<p>演示：</p>
	
	<div class="captcha center">
		<aj-page-captcha img-src="${pageContext.request.contextPath}/Captcha/"></aj-page-captcha>
	</div>
	<script>
		new Vue({el:'.captcha'});
	</script>
	
	
	
	<h4>HTML 在綫編輯器</h4>
	<p>小巧的HTML 在綫編輯器。</p>
	<ul class="center list">
		<li>设置内容的三种方法：1、定义属性 content；2、调用组件实例方法 setContent(content);3、通过 slot 标签中指定 content。</li>
		<li>注意：当使用插槽方式时候，必须提供一个 <slot> 包含有 &lt;textarea class="hide" name="content"&gt;${info.content} &lt;/textarea&gt;</li>
		<li>获取内容方法：调用实例方法 getContent() 返回内容的 HTML；getContent({encode:true}) 返回汉字在表单中的 encodeURIComponent 编码；getContent({cleanWord: true}) 清理 word 粘贴冗余的标签。</li>
		<li>图片上传依赖组件 App.$refs.uploadLayer，待优化 TODO</li>
	</ul>
	
	<p>属性说明：</p>
	<table class="ajaxjs-borderTable">
		<thead>
			<tr>
				<th>属性</th>
				<th>含义</th>
				<th>类型</th>
				<th>是否必填，默认值</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>fieldName</td>
				<td>表单 name，字段名</td>
				<td>String</td>
				<td>y</td>
			</tr>
			<tr>
				<td>content</td>
				<td>HTML内容</td>
				<td>String</td>
				<td>n</td>
			</tr>
			<tr>
				<td>basePath</td>
				<td>iframe 的 <code>&lt;base href="${param.basePath}/"
						/&gt;</code>路徑
				</td>
				<td>String</td>
				<td>n,默认空字符串</td>
			</tr>
		</tbody>
	</table>

	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-form-html-editor field-name="content">
	<textarea class="hide" name="content">Hello World!</textarea>
</aj-form-html-editor></xmp></pre>
	
	<p>演示：</p>
	
	<div class="htmleditor center">
		 <aj-form-html-editor field-name="content">
		 	<textarea class="hide" name="content">Hello World!</textarea>
		 </aj-form-html-editor>
	</div>
	<script>
		new Vue({el:'.htmleditor'});
	</script>
	
	<h4>日期选择器</h4>
	<p>该控件本身不应单独使用，应作为子组件嵌入到父组件。当用户选择日期后，会触发 pick-date 事件，并传入 date 日期的参数，形如 2018-8-8。</p>
	<p>TODO 年份不能选择！</p>

	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-form-calendar></aj-form-calendar></xmp></pre>
	
	<p>演示：</p>
	
	<div class="datepicker center">
		 <aj-form-calendar></aj-form-calendar>
	</div>
	<script>
		new Vue({el:'.datepicker'});
	</script>
	
	<h4>日期选择器（带输入框的）</h4>
	<p>输入框本身不美化，应伴随表单样式一起定义。</p>

	<p>属性说明：</p>
	<table class="ajaxjs-borderTable">
		<thead>
			<tr>
				<th>属性</th>
				<th>含义</th>
				<th>类型</th>
				<th>是否必填，默认值</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>fieldName</td>
				<td>表单 name，字段名</td>
				<td>String</td>
				<td>y</td>
			</tr>
			<tr>
				<td>fieldValue</td>
				<td>表单值，可选的</td>
				<td>String</td>
				<td>n</td>
			</tr>
			<tr>
				<td>positionFixed</td>
				<td>表是否采用固定定位，<br>有时父元素限制了溢出，所以采用固定布局则无此问题。</td>
				<td>Boolean</td>
				<td>n</td>
			</tr>
		</tbody>
	</table>

	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-form-calendar-input field-name="date" field-value="2018-8-2"></aj-form-calendar-input></xmp></pre>
	
	<p>演示：</p>
	
	<div class="datepicker2 center">
		 <aj-form-calendar-input field-name="date" field-value="2018-8-2"></aj-form-calendar-input>
	</div>
	<script>
		new Vue({el:'.datepicker2'});
	</script>

	
	<h4>图片文件上传</h4>
	<p>图片选择器、预览和相关校验。</p>

	<p>属性说明：</p>
	<table class="ajaxjs-borderTable">
		<thead>
			<tr>
				<th>属性</th>
				<th>含义</th>
				<th>类型</th>
				<th>是否必填，默认值</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>imgPlace</td>
				<td>图片占位符，用户没有选定图片时候使用的图片</td>
				<td>String</td>
				<td>y</td>
			</tr>
			<tr>
				<td>imgName</td>
				<td>表单提交时字段的名称</td>
				<td>String</td>
				<td>y</td>
			</tr>
			<tr>
				<td>imgId</td>
				<td>图片在数据库对应的 id</td>
				<td>Number</td>
				<td>n</td>
			</tr>
		</tbody>
	</table>
	
	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-form-calendar-input field-name="date" field-value="2018-8-2"></aj-form-calendar-input></xmp></pre>
	
	<p>演示：</p>
	
	<div class="upload center">
		 <ajaxjs-img-upload-perview></ajaxjs-img-upload-perview>
	</div>
	<script>
		new Vue({el:'.upload'});
	</script>