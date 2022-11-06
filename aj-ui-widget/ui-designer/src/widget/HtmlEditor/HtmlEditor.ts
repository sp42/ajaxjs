export default {
    props: {
        vModel: String,                 // 双向绑定
        uploadImageActionUrl: String,   // 图片上传路径
        isIonicons: Boolean             // 是否使用 ionicons 图标
    },
    data(): any {
        return {
            isShowCode: false,   // 是否显示 HTML 源码
            iframeEl: null,
            sourceEditor: null,
        };
    },
    mounted(): void {
        this.iframeEl = <HTMLIFrameElement>this.$el.querySelector('iframe');
        this.sourceEditor = <HTMLTextAreaElement>this.$el.querySelector('textarea');

        (<Window>this.iframeEl.contentWindow).onload = (ev: Event) => { // 这个方法只能写在 onload 事件里面， 不写 onload 里还不执行
            this.iframeDoc = (<Window>this.iframeEl.contentWindow).document;
            this.iframeDoc.designMode = 'on';
            this.iframeDoc.addEventListener('paste', onImagePaste.bind(this));// 直接剪切板粘贴上传图片

            new MutationObserver((mutationsList: MutationRecord[], observer: MutationObserver) => {// 监听DOM树变化
                if (!this.isShowCode) {
                    this.sourceEditor.value = this.iframeDoc.body.innerHTML;
                    this.$emit('on-change', this.sourceEditor.value)
                }

            }).observe(this.iframeDoc.body, { attributes: true, childList: true, subtree: true, characterData: true });

            this.vModel && this.setIframeBody(this.vModel);
            // this.sourceEditor.value && this.setIframeBody(this.sourceEditor.value);// 有内容
        }

        this.sourceEditor.oninput = (ev: Event) => {
            if (this.isShowCode && this.sourceEditor.value) {
                this.setIframeBody(this.sourceEditor.value);
                this.$emit('on-change', this.sourceEditor.value)
            }
        }

        // this.uploadImgMgr = this.$refs.uploadLayer;
    },
    methods: {
        /**
         * 输入 HTML 内容
         * 
         * @param html 
         */
        setIframeBody(html: string): void {
            if (this.iframeDoc && this.iframeDoc.body)
                this.iframeDoc.body.innerHTML = html;
        },

        /**
        * 获取内容的 HTML
        * 
        * @param cleanWord  是否清理冗余标签
        * @param encode     是否 URL 编码
        */
        getValue(cleanWord: boolean, encode: boolean): string {
            let result: string = this.iframeDoc.body.innerHTML;

            if (cleanWord)
                result = cleanPaste(result);

            if (encode)
                result = encodeURIComponent(result);

            return result;
        },

        createLink(): void {
            let result: string = prompt("请输入 URL 地址");
            if (result)
                this.format("createLink", result);
        },

        insertImage(): void {
            // @ts-ignore
            if (window.isCreate)
                alert('请保存记录后再上传图片。');
            else {
                this.uploadImgMgr.show((json: any) => {
                    if (json && json.isOk)
                        this.format("insertImage", json.fullUrl);
                });
            }
        },

        /**
         * 清理冗余 HTML
         */
        cleanHTML(): void {
            // @ts-ignore
            this.setIframeBody(HtmlSanitizer.SanitizeHtml(this.iframeDoc.body.innerHTML));
        },

        saveRemoteImage2Local(): void {
            saveRemoteImage2Local.call(this);
        },

        /**
         * 当工具条点击的时候触发
         * 
         * @param ev 
         */
        onCmdClk(ev: Event): void {
            let el: HTMLElement = <HTMLElement>ev.target,
                clsName = <string>el.className.split(' ').shift();

            this.format(clsName);
        },

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

            // (<Window>this.iframeEl.contentWindow).focus();
        },

        /**
         * 选择字号大小
         * 
         * @param ev
         */
        onFontsizeChoserClk(ev: Event): void {
            let el: HTMLElement = <HTMLElement>ev.target,
                els = (<HTMLElement>ev.currentTarget).children;

            let i: number, j: number;
            for (i = 0, j = els.length; i < j; i++)
                if (el == els[i])
                    break;

            this.format('fontsize', i + "");
        },

        /**
         * 选择字体
         * 
         * @param ev
         */
        onFontfamilyChoserClk(ev: Event): void {
            let el: HTMLElement = <HTMLElement>ev.target;
            this.format('fontname', el.innerHTML);

            /* 如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：*/
            let menuPanel: HTMLElement = <HTMLElement>el.parentNode;
            menuPanel.style.display = 'none';
            setTimeout(() => menuPanel.style.display = '', 300);
        },

        /**
         * 创建颜色选择器
         */
        createColorPickerHTML(): string {
            let cl: string[] = ['00', '33', '66', '99', 'CC', 'FF'],
                b: string, d: string, e: string, f: string,
                h: string[] = ['<div class="colorhead"><span class="colortitle">颜色选择</span></div><div class="colorbody"><table cellspaci="0" cellpadding="0"><tr>'];

            // 创建 body  [6 x 6的色盘]
            for (let i = 0; i < 6; ++i) {
                h.push('<td><table class="colorpanel" cellspacing="0" cellpadding="0">');

                for (let j = 0, a = cl[i]; j < 6; ++j) {
                    h.push('<tr>');

                    for (let k = 0, c = cl[j]; k < 6; ++k) {
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
    },

    watch: {
        vModel(newHtml: string, oldHtml: string): void {
            // if (!html)
            //     html = '';
            if (!oldHtml) // 当没有值的时候输入，就是在初始化的时候（第一次）
                this.setIframeBody(newHtml);
        },

        /**
         * 切換 HTML 編輯 or 可視化編輯
         * 
         * @param n 
         */
        isShowCode(n: boolean): void {
            if (n) {
                this.iframeEl.classList.add('hide');
                this.sourceEditor.classList.add('show');
                grayImg.call(this, true);
            } else {
                this.iframeEl.classList.remove('hide');
                this.sourceEditor.classList.remove('show');
                grayImg.call(this, false);
            }
        },
    }
};

/**
 * 粘贴图片
 * 
 * @param this 
 * @param ev 
 */
function onImagePaste(ev: ClipboardEvent): void {
    if (!this.uploadImageActionUrl) {
        alert('未提供图片上传地址');
        return;
    }

    let items: DataTransferItemList | null = ev.clipboardData && ev.clipboardData.items,
        file: File | null = null; // file 就是剪切板中的图片文件

    if (items && items.length) {// 检索剪切板 items
        for (let i = 0; i < items.length; i++) {
            const item: DataTransferItem = items[i];

            if (item.type.indexOf('image') !== -1) {
                // @ts-ignore
                if (window.isCreate) { // 有图片
                    alert('请保存记录后再上传图片。');
                    return;
                }

                file = item.getAsFile();
                break;
            }
        }
    }

    if (file) {
        // ev.preventDefault();
        // img.changeBlobImageQuality(file, (newBlob: Blob): void => {
        //     // 复用上传的方法
        //     Vue.options.components["aj-xhr-upload"].extendOptions.methods.doUpload.call({
        //         action: this.uploadImageActionUrl,
        //         progress: 0,
        //         uploadOk_callback(j: ImgUploadRepsonseResult) {
        //             if (j.isOk)
        //                 this.format("insertImage", this.ajResources.imgPerfix + j.imgUrl);
        //         },
        //         $blob: newBlob,
        //         $fileName: 'foo.jpg' // 文件名不重要，反正上传到云空间会重命名
        //     });
        // });
    }
}

/**
   * 一键存图
   * 
   * @param this 
   */
function saveRemoteImage2Local(): void {
    const arr: NodeListOf<HTMLImageElement> = this.iframeDoc.querySelectorAll('img'),
        remotePicArr: HTMLImageElement[] = new Array<HTMLImageElement>(),
        srcs: string[] = [];

    for (let i = 0, j = arr.length; i < j; i++) {
        const imgEl: HTMLImageElement = arr[i],
            src: string = <string>imgEl.getAttribute('src');

        if (/^http/.test(src)) {
            remotePicArr.push(imgEl);
            srcs.push(src);
        }
    }

    if (srcs.length) {
        // xhr.post('../downAllPics/', (json: any) => {
        //     const _arr: string[] = json.pics;

        //     for (let i = 0, j = _arr.length; i < j; i++)
        //         remotePicArr[i].src = "images/" + _arr[i]; // 改变 DOM 的旧图片地址为新的

        //     alert('所有图片下载完成。');
        // }, { pics: srcs.join('|') });
    } else
        alert('未发现有远程图片');
}

/**
 * 使图标变灰色
 * 
 * @param this 
 * @param isGray 
 */
function grayImg(isGray: boolean): void {
    this.$el.querySelectorAll('.toolbar i').forEach((item: HTMLElement) => {
        if (item.className.indexOf('switchMode') != -1) // 这个按钮永远可按，不受影响
            return;
        item.style.color = isGray ? 'lightgray' : '';
    });
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