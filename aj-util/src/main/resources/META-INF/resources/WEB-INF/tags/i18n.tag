<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@attribute name="eng" 	required="true" 	type="String" 	description="英文输出"%>
<%@attribute name="engHTML" required="false" 	fragment="true" description="英文 HTML输出"%>
<%@attribute name="zh"  	required="true" 	type="String" 	description="简体中文输出"%>
<%@attribute name="zhHTML" required="false" 	fragment="true" description="简体中文 HTML输出"%>
<%
	String targetLanguage = request.getParameter("i18n");

	if (targetLanguage != null) 
		session.setAttribute("i8n", targetLanguage);

	Object si18n = session.getAttribute("i8n");

	if (si18n != null) 
		targetLanguage = si18n.toString();

	if (targetLanguage == null) {
		java.util.Locale l = request.getLocale();

		if (l == null) 
			targetLanguage = "en";
		 else {
			String _targetLanguage = l.toString();

			if (_targetLanguage.indexOf("en") != -1) 
				targetLanguage = "en";
			 else if (_targetLanguage.indexOf("zh") != -1) 
				targetLanguage = "zh";
			
			session.setAttribute("i8n", targetLanguage);
		}
	}
	
	if ("en".equals(targetLanguage)) {
		%>${eng}<jsp:invoke fragment="engHTML"/><%
	} else if ("zh".equals(targetLanguage)) {
		%>${zh}<jsp:invoke fragment="zhHTML"/><%
	}
%>