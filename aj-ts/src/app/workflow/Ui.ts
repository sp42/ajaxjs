setTimeout(() => {
	aj.svg.PAPER = window.PAPER = aj.svg.Mgr.initSVG(document.body.$(".canvas"));
	//MyBOX = PAPER.rect().attr( {x: 50, y: 20, width: 500, height: 200, fill: "90-#fff-#F6F7FF"} );
	// vueObj1 = aj.svg.createBaseComponent(PAPER, {x: 50, y: 20, width: 500, height: 200, fill: "90-#fff-#F6F7FF"});
	//vueObj1.isDrag = false;
	/*	vueObj1.init(); 
		
		console.assert(vueObj1.id);
	
		vueObj2 = aj.svg.createBaseComponent(PAPER, {x: 50, y: 590, width: 300, height: 100, fill: "90-#fff-#F6F7FF"});
		vueObj2.text = "Hello World!";
		vueObj2.init();  
		setTimeout(()=>vueObj2.pos(100, 600), 1000);
		
		PATH = new aj.svg.Path(vueObj1.svg, vueObj2.svg, 'text');
		PATH.restore([{x:306,y:363},{x:450,y:442}]);
	
		vueObj3 = aj.svg.createBaseComponent(PAPER, {x: 110, y: 369, width: 48, height: 48, src: "../asset/images/workflow/start.png"}, 'img'); 
		vueObj3.init(); 
	
		PATH2 = new aj.svg.Path(vueObj3.svg, vueObj1.svg);*/

	// @ts-ignore
	aj.wf.ui.init(TEST_DATA);
}, 800);

namespace aj.wf.ui {
	let imgBox = (img: string, size: VBox) => aj.apply({ src: "../../asset/images/workflow/" + img }, size);

	export function init(data: JsonParam) {
		// 生成 box
		let states = aj.wf.data.states, paths = aj.workflow.data.paths;

		for (let i in data.states) {
			let state = data.states[i];
			let box: SvgVue;

			switch (state.type) { // 
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
					box.text = state.props.displayName;
			}

			box.type = state.type;
			box.id = i;
			box.rawData = state;
			box.wfData = state.props;
			box.init();

			states[i] = box;
		}

		// 连线
		for (let i in data.paths) {
			let pathCfg = data.paths[i];
			let from = states[pathCfg.from], to = states[pathCfg.to];
			let path = new aj.svg.Path(from.svg, to.svg, pathCfg.text.text);
			path.id = i;
			path.rawData = pathCfg;
			path.wfData = pathCfg.props;
			paths[i] = path;

			if (pathCfg.dots && pathCfg.dots.length)
				path.restore(pathCfg.dots);
		}
	}

	/**
	 * 创建图形基类的工厂函数
	 * 
	 * @param box 
	 * @param type 
	 */
	function createRect(box: VBox, type: string): SvgVue {
		let raphaelObj;

		switch (type) {
			case 'img':
				raphaelObj = aj.svg.PAPER.image().attr(box).addClass('baseImg');
				break;
			default:
				raphaelObj = aj.svg.PAPER.rect().attr(box).attr({ fill: "90-#fff-#F6F7FF" }).addClass('rectBaseStyle');
		}

		let vueObj: SvgVue = <SvgVue>new Vue({ mixins: [aj.svg.BaseRect], data: { vBox: box } });

		vueObj.PAPER = aj.svg.PAPER;
		vueObj.svg = raphaelObj;

		// 登记注册
		aj.svg.Mgr.register(vueObj);

		if (type == 'img')
			vueObj.resize = false; // 图片禁止放大缩小

		if (aj.workflow.isREAD_ONLY)
			vueObj.resize = vueObj.isDrag = false;

		return vueObj;
	}
}


// 菜单选中的
document.body.$('.components ul li.selectable', li => {
	li.onclick = e => {
		let el = e.target;

		let selected = el.parentNode.$('.selected');
		if (selected)
			selected.classList.remove('selected');

		el.classList.add('selected');

		// 切换模式
		if (el.classList.contains('pointer'))
			aj.svg.Mgr.currentMode = aj.workflow.POINT_MODE;

		if (el.classList.contains('path'))
			aj.svg.Mgr.currentMode = aj.workflow.PATH_MODE;
	}
});

// 表单通用属性
aj.workflow.propertyForm = {
	props: {
		cop: Object
	}
};

Vue.component('aj-wf-start-form', {
	template: document.body.$('.startEndForm'),
	mixins: [aj.workflow.propertyForm]
});

Vue.component('aj-wf-end-form', {
	template: document.body.$('.startEndForm'),
	mixins: [aj.workflow.propertyForm]
});

Vue.component('aj-wf-task-form', {
	template: document.body.$('.taskForm'),
	mixins: [aj.workflow.propertyForm]
});

Vue.component('aj-wf-custom-form', {
	template: document.body.$('.startForm'),
	mixins: [aj.workflow.propertyForm]
});

Vue.component('aj-wf-subprocess-form', {
	template: document.body.$('.startForm'),
	mixins: [aj.workflow.propertyForm]
});

Vue.component('aj-wf-decision-form', {
	template: document.body.$('.decisionForm'),
	mixins: [aj.workflow.propertyForm],
	mounted() {
		// debugger;
	}
});

Vue.component('aj-wf-fork-form', {
	template: document.body.$('.startForm'),
	mixins: [aj.workflow.propertyForm]
});

Vue.component('aj-wf-join-form', {
	template: document.body.$('.startForm'),
	mixins: [aj.workflow.propertyForm]
});

Vue.component('aj-wf-transition-form', {
	template: document.body.$('.transitionForm'),
	mixins: [aj.workflow.propertyForm],
	mounted() {
		// debugger;// transition 连线 expr 属性在 json 里面丢失
	}
});

aj.workflow.propertyEditor = new Vue({
	el: '.property',
	data: {
		selected: '', 	// 组件类型
		cop: null		// 组件
	},
	methods: {
		show() {
			return aj.svg.Mgr.selectedComponent != null;
		}
	},
	computed: {
		currentForm() {
			if (!this.cop)
				return '';

			return 'aj-wf-' + this.cop.type + '-form';
		}
	}
});