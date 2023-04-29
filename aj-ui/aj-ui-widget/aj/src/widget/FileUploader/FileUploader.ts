import Empty from '../EmptyContent';

export default {
    props: {
        action: { type: String, required: false }, // 上传路径
        limitSize: { type: Number, default: 20000 }, // 文件大小限制
        limitFileType: String,
        isImgUpload: Boolean,
        isShowBtn: { type: Boolean, default: true },
        accpectFileType: {
            type: String, default() {
                if (this.isImgUpload)
                    return 'image/*';
            }
        }, // 可以上传类型 如 image/*
        buttonBottom: Boolean, // 上传按钮是否位于下方
        radomId: {
            type: Number, default() { // 不重复的 id，用关于关联 label 与 input[type=file]
                return Math.round(Math.random() * 1000);
            },
        },
    },

    data() {
        return {
            model: { children: [] },
            imgSrc: Empty.empty,
            open: false,
            allowAddNode: false,
            // isFolder : false
            fileName: '',        // 获取文件名称，只能是名称，不能获取完整的文件目录
            fileSize: 0,         // 文件大小
            progress: 0,         // 上传进度百分比
            errMsg: '',          // 错误信息。约定：只有为空字符串，才表示允许上传。
            newlyId: '',         // 成功上传之后的文件 id
            fileObj: null
        }
    },

    methods: {
        /**
         * 选择文件后触发的事件
         *
         * @param ev
         */
        onUploadInputChange(ev: Event): void {
            let fileInput: HTMLInputElement = <HTMLInputElement>ev.target;
            if (!fileInput.files || !fileInput.files[0]) return;

            // this.errStatus = [false, false, false];
            this.onFileGet(fileInput.files);

            let file: File = fileInput.files[0];
            this.fileObj = file;

            if (this.isImgUpload) {
                let reader: FileReader = new FileReader();
                reader.onload = (e) => {
                    let imgBase64: string = <string>e.target.result; // 得到了图片的 base64 编码
                    // alert(imgBase64Str);
                    this.imgSrc = imgBase64;
                }

                reader.readAsDataURL(file);
            }
        },

        /**
         * 返回 File 对象，在表单混合上传时候有用
         * @returns 
         */
        getFileObj(): File {
            return this.fileObj;
        },

        onDrop(ev: DragEvent): void {
            ev.preventDefault(); // 阻止进行拖拽时浏览器的默认行为，即自动打开图片

            if (ev.dataTransfer?.files) this.onFileGet(ev.dataTransfer.files);
        },

        onFileGet(files: FileList): void {
            let file: File = files[0],
                fileType: string = file.type;

            this.$fileObj = file;
            this.fileName = file.name;
            this.fileSize = file.size;
        },
        /**
         * 执行上传
         *
         * @param this
         */
        doUpload(): void {
            // this.$uploadOk_callback({ isOk: true, msg: "ok!", imgUrl: "fdfdf" });
            // return;

            let fd: FormData = new FormData();

            if (this.$blob) fd.append("file", this.$blob, this.fileName);
            else if (this.$fileObj) fd.append("file", this.$fileObj);

            let xhr: XMLHttpRequest = new XMLHttpRequest();
            //@ts-ignore
            xhr.onreadystatechange = aj.xhr.requestHandler.delegate(
                null,
                this.$uploadOk_callback,
                "json"
            );
            xhr.open("POST", this.action, true);
            xhr.onprogress = (ev: ProgressEvent) => {
                let progress: number = 0,
                    p: number = ~~((ev.loaded * 1000) / ev.total);
                p = p / 10;

                if (progress !== p) progress = p;

                this.progress = progress;
            };

            xhr.send(fd);
        },

        changeByte: changeByte
    }
}

/**
 * 字节 Byte 转化成 KB，MB，GB
 *
 * @param limit
 */
function changeByte(limit: number): string {
    let size: string = "";

    if (limit < 0.1 * 1024)
        // 小于 0.1KB，则转化成 B
        size = limit.toFixed(2) + "B";
    else if (limit < 0.1 * 1024 * 1024)
        // 小于 0.1MB，则转化成 KB
        size = (limit / 1024).toFixed(2) + "KB";
    else if (limit < 0.1 * 1024 * 1024 * 1024)
        // 小于 0.1GB，则转化成 MB
        size = (limit / (1024 * 1024)).toFixed(2) + "MB";
    // 其他转化成 GB
    else size = (limit / (1024 * 1024 * 1024)).toFixed(2) + "GB";

    let index = size.indexOf("."); // 获取小数点处的索引

    if (size.substr(index + 1, 2) == "00")
        // 获取小数点后两位的值，判断后两位是否为 00，如果是则删除 00
        return size.substring(0, index) + size.substr(index + 3, 2);

    return size;
}