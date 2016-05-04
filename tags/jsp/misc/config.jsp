<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/public"%>
<%@taglib prefix="commonUI"  tagdir="/WEB-INF/tags/public/UI"%>
<%-- <jsp:useBean id="bean" class="com.ajaxjs.framework.javascript.JSON_Saver" /> --%>
<%
// 	if (request.getParameter("getConfig") != null) {
// 		String filePath = new RequestHelper(request).Mappath("/META-INF/site_config.js");
// 		out.println(Base.text.readFile(filePath));
// 		return;
// 	} else if (request.getMethod().equals("POST")) {
// 		//bean.doAction(request, response);
// 		return;
// 	}
%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/bigfoot/asset/less/user.less" title="网站配置" />
<body>
	<commonUI:adminHeader pageTitle="网站配置" />
	<style>
		body{
			background-color: #f4f4f4;
		}
	</style>
	<div class="panel">
		<h4>请输入网址配置</h4>
		<form:form  action="?" method="POST" class="aj">
			<input type="hidden" name="jsonFile" value="/WEB-INF/src/site_config.js" />
			<input type="hidden" name="topVarName" value="bf_Config" />

			<label>
				<div class="heightLabel">&nbsp;&nbsp;<span class="required-note">*</span>公司全称</div>
				<input type="text" placeholder="公司全称" name="bf_Config.clientFullName"
					value="${global_config.clientFullName}" 
				 />
			</label>
			<label>
				<div class="heightLabel">&nbsp;&nbsp;公司简称</div>
				<input type="text" placeholder="公司简称" name="bf_Config.clientShortName"
				value="${global_config.clientShortName}" />
			</label>
						
			<label>
				<span class="required-note">*</span>网站标题前缀：
				<input type="text" placeholder="用户名" 
					name="bf_Config.site.titlePrefix" 
					required
					data-regexp="Username"
					data-note="网站全局的标题，在 title 标签中显示"
					value="${global_config.site_titlePrefix}"
				 />
			</label>
			<label>
				<div class="heightLabel">
					<div>
						搜索关键字：
					</div>
					<div class="sub">
						用于 SEO 关键字优化，以便于搜索引擎机器人查找、分类。多个关键字之间用英文 , 逗号分开
					</div>
				</div>
				<textarea name="bf_Config.site.keywords" rows="10">${global_config.site_keywords}</textarea>
			</label>
			<label>
				<div class="heightLabel">
					<div>网站描述：</div>
					<div class="sub">网站的说明文本。通常是一段话介绍网站即可。</div>
				</div>
				<textarea name="bf_Config.site.description" rows="10">${global_config.site_description}</textarea>
			</label>
 
			<div class="labelRow">
				<div class="heightLabel">&nbsp;&nbsp;是否启动网站？</div>
				<div class="input"> 
				    <label>
				        <input type="checkbox" class="siteStatus_pc" checked value="${command.siteStatus_pc}" /> PC 版
				    </label>
				    <label>
				        <input type="checkbox" class="siteStatus_wap" checked /> 手机版
				    </label>
				    <label>
				        <input type="checkbox" class="siteStatus_weixin" checked value="on" /> 微信版
				    </label>
				    <span style="color:gray;font-size:.8rem;">请慎重选择是否关闭网站！</span>
				</div>
			</div>
	 
			<div> 
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="保存" class="my-btn-3" /> &nbsp;&nbsp;&nbsp;&nbsp;
			</div>
	</form:form>
	</div>
 
	<script>
		new bf_form(document.querySelector('form'), {
			isCommonAfterSubmit : true
		});
	</script>
</body>
</html>