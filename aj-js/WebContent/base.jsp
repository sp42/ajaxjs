<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
    <html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AJAXJS-Base</title>
        <link rel="icon" type="image/x-icon" href="https://framework.ajaxjs.com/framework/asset/images/favicon.ico" />
        <link rel="shortcut icon" type="image/x-icon" href="https://framework.ajaxjs.com/framework/asset/images/favicon.ico" />
        <meta name="keywords" content="ajaxjs base java ioc aop orm " />
        <meta name="description" content="AJAXJS-Base 基础模块包，为纯 Java 项目与特定 jvm 环境脱离，面向 Web 开发，但可运行在 web、swing、android 环境中。" />
        <meta name="viewport" content="width=320, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />

        <style>
/* AJAXJS Base CSS */
body, dl, dt, dd, ul, li, pre, form, fieldset, input, p, blockquote, th,
	td, h1, h2, h3, h4, h5 {
	margin: 0;
	padding: 0;
}

h1, h2, h3, h4, h5 {
	font-weight: normal;
}

img {
	border: 0;
}

ul li {
	list-style-type: none
}

.hide {
	display: none
}

body {
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
	font-family: "Lantinghei SC", "Open Sans", Arial, "Hiragino Sans GB",
		"Microsoft YaHei", "微软雅黑", "STHeiti", "WenQuanYi Micro Hei", SimSun,
		sans-serif;
}

a {
	color: #999;
	transition: color 400ms ease-in-out;
}

a:hover {
	color: lightgray;
	text-decoration: underline;
}

.top {
	width: 100%;
	background-color: white;
	height: 300px;
	border-bottom: 1px solid lightgray;
}

.bottom {
	width: 1000px;
	height: 400px;
	border: 1px solid lightgray;
	border-top: 0;
}

.box2 {
	width: 1000px;
	height: 500px;
	border: 1px solid lightgray;
	border-radius: 4px;
	background-color: #f7f7f9;
}

fieldset {
	width: 1000px;
	position: relative;
	height: 550px;
	border: 1px solid rgba(255, 255, 255, .5);
	border-radius: 5px;
	text-align: left;
}

legend {
	font-size: 2rem;
	color: white;
	margin-left: 2%;
}

fieldset div.box {
	position: absolute;
	right: -10%;
	/* 	background-color: rgba(255, 255, 255, .1); */
	background-image: linear-gradient(90deg, rgba(255, 255, 255, .1) 0,
		rgba(89, 195, 232, .9) 100%);
	height: 460px;
	width: 95%;
	color: white;
	padding: 3% 10% 4% 4%;
	box-sizing: border-box;
}

body {
	background-image: linear-gradient(45deg, #1832a2 0, #4ae9f2 100%);
}

legend span {
	font-size: 1.5rem;
}

h1 {
	margin-top: 0;
}

.links {
	position: absolute;
	bottom: -8%;
	left: 20%;
	color: white;
	opacity: .5;
	width: 60%;
	letter-spacing: 2px;
}

a {
	color: white;
}

ul.section {
	position: absolute;
	right: -9%;
	top: 65%;
	text-align: left;
	list-style-type: none;
	letter-spacing: 2px;
}

p {
	letter-spacing: 1px;
	clear: left;
	margin: 2% 0;
	font-size:.9rem;
}

.box ul {
	margin-bottom: 20px;
	overflow: hidden;
}

.box li {
	padding-left: 0;
	margin-left: 30px;
	list-style-type: disc;
}

@media screen and (max-width:480px) {
	fieldset {
		border: 0;
		width: 100%;
		height: auto;
	}
	legend {
		display: none;
	}
	fieldset div.box {
		height: auto;
		position: static;
		margin: 0 auto;
		padding: 3%;
	}
	p {
		text-align: justify;
		line-height: 130%;
	}
	ul.section {
		display: none;
	}
	.links {
		position: static;
		margin: 5% auto;
		margin-bottom: 5%;
		text-align: center;
	}
}
</style>
    </head>

    <body>
        <%
		String show = request.getParameter("show");
		if (show == null)
			show = "";
	%>
            <table width="100%" height="100%">
                <tr>
                    <Td align="center" valign="middle">
                        <fieldset>
                            <legend>
                                AJAXJS-Base, <span>a pure Java library</span>
                            </legend>
                            <br />
                            <div class="box">
                              <h1>
                                  <a id="ajaxjs-framework-base-基础库" class="anchor" href="#ajaxjs-framework-base-%E5%9F%BA%E7%A1%80%E5%BA%93"></a>AJAXJS Framework Base 基础库
                              </h1>
                              <p>这也许是一个平淡无奇的库，相信不会为绝大多数大神们带来一点惊喜——只是自己业余累积的一些代码库（“轮子”），因此只有我一人在完善。我 希望通过简单的手段做一件事（没啥三方依赖），不过可能考虑的情况不是很足，方法不是最主流的，性能不是最优的。</p>
                              <p>包含有：ioc/aop/orm/json/logger，纯 Java 项目，Maven：</p>
<pre>&lt;dependency&gt;
  &lt;groupId&gt;com.ajaxjs&lt;/groupId&gt;
  &lt;artifactId&gt;ajaxjs-base&lt;/artifactId&gt;
  &lt;version&gt;1.1.6&lt;/version&gt;
&lt;/dependency&gt;</pre>
                                  <p>运行要求：Java 1.8+。关联项目： <a href="index.jsp">AJAXJS-Web</a></p>
                                   <p>基于 Apache License Ver2.0 开源协议，免费使用、修改，引用请保留头注释。</p>
                            </div>

                            <div class="links">©1999-2018 Sp42， 粤ICP备18053388号</div>

                            <ul class="section">
                                <li><a href="https://gitee.com/sp42_admin/ajaxjs-base">Git/SVN 源码</a></li>
                                <li><a href="ajaxjs-base-doc/">JavaDoc</a></li>
                                <li><a href="http://blog.csdn.net/zhangxin09">博客</a></li>
                                <li><a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22">QQ群：315006</a></li>
                                <li><a href="mailto:support@ajaxjs.com">联系邮箱</a></li>
                            </ul>
                        </fieldset>

                    </Td>
                </tr>
            </table>
            <!-- CNZZ 统计 -->
            <div style="display: none" ;>
                <script src="https://s11.cnzz.com/z_stat.php?id=1261173351&web_id=1261173351"></script>
            </div>
            <!-- // CNZZ 统计 -->
    </body>

    </html>