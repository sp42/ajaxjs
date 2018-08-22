<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<!DOCTYPE html>
<html>
<commonTag:head title="AJAXJS UI 测试" />
<body style="padding:3%;">
	<span class="aj-page-captcha">
		<aj-page-captcha img-src="${pageContext.request.contextPath}/Captcha/"></aj-page-captcha>
	</span>
	
	<br />
	<br />
	<br />
	<br />	

	<span class="aj-page-share">
		<aj-page-share></aj-page-share>
	</span>
	
	<br />
	<br />
	<br />
	<br />
	
	
	<span class="aj-adjust-font-size">
		<aj-adjust-font-size></aj-adjust-font-size>
	</span>
	
	<br />
	<br />
	<br />
	<br />
	
	<div class="aj-baidu-search">
		<aj-baidu-search site-domain-name="${aj_allConfig.site.domainName}"></aj-baidu-search>
	</div>
	
	<br />
	<br />
	<br />
	<br />
	
	<div class="aj-chinese-switch">
		<aj-chinese-switch jsurl="${commonAsset}js/libs/chinese.js"></aj-chinese-switch>
	</div>
	
	<br />
	<br />
	<br />
	<br />

	<div class="aj-expander">
		<aj-expander :close-height="50">
1、什么是WebpackWebPack可以看做是模块打包机：它做的事情是，分析你的项目结构，找到JavaScript模块以及其它的一些浏览器不能直接运行的拓展语言（Scss，TypeScript等），并将其打包为合适的格式以供浏览器使用。
2、为什要使用WebPack今的很多网页其实可以看做是功能丰富的应用，它们拥有着复杂的JavaScript代码和一大堆依赖包。为了简化开发的复杂度，前端社区涌现出了很多好的实践方法
a:模块化，让我们可以把复杂的程序细化为小的文件;
b:类似于TypeScript这种在JavaScript基础上拓展的开发语言：使我们能够实现目前版本的JavaScript不能直接使用的特性，并且之后还能能装换为JavaScript文件使浏览器可以识别；
c:scss，less等CSS预处理器
		</aj-expander>
	</div>
	
	
	<br />
	<br />
	<br />
	<br />
	<div class="aj-article-body">
		<aj-article-body title="${info.name}" content="什么是WebpackWebPack可以看做是模块打包机……"></aj-article-body>
	</div>
	
	<br />
	<br />
	<br />
	<br />

	<!-- 日历控件 -->
	<div class="aj-form-calendar-input">
		<aj-form-calendar-input field-name="date" field-value="2018-8-2"></aj-form-calendar-input>
	</div>
	<br />
	<br />
	<br />
	<br />

	
	<div class="aj-form-calendar calendar1">
		<aj-form-calendar></aj-form-calendar>
	</div>
	
	<br />
	<br />
	<br />
	<br />
	
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	
		<br />
	<br />
	<br />
	<br />
	
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />	<br />
	<br />
	<br />
	<br />
	
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	
	<!-- // 日历控件 -->
	
	<div class="aj-form-html-editor">
		<aj-form-html-editor field-name="content"></aj-form-html-editor>
	</div>
	<br />
	<br />
	<br />
	<br />

	<script>		
		new Vue({ el : '.aj-page-captcha' });
		new Vue({ el : '.aj-page-share' });
		
		
		new Vue({ el : '.aj-adjust-font-size' });
		new Vue({ el : '.aj-baidu-search' });
		new Vue({ el : '.aj-chinese-switch' });
		new Vue({ el : '.aj-expander' });
		
		new Vue({ el : '.aj-article-body' });
		new Vue({ el : '.aj-form-calendar' });
		new Vue({ el : '.aj-form-calendar-input' });
		new Vue({ el : '.aj-form-html-editor' });
		//new Vue({ el : '.fileupload-iframe' });
		//UploadController = new Vue({ el : '.upload' });
	</script>
	</body>
</html>