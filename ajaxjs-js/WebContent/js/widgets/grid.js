/**
 * 显示头像
 */
Vue.component('aj-avatar', {
	template: '	a :href="avatar" target="_blank">\
		<img :src="avatar" style="max-width:50px;max-height:60px;vertical-align: middle;" \
	 		@mouseenter="mouseEnter" onmouseleave="aj.widget.imageEnlarger.singleInstance.imgUrl = null;" />\
	</a>',
	props: {
		avatar: {type: String, required: true}
	},
	methods: {
		mouseEnter() {
			aj.widget.imageEnlarger.singleInstance.imgUrl = this.avatar;
		}
	}
});

/**
 * 动态组件，可否换为  component？
 */
Vue.component('aj-cell-renderer', {
    props: {
        html: {type: String, default: ''},
        form: Object,
    },
    render(h) {
		if(this.html.indexOf('aj')!= -1){
	        var com = Vue.extend({
	            template: this.html,
	            props: {
	                form: Object,
	            }
	        });
	
	        return h(com, {
	            props: {
	                form: this.form
	            }
	        });
		} else {
			return this._v(this.html); // html
		}
    }
});

/**
	新建、编辑都同一表单
 */
Vue.component('aj-edit-form', {
	beforeCreate() {	
		aj.getTemplate('grid', 'aj-edit-form', this);
	},
	props: {
		initIsCreate: { // 是否新建模式
			type: Boolean, default: true
		},
		uiName: String,
		id: String,
		getInfoApi: {// 获取实体详情的接口地址 
			type: String, required: true
		}
	},
	data(){
		return {
			isCreate: this.initIsCreate,
			info: {}
		}
	},
	mounted() {		
		aj.xhr.form(this.$el, j => {
			if(j) {
				if(j.isOk) {
					var msg = (this.isCreate ? "新建":"保存") + this.uiName + "成功";
					aj.alert.show(msg);
					
					this.isCreate = false; // 切换编辑模式
				} else 
					aj.alert.show(j.msg);
			}
		}, {
			beforeSubmit(form, json) {
				//json.content = App.$refs.htmleditor.getValue({cleanWord : eval('${aj_allConfig.article.cleanWordTag}'), encode : true});
			}
		});
	},
	methods: {
		load(id) { // 加载数据，获取实体详情
			this.isCreate = false;
			
			aj.xhr.get(this.$el.action + id + "/", j => {
				this.info = j.result;
			});
		},
		close(){
			if(this.$parent.$options._componentTag === 'aj-layer') {
				this.$parent.close();
			} else 
				history.back();
		},
		del() {
			var id = this.$el.$('input[name=id]').value, title = this.$el.$('input[name=name]').value;
			
			aj.showConfirm('请确定删除记录：\n' + title + ' ？', () => 
				aj.xhr.dele('../' + id + '/', j => {
					if (j.isOk) {
						aj.msg.show('删除成功！');
						//setTimeout(() => location.reload(), 1500);
					} else 
						aj.alert('删除失败！');
				})
			);
		}
	}
});

Vue.component('aj-entity-toolbar', {
	beforeCreate() {	
		aj.getTemplate('grid', 'aj-entity-toolbar', this);
	},
	props: {
		betweenDate: {type: Boolean, default: true},
		create: {type: Boolean, default: true},
		save: {type: Boolean, default: true},
		excel: {type: Boolean, default: false},
		deleBtn: {type: Boolean, default: true},
		search: {type: Boolean, default: true},
	},
	methods: {
		valid(e) {
			var start = this.$el.$('input[name=startDate]').value, end = this.$el.$('input[name=endDate]').value;
			
			if(!start||!end) {
				aj.showOk("输入数据不能为空");					
				e.preventDefault();
			}
			
			if(new Date(start) > new Date(end)) {
				aj.showOk("起始日期不能晚于结束日期");					
				e.preventDefault();
			}
		},
		doSearch(e) {
			e.preventDefault();
			aj.apply(this.$parent.$refs.pager.baseParam, {
				keyword: this.$el.$('input[name=keyword]').value
			});
			this.$parent.reload();
		}
		
	}
});

/**
	总体就是一个普通的 HTML Table，程序是通过读取 JSON 数据（实际 JS 数组）里面的字段，
	然后通过动态添加表格的方法逐行添加 tr，td，在添加行的过程中根据需要设置样式，添加方法等。
	每一行数据（即 tr 的 DOM 对象）都有 id 属性，其值就等于数据中的idColumn对应的列的值
 */
Vue.component('aj-grid-inline-edit-row', {
	beforeCreate() {	
		aj.getTemplate('grid', 'aj-grid-inline-edit-row', this);
	},
	props: {
		rowData: { // 输入的数据
			type: Object,
			required: true,
		},
		showIdCol: { // 是否显示 id 列
			type: Boolean,
			default: true
		},
		columns: Array, // 列
		showCheckboxCol: { // 是否显示 selectCheckbox 列
			type: Boolean,
			default: true
		},
		filterField: Array, // 不要显示的字段,
		enableInlineEdit: { // 是否可以 inline-edit
			type: Boolean,
			default: false
		},
		deleApi: String // 删除路径
	},
	data() {
     	return {
			id: this.rowData.id,
     		data: this.rowData,
			isEditMode: false
		};
	},
	mounted() {
		for(var i in this.data) { // 监视每个字段
			this.$watch('data.' + i, this.makeWatch(i));
		}
	},
	computed:{
    	filterData() {// dep
			var data = JSON.parse(JSON.stringify(this.data));// 剔除不要的字段
			delete data.id;
			delete data.dirty;
			
			if(this.filterField && this.filterField.length) {
				this.filterField.forEach(i => delete data[i]);
			}
			
	        return data;
	    },

		styleModifly() {
			return {
				padding:  this.isEditMode ? 0 : '',
				//fontSize: this.isEditMode ? 0 : ''
			};
		}
	},
	methods: {
		isFixedField(field) {
			if(this.filterField && this.filterField.length) {
				for(var i = 0, j = this.filterField.length; i < j; i++) {
					if(this.filterField[i] == field)
						return true;
				}
			}
			
			return false;
		},
		canEdit(key) {		
			return !this.isFixedField(key) && this.isEditMode;
		},
		renderCell(data, key) {
			if(typeof key == 'function') 
				return key(data);	
			
			if(key === '')
				return '';
				
			var v = data[key];
			return (v === null ? '' : v) + '';
		},
		// 编辑按钮事件
		onEditClk() {
			if(this.enableInlineEdit)
				this.isEditMode = !this.isEditMode;
			else
				this.$parent.onEditClk(this.id);
		},
		makeWatch(field) {
			return function(_new) {
				var arr = this.$parent.list;
				
				var data;
				for(var i = 0, j = arr.length; i < j; i++) {
					if(this.id && (arr[i].id === this.id)) {
						data = arr[i];
						break;
					}
				}
				
				if(!data)
					throw '找不到匹配的实体！目标 id: ' + this.id;
				
				if(!data.dirty)
					data.dirty = {};
				
				data.dirty[field] = _new; // 保存新的值，key 是字段名
			}
		},
		dbEdit($event){
			if(!this.enableInlineEdit)
				return;
			
			this.isEditMode = !this.isEditMode;
			
			if(this.isEditMode){
				var el = $event.target;
					
				setTimeout(()=> {
					if(el.tagName !== 'INPUT')
						el = el.$('input');

					el && el.focus();
				}, 200);
			}
		},
		selectCheckboxChange($event){
			var checkbox = $event.target;
			var parent = this.$parent;
			
			if(checkbox.checked)
				parent.$set(parent.selected, this.id, true);
				//this.$parent.selected[this.id] = true;
			else
				parent.$set(parent.selected, this.id, false);
		},
		// 删除记录
		dele(id){
			aj.showConfirm('确定删除记录id:[' + id + ']', ()=>
				aj.xhr.dele(this.deleApi + '/' + id + '/', j => {
					if(j.isOk) {
						aj.msg.show('删除成功');
						this.$parent.reload();
					}
				})
			);
		}
	}
});

Vue.component('aj-grid-inline-edit-row-create', {
	beforeCreate() {	
		aj.getTemplate('grid', 'aj-grid-inline-edit-row-create', this);
	},
	props: {
		columns: {
			type: Array, required: true
		},
		createApi: {
			type: String, required: true
		}
	},
	methods: {
		// 编辑按钮事件
		addNew() {
			var map = {};
			this.$el.$('input[name]', i => {
				map[i.name] = i.value;
			});
			
			aj.xhr.post(this.createApi, j => {
				if(j && j.isOk) {
					aj.msg.show('新建实体成功');
					
					this.$el.$('input[name]', i => { // clear
						i.value = '';
					});
					
					this.$parent.reload();
					this.$parent.showAddNew = false;
				}
			}, map);
		},
	
		dbEdit($event){
			this.isEditMode = !this.isEditMode;
			var el = $event.target;
			
			if(el.tagName !== 'INPUT')
				el = el.$('input');
				
			setTimeout(()=> el && el.focus(), 200)
		}
	}
});

aj.Grid = {};

aj.Grid.common = {
	data(){			
		return {
			list: [],
			updateApi: null,
			showAddNew: false
		};
	},
	mounted() { // 加载数据
		this.$refs.pager.$on("pager-result", result => {
			this.list = result;
			this.maxRows = result.length;
		});
		
		this.$refs.pager.autoLoad && this.$refs.pager.get();
	},
	methods: {
		// 按下【新建】按钮时候触发的事件，你可以覆盖这个方法提供新的事件
		onCreateClk(){
			this.showAddNew = true; 
		},
		getDirty() {
			var dirties = [];
			this.list.forEach(item => {
				if(item.dirty) {
					item.dirty.id = item.id;
					dirties.push(item);
				}
			});
			
			return dirties;
		},
		reload(){
			this.$refs.pager.get();
		},
		onDirtySaveClk() {
			var dirties = this.getDirty();
			
			if(!dirties.length) {
				aj.msg.show('没有修改过的记录');
				return;
			}
			
			dirties.forEach(item => {
				aj.xhr.put(this.updateApi + '/' + item.id + '/', j => {
					if(j.isOk) {
 		 				this.list.forEach(item => { // clear
			 				if(item.dirty) 
			 					delete item.dirty;
			 			});			
								
						aj.msg.show('修改记录成功');
					}
				}, item.dirty);
			});
		}
	}
};

aj.SectionModel = {
	data(){
		return {
			isSelectAll : false,// TODO 顶部是否全选的状态
			selected: {},		// 选择的行
			selectedTotal: 0,	// 选中了多少？
			maxRows: 0			// 最多的行数，用于判断是否全选
		}
	},
	methods:{
		// 全选
		selectAll() {
			var checkAll = item => {
				item.checked = true;
				var id = item.dataset.id;
				
				if(!id)
					throw '需要提供 id 在 DOM 属性中';
				
				this.$set(this.selected, Number(id), true);
			}, diskCheckAll = item => {
				item.checked = false;
				var id = item.dataset.id;
				
				if(!id)
					throw '需要提供 id 在 DOM 属性中';
				
				this.$set(this.selected, Number(id), false);
			}
			
			this.$el.$('table .selectCheckbox input[type=checkbox]', this.selectedTotal === this.maxRows ? diskCheckAll : checkAll);
		}
		 
	},
	watch: {
		selected: {
			handler(n) {
				var j = 0;
				
				// clear falses
				for(var i in this.selected) {
					if(this.selected[i] === false)
						delete this.selected[i];
					else 
						j++;
				}
				
				this.selectedTotal = j;
				
				if(j === this.maxRows)
					this.$el.$('.top-checkbox').checked = true;
				else
					this.$el.$('.top-checkbox').checked = false;
			},
			deep: true
		}
	}
};