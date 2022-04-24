export default {
    props: {
        table: { type: Object, required: false },
        dml: {
            type: Object, required: false, default() { return {};  /* 因为异步的关系，所以配置不是即刻可以获取。为了防止 nullpoint，给出一个默认的对象 */ }
        },
    },
    data() {
        return {
            showFields: false,
            fields: {},
        };
    },
    methods: {
        getIdField(): string {
            if (this.table)
                return '`' + this.table.fieldsMapping.id + '`';
        },

        /**
         * 获取用户选择的字段，并保存到 DML 配置中
         */
        selectField(): string[] {
            let arr: string[] = [];

            for (let i in this.fields) {
                if (this.fields[i]) arr.push(i);
            }

            this.dml.fields = arr;
            this.getSql();

            return arr;
        },
    }
}