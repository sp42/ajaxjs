<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tagfiles" tagdir="/WEB-INF/tags/"%>
<tagfiles:common type="content" banner="../../images/contact.jpg" showPageHelper="true" bodyClass="contact">
	<jsp:attribute name="body">
		<div id="map"></div>
		<script src="//api.map.baidu.com/api?ak=6dcb06a7f08c5039a08c62fe4e2617e5&type=lite&v=1.0"></script>
		<script>
			var map = new BMap.Map('map');
			// 创建地图实例
			var point = new BMap.Point(113.418564, 23.178375); // 

			http: //api.map.baidu.com/lbsapi/getpoint/index.html 拾取坐标系统
			// 创建点坐标
			map.centerAndZoom(point, 15);
			// 初始化地图， 设置中心点坐标和地图级别
			var zoomCtrl = new BMap.ZoomControl({
				anchor :

				BMAP_ANCHOR_BOTTOM_RIGHT,
				offset : new BMap.Size(20, 20)
			});
			map.addControl(zoomCtrl);
		</script>
		
		<ul>
			<li>公司电话（020）37392396</li>
			<li>邮箱地址 bp@egdtv.com</li>
			<li>公司地址 广州市天河区高普路国家软件基地</li>
		</ul>
	</jsp:attribute>
</tagfiles:common>

