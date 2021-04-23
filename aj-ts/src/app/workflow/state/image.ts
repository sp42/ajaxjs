namespace aj.wf {
    /**
     * 一个流程节点（图片）
     */
    class ImageState extends BaseState {
        resize: boolean = false;// 图片禁止放大缩小

        constructor(PAPER: any, type: string, ref: string, rawData: JsonState, vBox: VBox) {
            super(PAPER, type, ref, rawData, vBox, getAttr(PAPER, type, vBox));
        }
    }

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

        return PAPER.image().attr({}).addClass('baseImg');
    }

    /**
     * 增加图片 src 属性
     * 
     * @param img 
     * @param size 
     * @returns 
     */
    let imgBox: Function = (img: string, size: VBox) => aj.apply({ src: `../asset/images/workflow/${img}` }, size);
}