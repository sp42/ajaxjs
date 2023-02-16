<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<%@include file="/common/head.jsp"%>
<script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.13/vue.js"></script>

<style>
body {
	padding: 20px 40px;
}

h1 {
	font-weight: bold;
	font-size: 18pt;
	text-align: left;
	letter-spacing: 2px;
	margin: 20px 0;
}

.tree-like select {
	width: 100%;
	height: 500px;
	border: 0;
}

.tree-like.main-panel {
	height: 500px;
}

.toolbar {
	margin: 30px 0;
	font-size: 10pt;
}

.toolbar li {
	padding: 10px;
}

.toolbar li button {
	margin-left: 20px;
}

.main-panel {
	width: 70%;
	float: left;
	margin-top: 30px;
}

.main-panel>div {
	text-align: center;
	margin: 20px;
}

.main-panel select {
	border: 1px solid lightgray;
}
</style>
</head>
<body>
	<div class="tree-like">
		<h1>组织管理</h1>
		<div class="note">你可以在这里添加、修改、删除组织。请于分类中点选目标的节点，成为选中的状态后，再进行下面的编辑。</div>

		<div class="main-panel">
			<select multiple @change="onChange"></select>

			<div class="note" v-show="selectedId != 0">已选择#
				{{selectedId}}-{{selectedName}}</div>
		</div>

		<!-- 菜单工具栏-->
		<ul class="toolbar">
			<li>
				<button>
					<i class="fa fa-plus" style="color: #ffaf0a;"></i> 新增顶级${uiName}
				</button>
			</li>
			<li>
				<!-- <input type="text" name="name" required size="12" :class="{'aj-disable': selectedId == 0}" /> -->
				<button :class="{'aj-disable': selectedId == 0}">
					<i class="fa fa-plus" style="color: #ffaf0a;"></i> 新建子分类
				</button>
			</li>
			<li>
				<button @click="rename">
					<i class="fa fa-pencil-square" style="color: #0a90f0;"></i> 修改分类名称
				</button>
			</li>
			<li>
				<button @click="dele">
					<i class="fa fa-trash" style="color: red;"></i> 删除
				</button>
			</li>
		</ul>
	</div>

	<script>
		function createTopNode(){
			jsp.xhr.postForm('.', {name: 'xx', pid: -1});
		}
		function createUnderNode(){
			jsp.xhr.postForm('.', {name: 'xx', pid: selectedId});
		}
	
	    var stack = [];
	    
	    function output(map, cb) {
	    	stack.push(map);
	    	for ( var i in map) {
	    		map[i].level = stack.length;
	    		cb(map[i], i);
	    		
	    		var c = map[i].children;
	    		if (c) {
	    			for (var q = 0, p = c.length; q < p; q++) 
	    				output(c[q], cb);
	    		}
	    	}
	    	stack.pop();
	    }
	    
		new Vue({
		    el: '.tree-like',
		    data: {
		        selectedId: 0,
		        selectedName: ''
		    },
		    mounted() {
		        // 新增顶级${uiName}
		    /*     jsp.xhr.form(".createTopNode", json => {
		            document.querySelector(".createTopNode input[name=name]").value = '';
		            this.refresh(json);
		        });
	
		        // 在${uiName}下添加子${uiName}
		        jsp.xhr.form(".createUnderNode", json => {
		            document.querySelector(".createUnderNode input[name=name]").value = '';
		            this.refresh(json);
		        });
	
		        // 修改名称
		        jsp.xhr.form(this.$el.querySelector('.rename'), json => {
		            this.$refs.layer.close();
		            this.refresh(json);
		        }); */
	
		        this.render();
		    },
		    methods: {
		        onChange(ev) {
		            let selectEl = ev.target,
		                option = selectEl.selectedOptions[0],
		                id = option.value, 
		                pid = option.dataset['pid'],
		                name = option.innerHTML.replace(/&nbsp;|└─/g, '');
	
		            this.selectedId = id;
		            this.selectedName = name;
		        },
		        rename() {
		            if (!this.selectedId) {
		                alert('未选择任何分类');
		                return;
		            }
	
		        	var name = prompt('更新名称', selectedName);
		        	if (name != null) {
		        		jsp.xhr.put(selectedId + 'selectedId/', {name: selectedName}, j=> {});
		        	}
		        },
		        // 删除
		        dele() {
		            if (!this.selectedId) {
		                alert('未选择任何分类');
		                return;
		            }
	
		            let msg = '确定删除该${uiName}[{0}]？\n[{0}]下所有的子节点也会随着一并全部删除。'.replace(/\{0\}/g, this.selectedName);
		            if(confirm(msg)){
		            	 aj.xhr.dele("" + this.selectedId + "/", this.refresh);
		            }
		        },
	
		        refresh(json) {
		            if (json.isOk) {
		                alert(json.msg);
		                this.render();
		            } else
		                alert(json.msg);
		        },
	
		        render() {
		            let select = this.$el.querySelector('select');
		            select.innerHTML = '';
		            //jsp.xhr.get('.', j => rendererOption(j.result, select));
		            
		            
		            var result = [
		            	{
		            		id:1,
		            		name:'dfdf', 
		            		pid: -1, 
		            		//level: 1
		            		
		            	},
		            	{		            			
                			id:2,
                			name: 'dfdf22', 
                			//level: 2,
	            			pid:  1
	            		}
		            ];
		            rendererOption(result, select);
		        }
		    }
		});
		   
		/**
	     * 根据传入 id 查找父亲节点
	     * 
	     * @param map 
	     * @param id 
	     */
	     function findParentInMap(map, id) {
	        for (let i in map) {
	            if (i == id)
	                return map[i];

	            let c = map[i].children;

	            if (c) {
	                for (let q = 0, p = c.length; q < p; q++) {
	                    let result = findParentInMap(c[q], id);

	                    if (result != null)
	                        return result;
	                }
	            }
	        }

	        return null;
	    }		

	    /**
	     * 生成树，将扁平化的数组结构 还原为树状的结构
	     * 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故前提条件是，这个数组必须先排序
	     * 
	     * @param jsonArray 
	     */
	    function toTree(jsonArray) {
	        if (!jsonArray)
	            return {};

	        let m = {};

	        for (var i = 0, j = jsonArray.length; i < j; i++) {
	            let n = jsonArray[i], parentNode = findParentInMap(m, n.pid + "");

	            if (parentNode == null) {	// 没有父节点，那就表示这是根节点，保存之
	                m[n.id] = n;			// id 是key，value 新建一对象
	            } else { 					// 有父亲节点，作为孩子节点保存
	                let obj = {};
	                obj[n.id] = n;

	                if (!parentNode.children)
	                    parentNode.children = [];

	                parentNode.children.push(obj);
	            }
	        }

	        return m;
	    }

	    /**
	     * 渲染 Option 标签的 DOM
	     * 
	     * @param jsonArray 
	     * @param select 
	     * @param selectedId 
	     * @param cfg 
	     */
	     function rendererOption(jsonArray, select, selectedId, cfg) {
	        if (cfg && cfg.makeAllOption) {
	            let option = document.createElement('option');
	            option.value = option.innerHTML = "全部分类";
	            select.appendChild(option);
	        }

	        // 生成 option
	        let temp = document.createDocumentFragment();

	        output(toTree(jsonArray), (node, nodeId) => {
	            let option = document.createElement('option'); // 节点
	            option.value = nodeId;

	            if (selectedId && selectedId == nodeId) // 选中的
	                option.selected = true;

	            option.dataset['pid'] = node.pid + "";
	            //option.style= "padding-left:" + (node.level - 1) +"rem;";
	            option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
	            temp.appendChild(option);
	        });

	        select.appendChild(temp);
	    }
	    

	</script>
</body>
</html>