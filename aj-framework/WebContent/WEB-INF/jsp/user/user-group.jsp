<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<div class="user-group">
	<aj-layer ref="layer">	
		<h3 style="padding-left:2%; font-weight: bold;">用户组管理</h3>
		<div class="soft-container">
			<div class="box padding">你可以在这里添加、修改、删除用户角色</div>
			<div class="box">
				<!-- 菜单工具栏-->
				<aj-entity-toolbar :between-date="false" :save="false" :search="false" :dele-btn="false"
					@on-create-btn-clk="onCreateBtnClk(-1)">
				</aj-entity-toolbar>
			</div>
			
			<div class="main-panel" style="max-height:500px; overflow-y: scroll;"> 
				<div class="theader-bg"></div>
				<table>
					<thead>
						<tr>
							<th>id</th>
							<th>名称</th>
							<th>描述</th>
							<th>资源权限值</th>
							<th>创建日期</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<tr v-for="item in list">
		<td class="id">{{item.id}}</td><td class="name" v-html="item.indentName" style="text-align:left;"></td> <td class="content">{{item.content}}</td>
		<td width="120" style="text-align:center;">{{item.accessKey}}</td><td class="createDate">{{new Date(item.createDate).format('yyyy-MM-dd mm:hh')}}</td>
		<td class="action">
			<a href="#" @click="onCreateBtnClk(item.id);"><img src="${commonAssetIcon}/add.gif" /> 新 建</a> | 
			<a href="#" @click="mofidly(item.id, item.name, item.content)"><img src="${commonAssetIcon}/update.gif" /> 修 改</a> | 
			<a href="#" @click="dele(item.id, item.name)"><img src="${commonAssetIcon}/delete.gif" /> 删 除</a> 
		</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="aj-btnsHolder" style="padding:0">
			<button @click="$refs.layer.close()">关闭</button>
		</div>
	</aj-layer>	
	
	
	<aj-layer ref="createOrUpdate">
		<form :action="createOrUpdate.action" :method="createOrUpdate.isCreate ? 'POST' : 'PUT'">
			请输入用户组名称以及简介<br /> <br />
			<input type="text" name="name" v-model="createOrUpdate.name" class="aj-input" style="width:100%;" placeholder="请输入名称" /><br /><br />
			<textarea placeholder="请输入简介" name="content" v-model="createOrUpdate.content" class="aj-input" style="width:400px;height:150px;"></textarea>
			<input type="hidden" class="id" :name="createOrUpdate.isCreate?  'pid' : 'id'" v-model="createOrUpdate.id" />
			
			<div class="aj-btnsHolder" style="padding:0">
				<button>保存</button>
				<button @click.prevent="$refs.createOrUpdate.close()">关闭</button>
			</div>
		</form>
	</aj-layer>
	
</div>
	<script>
		USER_GROUP = new Vue({
			el: '.user-group',
			mixins: [aj.treeLike],
			data: {
				list: [],
				createOrUpdate: {
					isCreate: true,
					id: null, 
					name: null,
					content: null,
					action: null
				}
			},
			mounted() {
				this.load();
				aj.xhr.form(this.$refs.createOrUpdate.$el.$('form'), j =>　{
					j && aj.msg.show(j.msg);
					this.load();
				});
				
			},
			methods: {
				load(){
					this.list = [];
					aj.xhr.get('../user_group/list/', j => {
						if(j.errorMsg) {
							aj.modal(null, {text: j.errorMsg});
							return;
						}
						
						var tree = this.toTree(j.result);
						
						this.output(tree, (node, nodeId) => {
							var level = node.level - 1;
							node.indentName = new Array(level * 8).join('&nbsp;') + (level == 0 ? '' : '└─') + node.name;
							
							this.list.push(node);
						});
					});
				},
				mofidly(id, name, content){
					this.createOrUpdate.isCreate = false;
					this.createOrUpdate.id = id;
					this.createOrUpdate.name = name;
					this.createOrUpdate.content = content;
					this.createOrUpdate.action = '../user_group/' + id + '/';
					this.$refs.createOrUpdate.show();
				},
				onCreateBtnClk(pid) {
					this.createOrUpdate.isCreate = true;
					this.createOrUpdate.id = pid;
					this.createOrUpdate.name = this.createOrUpdate.content = null;
					this.createOrUpdate.action = '../user_group/';
					this.$refs.createOrUpdate.show();
				},
				dele(id, title){
					aj.showConfirm('请确定删除用户组【' + title + '】 ？', () => 
						aj.xhr.dele('../user_group/' + id + '/', j => {
							if (j.isOk) {
								aj.msg.show('删除成功！');
								this.load();
								//setTimeout(() => location.reload(), 1500);
							} else 
								aj.alert('删除失败！');
						})
					);
				}
			}
		});
	</script>
</body>
</html>
