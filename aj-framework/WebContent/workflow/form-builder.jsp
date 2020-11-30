<%@page pageEncoding="UTF-8"%>
<%
	String shop = "test-shop";
	request.setAttribute("shop", shop);
%>
<!DOCTYPE html>
<html>
    <head>
		<!-- 通用头部 -->
		<jsp:include page="head.jsp">
		    <jsp:param name="lessFile" value="/asset/less/form-builder.less" />
		    <jsp:param name="title" value="Form Builder" />
		</jsp:include>
	</head>

    <body>
    	<div class="components">
    		<h3 class="title">Components</h3>
    		<ul>
    			<li><img src="../asset/images/form-builder/label.png" />Label</li>
    			<li draggable="true" ondragstart="event.dataTransfer.setData('text', event.target.innerText);"><img src="../asset/images/form-builder/inputText.png" />Text Field</li>
    			<li><img src="../asset/images/form-builder/button.png" />Button</li>
    			<li><img src="../asset/images/form-builder/radio.png" />Radio</li>
    			<li><img src="../asset/images/form-builder/checkbox.png" />CheckBox</li>
    			<li><img src="../asset/images/form-builder/textarea.png" />Text Area</li>
    			<li><img src="../asset/images/form-builder/label.png" />Label</li>
    		</ul>
    	</div>
    	<div class="center">
	    	<header>
	    		<h3 class="title">Form Builder</h3>
	    		<div>
	    			<span>
	    				<a href="#" @click="toggleCodeMode" v-bind:class="{active:!codeMode}">Design</a> | <a href="#" @click="toggleCodeMode" v-bind:class="{active:codeMode}">Code</a>
	    			</span>
	    			表单：article  <button>打开</button> <button><i class="fa fa-floppy-o" aria-hidden="true"></i> 保存</button>
	    		</div>
	    	</header>
	    	
    		<div class="stage admin-entry-form" >
		    	
		    	<form v-show="!codeMode" @click="onStageClk">
					<div>
						<label>
							<div contenteditable="true" class="label">名 称：</div> 
							<input placeholder="请填写${uiName}名称" required="required" name="name" value="${info.name}" type="text" />
						</label> 
						<label>
							<div contenteditable="true" class="label">名 称：</div> 
							<input placeholder="请填写${uiName}名称"  required="required" name="name" value="${info.name}" type="text" />
						</label> 
					</div>
					<div @dragenter="onDragEnter" @dragleave="onDragLeave">
						<label> 
							<div contenteditable="true" class="label">名 称：</div><input placeholder="请填写766名称" required="required" name="name22" value="${info.name}" type="text" />
						</label> 
					</div>
					<div @dragenter="onDragEnter" @dragleave="onDragLeave" ondragover="event.preventDefault();" @drop="onDrop">
						<label>
							<div contenteditable="true" class="label">名 称：</div> 
						</label> 
					</div>
					<div @dragenter="onDragEnter" @dragleave="onDragLeave" ondragover="event.preventDefault();" @drop="onDrop">
						<label>
							<div contenteditable="true" class="label">名 称：</div> 
						</label> 
					</div>
					<div @dragenter="onDragEnter" @dragleave="onDragLeave" ondragover="event.preventDefault();" @drop="onDrop">
						<label>
							<div contenteditable="true" class="label">名 称：</div> 
						</label> 
					</div>
				</form>
				
				
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
		<script src="form-builder/startup.js"></script>
    </body>
</html>