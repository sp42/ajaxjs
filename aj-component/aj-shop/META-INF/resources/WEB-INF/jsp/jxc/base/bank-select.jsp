<%@page pageEncoding="UTF-8"%>
<div class="findBank">
	<aj-layer ref="findBank">
		<h3 style="padding-left: 2%; font-weight: bold;">选用银行账号</h3>
		<div class="soft-container">
			<div class="box padding aj-form" style="padding-left:3%;position: relative;">
				<div class="right">
					<button @click="showCreate">新 建</button> 
					<button @click="clearSearch">复位查询</button>
				</div>
				<div style="position: absolute;bottom: 20px; right:0px;">增加、修改、删除银行账号请到“<a href="${ctx}/base/bank/">银行管理</a>”。</div>
				<input type="text" placeholder="请输入银行名称" class="byBankName" /> <button @click="searchBy('keyword', '.byBankName')">按银行查找</button>
				<br />
				<input type="text" placeholder="请输入银行账号" class="byAccount"  /> <button @click="searchAny('account', '.byAccount')">按账号查找</button>
				<br />
				<aj-form-between-date class="left"></aj-form-between-date>
			</div>
			
			<div class="main-panel"> 
				<div class="theader-bg"></div>
				<table class="in-layer">
					<thead>
						
						<th>#</th><th style="width: 150px;">银行名称</th><th>支行名称</th><th style="width: 250px;">银行账号</th><th>创建日期</th><th>操作</th>
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
				<aj-pager class="right" api-url="${ctx}/base/bank/" :auto-load="false" ref="pager"></aj-pager>
			</div>
		</div>
	</aj-layer>
	
	<aj-layer ref="create">
		<form class="aj-form" style="text-align:center;" action="${ctx}/base/bank/" method="post">
			<h3>新建银行账号</h3>
			<br />
			<br />
			<input type="text" name="name" placeholder="请输入银行名称" size="30" required />
			<br />
			<br />
			<input type="text" name="branch" placeholder="请输入支行名称" size="30" />
			<br />
			<br />
			<input type="text" name="account" placeholder="请输入银行账号" size="30" required />
			
			<input type="hidden" name="owner" :value="owner" />
			<br />
			<br />
			<button>新建</button>
		</form>
	</aj-layer>
</div>

<script>
	SELECT_BANK = new Vue({
		el: '.findBank',
		mixins: [aj.SectionModel, aj.Grid.common, aj.searchPanel],
		data: {
			owner: 0
		},
		mounted() {
			aj.xhr.form(this.$el.$('form.aj-form'), j => {
				if(j && j.isOk) {
					aj.msg.show(j.msg);
					this.$refs.create.close();
					this.$refs.pager.get();
				}
			});
			this.BUS.$on('on-bank-select', this.onSelect);
		},
		methods: {
			gridCols() {
				return ['name', 'branch', 'account', data => new Date(data.createDate).format("yyyy-MM-dd hh:mm"),
					()=> '<aj-grid-select-row type="bank"></aj-grid-select-row>'
				];
			},
			onSelect(data) {
				this.$refs.findBank.close();
				
				var info = GRID.showingForm.$refs.form.info;
				
 				if(!info.extractData)
					 Vue.set(info, 'extractData', {});
				
				info.extractData.银行名称 = data.name;
				info.extractData.银行账号 = data.account;
				
				info.银行码 = data.id;
			},
			showCreate() {
				this.$refs.create.show();
			},
			show(owner) {
				this.owner = this.$refs.pager.baseParam.owner = owner;
				this.$refs.pager.get();
				this.$refs.findBank.show();
			}
		}
	});
</script>

