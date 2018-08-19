<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/ajaxjs-ui/less/admin.less" title="网站配置" />
<body class="configForm admin-entry-form">
	<header class="top">
		<div>
			<a href="#" target="_blank">
				<img width="12" src="data:image/gif;base64,R0lGODlhEAAQAIABAAAAAP///yH5BAEAAAEALAAAAAAQABAAAAImjG+gq+je3gOBWURrlvVEuWlcKE4T2Xkql6zshkLuOIO1mVj6VgAAOw==" />
				新窗口打开
			</a>
		</div>
		
		<fieldset>
			<legend>
				网站配置
			</legend>
		</fieldset>
	</header>
	<form method="POST" action="action.do">
		<div class="row">
			<dl>
				<label>
					<dt>
						公司全称<span class="required-note">*</span>
					</dt>
					<dd>
						<input type="text" placeholder="公司全称"
							name="clientFullName" data-note2="请输入你公司的全称"
							value="${all_config.clientFullName}" />
					</dd>
				</label>
			</dl>
		</div>
		<div class="row">
			<dl>
				<label>
					<dt>公司简称</dt>
					<dd>
						<input type="text" placeholder="公司简称"
							name="clientShortName" data-note2="请输入你公司的简称"
							value="${all_config.clientShortName}" />
					</dd>
				</label>
			</dl>
		</div>
		<div class="row">
			<dl>
				<label>
					<dt>
						网站标题前缀<span class="required-note">*</span>
					</dt> 
					<dd>
						<input type="text" placeholder="用户名"
							name="site.titlePrefix" required data-regexp="Username"
							data-note="网站全局的标题，在 title 标签中显示"
							value="${all_config.site.titlePrefix}" />
					</dd>
				</label>
			</dl>
		</div>
		<div class="row">
			<dl>
				<label> 
					<dt>搜索关键字</dt>
					<dd>
						<textarea name="site.keywords" rows="10" cols="50"
							class="textarea" data-note2="用于 SEO 关键字优化，以便于搜索引擎机器人查找、分类。<br />多个关键字之间用英文 , 逗号分开">${all_config.site.keywords}</textarea>
						<div class="sub">用于 SEO 关键字优化，以便于搜索引擎机器人查找、分类。多个关键字之间用英文 , 逗号分开</div>
					</dd>
				</label>

			</dl>
		</div>
		<div class="row">
			<dl>
				<label>
					<dt>网站描述</dt>
					<dd>
						<textarea name="site.description" rows="10">${all_config.site.description}</textarea>
						<div class="sub">网站的说明文本。通常是一段话介绍网站即可。</div>
					</dd>
				</label>
			</dl>
		</div>

		<section style="text-align: center;padding:2% 0;">
			<button class="ajaxjs-btn">
				<img src="${commonAssetIcon}save.gif" /> 修改
			</button>
			<button class="ajaxjs-btn"
				onclick="this.up('form').reset();return false;">复 位</button>

		</section>
	</form>
	<script>
		ajaxjs.xhr.form('form');
	</script>
</body>
</html>