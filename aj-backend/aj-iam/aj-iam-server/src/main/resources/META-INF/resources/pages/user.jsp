<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.toway.cc2.service.AdminService" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common.jsp" %>
</head>
<body class="inner-page">
	<h2>用户管理</h2>

	<div id="vue">
		<div class="btns" align="right">
			<a href="#" @click="isShowCreate = true"><span style="color:green">➕</span> 添加</a>
		</div>
		<table class="list-table">
			<thead>
				<tr>
					<th>id</th><th>账号</th><th>用户名</th><th>代号</th><th>密码</th><th>头像</th><th>创建时间</th><th>操作</th>
				</tr>
			</thead>
			<tr v-for="(item) in mapList">
				<td>{{item.userId}}</td>
				<td>
				    <span v-show="editingId != item.userId">{{item.loginId}}</span>
				    <input type="text" v-show="editingId == item.userId" v-model="item.loginId" />
				</td>
				<td>
				    <span v-show="editingId != item.userId">{{item.userName}}</span>
				    <input type="text" v-show="editingId == item.userId" v-model="item.userName" />
				</td>
				<td>
                    <span v-show="editingId != item.userId">{{item.userSymbol}}</span>
                    <input type="text" v-show="editingId == item.userId" v-model="item.userSymbol" />
				</td>
				<td>
                    <span v-show="editingId != item.userId">{{item.password}}</span>
                    <input type="text" v-show="editingId == item.userId" v-model="item.password" />
				</td>
				<td>
				    <span v-show="editingId != item.userId">{{item.head}}</span>
				    <input type="text" v-show="editingId == item.userId" v-model="item.head" style="width:150px" />
				</td>
				<td>{{item.createdOn}}</td>
				<td>
					<a href="#" @click="del(item.userId)" v-show="editingId != item.userId"><span style="color:red">✖</span> 删除</a>
					<a href="#" @click="save(item)" v-show="editingId == item.userId"><span style="color:green">✔</span> 保存</a>
					|
					<a href="#" @click="editingId = item.userId" v-show="editingId != item.userId">编辑</a>
					<a href="#" @click="editingId = 0" v-show="editingId == item.userId"> 取消</a>
				</td>
			</tr>
			<tr :class="{hide: !isShowCreate}">
				<td></td>
                <td>
                    <input type="text" v-model="create.loginId" />
                </td>
                <td>
                    <input type="text" v-model="create.userName" />
                </td>
                <td>
                    <input type="text" v-model="create.userSymbol" />
                </td>
                <td>
                    <input type="text" v-model="create.password" />
                </td>
                <td>
                    <input type="text" v-model="create.head" style="width:150px" />
                </td>
                <td></td>
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
        aj.xhr.get('../admin/common/user/list', json => {
            this.mapList = json.data;
        }, {
            header: {
                Authorization: 'Bearer ' + accessToken
            }
        });
    },
    methods: {
        doCreate() {
            let url = "../admin/common/user/create";

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
                let url = "../admin/common/user/delete/" + id;
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
            let url = "../admin/common/user/update";
            let _e = convertKeysToUnderscore(entity);
            let e = {
                user_id: _e.user_id,
                user_name: _e.user_name,
                user_symbol: _e.user_symbol,
                password: _e.password,
                head: _e.head
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