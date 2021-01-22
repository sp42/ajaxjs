namespace aj.wf.ui {
    /**
     * 流程定义
     */
    export var defInfo: Vue = new Vue({
        el: '.defInfo',
        data: {
            info: {},
            isCreate: false
        },
        methods: {
            saveOrUpdate() {
                this.toXML();
            },
            toXML(this: Vue) {
                let xml = ["<process "];
                let clearInfo = { name: this.info.name, displayName: this.info.displayName, instanceUrl: this.info.instanceUrl, expireTime: this.info.expireTime };

                // 流程定义的属性
                for (var attrib in clearInfo) {
                    var value = clearInfo[attrib];

                    if ((attrib == "name" || attrib == "displayName") && !value) {
                        aj.alert("流程定义名称、显示名称不能为空");
                        return;
                    }

                    if (value)
                        xml.push(' ' + attrib + '="' + value + '"');
                }

                xml.push(">\n");

                let arr = [], node, states = aj.workflow.data.states, paths = aj.workflow.data.paths, pathToXml = aj.svg.serialize.path.toXml;

                for (let i in states) {
                    node = states[i];

                    if (!node)
                        continute;

                    xml.push(aj.svg.serialize.rect.toBeforeXml(node));

                    for (let i2 in paths) {
                        var transition = paths[i2];

                        if (!transition)
                            continute;

                        if (node.id == transition.from().vue.id) {
                            var transitionXml = pathToXml(transition);

                            if (!transitionXml) {
                                aj.alert("连接线名称不能为空");
                                return;
                            } else {
                                arr.push(transition.id); // arr 用于判别是否重复
                                xml.push("\n" + transitionXml);
                            }
                        }
                    }

                    xml.push("\n </" + node.type + ">\n");
                }

                arr = arr.sort();
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i] == arr[i + 1]) {
                        aj.alert("连接线名称不能重复[" + arr[i] + "]");
                        return;
                    }
                }

                xml.push("</process>");

                console.log(xml.join(''))
                alert(xml.join(''));
            }
        }
    });
}