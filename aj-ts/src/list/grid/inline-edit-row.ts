/**
    总体就是一个普通的 HTML Table，程序是通过读取 JSON 数据（实际 JS 数组）里面的字段，
    然后通过动态添加表格的方法逐行添加 tr，td，在添加行的过程中根据需要设置样式，添加方法等。
    每一行数据（即 tr 的 DOM 对象）都有 id 属性，其值就等于数据中的idColumn对应的列的值
 */
Vue.component('aj-grid-inline-edit-row', {
    template: `
        <tr class="aj-grid-inline-edit-row" :class="{editing: isEditMode}">
            <td v-if="showCheckboxCol" class="selectCheckbox">
                <input type="checkbox" @change="selectCheckboxChange" :data-id="id" />
            </td>
            <td v-if="showIdCol">{{id}}</td>
            <td v-for="key in columns" :style="styleModifly" class="cell" @dblclick="dbEdit">
                <aj-cell-renderer v-if="!isEditMode" :html="renderCell(data, key)" :form="data"></aj-cell-renderer>
                <aj-cell-renderer v-if="isEditMode && key && key.editMode" :html="rendererEditMode(data, key)" :form="data"></aj-cell-renderer>
                <input type="text" v-if="canEdit(key)" size="0" v-model="data[key]" /> 
            </td>
            <td v-if="showControl" class="control">
                <aj-cell-renderer v-if="controlUi" :html="controlUi" :form="data"></aj-cell-renderer>
                <span @click="onEditClk"><img :src="ajResources.commonAsset + '/icon/update.gif'" /> 
                {{!isEditMode ? "编辑" : "确定"}}</span>
                <span @click="dele(id)"><img :src="ajResources.commonAsset + '/icon/delete.gif'" /> 删除</span> 
            </td>
        </tr>    
    `,
    props: {
        rowData: { type: Object, required: true },			    // 输入的数据
        showIdCol: { type: Boolean, default: true },			// 是否显示 id 列
        columns: Array, // 列
        showCheckboxCol: { type: Boolean, default: true },	    // 是否显示 selectCheckbox 列
        showControl: { type: Boolean, default: true },
        filterField: Array, 								    // 不要显示的字段,
        enableInlineEdit: { type: Boolean, default: false },	// 是否可以 inline-edit
        deleApi: String, 									    // 删除路径
        controlUi: String									    // 自定义“操作”按钮，这里填组件的名字
    },
    data() {
        return {
            id: this.rowData.id,
            data: this.rowData,
            isEditMode: false
        };
    },
    mounted() {
        for (var i in this.data) { // 监视每个字段
            this.$watch('data.' + i, this.makeWatch(i));
        }
    },
    computed: {
        filterData() {// dep
            var data = JSON.parse(JSON.stringify(this.data));// 剔除不要的字段
            delete data.id;
            delete data.dirty;

            if (this.filterField && this.filterField.length)
                this.filterField.forEach(i => delete data[i]);

            return data;
        },

        styleModifly() {
            return {
                padding: this.isEditMode ? 0 : '',
                //fontSize: this.isEditMode ? 0 : ''
            };
        }
    },
    methods: {
        // 是否固定的字段，不能被编辑
        isFixedField(field) {
            if (this.filterField && this.filterField.length) {
                for (var i = 0, j = this.filterField.length; i < j; i++) {
                    if (this.filterField[i] == field)
                        return true;
                }
            }

            return false;
        },
        // 没有指定编辑器的情况下，使用 input 作为编辑器
        canEdit(key) {
            return this.isEditMode && !this.isFixedField(key) && !key.editMode;
        },
        renderCell(data, key) {
            var v;
            if (typeof key == 'function') {
                v = key(data);
                return v;
            }

            if (key === '')
                return '';

            if (typeof key == 'object' && key.showMode) {
                if (typeof key.showMode === 'function')
                    return key.showMode(data);

                if (typeof key.showMode === 'string')
                    return data[key.showMode];
            }

            v = data[key];
            return (v === null ? '' : v) + '';
        },
        // 编辑按钮事件
        onEditClk() {
            if (this.enableInlineEdit)
                this.isEditMode = !this.isEditMode;
            else if (this.$parent.onEditClk)
                this.$parent.onEditClk(this.id);
        },
        rendererEditMode(data, cfg) {
            if (typeof cfg === 'string')
                return cfg;
            if (cfg.editMode && typeof cfg.editMode === 'function')
                return cfg.editMode(data);

            return "NULL";
        },
        makeWatch(field) {
            return function (_new) {
                var arr = this.$parent.list;

                var data;
                for (var i = 0, j = arr.length; i < j; i++) {
                    if (this.id && (arr[i].id === this.id)) {
                        data = arr[i];
                        break;
                    }
                }

                if (!data)
                    throw '找不到匹配的实体！目标 id: ' + this.id;

                if (!data.dirty)
                    data.dirty = {};

                data.dirty[field] = _new; // 保存新的值，key 是字段名
            }
        },
        dbEdit($event) {
            if (!this.enableInlineEdit)
                return;

            this.isEditMode = !this.isEditMode;

            if (this.isEditMode) {
                var el = $event.target;

                setTimeout(() => {
                    if (el.tagName !== 'INPUT')
                        el = el.$('input');

                    el && el.focus();
                }, 200);
            }
        },
        selectCheckboxChange($event) {
            var checkbox = $event.target;
            var parent = this.$parent;

            if (checkbox.checked)
                parent.$set(parent.selected, this.id, true);
            //this.$parent.selected[this.id] = true;
            else
                parent.$set(parent.selected, this.id, false);
        },
        // 删除记录
        dele(id) {
            aj.showConfirm('确定删除记录id:[' + id + ']', () =>
                aj.xhr.dele(this.deleApi + '/' + id + '/', j => {
                    if (j.isOk) {
                        aj.msg.show('删除成功');
                        this.$parent.reload();
                    }
                })
            );
        }
    }
});