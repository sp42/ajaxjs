; (() => {
    /**
     * HTML 在綫編輯器
     * 
     * 注意：必须提供一个 <slot> 包含有 <textarea class="hide" name="content">${info.content}</textarea>
     */
    interface HTMLEditor extends Vue, FormFieldElementComponent {
        uploadImageActionUrl: string;
        iframeEl: HTMLIFrameElement;
        iframeBody: HTMLBodyElement;
        sourceEditor: HTMLTextAreaElement;
        iframeWin: Window;
        iframeDoc: Document;
        mode: 'iframe' | 'textarea';
        toolbarEl: HTMLElement;
        format(this: HTMLEditor, type: string, para: any): void;
    }

    Vue.component('aj-form-html-editor', {
        template: `
        <div class="aj-form-html-editor">
            <ul class="toolbar" @click="onToolBarClk">
                <li class="dorpdown">
                    <span title="字体" class="bg-4"></span>
                    <div class="fontfamilyChoser" @click="onFontfamilyChoserClk">
                        <a href="javascript:;" style="font-family: '宋体'">宋体</a>
                        <a href="javascript:;" style="font-family: '黑体'">黑体</a>
                        <a href="javascript:;" style="font-family: '楷体'">楷体</a>
                        <a href="javascript:;" style="font-family: '隶书'">隶书</a>
                        <a href="javascript:;" style="font-family: '幼圆'">幼圆</a>
                        <a href="javascript:;" style="font-family: 'Microsoft YaHei'">Microsoft YaHei</a>
                        <a href="javascript:;" style="font-family: Arial">Arial</a>
                        <a href="javascript:;" style="font-family: 'Arial Narrow'">Arial Narrow</a>
                        <a href="javascript:;" style="font-family: 'Arial Black'">Arial Black</a>
                        <a href="javascript:;" style="font-family: 'Comic Sans MS'">Comic Sans MS</a>
                        <a href="javascript:;" style="font-family: Courier">Courier</a>
                        <a href="javascript:;" style="font-family: System">System</a>
                        <a href="javascript:;" style="font-family: 'Times New Roman'">Times New Roman</a>
                        <a href="javascript:;" style="font-family: Verdana">Verdana</a>
                    </div>
                </li>		
                <li class="dorpdown">
                    <span title="字号" class="bg-5" ></span>
                    <div class="fontsizeChoser" @click="onFontsizeChoserClk">
                        <a href="javascript:;" style="font-size: xx-small; line-height: 120%">极小</a>
                        <a href="javascript:;" style="font-size: x-small;  line-height: 120%">特小</a>
                        <a href="javascript:;" style="font-size: small;    line-height: 120%">小</a>
                        <a href="javascript:;" style="font-size: medium;   line-height: 120%">中</a>
                        <a href="javascript:;" style="font-size: large;    line-height: 120%">大</a>
                        <a href="javascript:;" style="font-size: x-large;  line-height: 120%">特大</a>
                        <a href="javascript:;" style="font-size: xx-large; line-height: 140%">极大</a>
                    </div>
                </li>		
                <li><span title="加粗"     class="bold bg-6"></span></li>		
                <li><span title="斜体"     class="italic bg-7"></span></li>		
                <li><span title="下划线"   class="underline bg-8"></span></li>
                <li><span title="左对齐"   class="justifyleft bg-9"></span></li>
                <li><span title="中间对齐" class="justifycenter bg-10"></span></li>
                <li><span title="右对齐"   class="justifyright bg-11"></span></li>
                <li><span title="数字编号" class="insertorderedlist bg-12"></span></li>
                <li><span title="项目编号" class="insertunorderedlist bg-13"></span></li>
                <li><span title="增加缩进" class="outdent bg-14"></span></li>
                <li><span title="减少缩进" class="indent bg-15"></span></li>
                <li class="dorpdown">
                    <span title="字体颜色" class="bg-16"></span>
                    <div class="fontColor colorPicker" v-html="createColorPickerHTML()" @click="onFontColorPicker"></div>
                </li>
                <li class="dorpdown">
                    <span title="背景颜色" class="backColor bg-17" ></span>
                    <div class="bgColor colorPicker" v-html="createColorPickerHTML()" @click="onFontBgColorPicker"></div>
                </li>
                <li> <span title="增加链接" class="createLink bg-18" ></span> </li>
                <li> <span title="增加图片" class="insertImage bg-19" ></span> </li>
                <li> <i title="一键存图" class="saveRemoteImage2Local fa-hdd-o"></i> </li>
                <li> <span title="清理冗余的 HTML" class="cleanHTML" ></span> </li>
                <li> <i class="switchMode fa-code"></i> </li>
            </ul>

            <div class="editorBody">	
                <iframe :src="ajResources.commonAsset + '/resources/htmleditor_iframe.jsp?basePath=' + basePath"></iframe>
                <slot></slot>
            </div>
        </div>
    `,
        props: {
            fieldName: { type: String, required: true },    // 表单 name，字段名
            content: { type: String, required: false },     // 内容
            basePath: { type: String, required: false, default: '' },// iframe 的 <base href="${param.basePath}/" />路徑
            uploadImageActionUrl: String                    // 图片上传路径
        },
        mounted(this: HTMLEditor): void {
            var el = this.$el;
            this.iframeEl = <HTMLIFrameElement>el.$('iframe');
            this.sourceEditor = <HTMLTextAreaElement>el.$('textarea');
            this.iframeWin = <Window>this.iframeEl.contentWindow;
            this.mode = 'iframe';                               // 当前可视化编辑 iframe|textarea
            this.toolbarEl = <HTMLElement>el.$('.toolbar');

            // 这个方法只能写在 onload 事件里面， 不写 onload 里还不执行
            this.iframeWin.onload = (e: Event) => {
                this.iframeDoc = this.iframeWin.document;
                this.iframeDoc.designMode = 'on';
                this.iframeBody = this.iframeDoc.body;
                // 有内容
                this.sourceEditor.value && this.setValue(this.sourceEditor.value);
                // 直接剪切板粘贴上传图片
                this.iframeDoc.addEventListener('paste', this.onImagePaste);
            }
        },
        methods: {
            /*
             * 富文本编辑器中粘贴图片时，chrome可以得到e.clipBoardData.items并从中获取二进制数据，以便ajax上传到后台，
             * 实现粘贴图片的功能。firefox中items为undefined，可选的方案：1将base64原样上传到后台进行文件存储替换，2将内容清空，待粘贴完毕后取图片src，再恢复现场
             * https://stackoverflow.com/questions/2176861/javascript-get-clipboard-data-on-paste-event-cross-browser
             */
            onImagePaste(this: HTMLEditor, event: ClipboardEvent): void {
                if (!this.uploadImageActionUrl) {
                    aj.alert('未提供图片上传地址');
                    return;
                }

                var items: DataTransferItemList | null = event.clipboardData && event.clipboardData.items;
                var file: File | null = null; // file就是剪切板中的图片文件

                if (items && items.length) {// 检索剪切板items
                    for (var i = 0; i < items.length; i++) {
                        let item: DataTransferItem = items[i];

                        if (item.type.indexOf('image') !== -1) {
                            if (window.isCreate) { // 有图片
                                aj.alert.show('请保存记录后再上传图片。');
                                return;
                            }

                            file = item.getAsFile();
                            break;
                        }
                    }
                }

                if (file) {
                    event.preventDefault();

                    aj.img.changeBlobImageQuality(file, (newBlob: Blob) => {
                        Vue.options.components["aj-xhr-upload"].extendOptions.methods.doUpload.call({
                            action: this.uploadImageActionUrl,
                            progress: 0,
                            uploadOk_callback(j: ImgUploadRepsonseResult) {
                                if (j.result)
                                    j = j.result;

                                this.format("insertImage", this.ajResources.imgPerfix + j.imgUrl);
                            },
                            $blob: newBlob,
                            $fileName: 'foo.jpg'
                        });
                    });
                }
            },

            /**
             * 当工具条点击的时候触发
             * 
             * @param this
             * @param e 
             */
            onToolBarClk(this: HTMLEditor, e: Event): void {
                let el: HTMLElement = <HTMLElement>e.target, clsName = el.className.split(' ').shift();

                switch (clsName) {
                    case 'createLink':
                        var result = prompt("请输入 URL 地址");
                        if (result)
                            this.format("createLink", result);
                        break;
                    case 'insertImage':
                        if (window.isCreate)
                            aj.alert.show('请保存记录后再上传图片。');
                        else {
                            App.$refs.uploadLayer.show((json: ImgUploadRepsonseResult) => {
                                if (json.result)
                                    json = json.result;
                                if (json && json.isOk)
                                    this.format("insertImage", json.fullUrl);
                            });
                        }

                        break;
                    case 'switchMode':
                        this.setMode();
                        break;
                    case 'cleanHTML':
                        this.cleanHTML();
                        break;
                    case 'saveRemoteImage2Local':
                        this.saveRemoteImage2Local();
                        break;
                    default:
                        this.format(clsName);
                }
            },

            format(this: HTMLEditor, type: string, para: any): void {
                this.iframeWin.focus();

                if (!para) {
                    if (document.all)
                        this.iframeDoc.execCommand(type);
                    else
                        this.iframeDoc.execCommand(type, false, false);
                } else
                    this.iframeDoc.execCommand(type, false, para);

                this.iframeWin.focus();
            },

            insertEl(this: HTMLEditor, html: string): void {// 重複？
                this.iframeDoc.body.innerHTML = html;
            },

            saveRemoteImage2Local(this: HTMLEditor): void {
                var str: string[] = [], remotePicArr: string[] = [], arr: NodeListOf<HTMLImageElement> = this.iframeDoc.querySelectorAll('img');

                for (var i = 0, j = arr.length; i < j; i++) {
                    let imgEl: HTMLImageElement = arr[i];
                    let url: string = <string>imgEl.getAttribute('src');

                    if (/^http/.test(url)) {
                        str.push(url);
                        remotePicArr.push(imgEl);
                    }
                }

                if (str.length)
                    aj.xhr.post('../downAllPics/', (json: RepsonseResult) => {
                        let _arr = json.result.pics;
                        for (var i = 0, j = _arr.length; i < j; i++)
                            remotePicArr[i].src = "images/" + _arr[i];

                        aj.alert.show('所有图片下载完成。');
                    }, { pics: str.join('|') });
                else
                    aj.alert.show('未发现有远程图片');
            },

            /**
             * 設置 HTML
             * 
             * @param v 
             */
            setValue(this: HTMLEditor, v: string): void {
                setTimeout(() => {
                    this.iframeWin.document.body.innerHTML = v;
                    // self.iframeBody.innerHTML = v;
                }, 500);
            },

            // 獲取 HTML
            getValue(this: HTMLEditor, cfg): string {
                var result = this.iframeBody.innerHTML;

                if (cfg && cfg.cleanWord)
                    result = this.cleanPaste(result);

                if (cfg && cfg.encode)
                    result = encodeURIComponent(result);

                return result;
            },

            // MSWordHtmlCleaners.js https://gist.github.com/ronanguilloux/2915995
            cleanPaste(html: string): string {
                // Remove additional MS Word content
                html = html.replace(/<(\/)*(\\?xml:|meta|link|span|font|del|ins|st1:|[ovwxp]:)((.|\s)*?)>/gi, ''); // Unwanted tags
                html = html.replace(/(class|style|type|start)=("(.*?)"|(\w*))/gi, ''); // Unwanted sttributes
                html = html.replace(/<style(.*?)style>/gi, '');   // Style tags
                html = html.replace(/<script(.*?)script>/gi, ''); // Script tags
                html = html.replace(/<!--(.*?)-->/gi, '');        // HTML comments

                return html;
            },

            /**
             * 切換 HTML 編輯 or 可視化編輯
             * 
             * @param this 
             */
            setMode(this: HTMLEditor): void {
                if (this.mode == 'iframe') {
                    this.iframeEl.classList.add('hide');
                    this.sourceEditor.classList.remove('hide');
                    this.sourceEditor.value = this.iframeBody.innerHTML;
                    this.mode = 'textarea';
                    grayImg.call(this, true);
                } else {
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
            onFontfamilyChoserClk(this: HTMLEditor, e: Event): void {
                let el: HTMLElement = <HTMLElement>e.target;
                this.format('fontname', el.innerHTML);

                /* 如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：*/
                let menuPanel: HTMLElement = <HTMLElement>el.parentNode;
                menuPanel.style.display = 'none';
                setTimeout(() => menuPanel.style.display = '', 300);
            },

            /**
             * 选择字号大小
             * 
             * @param this
             * @param e 
             */
            onFontsizeChoserClk(this: HTMLEditor, e: Event): void {
                let el: HTMLElement = <HTMLElement>e.target;
                let els = (<HTMLElement>e.currentTarget).children;

                for (var i = 0, j = els.length; i < j; i++)
                    if (el == els[i])
                        break;

                this.format('fontsize', i);
            },

            onFontColorPicker(this: HTMLEditor, e: Event): void {
                this.format('foreColor', (<HTMLElement>e.target).title);
            },

            onFontBgColorPicker(this: HTMLEditor, e: Event): void {
                this.format('backColor', (<HTMLElement>e.target).title);
            },

            /**
             * 创建颜色选择器
             */
            createColorPickerHTML(): string {
                // 定义变量
                var cl = ['00', '33', '66', '99', 'CC', 'FF'], a, b, c, d, e, f, i, j, k, T;
                // 创建 head
                let h = '<div class="colorhead"><span class="colortitle">颜色选择</span></div><div class="colorbody"><table cellspaci="0" cellpadding="0"><tr>';

                // 创建 body  [6 x 6的色盘]
                for (var i: number = 0; i < 6; ++i) {
                    h += '<td><table class="colorpanel" cellspacing="0" cellpadding="0">';

                    for (var j = 0, a = cl[i]; j < 6; ++j) {
                        h += '<tr>';
                        for (var k = 0, c = cl[j]; k < 6; ++k) {
                            b = cl[k];
                            e = k == 5 && i != 2 && i != 5 ? ';border-right:none;' : '';
                            f = j == 5 && i < 3 ? ';border-bottom:none' : '';
                            d = '#' + a + b + c;
                            T = document.all ? '&nbsp;' : '';
                            h += '<td unselectable="on" style="background-color: ' + d + e + f + '" title="' + d + '">' + T + '</td>';
                        }
                        h += '</tr>';
                    }

                    h += '</table></td>';
                    if (cl[i] == '66') h += '</tr><tr>';
                }

                h += '</tr></table></div>';

                return h;
            },

            /**
             * 清理冗余 HTML
             * 
             * @param this 
             */
            cleanHTML(this: HTMLEditor): void {
                this.iframeBody.innerHTML = HtmlSanitizer.SanitizeHtml(this.iframeBody.innerHTML);
            }
        }
    });

    /**
     * 使图标变灰色
     * 
     * @param this 
     * @param isGray 
     */
    function grayImg(this: HTMLEditor, isGray: boolean): void {
        this.toolbarEl.$('span', (item: HTMLElement) => {
            if (item.className.indexOf('switchMode') != -1)
                item.style.color = isGray ? 'red' : '';
            else
                item.style.filter = isGray ? 'grayscale(100%)' : '';
        });
    }

    // https://github.com/jitbit/HtmlSanitizer/blob/master/HtmlSanitizer.js
    // @ts-ignore
    var HtmlSanitizer = new (function () {
        var tagWhitelist_ = {
            'A': true, 'ABBR': true, 'B': true, 'BLOCKQUOTE': true, 'BODY': true, 'BR': true, 'CENTER': true, 'CODE': true, 'DIV': true, 'EM': true, 'FONT': true,
            'H1': true, 'H2': true, 'H3': true, 'H4': true, 'H5': true, 'H6': true, 'HR': true, 'I': true, 'IMG': true, 'LABEL': true, 'LI': true, 'OL': true, 'P': true, 'PRE': true,
            'SMALL': true, 'SOURCE': true, 'SPAN': true, 'STRONG': true, 'TABLE': true, 'TBODY': true, 'TR': true, 'TD': true, 'TH': true, 'THEAD': true, 'UL': true, 'U': true, 'VIDEO': true
        };

        var contentTagWhiteList_ = { 'FORM': true }; //tags that will be converted to DIVs
        var attributeWhitelist_ = { 'align': true, 'color': true, 'controls': true, 'height': true, 'href': true, 'src': true, 'style': false, 'target': true, 'title': true, 'type': true, 'width': true };
        var cssWhitelist_ = { 'color': true, 'background-color': true, 'font-size': true, 'text-align': true, 'text-decoration': true, 'font-weight': true };
        var schemaWhiteList_ = ['http:', 'https:', 'data:', 'm-files:', 'file:', 'ftp:']; //which "protocols" are allowed in "href", "src" etc
        var uriAttributes_ = { 'href': true, 'action': true };
        // @ts-ignore
        this.SanitizeHtml = function (input) {
            input = input.trim();
            if (input == "") return ""; //to save performance and not create iframe

            //firefox "bogus node" workaround
            if (input == "<br>") return "";

            var iframe = document.createElement('iframe');
            if (iframe['sandbox'] === undefined) {
                alert('Your browser does not support sandboxed iframes. Please upgrade to a modern browser.');
                return '';
            }

            iframe['sandbox'] = 'allow-same-origin';
            iframe.style.display = 'none';
            document.body.appendChild(iframe); // necessary so the iframe contains a document

            var iframedoc = iframe.contentDocument || iframe.contentWindow.document;
            if (iframedoc.body == null) iframedoc.write("<body></body>"); // null in IE
            iframedoc.body.innerHTML = input;

            function makeSanitizedCopy(node: Element) {
                if (node.nodeType == Node.TEXT_NODE) {
                    var newNode = node.cloneNode(true);
                } else if (node.nodeType == Node.ELEMENT_NODE && (tagWhitelist_[node.tagName] || contentTagWhiteList_[node.tagName])) {

                    //remove useless empty spans (lots of those when pasting from MS Outlook)
                    if ((node.tagName == "SPAN" || node.tagName == "B" || node.tagName == "I" || node.tagName == "U")
                        && node.innerHTML.trim() == "") {
                        return document.createDocumentFragment();
                    }

                    if (contentTagWhiteList_[node.tagName])
                        newNode = iframedoc.createElement('DIV'); //convert to DIV
                    else
                        newNode = iframedoc.createElement(node.tagName);

                    for (var i = 0; i < node.attributes.length; i++) {
                        var attr = node.attributes[i];
                        if (attributeWhitelist_[attr.name]) {
                            if (attr.name == "style") {
                                for (s = 0; s < node.style.length; s++) {
                                    var styleName = node.style[s];
                                    if (cssWhitelist_[styleName])
                                        newNode.style.setProperty(styleName, node.style.getPropertyValue(styleName));
                                }
                            }
                            else {
                                if (uriAttributes_[attr.name]) { //if this is a "uri" attribute, that can have "javascript:" or something
                                    if (attr.value.indexOf(":") > -1 && !startsWithAny(attr.value, schemaWhiteList_))
                                        continue;
                                }
                                newNode.setAttribute(attr.name, attr.value);
                            }
                        }
                    }
                    for (i = 0; i < node.childNodes.length; i++) {
                        var subCopy = makeSanitizedCopy(node.childNodes[i]);
                        newNode.appendChild(subCopy, false);
                    }
                } else {
                    newNode = document.createDocumentFragment();
                }
                return newNode;
            };

            var resultElement = makeSanitizedCopy(iframedoc.body);
            document.body.removeChild(iframe);
            return resultElement.innerHTML
                .replace(/<br[^>]*>(\S)/g, "<br>\n$1")
                .replace(/div><div/g, "div>\n<div"); //replace is just for cleaner code
        }

        function startsWithAny(str: string, substrings: string) {
            for (var i = 0; i < substrings.length; i++) {
                if (str.indexOf(substrings[i]) == 0) {
                    return true;
                }
            }

            return false;
        }

        this.AllowedTags = tagWhitelist_;
        this.AllowedAttributes = attributeWhitelist_;
        this.AllowedCssStyles = cssWhitelist_;
        this.AllowedSchemas = schemaWhiteList_;
    });

})();