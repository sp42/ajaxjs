<%@tag pageEncoding="UTF-8" description="滚动图" import="java.util.Map"%>
<%@attribute name="banners" required="true" type="com.ajaxjs.framework.mvc.model.Banner" description="banner 列表"%> 
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

			<div class="">
				<a href="#">
					<img src="" />
				</a>
			</div>
	
		</div>
	    <ol class="indicator"> 

			<li class=" active">1</li>
	
	    </ol>
	</section>
	<script>
		var banner = Object.create(bf_banner);
		banner.el = document.querySelector('.banner');
		banner.init();
		banner.loop();
		banner.initIndicator();
	</script>
	<!-- // 广告 -->