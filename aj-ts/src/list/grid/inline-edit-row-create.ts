namespace aj.list.grid {
    /**
     * 新建记录的行
     */
    class EditRowCreate extends VueComponent {
        name = "aj-grid-inline-edit-row-create";

        template = html`
            <tr class="aj-grid-inline-edit-row isEditMode">
                <td></td>
                <td></td>
                <td v-for="key in columns" style="padding:0" class="cell">
                    <aj-select v-if="key != null && key.type == 'select'" :name="key.name" :options="key.data"  style="width: 200px;"></aj-select>
                    <input v-if="key != null && !key.type" type="text" size="0" :name="key" style="background-color:white" />
                </td>
                <td class="control">
                    <span @click="addNew"><i class="fa fa-plus" style="color:#080;"></i> 新增</span>
                    <span @click="$parent.showAddNew = false"><i class="fa fa-undo" style="color:#bc49eb;"></i> 撤销</span>
                </td>
            </tr>
        `;

        props = {
            columns: { type: Array, required: true },
            createApi: { type: String, required: false, default: '.' }
        };

        /**
         * 创建的 API 地址
         */
        createApi: string = "";

        $parent: Grid | null = null;

        /**
         * 新增按钮事件
         */
        addNew(): void {
            let map: { [key: string]: string } = {}; // 创建动作的表单数据

            this.$el.$('*[name]', (i: HTMLInputElement) => map[i.name] = i.value);
            this.BUS.$emit('before-add-new', map);

            xhr.post(this.createApi, (j: RepsonseResult) => {
                if (j && j.isOk) {
                    aj.msg.show('新建实体成功');

                    this.$el.$('input[name]', (i: HTMLInputElement) => { // clear
                        i.value = '';
                    });

                    if (this.$parent) {
                        this.$parent.reload();
                        this.$parent.showAddNew = false;
                    }
                } else if (j && j.msg) {
                    aj.msg.show(j.msg);
                }
            }, map);
        }
    }

    new EditRowCreate().register();
}
