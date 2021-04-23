"use strict";
var aj;
(function (aj) {
    var wf;
    (function (wf) {
        var ui;
        (function (ui) {
            /**
             * 流程定义
             */
            ui.DefInfo = new Vue({
                el: '.defInfo',
                data: {
                    info: {},
                    isCreate: false
                },
                methods: {
                    saveOrUpdate: function () {
                        // this.toXML();
                    },
                    toXML: function () {
                        var xml = ["<process "], clearInfo = {
                            name: this.info.name, displayName: this.info.displayName, instanceUrl: this.info.instanceUrl, expireTime: this.info.expireTime
                        };
                        // 流程定义的属性
                        for (var attrib in clearInfo) {
                            var value = clearInfo[attrib];
                            if ((attrib == "name" || attrib == "displayName") && !value) {
                                aj.alert("流程定义名称、显示名称不能为空");
                                return "";
                            }
                            if (value)
                                xml.push(" " + attrib + "=\"" + value + "\"");
                        }
                        xml.push(">\n");
                        var arr = [], // 用来检查是否重复
                        node, states = wf.DATA.states, paths = wf.DATA.paths, pathToXml = aj.svg.serialize.path.toXml;
                        for (var i in states) {
                            node = states[i];
                            if (!node)
                                continue;
                            xml.push(aj.svg.serialize.rect.toBeforeXml(node));
                            for (var i2 in paths) {
                                var transition = paths[i2];
                                if (!transition)
                                    continue;
                                if (node.id == transition.from().vue.id) {
                                    var transitionXml = pathToXml(transition);
                                    if (!transitionXml) {
                                        aj.alert("连接线名称不能为空");
                                        return "";
                                    }
                                    else {
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
                                return "";
                            }
                        }
                        xml.push("</process>");
                        window.alert(xml.join(''));
                        return xml.join('');
                    }
                }
            });
        })(ui = wf.ui || (wf.ui = {}));
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
