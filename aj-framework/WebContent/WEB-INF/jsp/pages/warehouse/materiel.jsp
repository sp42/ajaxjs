<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("UTF-8");
%>
<!doctype html>
<html>
	<head>
		<!-- 全局统一的 HTML 文件头 -->
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
	</head>
	<body>
		<div class="DataDict">	
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${uiName}一览</template>
			</ajaxjs-admin-header>
			
			<!-- 搜索、分类下拉 -->
			<div class="top-panel">
				<div class="whiteLine"></div>
				<p>供应商（Supplier）供应各种所需资源。</p>
				<div class="right">
					分 类 <aj-tree-like-select :catalog-id="66" @selected="onCatalogChange"></aj-tree-like-select>
				</div>
			</div>
			
			<div class="aj-grid admin-panel">
				<!-- 菜单工具栏-->
				<aj-entity-toolbar></aj-entity-toolbar>
				
				<div class="tableHolder">
					<div class="theader-bg"></div>
					<table class="ajaxjs-niceTable">
						<thead>
							<tr><th><input type="checkbox" class="top-checkbox" @click="selectAll" /> </th>
							<th>#</th><th>供应商名称</th><th>联系人</th><th>联系电话</th><th>供应商分类</th><th>创建日期</th><th>状态</th><th>操作</th></tr>
						</thead>
						<tr is="aj-grid-inline-edit-row" v-for="value in list" v-bind:key="value.id" :row-data="value"
							:show-id-col="false" :columns="gridCols()">
						</tr>
					</table>
				</div>
				
				<!-- 菜单工具栏-->
				<div class="toolbar bottom">
					<div class="whiteLine"></div>
					<aj-pager class="right" api-url="../" ref="pager"></aj-pager>
					<div class="stateMsg" v-show="selectedTotal != 0">已选择[{{selectedTotal}}]笔记录</div>
				</div>
			</div>
			
<!-- 实体编辑表单 -->		
<aj-layer ref="createUI">
	<aj-edit-form style="width:900px;" ui-name="${uiName}" ref="form" get-info-api="../">
	   <template v-slot:default="slotProps">
		<table>
	      <tbody>
	        <tr>
	          <td>供应商 id </td>
	          <td>GYS-{{slotProps.info.id}}</td>
	          <td>供应商名称</td>
	          <td rowspan="1" colspan="3" align="left"><input name="name" v-model="slotProps.info.name" type="text" size="40" required /> 
	          </td>
	        </tr>
	        <tr>
	          <td>供应商地址</td>
	          <td rowspan="1" colspan="3" align="left">
	          	<input name="shortAddress" v-model="slotProps.info.name" type="text" size="40" placeholder="请输入地址或者从地址薄选择" /> <a href="#">地址薄选择</a></td>
	          <td>供应商分类</td>
	          <td>
	          	<aj-tree-like-select :catalog-id="66"></aj-tree-like-select>
	          </td>
	        </tr>
	        <tr>
	          <td>联系人</td>
	          <td><input name="供应商联系人" v-model="slotProps.info.供应商联系人" type="text" size="8"></td>
	          <td>联系电话</td>
	          <td><input name="供应商联系电话" v-model="slotProps.info.供应商联系电话" type="text"></td>
	          <td>邮箱</td>
	          <td><input name="邮箱" v-model="slotProps.info.邮箱" type="email"></td>
	        </tr>
	        <tr>
	          <td>付款暂停</td>
	          <td>
	          	<label><input v-model="付款暂停" type="radio" value="0"> 正常</label>  
	          	<label><input v-model="付款暂停" type="radio" value="1" /> 暂停</label>
	          </td>
	          <td>暂停原因码</td>
	          <td>
	            <select name="暂停原因码" :class="{'aj-disable': 付款暂停  != 1}">
	            	<c:foreach items="${暂停原因}">
						<option value="${item.key}" ${item.key == info.sellerId ? 'selected' : ''}>${item.key}-${item.value}</option>
					</c:foreach>
	            </select>
	          </td>
	          <td>暂停付款日期</td>
	          <td>
	          	<aj-form-calendar-input field-name="暂停付款日期" field-value="2019-2-2" :class="{'aj-disable': 付款暂停  != 1}"></aj-form-calendar-input>
	          </td>
	        </tr>
	        <tr>
	          <td>采购员</td>
	          <td><input placeholder="我方对接采购的人员" type="text" size="8" /><input name="采购员" v-model="slotProps.info.采购员" type="hidden"><br>
	          </td>
	          <td>绑定会员</td>
	          <td><input name="userId" type="text" placeholder="若对应会员系统，请绑定其id"  />
	          </td>
	          <td></td> <td></td>
	        </tr>
	        <tr>
	          <td>货币</td><td>
	           <select name="货币">
	            	<c:foreach items="${货币主文件}">
						<option value="${item.key}" ${item.key == info.sellerId ? 'selected' : ''}>${item.key}-${item.value}</option>
					</c:foreach>
	            </select>
	          </td>
	          <td>银行</td><td rowspan="1" colspan="3"> <a href="#">选择银行</a> </td>
	        </tr>
	        <tr>
	          <td>税码</td><td><input name="税码" type="hidden"></td>
	          <td></td><td rowspan="1" colspan="3">  </td>
	        </tr>
	      </tbody>
	    </table>
	    <br />
	    <br />
	    </template>
	</aj-edit-form>
</aj-layer>

		</div>
		
		<script>
BAR = new Vue({
	el: '.DataDict',
	mixins: [aj.SectionModel],
	data: {
		list: [],
		catalogId: 0,
		付款暂停: 0
	},
	mounted() {
		this.$refs.pager.$on("pager-result", result => {
			this.list = result;
			this.maxRows = result.length;
		});
		
		this.$refs.pager.get();
		
	},
	methods: {
		gridCols(){
			return ['id', 'name', '供应商联系人', '供应商联系电话', 'catalogId', 
				data => new Date(data.createDate).format("yyyy-MM-dd"), 'stat'
			];
		},
		getDirty() {
 			var dirties = [];
 			this.list.forEach(item => {
 				if(item.dirty) {
 					item.dirty.id = item.id;
 					dirties.push(item);
 				}
 			});
 			
 			return dirties;
 		},
 		onCatalogChange(v){ 
 			alert(v) 
 		}
	}
}); 

//BAR.$refs.createUI.show();
//BAR.$refs.form.load(1);
		</script>
		
	</body>
</html>
