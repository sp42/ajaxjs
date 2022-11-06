export default {
    props: {
        apiRoot: { type: String, required: true },  // API 选择器需要这个属性
    },

    data() {
        return {
            isShowFieldsSelect: false,
            isShowApiSelect: false,
            isShowPerview: false,       // 是否显示预览
        };
    },
    methods: {
        /**
         * 导出组件
         */
        exportCmp(): void {
            alert('TODO');
        },

        /**
         * 显示更多的属性
         */
        moreAttrib(row: any, index: Number): void {
            this.isShowMoreAttrib = true;
            this.$refs.MoreAttrib.edit(row);
        },
    }
};