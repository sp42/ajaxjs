"use strict";
;
(function () {
    Vue.component('aj-xhr-upload', {
        template: "\n            <div class=\"aj-xhr-upload\" :style=\"{display: buttonBottom ? 'inherit': 'flex'}\">\n                <input v-if=\"hiddenField\" type=\"hidden\" :name=\"hiddenField\" :value=\"hiddenFieldValue\" />\n                <div v-if=\"isImgUpload\">\n                    <a :href=\"imgPlace\" target=\"_blank\">\n                        <img class=\"upload_img_perview\" :src=\"(isFileSize && isExtName && imgBase64Str) ? imgBase64Str : imgPlace\" />\n                    </a>\n                </div>\n                <div class=\"pseudoFilePicker\">\n                    <label :for=\"'uploadInput_' + radomId\"><div><div>+</div>\u70B9\u51FB\u9009\u62E9{{isImgUpload ? '\u56FE\u7247': '\u6587\u4EF6'}}</div></label>\n                </div>\n                <input type=\"file\" :name=\"fieldName\" class=\"hide\" :id=\"'uploadInput_' + radomId\" \n                    @change=\"onUploadInputChange\" :accept=\"isImgUpload ? 'image/*' : accpectFileType\" />\n                <div v-if=\"!isFileSize || !isExtName\">{{errMsg}}</div>\n                <div v-if=\"isFileSize && isExtName\">\n                    {{fileName}}<br />\n                    <button @click.prevent=\"doUpload\" style=\"min-width:110px;\">{{progress && progress !== 100 ? '\u4E0A\u4F20\u4E2D ' + progress + '%': '\u4E0A\u4F20'}}</button>\n                </div>\n            </div>    \n        ",
        props: {
            action: { type: String, required: true },
            fieldName: String,
            limitSize: Number,
            hiddenField: { type: String, default: null },
            hiddenFieldValue: String,
            limitFileType: String,
            accpectFileType: String,
            isImgUpload: Boolean,
            imgPlace: String,
            imgMaxWidth: { type: Number, default: 1920 },
            imgMaxHeight: { type: Number, default: 1680 },
            buttonBottom: Boolean // 上传按钮是否位于下方
        },
        data: function () {
            var _this = this;
            return {
                isFileSize: false,
                isExtName: false,
                isImgSize: false,
                errMsg: null,
                newlyId: null,
                radomId: Math.round(Math.random() * 1000),
                uplodedFileUrl: null,
                uploadOk_callback: function (json) {
                    if (json.isOk) {
                        _this.uplodedFileUrl = json.imgUrl;
                        if (_this.hiddenField)
                            _this.$el.$('input[name=' + _this.hiddenField + ']').value = json.imgUrl;
                    }
                    aj.xhr.defaultCallBack(json);
                },
                imgBase64Str: null,
                progress: 0,
                fileName: '' // 获取文件名称，只能是名称，不能获取完整的文件录
            };
        },
        methods: {
            /**
             *
             * @param this
             * @param $event
             */
            onUploadInputChange: function ($event) {
                var _this = this;
                var fileInput = $event.target;
                if (!fileInput.files || !fileInput.files[0])
                    return;
                var ext = fileInput.value.split('.').pop(); // 扩展名
                this.$fileObj = fileInput.files[0]; // 保留引用
                this.$fileName = this.$fileObj.name;
                this.$fileType = this.$fileObj.type;
                var size = this.$fileObj.size;
                if (this.limitSize) {
                    this.isFileSize = size < this.limitSize;
                    this.errMsg = "要上传的文件容量过大，请压缩到 " + this.limitSize + "kb 以下";
                }
                else
                    this.isFileSize = true;
                if (this.limitFileType) {
                    this.isExtName = new RegExp(this.limitFileType, 'i').test(ext);
                    this.errMsg = '根据文件后缀名判断，此文件不能上传';
                }
                else
                    this.isExtName = true;
                readBase64.call(this, fileInput.files[0]);
                if (this.isImgUpload) {
                    var imgEl = new Image();
                    imgEl.onload = function () {
                        if (imgEl.width > _this.imgMaxWidth || imgEl.height > _this.imgMaxHeight) {
                            _this.isImgSize = false;
                            _this.errMsg = '图片大小尺寸不符合要求哦，请重新图片吧~';
                        }
                        else {
                            _this.isImgSize = true;
                        }
                    };
                }
                getFileName.call(this);
            },
            /**
             * 执行上传
             *
             * @param this
             */
            doUpload: function () {
                var _this = this;
                var fd = new FormData();
                if (this.$blob)
                    fd.append("file", this.$blob, this.$fileName);
                else
                    fd.append("file", this.$fileObj);
                var xhr = new XMLHttpRequest();
                //@ts-ignore
                xhr.onreadystatechange = aj.xhr.requestHandler.delegate(null, this.uploadOk_callback, 'json');
                xhr.open("POST", this.action, true);
                xhr.onprogress = function (e) {
                    var progress = 0, p = ~~(e.loaded * 1000 / e.total);
                    p = p / 10;
                    if (progress !== p)
                        progress = p;
                    _this.progress = progress;
                };
                xhr.send(fd);
            }
        }
    });
    // 文件头判别，看看是否为图片
    var imgHeader = { "jpeg": "/9j/4", "gif": "R0lGOD", "png": "iVBORw" };
    /**
     * 获取文件名称，只能是名称，不能获取完整的文件目录
     *
     * @param this
     */
    function getFileName() {
        var _a;
        var v = this.$el.$('input[type=file]').value;
        var arr = v.split('\\');
        this.fileName = (_a = arr.pop()) === null || _a === void 0 ? void 0 : _a.trim();
    }
    /**
     *
     * @param this
     * @param file
     */
    function readBase64(file) {
        var _this = this;
        var reader = new FileReader();
        reader.onload = function (_e) {
            var e = _e;
            _this.imgBase64Str = e.target.result;
            if (_this.isImgUpload) {
                var imgEl = new Image();
                imgEl.onload = function () {
                    if (file.size > 300 * 1024) // 大于 300k 才压缩
                        aj.img.compress(imgEl, _this);
                    if (imgEl.width > _this.imgMaxWidth || imgEl.height > _this.imgMaxHeight) {
                        _this.isImgSize = false;
                        _this.errMsg = '图片大小尺寸不符合要求哦，请裁剪图片重新上传吧~';
                    }
                    else
                        _this.isImgSize = true;
                };
                imgEl.src = _this.imgBase64Str;
                // 文件头判别，看看是否为图片
                for (var i in imgHeader) {
                    if (~_this.imgBase64Str.indexOf(imgHeader[i])) {
                        _this.isExtName = true;
                        return;
                    }
                }
                _this.errMsg = "亲，改了扩展名我还能认得你不是图片哦";
            }
        };
        reader.readAsDataURL(file);
    }
})();
