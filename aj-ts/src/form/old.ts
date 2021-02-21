namespace aj.xhr_upload {

    /**
     * 上传组件
     */
    interface XHR_Upload extends Vue, FormFieldElementComponent {
        fileName: string;
        $fileObj: File;
        $fileName: string;
        $fileType: string;
        $blob: Blob;
        action: string;
        imgMaxWidth: number;
        imgMaxHeight: number;
        isImgUpload: boolean;
        limitSize: number;
        progress: number;
        imgBase64Str: string;
        hiddenField: string;
        uploadOk_callback: Function;
        errMsg: string;
        isImgSize: boolean;//???
        isFileSize: boolean;
        isExtName: boolean;
        limitFileType: string;
        uplodedFileUrl: string;
        readBase64(file: File): void;
    }

    Vue.component('aj-xhr-upload', {
        template: html`
            <div class="aj-xhr-upload" :style="{display: buttonBottom ? 'inherit': 'flex'}">
            
                <img class="upload_img_perview" v-if="isImgUpload"
                    :src="(isFileSize && isExtName && imgBase64Str) ? imgBase64Str : imgPlace" />
                <div class="pseudoFilePicker">
                    <label :for="'uploadInput_' + radomId">
                        <div>
                            <div>+</div>点击选择{{isImgUpload ? '图片': '文件'}}
                        </div>
                    </label>
                </div>
                <input type="file" :name="fieldName" :id="'uploadInput_' + radomId" @change="onUploadInputChange"
                    :accept="isImgUpload ? 'image/*' : accpectFileType" />
                <div v-if="!isFileSize || !isExtName">{{errMsg}}</div>
                <div v-if="isFileSize && isExtName">
                    {{fileName}}<br />
                    <button @click.prevent="doUpload">{{progress && progress !== 100 ? '上传中 ' + progress + '%': '上传'}}</button>
                </div>
            </div>
        `,
        props: {
            action: { type: String, required: true },       // 上传路径
            fieldName: String, 	                            // input name 字段名
            fieldValue: String,
            limitSize: Number,                              // 文件大小限制
            limitFileType: String,
            accpectFileType: String,                        // 可以上传类型
            isImgUpload: Boolean, 	                        // 是否只是图片上传
            imgPlace: {
                type: String, default: "data:image/svg+xml,%3Csvg class='icon' viewBox='0 0 1024 1024' xmlns='http://www.w3.org/2000/svg' width='200' height='200'%3E%3Cpath d='M304.128 456.192c48.64 0 88.064-39.424 88.064-88.064s-39.424-88.064-88.064-88.064-88.064 39.424-88.064 88.064 39.424 88.064 88.064 88.064zm0-116.224c15.36 0 28.16 12.288 28.16 28.16s-12.288 28.16-28.16 28.16-28.16-12.288-28.16-28.16 12.288-28.16 28.16-28.16z' " +
                    "fill='%23e6e6e6'/%3E%3Cpath d='M887.296 159.744H136.704C96.768 159.744 64 192 64 232.448v559.104c0 39.936 32.256 72.704 72.704 72.704h198.144L500.224 688.64l-36.352-222.72 162.304-130.56-61.44 143.872 92.672 214.016-105.472 171.008h335.36C927.232 864.256 960 832 960 791.552V232.448c0-39.936-32.256-72.704-72.704-72.704zm-138.752 71.68v.512H857.6c16.384 0 30.208 13.312 30.208 30.208v399.872L673.28 408.064l75.264-176.64zM304.64 " +
                    "792.064H165.888c-16.384 0-30.208-13.312-30.208-30.208v-9.728l138.752-164.352 104.96 124.416-74.752 79.872zm81.92-355.84l37.376 228.864-.512.512-142.848-169.984c-3.072-3.584-9.216-3.584-12.288 0L135.68 652.8V262.144c0-16.384 13.312-30.208 30.208-30.208h474.624L386.56 436.224zm501.248 325.632c0 16.896-13.312 30.208-29.696 30.208H680.96l57.344-93.184-87.552-202.24 7.168-7.68 229.888 272.896z' fill='%23e6e6e6'/%3E%3C/svg%3E"
            },		                        // 图片占位符，用户没有选定图片时候使用的图片
            imgMaxWidth: { type: Number, default: 1920 },
            imgMaxHeight: { type: Number, default: 1680 },
            buttonBottom: Boolean                           // 上传按钮是否位于下方
        },

        data(this: XHR_Upload) {
            return {
                isFileSize: false,			// 文件大小检查
                isExtName: false,			// 文件扩展名检查
                isImgSize: false, 			// 图片分辨率大小检查
                errMsg: null,				// 错误信息
                newlyId: null,				// 成功上传之后的文件 id
                radomId: Math.round(Math.random() * 1000),		// 不重复的 id
                uplodedFileUrl: null,
                uploadOk_callback: (json: ImgUploadRepsonseResult): void => {// 回调函数
                    if (json.isOk) {
                        this.uplodedFileUrl = json.imgUrl;

                        if (this.hiddenField)
                            (<HTMLInputElement>this.$el.$(`input[name=${this.hiddenField}]`)).value = json.imgUrl;
                    }

                    xhr.defaultCallBack(json);
                },
                imgBase64Str: null,			// 图片的 base64 形式，用于预览
                progress: 0,                // 上传进度
                fileName: ''                // 获取文件名称，只能是名称，不能获取完整的文件录
            };
        },

        methods: {
            /**
             * 
             * @param this 
             * @param ev 
             */
            onUploadInputChange(this: XHR_Upload, ev: Event): void {
                let fileInput: HTMLInputElement = <HTMLInputElement>ev.target;
                if (!fileInput.files || !fileInput.files[0])
                    return;

                let ext: string = <string>fileInput.value.split('.').pop(); // 扩展名

                this.$fileObj = fileInput.files[0]; // 保留引用
                this.$fileName = this.$fileObj.name;
                this.$fileType = this.$fileObj.type;
                var size: number = this.$fileObj.size;

                if (this.limitSize) {
                    this.isFileSize = size < this.limitSize;
                    this.errMsg = "要上传的文件容量过大，请压缩到 " + this.limitSize + "kb 以下";
                } else
                    this.isFileSize = true;

                if (this.limitFileType) {
                    this.isExtName = new RegExp(this.limitFileType, 'i').test(ext);
                    this.errMsg = '根据文件后缀名判断，此文件不能上传';
                } else
                    this.isExtName = true;

                readBase64.call(this, fileInput.files[0]);

                if (this.isImgUpload) {
                    var imgEl: HTMLImageElement = new Image();
                    imgEl.onload = () => {
                        if (imgEl.width > this.imgMaxWidth || imgEl.height > this.imgMaxHeight) {
                            this.isImgSize = false;
                            this.errMsg = '图片大小尺寸不符合要求哦，请重新图片吧~';
                        } else {
                            this.isImgSize = true;
                        }
                    }

                }

                getFileName.call(this);
            },

            /**
             * 执行上传
             * 
             * @param this 
             */
            doUpload(this: XHR_Upload): void {
                let fd: FormData = new FormData();

                if (this.$blob)
                    fd.append("file", this.$blob, this.$fileName);
                else
                    fd.append("file", this.$fileObj);

                let xhr: XMLHttpRequest = new XMLHttpRequest();
                //@ts-ignore
                xhr.onreadystatechange = xhr.requestHandler.delegate(null, this.uploadOk_callback, 'json');
                xhr.open("POST", this.action, true);
                xhr.onprogress = (ev: ProgressEvent) => {
                    let progress: number = 0,
                        p: number = ~~(ev.loaded * 1000 / ev.total);
                    p = p / 10;

                    if (progress !== p)
                        progress = p;

                    this.progress = progress;
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
    function getFileName(this: XHR_Upload): void {
        let v: string = (<HTMLInputElement>this.$el.$('input[type=file]')).value,
            arr: string[] = v.split('\\');

        this.fileName = <string>arr.pop()?.trim();
    }

    /**
     * 
     * @param this 
     * @param file 
     */
    function readBase64(this: XHR_Upload, file: File): void {
        let reader: FileReader = new FileReader();
        reader.onload = (_e: Event) => {
            let e: FileReaderEvent = <FileReaderEvent>_e;
            this.imgBase64Str = e.target.result;

            if (this.isImgUpload) {
                var imgEl = new Image();
                imgEl.onload = () => {
                    if (file.size > 300 * 1024)  // 大于 300k 才压缩
                        aj.img.compress(imgEl, this);

                    if (imgEl.width > this.imgMaxWidth || imgEl.height > this.imgMaxHeight) {
                        this.isImgSize = false;
                        this.errMsg = '图片大小尺寸不符合要求哦，请裁剪图片重新上传吧~';
                    } else
                        this.isImgSize = true;
                }

                imgEl.src = this.imgBase64Str;
            }
        }

        reader.readAsDataURL(file);
    }
}