<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.toway.cc2.service.AdminService" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common.jsp" %>
	<script>
	    ET_TYPE = {0:'仿真模式',1:'作战模式'};
	</script>
</head>
<body class="inner-page">
	<h2>演训任务</h2>

	<div id="vue">
		<div class="btns" align="right">
			<a href="#" @click="isShowCreate = true"><span style="color:green">➕</span> 添加</a>
		</div>
		<table class="list-table">
			<thead>
				<tr>
					<th>id</th><th>演训名称</th><th>演训类型</th><th>T时刻</th><th>结束时间</th><th>状态</th><th>创建时间</th><th>操作</th>
				</tr>
			</thead>
			<tr v-for="(item) in mapList">
				<td>{{item.etId}}</td>
				<td>
				    <span v-show="editingId != item.etId">{{item.etName}}</span>
				    <input type="text" v-show="editingId == item.etId" v-model="item.etName" />
				</td>
				<td>
				    <span v-show="editingId != item.etId">{{ET_TYPE[item.etType]}}</span>
				    <span v-show="editingId == item.etId">
				        <label>
				            <input type="radio" value="0" v-model="item.etType" /> 仿真模式
				        </label>

				        <label>
				            <input type="radio" value="1" v-model="item.etType" /> 作战模式
				        </label>
				    </span>
				</td>
				<td>
                    <span v-show="editingId != item.etId">{{item.tTime}}</span>
                    <input type="text" v-show="editingId == item.etId" v-model="item.tTime" />
				</td>
				<td>
                    <span v-show="editingId != item.etId">{{item.endTime}}</span>
                    <input type="text" v-show="editingId == item.etId" v-model="item.endTime" />
				</td>
				<td>
				    <span v-show="editingId != item.etId">{{{1:'进行中',2:'已结束'}[item.status]}}-{{item.status}}</span>
                    <span v-show="editingId == item.etId">
                        <label>
                            <input type="radio" value="1" v-model="item.status" /> 进行中
                        </label>

                        <label>
                            <input type="radio" value="2" v-model="item.status" /> 已结束
                        </label>
                    </span>
				</td>
				<td>{{item.createdOn}}</td>
				<td>
					<a href="#" @click="del(item.etId)" v-show="editingId != item.etId"><span style="color:red">✖</span> 删除</a>
					<a href="#" @click="save(item)" v-show="editingId == item.etId"><span style="color:green">✔</span> 保存</a>
					|
					<a href="#" @click="editingId = item.etId" v-show="editingId != item.etId">编辑</a>
					<a href="#" @click="editingId = 0" v-show="editingId == item.etId"> 取消</a>
				</td>
			</tr>
			<tr :class="{hide: !isShowCreate}">
				<td></td>
                <td>
                    <input type="text" v-model="create.etName" />
                </td>
                <td>
                    <label>
                        <input type="radio" value="0" v-model="create.etType" /> 仿真模式
                    </label>

                    <label>
                        <input type="radio" value="1" v-model="create.etType" /> 作战模式
                    </label>
                </td>
                <td>

                </td>
                <td>

                </td>
                <td>
                    <label>
                        <input type="radio" value="1" v-model="create.status" /> 进行中
                    </label>

                    <label>
                        <input type="radio" value="2" v-model="create.status" /> 已结束
                    </label>
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
        aj.xhr.get('../admin/common/combat/list', json => {
            this.mapList = json.data;
        }, {
            header: {
                Authorization: 'Bearer ' + accessToken
            }
        });
    },
    methods: {
        doCreate() {
            let url = "../admin/common/combat/create";

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
                let url = "../admin/common/combat/delete/" + id;
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
            let url = "../admin/common/combat/update";
            let _e = convertKeysToUnderscore(entity);
            let e = {
                et_id: _e.et_id,
                et_type: _e.et_type,
                status: _e.status
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