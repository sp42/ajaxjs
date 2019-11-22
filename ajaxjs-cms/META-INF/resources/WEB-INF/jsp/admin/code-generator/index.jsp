<%@page pageEncoding="UTF-8"%>
<style>
nav {
	background-color: #C55042;
	color: white;
	text-align: center;
	padding: 2%;
	margin: 1px;
}

.body {
	background: #FFC;
	padding: 2%;
	color: #444;
}

input {
	height: 26px;
	padding: 4px;
	box-shadow: 1px 2px 3px #e5e5e5 inset;
	border-radius: 3px;
	border: 1px solid #abadb3;
	box-sizing: border-box;
	vertical-align: middle;
	color: #333;
	font-size: .9rem;
	letter-spacing: 1px;
	transition: border-color ease-in-out 200ms;
}

button {
	height: 26px;
	font-size: .9rem;
	line-height: 10px;
	padding: .4em 2.3em;
	margin: 10px;
	color: #333;
	letter-spacing: .3em;
	border-radius: 4px;
	border: 1px solid #ccc;
	border-bottom-color: #B4B4B4;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12);
	text-shadow: 0 1px 0 rgba(255, 255, 255, 0.8);
	background-color: lightgray;
	background-repeat: repeat-x;
	transition: background-image 0.5s linear 1s;
	background-image: -webkit-linear-gradient(top, #ffffff 0%, #efefef 60%, #e1dfe2 100%);
}

form>div {
	margin: 1%
}
form>div>div{
	display:inline-block;
	min-width:10%;
}
</style>
<nav>AJAXJS-Tools</nav>
<div class="body">

	<!-- http://localhost:8080/ajaxjs-tools/CodeGenerators?getTable=user_admin_reosurces&isMap=true&beanName=UserRoleResources -->
	<form action="." method="post">
		<div>
			<div>SQL表名：</div><input type="text" name="getTable" />
		</div>
		<div>
			<div>BeanName:</div><input type="text" name="beanName" /> 通常用于类名，例如 UserRoleResources
		</div>
		<div>
			<div>isMap: </div><input type="radio" name="isMap" value="true">true <input type="radio" name="isMap" value="false" checked /> false   当 isMap=false 时不生成 Bean 类，id使用 int
			
			
		</div>
		<div>
			<div>saveFolder: </div><input type="text" name="saveFolder" placeholder="C:\project\temp\CodeGenerators\" value="${saveFolder}" size="80" /> 保存目录
		</div>
		<div>
			<div>packageName: </div><input type="text" name="packageName" value="com.ajaxjs.user.role"  size="80" /> 包名
		</div>
		<div>
			<div>dbUrl: </div><input type="text" name="dbUrl" value="${conn.url}" size="80" /> 数据库配置
		</div>
		<div>
			<div>dbUser: </div><input type="text" name="dbUser" value="${conn.username}" /> 
		</div>
		<div>
			<div>dbPassword: </div><input type="text" name="dbPassword" value="${conn.password}" /> 
		</div>
		<div>
			<button>生成</button><br>注意：1、仅支持 MySQL 数据库  
		</div>
	</form>
	<br />
	<hr />
	
	<form action="CodeGenerators" method="post">
		<div>
			<div>saveFolder: </div><input type="text" name="saveFolder" value="C:\\temp" size="80" /> 保存目录
		</div>
		<div>
			<div>packageName: </div><input type="text" name="packageName" value="com.ajaxjs.user.role"  size="80" /> 包名
		</div>
		<div>
			<div>dbUrl: </div><input type="text" name="dbUrl" value="jdbc:mysql://115.28.242.232/zyjf" size="80" /> 数据库配置
		</div>
		<div>
			<div>dbUser: </div><input type="text" name="dbUser" value="root" /> 
		</div>
		<div>
			<div>dbPassword: </div><input type="text" name="dbPassword" value="root123abc" /> 
		</div>	
		<button>生成所有表的（速度较慢）</button>
	</form>
	<br />
	<hr />
	<form action="CodeCount" method="get">
		<div>
			<div>文件夹名：</div><input type="text" name="folder" size="100" />
		</div>
		<div>
			<button>统计代码行数</button>
		</div>
	</form>
</div>