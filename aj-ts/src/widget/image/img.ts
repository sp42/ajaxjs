/**
 * 图片工具库
 */
namespace aj.img {
    /**
     * 得到 Blob 二进制数据时候的回调函数
     */
    type gotBlob = (blob: Blob) => void;

    /**
     * 为了避免压缩图片变形，一般采用等比缩放，
     * 首先要计算出原始图片宽高比 aspectRatio，
     * 用户设置的高乘以 aspectRatio，得出等比缩放后的宽，
     * 若比用户设置宽的小，则用户设置的高为为基准缩放，否则以宽为基准缩放。
     * 
     * @param originWidth 
     * @param originHeight 
     * @param maxWidth 
     * @param maxHeight 
     */
    export function fitSize(originWidth: number, originHeight: number, maxWidth: number, maxHeight: number): { targetWidth: number, targetHeight: number } {
        // 目标尺寸
        let targetWidth: number = originWidth,
            targetHeight: number = originHeight;

        // 图片尺寸超过的限制
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

        return { targetWidth, targetHeight };
    }

    /**
     * 图片对象转换为 Canvas
     * 
     * @param img 
     * @param targetWidth 
     * @param targetHeigh 
     */
    export function imgObj2Canvas(img: HTMLImageElement, targetWidth?: number, targetHeigh?: number): HTMLCanvasElement {
        let canvas: HTMLCanvasElement = document.createElement('canvas');
        canvas.width = targetWidth || img.width;
        canvas.height = targetHeigh || img.height;
        canvas.getContext('2d')?.drawImage(img, 0, 0);

        return canvas;
    }

    /**
    * Canvas.toDataURL() 也能调整图片大小（压缩图片），但不推荐
    * 参见：https://www.zhihu.com/question/59267048
    * 除了作为转换图片的 base64 编码的时候
    * 
    * 推荐使用 reszieAsBlob()
    * 
    * @param img 
    * @param targetWidth 
    * @param targetHeight 
    * @param format         目标格式，mime 格式
    * @param quality        介于 0-1 之间的数字，用于控制输出图片质量，仅当格式为 jpg 和 webp 时才支持质量，png 时 quality 参数无效
    */
    export function reszieCompressCompressAsDataURL(img: HTMLImageElement, targetWidth: number, targetHeight: number, format: string = 'image/jpeg', quality: number = .9): string {
        return imgObj2Canvas(img, targetWidth, targetHeight).toDataURL(format, quality);
    }

    /**
     * 转换图片为 base64 编码
     * 如果你要展示 blob 图片，更推荐用 img.src = URL.createObjectURL(newBlob); 方法，这个 base64 性能耗损比较大
     * 
     * @param img 
     */
    export function img2base64(img: HTMLImageElement): string {
        return reszieCompressCompressAsDataURL(img, img.width, img.height);
    }

    /**
     * 调整图片大小（压缩图片）异步方法
     * 
     * @param img 
     * @param targetWidth 
     * @param targetHeight 
     * @param cb 
     * @param format         目标格式，mime 格式
     * @param quality        介于 0-1 之间的数字，用于控制输出图片质量，仅当格式为 jpg 和 webp 时才支持质量，png 时 quality 参数无效
     */
    export function reszieCompressAsBlob(img: HTMLImageElement, targetWidth: number, targetHeight: number, cb: gotBlob, format: string = 'image/jpeg', quality: number = .9): void {
        imgObj2Canvas(img, targetWidth, targetHeight).toBlob((blob: Blob | null) => blob && cb(blob), format, quality);
    }

    export function compressAsBlob(img: HTMLImageElement, cb: gotBlob, format: string = 'image/jpeg', quality: number = .9): void {
        return reszieCompressAsBlob(img, img.width, img.height, cb, format, quality);
    }


    /**
     * 对粘贴板的图片，都是原始 bitmap 比较大，压缩一下
     * 
     * @param blob  图片对象
     * @param cb    转换成功回调，接收一个新的 blob 对象作为参数
     */
    export function changeBlobImageQuality(blob: Blob, cb: gotBlob): void {
        let img: HTMLImageElement = new Image();
        img.onload = () => compressAsBlob(img, cb);
        // 也可以用 FileReader 转 base64，但 img 实际支持 blob 加载的
        img.src = URL.createObjectURL(blob);
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
        let orient: number = 0;
        EXIF.getData(img, function () {
            //@ts-ignore
            orient = EXIF.getTag(this, 'Orientation');
        });

        return orient;
    }

    /**
     * 调整图片方向，返回新图片的 Base64 编码
     * 
     * @param img 
     * @param orient 
     */
    export function rotate(img: HTMLImageElement, orient: number): string {
        let width: number = img.width,
            height: number = img.height,
            canvas: HTMLCanvasElement = document.createElement('canvas'),
            ctx: CanvasRenderingContext2D = <CanvasRenderingContext2D>canvas.getContext("2d");

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
        let maxWidth: number = 1000,
            maxHeight: number = 1500,
            fitSizeObj = fitSize(imgObj.width, imgObj.height, maxWidth, maxHeight),
            targetWidth: number = fitSizeObj.targetWidth,
            targetHeight: number = fitSizeObj.targetHeight;

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
    export function dataURLtoBlob(dataurl: string): Blob {
        // @ts-ignore
        let arr: string[] = dataurl.split(','), mime: string = arr[0].match(/:(.*?);/)[1],
            bstr: string = atob(arr[1]),
            len: number = bstr.length, u8arr: Uint8Array = new Uint8Array(len);

        while (len--)
            u8arr[len] = bstr.charCodeAt(len);

        return new Blob([u8arr], { type: mime });
    }
}