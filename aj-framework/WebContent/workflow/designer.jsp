<%@page pageEncoding="UTF-8"%>
<% 
	request.setCharacterEncoding("utf-8");
	request.setAttribute("HEAD_TITLE", "工作流流程设计器");
%>
<!DOCTYPE html>
<html>
<head>
	<!-- 通用头部 -->
	<jsp:include page="head.jsp">
	    <jsp:param name="lessFile" value="/asset/less/workflow.less" />
	    <jsp:param name="title" value="${HEAD_TITLE}" />
	</jsp:include>
	
	<!-- 工作流组件 -->
	<script src="designer/lib/raphael-2.3.js"></script>
    <script src="designer/svg-util.js"></script>
    <script src="designer/svg-serialize.js"></script>
    <script src="designer/svg-resize.js"></script>
    <script src="designer/svg-dot.js"></script>
    <script src="designer/svg-path.js"></script>
    <script src="designer/svg-component.js"></script>
    <script src="designer/svg.js"></script>
    <script src="designer/test-data.js"></script>
</head>
<body>
	<div class="components">
    	<h3 class="title">组件 Components</h3>
    	<ul>
			<li class="selectable selected pointer">
				<img src="../asset/images/workflow/select16.gif" /> 选择 Select
			</li>
			<li class="selectable path">
				<img src="../asset/images/workflow/flow_sequence.png" /> 流转 Transition
			</li>
			<li class="hr"><hr /></li>
			<li class="state" data-type="start">
				<img src="../asset/images/workflow/start.png" /> 开始节点 Start
			</li>
			<li class="state" data-type="end">
				<img src="../asset/images/workflow/end.png" /> 结束节点 End
			</li>
			<li class="state" data-type="task">
				<img src="../asset/images/workflow/process.png" /> 任务节点 Task
			</li>
			<li class="state" data-type="custom">
				<img src="../asset/images/workflow/process.png" /> 自定义节点 Custom
			</li>
			<li class="state" data-type="subprocess">
				<img src="../asset/images/workflow/process.png" /> 子流程 Subprocess
			</li>
			<li class="state" data-type="decision">
				<img src="../asset/images/workflow/fork.png" /> 抉择节点 Decision
			</li>
			<li class="state" data-type="fork">
				<img src="../asset/images/workflow/fork.png" /> 分支节点 Fork
			</li> 
			<li  class="state" data-type="join">
				<img src="../asset/images/workflow/join.png" /> 合并节点 Join
			</li>
   		</ul>
    </div>
    
   	<div class="center">
    	<header>
    		<h3 class="title">${HEAD_TITLE} Workflow Designer</h3> 
    		<div>
    			<span>
    				<a href="#" @click="toggleCodeMode" v-bind:class="{active:!codeMode}">Design</a> | 
    				<a href="#" @click="toggleCodeMode" v-bind:class="{active:codeMode}">Code</a>
    			</span>
    			流程：请假 <button>打开</button> <button><i class="fa fa-floppy-o" aria-hidden="true"></i>保存</button>
    		</div>
    	</header>
    	
   		<div class="stage admin-entry-form" >
	    	<div v-show="!codeMode" class="canvas"></div>
			<div class="codeEdit" v-show="codeMode">
				<pre class="prettyprint">var menuPanel = el.parentNode;
menuPanel.style.display = 'none';
setTimeout(function(){
  menuPanel.style.display = '';
}, 500);</pre>
			</div>
   		</div>
   	</div>
    	
   	<div class="right">
   		<!-- 属性修改面板 -->
   		<div class="dataSource">
   			<h3 class="title">Data Source</h3>
   			<label>表名：
    			<select class="aj-select" @change="tableSelect($event)">
					<option>不指定</option>
					<option v-for="v in tables">{{v}}</option>
				</select>
   			</label>
   			<ul>
   				<li v-for="v in fields" draggable="true">
   					<div>{{v.name}}<span>:{{v.type}}</span> <img :src="ajResources.commonAsset + '/icon/add.gif'" title="绑定该字段" /></div>
   					<div class="comment">{{v.comment}}</div>
   				</li>
   			</ul>
   		</div>

   		<!-- 属性修改面板 -->
   		<div class="bottom">
   			<div class="property">
				<h3 class="title">Property Editor</h3>
				<table>
					<tr><td class="key">id</td>			<td><input type="text" /></td></tr>
					<tr><td class="key">名称 name</td>	<td><input type="text" v-model="name" /></td></tr>
					<tr><td class="key">提示</td>			<td><input type="text" v-model="placeHolder" /></td></tr>
					<tr><td class="key">值 value</td>		<td><input type="text" /></td></tr>
					<tr><td class="key">值类型</td>		<td>
						<select class="aj-select">
							<option>不指定</option>
							<option>纯数字</option>
							<option></option>
						</select>
					</td></tr>
					<tr><td class="key">必填 required</td><td><input type="checkbox" /></td></tr>
					<tr><td class="key">正则验证</td>		<td><input type="text" /></td></tr>
				</table>
				<div class="bottom"></div>
			</div>
   		</div>
   	</div>

	<template class="propertyTpl">
		<div class="property">
			<h3 class="title">Property Editor</h3>
			<table>
				<tr><td class="key">id</td>			<td><input type="text" /></td></tr>
				<tr><td class="key">名称 name</td>	<td><input type="text" v-model="name" /></td></tr>
				<tr><td class="key">提示</td>			<td><input type="text"  /></td></tr>
				<tr><td class="key">值 value</td>		<td><input type="text" /></td></tr>
				<tr><td class="key">值类型</td>		<td>
					<select class="aj-select">
						<option>不指定</option>
						<option>纯数字</option>
						<option></option>
					</select>
				</td></tr>
				<tr><td class="key">必填 required</td><td><input type="checkbox" /></td></tr>
				<tr><td class="key">正则验证</td>		<td><input type="text" /></td></tr>
			</table>
			<div class="bottom"></div>
		</div>
	</template>
		
	<!-- 状态栏 -->
	<div class="statusBar" v-bind:class="{hideMe:!show}">{{text}}</div>
	
	<!-- 可视化环境组件 -->
	<script src="wysiwyg/wysiwyg.main.js"></script>
	<script src="wysiwyg/wysiwyg.data-source.js"></script>
	<script src="wysiwyg/wysiwyg.property-editor.js"></script>
	<script src="designer/startup.js"></script>
</body>
</html>
