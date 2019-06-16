<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tagfiles" tagdir="/WEB-INF/tags/"%>
<tagfiles:common type="content" banner="../../images/contact.jpg" showPageHelper="true">
	<jsp:attribute name="body">
	<style>
		.gallery {
			width: 70%;
			margin-top: -20px;
			height: 530px;
		}
		
		.gallery ul {
			margin: 0 0;
		}
		
		.gallery ul li {
			float: left;
			width: 33.3%;
			list-style: none;
			margin: 30px 0;
			max-height: 155px;
		}
		
		.gallery ul li .imgContainer {
			margin: 40px 20px 0 20px;
			border: 4px white solid;
			box-shadow: 2px 2px 5px #888888;
			position: relative;
			cursor: pointer;
		}
		
		.gallery ul li .imgContainer img {
			width: 100%;
		}
		
		.gallery ul li .imgContainer img.jp {
			position: absolute;
			top: -25px;
			left: 40%;
			width: 40px;
		}
		
		.r10 {
			transform: rotate(5deg);
			-ms-transform: rotate(5deg);
			/* IE 9 */
			-webkit-transform: rotate(5deg);
			/* Safari and Chrome */
		}
		
		.r_10 {
			transform: rotate(-3deg);
			-ms-transform: rotate(-3deg);
			/* IE 9 */
			-webkit-transform: rotate(-3deg);
			/* Safari and Chrome */
		}
		
		.r5 {
			transform: rotate(5deg);
			-ms-transform: rotate(5deg);
			/* IE 9 */
			-webkit-transform: rotate(5deg);
			/* Safari and Chrome */
		}
	</style>
	<div class="text gallery">	
		<ul>
	        <li>
	            <div class="imgContainer r10">
	                <img src="7.jpg" height="180">
	                <img src="${commonAsset}/images/gallery_jp.png"
	class="jp">
	             </div>
	         </li>
	         <li>
	             <div class="imgContainer r_10">
	                 <img src="8.jpg" height="180">
	                 <img src="${commonAsset}/images/gallery_jp.png"
	class="jp">
	             </div>
	         </li>
	         <li>
	             <div class="imgContainer r5">
	                 <img src="9.jpg">
	                 <img src="${commonAsset}/images/gallery_jp.png"
	class="jp">
	             </div>
	         </li>
	         <!-- br -->
	        <li>
	            <div class="imgContainer r_10">
	                <img src="10.jpg" height="180">
	                <img src="${commonAsset}/images/gallery_jp.png"
	class="jp">
	             </div>
	         </li>
	         <li>
	             <div class="imgContainer">
	                 <img src="11.jpg" height="180">
	                 <img src="${commonAsset}/images/gallery_jp.png"
	class="jp">
	             </div>
	         </li>
	         <li>
	             <div class="imgContainer r10">
	                 <img src="12.jpg" height="180">
	                 <img src="${commonAsset}/images/gallery_jp.png"
	class="jp">
	             </div>
	         </li>       
		</ul>
	</div>  
	</jsp:attribute>
</tagfiles:common>