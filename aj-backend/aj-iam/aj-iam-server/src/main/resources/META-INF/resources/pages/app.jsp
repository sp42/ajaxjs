<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common.jsp" %>
</head>

<body class="inner-page">
	<h2>应用管理</h2>

	<div id="vue">
		<div class="btns" align="right">
			<a href="#" @click="isShowCreate = true"><span style="color:green">➕</span> 添加</a>
		</div>
		<table class="list-table">
			<thead>
				<tr>
					<th>id</th><th>应用名称</th><th>应用简介</th><th>客户端 id</th><th>客户端秘钥</th><th>应用类型</th><th>图标</th><th>操作</th>
				</tr>
			</thead>
			<tr v-for="(item) in mapList">
				<td>{{item.id}}</td>
				<td>
				    <span v-show="editingId != item.id">{{item.name}}</span>
				    <input type="text" v-show="editingId == item.id" v-model="item.name" />
				</td>
				<td>
				    <span v-show="editingId != item.id">{{item.content}}</span>
				    <input type="text" v-show="editingId == item.id" v-model="item.content" />
				</td>
                <td>
                    <span v-show="editingId != item.id">{{item.clientId}}</span>
                    <input type="text" v-show="editingId == item.id" v-model="item.clientId" />
                </td>
				<td>
				    <span v-show="editingId != item.id">{{item.clientSecret}}</span>
				    <input type="text" v-show="editingId == item.id" v-model="item.clientSecret" />
				</td>

				<td>
				    <select v-model="item.type" style="width:80px">
                        <option value="HTML">Web/H5 页面</option>
                        <option value="APP">安卓/iOS/小程序</option>
                        <option value="API_SERVICE">REST API</option>
                        <option value="RPC_SERVICE">RPC API</option>
                        <option value="MISC">其他</option>
                    </select>
				</td>

				<td>
				    <img :src="item.logo" />
				</td>
				<td>
				    <a href="#" @click="editingId = 0"> 查看 Token</a> |
					<a href="#" @click="del(item.id)" v-show="editingId != item.id"><span style="color:red">✖</span> 删除</a>
					<a href="#" @click="save(item)" v-show="editingId == item.id"><span style="color:green">✔</span> 保存</a>
					|
					<a href="#" @click="editingId = item.id" v-show="editingId != item.id">编辑</a>
					<a href="#" @click="editingId = 0" v-show="editingId == item.id"> 取消</a>
				</td>
			</tr>
			<tr :class="{hide: !isShowCreate}">
				<td></td>
				<td><input type="text" v-model="create.name" /></td>
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
    const apiPrefix = '../admin_api/common/app';

    new Vue({
        el: '#vue',
        data: {
            mapList: [],
            create: {},
            isShowCreate: false,
            editingId: 0
        },
        mounted() {
            aj.xhr.get(apiPrefix + '/list', json => {
                this.mapList = json.data;
            }, {
                header: {
                    Authorization: 'Bearer ' + accessToken
                }
            });
        },
        methods: {
            doCreate() {
                let url = "../admin_api/common/objectType/create";

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
                    let url = "../admin_api/common/objectType/delete/" + id;
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
                let url = "../admin_api/common/objectType/update";
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