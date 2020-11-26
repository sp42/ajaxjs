setTimeout(()=>{
	PAPER = aj.svg.Mgr.initSVG(aj(".canvas"));
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
	
	// 生成 box
	var imgSize = {width: 48, height: 48};
	var imgBox = (img, size) => aj.apply({ src: "../asset/images/workflow/" + img }, size); 
	
	for(var i in TEST_DATA.states) {
		var state = TEST_DATA.states[i];
		var box;
		
		switch(state.type) {
			case 'start':
				box = aj.svg.createBaseComponent(PAPER, imgBox('start.png', state.attr), 'img');
			break;
			case 'decision':
				box = aj.svg.createBaseComponent(PAPER, imgBox('fork.png', state.attr), 'img');
			break;
			case 'end':
				box = aj.svg.createBaseComponent(PAPER, imgBox('end.png', state.attr), 'img');
			break;
			default:
				box = aj.svg.createBaseComponent(PAPER, state.attr);
				box.text = state.text.text;
		}
		
		box.name = i;
		box.wfData = state.props;
		box.init();
		
		aj.workflow.states[i] = box;
	}
	
	// 连线
	for(var i in TEST_DATA.paths) {
		var pathCfg = TEST_DATA.paths[i];
		var from = aj.workflow.states[pathCfg.from], to = aj.workflow.states[pathCfg.to];
		var path = new aj.svg.Path(from.svg, to.svg, pathCfg.text.text);
		
		if (pathCfg.dots && pathCfg.dots.length) 
			path.restore(pathCfg.dots);
	}

}, 800);


// 菜单选中的
aj('.components ul li.selectable', li => {
	li.onclick = e => {
		var el = e.target;
		
		var selected = el.parentNode.$('.selected');
		if(selected)
			selected.classList.remove('selected');
			
		el.classList.add('selected');
		
		// 切换模式
		if(el.classList.contains('pointer'))
			aj.svgMgr.data.currentMode = aj.workflow.POINT_MODE;
			
		if(el.classList.contains('path'))
			aj.svgMgr.data.currentMode = aj.workflow.PATH_MODE;
	}
});

