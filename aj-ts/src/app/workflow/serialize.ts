namespace aj.svg {
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
                strArr.push(" " + key + '="' + value + '"');
            }
        },
        rect: {
            toJson(_o, _text, _rect): string {
                let data = "{type:'" + _o.type + "',text:{text:'" + _text.attr('text') +
                    "'}, attr:{ x:" + Math.round(_rect.attr('x')) + ", y:" + Math.round(_rect.attr('y')) +
                    ", width:" + Math.round(_rect.attr('width')) + ", height:" + Math.round(_rect.attr('height')) + "}, props:{";

                for (let k in _o.props)
                    data += k + ":{value:'" + _o.props[k].value + "'},";

                if (data.substring(data.length - 1, data.length) == ',')
                    data = data.substring(0, data.length - 1);

                data += "}}";

                return data;
            },
            toBeforeXml(comp): string {
                let str = [" <", comp.type, ' layout="' +
                    (Math.round(comp.svg.attr("x")) - 180), ",", Math.round(comp.svg.attr("y")), ",",
                    Math.round(comp.svg.attr("width")), ",", Math.round(comp.svg.attr("height")), '"'];

                let value, keyValue = aj.svg.serialize.keyValue;
                for (let i in comp.wfData) {
                    value = comp.wfData[i].value;

                    if (i == "name" && !value) {
                        aj.alert(comp.type + " 名称 不能为空");
                        return "";
                    }

                    if (i === "layout")
                        continue;

                    keyValue(str, i, value);
                }

                str.push(">");
                return str.join('');
            }
        },
        path: {
            toJson(_from, _to, _dotList: DotList, _text, _textPos): string {
                let r = "{from:'" + _from.getId() + "',to:'" + _to.getId() + "', dots:" + _dotList.toJson() + ",text:{text:'" + _text.attr("text") +
                    "'},textPos:{_dotList:" + Math.round(_textPos.x) + ",_ox:" + Math.round(_textPos.y) + "}, props:{";

                for (let o in _o.props)
                    r += o + ":{value:'" + _o.props[o].value + "'},"

                if (r.substring(r.length - 1, r.length) == ",")
                    r = r.substring(0, r.length - 1)

                r += "}}";
                return r;
            },

            toXml(path: Path, _textPos): string {
                //	let str = ['<transition offset="', Math.round(_textPos.x) + "," + Math.round(_textPos.y), '" to="', path.to().getName(), '" '];
                let str = ['<transition offset="" to="', path.to().vue.id, '" '];
                let dots = aj.svg.serialize.dotList.toXml(path.fromDot);

                if (dots != "")
                    str.push(' g="' + dots + '" ');

                let value, keyValue = aj.svg.serialize.keyValue;
                for (let i in path.wfData) {
                    value = path.wfData[i].value;

                    if (i === "name" && value == "") {// name 为空，使用 id 作为 name
                        str.push(i + '="' + _id + '" ');
                        continue;
                    }

                    keyValue(str, i, value);
                }

                str.push("/>");
                return str.join('');
            }
        },
        dotList: {
            toJson(_fromDot: Dot): string {
                let data = "[", d = _fromDot;

                while (d) {
                    if (d.type === aj.svg.Dot.BIG)
                        data += "{x:" + Math.round(d.pos().x) + ",y:" + Math.round(d.pos().y) + "},";

                    d = d.right();
                }

                if (data.substring(data.length - 1, data.length) == ",")
                    data = data.substring(0, data.length - 1);

                data += "]";
                return data;
            },

            toXml(_fromDot: Dot): string {
                let data = "", d = _fromDot;

                while (d) {
                    if (d.type === aj.svg.Dot.BIG)
                        data += (Math.round(d.pos().x) - 180) + "," + Math.round(d.pos().y) + ";"

                    d = d.right();
                }

                if (data.substring(data.length - 1, data.length) == ";")
                    data = data.substring(0, data.length - 1);

                return data;
            }
        }
    };
}