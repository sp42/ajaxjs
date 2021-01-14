/**
 * 图片工具库
 */
namespace aj.img {
    /**
     * 限制图片大小
     * 
     * @param originWidth 
     * @param originHeight 
     * @param maxWidth 
     * @param maxHeight 
     */
    export function fitSize(originWidth: number, originHeight: number, maxWidth: number, maxHeight: number): { targetWidth: number, targetHeight: number } {
        // 目标尺寸
        let targetWidth: number = originWidth, targetHeight: number = originHeight;

        // 图片尺寸超过400x400的限制
        if (originWidth > maxWidth || originHeight > maxHeight) {
            if (originWidth / originHeight > maxWidth / maxHeight) {
                // 更宽，按照宽度限定尺寸
                targetWidth = maxWidth;
                targetHeight = Math.round(maxWidth * (originHeight / originWidth));
            } else {
                targetHeight = maxHeight;
                targetWidth = Math.round(maxHeight * (originWidth / originHeight));
            }
        }

        return { targetWidth: targetWidth, targetHeight: targetHeight };
    }

    /**
     * 图片对象转换为 Canvas
     * 
     * @param img 
     */
    export function imgObj2Canvas(img: HTMLImageElement): HTMLCanvasElement {
        let canvas: HTMLCanvasElement = document.createElement('canvas');
        canvas.width = img.width;
        canvas.height = img.height;
        canvas.getContext('2d')?.drawImage(img, 0, 0);

        return canvas;
    }

    /**
     * 输入图片远程地址，获取成功之后将其转换为 Canvas。
     * 在回调中得到 Canvas Base64 结果
     * 
     * @param imgUrl 
     * @param cb 
     * @param format 
     * @param quality 
     */
    export function imageToCanvas(imgUrl: string, cb: Function, format: string = 'image/jpeg', quality: number = .9): void {
        var img: HTMLImageElement = new Image();
        img.onload = () => {
            let canvas: HTMLCanvasElement = imgObj2Canvas(img);
            cb(canvas.toDataURL(format, quality));
        }
        img.src = imgUrl;
    }

    /**
     * 输入图片远程地址，获取成功之后将其转换为 Blob。
     * 在回调中得到 Blob 结果
     * 
     * @param imgUrl 
     * @param cb 
     */
    export function imageElToBlob(imgUrl: string, cb: Function): void {
        imageToCanvas(imgUrl, (canvas: string) => cb(dataURLtoBlob(canvas)));
    }

    /**
     * 改变 blob 图片的质量，为考虑兼容性
     * 
     * @param blob 图片对象
     * @param callback 转换成功回调，接收一个新的blob对象作为参数
     * @param format  目标格式，mime格式
     * @param quality 介于0-1之间的数字，用于控制输出图片质量，仅当格式为jpg和webp时才支持质量，png时quality参数无效
     */
    export function changeBlobImageQuality(blob: Blob, cb: Function, format: string = 'image/jpeg', quality: number = .9): void {
        let fr: FileReader = new FileReader();

        fr.onload = (_e: Event) => {
            let e: FileReaderEvent = <FileReaderEvent>_e;
            var dataURL = e.target.result;
            var img = new Image();

            img.onload = () => {
                let canvas = imgObj2Canvas(img);
                cb && cb(dataURLtoBlob(canvas.toDataURL(format, quality)));
            };

            img.src = dataURL;
        }

        fr.readAsDataURL(blob); // blob 转 dataURL
    }

    // EXIF {
    //     getTag(ob: any, ori: string):number;
    // }

    /**
     * 获取图片的方向
     * 
     * @param img 
     */
    export function getPhotoOrientation(img: HTMLImageElement): number {
        var orient: number;
        EXIF.getData(img, function () {
            //@ts-ignore
            orient = EXIF.getTag(this, 'Orientation');
        });

        //@ts-ignore
        return orient;
    }

    /**
     * 调整图片方向，返回新图片的 Base64 编码
     * 
     * @param img 
     * @param orient 
     */
    export function rotate(img: HTMLImageElement, orient: number): string {
        let width = img.width, height = img.height;
        let canvas: HTMLCanvasElement = document.createElement('canvas'), ctx: CanvasRenderingContext2D = <CanvasRenderingContext2D>canvas.getContext("2d");

        // set proper canvas dimensions before transform & export
        if ([5, 6, 7, 8].indexOf(orient) > -1) {
            canvas.width = height;
            canvas.height = width;
        } else {
            canvas.width = width;
            canvas.height = height;
        }

        switch (orient) {// transform context before drawing image
            case 2:
                ctx.transform(-1, 0, 0, 1, width, 0);
                break;
            case 3:
                ctx.transform(-1, 0, 0, -1, width, height);
                break;
            case 4:
                ctx.transform(1, 0, 0, -1, 0, height);
                break;
            case 5:
                ctx.transform(0, 1, 1, 0, 0, 0);
                break;
            case 6:
                ctx.transform(0, 1, -1, 0, height, 0);
                break;
            case 7:
                ctx.transform(0, -1, -1, 0, height, width);
                break;
            case 8:
                ctx.transform(0, -1, 1, 0, 0, width);
                break;
            default:
                ctx.transform(1, 0, 0, 1, 0, 0);
        }

        ctx.drawImage(img, 0, 0);
        return canvas.toDataURL('image/jpeg');
    }

    /**
     * 图片压缩
     * 
     * @param imgObj 
     */
    export function compress(imgObj: HTMLImageElement, vueCmp: any): void {
        let maxWidth: number = 1000, maxHeight: number = 1500;
        let fitSizeObj = fitSize(imgObj.width, imgObj.height, maxWidth, maxHeight);
        let targetWidth: number = fitSizeObj.targetWidth, targetHeight: number = fitSizeObj.targetHeight;
        let orient: number = getPhotoOrientation(imgObj);// 获取照片的拍摄方向

        if (orient == 6) {
            targetWidth = fitSizeObj.targetHeight;
            targetHeight = fitSizeObj.targetWidth;
        }

        let comp: HTMLImageElement = new Image();
        comp.onload = () => {
            let canvas: HTMLCanvasElement = document.createElement('canvas');
            canvas.width = targetWidth;
            canvas.height = targetHeight;
            canvas.getContext('2d')?.drawImage(comp, 0, 0, targetWidth, targetHeight); // 图片压缩
            canvas.toBlob(function (blob) { // canvas转为blob并上传
                vueCmp.$blob = blob;
            }, vueCmp.$fileType || 'image/jpeg');
        }

        comp.src = rotate(imgObj, orient);
    }

    /**
     * 
     * @param dataurl 
     */
    function dataURLtoBlob(dataurl: string): Blob {
        // @ts-ignore
        let arr: string[] = dataurl.split(','), mime: string = arr[0].match(/:(.*?);/)[1],
            bstr: string = atob(arr[1]),
            len: number = bstr.length, u8arr: Uint8Array = new Uint8Array(len);

        while (len--)
            u8arr[len] = bstr.charCodeAt(len);

        return new Blob([u8arr], { type: mime });
    }
}