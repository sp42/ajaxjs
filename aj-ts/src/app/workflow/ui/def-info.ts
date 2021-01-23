namespace aj.wf.ui {
    interface DefInfo extends Vue {
        info: any;
        isCreate: boolean;
    }

    /**
     * 流程定义
     */
    export let DefInfo: DefInfo = <DefInfo>new Vue({
        el: '.defInfo',
        data: {
            info: {},
            isCreate: false
        },
        methods: {
            saveOrUpdate() {
                // this.toXML();
            },
            toXML(this: DefInfo): string {
                let xml: string[] = ["<process "],
                    clearInfo: JsonParam = {
                        name: this.info.name, displayName: this.info.displayName, instanceUrl: this.info.instanceUrl, expireTime: this.info.expireTime
                    };

                // 流程定义的属性
                for (let attrib in clearInfo) {
                    let value = clearInfo[attrib];

                    if ((attrib == "name" || attrib == "displayName") && !value) {
                        aj.alert("流程定义名称、显示名称不能为空");
                        return "";
                    }

                    if (value)
                        xml.push(` ${attrib}="${value}"`);
                }

                xml.push(">\n");

                let arr: string[] = [], // 用来检查是否重复
                    node,
                    states = DATA.states,
                    paths = DATA.paths,
                    pathToXml = aj.svg.serialize.path.toXml;

                for (let i in states) {
                    node = states[i];

                    if (!node)
                        continue;

                    xml.push(aj.svg.serialize.rect.toBeforeXml(node));

                    for (let i2 in paths) {
                        let transition = paths[i2];

                        if (!transition)
                            continue;

                        if (node.id == transition.from().vue.id) {
                            let transitionXml = pathToXml(transition);

                            if (!transitionXml) {
                                aj.alert("连接线名称不能为空");
                                return "";
                            } else {
                                arr.push(transition.id); // arr 用于判别是否重复
                                xml.push("\n" + transitionXml);
                            }
                        }
                    }

                    xml.push("\n </" + node.type + ">\n");
                }

                arr = arr.sort();
                for (let i = 0; i < arr.length; i++) {
                    if (arr[i] == arr[i + 1]) {
                        aj.alert("连接线名称不能重复[" + arr[i] + "]");
                        return "";
                    }
                }

                xml.push("</process>");
                window.alert(xml.join(''));

                return xml.join('');
            }
        }
    });
}