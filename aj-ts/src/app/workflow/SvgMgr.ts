/**
 * 定义常量、工具方法等
 */
aj.workflow = {
	POINT_MODE: 1,		// 选中的模式，点选类型
	PATH_MODE: 2,		// 选中的模式，添加路径类型
	isREAD_ONLY: false,	// 只读模式不可编辑
	data: {
		states: {},		// 所有的状态，通常是 key= box 的 name，value 是 box 的 vue 实例
		paths: {},		// 所有的路径
		JSON_DATA: {}	// 流程定义 JSON 数据
	}
};

aj.wf = aj.workflow; // shorthand

// 管理器，单例
aj.svg.Mgr = (function() {
	var name = {start: '开始节点', end: '结束节点', task: '任务节点', decision: '抉择节点', transition: '变迁路径'};

	function setTypeName(cop) {
		aj.wf.ui.PropertyForm.cop = cop;
		aj.wf.ui.PropertyForm.selected = name[cop.type];
	}

	return new Vue({
		data: {
			selectedComponent: null,			 	// 选中的 SVG 图形对象
			currentNode: null,						// 当前选中的节点
			currentMode: aj.workflow.POINT_MODE,	// 当前选中的选择模式
		},
		watch: {
			selectedComponent(newCop:SvgComp, old:SvgComp) {
				newCop && newCop.resizeController && newCop.resizeController.showBox();
				old && old.resizeController && old.resizeController.hideBox();

				if (newCop instanceof aj.svg.Path)
					newCop.show();

				if (old instanceof aj.svg.Path)
					old.hide();

				if (newCop && newCop.wfData) {
					console.log(newCop.wfData)
				}

				if (newCop) {
					setTypeName(newCop);
				}
			}
		},
		created() {
			this.uid = 0; 			// 每个图形对象赋予一个 id
			this.allComps = {}; 	// key=uid, value = 图形实例 component
			this.PAPER = null;		// Raphael.js 画布
		},
		methods: {
			initSVG(el) {		// 初始化
				this.PAPER = Raphael(el, el.clientWidth, el.clientHeight);

				document.addEventListener('click', e => {
					var el = e.target;
					var isSVGAElement = !!el.ownerSVGElement; // 点击页面任何一个元素，若为 SVG
					// 且是组件，使其选中的状态

					if (isSVGAElement && el.id.indexOf('ajSVG') != -1) {
						var component = this.allComps[el.id];

						if (!component)
							throw '未登记组件 ' + el.id;

						this.setSelectedComponent(component);
					}
				});

				/**
				 * 删除： 删除状态时，触发removerect事件，连接在这个状态上当路径监听到这个事件，触发removepath删除自身；
				 * 删除路径时，触发removepath事件
				 */
				document.addEventListener('keydown', e => {
					if (aj.workflow.isREAD_ONLY) return;

					// 键盘删除节点
					if (e.keyCode == 46 && this.selectedComponent) {
						this.selectedComponent.remove();
						this.selectedComponent = null;
					}
				});

				return this.PAPER;
			},
			// 设置选中的组件
			setSelectedComponent(cop) {
				this.selectedComponent = cop;
			},
			// 获取选中的组件
			getSelectedComponent() {
				return this.selectedComponent;
			},

			// 创建下个组件的 id
			nextId() {
				return ++this.uid;
			},

			// 登记组件
			register(vueObj) {
				vueObj.id = this.nextId();
				vueObj.svg.node.id = "ajSVG-" + vueObj.id;
				this.allComps[vueObj.svg.node.id] = vueObj;
			},

			// 注销组件
			unregister(id) {
				delete this.allComps[id];
			},

			// 清除桌布
			clearStage() {
				this.setSelectedComponent(null);
				this.PAPER.clear();

				for (var i in aj.wf.data)
					aj.wf.data[i] = {};
			}
		}
	});
})();