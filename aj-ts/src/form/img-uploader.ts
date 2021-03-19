namespace aj.xhr_upload {
    /**
     * 图片占位符，用户没有选定图片时候使用的图片
     */
    const emptyImg: string = "data:image/svg+xml,%3Csvg class='icon' viewBox='0 0 1024 1024' xmlns='http://www.w3.org/2000/svg' width='200' height='200'%3E%3Cpath d='M304.128 456.192c48.64 0 88.064-39.424 88.064-88.064s-39.424-88.064-88.064-88.064-88.064 39.424-88.064 88.064 39.424 88.064 88.064 88.064zm0-116.224c15.36 0 28.16 12.288 28.16 28.16s-12.288 28.16-28.16 28.16-28.16-12.288-28.16-28.16 12.288-28.16 28.16-28.16z' " +
        "fill='%23e6e6e6'/%3E%3Cpath d='M887.296 159.744H136.704C96.768 159.744 64 192 64 232.448v559.104c0 39.936 32.256 72.704 72.704 72.704h198.144L500.224 688.64l-36.352-222.72 162.304-130.56-61.44 143.872 92.672 214.016-105.472 171.008h335.36C927.232 864.256 960 832 960 791.552V232.448c0-39.936-32.256-72.704-72.704-72.704zm-138.752 71.68v.512H857.6c16.384 0 30.208 13.312 30.208 30.208v399.872L673.28 408.064l75.264-176.64zM304.64 " +
        "792.064H165.888c-16.384 0-30.208-13.312-30.208-30.208v-9.728l138.752-164.352 104.96 124.416-74.752 79.872zm81.92-355.84l37.376 228.864-.512.512-142.848-169.984c-3.072-3.584-9.216-3.584-12.288 0L135.68 652.8V262.144c0-16.384 13.312-30.208 30.208-30.208h474.624L386.56 436.224zm501.248 325.632c0 16.896-13.312 30.208-29.696 30.208H680.96l57.344-93.184-87.552-202.24 7.168-7.68 229.888 272.896z' fill='%23e6e6e6'/%3E%3C/svg%3E";

    const emptyImg2 = `data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALMAAAB2CAYAAACDMaL0AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA1xpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDM0MiwgMjAxMC8wMS8xMC0xODowNjo0MyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo5RkM5NjQxRDQxQUNFNDExOTcwMkU4MjNEOTc3MDU5RiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo4QTc3NDNFQkFDNDIxMUU0QTEzMkQyOUQzOTFFQkMzRCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo4QTc3NDNFQUFDNDIxMUU0QTEzMkQyOUQzOTFFQkMzRCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6OUZDOTY0MUQ0MUFDRTQxMTk3MDJFODIzRDk3NzA1OUYiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6OUZDOTY0MUQ0MUFDRTQxMTk3MDJFODIzRDk3NzA1OUYiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz77Tka5AAADFElEQVR42uzd0W3aUBiAUVNFlTqBO4VRn/scT1FvQaaAKWCK0hX6FKZghb44Rr2uLGqCL+AY7HMkqyitSHC+XP5riDoryzIZs91ulzAJ5SfngJGYiZnREDNiBjGDmEHMiBnEDGIGMYOYETOIGcQMYkbMIGYQM4gZxIyYQcwgZhAzYgYxg5hBzPDP0wQeYzmR7+Wf6vhdHd+tzDy6z9Xxzco8HZvq2I/sMaXVUYTbX8Q8DasQ8+vIHlcWfkAXNoDTsR9hyMlIH5OYETPjeNYRs1OAmEHM0I8np+BuHK4Xz8PtrdNhZX5UeXUsq+NndfwIt3Onxcr8iCvyIeD6Vbyi8XErtJX54Vbl4sTfFU6PlTlWFv58HfBzt4Xs+rGYo9XheGlYzA9tEebTe2RlNjNHbb6yls3XR/qV/H0337GNDaCYY1flonH7eYCvYRtW4GbQqzDyGHuMGZ3n5PTE1YWPXhFfGnP7vhE0Yu50BeG5Zayogxri6X0jR2PGpaty8c4cfYtX3/LEb3+IuecrA3mH0K+ZnbPG/J0mXvgwZvQ8XpxbMesIY5766xU9O7r/1UBzuJhHbB05huxDoF2u9ebv/JAsGlct+n7WMWbQatFxREg7rvbGDTEPqstmMO0Q8iKMH5lTKuYhV+dzm8E84r68R1nMgyvOrMwx+gg69S0Sc9cV9dSIEBvmMtzPreOzARRzVNBt4WZJ/Asjyws3g0YUMd/UrYJKIzeD9fXrwiZSzLewPArpmqgOq/k84t9n4fOvQ9hWaTHfdDM4D4H1vdLnJ36o7vmXCgbhN03iV+f67ZrXhrQO93WwPTOSLFu+jnrjZ/MXzMpy3P9Lwm63u/cH+JL8/+b841k55hlgZsxgyNW+nqOzDqsyxoy71nwT1NcwdvRxPVrMfPhMnlwwXojZKbjbsQMzM2IGMYOYQcwgZsQMYgYxg5gRMzy+0b+fGTGDmEHMIGYQM2IGMYOYQcyIGcQMYgYxg5gRM4gZxAxiRswgZhAziBnEjJhBzCBmEDNiBjGDmEHMIGbEDGIGMYOYmaY3AQYAXojZOq5J9RcAAAAASUVORK5CYII=`

    // 文件头判别，看看是否为图片
    const imgHeader: StringJsonParam = { "jpeg": "/9j/4", "gif": "R0lGOD", "png": "iVBORw" };

    export class ImgFileUploader extends FileUploader {
        name = "aj-img-uploder";

        template = `<div class="aj-img-uploader">
                <img :src="imgSrc" />${fileUploader.template}
            </div>`;

        /**
         * 预览图像显示的内容，可以是图片的 url 也可以是图片的 base64 形式
         */
        imgSrc: string = emptyImg2;

        propsFactory(): { [key: string]: any } {
            let p = aj.apply({
                imgMaxWidth: { type: Number, default: 1920 },
                imgMaxHeight: { type: Number, default: 1680 },
                imgPerfix: { type: String }
            }, this.props);

            p.accpectFileType = { type: String, default: "image/*" };
            p.limitFileType = { type: String, default: 'jpg|png|gif|jpeg' };

            return p
        }

        imgMaxWidth: number = 0;
        imgMaxHeight: number = 0;

        /**
         * 图片前缀
         */
        imgPerfix: string = "";

        // onUploadInputChange(ev: Event): void {
        //     super.onUploadInputChange(ev);
        //     // fileUploader.onUploadInputChange.call(this, ev);
        //     this.readBase64();
        // }

        beforeCreate() {
            this.$options.template = this.$options.template.replace('<span class="slot"></span>', '<br />最大尺寸：{{imgMaxWidth}}x{{imgMaxHeight}}');
        }

        mounted() {
            if (this.fieldValue)
                this.imgSrc = this.imgPerfix + this.fieldValue;

            let imgEl = <HTMLImageElement>this.$el.$('img');
            imgEl.onload = () => {
                if (imgEl.width > this.imgMaxWidth || imgEl.height > this.imgMaxHeight) {
                    this.errMsg = '图片大小尺寸不符合要求哦，请裁剪图片重新上传吧~';
                }

                // if (this.fileSize > 300 * 1024)  // 大于 300k 才压缩
                //     img.compressAsBlob(imgEl, (blob: Blob): void => {
                //         this.$blob = blob;
                //     });
            }
        }

        watchFactory() {
            return {
                imgBase64Str(this: ImgFileUploader, newV: string): void {
                    // 文件头判别，看看是否为图片
                    let isPic: boolean = false;

                    for (var i in imgHeader) {
                        if (~newV.indexOf(imgHeader[i])) {
                            isPic = true;
                            break;
                        }
                    }

                    if (!isPic) {
                        let msg = '亲，改了扩展名我还能认得你不是图片哦'
                        Vue.set(this.errStatus, 2, msg);
                        aj.alert(msg);
                    } else
                        Vue.set(this.errStatus, 2, "");
                },
                errMsg(this: ImgFileUploader, newV: string): void {
                    if (!newV) // 没有任何错误才显示图片
                        this.readBase64();
                }
            }
        }

        private readBase64(): void {
            let reader: FileReader = new FileReader();
            reader.onload = (ev: ProgressEvent<FileReader>) => {
                let fileReader: FileReader = <FileReader>ev.target;
                this.imgSrc = <string>fileReader.result;
            }

            this.$fileObj && reader.readAsDataURL(this.$fileObj);
        }
    }

    new ImgFileUploader().register();
}