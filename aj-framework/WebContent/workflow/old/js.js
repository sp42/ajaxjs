ajflow.DESIGNER = new Vue({
	el: '.aj-workflow',
	data: {
		currentNode: null, 			// 当前选中的那个节点
		states: {},
		paths: {},
		property: {} 				// 属性
	},
	mounted() {
		var el = aj('#snakerflow');
		ajflow.PAPER = Raphael(el, window.innerWidth, window.innerHeight);// Raphael.js 画布
		this.init(el);
		this.initAdd(el);
		this.initEvent(el);
		this.initDD();
	},
	foo:'hi',
	methods: {
		init(el) {
			var _states = this.states, _paths = this.paths;
	 		aj.apply(ajflow.config, CONFIG);

	        var removeHandler = function(e, src) {
	            if (!ajflow.config.editable) 
	                return;
	            
	            if (src.getId().substring(0, 4) == "rect") 
	                this.states[src.getId()] = null;
	            else if (src.getId().substring(0, 4) == "path") 
	                this.paths[src.getId()] = null;

                src.remove();
	        }.bind(this);
	
	        $(ajflow.PAPER).bind("removepath", removeHandler);
	        $(ajflow.PAPER).bind("removerect", removeHandler);
 			$(ajflow.PAPER).data("mod", "point");		// 模式
			
			if (ajflow.config.editable) 
				new ajflow.Props({});			// 属性框
			
			this.renderer(CONFIG.restore);
			this.history();
		},
		initAdd(el) {
			this.$el.$('.toolbox li.state', item => {
				item.draggable = true;
				item.ondragstart = e => {
					e.dataTransfer.setData("type", e.target.dataset.type);
				}
			});
			
			el.ondragover = e => {
				var type = e.dataTransfer.getData("type");
				if (!type)
					e.preventDefault();
			}
			
			el.addEventListener('drop', e => {
				var type = e.dataTransfer.getData("type");
				if (type) {
					console.log(type);
					var o = {attr : { x: e.x, y: e.y }};
					var rect = new ajflow.Rect(aj.extend(true, {}, ajflow.config.tools.states[type], o));
            		this.states[rect.getId()] = rect;
				}
				
				e.preventDefault();
			});
		},
		initEvent(el){
			// 点击恢复默认的属性面板
/*			document.addEventListener('click', ()=>{
				this.currentNode = null;
	
				$(ajflow.PAPER).trigger("click", { // 取消图形上的选中状态
				        getId() {return "";}
				});
				
				$(ajflow.PAPER).trigger("showprops", [ajflow.config.props.props, {
				    getId() {
				        return ""
				    }
				}]);
			});*/
			
			/**
			 * 删除： 删除状态时，触发removerect事件，连接在这个状态上当路径监听到这个事件，触发removepath删除自身；
			 * 删除路径时，触发removepath事件
			 */
	        document.addEventListener('keydown', i => {
	            if (!ajflow.config.editable) 
	                return;

	            // 键盘删除节点
	            if (i.keyCode == 46 && this.currentNode) {
	                if (this.currentNode.getId().substring(0, 4) == "rect") 
	                    $(ajflow.PAPER).trigger("removerect", this.currentNode);
	                else if (this.currentNode.getId().substring(0, 4) == "path") 
	                    $(ajflow.PAPER).trigger("removepath", this.currentNode);
	                    
	                this.currentNode = null;
	            }
	        });
		},
		// 使对象可拖动
		initDD() {
			var box = this.$el.$('.aj-workflow .toolbox');
			var dragBar = box.$('legend');
			
			initDD(box, dragBar);
			initDD(this.$el.$('.aj-workflow .prop'), this.$el.$('.aj-workflow .prop legend'));
		},
	
		// 添加路径
       	addPath(from, to) {
            var path = new ajflow.Path({}, from, to);
            this.paths[path.getId()] = path;
        },

		toXML() {
            var xml = "<process ", props = ajflow.config.props.props;

			// 流程定义的属性
            for (var i in props) {
				var value = props[i].value;
                if ((i == "name" || i == "displayName") && value == "") {
                    alert("流程定义名称、显示名称不能为空");
                    return;
                }

                if (value) 
                    xml += i + '="' + value + '" ';
            }

            xml += ">\n";
            var arr = [], node;

            for (var i in this.states) {
				node = this.states[i];
				
                if (!node) 
					continute;
					
                xml += node.toBeforeXml();

                for (var i2 in this.paths) {
					var transition = this.paths[i2];
					
					if(!transition)
						continute;

                    if (transition.from().getId() == node.getId()) {
                        var transitionXml = transition.toXml();

                        if (transitionXml == "") {
                            alert("连接线名称不能为空");
                            return;
                        } else {
                            arr.push(transition.name()); // 判别是否重复
                            xml += "\n" + transitionXml;
                        }
                    }
                }

                xml += "\n" + node.toAfterXml() + "\n";
            }

            xml += "</process>";
            arr = arr.sort();

            for (var i = 0; i < arr.length; i++) {
                if (arr[i] == arr[i + 1]) {
                    alert("连接线名称不能重复[" + arr[i] + "]");
                    return;
                }
            }

            alert(xml);
			// 保存到数据库。。。
        },

		// 渲染图形
		renderer(data) {
			var rmap = {}, states = data.states, paths = data.paths;

            if (states) {
                for (var s in states) {
					var state = states[s];
                    var rect = new ajflow.Rect(aj.extend(true, {}, ajflow.config.tools.states[state.type], state));
                    rect.restore(state);
                    rmap[s] = rect;

                    this.states[rect.getId()] = rect; 
                }
            }

            if (paths) {
                for (var s in paths) {
					var path = paths[s];
                    var p = new ajflow.Path(aj.extend(true, {}, ajflow.config.tools.path, path), rmap[path.from], rmap[path.to]);
                    p.restore(path);

                    this.paths[p.getId()] = p;
                }
            }

            if (data.props && data.props.props) {
				var p = ajflow.config.props.props;
                for (var s in p) {
                    var tmp = p[s];

                    for (var ss in data.props.props) {
                        if (tmp.name == data.props.props[ss].name) {
                            tmp.value = data.props.props[ss].value;
                            break;
                        }
                    }
                }
            }
		},
		history() {
			// 历史状态
	        var hr = ajflow.config.historyRects, ar = ajflow.config.activeRects;
	
	        if (hr.rects.length || ar.rects.length) {
	            var pmap = {}, rmap = {};
	
	            for (var h in this.paths) { // 先组织 map
					var path = this.paths[h];
					
	                if (!rmap[path.from().getName()]) {
	                    rmap[path.from().getName()] = {
	                        rect: path.from(),
	                        paths: {}
	                    };
	                }
	
	                if (!rmap[path.to().getName()]) {
	                    rmap[path.to().getName()] = {
	                        rect: path.to(),
	                        paths: {}
	                    };
	                }
	
	                rmap[path.from().getName()].paths[path.name()] = path;
	                //alert(_paths[h].from().getName() + "======>" + _paths[h].name());
	            }
	
	            for (var u = 0; u < hr.rects.length; u++) {
	                if (rmap[hr.rects[u].name]) 
	                    rmap[hr.rects[u].name].rect.attr(hr.rectAttr)
	                
	                for (var t = 0; t < hr.rects[u].paths.length; t++) {
	                    if (rmap[hr.rects[u].name].paths[hr.rects[u].paths[t]]) 
	                        rmap[hr.rects[u].name].paths[hr.rects[u].paths[t]].attr(hr.pathAttr)
	                }
	            }
	
	            for (var u = 0; u < ar.rects.length; u++) {
	                if (rmap[ar.rects[u].name]) 
	                    rmap[ar.rects[u].name].rect.attr(ar.rectAttr)
	                
	                for (var t = 0; t < ar.rects[u].paths.length; t++) {
	                    if (rmap[ar.rects[u].name].paths[ar.rects[u].paths[t]]) 
	                        rmap[ar.rects[u].name].paths[ar.rects[u].paths[t]].attr(ar.pathAttr)
	                }
	            }
	        }
		}
	}
});

function initDD(box, dragBar) {
	// 鼠标按下的函数
	dragBar.onmousedown = function(oEvent) {
		// 求出鼠标和box的位置差值
		var x = oEvent.clientX - box.offsetLeft;
		var y = oEvent.clientY - box.offsetTop;
		
		// 鼠标移动的函数
		// 把事件加在document上，解决因为鼠标移动太快时，鼠标超过box后就没有了拖拽的效果的问题
		document.onmousemove = function(oEvent) {
			// 只能拖动窗口标题才能移动
			if (oEvent.target != dragBar) {
				// return;
			}
			
			// 保证拖拽框一直保持在浏览器窗口内部，不能被拖出的浏览器窗口的范围
			var l = oEvent.clientX - x, t = oEvent.clientY - y;
			var doc = document.documentElement;
			
			if (l < 0) {
				l = 0;
			} else if (l > doc.clientWidth - box.offsetWidth) {
				l = doc.clientWidth - box.offsetWidth;
			}
			
			if (t < 0) {
				t = 0;
			} else if (t > doc.clientHeight - box.offsetHeight) {
				t = doc.clientHeight - box.offsetHeight;
			}
			
			box.style.left = l + "px";
			box.style.top = t + "px";
		}
		
		// 鼠标抬起的函数
		document.onmouseup = function() {
			document.onmousemove = document.onmouseup = null;
		}
		
		// 火狐浏览器在拖拽空div时会出现bug
		// return false阻止默认事件，解决火狐的bug
		return false;
	}
}
