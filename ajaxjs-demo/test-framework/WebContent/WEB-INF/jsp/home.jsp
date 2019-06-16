<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:content banner="../images/banner.jpg" bodyClass="home">
	<jsp:attribute name="left">
		<h2>最新新闻</h2>
		<commonTag:list type="list-simple" urlPerfix="../news/" list="${news}" />
		<br />
		<br />
		<img src="../images/home2.png" width="330" />
		<br />
	</jsp:attribute>
	<jsp:attribute name="body">
		<fieldset>
			<legend>
			关于 NIKE
			</legend>
			NIKE公司总部位于美国俄勒冈州波特兰市。公司生产的体育用品包罗万象，例如服装，鞋类，运动器材等。NIKE是全球著名的体育运动品牌，英文原意指希腊胜利女神，中文译为耐克。 耐克商标图案是个小钩子。耐克一直将激励全世界的每一位运动员并为其献上最好的产品视为光荣的任务。耐克首创的气垫技术给体育界带来了一场革命。
		</fieldset>
		<img src="../images/home1.png" width="830" />
		<h2>最新产品</h2>
	</jsp:attribute>
</tags:content>

