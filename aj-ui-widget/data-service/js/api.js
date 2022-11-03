Vue.component('api', {
    template: `<div class="api">
        <span :class="httpMethod" style="font-weight: bold;">
            {{httpMethod}}
        </span>
        {{getUrl()}}
        <a :href="getUrl()" target="_blank" title="打开连接">&#x1f517;</a>
        <a href="javascript:void(0)" @click="copy" title="复制">📄</a>
        <a href="javascript:alert('TODO')" style="text-decoration:underline;">测试</a>
    </div>`,
    data() {
        return {
            httpMethod: 'GET'
        };
    },
    methods: {
        getUrl() {
            return 'http://ssssssssss.com';
        },
        copy() {
            copyToClipboard(this.getUrl())
            this.$Message.success('复制成功');
        }
    }
});  
