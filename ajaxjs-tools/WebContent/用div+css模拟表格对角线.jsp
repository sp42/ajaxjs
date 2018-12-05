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
	
	caption {
		font-size: 14px;
		font-weight: bold;
	}
	
	table {
		border-collapse: collapse;
		border: 1px #525152 solid;
		width: 50%;
		margin: 0 auto;
		margin-top: 100px;
	}
	
	th, td {
		border: 1px #525152 solid;
		text-align: center;
		font-size: 12px;
		line-height: 30px;
	}
	
	/*模拟对角线*/
	.out {
		border-top: 40px #D6D3D6 solid; /*上边框宽度等于表格第一行行高*/
		width: 0px; /*让容器宽度为0*/
		height: 0px; /*让容器高度为0*/
		border-left: 80px #BDBABD solid; /*左边框宽度等于表格第一行第一格宽度*/
		position: relative; /*让里面的两个子容器绝对定位*/
	}
	
	b {
		font-style: normal;
		display: block;
		position: absolute;
		top: -40px;
		left: -40px;
		width: 35px;
	}
	
	em {
		font-style: normal;
		display: block;
		position: absolute;
		top: -25px;
		left: -70px;
		width: 55x;
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
    <a href="http://www.haorooms.com"><img src="http://sandbox.runjs.cn/uploads/rs/216/0y89gzo2/banner03.jpg" /></a>
</div>


	<table>
		<caption>用div+css模拟表格对角线</caption>
		<tr>
			<th style="width: 80px;">
				<div class="out">
					<b>类别</b> <em>姓名</em>
				</div>
			</th>
			<th>年级</th>
			<th>班级</th>
			<th>成绩</th>
			<th>班级均分</th>
		</tr>
		<tr>
			<td class="t1">张三</td>
			<td>三</td>
			<td>2</td>
			<td>62</td>
			<td>61</td>
		</tr>
		<tr>
			<td class="t1">李四</td>
			<td>三</td>
			<td>1</td>
			<td>48</td>
			<td>67</td>
		</tr>
		<tr>
			<td class="t1">王五</td>
			<td>三</td>
			<td>5</td>
			<td>79</td>
			<td>63</td>
		</tr>
		<tr>
			<td class="t1">赵六</td>
			<td>三</td>
			<td>4</td>
			<td>89</td>
			<td>66</td>
		</tr>
	</table>
</body>
</html>