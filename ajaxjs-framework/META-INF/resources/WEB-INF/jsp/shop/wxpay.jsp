<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<tags:content bodyClass="shop-checkout" bannerImg="${ctx}/images/memberBanner.jpg">
	<h2 class="aj-center-title" style="padding:0;">微信支付金额：￥${totalPrice}</h2>
	
	<section class="payment" style="margin:5% auto;text-align: center;">
		<img src="https://my.tv.sohu.com/user/a/wvideo/getQRCode.do?text=${codeUrl}" /> 
	</section>
	 
	<div style="clear:both;"></div>
</tags:content>

