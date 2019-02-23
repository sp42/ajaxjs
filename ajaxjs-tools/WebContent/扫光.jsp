<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用div+css模拟表格对角线</title>
<style type="text/css">
	* {
		padding: 0;
		margin: 0;
	}
	
	
	
	
	 .sample{
    background-color: #0E1326;
    padding-top:30px;
    overflow: hidden;
  }
  .blank_text{
    position: relative;
    width:200px;
    margin:20px auto;
    color: #fff;
    line-height: 1;
    font-size: 50px;
    font-size: 0.74074rem;
    text-align: center;
    overflow: hidden;
    font-family: "icomoon";
  }
.blank_text:after{
    width: 300%;
    height: 100%;
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    background: -webkit-gradient(linear, left top, right top, color-stop(0, rgba(15,20,40, 0.7)), color-stop(0.4, rgba(15,20,40, 0.7)), color-stop(0.5, rgba(15,20,40, 0)), color-stop(0.6, rgba(15,20,40, 0.7)), color-stop(1, rgba(15,20,40, 0.7)));
    -webkit-animation: slide ease-in-out 2s infinite; 
}
@-webkit-keyframes slide{
    0%{-webkit-transform:translateX(-66.666%);}
    100%{-webkit-transform:translateX(0);}
}


/* */
@keyframes aniBlink {
from {
margin-left:-440px
}
to {
    margin-left:500px
}
}
@-webkit-keyframes aniBlink {
from {
margin-left:-440px
}
to {
    margin-left:500px
}
}
.logo {
    position:relative;
    width:440px;
    height:160px;
    overflow:hidden;
}
.logo a {
    display:inline-block
}
.logo a:before {
    content:'';
    position:absolute;
    width:60px;
    height:160px;
    margin-top:0px;
    margin-left:-440px;
    overflow:hidden;
    z-index:6;
  overflow: hidden;
  background: -moz-linear-gradient(left, rgba(255, 255, 255, 0) 0, rgba(255, 255, 255, 0.2) 50%, rgba(255, 255, 255, 0) 100%);
  background: -webkit-gradient(linear, left top, right top, color-stop(0%, rgba(255, 255, 255, 0)), color-stop(50%, rgba(255, 255, 255, 0.2)), color-stop(100%, rgba(255, 255, 255, 0)));
  background: -webkit-linear-gradient(left, rgba(255, 255, 255, 0) 0, rgba(255, 255, 255, 0.2) 50%, rgba(255, 255, 255, 0) 100%);
  background: -o-linear-gradient(left, rgba(255, 255, 255, 0) 0, rgba(255, 255, 255, 0.2) 50%, rgba(255, 255, 255, 0) 100%);
  -webkit-transform: skewX(-25deg);
  -moz-transform: skewX(-25deg);
}
.logo:hover a:before {
 -webkit-animation:aniBlink .8s ease-out forwards;
 -moz-animation:aniBlink .8s ease-out forwards;
 -o-animation:aniBlink .8s ease-out forwards;
 animation:aniBlink .8s ease-out forwards
}
</style>
</head>
<body>

<div class="sample">
    <div class="blank_text">haorooms博客扫光测试</div>
</div>

<div class="logo">
    <a href="http://www.haorooms.com"><img src="https://pic1.zhimg.com/80/f72949cfeafa969f97b2ae994d37092e_hd.jpg" /></a>
</div>



</body>
</html>