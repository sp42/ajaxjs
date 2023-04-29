<script lang="ts">
import ItemRender from './item-render.vue';

/*
  当需要动态维护 FormItem 时，也可以直接给 FormItem 设置属性 rules 来单独为该域做验证。
  动态设置 FormItem 的 prop 属性时，会依据上层的 Form 组件的 model 来获取，查看示例代码。
  FormItem 还可以独立设置 required、error 等属性，详见 API。
*/
export default {
    components: { ItemRender },
    props: {
        cfg: { type: Object, required: true }
    },
    data() {
        return {
            index: 1,
            formDynamic: {
                fields: this.cfg.fields
            },
            data: {}, // 数据
            status: 1  // 0=查看/1=新增/2=修改
        };
    },
    render(h: Function): any {
        if(!this.cfg.fields || !this.cfg.fields.length)
            return;
            
        let children: any[] = [];
        let stack2: any[] = [], stack3: any[] = [];

        for (let i = 0, j = this.cfg.fields.length; i < j; i++) {
            let item = this.cfg.fields[i];
            
            if (item.isShow) {
                let uiLayout = item.uiLayout;
                let itemTag = [h('ItemRender', { props: { item: item, data: this.data, status: this.status } })];

                if (uiLayout == 1)
                    children.push([h('Col', { props: { span: 24 } }, itemTag)]);
                else if (uiLayout == 2) {
                    stack2.push([h('Col', { props: { span: 12 } }, itemTag)]);

                    if (stack2.length == 2) {
                        children.push(stack2);
                        stack2 = [];
                    }
                } else if (uiLayout == 3) {
                    stack3.push([h('Col', { props: { span: 8 } }, itemTag)]);

                    if (stack3.length == 3) {
                        children.push(stack3);
                        stack3 = [];
                    }
                }
            }
        }

        let arr: any[] = [];
        children.forEach(item => arr.push(h('Row', item))); // 输入到 行

        return this.$createElement('Form', {
            ref: 'formDynamic',
            props: {
                 'label-width': this.cfg.labelWidth || 80, 'label-colon': this.status === 0
            }
        }, arr);
    },

    methods: {
        handleSubmit(name) {
            this.$refs[name].validate((valid) => {
                if (valid) {
                    this.$Message.success('Success!');
                } else {
                    this.$Message.error('Fail!');
                }
            });
        },

        handleAdd() {
            this.index++;
            this.formDynamic.fields.push({
                value: '',
                index: this.index,
                status: 1
            });
        },
        handleRemove(index) {
            this.formDynamic.fields[index].status = 0;
        },
    }
};
</script>

<style lang="less">
.html-content {
    img {
        max-width: 60%;
    }
}
</style>