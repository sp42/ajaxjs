setTimeout(() => {
	window.PAPER = aj.svg.Mgr.initSVG(document.body.$(".canvas"));
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

	aj.svg.startup.init(TEST_DATA);
}, 800);

namespace aj.wf.ui {
	export function init(data) {
		// 生成 box
		var imgBox = (img, size) => aj.apply({ src: "../../asset/images/workflow/" + img }, size);
		var states = aj.wf.data.states, paths = aj.workflow.data.paths;

		for (var i in data.states) {
			var state = data.states[i];
			var box;

			switch (state.type) { // 
				case 'start':
					box = aj.svg.createRect(PAPER, imgBox('start.png', state.attr), 'img');
					break;
				case 'decision':
					box = aj.svg.createRect(PAPER, imgBox('decision.png', state.attr), 'img');
					break;
				case 'end':
					box = aj.svg.createRect(PAPER, imgBox('end.png', state.attr), 'img');
					break;
				default:
					box = aj.svg.createRect(PAPER, state.attr);
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
		for (var i in data.paths) {
			var pathCfg = data.paths[i];
			var from = states[pathCfg.from], to = states[pathCfg.to];
			var path = new aj.svg.Path(from.svg, to.svg, pathCfg.text.text);
			path.id = i;
			path.rawData = pathCfg;
			path.wfData = pathCfg.props;
			paths[i] = path;

			if (pathCfg.dots && pathCfg.dots.length)
				path.restore(pathCfg.dots);
		}
	}
}


// 菜单选中的
document.body.$('.components ul li.selectable', li => {
	li.onclick = e => {
		var el = e.target;

		var selected = el.parentNode.$('.selected');
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
}); n

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