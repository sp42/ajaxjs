<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
	</head>
	<body>
		<div class="user-list">	
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${uiName}管理</template>
				<template slot="btns"><a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | </template>
			</ajaxjs-admin-header>


			<div class="soft-container">
				<div class="box padding">
					<div class="right">
						分 类 	<aj-tree-user-role-select :value="${empty param.roleId ? '0' : param.roleId}" :json="${UserGroupsJSON}">
							</aj-tree-user-role-select>
					</div>
					查看系统所有的会员资料
				</div>
				<div class="box">
					<!-- 菜单工具栏-->
					<aj-entity-toolbar :create="false" :save="false"> 
						<li onclick="window.open('${ctx}/user/register/');"> 用户注册</li>
						<li class="fa fa-user-o" onclick="USER_GROUP.$refs.layer.show();"> 用户组</li>
						<li class="fa fa-user-circle-o" onclick="ASSIGN_RIGHT.$refs.assignRight.show();"> 角色分配</li>
					</aj-entity-toolbar>
				</div>
				<div class="main-panel">
					<div class="theader-bg"></div>
					<table >
						<thead>
							<tr><th><input type="checkbox" class="top-checkbox" @click="selectAll" /> </th>
								<th>#</th>
								<th>头像</th>
								<th>${uiName}账号</th>
								<th>用户姓名</th>
								<th>性别</th>
								<th>邮件/手机</th>
								<th>注册日期</th>
								<th>用户组</th>
								<th>状态</th>
								<th>控 制</th>
							</tr>
						</thead>
						<tr is="aj-grid-inline-edit-row" v-for="value in list" v-bind:key="value.id" :row-data="value"
							:show-id-col="false" :columns="gridCols()" :enable-inline-edit="false">
						</tr>
					</table> 
				</div>
				<div class="box bottom-bar padding">
					<aj-pager class="right in-one-line" api-url="../listJson/" ref="pager"></aj-pager>
					<div class="stateMsg" v-show="selectedTotal != 0">已选择[{{selectedTotal}}]笔记录</div>
				</div>
			</div>
		</div>
		
		<script src="${ctx}/inline-edit.js"></script>
		
		<%@include file="user-group.jsp" %>
		<%@include file="assign-right.jsp" %>
		
		<script>
BAR = new Vue({
	el: '.user-list',
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
		gridCols: (()=>{
			var sex = data => {
				switch(data['sex']){
					case 1:
						return '男';
					case 2:
						return '女';
					default:
						return '未知';
				}
			}, date = data => new Date(data.createDate).format("yyyy-MM-dd hh:mm"),
			telOrMail = data => {
				var　email = data['email'], phone = data['phone'];
				if(email && phone)
					return email + '<br /> ' + phone;
				return email || phone;
			};
			
			var UserGroupsJson = ${UserGroupsJson}, group = data => UserGroupsJson[data['roleId']].name;
			
			var avatar = data => {
				var prefix = '${aj_allConfig.uploadFile.imgPerfix}';
				var avatar = data.avatar;
				if(!avatar)
					return "";
				
				if(avatar.indexOf('http') === -1)
					avatar = prefix + avatar;
				
				return '<aj-avatar avatar="' + avatar +'"></aj-avatar>';	
			}
			
			return () => {
				return ['id', avatar, 'name', 'username', sex, telOrMail, date, group, 'stat'];
			};
		})(),
		
		// 编辑按钮事件
		onEditClk(id) {
			location.assign('../' + id + '/');
		},
		
 		onCatalogChange(v){ 
 			alert(v) 
 		},
 		onCreateClk(){} 
	}
}); 
// USER_GROUP.$refs.layer.show();
//BAR.$refs.createUI.show();
//BAR.$refs.form.load(1);
	 
			aj.widget.imageEnlarger();// 鼠标移动大图
		</script>
	</body>
</html>