<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:content banner="../images/contact.jpg" bodyClass="contact">
	<jsp:attribute name="left">
		<ul>
			<li>公司电话（020）37392396</li>
			<li>邮箱地址 bp@egdtv.com</li>
			<li>公司地址 广州市天河区高普路国家软件基地</li>
		</ul>
	</jsp:attribute>
	<jsp:attribute name="body">

		<div id="map"></div>
		<script src="//api.map.baidu.com/api?ak=6dcb06a7f08c5039a08c62fe4e2617e5&type=lite&v=1.0"></script>
		<script>
			var map = new BMap.Map('map');
			// 创建地图实例
			var point = new BMap.Point(113.418564, 23.178375); // 

http://api.map.baidu.com/lbsapi/getpoint/index.html 拾取坐标系统
			// 创建点坐标
			map.centerAndZoom(point, 15);
			// 初始化地图， 设置中心点坐标和地图级别
			var zoomCtrl = new BMap.ZoomControl({anchor: 

BMAP_ANCHOR_BOTTOM_RIGHT, offset: new BMap.Size(20, 20)});
			map.addControl(zoomCtrl);
		</script>
		
		<fieldset>
			<legend>留言反馈</legend>
		</fieldset>
		<form method="POST" action="" class="feedback">
			<dl>
				<label>
					<dt>名称：<span class="required-note">*</span>
					</dt>
					<dd>
						<input type="text" name="name" placeholder="用户名" required
							data-regexp="Username" data-note="用户名等于账号名；不能与现有的账号名相同；注册后不能修改；"
							data-note2="用户名22等于账号名；不能与现有的账号名相同；注册后不能修改；" />
					</dd>
				</label>
			</dl>
			<dl>
		
			<label>
				<dt>电子邮件：</dt>
				<dd>				
					<input type="text" name="email" placeholder="请输入电子邮件"
							data-regexp="email" data-note="邮件可用于登录和查找密码等；不能与现有的邮件相同；注册后可以修改；"
							data-note2="推荐使用国内邮箱，如 QQ、163 等的邮箱" />
				</dd>
			</label>
			</dl>
			<dl>
			<label>
				<dt>手机：</dt>
				<dd>				
					<input type="text" name="phone" placeholder="请输入手机"
							data-regexp="email" data-note="邮件可用于登录和查找密码等；不能与现有的邮件相同；注册后可以修改；"
							data-note2="推荐使用国内邮箱，如 QQ、163 等的邮箱" />
				</dd>
			</label>
			</dl>
			<dl>
				<label>
					<dt>留言：<span class="required-note">*</span></dt>
					<dd>	
						<textarea name="content" rows="5" cols="20" placeholder="请输入留言"></textarea>
					</dd>
				</label>
			</dl>
			<dl>
				<label>
					<dt>验证码：<span class="required-note">*</span></dt>
					<dd>	
						<commonTag:form type="captcha" />
					</dd>
				</label>
			</dl>
			<dl>
				<label>
					<dt></dt>
					<dd>	
						<button>提交</button>
					</dd>
				</label>
			</dl>
		</form>
		
		
		<div style="padding: 20px 0;" align="right">
			<!-- 分享功能 -->
			<commonTag:article type="share" />
			<!-- 调整页面字号 -->
			<commonTag:article type="adjustFontSize" />
			<!-- 页面功能 -->
			<commonTag:article type="function" />
		</div>
		
		
		
		
	</jsp:attribute>
</tags:content>

