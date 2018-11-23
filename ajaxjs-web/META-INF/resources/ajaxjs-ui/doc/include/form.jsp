<%@page pageEncoding="UTF-8"%>
<h4>按钮 Button</h4>
	<pre class="prettyprint"><xmp><button class="ajaxjs-btn">按钮</button>
<button class="ajaxjs-btn-1">按钮</button>
<button class="ajaxjs-btn-1 ajaxjs-disable">按钮（禁止状态）</button></xmp></pre>

<p>演示：</p>
	<div class="center">
		<button class="ajaxjs-btn">按钮</button>
		<button class="ajaxjs-btn-1">按钮</button>
		<br />
		<button class="ajaxjs-btn-1 ajaxjs-disable">按钮（禁止状态）</button>
	</div>

	<h4>输入框 Input Field</h4>
	<pre class="prettyprint"><xmp><input class="ajaxjs-inputField" type="text" value="文本输入框" /> 
<input class="ajaxjs-inputField ajaxjs-disable" type="text" value="文本输入框（禁止状态）" /></xmp></pre>

	
	<p>演示：</p>
	<div class="center">
		<input class="ajaxjs-inputField" type="text" value="文本输入框" /> 
		<br />
		<br /> 
		<input class="ajaxjs-inputField ajaxjs-disable" type="text" value="文本输入框（禁止状态）" />
	</div>

	<h4>文本框 Textarea</h4>
	<pre class="prettyprint"><xmp><textarea class="ajaxjs-inputField" rows="15" cols="20">……</textarea></xmp></pre>

	<p>演示：</p>
	<div class="center">
		<textarea class="ajaxjs-inputField" rows="15" cols="20"
			style="resize: none; padding-top: 5px; height: 100px; width: 400px;-webkit-overflow-scrolling : touch"
			name="intro">“北上广深同比涨幅高达20%，26个主要城市涨幅超10%，69个城市房价上涨……”2013年全国房价涨声一片，这与年初各地制定的房价控制目标相差甚远。 经济学者马光远[微博]表示，房价调控目标是政府对于百姓的“承诺”，没有完成目标而“爽约”，蒙混过关只会丧失公信力。这与年初各地制定的房价控制目标相差甚远。 经济学者马光远[微博]表示，房价调控目标是政府对于百姓的“承诺”，没有完成目标而“爽约”，蒙混过关只会丧失公信力。</textarea>
	</div>

	<h4>下拉列表 DropdownList</h4>
	<pre class="prettyprint"><xmp><select name="catalog" class="ajaxjs-select" style="width:250px;">……</select></xmp></pre>
	
	<p>演示：</p>
	<div class="center">
		<select name="catalog" class="ajaxjs-select" style="width:250px;">
			<option value="1">公司新闻</option>
			<option value="2" selected="selected">行业动态</option>
			<option value="3">媒体报道</option>
		</select>
		<br/>
		<br/>
		<select class="ajaxjs-select center" multiple style="width:250px;height:100px;">
			<option>多项选择</option>
			<option>1</option>
			<option>2</option>
		</select>
	</div>
	
	<h4>自定义文件上传按钮</h4>
	<p>通过 label 标签 for 属性关联具体的 input[type=file] 触发本地 file picker。input[type=file] 本身隐藏。</p>
	
	<p>演示：</p>
	<div class="center" style="width: 100px;">
		<input id="input_file_molding" type="file" class="hide" name="uploadfile" /> 
		<label for="input_file_molding">
			<div
				style="border: 1px solid lightgray; border-radius: 3px; text-align: center; color: gray; cursor: pointer; font-size: .8rem; padding: 10px;">
				<div style="font-size: 2rem;">✚</div>
				点击选择图片
			</div>
		</label>
	</div>
	
<h4>Form Validator</h4>

<form action="?" method="POST" class="form-1 center">
	<dl>
		<label>
			<dt>
				用户名：<span class="required-note">*</span>
			</dt>
			<dd>
				<input type="text" name="username" placeholder="用户名" required
					data-regexp="Username" data-note="用户名等于账号名；不能与现有的账号名相同；注册后不能修改；"
					data-note2="用户名22等于账号名；不能与现有的账号名相同；注册后不能修改；" />
			</dd>
		</label>


		<label>
			<dt>
				电子邮件：<span class="required-note">*</span>
			</dt>
			<dd>
				<input type="text" name="username" placeholder="请输入电子邮件"
					data-regexp="email" data-note="邮件可用于登录和查找密码等；不能与现有的邮件相同；注册后可以修改；"
					data-note2="推荐使用国内邮箱，如 QQ、163 等的邮箱" />
			</dd>
		</label>

		<label>
			<dt>
				用户头像：<span class="required-note">*</span>
			</dt>
			<dd>
				<img id="preview" /> <input type="file" name="avatar"
					onchange="onAvatartUploadChange(this);" placeholder="请输入电子邮件"
					data-regexp="email" data-note="邮件可用于登录和查找密码等；不能与现有的邮件相同；注册后可以修改；"
					data-note2="推荐使用国内邮箱，如 QQ、163 等的邮箱" />
			</dd>
		</label>



		<label>
			<dt
				style="overflow: hidden; vertical-align: middle; display: table-cell; height: 100px; line-height: 100px;">个人简介
				:</dt>
			<dd>
				<textarea style="width: 100%; height: 100px; resize: none;"></textarea>
			</dd>
		</label>


		<dt>性别：</dt>
		<dd>
			<label><input type="radio" value="" name="sex"
				checked="checked" /> 男</label> <label><input type="radio" value=""
				name="sex" /> 女</label>
		</dd>

		<dt>爱好：</dt>
		<dd>
			<label><input type="checkbox" /> 篮球</label> <label><input
				type="checkbox" /> 足球</label> <label><input type="checkbox" /> 游泳</label>
		</dd>

		<dt>城市</dt>
		<dd>
			<select>
				<option>北京</option>
				<option>上海</option>
				<option>南京</option>
			</select>
		</dd>

		<label>
			<dt>
				<span class="required-note">*</span>登录密码 :
			</dt>
			<dd>
				<input type="password" name="password" />
			</dd>
		</label>


		<label>
			<dt>
				<span class="required-note">*</span>确认密码 :
			</dt>
			<dd>
				<input type="password" name="password" />
			</dd>
		</label>

		<!-- 验证码 -->
		<label class="captchaCode">
			<dt>
				<span class="required-note">*</span>验证码：
			</dt>
			<dd>
				<input type="text" data-regexp="integer" required />
			</dd>
		</label>
		<label>
			<dt>&nbsp;</dt>
			<dd>
				<input name="agree" type="checkbox" /> 我已仔细阅读并接受<a
					href="misc/registration-policy.jsp" target="_blank">注册条款</a>
			</dd>
		</label>
		<dt>&nbsp;</dt>
		<dd>
			<input type="submit" value="注 册" /> &nbsp;&nbsp;&nbsp;&nbsp;<a
				href="?">现有账号登录</a>
		</dd>
	</dl>
</form>