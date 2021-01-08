interface EditRowCreate extends Vue {
    /**
     * 创建的 API 地址
     */
    createApi: string,

    /**
     * 是否处于编辑模式
     */
    isEditMode: boolean
}

Vue.component('aj-grid-inline-edit-row-create', {
    template: `
        <tr class="aj-grid-inline-edit-row isEditMode"> 
            <td><input type="checkbox" /></td>
            <td></td>
            <td v-for="key in columns" style="padding:0" class="cell" @dblclick="dbEdit">
                <aj-select v-if="key != null && key.type == 'select'" :name="key.name" :options="key.data" style="width: 200px;"></aj-select>
                <input  v-if="key != null && !key.type" type="text" size="0" :name="key" /> 
            </td>
            <td class="control">
                <span @click="addNew"><img :src="ajResources.commonAsset + '/icon/update.gif'" />新增</span>
                <span @click="$parent.showAddNew = false"><img :src="ajResources.commonAsset + '/icon/delete.gif'" /> 撤销</span>
            </td>
        </tr>
    `,
    props: {
        columns: { type: Array, required: true },
        createApi: { type: String, required: false, default: '.' }
    },
    methods: {
        /**
         * 编辑按钮事件
         * 
         * @param this 
         */
        addNew(this: EditRowCreate): void {
            var map: { [key: string]: string } = {};
            this.$el.$('*[name]', (i: HTMLInputElement) => map[i.name] = i.value);
            this.BUS.$emit('before-add-new', map);

            aj.xhr.post(this.createApi, (j: RepsonseResult) => {
                if (j && j.isOk) {
                    aj.msg.show('新建实体成功');

                    this.$el.$('input[name]', (i: HTMLInputElement) => { // clear
                        i.value = '';
                    });

                    // @ts-ignore
                    this.$parent.reload();
                    // @ts-ignore
                    this.$parent.showAddNew = false;
                } else if (j && j.msg) {
                    aj.msg.show(j.msg);
                }
            }, map);
        },

        /**
         * 
         * @param this 
         * @param $event 
         */
        dbEdit(this: EditRowCreate, $event: Event) {
            this.isEditMode = !this.isEditMode;
            var el: HTMLElement = <HTMLElement>$event.target;

            if (el.tagName !== 'INPUT')
                el = <HTMLElement>el.$('input');

            setTimeout(() => el && el.focus(), 200);
        }
    }
});