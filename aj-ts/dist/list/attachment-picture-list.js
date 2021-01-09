"use strict";
/**
 * 相册列表
 */
Vue.component('aj-attachment-picture-list', {
    template: "\n        <table>\n            <tr>\n            <td>\n                <div class=\"label\">\u76F8\u518C\u56FE\uFF1A</div>\n                <ul>\n                    <li v-for=\"pic in pics\" style=\"float:left;margin-right:1%;text-align:center;\">\n                        <a :href=\"picCtx + pic.path\" target=\"_blank\"><img :src=\"picCtx + pic.path\" style=\"max-width: 100px;max-height: 100px;\" /></a><br />\n                        <a href=\"###\" @click=\"delPic(pic.id);\">\u5220 \u9664</a>\n                    </li>\n                </ul>\n            </td>\n            <td>\n                <aj-xhr-upload ref=\"attachmentPictureUpload\" :action=\"uploadUrl\" :is-img-upload=\"true\" :img-place=\"blankBg\"></aj-xhr-upload>\n            </td></tr>\n        </table>\n    ",
    props: {
        picCtx: String,
        uploadUrl: String,
        blankBg: String,
        delImgUrl: String,
        apiUrl: String
    },
    data: function () {
        return {
            pics: []
        };
    },
    mounted: function () {
        this.getData();
        this.$refs.attachmentPictureUpload.uploadOk_callback = this.getData;
    },
    methods: {
        getData: function () {
            var _this = this;
            aj.xhr.get(this.apiUrl, function (j) { return _this.pics = j.result; });
        },
        delPic: function (picId) {
            var _this = this;
            aj.showConfirm("确定删除相册图片？", function () {
                aj.xhr.dele(_this.delImgUrl + picId + "/", function (j) {
                    if (j.isOk)
                        _this.getData(); // 刷新
                });
            });
        }
    }
});
