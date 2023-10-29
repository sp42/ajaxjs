<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common.jsp" %>
</head>

<body class="inner-page">
	<h2>席位与对象类型关系</h2>

	<div id="vue">
		<div class="btns" align="right">
			<a href="#" @click="isShowCreate = true"><span style="color:green">➕</span> 添加</a>
		</div>
		<table class="list-table">
			<thead>
				<tr>
					<th>id</th><th>任务/席位类型</th><th>对象类型（对应 ObjectType）</th><th>操作</th>
				</tr>
			</thead>
			<tr v-for="(item) in mapList">
				<td>{{item.id}}</td>
				<td>
                     {{TASK_TYPE[item.taskTypeId]}}{{'#'+item.taskTypeId}}
				</td>
				<td style="max-width:1000px;" width="850">
				 <!-- {{MAP[item.taskTypeId]}} -->
                    <ul class="item">
                        <li v-for="(oType) in objectType">
                            <label>
                                <input type="checkbox" v-model="MAP[item.taskTypeId]" :value="oType.objectTypeId" /> {{oType.objTypeName}}{{'#'+oType.objectTypeId}}
                           </label>
                        </li>
                    </ul>
				</td>
				<td>
					<a href="#" @click="save(item.taskTypeId)"><span style="color:green">✔</span> 保存</a>
				</td>
			</tr>
			<tr :class="{hide: !isShowCreate}">
				<td></td>
                <td>
                    <select v-model="create.taskTypeId">
                        <option v-for="(k, v) in TASK_TYPE" :value="v">
                            {{k}}-{{v}}
                        </option>
                    </select>
                </td>
                <td>
                    <ul class="item">
                        <li v-for="(oType) in objectType">
                            <label>
                                <input type="checkbox" v-model="create.objectTypeIds" :value="oType.objectTypeId" /> {{oType.objTypeName}}{{'#'+oType.objectTypeId}}
                           </label>
                        </li>
                    </ul>
                </td>
				<td>
					<a href="#" @click="doCreate">新增</a> | <a href="#" @click="isShowCreate = false">取消</a>
				</td>
			</tr>

		</table>
	</div>

    <script>
let MAP = {};

new Vue({
    el: '#vue',
    data: {
        mapList: [],
        create: {
            taskTypeId: 0,
            objectTypeIds: []
        },
        isShowCreate: false,
        editingId: 0,
        objectType: [{ objectTypeId: 232 }]
    },
    mounted() {
        aj.xhr.get('../admin/common/objectType/list', json => {
            this.objectType = json.data;

            aj.xhr.get('../admin/task_type_obj_type/list', json => {
                this.mapList = json.data;

                json.data.forEach(item => {
                    MAP[item.taskTypeId] = item.objTypeIds.split(",").map(Number);
                });
            }, {
                header: {
                    Authorization: 'Bearer ' + accessToken
                }
            });
        }, {
            header: {
                Authorization: 'Bearer ' + accessToken
            }
        });
    },
    methods: {
        doCreate() {
            let url = '../admin/task_type_obj_type/save?taskTypeId=' + this.create.taskTypeId + '&objTypeIds=' + this.create.objectTypeIds;

            aj.xhr.postForm(url, {}, (json) => {
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
                let url = "../admin/common/taskTypeObjType/delete/" + id;
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
        save(taskTypeId) {
            let url = `../admin/task_type_obj_type/save?taskTypeId=${taskTypeId}&objTypeIds=` + MAP[taskTypeId];

            aj.xhr.postForm(url, {}, (json) => {
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