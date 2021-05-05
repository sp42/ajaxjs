<%@page pageEncoding="UTF-8"%>
<div class="findUser">
	<aj-layer ref="findUser">
		<h3 style="padding-left: 2%; font-weight: bold;">选择用户</h3>
		<div class="soft-container">
			<div class="box padding aj-form" style="padding-left:3%;position: relative;min-width: 800px;">
				<div class="right">
					<button @click="clearSearch">复位查询</button>
				</div>
				<div style="position: absolute;bottom: 10px; right: 10px;"><a href="${ctx}/base/bank/">用户管理</a> </div>
				<input type="text" placeholder="请用户id或账号名或姓名" class="keyword" /> <button @click="searchBy('keyword', '.keyword')">查找</button>
				<br />
	
				<aj-form-between-date class="left"></aj-form-between-date>
			</div>
			
			<div class="main-panel"> 
				<div class="theader-bg"></div>
				<table class="in-layer">
					<thead>
						<tr>
							<th>#</th><th>头像</th><th>用户账号</th><th>用户姓名</th><th>性别</th><th>注册日期</th><th>操作</th>
						</tr>
					</thead>
					<tr is="aj-grid-inline-edit-row" v-for="value in list" v-bind:key="value.id" :row-data="value"
						:show-checkbox-col="false" :show-control="false"
						:columns="gridCols()" :enable-inline-edit="false"
						:filter-field="['id', 'createDate']" dele-api=".">
					</tr>
				</table>
			</div>
			<!-- 菜单工具栏-->
			<div class="box bottom-bar padding">
				<aj-pager class="right" api-url="${ctx}/admin/user/listJson/" ref="pager"></aj-pager>
			</div>
		</div>
	</aj-layer>
</div>

<script>

var avatar = data => {
	var prefix = '${aj_allConfig.uploadFile.imgPerfix}';
	var avatar = data.avatar;
	if(!avatar)
		return "";
	
	if(avatar.indexOf('http') === -1)
		avatar = prefix + avatar;
	
	return '<aj-avatar avatar="' + avatar +'"></aj-avatar>';	
}

var sex = data => {
	switch(data.sex) {
	case 0:
		return '未知';
	case 1:
		return '男';
	case 2:
		return '女';
	}
}

	FIND_USER = new Vue({
		el: '.findUser',
		mixins: [aj.SectionModel, aj.Grid.common, aj.searchPanel],
		data: {
			owner: 0,
			how2showSelected: ()=>{} // 目标对象，选择好的数据修改到这里
		},
		mounted() {
			aj.xhr.form(this.$el.$('form.aj-form'), j => {
				if(j && j.isOk) {
					aj.msg.show(j.msg);
					this.$refs.create.close();
					this.$refs.pager.get();
				}
			});
			this.BUS.$on('on-user-select', this.onSelect);
			this.BUS.$on('on-open-link-clk', data => {
				window.open("${ctx}/admin/user/" + data.id + "/");
			});
		},
		methods: {
			gridCols() {
				return [avatar, 'name', 'username', sex, data => new Date(data.createDate).format("yyyy-MM-dd"),
					()=> '<span><aj-grid-select-row type="user"></aj-grid-select-row> | <aj-grid-open-link></aj-grid-open-link></span>'
				];
			},
			onSelect(data) {
				this.$refs.findUser.close();
				this.how2showSelected(data);
			},
			showCreate() {
				this.$refs.create.show();
			},
			show(how2showSelected) {			
				this.how2showSelected = how2showSelected;
				this.$refs.findUser.show();
			}
		}
	});
	
	
</script>

