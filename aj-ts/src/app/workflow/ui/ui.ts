namespace aj.wf {
	let imgBox = (img: string, size: VBox) => aj.apply({ src: `../asset/images/workflow/${img}` }, size);

	/**
	 * JSON 渲染为图形
	 * 
	 * @param data 
	 */
	export function init(data: RawWorkflowData): void {
		let states: { [key: string]: SvgVue } = DATA.states;

		for (let i in data.states) {
			let state: JsonState = data.states[i],
				box: SvgVue;

			switch (state.type) { // 生成 box
				case 'start':
					box = createRect(imgBox('start.png', state.attr), 'img');
					break;
				case 'decision':
					box = createRect(imgBox('decision.png', state.attr), 'img');
					break;
				case 'end':
					box = createRect(imgBox('end.png', state.attr), 'img');
					break;
				default:
					box = createRect(state.attr);
					box.text = <string><unknown>state.props.displayName;
			}

			box.type = state.type;
			box.ref = i;
			box.rawData = state;
			box.init();

			states[i] = box;
		}

		// 连线
		let paths = DATA.paths;
		for (let i in data.paths) {
			let pathCfg: JsonTransition = data.paths[i],
				from: SvgVue = states[pathCfg.from],
				to: SvgVue = states[pathCfg.to],
				path: svg.Path = new svg.Path(from.svg, to.svg, pathCfg);

			path.id = i;
			// path.wfData = pathCfg.props;
			paths[i] = path;

			pathCfg.dots && pathCfg.dots.length && path.restore(pathCfg.dots);
		}
	}

	/**
	 * 创建图形基类的工厂函数
	 * 
	 * @param box 
	 * @param type 
	 */
	function createRect(box: VBox, type?: string): SvgVue {
		let raphaelObj;

		switch (type) {
			case 'img':
				raphaelObj = svg.PAPER.image().attr(box).addClass('baseImg');
				break;
			default:
				raphaelObj = svg.PAPER.rect().attr(box).attr({ fill: "90-#fff-#F6F7FF" }).addClass('rectBaseStyle');
		}

		let vueObj: SvgVue = <SvgVue>new Vue({
			mixins: [svg.BaseRect],
			data: { vBox: box }
		});

		vueObj.PAPER = svg.PAPER;
		vueObj.svg = raphaelObj;

		// 登记注册
		Mgr.register(vueObj);

		if (type == 'img')
			vueObj.resize = false; // 图片禁止放大缩小

		if (isREAD_ONLY)
			vueObj.resize = vueObj.isDrag = false;

		return vueObj;
	}

	setTimeout(() => {
		let el = <HTMLElement>document.body.$(".canvas");
		//@ts-ignore
		svg.PAPER = window.PAPER = Raphael(el, el.clientWidth, el.clientHeight);
		// @ts-ignore
		init(TEST_DATA);

		wysiwyg.statusBar.showMsg('欢迎来到 工作流流程设计器');
	}, 800);

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
				Mgr.currentMode = SELECT_MODE.POINT_MODE;

			if (el.classList.contains('path'))
				Mgr.currentMode = SELECT_MODE.PATH_MODE;
		}
	});

	document.addEventListener('click', (ev: Event) => {
		let el: HTMLElement = <HTMLElement>ev.target,
			// @ts-ignore
			isSVGAElement = !!el.ownerSVGElement; // 点击页面任何一个元素，若为 SVG 且是组件，使其选中的状态

		if (isSVGAElement && el.id.indexOf('ajSVG') != -1) {
			//@ts-ignore
			let component = Mgr.allComps[el.id];

			if (!component)
				throw '未登记组件 ' + el.id;

			Mgr.setSelectedComponent(component);
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
		if (ev.keyCode == 46 && Mgr.selectedComponent) {
			Mgr.selectedComponent.remove();
			Mgr.selectedComponent = null;
		}
	});
}

