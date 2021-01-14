interface AttachmentPictureList extends Ajax {
    delImgUrl: string;
    pics: BaseObject[];
}

/**
 * 相册列表
 */
Vue.component('aj-attachment-picture-list', {
    template: `
        <table>
            <tr>
            <td>
                <div class="label">相册图：</div>
                <ul>
                    <li v-for="pic in pics" style="float:left;margin-right:1%;text-align:center;">
                        <a :href="picCtx + pic.path" target="_blank"><img :src="picCtx + pic.path" style="max-width: 100px;max-height: 100px;" /></a><br />
                        <a href="###" @click="delPic(pic.id);">删 除</a>
                    </li>
                </ul>
            </td>
            <td>
                <aj-xhr-upload ref="attachmentPictureUpload" :action="uploadUrl" :is-img-upload="true" :img-place="blankBg"></aj-xhr-upload>
            </td></tr>
        </table>
    `,
    props: {
        picCtx: String,
        uploadUrl: String,
        blankBg: String,
        delImgUrl: String,
        apiUrl: String
    },
    data() {
        return {
            pics: []
        };
    },
    mounted(): void {
        this.getData();
        this.$refs.attachmentPictureUpload.uploadOk_callback = this.getData;
    },
    methods: {
        getData(this: AttachmentPictureList): void {
            aj.xhr.get(this.apiUrl, (j: RepsonseResult) => this.pics = j.result);
        },
        delPic(this: AttachmentPictureList, picId: string): void {
            aj.showConfirm("确定删除相册图片？", () => {
                aj.xhr.dele(this.delImgUrl + picId + "/", (j: RepsonseResult) => {
                    if (j.isOk)
                        this.getData();// 刷新
                });
            });
        }
    }
});