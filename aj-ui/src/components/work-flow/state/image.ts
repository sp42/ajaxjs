import { BaseState } from './base';

/**
 * 一个流程节点（图片）
 */
export class ImageState extends BaseState {
    resize: boolean = false;// 图片禁止放大缩小

    constructor(PAPER: any, ref: string, rawData: JsonState) {
        super(PAPER, ref, rawData, getAttr(PAPER, rawData.type, rawData.attr));
    }
}

/**
 * super 限制，故设立一个函数
 * 
 * @param PAPER 
 * @param type 
 * @param vBox 
 * @returns 
 */
function getAttr(PAPER: any, type: string, vBox: VBox): Raphael {
    let attr: {} = {};

    switch (type) { // 生成 box
        case 'start':
            attr = imgBox('start.png', vBox);
            break;
        case 'decision':
            attr = imgBox('decision.png', vBox);
            break;
        case 'end':
            attr = imgBox('end.png', vBox);
            break;
    }

    return PAPER.image().attr(attr).addClass('baseImg');
}

/**
 * 增加图片 src 属性
 * 
 * @param img 
 * @param size 
 * @returns 
 */
let imgBox: Function = (img: string, size: VBox) => Object.assign({ src: require(`@/assets/workflow/${img}`) }, size);