/**
 * 定义常量、工具方法等
 */
aj.workflow = {
	data: {
		states: {},		// 所有的状态，通常是 key= box 的 name，value 是 box 的 vue 实例
		paths: {},		// 所有的路径
		JSON_DATA: {}	// 流程定义 JSON 数据
	}
};

aj.wf = aj.workflow; // shorthand

namespace aj.wf {
	export enum SELECT_MODE {
		/**
		 * 选中的模式，点选类型
		 */
		POINT_MODE = 1,

		/**
		 * 选中的模式，添加路径类型
		 */
		PATH_MODE = 2
	}

	/**
	 * 只读模式不可编辑
	 */
	export var isREAD_ONLY: boolean = false;
}

namespace aj.svg {
	let name: StringJsonParam = { start: '开始节点', end: '结束节点', task: '任务节点', decision: '抉择节点', transition: '变迁路径' };

	/**
	 * 
	 * @param cop 
	 */
	function setTypeName(cop: SvgComp): void {
		aj.wf.ui.PropertyForm.cop = cop;
		aj.wf.ui.PropertyForm.selected = name[cop.type];
	}

	interface Mgr extends Vue {
		/**
		 * 选中的 SVG 图形对象
		 */
		selectedComponent: SvgComp | null;

		/**
		 * 当前选中的选择模式
		 */
		currentMode: aj.wf.SELECT_MODE;

		/**
		 * 所有的组件
		 * key=uid, value = 图形实例 component
		 */
		allComps: { [key: number]: SvgComp };

		/**
		 * 设置选中的组件
		 * 
		 * @param cop 
		 */
		setSelectedComponent(cop: SvgComp | null): void;

		/**
		 * 创建下个组件的 id
		 */
		nextId(): number;

		/**
		 * 登记组件
		 * 
		 * @param this 
		 * @param vueObj 
		 */
		register(vueObj): void;

		/**
		 * 注销组件
		 * 
		 * @param this 
		 * @param id 
		 */
		unregister(this: Mgr, id: number): void;
	}

	let uid: number = 0;

	export var Mgr: Mgr = <Mgr>new Vue({
		data: {
			selectedComponent: null,			 	// 选中的 SVG 图形对象
			currentNode: null,						// 当前选中的节点
			currentMode: aj.wf.SELECT_MODE.POINT_MODE,	// 当前选中的选择模式
		},
		watch: {
			selectedComponent(newCop: SvgComp, old: SvgComp): void {
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
			this.allComps = {}; 	// key=uid, value = 图形实例 component
		},
		methods: {
			// 设置选中的组件
			setSelectedComponent(this: Mgr, cop: SvgComp | null): void {
				this.selectedComponent = cop;
			},

			// 获取选中的组件
			getSelectedComponent(this: Mgr): SvgComp | null {
				return this.selectedComponent;
			},

			// 创建下个组件的 id
			nextId(): number {
				return ++uid;
			},

			// 登记组件
			register(this: Mgr, vueObj): void {
				vueObj.id = this.nextId();
				vueObj.svg.node.id = "ajSVG-" + vueObj.id;
				this.allComps[vueObj.svg.node.id] = vueObj;
			},

			// 注销组件
			unregister(this: Mgr, id: number): void {
				delete this.allComps[id];
			},

			// 清除桌布
			clearStage(this: Mgr): void {
				this.setSelectedComponent(null);
				aj.svg.PAPER.clear();

				for (var i in aj.wf.data)
					aj.wf.data[i] = {};
			}
		}
	});
}