namespace aj.xhr_upload {
    /**
     * 定义错误状态，可以为 boolean = true 表示通过，没有错误； string 的时候表示有错误，为具体的异常信息
     */
    type ErrStatus = boolean | string;

    /**
     * 属性较多，设一个抽象类
     */
    abstract class BaseFileUploader extends VueComponent implements FormFieldElementComponent {
        fieldName: string = "";
        fieldValue: string = "";

        /**
         * 不重复的 id，用关于关联 label 与 input[type=file]
         */
        radomId: number = 0;

        /**
         * 上传路径，必填
         */
        action: string = "";

        /**
         * 运行上传的类型
         */
        accpectFileType: string = "";

        /**
         * 限制的文件扩展名，这是一个正则。如无限制，不设置或者空字符串
         * 正则如：txt|pdf|doc
         */
        limitFileType: string = "";

        /**
         * 文件大小
         */
        fileSize: number = 0;

        /**
         * 获取文件名称，只能是名称，不能获取完整的文件目录
         */
        fileName: string = '';

        /**
         * 文件对象，实例属性
         */
        $fileObj: File | null = null;

        /**
         * 二进制数据，用于图片预览
         */
        $blob: Blob | null = null;

        /**
         * 上传按钮是否位于下方
         */
        buttonBottom = false;

        /**
         * 文件大小限制，单位：KB。
         * 若为 0 则不限制
         */
        limitSize: number = 0;

        /**
         * 上传进度百分比
         */
        progress: number = 0;

        /**
         * 错误信息。约定：只有为空字符串，才表示允许上传。
         */
        errMsg: string = "init";

        /**
         * 固定的错误结构，元素[0]为文件大小，[1]为文件类型。
         * 如果元素非 true，表示不允许上传。
         */
        errStatus: ErrStatus[] = [false, false];

        /**
         * 成功上传之后的文件 id
         */
        newlyId: string = "";

        /**
         * 上传之后的回调函数
         */
        $uploadOk_callback: Function = function (this: FileUploader, json: ImgUploadRepsonseResult) {
            if (json.isOk)
                this.fieldValue = json.imgUrl;

            xhr.defaultCallBack(json);
        }
    }

    /**
     * 文件上传器
     */
    export class FileUploader extends BaseFileUploader {
        name: string = "aj-file-uploader";

        template = html`
            <div class="aj-file-uploader">
                <input type="hidden" :name="fieldName" :value="fieldValue" />
                <input type="file" :id="'uploadInput_' + radomId" @change="onUploadInputChange" :accept="accpectFileType" />
            
                <label class="pseudoFilePicker" :for="'uploadInput_' + radomId">
                    <div @drop="onDrop" ondragover="event.preventDefault();">
                        <div>+</div>点击选择文件<br />或拖放到此
                    </div>
                </label>
            
                <div class="msg" v-if="errMsg != ''">
                    允许类型：{{limitFileType || '无限制'}}
                    <br />
                    允许大小：{{limitSize ? changeByte(limitSize * 1024) : '无限制'}}
                </div>
                <div class="msg" v-if="errMsg == ''">
                    {{fileName}}<div v-if="fileSize">{{changeByte(fileSize)}}</div>
                    <button @click.prevent="doUpload">{{progress && progress !== 100 ? '上传中 ' + progress + '%': '上传'}}</button>
                </div>
            </div>
        `
        props = {
            action: { type: String, required: true },       // 上传路径
            fieldName: String, 	                            // input name 字段名
            limitSize: { type: Number, default: 20000 },    // 文件大小限制
            limitFileType: String,
            accpectFileType: String,                        // 可以上传类型
            buttonBottom: Boolean,                          // 上传按钮是否位于下方
            radomId: { type: Number, default() { return Math.round(Math.random() * 1000) } }
        };

        /**
         * 选择文件后触发的事件
         * 
         * @param ev 
         */
        onUploadInputChange(ev: Event): void {
            let fileInput: HTMLInputElement = <HTMLInputElement>ev.target;
            if (!fileInput.files || !fileInput.files[0])
                return;

            // this.errStatus = [false, false, false];
            this.onFileGet(fileInput.files);
        }

        onDrop(ev: DragEvent): void {
            ev.preventDefault();// 阻止进行拖拽时浏览器的默认行为，即自动打开图片

            if (ev.dataTransfer?.files)
                this.onFileGet(ev.dataTransfer.files);
        }

        onFileGet(files: FileList): void {
            let file: File = files[0],
                fileType: string = file.type;

            this.$fileObj = file;
            this.fileName = file.name;
            this.fileSize = file.size;
        }

        watch = {
            fileName(this: FileUploader, newV: string): void {
                if (!this.limitFileType) {   // 无限制，也不用检查，永远是 true
                    Vue.set(this.errStatus, 0, true);
                    return;
                }

                if (newV && this.limitFileType) {
                    let ext = <string>newV.split('.').pop(); // 扩展名，fileInput.value.split('.').pop(); 也可以获取

                    if (!new RegExp(this.limitFileType, 'i').test(ext)) {
                        let msg: string = `上传文件为 ${newV}，<br />抱歉，不支持上传 *.${ext} 类型文件`;
                        Vue.set(this.errStatus, 0, msg);
                    } else
                        Vue.set(this.errStatus, 0, true); // 检查通过
                }
            },

            fileSize(this: FileUploader, newV: number): void {
                if (!this.limitSize) {   // 无限制，也不用检查，永远是 true
                    Vue.set(this.errStatus, 1, true);
                    return;
                }

                if (this.limitSize && newV > this.limitSize * 1024) {
                    let msg: string = `要上传的文件容量过大(${this.changeByte(newV)})，请压缩到 ${this.changeByte(this.limitSize * 1024)} 以下`;
                    Vue.set(this.errStatus, 1, msg);
                } else
                    Vue.set(this.errStatus, 1, true);
            },

            errStatus(this: FileUploader, newV: ErrStatus[]): void {
                let j = newV.length;
                if (!j)
                    return;

                let msg: string = "";

                for (let i = 0; i < j; i++) {
                    let err: ErrStatus = newV[i];
                    if (err === false)
                        return; // 未检查完，退出

                    if (err && typeof err == 'string')
                        msg += err + '；<br/>';
                }

                // 到这步，所有检查完毕
                if (msg) {       // 有错误
                    alert(msg);
                    this.errMsg = msg;
                } else {         // 全部通过，复位
                    this.errMsg = "";
                    this.errStatus = [false, false];
                }
            }
        };

        /**
         * 字节 Byte 转化成 KB，MB，GB
         * 
         * @param limit 
         */
        private changeByte(limit: number): string {
            let size: string = "";

            if (limit < 0.1 * 1024)                             // 小于 0.1KB，则转化成 B
                size = limit.toFixed(2) + "B";
            else if (limit < 0.1 * 1024 * 1024)                 // 小于 0.1MB，则转化成 KB
                size = (limit / 1024).toFixed(2) + "KB";
            else if (limit < 0.1 * 1024 * 1024 * 1024)          // 小于 0.1GB，则转化成 MB
                size = (limit / (1024 * 1024)).toFixed(2) + "MB";
            else                                                // 其他转化成 GB
                size = (limit / (1024 * 1024 * 1024)).toFixed(2) + "GB";

            let index = size.indexOf(".");                      // 获取小数点处的索引

            if (size.substr(index + 1, 2) == "00")              // 获取小数点后两位的值，判断后两位是否为 00，如果是则删除 00                
                return size.substring(0, index) + size.substr(index + 3, 2)

            return size;
        }

        /**
         * 执行上传
         * 
         * @param this 
         */
        doUpload(): void {
            // this.$uploadOk_callback({ isOk: true, msg: "ok!", imgUrl: "fdfdf" });
            // return;

            let fd: FormData = new FormData();

            if (this.$blob)
                fd.append("file", this.$blob, this.fileName);
            else if (this.$fileObj)
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

    /**
     * 用于继承，获取方法句柄
     */
    export let fileUploader: FileUploader = new FileUploader();
    fileUploader.register();
}