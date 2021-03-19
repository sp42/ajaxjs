namespace aj.list.grid {
    /**
     * 行的 UI
        总体就是一个普通的 HTML Table，程序是通过读取 JSON 数据（实际 JS 数组）里面的字段，
        然后通过动态添加表格的方法逐行添加 tr，td，在添加行的过程中根据需要设置样式，添加方法等。
        每一行数据（即 tr 的 DOM 对象）都有 id 属性，其值就等于数据中的 idColumn 对应的列的值
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
                    <aj-cell-renderer v-if="!isEditMode" :html="renderCell(rowData, cellRenderer)" :form="rowData"></aj-cell-renderer>
                    <aj-cell-renderer v-if="isEditMode && cellRenderer && cellRenderer.editMode"
                        :html="rendererEditMode(rowData, cellRenderer)" :form="rowData">
                    </aj-cell-renderer>
                    <input type="text" v-if="canEdit(cellRenderer)" size="0" v-model="rowData[cellRenderer]" />
                </td>
                <td v-if="showControl" class="control">
                    <aj-cell-renderer v-if="controlUi" :html="controlUi" :form="rowData"></aj-cell-renderer>
                    <span @click="onEditClk" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i>
                        {{!isEditMode ? "编辑" : "确定"}}</span>
                    <span @click="dele(id)" class="delete"><i class="fa fa-times" aria-hidden="true"></i> 删除</span>
                </td>
            </tr>
        `;

        props = {
            initRowData: { type: Object, required: true },
            showIdCol: { type: Boolean, default: true },			// 是否显示 id 列
            columns: Array, // 列
            showCheckboxCol: { type: Boolean, default: true },	    // 是否显示 selectCheckbox 列
            showControl: { type: Boolean, default: true },
            filterField: Array, 								    // 不可编辑的字段,
            enableInlineEdit: { type: Boolean, default: false },	// 是否可以 inline-edit
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

        //@ts-ignore
        $parent: Grid = null;

        computed = {
            filterData(this: GridEditRow) {// dep
                let data = JSON.parse(JSON.stringify(this.rowData));// 剔除不要的字段
                delete data.id;
                delete data.dirty;

                if (this.filterField && this.filterField.length)
                    this.filterField.forEach(i => delete data[i]);

                return data;
            },

            /**
             * 
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
         * 没有指定编辑器的情况下，使用 input 作为编辑器
         * 
         * @param cellRenderer 
         */
        canEdit(cellRenderer: CellRenderer): boolean {
            return this.isEditMode && !isFixedField.call(this, cellRenderer) && !((<CellRendererConfig>cellRenderer).editMode);
        }

        /**
         * 渲染单元格
         * 
         * @param data 
         * @param cellRenderer 
         */
        renderCell(data: JsonParam, cellRenderer: CellRenderer): string {
            let v;
            if (cellRenderer === '')
                return '';

            if (typeof cellRenderer == 'string') {
                v = data[<CellRendererKey>cellRenderer];
                return v + "";
            }

            if (typeof cellRenderer == 'function') {
                v = (<CellRendererFn>cellRenderer)(data);
                return v;
            }

            if (typeof cellRenderer == 'object') {
                let cfg: CellRendererConfig = <CellRendererConfig>cellRenderer;

                if (!!cfg.renderer)
                    v = cfg.renderer(data);
                // if (typeof key.showMode === 'function')
                //     return key.showMode(data);

                // if (typeof key.showMode === 'string')
                //     return data[key.showMode];
            }

            return (v === null ? '' : v) + '';
        }

        /**
         * 编辑按钮事件
         * 
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
         * 渲染编辑模式下的行
         * 
         * @param data 
         * @param cellRenderer 
         */
        rendererEditMode(data: JsonParam, cellRenderer: CellRenderer): string {
            if (typeof cellRenderer === 'string')
                return cellRenderer.toString();

            if ((<CellRendererConfig>cellRenderer).editMode && typeof (<CellRendererConfig>cellRenderer).editRenderer === 'function')
                return (<CellRendererConfig>cellRenderer).editRenderer(data);

            return "NULL";
        }

        /**
         * 双击单元格进入编辑
         * 
         * @param ev 
         */
        dbEdit(ev: Event): void {
            if (!this.enableInlineEdit)
                return;

            this.isEditMode = !this.isEditMode;

            if (this.isEditMode) {
                let el: HTMLElement = <HTMLElement>ev.target;

                setTimeout(() => {
                    if (el.tagName !== 'INPUT')
                        el = <HTMLElement>el.$('input');

                    el && el.focus();
                }, 200);
            }
        }

        /**
         * 
         * @param ev 
         */
        selectCheckboxChange(ev: Event): void {
            let checkbox: HTMLInputElement = <HTMLInputElement>ev.target,
                parent = this.$parent;

            if (checkbox.checked)
                parent.$set(parent.selected, this.id, true);
            //this.$parent.selected[this.id] = true;
            else
                parent.$set(parent.selected, this.id, false);
        }

        /**
         * 删除记录
         * 
         * @param id 
         */
        dele(id: string): void {
            showConfirm(`确定删除记录 id:[${id}] 吗？`, () =>
                xhr.dele(`${this.$parent.apiUrl}/${id}/`, (j: RepsonseResult) => {
                    if (j.isOk) {
                        aj.msg.show('删除成功');
                        this.$parent.reload();
                    }
                })
            );
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
            let arr: GridRecord[] = this.$parent.list, data!: GridRecord;

            for (let i = 0, j = arr.length; i < j; i++) {// 已知 id 找到原始数据
                if (this.id && (String(arr[i].id) == this.id)) {
                    data = arr[i];
                    break;
                }
            }

            if (!data)
                throw '找不到匹配的实体！目标 id: ' + this.id;

            if (!data.dirty)
                data.dirty = {
                    id: this.id
                };

            data.dirty[field] = _new; // 保存新的值，key 是字段名
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