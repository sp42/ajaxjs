namespace aj.wf {
    export let serialize = {
        /**
         * 生成键对值
         * 
         * @param strArr 
         * @param key 
         * @param value 
         */
        keyValue(strArr: string[], key: string, value: string): void {
            if (value) {
                value = value.replace(/>/g, "#5").replace(/</g, "#6").replace(/&/g, "#7");
                strArr.push(` ${key}="${value}"`);
            }
        },
        rect: {
            // toJson(_o, _text, _rect): string {
            //     let str: string = "{type:'" + _o.type + "',text:{text:'" + _text.attr('text') +
            //         "'}, attr:{ x:" + Math.round(_rect.attr('x')) + ", y:" + Math.round(_rect.attr('y')) +
            //         ", width:" + Math.round(_rect.attr('width')) + ", height:" + Math.round(_rect.attr('height')) + "}, props:{";

            //     for (let k in _o.props)
            //         data += k + ":{value:'" + _o.props[k].value + "'},";

            //     str = removeLast(str, ",");

            //     return str + "}}";
            // },
            // toBeforeXml(comp: SvgComp): string {
            //     let str: string[] = [" <", comp.type, ' layout="' +
            //         (Math.round(Number(comp.svg.attr("x"))) - 180), ",", Math.round(comp.svg.attr("y")), ",",
            //         Math.round(comp.svg.attr("width")), ",", Math.round(comp.svg.attr("height")), '"'];

            //     let value;

            //     for (let i in comp.wfData) {
            //         value = comp.wfData[i].value;

            //         if (i == "name" && !value) {
            //             aj.alert(comp.type + " 名称 不能为空");
            //             return "";
            //         }

            //         if (i === "layout")
            //             continue;

            //         serialize.keyValue(str, i, value);
            //     }

            //     str.push(">");

            //     return str.join('');
            // }
        },
        path: {
            // toJson(_from, _to, _dotList: DotList, _text, _textPos): string {
            //     let str: string = "{from:'" + _from.getId() + "',to:'" + _to.getId() + "', dots:" + _dotList.toJson() + ",text:{text:'" + _text.attr("text") +
            //         "'},textPos:{_dotList:" + Math.round(_textPos.x) + ",_ox:" + Math.round(_textPos.y) + "}, props:{";

            //     for (let o in _o.props)
            //         str += `${o} : {value : '${_o.props[o].value}'},`

            //     str = removeLast(str, ",");

            //     return str + "}}";
            // },

            // toXml(path: Path, _textPos?: any): string {
            //     //	let str = ['<transition offset="', Math.round(_textPos.x) + "," + Math.round(_textPos.y), '" to="', path.to().getName(), '" '];
            //     let str: string[] = ['<transition offset="" to="', path.to().vue.id, '" '],
            //         dots: string = serialize.dotList.toXml(path.fromDot);

            //     if (dots != "")
            //         str.push(' g="' + dots + '" ');

            //     let value;

            //     for (let i in path.rawData.props) {
            //         value = path.wfData[i].value;

            //         if (i === "name" && value == "") {// name 为空，使用 id 作为 name
            //             str.push(i + '="' + _id + '" ');
            //             continue;
            //         }

            //         serialize.keyValue(str, i, value);
            //     }

            //     str.push("/>");

            //     return str.join('');
            // }
        },
        dotList: {
            toJson(fromDot: svg.Dot): string {
                let str: string = "[",
                    d: svg.Dot = fromDot;

                while (d) {
                    if (d.type === svg.DOT_TYPE.BIG)
                        str += `{x:${Math.round(d.pos().x)}, y:${Math.round(d.pos().y)}},`;

                    d = d.getRightDot();
                }

                str = removeLast(str, ",");

                return str + "]";
            },

            toXml(fromDot: svg.Dot): string {
                let str: string = "",
                    d: svg.Dot = fromDot;

                while (d) {
                    if (d.type === svg.DOT_TYPE.BIG)
                        str += `${Math.round(d.pos().x) - 180}, ${Math.round(d.pos().y)};`;

                    d = d.getRightDot();
                }

                return removeLast(str, ";");
            }
        }
    };

    /**
     * 移除最后一个 ;
     * 
     * @param str 
     * @param char 
     * @returns 
     */
    function removeLast(str: string, char: string): string {
        if (str.substring(str.length - 1, str.length) === char)
            return str.substring(0, str.length - 1);
        else
            return str;
    }
}