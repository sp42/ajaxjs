namespace aj.list.grid {

    Vue.component('foo', {
        template: '<div>foo</div>'
    })
    /**
     * 行的 UI
     */
    class GridEditRow extends VueComponent {
        name = "aj-grid-inline-edit-row";

        template = html`
            <tr class="aj-grid-inline-edit-row" :class="{editing: isEditMode}">
                <td v-if="showCheckboxCol" class="selectCheckbox">
                    <input type="checkbox" @change="selectCheckboxChange" :data-id="id" />
                </td>
                <td v-if="showIdCol">{{id}}</td>
                <td v-for="cellRenderer in columns" :style="styleModifly" class="cell" @dblclick="dbEdit">
                    <span v-if="!isEditMode" v-html="renderCell(rowData, cellRenderer)"></span>
                    <input v-if="canEdit(cellRenderer)" v-model="rowData[cellRenderer]" type="text" size="0" />
                    <span v-if="cellRenderer && cellRenderer.cmpName">
                        <component v-if="!isEditMode || !cellRenderer.editMode" v-bind:is="cellRenderer.cmpName" v-bind="cellRenderer.cmpProps(rowData)"></component>
                        <component v-if="isEditMode && cellRenderer.editMode" v-bind:is="cellRenderer.editCmpName" v-bind="cellRenderer.cmpProps(rowData)">
                                    </component> 
                    </span>
                </td>
                <td v-if="showControl" class="control">
                    <aj-cell-renderer v-if="controlUi" :html="controlUi" :form="rowData"></aj-cell-renderer>
                    <span @click="onEditClk" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i>
                        {{!isEditMode ? "编辑" : "确定"}}</span>
                    <span @click="dele" class="delete"><i class="fa fa-times" aria-hidden="true"></i> 删除</span>
                </td>
            </tr>`;


        props = {
            initRowData: { type: Object, required: true },
            showIdCol: { type: Boolean, default: true },			// 是否显示 id 列
            showCheckboxCol: { type: Boolean, default: true },	    // 是否显示 selectCheckbox 列
            showControl: { type: Boolean, default: true },
            enableInlineEdit: { type: Boolean, default: false },	// 是否可以 inline-edit
            columns: Array,                                         // 列
            filterField: Array, 								    // 不可编辑的字段,
            deleApi: String, 									    // 删除路径
            controlUi: String									    // 自定义“操作”按钮，这里填组件的名字
        };

        /**
         * 固定不可编辑的字段
         */
        filterField: Array<string> = [];

        /**
         * 每行记录它的 id
         */
        id: string = "";

        /**
         * 输入的数据
         */
        initRowData: JsonParam = {};

        /**
         * 每行记录的数据
         */
        rowData: JsonParam = {};

        /**
         * 是否处于编辑模式
         */
        isEditMode: boolean = false;

        /**
         * 表格是否可以被编辑
         */
        enableInlineEdit: boolean = false;

        /**
         * 单元格渲染器的类型，这是一个有序的数组
         */
        columns: Array<CellRenderer> = [];

        $parent: Grid | null = null;

        computed = {
            // filterData(this: GridEditRow) {// dep
            //     let data = JSON.parse(JSON.stringify(this.rowData));// 剔除不要的字段
            //     delete data.id;
            //     delete data.dirty;

            //     if (this.filterField && this.filterField.length)
            //         this.filterField.forEach(i => delete data[i]);

            //     return data;
            // },
 
            /**
             * 修改样式
             * 
             * @param this 
             */
            styleModifly(this: GridEditRow): Object {
                return {
                    padding: this.isEditMode ? 0 : '',
                    //fontSize: this.isEditMode ? 0 : ''
                };
            }
        };

        data() {
            return {
                id: this.initRowData.id,
                rowData: this.initRowData,
                isEditMode: false
            };
        }

        mounted(): void {
            for (var i in this.rowData) // 监视每个字段
                this.$watch('rowData.' + i, makeWatch.call(this, i));
        }

        /**
         * 选区模型的写入，记录哪一行被选中了
         * 
         * @param ev 事件对象 
         */
        selectCheckboxChange(ev: Event): void {
            let checkbox: HTMLInputElement = <HTMLInputElement>ev.target,
                parent = this.$parent;

            if (parent) {
                if (checkbox.checked)
                    parent.$set(parent.selected, this.id, true);
                else
                    parent.$set(parent.selected, this.id, false);
            }
        }

        /**
         * 渲染单元格
         * 
         * @param data 
         * @param cellRenderer 
         */
        renderCell(data: JsonParam, cellRenderer: CellRenderer): string {
            let v: string = "";
            if (cellRenderer === '')
                return v;

            if (typeof cellRenderer == 'string')
                v = data[<CellRendererKey>cellRenderer] + "";

            if (typeof cellRenderer == 'function')
                v = (<CellRendererFn>cellRenderer)(data);

            if (typeof cellRenderer == 'object') {
                let cfg: CellRendererConfig = <CellRendererConfig>cellRenderer;

                if (!!cfg.renderer)
                    v = cfg.renderer(data);
            }

            return v;
        }

        /**
         * 没有指定编辑器的情况下，使用 input 作为编辑器
         * 
         * @param cellRenderer 
         */
        canEdit(cellRenderer: CellRenderer): boolean {
            return this.isEditMode && !isFixedField.call(this, cellRenderer) && !((<CellRendererConfig>cellRenderer).editMode);
        }

        /**
         * 渲染编辑模式下的行
         * 
         * @param data 
         * @param cellRenderer 
         */
        rendererEditMode(data: JsonParam, cellRenderer: CellRenderer): string {
            if (typeof cellRenderer === 'string')
                return cellRenderer.toString();

            let cfg: CellRendererConfig = <CellRendererConfig>cellRenderer;

            if (cfg.editMode && typeof cfg.editRenderer === 'function')
                return cfg.editRenderer(data);

            return "NULL";
        }

        /**
         * 编辑按钮事件
         */
        onEditClk(): void {
            if (this.enableInlineEdit)
                this.isEditMode = !this.isEditMode;
            //@ts-ignore    
            else if (this.$parent.onEditClk) // 打开另外的编辑界面
                //@ts-ignore    
                this.$parent.onEditClk(this.id);
        }

        /**
         * 双击单元格进入编辑
         * 
         * @param ev 
         */
        dbEdit(ev: Event): void {
            this.onEditClk();

            if (this.enableInlineEdit && this.isEditMode) {
                let el: HTMLElement = <HTMLElement>ev.target;
                if (el.tagName !== 'TD')
                    el = <HTMLElement>el.up('td');

                setTimeout(() => {
                    let _el;
                    if (el.tagName !== 'INPUT')
                        _el = <HTMLElement>el.$('input');

                    _el && _el.focus();
                }, 200);
            }
        }

        /**
         * 删除记录
         */
        dele(id: string): void {
            showConfirm(`确定删除记录 id:[${this.id}] 吗？`, () => {
                if (this.$parent)
                    xhr.dele(`${this.$parent.apiUrl}/${this.id}/`, (j: RepsonseResult) => {
                        if (j.isOk) {
                            msg.show('删除成功');
                            this.$parent && this.$parent.reload();
                        }
                    })
            });
        }
    }

    new GridEditRow().register();

    /**
     * 生成该字段的 watch 函数
     * 
     * @param this 
     * @param field 
     */
    function makeWatch(this: GridEditRow, field: string): (_new: any) => void {
        return function (this: GridEditRow, _new: any) {
            if (this.$parent) {
                let arr: GridRecord[] = this.$parent.list,
                    data!: GridRecord;

                for (let i = 0, j = arr.length; i < j; i++) {// 已知 id 找到原始数据
                    if (this.id && (String(arr[i].id) == this.id)) {
                        data = arr[i];
                        break;
                    }
                }

                if (!data)
                    throw '找不到匹配的实体！目标 id: ' + this.id;

                if (!data.dirty)
                    data.dirty = { id: this.id };

                data.dirty[field] = _new; // 保存新的值，key 是字段名
            }
        }
    }

    /**
     * 是否固定的字段，固定的字段不能被编辑
     * 
     * @param this 
     * @param cellRenderer 
     */
    function isFixedField(this: GridEditRow, cellRenderer: CellRenderer): boolean {
        if (this.filterField && this.filterField.length) {
            for (let i = 0, j = this.filterField.length; i < j; i++) {
                if (this.filterField[i] == cellRenderer)
                    return true;
            }
        }

        return false;
    }
}