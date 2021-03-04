"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
var aj;
(function (aj) {
    var xhr_upload;
    (function (xhr_upload) {
        /**
         * 属性较多，设一个抽象类
         */
        var BaseFileUploader = /** @class */ (function (_super) {
            __extends(BaseFileUploader, _super);
            function BaseFileUploader() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.fieldName = "";
                _this.fieldValue = "";
                /**
                 * 不重复的 id，用关于关联 label 与 input[type=file]
                 */
                _this.radomId = 0;
                /**
                 * 上传路径，必填
                 */
                _this.action = "";
                /**
                 * 允许文件选择器列出的文件类型
                 */
                _this.accpectFileType = "";
                /**
                 * 限制的文件扩展名，这是一个正则。如无限制，不设置或者空字符串
                 * 正则如：txt|pdf|doc
                 */
                _this.limitFileType = "";
                /**
                 * 文件大小
                 */
                _this.fileSize = 0;
                /**
                 * 获取文件名称，只能是名称，不能获取完整的文件目录
                 */
                _this.fileName = '';
                /**
                 * 文件对象，实例属性
                 */
                _this.$fileObj = null;
                /**
                 * 二进制数据，用于图片预览
                 */
                _this.$blob = null;
                /**
                 * 上传按钮是否位于下方
                 */
                _this.buttonBottom = false;
                /**
                 * 文件大小限制，单位：KB。
                 * 若为 0 则不限制
                 */
                _this.limitSize = 0;
                /**
                 * 上传进度百分比
                 */
                _this.progress = 0;
                /**
                 * 错误信息。约定：只有为空字符串，才表示允许上传。
                 */
                _this.errMsg = "init";
                /**
                 * 固定的错误结构，元素[0]为文件大小，[1]为文件类型。
                 * 如果元素非 true，表示不允许上传。
                 */
                _this.errStatus = [false, false];
                /**
                 * 成功上传之后的文件 id
                 */
                _this.newlyId = "";
                /**
                 * 上传之后的回调函数
                 */
                _this.$uploadOk_callback = function (json) {
                    if (json.isOk)
                        this.fieldValue = json.imgUrl;
                    aj.xhr.defaultCallBack(json);
                };
                return _this;
            }
            return BaseFileUploader;
        }(aj.VueComponent));
        /**
         * 文件上传器
         */
        var FileUploader = /** @class */ (function (_super) {
            __extends(FileUploader, _super);
            function FileUploader() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-file-uploader";
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-file-uploader\">\n                <input type=\"hidden\" :name=\"fieldName\" :value=\"fieldValue\" />\n                <input type=\"file\" :id=\"'uploadInput_' + radomId\" @change=\"onUploadInputChange\" :accept=\"accpectFileType\" />\n            \n                <label class=\"pseudoFilePicker\" :for=\"'uploadInput_' + radomId\">\n                    <div @drop=\"onDrop\" ondragover=\"event.preventDefault();\">\n                        <div>+</div>\u70B9\u51FB\u9009\u62E9\u6587\u4EF6<br />\u6216\u62D6\u653E\u5230\u6B64\n                    </div>\n                </label>\n            \n                <div class=\"msg\" v-if=\"errMsg != ''\">\n                    \u5141\u8BB8\u7C7B\u578B\uFF1A{{limitFileType || '\u65E0\u9650\u5236'}}\n                    <br />\n                    \u5141\u8BB8\u5927\u5C0F\uFF1A{{limitSize ? changeByte(limitSize * 1024) : '\u65E0\u9650\u5236'}}\n                    <span class=\"slot\"></span>\n                </div>\n                <div class=\"msg\" v-if=\"errMsg == ''\">\n                    {{fileName}}<div v-if=\"fileSize\">{{changeByte(fileSize)}}</div>\n                    <button @click.prevent=\"doUpload\">{{progress && progress !== 100 ? '\u4E0A\u4F20\u4E2D ' + progress + '%': '\u4E0A\u4F20'}}</button>\n                </div>\n            </div>\n        "], ["\n            <div class=\"aj-file-uploader\">\n                <input type=\"hidden\" :name=\"fieldName\" :value=\"fieldValue\" />\n                <input type=\"file\" :id=\"'uploadInput_' + radomId\" @change=\"onUploadInputChange\" :accept=\"accpectFileType\" />\n            \n                <label class=\"pseudoFilePicker\" :for=\"'uploadInput_' + radomId\">\n                    <div @drop=\"onDrop\" ondragover=\"event.preventDefault();\">\n                        <div>+</div>\u70B9\u51FB\u9009\u62E9\u6587\u4EF6<br />\u6216\u62D6\u653E\u5230\u6B64\n                    </div>\n                </label>\n            \n                <div class=\"msg\" v-if=\"errMsg != ''\">\n                    \u5141\u8BB8\u7C7B\u578B\uFF1A{{limitFileType || '\u65E0\u9650\u5236'}}\n                    <br />\n                    \u5141\u8BB8\u5927\u5C0F\uFF1A{{limitSize ? changeByte(limitSize * 1024) : '\u65E0\u9650\u5236'}}\n                    <span class=\"slot\"></span>\n                </div>\n                <div class=\"msg\" v-if=\"errMsg == ''\">\n                    {{fileName}}<div v-if=\"fileSize\">{{changeByte(fileSize)}}</div>\n                    <button @click.prevent=\"doUpload\">{{progress && progress !== 100 ? '\u4E0A\u4F20\u4E2D ' + progress + '%': '\u4E0A\u4F20'}}</button>\n                </div>\n            </div>\n        "]));
                _this.props = {
                    action: { type: String, required: true },
                    fieldName: String,
                    limitSize: { type: Number, default: 20000 },
                    limitFileType: String,
                    accpectFileType: String,
                    buttonBottom: Boolean,
                    radomId: { type: Number, default: function () { return Math.round(Math.random() * 1000); } }
                };
                _this.watch = {
                    fileName: function (newV) {
                        if (!this.limitFileType) { // 无限制，也不用检查，永远是 true
                            Vue.set(this.errStatus, 0, true);
                            return;
                        }
                        if (newV && this.limitFileType) {
                            var ext = newV.split('.').pop(); // 扩展名，fileInput.value.split('.').pop(); 也可以获取
                            if (!new RegExp(this.limitFileType, 'i').test(ext)) {
                                var msg_1 = "\u4E0A\u4F20\u6587\u4EF6\u4E3A " + newV + "\uFF0C<br />\u62B1\u6B49\uFF0C\u4E0D\u652F\u6301\u4E0A\u4F20 *." + ext + " \u7C7B\u578B\u6587\u4EF6";
                                Vue.set(this.errStatus, 0, msg_1);
                            }
                            else
                                Vue.set(this.errStatus, 0, true); // 检查通过
                        }
                    },
                    fileSize: function (newV) {
                        if (!this.limitSize) { // 无限制，也不用检查，永远是 true
                            Vue.set(this.errStatus, 1, true);
                            return;
                        }
                        if (this.limitSize && newV > this.limitSize * 1024) {
                            var msg_2 = "\u8981\u4E0A\u4F20\u7684\u6587\u4EF6\u5BB9\u91CF\u8FC7\u5927(" + this.changeByte(newV) + ")\uFF0C\u8BF7\u538B\u7F29\u5230 " + this.changeByte(this.limitSize * 1024) + " \u4EE5\u4E0B";
                            Vue.set(this.errStatus, 1, msg_2);
                        }
                        else
                            Vue.set(this.errStatus, 1, true);
                    },
                    errStatus: function (newV) {
                        var j = newV.length;
                        if (!j)
                            return;
                        var msg = "";
                        for (var i = 0; i < j; i++) {
                            var err = newV[i];
                            if (err === false)
                                return; // 未检查完，退出
                            if (err && typeof err == 'string')
                                msg += err + '；<br/>';
                        }
                        // 到这步，所有检查完毕
                        if (msg) { // 有错误
                            aj.alert(msg);
                            this.errMsg = msg;
                        }
                        else { // 全部通过，复位
                            this.errMsg = "";
                            this.errStatus = [false, false];
                        }
                    }
                };
                return _this;
            }
            /**
             * 选择文件后触发的事件
             *
             * @param ev
             */
            FileUploader.prototype.onUploadInputChange = function (ev) {
                var fileInput = ev.target;
                if (!fileInput.files || !fileInput.files[0])
                    return;
                // this.errStatus = [false, false, false];
                this.onFileGet(fileInput.files);
            };
            FileUploader.prototype.onDrop = function (ev) {
                var _a;
                ev.preventDefault(); // 阻止进行拖拽时浏览器的默认行为，即自动打开图片
                if ((_a = ev.dataTransfer) === null || _a === void 0 ? void 0 : _a.files)
                    this.onFileGet(ev.dataTransfer.files);
            };
            FileUploader.prototype.onFileGet = function (files) {
                var file = files[0], fileType = file.type;
                this.$fileObj = file;
                this.fileName = file.name;
                this.fileSize = file.size;
            };
            /**
             * 字节 Byte 转化成 KB，MB，GB
             *
             * @param limit
             */
            FileUploader.prototype.changeByte = function (limit) {
                var size = "";
                if (limit < 0.1 * 1024) // 小于 0.1KB，则转化成 B
                    size = limit.toFixed(2) + "B";
                else if (limit < 0.1 * 1024 * 1024) // 小于 0.1MB，则转化成 KB
                    size = (limit / 1024).toFixed(2) + "KB";
                else if (limit < 0.1 * 1024 * 1024 * 1024) // 小于 0.1GB，则转化成 MB
                    size = (limit / (1024 * 1024)).toFixed(2) + "MB";
                else // 其他转化成 GB
                    size = (limit / (1024 * 1024 * 1024)).toFixed(2) + "GB";
                var index = size.indexOf("."); // 获取小数点处的索引
                if (size.substr(index + 1, 2) == "00") // 获取小数点后两位的值，判断后两位是否为 00，如果是则删除 00                
                    return size.substring(0, index) + size.substr(index + 3, 2);
                return size;
            };
            /**
             * 执行上传
             *
             * @param this
             */
            FileUploader.prototype.doUpload = function () {
                // this.$uploadOk_callback({ isOk: true, msg: "ok!", imgUrl: "fdfdf" });
                // return;
                var _this = this;
                var fd = new FormData();
                if (this.$blob)
                    fd.append("file", this.$blob, this.fileName);
                else if (this.$fileObj)
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
            };
            return FileUploader;
        }(BaseFileUploader));
        xhr_upload.FileUploader = FileUploader;
        /**
         * 用于继承，获取方法句柄
         */
        xhr_upload.fileUploader = new FileUploader();
        xhr_upload.fileUploader.register();
    })(xhr_upload = aj.xhr_upload || (aj.xhr_upload = {}));
})(aj || (aj = {}));
