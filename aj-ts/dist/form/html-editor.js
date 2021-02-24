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
    var form;
    (function (form) {
        /**
         * HTML 在綫編輯器
         *
         * 注意：必须提供一个 <slot> 包含有 <textarea class="hide" name="content">${info.content}</textarea>
         */
        var HtmlEditor = /** @class */ (function (_super) {
            __extends(HtmlEditor, _super);
            function HtmlEditor() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-form-html-editor";
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-form-html-editor\">\n                <ul class=\"toolbar\" @click=\"onToolBarClk\">\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\" class=\"fa-font\"></i>\n                        <div class=\"fontfamilyChoser\" @click=\"onFontfamilyChoserClk\">\n                            <a style=\"font-family: '\u5B8B\u4F53'\">\u5B8B\u4F53</a>\n                            <a style=\"font-family: '\u9ED1\u4F53'\">\u9ED1\u4F53</a>\n                            <a style=\"font-family: '\u6977\u4F53'\">\u6977\u4F53</a>\n                            <a style=\"font-family: '\u96B6\u4E66'\">\u96B6\u4E66</a>\n                            <a style=\"font-family: '\u5E7C\u5706'\">\u5E7C\u5706</a>\n                            <a style=\"font-family: 'Microsoft YaHei'\">Microsoft YaHei</a>\n                            <a style=\"font-family: Arial\">Arial</a>\n                            <a style=\"font-family: 'Arial Narrow'\">Arial Narrow</a>\n                            <a style=\"font-family: 'Arial Black'\">Arial Black</a>\n                            <a style=\"font-family: 'Comic Sans MS'\">Comic Sans MS</a>\n                            <a style=\"font-family: Courier\">Courier</a>\n                            <a style=\"font-family: System\">System</a>\n                            <a style=\"font-family: 'Times New Roman'\">Times New Roman</a>\n                            <a style=\"font-family: Verdana\">Verdana</a>\n                        </div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u53F7\" class=\"fa-header\"></i>\n                        <div class=\"fontsizeChoser\" @click=\"onFontsizeChoserClk\">\n                            <a style=\"font-size: xx-small; \">\u6781\u5C0F</a>\n                            <a style=\"font-size: x-small;  \">\u7279\u5C0F</a>\n                            <a style=\"font-size: small;    \">\u5C0F</a>\n                            <a style=\"font-size: medium;   \">\u4E2D</a>\n                            <a style=\"font-size: large;    \">\u5927</a>\n                            <a style=\"font-size: x-large;  \">\u7279\u5927</a>\n                            <a style=\"font-size: xx-large; line-height: 140%\">\u6781\u5927</a>\n                        </div>\n                    </li>\n                    <li><i title=\"\u52A0\u7C97\" class=\"bold fa-bold\"></i></li>\n                    <li><i title=\"\u659C\u4F53\" class=\"italic fa-italic\"></i></li>\n                    <li><i title=\"\u4E0B\u5212\u7EBF\" class=\"underline fa-underline\"></i></li>\n                    <li><i title=\"\u5DE6\u5BF9\u9F50\" class=\"justifyleft fa-align-left\"></i></li>\n                    <li><i title=\"\u4E2D\u95F4\u5BF9\u9F50\" class=\"justifycenter fa-align-center\"></i></li>\n                    <li><i title=\"\u53F3\u5BF9\u9F50\" class=\"justifyright fa-align-right\"></i></li>\n                    <li><i title=\"\u6570\u5B57\u7F16\u53F7\" class=\"insertorderedlist fa-list-ol\"></i></li>\n                    <li><i title=\"\u9879\u76EE\u7F16\u53F7\" class=\"insertunorderedlist fa-list-ul\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u7F29\u8FDB\" class=\"outdent fa-outdent\"></i></li>\n                    <li><i title=\"\u51CF\u5C11\u7F29\u8FDB\" class=\"indent fa-indent\"></i></li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\u989C\u8272\" class=\"fa-paint-brush\"></i>\n                        <div class=\"fontColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontColorPicker\"></div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u80CC\u666F\u989C\u8272\" class=\"fa-pencil\"></i>\n                        <div class=\"bgColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontBgColorPicker\"></div>\n                    </li>\n                    <li><i title=\"\u589E\u52A0\u94FE\u63A5\" class=\"createLink fa-link\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u56FE\u7247\" class=\"insertImage fa-file-image-o\"></i></li>\n                    <li><i title=\"\u4E00\u952E\u5B58\u56FE\" class=\"saveRemoteImage2Local fa-hdd-o\"></i></li>\n                    <li><i title=\"\u6E05\u7406 HTML\" class=\"cleanHTML fa-eraser\"></i></li>\n                    <li><i title=\"\u5207\u6362\u5230\u4EE3\u7801\" class=\"switchMode fa-code\"></i></li>\n                </ul>\n            \n                <div class=\"editorBody\">\n                    <iframe srcdoc=\"<html><body></body></html>\"></iframe>\n                    <slot></slot>\n                </div>\n            </div>\n        "], ["\n            <div class=\"aj-form-html-editor\">\n                <ul class=\"toolbar\" @click=\"onToolBarClk\">\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\" class=\"fa-font\"></i>\n                        <div class=\"fontfamilyChoser\" @click=\"onFontfamilyChoserClk\">\n                            <a style=\"font-family: '\u5B8B\u4F53'\">\u5B8B\u4F53</a>\n                            <a style=\"font-family: '\u9ED1\u4F53'\">\u9ED1\u4F53</a>\n                            <a style=\"font-family: '\u6977\u4F53'\">\u6977\u4F53</a>\n                            <a style=\"font-family: '\u96B6\u4E66'\">\u96B6\u4E66</a>\n                            <a style=\"font-family: '\u5E7C\u5706'\">\u5E7C\u5706</a>\n                            <a style=\"font-family: 'Microsoft YaHei'\">Microsoft YaHei</a>\n                            <a style=\"font-family: Arial\">Arial</a>\n                            <a style=\"font-family: 'Arial Narrow'\">Arial Narrow</a>\n                            <a style=\"font-family: 'Arial Black'\">Arial Black</a>\n                            <a style=\"font-family: 'Comic Sans MS'\">Comic Sans MS</a>\n                            <a style=\"font-family: Courier\">Courier</a>\n                            <a style=\"font-family: System\">System</a>\n                            <a style=\"font-family: 'Times New Roman'\">Times New Roman</a>\n                            <a style=\"font-family: Verdana\">Verdana</a>\n                        </div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u53F7\" class=\"fa-header\"></i>\n                        <div class=\"fontsizeChoser\" @click=\"onFontsizeChoserClk\">\n                            <a style=\"font-size: xx-small; \">\u6781\u5C0F</a>\n                            <a style=\"font-size: x-small;  \">\u7279\u5C0F</a>\n                            <a style=\"font-size: small;    \">\u5C0F</a>\n                            <a style=\"font-size: medium;   \">\u4E2D</a>\n                            <a style=\"font-size: large;    \">\u5927</a>\n                            <a style=\"font-size: x-large;  \">\u7279\u5927</a>\n                            <a style=\"font-size: xx-large; line-height: 140%\">\u6781\u5927</a>\n                        </div>\n                    </li>\n                    <li><i title=\"\u52A0\u7C97\" class=\"bold fa-bold\"></i></li>\n                    <li><i title=\"\u659C\u4F53\" class=\"italic fa-italic\"></i></li>\n                    <li><i title=\"\u4E0B\u5212\u7EBF\" class=\"underline fa-underline\"></i></li>\n                    <li><i title=\"\u5DE6\u5BF9\u9F50\" class=\"justifyleft fa-align-left\"></i></li>\n                    <li><i title=\"\u4E2D\u95F4\u5BF9\u9F50\" class=\"justifycenter fa-align-center\"></i></li>\n                    <li><i title=\"\u53F3\u5BF9\u9F50\" class=\"justifyright fa-align-right\"></i></li>\n                    <li><i title=\"\u6570\u5B57\u7F16\u53F7\" class=\"insertorderedlist fa-list-ol\"></i></li>\n                    <li><i title=\"\u9879\u76EE\u7F16\u53F7\" class=\"insertunorderedlist fa-list-ul\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u7F29\u8FDB\" class=\"outdent fa-outdent\"></i></li>\n                    <li><i title=\"\u51CF\u5C11\u7F29\u8FDB\" class=\"indent fa-indent\"></i></li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\u989C\u8272\" class=\"fa-paint-brush\"></i>\n                        <div class=\"fontColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontColorPicker\"></div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u80CC\u666F\u989C\u8272\" class=\"fa-pencil\"></i>\n                        <div class=\"bgColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontBgColorPicker\"></div>\n                    </li>\n                    <li><i title=\"\u589E\u52A0\u94FE\u63A5\" class=\"createLink fa-link\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u56FE\u7247\" class=\"insertImage fa-file-image-o\"></i></li>\n                    <li><i title=\"\u4E00\u952E\u5B58\u56FE\" class=\"saveRemoteImage2Local fa-hdd-o\"></i></li>\n                    <li><i title=\"\u6E05\u7406 HTML\" class=\"cleanHTML fa-eraser\"></i></li>\n                    <li><i title=\"\u5207\u6362\u5230\u4EE3\u7801\" class=\"switchMode fa-code\"></i></li>\n                </ul>\n            \n                <div class=\"editorBody\">\n                    <iframe srcdoc=\"<html><body></body></html>\"></iframe>\n                    <slot></slot>\n                </div>\n            </div>\n        "]));
                _this.props = {
                    fieldName: { type: String, required: true },
                    uploadImageActionUrl: String // 图片上传路径
                };
                _this.fieldName = "";
                _this.fieldValue = "";
                /**
                 * 图片上传路径
                 */
                _this.uploadImageActionUrl = "";
                _this.toolbarEl = document.body;
                _this.iframeEl = document.body;
                _this.iframeDoc = document;
                _this.sourceEditor = document.body;
                _this.mode = "iframe";
                return _this;
            }
            HtmlEditor.prototype.mounted = function () {
                var _this = this;
                var el = this.$el;
                this.mode = 'iframe'; // 当前可视化编辑 iframe|textarea
                this.toolbarEl = el.$('.toolbar');
                this.iframeEl = el.$('iframe');
                // 这个方法只能写在 onload 事件里面， 不写 onload 里还不执行
                this.iframeEl.contentWindow.onload = function (ev) {
                    var iframeDoc = _this.iframeEl.contentWindow.document;
                    iframeDoc.designMode = 'on';
                    iframeDoc.addEventListener('paste', onImagePaste.bind(_this)); // 直接剪切板粘贴上传图片
                    _this.iframeDoc = iframeDoc;
                    new MutationObserver(function (mutationsList, observer) {
                        if (_this.mode === 'iframe')
                            _this.sourceEditor.value = _this.iframeDoc.body.innerHTML;
                    }).observe(iframeDoc.body, { attributes: true, childList: true, subtree: true, characterData: true });
                    _this.sourceEditor.value && _this.setIframeBody(_this.sourceEditor.value); // 有内容
                };
                this.sourceEditor = el.$('textarea');
                this.sourceEditor.classList.add("hide");
                this.sourceEditor.name = this.fieldName;
                this.sourceEditor.oninput = function (ev) {
                    if (_this.mode === 'textarea')
                        _this.setIframeBody(_this.sourceEditor.value);
                };
                this.initImgMgr();
            };
            HtmlEditor.prototype.initImgMgr = function () {
                var div = document.body.appendChild(document.createElement('div'));
                div.innerHTML = "<aj-form-popup-upload ref=\"uploadLayer\" upload-url=\"" + this.uploadImageActionUrl + "\"></aj-form-popup-upload>";
                this.uploadImgMgr = new Vue({
                    el: div
                }).$refs.uploadLayer;
            };
            /**
            * 输入 HTML 内容
            *
            * @param html
            */
            HtmlEditor.prototype.setIframeBody = function (html) {
                this.iframeDoc.body.innerHTML = html;
            };
            /**
             * 获取内容的 HTML
             *
             * @param cleanWord
             * @param encode
             */
            HtmlEditor.prototype.getValue = function (cleanWord, encode) {
                var result = this.iframeDoc.body.innerHTML;
                if (cleanWord)
                    result = cleanPaste(result);
                if (encode)
                    result = encodeURIComponent(result);
                return result;
            };
            /**
             * 切換 HTML 編輯 or 可視化編輯
             *
             */
            HtmlEditor.prototype.setMode = function () {
                if (this.mode == 'iframe') {
                    this.iframeEl.classList.add('hide');
                    this.sourceEditor.classList.remove('hide');
                    this.mode = 'textarea';
                    grayImg.call(this, true);
                }
                else {
                    this.iframeEl.classList.remove('hide');
                    this.sourceEditor.classList.add('hide');
                    this.mode = 'iframe';
                    grayImg.call(this, false);
                }
            };
            /**
             * 当工具条点击的时候触发
             *
             * @param ev
             */
            HtmlEditor.prototype.onToolBarClk = function (ev) {
                var _this = this;
                var el = ev.target, clsName = el.className.split(' ').shift();
                switch (clsName) {
                    case 'createLink':
                        var result = prompt("请输入 URL 地址");
                        if (result)
                            this.format("createLink", result);
                        break;
                    case 'insertImage':
                        // @ts-ignore
                        if (window.isCreate)
                            aj.alert('请保存记录后再上传图片。');
                        else {
                            this.uploadImgMgr.show(function (json) {
                                if (json && json.isOk)
                                    _this.format("insertImage", json.fullUrl);
                            });
                        }
                        break;
                    case 'switchMode':
                        this.setMode();
                        break;
                    case 'cleanHTML':
                        // @ts-ignore
                        this.setIframeBody(HtmlSanitizer.SanitizeHtml(this.iframeDoc.body.innerHTML)); // 清理冗余 HTML
                        break;
                    case 'saveRemoteImage2Local':
                        saveRemoteImage2Local.call(this);
                        break;
                    default:
                        this.format(clsName);
                }
            };
            /**
             * 通过 document.execCommand() 来操纵可编辑内容区域的元素
             *
             * @param type 命令的名称
             * @param para 一些命令（例如 insertImage）需要额外的参数（insertImage 需要提供插入 image 的 url），默认为 null
             */
            HtmlEditor.prototype.format = function (type, para) {
                if (para)
                    this.iframeDoc.execCommand(type, false, para);
                else
                    this.iframeDoc.execCommand(type, false);
                this.iframeEl.contentWindow.focus();
            };
            /**
             * 选择字号大小
             *
             * @param ev
             */
            HtmlEditor.prototype.onFontsizeChoserClk = function (ev) {
                var el = ev.target, els = ev.currentTarget.children;
                for (var i = 0, j = els.length; i < j; i++)
                    if (el == els[i])
                        break;
                this.format('fontsize', i + "");
            };
            HtmlEditor.prototype.onFontColorPicker = function (ev) {
                this.format('foreColor', ev.target.title);
            };
            HtmlEditor.prototype.onFontBgColorPicker = function (ev) {
                this.format('backColor', ev.target.title);
            };
            /**
             * 选择字体
             *
             * @param ev
             */
            HtmlEditor.prototype.onFontfamilyChoserClk = function (ev) {
                var el = ev.target;
                this.format('fontname', el.innerHTML);
                /* 如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：*/
                var menuPanel = el.parentNode;
                menuPanel.style.display = 'none';
                setTimeout(function () { return menuPanel.style.display = ''; }, 300);
            };
            /**
             * 创建颜色选择器
             */
            HtmlEditor.prototype.createColorPickerHTML = function () {
                var cl = ['00', '33', '66', '99', 'CC', 'FF'], b, d, e, f, h = ['<div class="colorhead"><span class="colortitle">颜色选择</span></div><div class="colorbody"><table cellspaci="0" cellpadding="0"><tr>'];
                // 创建 body  [6 x 6的色盘]
                for (var i = 0; i < 6; ++i) {
                    h.push('<td><table class="colorpanel" cellspacing="0" cellpadding="0">');
                    for (var j = 0, a = cl[i]; j < 6; ++j) {
                        h.push('<tr>');
                        for (var k = 0, c = cl[j]; k < 6; ++k) {
                            b = cl[k];
                            e = (k == 5 && i != 2 && i != 5) ? ';border-right:none;' : '';
                            f = (j == 5 && i < 3) ? ';border-bottom:none' : '';
                            d = '#' + a + b + c;
                            // T = document.all ? '&nbsp;' : '';
                            h.push('<td unselectable="on" style="background-color: ' + d + e + f + '" title="' + d + '"></td>');
                        }
                        h.push('</tr>');
                    }
                    h.push('</table></td>');
                    if (cl[i] == '66')
                        h.push('</tr><tr>');
                }
                h.push('</tr></table></div>');
                return h.join('');
            };
            return HtmlEditor;
        }(aj.VueComponent));
        form.HtmlEditor = HtmlEditor;
        /**
         * 使图标变灰色
         *
         * @param this
         * @param isGray
         */
        function grayImg(isGray) {
            this.toolbarEl.$('i', function (item) {
                if (item.className.indexOf('switchMode') != -1)
                    return;
                item.style.color = isGray ? 'lightgray' : '';
            });
        }
        /**
         * 一键存图
         *
         * @param this
         */
        function saveRemoteImage2Local() {
            var arr = this.iframeDoc.querySelectorAll('img'), remotePicArr = new Array(), srcs = [];
            for (var i = 0, j = arr.length; i < j; i++) {
                var imgEl = arr[i], src = imgEl.getAttribute('src');
                if (/^http/.test(src)) {
                    remotePicArr.push(imgEl);
                    srcs.push(src);
                }
            }
            if (srcs.length)
                aj.xhr.post('../downAllPics/', function (json) {
                    var _arr = json.pics;
                    for (var i = 0, j = _arr.length; i < j; i++)
                        remotePicArr[i].src = "images/" + _arr[i]; // 改变 DOM 的旧图片地址为新的
                    aj.alert('所有图片下载完成。');
                }, { pics: srcs.join('|') });
            else
                aj.alert('未发现有远程图片');
        }
        /**
         * 粘贴图片
         *
         * @param this
         * @param ev
         */
        function onImagePaste(ev) {
            var _this = this;
            if (!this.uploadImageActionUrl) {
                aj.alert('未提供图片上传地址');
                return;
            }
            var items = ev.clipboardData && ev.clipboardData.items, file = null; // file 就是剪切板中的图片文件
            if (items && items.length) { // 检索剪切板 items
                for (var i = 0; i < items.length; i++) {
                    var item = items[i];
                    if (item.type.indexOf('image') !== -1) {
                        // @ts-ignore
                        if (window.isCreate) { // 有图片
                            aj.alert('请保存记录后再上传图片。');
                            return;
                        }
                        file = item.getAsFile();
                        break;
                    }
                }
            }
            if (file) {
                ev.preventDefault();
                aj.img.changeBlobImageQuality(file, function (newBlob) {
                    // 复用上传的方法
                    Vue.options.components["aj-xhr-upload"].extendOptions.methods.doUpload.call({
                        action: _this.uploadImageActionUrl,
                        progress: 0,
                        uploadOk_callback: function (j) {
                            if (j.isOk)
                                this.format("insertImage", this.ajResources.imgPerfix + j.imgUrl);
                        },
                        $blob: newBlob,
                        $fileName: 'foo.jpg' // 文件名不重要，反正上传到云空间会重命名
                    });
                });
            }
        }
        /**
         * Remove additional MS Word content
         * MSWordHtmlCleaners.js https://gist.github.com/ronanguilloux/2915995
         *
         * @param html
         */
        function cleanPaste(html) {
            html = html.replace(/<(\/)*(\\?xml:|meta|link|span|font|del|ins|st1:|[ovwxp]:)((.|\s)*?)>/gi, ''); // Unwanted tags
            html = html.replace(/(class|style|type|start)=("(.*?)"|(\w*))/gi, ''); // Unwanted sttributes
            html = html.replace(/<style(.*?)style>/gi, ''); // Style tags
            html = html.replace(/<script(.*?)script>/gi, ''); // Script tags
            html = html.replace(/<!--(.*?)-->/gi, ''); // HTML comments
            return html;
        }
        new HtmlEditor().register();
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));
