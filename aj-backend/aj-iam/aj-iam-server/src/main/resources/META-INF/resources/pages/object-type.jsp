<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common.jsp" %>
</head>

<body class="inner-page">
	<h2>对象类型管理</h2>

	<div id="vue">
		<div class="btns" align="right">
			<a href="#" @click="isShowCreate = true"><span style="color:green">➕</span> 添加</a>
		</div>
		<table class="list-table">
			<thead>
				<tr>
					<th>id</th><th>对象类型名称</th><th>大类 Group</th><th>小类 Type</th><th>所属战斗方</th><th>对象类型图标</th><th>对象类型预设数量</th><th>操作</th>
				</tr>
			</thead>
			<tr v-for="(item) in mapList">
				<td>{{item.objectTypeId}}</td>
				<td>
				    <span v-show="editingId != item.objectTypeId">{{item.objTypeName}}</span>
				    <input type="text" v-show="editingId == item.objectTypeId" v-model="item.objTypeName" />
				</td>
				<td>
                    <select v-model="item.objGroup">
                        <option v-for="(k, v) in OBJECT_GROUP" :value="v">
                            {{k}}-{{v}}
                        </option>
                    </select>
				</td>
				<td>
                    <select v-model="item.objType">
                        <option v-for="(k, v) in OBJECT_TYPE" :value="v">
                            {{k}}-{{v}}
                        </option>
                    </select>
				</td>
				<td>
				    <select v-model="item.combatParty" style="width:80px">
                        <option value="1">红方-1</option>
                        <option value="2">蓝方-2</option>
                        <option value="3">中立（如红十字会医院等）-3</option>
                        <option value="4">情报（针对拍摄的图片）-4</option>
                    </select>
				</td>
				<td>
				    <span v-show="editingId != item.objectTypeId">{{item.objTypeImg}}</span>
				    <input type="text" v-show="editingId == item.objectTypeId" v-model="item.objTypeImg" style="width:150px" />
				</td>
				<td>
				    <span v-show="editingId != item.objectTypeId">{{item.objTypeNum}}</span>
				    <input type="number" v-show="editingId == item.objectTypeId" v-model="item.objTypeNum" style="width:50px" />
				</td>
				<td>
					<a href="#" @click="del(item.objectTypeId)" v-show="editingId != item.objectTypeId"><span style="color:red">✖</span> 删除</a>
					<a href="#" @click="save(item)" v-show="editingId == item.objectTypeId"><span style="color:green">✔</span> 保存</a>
					|
					<a href="#" @click="editingId = item.objectTypeId" v-show="editingId != item.objectTypeId">编辑</a>
					<a href="#" @click="editingId = 0" v-show="editingId == item.objectTypeId"> 取消</a>
				</td>
			</tr>
			<tr :class="{hide: !isShowCreate}">
				<td></td>
				<td><input type="input" placeholder="对象类型名称" v-model="create.objTypeName" /></td>
				<td>
                    <select v-model="create.objGroup">
                        <option v-for="(k, v) in OBJECT_GROUP" :value="v">
                            {{k}}-{{v}}
                        </option>
                    </select>
				</td>
				<td>
                    <select v-model="create.objType">
                        <option v-for="(k, v) in OBJECT_TYPE" :value="v">
                            {{k}}-{{v}}
                        </option>
                    </select>
				</td>
				<td>
				    <select v-model="create.combatParty" style="width:80px">
                        <option value="1">红方-1</option>
                        <option value="2">蓝方-2</option>
                        <option value="3">中立（如红十字会医院等）-3</option>
                        <option value="4">情报（针对拍摄的图片）-4</option>
                    </select>
				</td>
				<td>
				    <input type="text" v-model="create.objTypeImg" placeholder="对象类型图标" style="width:150px" />
				</td>
				<td>
				    <input type="number" v-model="create.objTypeNum" placeholder="对象类型预设数量" style="width:50px" />
				</td>
				<td>
					<a href="#" @click="doCreate">新增</a> | <a href="#" @click="isShowCreate = false">取消</a>
				</td>
			</tr>

		</table>
	</div>

    <script>
    new Vue({
        el: '#vue',
        data: {
            mapList: [],
            create: {},
            isShowCreate: false,
            editingId: 0
        },
        mounted() {
            aj.xhr.get('../admin/common/objectType/list', json => {
                this.mapList = json.data;
            }, {
                header: {
                    Authorization: 'Bearer ' + accessToken
                }
            });
        },
        methods: {
            doCreate() {
                let url = "../admin/common/objectType/create";

                form("POST", url, convertKeysToUnderscore(this.create), (json) => {
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
                    let url = "../admin/common/objectType/delete/" + id;
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
                let url = "../admin/common/objectType/update";
                let _e = convertKeysToUnderscore(entity);
                let e = {
                    object_type_id: _e.object_type_id,
                    combat_party: _e.combat_party,
                    obj_type_name: _e.obj_type_name,
                    obj_group: _e.obj_group,
                    obj_type: _e.obj_type,
                    obj_type_img: _e.obj_type_img,
                    obj_type_num: _e.obj_type_num
                };

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