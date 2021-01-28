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
					<div class="right">
						分 类 <aj-tree-like-select :catalog-id="66" @selected="onCatalogChange"></aj-tree-like-select>
					</div>
					供应商（Supplier）供应各种所需资源。
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
							<th>#</th><th>供应商名称</th><th>联系人</th><th>联系电话</th><th>供应商分类</th><th>创建日期</th><th>状态</th><th>操作</th></tr>
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
		
<template id="edit-form">
	<!-- 实体编辑表单 -->		
	<aj-layer class="editForm" ref="layer" :clean-after-close="true">
		<aj-edit-form style="width:900px;" :is-create="isCreate" ui-name="${uiName}" ref="form" get-info-api="${ctx}/base/supplier/">
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
		          <td>
		          	<input name="ContactAddress.name" v-model="ContactAddress.name" type="text" size="8">
		          	<!-- 联系人 id （隐藏） -->
		          	<input name="addressId" v-model="slotProps.info.addressId" type="hidden" />
		          </td>
		          <td>联系电话</td>
		          <td><input name="ContactAddress.phone" v-model="ContactAddress.phone" type="text"></td>
		          <td>邮箱</td>
		          <td><input name="ContactAddress.email" v-model="ContactAddress.email" type="email"></td>
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
         		  <td>供应商分类</td>
		          <td>
		          	<aj-tree-like-select :catalog-id="66" ref="catalog" :selected-id="slotProps.info.catalogId"></aj-tree-like-select>
		          </td>
		          <td>采购员</td>
		          <td title="我方对接采购的人员">
		          
		          	<span v-if="slotProps.info.extractData && slotProps.info.extractData.采购员name">
		          		<a :href="'${ctx}/admin/user/' + slotProps.info.采购员 + '/'" target="_blank">
		          			{{slotProps.info.extractData.采购员name}}
		          		</a>
		          	</span>
				    <span v-else>未选择</span>
		          	&nbsp;&nbsp;
		          	<a href="###" @click="selectUser('采购员', '采购员name')"><i class="fa fa-user"></i></a>
		          	<input name="采购员" v-model="slotProps.info.采购员" type="hidden" />
		          </td>
		          
		          <td>绑定会员</td>
		          <td title="若对应会员系统，请绑定其id">
		          	<span v-if="slotProps.info.extractData && slotProps.info.extractData.供应商会员name">
		          		<a :href="'${ctx}/admin/user/' + slotProps.info.userId+ '/'" target="_blank">
		          			{{slotProps.info.extractData.供应商会员name}}
		          		</a>
		          	</span>
				    <span v-else>未选择</span>
		          	&nbsp;&nbsp;
					<a href="#" @click="selectUser('userId', '供应商会员name')"><i class="fa fa-user"></i></a>
			        <input name="userId" v-model="slotProps.info.userId" type="hidden" />
		          </td>

		        </tr>
		        <tr>
		          <td>货币</td><td>
		           <select name="货币">
		            	<c:foreach items="${货币主文件}">
							<option value="${item.key}" ${item.key == info.sellerId ? 'selected' : ''}>${item.key}-${item.value}</option>
						</c:foreach>
		            </select>
		          </td>
		          <td>银行账号</td><td rowspan="1" colspan="3"> {{isCreate ? '请保存记录后再设置' : ''}}
		          	<span v-if="slotProps.info.extractData && slotProps.info.extractData.银行名称">
		          		{{slotProps.info.extractData.银行名称}} {{slotProps.info.extractData.银行账号}}
		          	</span>
		          	<input type="hidden" name="银行码" v-model="slotProps.info.银行码" />				          
		          	<a v-if="!isCreate" href="#" @click="selectBank(slotProps.info.uid)">选择银行</a>
		          </td>
		        </tr>
		        <tr>
		          <td>税码</td><td><!-- <input name="税码" type="hidden"> --></td>
		          <td></td><td rowspan="1" colspan="3">  </td>
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
			 		selectBank(uid) {
			 			SELECT_BANK.show(uid);
			 		},
			 		selectUser(idField, field) {
			 			var info = this.$refs.form.info;
			 	
			 			FIND_USER.show(data => {
			 				info[idField] = data.id;
			 				
			 				if(!info.extractData)
			 					 Vue.set(info, 'extractData', {});
			 				
			 				info.extractData[field] = data.name;
			 			});
			 		}
				}
			};
			
			CATALOGS = ${CATALOGS};
			
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
						return ['id', 'name', 
							d => d.extractData.联系人,  
							d => d.extractData.联系电话,  
							data => CATALOGS[data['catalogId']] && CATALOGS[data['catalogId']].name, 
							data => new Date(data.createDate).format("yyyy-MM-dd"), 'stat'
						];
					},
			 		onCatalogChange(v){ 
			 			alert(v) 
			 		},
					// 编辑按钮事件
					onEditClk(id) {
						var editForm = new Vue({
							el: document.body.appendChild(document.createElement('div')),
							mixins: [EDIT_FORM]
						});
						
						editForm.$refs.form.load(id, j => {
							editForm.$refs.catalog.select(j.result.catalogId);// 选中分类
			 				
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
			
			// GRID.onEditClk(23);
		</script>
		<%@include file="bank-select.jsp" %>
		<%@include file="user-select.jsp" %>
	</body>
</html>
