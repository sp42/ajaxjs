<%@tag pageEncoding="UTF-8" description="滚动图" import="java.util.Map"%>
<%@attribute name="banners" required="true" type="com.ajaxjs.widget.Banner" description="banner 列表"%> 
	<script>
		// 广告图片宽度
		var bannerWidth = window.parseInt(window.innerWidth * window.devicePixelRatio),
		// 台标图片宽度
			tvIconWidth = window.parseInt(bannerWidth/5);
		
		if(bannerWidth > 640)bannerWidth = 640; // 给一个最大值，宽度不能无限扩大
	</script>
	
	<!-- 广告 -->
	<section class="banner">
		<div>
		<% 
			int i = 0;
			for(Map<String, Object> banner : banners.getBanner()){ 
		%>
			<div class="contentType_<%=banner.get("contentType")%>">
				<a href="<%=banners.getLink(banner, false)%>">
					<img src="<%=banner.get("horizontalPic")%>" />
				</a>
			</div>
		<%
				i++;
			}
		%>
		</div>
	    <ol class="indicator"> 
	    	<%for(int j = 0; j < i;++j){%>
			<li class="<%=j == 0 ? " active" : "" %>"><%=j%></li>
			<%}%>
	    </ol>
	</section>
	<script src="${bigfoot}/js/widget/banner.js"></script>
	<script>
		var banner = Object.create(bf_banner);
		banner.el = document.querySelector('.banner');
		banner.init();
		banner.loop();
		banner.initIndicator();
	</script>
	<!-- // 广告 -->