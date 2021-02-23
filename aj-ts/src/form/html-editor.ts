namespace aj.form {
    /**
     * HTML 编辑器的 DOM 元素引用
     */
    interface IHtmlEditorDomRef {
        /**
         * 工具条元素
         */
        toolbarEl: HTMLElement;

        /**
         * iframe 元素对象
         */
        iframeEl: HTMLIFrameElement;

        /**
         * iframe 下的 contentWindow.document 对象
         */
        iframeDoc: Document;

        /**
         * textrea 控件对象，保存着 HTML 代码，表单提交时就读取这个 textarea.value
         */
        sourceEditor: HTMLTextAreaElement;
    }

    /**
     * HTML 在綫編輯器
     * 
     * 注意：必须提供一个 <slot> 包含有 <textarea class="hide" name="content">${info.content}</textarea>
     */
    export class HtmlEditor extends VueComponent implements FormFieldElementComponent, IHtmlEditorDomRef {
        name = "aj-form-html-editor";

        template = html`
            <div class="aj-form-html-editor">
                <ul class="toolbar" @click="onToolBarClk">
                    <li class="dorpdown">
                        <i title="字体" class="fa-font"></i>
                        <div class="fontfamilyChoser" @click="onFontfamilyChoserClk">
                            <a style="font-family: '宋体'">宋体</a>
                            <a style="font-family: '黑体'">黑体</a>
                            <a style="font-family: '楷体'">楷体</a>
                            <a style="font-family: '隶书'">隶书</a>
                            <a style="font-family: '幼圆'">幼圆</a>
                            <a style="font-family: 'Microsoft YaHei'">Microsoft YaHei</a>
                            <a style="font-family: Arial">Arial</a>
                            <a style="font-family: 'Arial Narrow'">Arial Narrow</a>
                            <a style="font-family: 'Arial Black'">Arial Black</a>
                            <a style="font-family: 'Comic Sans MS'">Comic Sans MS</a>
                            <a style="font-family: Courier">Courier</a>
                            <a style="font-family: System">System</a>
                            <a style="font-family: 'Times New Roman'">Times New Roman</a>
                            <a style="font-family: Verdana">Verdana</a>
                        </div>
                    </li>
                    <li class="dorpdown">
                        <i title="字号" class="fa-header"></i>
                        <div class="fontsizeChoser" @click="onFontsizeChoserClk">
                            <a style="font-size: xx-small; ">极小</a>
                            <a style="font-size: x-small;  ">特小</a>
                            <a style="font-size: small;    ">小</a>
                            <a style="font-size: medium;   ">中</a>
                            <a style="font-size: large;    ">大</a>
                            <a style="font-size: x-large;  ">特大</a>
                            <a style="font-size: xx-large; line-height: 140%">极大</a>
                        </div>
                    </li>
                    <li><i title="加粗" class="bold fa-bold"></i></li>
                    <li><i title="斜体" class="italic fa-italic"></i></li>
                    <li><i title="下划线" class="underline fa-underline"></i></li>
                    <li><i title="左对齐" class="justifyleft fa-align-left"></i></li>
                    <li><i title="中间对齐" class="justifycenter fa-align-center"></i></li>
                    <li><i title="右对齐" class="justifyright fa-align-right"></i></li>
                    <li><i title="数字编号" class="insertorderedlist fa-list-ol"></i></li>
                    <li><i title="项目编号" class="insertunorderedlist fa-list-ul"></i></li>
                    <li><i title="增加缩进" class="outdent fa-outdent"></i></li>
                    <li><i title="减少缩进" class="indent fa-indent"></i></li>
                    <li class="dorpdown">
                        <i title="字体颜色" class="fa-paint-brush"></i>
                        <div class="fontColor colorPicker" v-html="createColorPickerHTML()" @click="onFontColorPicker"></div>
                    </li>
                    <li class="dorpdown">
                        <i title="背景颜色" class="fa-pencil"></i>
                        <div class="bgColor colorPicker" v-html="createColorPickerHTML()" @click="onFontBgColorPicker"></div>
                    </li>
                    <li><i title="增加链接" class="createLink fa-link"></i></li>
                    <li><i title="增加图片" class="insertImage fa-file-image-o"></i></li>
                    <li><i title="一键存图" class="saveRemoteImage2Local fa-hdd-o"></i></li>
                    <li><i title="清理 HTML" class="cleanHTML fa-eraser"></i></li>
                    <li><i title="切换到代码" class="switchMode fa-code"></i></li>
                </ul>
            
                <div class="editorBody">
                    <iframe srcdoc="<html><body></body></html>"></iframe>
                    <slot></slot>
                </div>
            </div>
        `;

        props = {
            fieldName: { type: String, required: true },    // 表单 name，字段名
            uploadImageActionUrl: String                    // 图片上传路径
        };

        fieldName = "";

        fieldValue = "";

        /**
         * 图片上传路径
         */
        uploadImageActionUrl = "";

        toolbarEl: HTMLElement = document.body;

        iframeEl: HTMLIFrameElement = <HTMLIFrameElement>document.body;

        iframeDoc: Document = document;

        sourceEditor: HTMLTextAreaElement = <HTMLTextAreaElement>document.body;

        mode: 'iframe' | 'textarea' = "iframe";

        mounted(): void {
            let el = this.$el;
            this.mode = 'iframe';  // 当前可视化编辑 iframe|textarea
            this.toolbarEl = <HTMLElement>el.$('.toolbar');

            this.iframeEl = <HTMLIFrameElement>el.$('iframe');
            // 这个方法只能写在 onload 事件里面， 不写 onload 里还不执行
            (<Window>this.iframeEl.contentWindow).onload = (ev: Event) => {
                let iframeDoc = (<Window>this.iframeEl.contentWindow).document;
                iframeDoc.designMode = 'on';
                iframeDoc.addEventListener('paste', onImagePaste.bind(this));// 直接剪切板粘贴上传图片

                this.iframeDoc = iframeDoc;

                new MutationObserver((mutationsList: MutationRecord[], observer: MutationObserver) => {
                    if (this.mode === 'iframe')
                        this.sourceEditor.value = this.iframeDoc.body.innerHTML;
                }).observe(iframeDoc.body, { attributes: true, childList: true, subtree: true, characterData: true });

                this.sourceEditor.value && this.setIframeBody(this.sourceEditor.value);// 有内容
            }

            this.sourceEditor = <HTMLTextAreaElement>el.$('textarea');
            this.sourceEditor.classList.add("hide");
            this.sourceEditor.name = this.fieldName;
            this.sourceEditor.oninput = (ev: Event) => {
                if (this.mode === 'textarea')
                    this.setIframeBody(this.sourceEditor.value);
            }

            this.initImgMgr();
        }

        private uploadImgMgr: any;

        private initImgMgr(): void {
            let div: HTMLDivElement = document.body.appendChild(document.createElement('div'));
            div.innerHTML = `<aj-form-popup-upload ref="uploadLayer" upload-url="${this.uploadImageActionUrl}"></aj-form-popup-upload>`;
            this.uploadImgMgr = new Vue({
                el: div
            }).$refs.uploadLayer;
        }

        /**
        * 输入 HTML 内容
        * 
        * @param html 
        */
        setIframeBody(html: string): void {
            this.iframeDoc.body.innerHTML = html;
        }

        /**
         * 获取内容的 HTML
         * 
         * @param cleanWord 
         * @param encode 
         */
        getValue(cleanWord: boolean, encode: boolean): string {
            let result: string = this.iframeDoc.body.innerHTML;

            if (cleanWord)
                result = cleanPaste(result);

            if (encode)
                result = encodeURIComponent(result);

            return result;
        }

        /**
         * 切換 HTML 編輯 or 可視化編輯
         * 
         */
        setMode(): void {
            if (this.mode == 'iframe') {
                this.iframeEl.classList.add('hide');
                this.sourceEditor.classList.remove('hide');
                this.mode = 'textarea';
                grayImg.call(this, true);
            } else {
                this.iframeEl.classList.remove('hide');
                this.sourceEditor.classList.add('hide');
                this.mode = 'iframe';
                grayImg.call(this, false);
            }
        }

        /**
         * 当工具条点击的时候触发
         * 
         * @param ev 
         */
        onToolBarClk(ev: Event): void {
            let el: HTMLElement = <HTMLElement>ev.target,
                clsName = <string>el.className.split(' ').shift();

            switch (clsName) {
                case 'createLink':
                    let result = prompt("请输入 URL 地址");
                    if (result)
                        this.format("createLink", result);
                    break;
                case 'insertImage':
                    // @ts-ignore
                    if (window.isCreate)
                        aj.alert('请保存记录后再上传图片。');
                    else {
                        this.uploadImgMgr.show((json: ImgUploadRepsonseResult) => {
                            if (json && json.isOk)
                                this.format("insertImage", json.fullUrl);
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
        }

        /**
         * 通过 document.execCommand() 来操纵可编辑内容区域的元素
         * 
         * @param type 命令的名称
         * @param para 一些命令（例如 insertImage）需要额外的参数（insertImage 需要提供插入 image 的 url），默认为 null
         */
        format(type: string, para?: string): void {
            if (para)
                this.iframeDoc.execCommand(type, false, para);
            else
                this.iframeDoc.execCommand(type, false);

            (<Window>this.iframeEl.contentWindow).focus();
        }

        /**
         * 选择字号大小
         * 
         * @param ev
         */
        private onFontsizeChoserClk(ev: Event): void {
            let el: HTMLElement = <HTMLElement>ev.target,
                els = (<HTMLElement>ev.currentTarget).children;

            for (var i = 0, j = els.length; i < j; i++)
                if (el == els[i])
                    break;

            this.format('fontsize', i + "");
        }

        private onFontColorPicker(ev: Event): void {
            this.format('foreColor', (<HTMLElement>ev.target).title);
        }

        private onFontBgColorPicker(ev: Event): void {
            this.format('backColor', (<HTMLElement>ev.target).title);
        }

        /**
         * 选择字体
         * 
         * @param ev
         */
        private onFontfamilyChoserClk(ev: Event): void {
            let el: HTMLElement = <HTMLElement>ev.target;
            this.format('fontname', el.innerHTML);

            /* 如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：*/
            let menuPanel: HTMLElement = <HTMLElement>el.parentNode;
            menuPanel.style.display = 'none';
            setTimeout(() => menuPanel.style.display = '', 300);
        }

        /**
         * 创建颜色选择器
         */
        private createColorPickerHTML(): string {
            let cl: string[] = ['00', '33', '66', '99', 'CC', 'FF'],
                b: string, d: string, e: string, f: string,
                h: string[] = ['<div class="colorhead"><span class="colortitle">颜色选择</span></div><div class="colorbody"><table cellspaci="0" cellpadding="0"><tr>'];

            // 创建 body  [6 x 6的色盘]
            for (let i: number = 0; i < 6; ++i) {
                h.push('<td><table class="colorpanel" cellspacing="0" cellpadding="0">');

                for (var j: number = 0, a = cl[i]; j < 6; ++j) {
                    h.push('<tr>');

                    for (var k: number = 0, c = cl[j]; k < 6; ++k) {
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
        }
    }

    /**
     * 使图标变灰色
     * 
     * @param this 
     * @param isGray 
     */
    function grayImg(this: HtmlEditor, isGray: boolean): void {
        this.toolbarEl.$('i', (item: HTMLElement) => {
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
    function saveRemoteImage2Local(this: HtmlEditor): void {
        let arr: NodeListOf<HTMLImageElement> = this.iframeDoc.querySelectorAll('img'),
            remotePicArr: HTMLImageElement[] = new Array<HTMLImageElement>(),
            srcs: string[] = [];

        for (var i = 0, j = arr.length; i < j; i++) {
            let imgEl: HTMLImageElement = arr[i],
                src: string = <string>imgEl.getAttribute('src');

            if (/^http/.test(src)) {
                remotePicArr.push(imgEl);
                srcs.push(src);
            }
        }

        if (srcs.length)
            xhr.post('../downAllPics/', (json: ImgUploadRepsonseResult) => {
                let _arr: string[] = json.pics;

                for (var i = 0, j = _arr.length; i < j; i++)
                    remotePicArr[i].src = "images/" + _arr[i]; // 改变 DOM 的旧图片地址为新的

                aj.alert('所有图片下载完成。');
            }, { pics: srcs.join('|') });
        else
            aj.alert('未发现有远程图片');
    }

    /*
    * 富文本编辑器中粘贴图片时，chrome可以得到e.clipBoardData.items并从中获取二进制数据，以便ajax上传到后台，
    * 实现粘贴图片的功能。firefox中items为undefined，可选的方案：1将base64原样上传到后台进行文件存储替换，2将内容清空，待粘贴完毕后取图片src，再恢复现场
    * https://stackoverflow.com/questions/2176861/javascript-get-clipboard-data-on-paste-event-cross-browser
    */

    /**
     * 
     * @param this 
     * @param ev 
     */
    function onImagePaste(this: HtmlEditor, ev: ClipboardEvent): void {
        if (!this.uploadImageActionUrl) {
            aj.alert('未提供图片上传地址');
            return;
        }

        let items: DataTransferItemList | null = ev.clipboardData && ev.clipboardData.items,
            file: File | null = null; // file 就是剪切板中的图片文件

        if (items && items.length) {// 检索剪切板 items
            for (let i = 0; i < items.length; i++) {
                let item: DataTransferItem = items[i];

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

            img.changeBlobImageQuality(file, (newBlob: Blob) => {
                let img = <HTMLImageElement>document.body.$('.test');
                img.src = URL.createObjectURL(newBlob);
                console.log('got blob');

                this.format("insertImage", URL.createObjectURL(newBlob));
                // Vue.options.components["aj-xhr-upload"].extendOptions.methods.doUpload.call({
                //     action: this.uploadImageActionUrl,
                //     progress: 0,
                //     uploadOk_callback(j: ImgUploadRepsonseResult) {
                //         if (j.isOk)
                //             this.format("insertImage", this.ajResources.imgPerfix + j.imgUrl);
                //     },
                //     $blob: newBlob,
                //     $fileName: 'foo.jpg'
                // });
            });
        }
    }

    /**
     * Remove additional MS Word content
     * MSWordHtmlCleaners.js https://gist.github.com/ronanguilloux/2915995
     * 
     * @param html 
     */
    function cleanPaste(html: string): string {
        html = html.replace(/<(\/)*(\\?xml:|meta|link|span|font|del|ins|st1:|[ovwxp]:)((.|\s)*?)>/gi, ''); // Unwanted tags
        html = html.replace(/(class|style|type|start)=("(.*?)"|(\w*))/gi, ''); // Unwanted sttributes
        html = html.replace(/<style(.*?)style>/gi, '');   // Style tags
        html = html.replace(/<script(.*?)script>/gi, ''); // Script tags
        html = html.replace(/<!--(.*?)-->/gi, '');        // HTML comments

        return html;
    }

    new HtmlEditor().register();
}