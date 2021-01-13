/**
 * 工具条
 */
Vue.component('aj-entity-toolbar', {
    template: html `
        <div class="toolbar"> 
            <form v-if="search" class="right">
                <input type="text" name="keyword" placeholder="请输入关键字" size="12" />
                <button @click="doSearch"><i class="fa fa-search" style="color:#417BB5;"></i>搜索</button>
            </form>
            <aj-form-between-date v-if="betweenDate" class="right"></aj-form-between-date>
            <ul>
                <li v-if="create" @click="$emit('on-create-btn-clk')"><i class="fa fa-plus" style="color:#0a90f0;"></i> 新建</li>
                <li v-if="save" @click="$emit('on-save-btn-clk')"><i class="fa fa-floppy-o" style="color:rgb(205, 162, 4);"></i> 保存</li>
                <li v-if="deleBtn" @click="$emit('on-delete-btn-clk')"><i class="fa fa-trash-o" style="color:red;"></i> 删除</li>
                <li v-if="excel"><i class="fa fa-file-excel-o" style="color:green;"></i> 导出</li>
                <slot></slot>
                
            </ul>
        </div>
    `,
    props: {
        betweenDate: { type: Boolean, default: true },
        create: { type: Boolean, default: true },
        save: { type: Boolean, default: true },
        excel: { type: Boolean, default: false },
        deleBtn: { type: Boolean, default: true },
        search: { type: Boolean, default: true }
    },
    methods: {
        /**
         * 获取关键字进行搜索
         * 
         * @param this 
         * @param e 
         */
        doSearch(this: GridToolbar, e: Event): void {
            e.preventDefault();

            aj.apply(this.$parent.$store.extraParam, { keyword: aj.form.utils.getFormFieldValue(this.$el, 'input[name=keyword]') });
            this.$parent.$store.reload();
        }
    }
});
