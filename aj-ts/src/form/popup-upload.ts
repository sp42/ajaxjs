/**
* 将上传控件嵌入到一个浮出层中
*/
Vue.component('aj-form-popup-upload', {
    template: html`
        <aj-layer>
            <h3>图片上传</h3>
            <p>上传成功后自动插入到正文</p>
            <aj-xhr-upload ref="uploadControl" :action="uploadUrl" :is-img-upload="true" :hidden-field="imgName"
                :img-place="ajResources.commonAsset + '/images/imgBg.png'">
            </aj-xhr-upload>
            <div>上传限制：{{text.maxSize}}kb 或以下，分辨率：{{text.maxHeight}}x{{text.maxWidth}}</div>
        </aj-layer>
    `,
    data() {
        return {
            text: {}
        };
    },
    props: {
        uploadUrl: { type: String, required: true },// 上传接口地址
        imgName: { type: String, required: false },// 貌似没用
        imgId: { type: Number, required: false },// 貌似没用
        imgPlace: String // 图片占位符，用户没有选定图片时候使用的图片
    },
    mounted(): void {
        let obj = this.$refs.uploadControl;
        this.text = { maxSize: obj.limitSize || 600, maxHeight: obj.imgMaxHeight, maxWidth: obj.imgMaxWidth };
    },
    methods: {
        /**
         * 显示上传控件
         * 
         * @param {Function} callback 上传成功之后的回调函数
         */
        show(this: Vue, callback: Function): void {
            if (callback)
                this.$refs.uploadControl.uploadOk_callback = callback;

            this.$children[0].show();
        }
    }
});