"use strict";
;
(function () {
    Vue.component('aj-form-html-editor', {
        template: "\n            <div class=\"aj-form-html-editor\">\n                <ul class=\"toolbar\" @click=\"onToolBarClk\">\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\" class=\"switchMode fa-font\"></i>\n                        <div class=\"fontfamilyChoser\" @click=\"onFontfamilyChoserClk\">\n                            <a href=\"javascript:;\" style=\"font-family: '\u5B8B\u4F53'\">\u5B8B\u4F53</a>\n                            <a href=\"javascript:;\" style=\"font-family: '\u9ED1\u4F53'\">\u9ED1\u4F53</a>\n                            <a href=\"javascript:;\" style=\"font-family: '\u6977\u4F53'\">\u6977\u4F53</a>\n                            <a href=\"javascript:;\" style=\"font-family: '\u96B6\u4E66'\">\u96B6\u4E66</a>\n                            <a href=\"javascript:;\" style=\"font-family: '\u5E7C\u5706'\">\u5E7C\u5706</a>\n                            <a href=\"javascript:;\" style=\"font-family: 'Microsoft YaHei'\">Microsoft YaHei</a>\n                            <a href=\"javascript:;\" style=\"font-family: Arial\">Arial</a>\n                            <a href=\"javascript:;\" style=\"font-family: 'Arial Narrow'\">Arial Narrow</a>\n                            <a href=\"javascript:;\" style=\"font-family: 'Arial Black'\">Arial Black</a>\n                            <a href=\"javascript:;\" style=\"font-family: 'Comic Sans MS'\">Comic Sans MS</a>\n                            <a href=\"javascript:;\" style=\"font-family: Courier\">Courier</a>\n                            <a href=\"javascript:;\" style=\"font-family: System\">System</a>\n                            <a href=\"javascript:;\" style=\"font-family: 'Times New Roman'\">Times New Roman</a>\n                            <a href=\"javascript:;\" style=\"font-family: Verdana\">Verdana</a>\n                        </div>\n                    </li>\t\t\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u53F7\" class=\"switchMode fa-header\"></i>\n                        <div class=\"fontsizeChoser\" @click=\"onFontsizeChoserClk\">\n                            <a href=\"javascript:;\" style=\"font-size: xx-small; line-height: 120%\">\u6781\u5C0F</a>\n                            <a href=\"javascript:;\" style=\"font-size: x-small;  line-height: 120%\">\u7279\u5C0F</a>\n                            <a href=\"javascript:;\" style=\"font-size: small;    line-height: 120%\">\u5C0F</a>\n                            <a href=\"javascript:;\" style=\"font-size: medium;   line-height: 120%\">\u4E2D</a>\n                            <a href=\"javascript:;\" style=\"font-size: large;    line-height: 120%\">\u5927</a>\n                            <a href=\"javascript:;\" style=\"font-size: x-large;  line-height: 120%\">\u7279\u5927</a>\n                            <a href=\"javascript:;\" style=\"font-size: xx-large; line-height: 140%\">\u6781\u5927</a>\n                        </div>\n                    </li>\t\t\n                    <li><i title=\"\u52A0\u7C97\"         class=\"bold fa-bold\"></i></li>\t\t\n                    <li><i title=\"\u659C\u4F53\"         class=\"italic fa-italic\"></i></li>\t\t\n                    <li><i title=\"\u4E0B\u5212\u7EBF\"       class=\"underline fa-underline\"></i></li>\n                    <li><i title=\"\u5DE6\u5BF9\u9F50\"       class=\"justifyleft fa-align-left\"></i></li>\n                    <li><i title=\"\u4E2D\u95F4\u5BF9\u9F50\"     class=\"justifycenter fa-align-center\"></i></li>\n                    <li><i title=\"\u53F3\u5BF9\u9F50\"       class=\"justifyright fa-align-right\"></i></li>\n                    <li><i title=\"\u6570\u5B57\u7F16\u53F7\"     class=\"insertorderedlist fa-list-ol\"></i></li>\n                    <li><i title=\"\u9879\u76EE\u7F16\u53F7\"     class=\"insertunorderedlist fa-list-ul\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u7F29\u8FDB\"     class=\"outdent fa-outdent\"></i></li>\n                    <li><i title=\"\u51CF\u5C11\u7F29\u8FDB\"     class=\"indent fa-indent\"></i></li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\u989C\u8272\" class=\"fa-paint-brush\"></i>\n                        <div class=\"fontColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontColorPicker\"></div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u80CC\u666F\u989C\u8272\" class=\"fa-pencil\" ></i>\n                        <div class=\"bgColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontBgColorPicker\"></div>\n                    </li>\n                    <li><i title=\"\u589E\u52A0\u94FE\u63A5\"     class=\"createLink fa-link\" ></i></li>\n                    <li><i title=\"\u589E\u52A0\u56FE\u7247\"     class=\"insertImage fa-file-image-o\" ></i></li>\n                    <li><i title=\"\u4E00\u952E\u5B58\u56FE\"     class=\"saveRemoteImage2Local fa-hdd-o\"></i></li>\n                    <li><i title=\"\u6E05\u7406 HTML\"    class=\"cleanHTML fa-eraser\" ></i></li>\n                    <li><i title=\"\u5207\u6362\u5230\u4EE3\u7801\"   class=\"switchMode fa-code\"></i></li>\n                </ul>\n\n                <div class=\"editorBody\">\t\n                    <iframe :src=\"ajResources.commonAsset + '/resources/htmleditor_iframe.jsp?basePath=' + basePath\"></iframe>\n                    <slot></slot>\n                </div>\n            </div>\n        ",
        props: {
            fieldName: { type: String, required: true },
            content: { type: String, required: false },
            basePath: { type: String, required: false, default: '' },
            uploadImageActionUrl: String // 图片上传路径
        },
        mounted: function () {
            var _this = this;
            var el = this.$el;
            this.iframeEl = el.$('iframe');
            this.sourceEditor = el.$('textarea');
            this.iframeWin = this.iframeEl.contentWindow;
            this.mode = 'iframe'; // 当前可视化编辑 iframe|textarea
            this.toolbarEl = el.$('.toolbar');
            // 这个方法只能写在 onload 事件里面， 不写 onload 里还不执行
            this.iframeWin.onload = function (e) {
                _this.iframeDoc = _this.iframeWin.document;
                _this.iframeDoc.designMode = 'on';
                _this.sourceEditor.value && _this.setValue(_this.sourceEditor.value); // 有内容
                _this.iframeDoc.addEventListener('paste', onImagePaste.bind(_this)); // 直接剪切板粘贴上传图片
            };
        },
        methods: {
            /**
             * 当工具条点击的时候触发
             *
             * @param this
             * @param e
             */
            onToolBarClk: function (e) {
                var _this = this;
                var el = e.target, clsName = el.className.split(' ').shift();
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
                            // @ts-ignore
                            App.$refs.uploadLayer.show(function (json) {
                                if (json.result)
                                    json = json.result;
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
                        this.iframeDoc.body.innerHTML = HtmlSanitizer.SanitizeHtml(this.iframeDoc.body.innerHTML); // 清理冗余 HTML
                        break;
                    case 'saveRemoteImage2Local':
                        saveRemoteImage2Local.call(this);
                        break;
                    default:
                        this.format(clsName);
                }
            },
            format: function (type, para) {
                // this.iframeWin.focus();
                if (para)
                    this.iframeDoc.execCommand(type, false, para);
                else
                    this.iframeDoc.execCommand(type, false);
                this.iframeWin.focus();
            },
            insertEl: function (html) {
                this.iframeDoc.body.innerHTML = html;
            },
            /**
             * 設置 HTML
             *
             * @param v
             */
            setValue: function (v) {
                var _this = this;
                setTimeout(function () {
                    _this.iframeWin.document.body.innerHTML = v;
                    // self.iframeBody.innerHTML = v;
                }, 500);
            },
            /**
             * 获取内容的 HTML
             *
             * @param this
             * @param cleanWord
             * @param encode
             */
            getValue: function (cleanWord, encode) {
                var result = this.iframeBody.innerHTML;
                if (cleanWord)
                    result = cleanPaste(result);
                if (encode)
                    result = encodeURIComponent(result);
                return result;
            },
            /**
             * 切換 HTML 編輯 or 可視化編輯
             *
             * @param this
             */
            setMode: function () {
                if (this.mode == 'iframe') {
                    this.iframeEl.classList.add('hide');
                    this.sourceEditor.classList.remove('hide');
                    this.sourceEditor.value = this.iframeBody.innerHTML;
                    this.mode = 'textarea';
                    grayImg.call(this, true);
                }
                else {
                    this.iframeEl.classList.remove('hide');
                    this.sourceEditor.classList.add('hide');
                    this.iframeBody.innerHTML = this.sourceEditor.value;
                    this.mode = 'iframe';
                    grayImg.call(this, false);
                }
            },
            /**
             * 选择字体
             *
             * @param this
             * @param e
             */
            onFontfamilyChoserClk: function (e) {
                var el = e.target;
                this.format('fontname', el.innerHTML);
                /* 如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：*/
                var menuPanel = el.parentNode;
                menuPanel.style.display = 'none';
                setTimeout(function () { return menuPanel.style.display = ''; }, 300);
            },
            /**
             * 选择字号大小
             *
             * @param this
             * @param e
             */
            onFontsizeChoserClk: function (e) {
                var el = e.target;
                var els = e.currentTarget.children;
                for (var i = 0, j = els.length; i < j; i++)
                    if (el == els[i])
                        break;
                this.format('fontsize', i + "");
            },
            onFontColorPicker: function (e) {
                this.format('foreColor', e.target.title);
            },
            onFontBgColorPicker: function (e) {
                this.format('backColor', e.target.title);
            },
            /**
             * 创建颜色选择器
             */
            createColorPickerHTML: function () {
                // 定义变量
                var cl = ['00', '33', '66', '99', 'CC', 'FF'], b, d, e, f, T;
                // 创建 head
                var h = '<div class="colorhead"><span class="colortitle">颜色选择</span></div><div class="colorbody"><table cellspaci="0" cellpadding="0"><tr>';
                // 创建 body  [6 x 6的色盘]
                for (var i = 0; i < 6; ++i) {
                    h += '<td><table class="colorpanel" cellspacing="0" cellpadding="0">';
                    for (var j = 0, a = cl[i]; j < 6; ++j) {
                        h += '<tr>';
                        for (var k = 0, c = cl[j]; k < 6; ++k) {
                            b = cl[k];
                            e = ((k == 5 && i != 2 && i != 5) ? ';border-right:none;' : '');
                            f = ((j == 5 && i < 3) ? ';border-bottom:none' : '');
                            d = '#' + a + b + c;
                            // T = document.all ? '&nbsp;' : '';
                            h += '<td unselectable="on" style="background-color: ' + d + e + f + '" title="' + d + '"></td>';
                        }
                        h += '</tr>';
                    }
                    h += '</table></td>';
                    if (cl[i] == '66')
                        h += '</tr><tr>';
                }
                h += '</tr></table></div>';
                return h;
            }
        }
    });
    /**
     * 使图标变灰色
     *
     * @param this
     * @param isGray
     */
    function grayImg(isGray) {
        this.toolbarEl.$('span', function (item) {
            if (item.className.indexOf('switchMode') != -1)
                item.style.color = isGray ? 'red' : '';
            else
                item.style.filter = isGray ? 'grayscale(100%)' : '';
        });
    }
    function saveRemoteImage2Local() {
        var str = [], remotePicArr = new Array();
        var arr = this.iframeDoc.querySelectorAll('img');
        for (var i = 0, j = arr.length; i < j; i++) {
            var imgEl = arr[i], url = imgEl.getAttribute('src');
            if (/^http/.test(url)) {
                str.push(url);
                remotePicArr.push(imgEl);
            }
        }
        if (str.length)
            aj.xhr.post('../downAllPics/', function (json) {
                var _arr = json.result.pics;
                for (var i = 0, j = _arr.length; i < j; i++)
                    remotePicArr[i].src = "images/" + _arr[i];
                aj.alert('所有图片下载完成。');
            }, { pics: str.join('|') });
        else
            aj.alert('未发现有远程图片');
    }
    /**
     * MSWordHtmlCleaners.js https://gist.github.com/ronanguilloux/2915995
     *
     * @param html
     */
    function cleanPaste(html) {
        // Remove additional MS Word content
        html = html.replace(/<(\/)*(\\?xml:|meta|link|span|font|del|ins|st1:|[ovwxp]:)((.|\s)*?)>/gi, ''); // Unwanted tags
        html = html.replace(/(class|style|type|start)=("(.*?)"|(\w*))/gi, ''); // Unwanted sttributes
        html = html.replace(/<style(.*?)style>/gi, ''); // Style tags
        html = html.replace(/<script(.*?)script>/gi, ''); // Script tags
        html = html.replace(/<!--(.*?)-->/gi, ''); // HTML comments
        return html;
    }
    /*
    * 富文本编辑器中粘贴图片时，chrome可以得到e.clipBoardData.items并从中获取二进制数据，以便ajax上传到后台，
    * 实现粘贴图片的功能。firefox中items为undefined，可选的方案：1将base64原样上传到后台进行文件存储替换，2将内容清空，待粘贴完毕后取图片src，再恢复现场
    * https://stackoverflow.com/questions/2176861/javascript-get-clipboard-data-on-paste-event-cross-browser
    */
    /**
     *
     * @param this
     * @param event
     */
    function onImagePaste(event) {
        var _this = this;
        if (!this.uploadImageActionUrl) {
            aj.alert('未提供图片上传地址');
            return;
        }
        var items = event.clipboardData && event.clipboardData.items;
        var file = null; // file就是剪切板中的图片文件
        if (items && items.length) { // 检索剪切板items
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
            event.preventDefault();
            aj.img.changeBlobImageQuality(file, function (newBlob) {
                Vue.options.components["aj-xhr-upload"].extendOptions.methods.doUpload.call({
                    action: _this.uploadImageActionUrl,
                    progress: 0,
                    uploadOk_callback: function (j) {
                        if (j.result)
                            j = j.result;
                        this.format("insertImage", this.ajResources.imgPerfix + j.imgUrl);
                    },
                    $blob: newBlob,
                    $fileName: 'foo.jpg'
                });
            });
        }
    }
})();
