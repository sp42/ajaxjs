<%@page pageEncoding="UTF-8" import="com.ajaxjs.user.role.RoleService"%>
<div class="assign-right">
	<aj-layer ref="assignRight">
		<h3>权限管理系统</h3>
		<span v-show="currentUserGroup" class="right">已选择用户组：{{currentUserGroup}}</span>
		
			<!-- 树控件 --> 
			<div class="tree-panel">
				<h4>用户组</h4>
				<!-- <aj-tree url="/admin/user/user_group/list/?limit=99" top-node-name="用户组"></aj-tree> -->
			</div>
			<table>
				<!-- <caption>权限管理系统</caption> -->
				<tr>
					<th style="width: 180px;">
						<div class="out"><b>操作权限</b> <em>功能模块</em></div>
					</th>
					<th>子模块</th>
					<th style="width: 380px;">操作权限</th>
				</tr>
				<tr>
					<td rowspan="3">前/后台</td>
					<td>允许浏览前台</td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.FRONTEND_ALLOW_ENTNER%>">
						</aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>进入后台</td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.ADMIN_SYSTEM_ALLOW_ENTNER%>">
						</aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td><p>API 接口</p></td>
					<td><aj-admin-role-check-right :res-id="<%=RoleService.API_ALLOW_ACCESS%>"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td rowspan="2">文章模块</td>
					<td>上线文章</td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.ARTICLE_ONLINE%>" :set-right-value="5">
						</aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>已下线文章</td>
					<td><aj-admin-role-check-right :res-id="<%=RoleService.ARTICLE_OFFLINE%>" :set-right-value="5"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td rowspan="2">招聘模块</td>
					<td>上线招聘</td>
					<td><aj-admin-role-check-right :res-id="<%=RoleService.HR_ONLINE%>" :set-right-value="5"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>下线招聘</td>
					<td><aj-admin-role-check-right :res-id="<%=RoleService.HR_OFFLINE%>" :set-right-value="5"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td rowspan="2">产品模块</td>
					<td>上线产品</td>
					<td><aj-admin-role-check-right :res-id="<%=RoleService.PRODUCT_ONLINE%>" :set-right-value="5"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>下线产品</td>
					<td><aj-admin-role-check-right :res-id="<%=RoleService.PRODUCT_OFFLINE%>" :set-right-value="5"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td rowspan="2">商城模块</td>
					<td>商城管理员</td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.SHOP%>"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>商家</td>
					<td><aj-admin-role-check-right :res-id="<%=RoleService.SHOP_SELLER%>"></aj-admin-role-check-right></td>
				</tr>
				<tr>
					<td>留言反馈模块</td>
					<td></td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.FEEDBACK%>"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>广告模块</td>
					<td></td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.ADS%>"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>栏目馈模块</td>
					<td></td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.SECTION%>"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>网站模块</td>
					<td></td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.WEBSITE%>"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>全局参数</td>
					<td></td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.GLOBAL_SETTING%>"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>用户管理</td>
					<td></td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.USER%>"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>用户分配权限</td>
					<td></td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.USER_PRIVILEGE%>"></aj-admin-role-check-right>
					</td>
				</tr>
				<tr>
					<td>开发者工具模块</td>
					<td></td>
					<td>
						<aj-admin-role-check-right :res-id="<%=RoleService.DEVELOPER_TOOL%>"></aj-admin-role-check-right>
					</td>
				</tr>
				<c:foreach items="${ExtendedRight}">
					<tr>
						<td>${item.description}</td>
						<td></td>
						<td><aj-admin-role-check-right :res-id="${item.value}"></aj-admin-role-check-right></td>
					</tr>
				</c:foreach>
			</table>
		
		
		<div class="aj-btnsHolder" style="padding: 0;">
			<button @click="$refs.assignRight.close()">关闭</button>
		</div>
	</aj-layer>
</div>

<style>
	.tree-panel {
		float:left;
		width:30%;
	}
	.assign-right table {
		float:right;
	}
</style>
<template id="aj-admin-role-check-right">
	<div>
		<label>
			<input type="checkbox" name="enable" v-model="enabled" @change="userEnableClick" /> 启 用
		</label> 
		<span :class="{crud: true, disabled: !this.enabled}" v-if="this.setRightValue || this.setRightValue === 0"> 
			<label><input type="checkbox" name="read"   v-model="allowRead"   @change="crudClick" /> 查 询</label> 
			<label><input type="checkbox" name="create" v-model="allowCreate" @change="crudClick" /> 新 增</label> 
			<label><input type="checkbox" name="update" v-model="allowUpdate" @change="crudClick" /> 修 改</label> 
			<label><input type="checkbox" name="delete" v-model="allowDelete" @change="crudClick" /> 删 除</label>
		</span>
	</div>
</template>