"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
var aj;
(function (aj) {
    var xhr_upload;
    (function (xhr_upload) {
        Vue.component('aj-xhr-upload', {
            template: html(__makeTemplateObject(["\n            <div class=\"aj-xhr-upload\" :style=\"{display: buttonBottom ? 'inherit': 'flex'}\">\n            \n                <img class=\"upload_img_perview\" v-if=\"isImgUpload\"\n                    :src=\"(isFileSize && isExtName && imgBase64Str) ? imgBase64Str : imgPlace\" />\n                <div class=\"pseudoFilePicker\">\n                    <label :for=\"'uploadInput_' + radomId\">\n                        <div>\n                            <div>+</div>\u70B9\u51FB\u9009\u62E9{{isImgUpload ? '\u56FE\u7247': '\u6587\u4EF6'}}\n                        </div>\n                    </label>\n                </div>\n                <input type=\"file\" :name=\"fieldName\" :id=\"'uploadInput_' + radomId\" @change=\"onUploadInputChange\"\n                    :accept=\"isImgUpload ? 'image/*' : accpectFileType\" />\n                <div v-if=\"!isFileSize || !isExtName\">{{errMsg}}</div>\n                <div v-if=\"isFileSize && isExtName\">\n                    {{fileName}}<br />\n                    <button @click.prevent=\"doUpload\">{{progress && progress !== 100 ? '\u4E0A\u4F20\u4E2D ' + progress + '%': '\u4E0A\u4F20'}}</button>\n                </div>\n            </div>\n        "], ["\n            <div class=\"aj-xhr-upload\" :style=\"{display: buttonBottom ? 'inherit': 'flex'}\">\n            \n                <img class=\"upload_img_perview\" v-if=\"isImgUpload\"\n                    :src=\"(isFileSize && isExtName && imgBase64Str) ? imgBase64Str : imgPlace\" />\n                <div class=\"pseudoFilePicker\">\n                    <label :for=\"'uploadInput_' + radomId\">\n                        <div>\n                            <div>+</div>\u70B9\u51FB\u9009\u62E9{{isImgUpload ? '\u56FE\u7247': '\u6587\u4EF6'}}\n                        </div>\n                    </label>\n                </div>\n                <input type=\"file\" :name=\"fieldName\" :id=\"'uploadInput_' + radomId\" @change=\"onUploadInputChange\"\n                    :accept=\"isImgUpload ? 'image/*' : accpectFileType\" />\n                <div v-if=\"!isFileSize || !isExtName\">{{errMsg}}</div>\n                <div v-if=\"isFileSize && isExtName\">\n                    {{fileName}}<br />\n                    <button @click.prevent=\"doUpload\">{{progress && progress !== 100 ? '\u4E0A\u4F20\u4E2D ' + progress + '%': '\u4E0A\u4F20'}}</button>\n                </div>\n            </div>\n        "])),
            props: {
                action: { type: String, required: true },
                fieldName: String,
                fieldValue: String,
                limitSize: Number,
                limitFileType: String,
                accpectFileType: String,
                isImgUpload: Boolean,
                imgPlace: {
                    type: String, default: "data:image/svg+xml,%3Csvg class='icon' viewBox='0 0 1024 1024' xmlns='http://www.w3.org/2000/svg' width='200' height='200'%3E%3Cpath d='M304.128 456.192c48.64 0 88.064-39.424 88.064-88.064s-39.424-88.064-88.064-88.064-88.064 39.424-88.064 88.064 39.424 88.064 88.064 88.064zm0-116.224c15.36 0 28.16 12.288 28.16 28.16s-12.288 28.16-28.16 28.16-28.16-12.288-28.16-28.16 12.288-28.16 28.16-28.16z' " +
                        "fill='%23e6e6e6'/%3E%3Cpath d='M887.296 159.744H136.704C96.768 159.744 64 192 64 232.448v559.104c0 39.936 32.256 72.704 72.704 72.704h198.144L500.224 688.64l-36.352-222.72 162.304-130.56-61.44 143.872 92.672 214.016-105.472 171.008h335.36C927.232 864.256 960 832 960 791.552V232.448c0-39.936-32.256-72.704-72.704-72.704zm-138.752 71.68v.512H857.6c16.384 0 30.208 13.312 30.208 30.208v399.872L673.28 408.064l75.264-176.64zM304.64 " +
                        "792.064H165.888c-16.384 0-30.208-13.312-30.208-30.208v-9.728l138.752-164.352 104.96 124.416-74.752 79.872zm81.92-355.84l37.376 228.864-.512.512-142.848-169.984c-3.072-3.584-9.216-3.584-12.288 0L135.68 652.8V262.144c0-16.384 13.312-30.208 30.208-30.208h474.624L386.56 436.224zm501.248 325.632c0 16.896-13.312 30.208-29.696 30.208H680.96l57.344-93.184-87.552-202.24 7.168-7.68 229.888 272.896z' fill='%23e6e6e6'/%3E%3C/svg%3E"
                },
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
                                _this.$el.$("input[name=" + _this.hiddenField + "]").value = json.imgUrl;
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
                 * @param ev
                 */
                onUploadInputChange: function (ev) {
                    var _this = this;
                    var fileInput = ev.target;
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
                    xhr.onreadystatechange = xhr.requestHandler.delegate(null, this.uploadOk_callback, 'json');
                    xhr.open("POST", this.action, true);
                    xhr.onprogress = function (ev) {
                        var progress = 0, p = ~~(ev.loaded * 1000 / ev.total);
                        p = p / 10;
                        if (progress !== p)
                            progress = p;
                        _this.progress = progress;
                    };
                    xhr.send(fd);
                }
            }
        });
        /**
         * 获取文件名称，只能是名称，不能获取完整的文件目录
         *
         * @param this
         */
        function getFileName() {
            var _a;
            var v = this.$el.$('input[type=file]').value, arr = v.split('\\');
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
                }
            };
            reader.readAsDataURL(file);
        }
    })(xhr_upload = aj.xhr_upload || (aj.xhr_upload = {}));
})(aj || (aj = {}));
