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


			            /* this上的数据不一定要在data中定义，如果不想变成响应式数据就没有必要定义，这样反而会性能优化 */
						aj.apply(this, {
							type: String,	// 组件类型
							id: String,		// 每个图形对象赋予一个 id
							name: String,	// 通过 name 保存到 states 的 map 之中，它是 key
							svg: Object,	// raphael 图形实例
							wfData: {},		// 工作流状态信息
							PAPER: Object,	// 图形所在的桌布
							isDrag: true, 	// 是否启用鼠标拖放
							resize: true, 	// 是否启用放大缩小
							resizeNode: null,// resize 对象
							textNode: Object// 文字对象，这是 Vue 实例
						});
			
						this.updateHandlers = []; // 当大小、位置有变化时候执行的函数列表