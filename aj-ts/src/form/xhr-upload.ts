; (() => {
    /**
     * 上传组件
     */
    interface XHR_Upload extends Vue {
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
        template: `
            <div class="aj-xhr-upload" :style="{display: buttonBottom ? 'inherit': 'flex'}">
                <input v-if="hiddenField" type="hidden" :name="hiddenField" :value="hiddenFieldValue" />
                <div v-if="isImgUpload">
                    <a :href="imgPlace" target="_blank">
                        <img class="upload_img_perview" :src="(isFileSize && isExtName && imgBase64Str) ? imgBase64Str : imgPlace" />
                    </a>
                </div>
                <div class="pseudoFilePicker">
                    <label :for="'uploadInput_' + radomId"><div><div>+</div>点击选择{{isImgUpload ? '图片': '文件'}}</div></label>
                </div>
                <input type="file" :name="fieldName" class="hide" :id="'uploadInput_' + radomId" 
                    @change="onUploadInputChange" :accept="isImgUpload ? 'image/*' : accpectFileType" />
                <div v-if="!isFileSize || !isExtName">{{errMsg}}</div>
                <div v-if="isFileSize && isExtName">
                    {{fileName}}<br />
                    <button @click.prevent="doUpload" style="min-width:110px;">{{progress && progress !== 100 ? '上传中 ' + progress + '%': '上传'}}</button>
                </div>
            </div>    
        `,
        props: {
            action: { type: String, required: true },       // 上传路径
            fieldName: String, 	                            // input name 字段名
            limitSize: Number,                              // 文件大小限制
            hiddenField: { type: String, default: null },   // 上传后的文件名保存在这个隐藏域之中
            hiddenFieldValue: String,
            limitFileType: String,
            accpectFileType: String,                        // 可以上传类型
            isImgUpload: Boolean, 	                        // 是否只是图片上传
            imgPlace: String,		                        // 图片占位符，用户没有选定图片时候使用的图片
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
                            (<HTMLInputElement>this.$el.$('input[name=' + this.hiddenField + ']')).value = json.imgUrl;
                    }

                    aj.xhr.defaultCallBack(json);
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
             * @param $event 
             */
            onUploadInputChange(this: XHR_Upload, $event: Event): void {
                var fileInput: HTMLInputElement = <HTMLInputElement>$event.target;
                if (!fileInput.files || !fileInput.files[0])
                    return;

                var ext: string = <string>fileInput.value.split('.').pop(); // 扩展名

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
                xhr.onreadystatechange = aj.xhr.requestHandler.delegate(null, this.uploadOk_callback, 'json');
                xhr.open("POST", this.action, true);
                xhr.onprogress = (e: ProgressEvent) => {
                    let progress: number = 0, p: number = ~~(e.loaded * 1000 / e.total);
                    p = p / 10;

                    if (progress !== p)
                        progress = p;

                    this.progress = progress;
                };

                xhr.send(fd);
            }
        }
    });

    // 文件头判别，看看是否为图片
    const imgHeader: { [key: string]: string } = { "jpeg": "/9j/4", "gif": "R0lGOD", "png": "iVBORw" };

    /**
     * 获取文件名称，只能是名称，不能获取完整的文件目录
     * 
     * @param this 
     */
    function getFileName(this: XHR_Upload): void {
        let v: string = (<HTMLInputElement>this.$el.$('input[type=file]')).value;
        let arr: string[] = v.split('\\');

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

                // 文件头判别，看看是否为图片
                for (var i in imgHeader) {
                    if (~this.imgBase64Str.indexOf(imgHeader[i])) {
                        this.isExtName = true;
                        return;
                    }
                }

                this.errMsg = "亲，改了扩展名我还能认得你不是图片哦";
            }
        }

        reader.readAsDataURL(file);
    }
})();