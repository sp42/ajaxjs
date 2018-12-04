<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:content banner="../../images/contact.jpg" bodyClass="">
	<jsp:attribute name="left">
		<commonTag:page type="secondLevelMenu" />
		<div style="text-align: right; margin: 5%;">
			<a href="../">回到上一层</a>
		</div>
	</jsp:attribute>
	<jsp:attribute name="body">
		<article>
		<h3>${PAGE.node.name}</h3>
		<!-- Editable AREA|START -->
		<p><img src="../../images/about1.png" style="float:right;margin-left:3%;" />
1908年，马萨诸塞州的利恩建了一个制鞋厂，使当地的制鞋技术得到了进一步发展。在那里工人不再是独立的做nike鞋，鞋的每个制作环节都由一个受过训练的专人负责。生产线开始形成。起初的nike板鞋仍然是订做的，但为了使工人在淡季有事可干，鞋坊老板开始做没有预订的鞋。这些鞋被称作待售鞋，摆在当地商店的橱窗里。
1958年，当时的创办人菲尔．奈特俄勒岗(Oregon)州立大学田径队选手，毕业后返乡任教于波特兰。</p>
			<p>
1963年，在俄勒冈大学毕业的菲尔·奈特和他的导师比尔·鲍尔曼共同创立了一家名为"蓝带体育用品公司"(Blue Ribbon Sports)的公司，主营体育用品。1964年，耐特与他的教练鲍尔曼各出资500美元，成立了运动鞋公司，取名为蓝带体育用品公司。</p>
			<p>
1972年，NIKE公司正式成立。其前身是由现任NIKE总裁菲尔·耐特以及比尔·鲍尔曼教练投资的蓝带体育公司。</p>
			<p>
2001年，耐克公司在研制出气垫技术后又推出了一种名为Shox的新型缓震技术。采用这种技术生产出来的运动鞋同样深受欢迎，销量节节攀升。除运动鞋以外，耐克公司的服装也不乏创新之作。例如：运用FIT技术制造的高性能纺织品能够有效地帮助运动员在任何气象条件下进行训练和比赛。耐克公司制造的其他体育用品，如：手表，眼镜等等都是高科技的结晶。
</p>
<!-- Editable AREA|END -->
		</article>
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

