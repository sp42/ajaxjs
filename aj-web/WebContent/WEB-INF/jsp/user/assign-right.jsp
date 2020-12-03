<%@page pageEncoding="UTF-8" import="com.ajaxjs.user.role.RightConstant"%>
<div class="assign-right">
	<aj-layer ref="assignRight">
		<h3>权限管理系统</h3>
		<div class="soft-container">
			<div class="box padding">
				<span v-show="currentUserGroup" class="right">已选择用户组：{{currentUserGroup}}</span>
				修改权限后，用户退出当前帐号重新登录才有效。
			</div>
			<div class="main-panel">
				<!-- 树控件 --> 
				<div class="tree-panel">
					<h4>用户组</h4>
					<aj-tree url="/admin/user/user_group/list/?limit=99" top-node-name="用户组">
					</aj-tree>
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
							<aj-admin-role-check-right :res-id="<%=RightConstant.FRONTEND_ALLOW_ENTNER%>">
							</aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>进入后台</td>
						<td>
							<aj-admin-role-check-right :res-id="<%=RightConstant.ADMIN_SYSTEM_ALLOW_ENTNER%>">
							</aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td><p>API 接口</p></td>
						<td><aj-admin-role-check-right :res-id="<%=RightConstant.API_ALLOW_ACCESS%>"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td rowspan="2">文章模块</td>
						<td>上线文章</td>
						<td>
							<aj-admin-role-check-right :res-id="<%=RightConstant.ARTICLE_ONLINE%>" :set-right-value="5">
							</aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>已下线文章</td>
						<td><aj-admin-role-check-right :res-id="<%=RightConstant.ARTICLE_OFFLINE%>" :set-right-value="5"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td rowspan="2">招聘模块</td>
						<td>上线招聘</td>
						<td><aj-admin-role-check-right :res-id="<%=RightConstant.HR_ONLINE%>" :set-right-value="5"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>下线招聘</td>
						<td><aj-admin-role-check-right :res-id="<%=RightConstant.HR_OFFLINE%>" :set-right-value="5"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td rowspan="2">产品模块</td>
						<td>上线产品</td>
						<td><aj-admin-role-check-right :res-id="<%=RightConstant.PRODUCT_ONLINE%>" :set-right-value="5"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>下线产品</td>
						<td><aj-admin-role-check-right :res-id="<%=RightConstant.PRODUCT_OFFLINE%>" :set-right-value="5"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td rowspan="2">商城模块</td>
						<td>商城管理员</td>
						<td>
							<aj-admin-role-check-right :res-id="<%=RightConstant.SHOP%>"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>商家</td>
						<td><aj-admin-role-check-right :res-id="<%=RightConstant.SHOP_SELLER%>"></aj-admin-role-check-right></td>
					</tr>
					<tr>
						<td>留言反馈模块</td>
						<td></td>
						<td>
							<aj-admin-role-check-right :res-id="<%=RightConstant.FEEDBACK%>"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>广告模块</td>
						<td></td>
						<td>
							<aj-admin-role-check-right :res-id="<%=RightConstant.ADS%>"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>栏目馈模块</td>
						<td></td>
						<td>
							<aj-admin-role-check-right :res-id="<%=RightConstant.SECTION%>"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>网站模块</td>
						<td></td>
						<td>
							<aj-admin-role-check-right :res-id="<%=RightConstant.WEBSITE%>"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>全局参数</td>
						<td></td>
						<td>
							<aj-admin-role-check-right :res-id="<%=RightConstant.GLOBAL_SETTING%>"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>用户管理</td>
						<td></td>
						<td>
							<aj-admin-role-check-right :res-id="<%=RightConstant.USER%>"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>用户分配权限</td>
						<td></td>
						<td>
							<aj-admin-role-check-right :res-id="<%=RightConstant.USER_PRIVILEGE%>"></aj-admin-role-check-right>
						</td>
					</tr>
					<tr>
						<td>开发者工具模块</td>
						<td></td>
						<td>
							<aj-admin-role-check-right :res-id="<%=RightConstant.DEVELOPER_TOOL%>"></aj-admin-role-check-right>
						</td>
					</tr>
	
				</table>
			</div>
		</div>
		<div class="aj-btnsHolder" style="padding: 0">
			<button @click="$refs.assignRight.close()">关闭</button>
		</div>
	</aj-layer>
</div>

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

<script>
	// 功能权限控件
	Vue.component("aj-admin-role-check-right", {
		template: '#aj-admin-role-check-right',
		props: {
			resId: Number, 			// 资源权限值
			setRightValue: Number,	// 操作权限值， 8421码
		},
		data() {
			return {
				enabled: true,
				rightValue :  this.setRightValue,
				allowRead  : (this.setRightValue & 1) === 1,
				allowCreate: (this.setRightValue & 2) === 2,
				allowUpdate: (this.setRightValue & 4) === 4,
				allowDelete: (this.setRightValue & 8) === 8
			};
		},
		mounted() {
			/* 可以通过 props 单向绑定 resRightValue，但每个组件要设置一样属性。这里避免多处重复设置属性  */
			this.$watch('$parent.$parent.resRightValue', v => this.enabled = this.check(v, this.resId));
		},
		methods: {
			/**
			 * 检查是否有权限
			 
			 * @return {Boolean} true =  有权限，反之无
			 */
			check(num, pos) {
				num = num >>> pos;
				return (num & 1) === 1;
			},
			toggleRight(val, right) {
				if(val === false && ((this.rightValue & right) === right)) // 有权限
					this.rightValue -= right;
				
				if(val === true && ((this.rightValue & right) !== right)) 
					this.rightValue += right;
			},
			userEnableClick(e) { // 用户点击事件，不是来自数据的变化，修改立刻被保存到服务端
				var isEnable = e.target.checked, userGroupId = ASSIGN_RIGHT.userGroupId; // 全局变量
				
				if(userGroupId && this.resId) {				
					aj.xhr.post('../user_group/updateResourceRightValue', j => aj.msg.show(j.msg), {
						userGroupId: userGroupId, isEnable: isEnable, resId: this.resId
					});
				}
			},
			crudClick(){}
		},
		watch: {
			enabled() {
				this.$el.$('.crud input', input => input.disabled = !this.enabled);
			},
			allowRead(val) {
				this.toggleRight(val, 1);
			},
			allowCreate(val) {
				this.toggleRight(val, 2);
			},
			allowUpdate(val) {
				this.toggleRight(val, 4);
			},
			allowDelete(val) {
				this.toggleRight(val, 8);
			}
		}
	});
	
	ASSIGN_RIGHT = new Vue({
		el: '.assign-right', 
		data: {
			userGroupId: null,		// 用户组 id
			resRightValue: 0,		// 用户组权限总值
			currentUserGroup: ''	// UI 提示用
		},
		mounted() {
			// 点击树节点时候，加载用户组的详情信息
			this.BUS.$on('tree-node-click', data => {
				if(data.id){
					this.userGroupId = data.id; 
					this.currentUserGroup = data.name;
					aj.xhr.get('../user_group/'+ data.id + '/', j => this.resRightValue = j.result.accessKey || 0);
				}
			});
		}
	});
	
	ASSIGN_RIGHT.$refs.assignRight.show();
</script>