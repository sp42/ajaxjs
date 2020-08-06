<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
		<jsp:param name="title" value="${uiName}管理" />
	</jsp:include>
</head>

<body>
	<div class="tree-like">
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header>
			<template slot="title">${uiName}一览</template>
		</ajaxjs-admin-header>
		
		<div class="soft-container">
			<div class="box padding">你可以在这里添加、修改、删除${uiName}。请于分类中点选目标的节点，成为选中的状态后，再进行下面的编辑。</div>
			<div class="box">
				<!-- 菜单工具栏-->
				<div class="toolbar">
					<ul>
						<li @click="rename"><i class="fa fa-pencil-square-o" style="color:#0a90f0;"></i> 修改分类名称</li>
						<li @click="dele"><i class="fa fa-trash-o" aria-hidden="true" style="color:red;"></i> 删除</li>
						<li>
							<form action="../" method="post" class="createUnderNode">
								<input type="hidden" name="pid" :value="selectedId" />
								<input type="text" name="name" required size="12" /> 
								<button><i class="fa fa-plus" aria-hidden="true" style="color:#ffaf0a;"></i> 新建子分类</button>	
							</form>
						</li>
						<li>
							<form action="../" method="post" class="createTopNode">
								<input type="hidden" name="pid" value="-1" />
								<input type="text" name="name" size="12" required /> 
								<button><i class="fa fa-plus" aria-hidden="true" style="color:#ffaf0a;"></i> 新增顶级${uiName}</button>	
							</form>
						</li>
					</ul>
				</div>
			</div>
			
			<div class="main-panel">
				<select multiple @change="onChange"></select>
			</div>
		
			<div class="box bottom-bar padding">
				<b v-show="selectedId != 0">已选择# {{selectedId}}-{{selectedName}}</b>
			</div> 
		</div>
		<aj-layer ref="layer">
			<form class="rename" :action="'../' + selectedId + '/'" method="put" style="text-align:center;">
				<h5>修改${uiName}名称</h5>
				<br />
				名称： <input type="text" class="aj-input" name="name" :value="selectedName" required /> 
				<br />
				<br />
				<button class="aj-btn">
					<img src="${commonAssetIcon}/update.gif" /> 更新名称
				</button>
				<br />
				<br />
			</form>
		</aj-layer>
	</div>

	<script>
		new Vue({
			el: '.tree-like',
			mixins: [aj.treeLike],
			data: {
				selectedId: 0,
				selectedName: ''
			},
			mounted(){
				// 新增顶级${uiName}
				aj.xhr.form(".createTopNode", json => {
					aj(".createTopNode input[name=name]").value = '';
					this.refresh(json);
				});

				// 在${uiName}下添加子${uiName}
				aj.xhr.form(".createUnderNode", json => {
					aj(".createUnderNode input[name=name]").value = '';
					this.refresh(json);
				});
				
				// 修改名称
				aj.xhr.form(this.$el.$('.rename'), json => {
					this.$refs.layer.close();
					this.refresh(json);
				});

				this.render();
			},
			methods: {
				onChange($event) {
					var selectEl = $event.target, option = selectEl.selectedOptions[0], 
						id = option.value, pid = option.dataset['pid'], name = option.innerHTML.replace(/&nbsp;|└─/g, '');
					
					this.selectedId = id;
					this.selectedName = name;
				},
				rename() {
					if(!this.selectedId) {
						aj.alert.show('未选择任何分类');
						return;
					}
					
					this.$refs.layer.show();
				},
				// 删除
				dele() {
					if(!this.selectedId) {
						aj.alert.show('未选择任何分类');
						return;
					} 
					
					aj.showConfirm('确定删除该${uiName}[{0}]？<br />[{0}]下所有的子节点也会随着一并全部删除。'.replace(/\{0\}/g, this.selectedName), 
						() => aj.xhr.dele("../" + this.selectedId + "/", this.refresh)
					);
				},
				refresh(json) {
					if (json.isOk) {
						aj.alert.show(json.msg);
						this.render();
					} else 
						aj.alert.show(json.msg);
				},
				
				render() {
					var select = this.$el.$('select');
					select.innerHTML = '';
					aj.xhr.get('.', j => this.rendererOption(j.result, select));
				}
			}
		});
	</script>
</body>
</html>
