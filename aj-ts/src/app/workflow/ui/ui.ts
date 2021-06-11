namespace aj.wf {
	setTimeout(() => {
		let el = <HTMLElement>document.body.$(".canvas");
		//@ts-ignore
		svg.PAPER = window.PAPER = Raphael(el, el.clientWidth, el.clientHeight);
		// @ts-ignore
		init(TEST_DATA);

		wysiwyg.statusBar.showMsg('欢迎来到 工作流流程设计器');
	}, 800);

	/**
	 * JSON 渲染为图形
	 * 
	 * @param data 
	 */
	function init(data: RawWorkflowData): void {
		let states: { [key: string]: BaseState } = DATA.STATES;

		for (let i in data.states) {
			let stateData: JsonState = data.states[i],
				state: BaseState;

			switch (stateData.type) { // 生成 box
				case 'start':
				case 'decision':
				case 'end':
					state = new ImageState(svg.PAPER, i, stateData);
					break;
				default:
					state = new State(svg.PAPER, i, stateData); 
			}

			if (isREAD_ONLY)
				state.resize = state.isDrag = false;

			states[i] = state;
		}

		// 连线
		let paths: { [key: string]: svg.Path } = DATA.PATHS;

		for (let i in data.paths) {
			let pathData: JsonTransition = data.paths[i],
				from: BaseState = states[pathData.from],
				to: BaseState = states[pathData.to],
				path: svg.Path = new svg.Path(i, from.svg, to.svg, pathData);

			paths[i] = path;
			pathData.dots && pathData.dots.length && path.restore(pathData.dots);
		}
	}

	// 菜单选中的
	document.body.$('.components ul li.selectable', li => {
		li.onclick = (ev: Event) => {
			let el: Element = <Element>ev.target,
				selected = (<Element>el.parentNode).$(SELECTED_CSS);

			if (selected)
				(<HTMLElement>selected).classList.remove(SELECTED);

			el.classList.add(SELECTED);

			// 切换模式
			if (el.classList.contains('pointer'))
				main.currentMode = SELECT_MODE.POINT_MODE;

			if (el.classList.contains('path'))
				main.currentMode = SELECT_MODE.PATH_MODE;
		}
	});

	document.addEventListener('click', (ev: Event) => {
		let el: HTMLElement = <HTMLElement>ev.target,
			// @ts-ignore
			isSVGAElement = !!el.ownerSVGElement; // 点击页面任何一个元素，若为 SVG 且是组件，使其选中的状态

		if (isSVGAElement && el.id.indexOf('ajComp') != -1) {
			let component:Component = DATA.ALL_COMPS[el.id];

			if (!component)
				throw '未登记组件 ' + el.id;

			main.setSelectedComponent(component);
		}
	});

	/**
	 * 删除： 删除状态时，触发removerect事件，连接在这个状态上当路径监听到这个事件，触发removepath删除自身；
	 * 删除路径时，触发removepath事件
	 */
	document.addEventListener('keydown', (ev: KeyboardEvent) => {
		if (isREAD_ONLY)
			return;

		// 键盘删除节点
		if (ev.keyCode == 46 && main.vue.selectedComponent) {
			main.selectedComponent.remove();
			main.selectedComponent = null;
		}
	});
}