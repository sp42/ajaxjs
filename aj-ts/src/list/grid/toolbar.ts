/**
 * 工具条
 */
; (function () {
    Vue.component('aj-entity-toolbar', {
        template: `
            <div class="toolbar">
                <form v-if="search" class="right">
                    <input type="text" name="keyword" placeholder="请输入关键字" size="12" />
                    <button @click="doSearch"><i class="fa fa-search" style="color:#417BB5;"></i>搜索</button>
                </form>
                <aj-form-between-date v-if="betweenDate" class="right"></aj-form-between-date>
                <ul>
                    <li v-if="create" @click="$emit('on-create-btn-clk')"><i class="fa fa-plus" style="color:#0a90f0;"></i> 新建</li>
                    <li v-if="save" @click="$emit('on-save-btn-clk')"><i class="fa fa-floppy-o" style="color:rgb(205, 162, 4);"></i> 保存</li>
                    <li v-if="deleBtn" @click="BUS.$emit('on-delete-btn-clk')"><i class="fa fa-trash-o" style="color:red;"></i> 删除</li>
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
             * 检查日期是否有效
             * 
             * @param this 
             * @param e 
             */
            valid(this: Vue, e: Event): void {
                let start: string | null = getValue.call(this, 'input[name=startDate]'),
                    end: string | null = getValue.call(this, 'input[name=endDate]');

                if (!start || !end) {
                    aj.showOk("输入数据不能为空");
                    e.preventDefault();
                }

                if (new Date(start) > new Date(end)) {
                    aj.showOk("起始日期不能晚于结束日期");
                    e.preventDefault();
                }
            },

            /**
             * 获取关键字进行搜索
             * 
             * @param this 
             * @param e 
             */
            doSearch(this: Vue, e: Event): void {
                e.preventDefault();
                // @ts-ignore
                aj.apply(this.$parent.$refs.pager.extraParam, { keyword: getValue.call(this, 'input[name=keyword]') });
                // @ts-ignore
                this.$parent.reload();
            }
        }
    });

    /**
     * 获取控件的值
     * 
     * @param this 
     * @param cssSelector 
     */
    function getValue(this: Vue, cssSelector: string): string | null {
        let el = this.$el.$(cssSelector);
        if (el)
            return (<HTMLInputElement>el).value;

        return null;
    }
})();