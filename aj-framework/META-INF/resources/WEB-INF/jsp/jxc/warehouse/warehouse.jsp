<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!doctype html>
<html>
	<head>
		<!-- 全局统一的 HTML 文件头 -->
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
		<script src="${ajaxjs_ui_output}/lib/China_AREA_full.js"></script>
	</head>
	<body>
		<div class="supplier">	
			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">${uiName}一览</template>
			</aj-admin-header>
			
			<div class="soft-container">
				<div class="box padding">
					有经济价值的任何物品的储藏，可指定货架编号。
				</div>
				<div class="box">
					<!-- 菜单工具栏-->
					<aj-entity-toolbar :save="false" @on-create-btn-clk="onCreateClk"></aj-entity-toolbar>
				</div>
				<div class="main-panel">
					<div class="theader-bg"></div>
					<table>
						<thead>
							<tr><th><input type="checkbox" class="top-checkbox" @click="selectAll" /> </th>
							<th>#</th><th>${uiName}名称</th><th>联系人</th><th>联系电话</th><th>地址</th><th>创建日期</th><th>状态</th><th>操作</th></tr>
						</thead>
						<tr is="aj-grid-inline-edit-row" v-for="value in list" v-bind:key="value.id" :row-data="value"
							:show-id-col="false" :columns="gridCols()" dele-api=".">
						</tr>
					</table>
				</div>
				
				<!-- 菜单工具栏-->
				<div class="box bottom-bar padding">
					<aj-pager class="right" api-url="." ref="pager"></aj-pager>
					<div class="stateMsg" v-show="selectedTotal != 0">已选择[{{selectedTotal}}]笔记录</div>
				</div>
			</div>
		</div>
		
<!-- 实体编辑表单 -->		
<template id="edit-form">
	<aj-layer class="editForm" ref="layer" :clean-after-close="true">
		<aj-edit-form style="width:900px;" :is-create="isCreate" ui-name="${uiName}" ref="form" get-info-api="${ctx}/warehouse/">
		   <template v-slot:default="slotProps">
			<table>
		      <tbody>
		        <tr>
		          <td>编号 id </td>
		          <td><span v-if="!isCreate">CK-{{slotProps.info.id}}</span></td>
		          <td>${uiName}名称</td>
		          <td rowspan="1" colspan="3" align="left"><input name="name" v-model="slotProps.info.name" type="text" size="40" required /> 
		          </td>
		        </tr>
		        <tr>
		          <td>状态</td>
		          <td>
		            <select name="stat"> 
		            	<c:foreach items="${StatusDicts}">
							<option value="${item.value}" ${item.key == info.sellerId ? 'selected' : ''}>${item.key}</option>
						</c:foreach>
		            </select>
		          </td>
		          <td>创建日期</td>
		          <td>{{slotProps.info.createDate}}</td>
		          <td>修改日期</td>
		          <td>{{slotProps.info.updateDate}}</td>
		        </tr>
		        <tr>
		          <td>${uiName}地址</td>
		          <td rowspan="1" colspan="5" align="left">
			        <aj-china-area class="left" ref="locationSelector"
			        	province-name="ContactAddress.locationProvince" 
			        	city-name="ContactAddress.locationCity" district-name="ContactAddress.locationDistrict" >
					</aj-china-area> 
		          	&nbsp;&nbsp;<input name="ContactAddress.address" v-model="ContactAddress.address" type="text" size="38" placeholder="请输入地址或者从地址薄选择" />
		          </td>
		        </tr>

		        <tr>
		          <td>联系人</td>
		          <td><input name="ContactAddress.name" v-model="ContactAddress.name" type="text" size="8"></td>
		          <td>联系电话</td>
		          <td><input name="ContactAddress.phone" v-model="ContactAddress.phone" type="text"></td>
		          <td>邮箱</td>
		          <td><input name="ContactAddress.email" v-model="ContactAddress.email" type="email"></td>
		        </tr>
		      </tbody>
		    </table>
		    <br />
		    <br />
		    </template>
		</aj-edit-form>
	</aj-layer>
</template>


		<script>	
			EDIT_FORM = {
				template: "#edit-form",
				data() {
					return {
						付款暂停: 0,
						isCreate: false,
						ContactAddress: {}
					}
				},
				methods: {

				}
			};
		
			GRID = new Vue({
				el: '.supplier',
				mixins: [aj.SectionModel, aj.Grid.common],
				data: {
					catalogId: 0,
					isCreate: true,
					ContactAddress: {},
					showingForm: null
				},
				methods: {
					gridCols(){
						return ['id', 'name', data => data.extractData ? data.extractData.contact : '', 
								data => data.extractData ? data.extractData.phone : '', 
							data => {
								var map = data.extractData;
								
								if(!map)
									return "";
								var str ="";
								
								if(map.locationProvince) 
									str += China_AREA[86][map.locationProvince];
								if(map.locationProvince && map.locationCity) 
									str += China_AREA[map.locationProvince][map.locationCity];
								if(map.locationCity && map.locationDistrict)
									str += China_AREA[map.locationCity][map.locationDistrict];
								str += map.address;
								
								return str;
							}, 
							data => new Date(data.createDate).format("yyyy-MM-dd"), 'stat'
						];
					},
					// 编辑按钮事件
					onEditClk(id) {
						var editForm = new Vue({
							el: document.body.appendChild(document.createElement('div')),
							mixins: [EDIT_FORM]
						});
						
						editForm.$refs.form.load(id, j => {
				
							aj.selectOption.call({ // 选中状态
								$el: editForm.$el.$('select[name=stat]')
							}, j.result.stat);
							
				 			aj.xhr.get('${ctx}/user/address/' + j.result.addressId + '/', j => {
				 				editForm.ContactAddress = j.result;
				 				var locationSelector = editForm.$refs.locationSelector;
				 				
				 				locationSelector.province = editForm.ContactAddress.locationProvince;
				 				locationSelector.city = editForm.ContactAddress.locationCity;
				 				locationSelector.district = editForm.ContactAddress.locationDistrict;
				 			});
			 			});
						
						editForm.$refs.layer.show();
						this.showingForm = editForm;
					},
			 		onCreateClk(){
						var editForm = new Vue({
							el: document.body.appendChild(document.createElement('div')),
							mixins: [EDIT_FORM]
						});
						editForm.isCreate = true;
						editForm.$refs.layer.show();
						this.showingForm = editForm;
			 		}
				}
			}); 
			
			//GRID.onEditClk(3);
		</script>
	</body>
</html>
