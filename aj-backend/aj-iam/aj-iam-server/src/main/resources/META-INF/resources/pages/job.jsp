<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.toway.cc2.service.AdminService" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common.jsp" %>
</head>
<body class="inner-page">
	<h2>Spring 定时任务管理</h2>
	<%
	    AdminService.getMenuGroup(request);
	%>
	
	<div id="vue">
		<div class="btns" align="right">
			<a href="#" @click="isShowCreate = true"><span style="color:green">➕</span> 添加</a>
		</div>
		<table class="list-table">
			<thead>
				<tr>
					<th>id</th><th>菜单 </th><th>编组 id</th><th>任务类型 id</th><th>操作</th>
				</tr>
			</thead>
			<tr v-for="(item) in mapList">
				<td>{{item.id}}

				</td>
				<td>
					<span v-show="editingId != item.id">{{item.name}}-{{item.menuId}}</span>
					<input type="text" v-show="editingId == item.id" v-model="item.name" />
					<input type="number" v-show="editingId == item.id" v-model="item.menuId" style="width:50px" />
				</td>
				<td>
					<select v-model="item.groupId">
						<option v-for="(group) in groups" :value="group.resoureGroupId">
							{{group.resoureGroupName}}-{{group.resoureGroupId}}
						</option>
					</select>
				</td>
				<td>
				    {{TASK_TYPE[item.taskTypeId]}}-{{item.taskTypeId}}
				    <input type="number" v-show="editingId == item.id" v-model="item.taskTypeId" style="width:50px" />
				</td>
				<td>
					<a href="#" @click="del(item.id)" v-show="editingId != item.id"><span style="color:red">✖</span> 删除</a>
					<a href="#" @click="save(item)" v-show="editingId == item.id"><span style="color:green">✔</span> 保存</a>
					| 
					<a href="#" @click="editingId = item.id" v-show="editingId != item.id">编辑</a>
					<a href="#" @click="editingId = 0" v-show="editingId == item.id"> 取消</a>
				</td>
			</tr>
			<tr :class="{hide: !isShowCreate}">
				<td></td>
				<td><input type="input" placeholder="名称" v-model="create.name" /> <input type="number" placeholder="菜单 id" v-model="create.menu_id" /></td>
				<td>
					<select v-model="create.group_id">
						<option v-for="(group) in groups" :value="group.resoureGroupId">
							{{group.resoureGroupName}}-{{group.resoureGroupId}}
						</option>
					</select>
				</td>
				<td>
					<input type="number" placeholder="任务类型 id" v-model="create.task_type_id" />
				</td>
				<td>
					<a href="#" @click="doCreate">新增</a> | <a href="#" @click="isShowCreate = false">取消</a>
				</td>
			</tr>
			
		</table>
	</div>	

		<script>
		    let groups = ${groups};
		    let mapList = ${mapList};
		</script>
		<script>
		new Vue({
            el: '#vue',
            data: {
                groups: groups,
                mapList: mapList,
                create: {
                    name: '',
                    menu_id: 0,
                    group_id: 0,
                    task_type_id: 0
                },
                isShowCreate: false,
                editingId: 0
            },
            methods: {
                doCreate() {
                    let url = "../admin/common/menuGroup/create";

                    form("POST", url, this.create, (json) => {
                        console.log(json);
                        if (json && json.status == 1) {
                            alert('创建成功');
                            location.reload();
                        }

                    }, {
                        header: {
                            Authorization: 'Bearer ' + accessToken
                        }
                    });
                },
                del(id) {
                    if (confirm('确定删除？')) {
                        let url = "../admin/common/menuGroup/delete/" + id;
                        aj.xhr.postForm(url, {}, (json) => {
                            if (json && json.status == 1) {
                                alert('删除成功');
                                location.reload();
                            }
                        }, {
                            header: {
                                Authorization: 'Bearer ' + accessToken
                            }
                        });
                    }
                },
                save(entity) {
                    console.log(entity)
                    // debugger

                    let url = "../admin/common/menuGroup/update";
                    let e = convertKeysToUnderscore(entity);
                    e.group_id = e.resoure_group_id;
                    delete e.resoure_group_id;

                    aj.xhr.postForm(url, e, (json) => {
                        console.log(json);
                        if (json && json.status == 1) {
                            alert('修改成功');
                            location.reload();
                        }

                    }, {
                        header: {
                            Authorization: 'Bearer ' + accessToken
                        }
                    });
                }
            }
        });
		</script>
</body>

</html>