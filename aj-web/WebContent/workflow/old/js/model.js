ajflow = {};

function newInput(){
	return new ajflow.editors.inputEditor();
}

function newText(){
	return new ajflow.editors.textEditor();
}

// 定义使用到的形状
ajflow.config = {
	editable: true,
	lineHeight: 15,
	basePath:"",
    ctxPath:"",
    orderId: "",
	rect: { // 状态
		attr:{x:10,y:10,width:100,height:50,r:8,fill:"90-#fff-#F6F7FF",stroke:"#03689A","stroke-width":2},
		showType:"image&text", // image,text,image&text
		type:"state",
		name:{text:"state","font-style":"italic"},
		text:{text:"状态","font-size":13},
		margin:5,
		props:[],
		img:{}
	},
	path:{// 路径转换
		attr:{
			path:{path:"M10 10L100 100",stroke:"#808080",fill:"none","stroke-width":2},
			arrow:{path:"M10 10L10 10",stroke:"#808080",fill:"#808080","stroke-width":2,radius:4},
			fromDot:{width:5,height:5,stroke:"#fff",fill:"#000",cursor:"move","stroke-width":2},
			toDot:{width:5,height:5,stroke:"#fff",fill:"#000",cursor:"move","stroke-width":2},
			bigDot:{width:5,height:5,stroke:"#fff",fill:"#000",cursor:"move","stroke-width":2},
			smallDot:{width:5,height:5,stroke:"#fff",fill:"#000",cursor:"move","stroke-width":3}
		},
		text:{text:"",cursor:"move",background:"#000"},
		textPos:{x:0,y:-10},
		props:{
			name:{name:"name",label:"名称",value:"",editor: newInput},
			displayName:{name:"displayName",label:"显示",value:"",editor:newText},
            expr:{name:"expr",label:"表达式",value:"",editor: newInput}
		}
	},
	tools:{// 工具栏
		attr:{left:10,top:10},
		pointer:{},
		path:{},
		states: {
			start : {
				showType: 'image',
				type : 'start',
				name : {text:'<<start>>'},
				text : {text:'start'},
				img : {src : '../../images/start.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					name: {name:'name',label: '名称', value:'start', editor:  newInput},
				    preInterceptors: {name:'preInterceptors', label : '前置拦截器', value:'', editor:  newInput},
					postInterceptors: {name:'postInterceptors', label : '后置拦截器', value:'', editor:  newInput}
				}},
			end : {
				showType: 'image',
				type : 'end',
				name : {text:'<<end>>'},
				text : {text:'end'},
				img : {src : '../../images/end.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					name: {name:'name',label: '名称', value:'end', editor:  newInput},
				    preInterceptors: {name:'preInterceptors', label : '前置拦截器', value:'', editor:  newInput},
					postInterceptors: {name:'postInterceptors', label : '后置拦截器', value:'', editor:  newInput}
				}},
			task : {
				showType: 'text',
				type : 'task',
				name : {text:'<<task>>'},
				text : {text:'task'},
				img : {src : 'images/48/task_empty.png',width :48, height:48},
				props : {
					name: {name:'name',label: '名称', value:'', editor:  newInput},
					displayName: {name:'displayName',label: '显示名称', value:'', editor: newText},
					form: {name:'form', label : '表单', value:'', editor:  newInput},
					assignee: {name:'assignee', value:''},
					assigneeDisplay: {name:'assigneeDisplay', label: '参与者', value:'', editor: function(){return new ajflow.editors.assigneeEditor('/dialogs/selectDialog.jsp?type=orgUserTree');}},
					assignmentHandler: {name:'assignmentHandler', label: '参与者处理类', value:'', editor:  newInput},
					taskType: {name:'taskType', label : '任务类型', value:'', editor: function(){return new ajflow.editors.selectEditor([{name:'主办任务',value:'Major'},{name:'协办任务',value:'Aidant'}]);}},
					performType: {name:'performType', label : '参与类型', value:'', editor: function(){return new ajflow.editors.selectEditor([{name:'普通参与',value:'ANY'},{name:'会签参与',value:'ALL'}]);}},
				    preInterceptors: {name:'preInterceptors', label : '前置拦截器', value:'', editor:  newInput},
					postInterceptors: {name:'postInterceptors', label : '后置拦截器', value:'', editor:  newInput},
				    reminderTime: {name:'reminderTime', label : '提醒时间', value:'', editor:  newInput},
				    reminderRepeat: {name:'reminderRepeat', label : '重复提醒间隔', value:'', editor:  newInput},
					expireTime: {name:'expireTime', label: '期望完成时间', value:'', editor:  newInput},
					autoExecute: {name:'autoExecute', label : '是否自动执行', value:'', editor: function(){return new ajflow.editors.selectEditor([{name:'否',value:'N'},{name:'是',value:'Y'}]);}},
					callback: {name:'callback', label : '回调处理', value:'', editor:  newInput}
				}},
			custom : {
				showType: 'text',
				type : 'custom',
				name : {text:'<<custom>>'},
				text : {text:'custom'},
				img : {src : 'images/48/task_empty.png',width :48, height:48},
				props : {
					name: {name:'name',label: '名称', value:'', editor:  newInput},
					displayName: {name:'displayName',label: '显示名称', value:'', editor: newText},
					clazz: {name:'clazz', label: '类路径', value:'', editor:  newInput},
					methodName: {name:'methodName', label : '方法名称', value:'', editor:  newInput},
					args: {name:'args', label : '参数变量', value:'', editor:  newInput},
				    preInterceptors: {name:'preInterceptors', label : '前置拦截器', value:'', editor:  newInput},
					postInterceptors: {name:'postInterceptors', label : '后置拦截器', value:'', editor:  newInput}
				}},
			subprocess : {
				showType: 'text',
				type : 'subprocess',
				name : {text:'<<subprocess>>'},
				text : {text:'subprocess'},
				img : {src : '../../images/process.png',width :48, height:48},
				props : {
					name: {name:'name',label: '名称', value:'', editor:  newInput},
					displayName: {name:'displayName',label: '显示名称', value:'', editor: newText},
					processName: {name:'processName', label: '子流程名称', value:'', editor:  newInput},
				    preInterceptors: {name:'preInterceptors', label : '前置拦截器', value:'', editor:  newInput},
					postInterceptors: {name:'postInterceptors', label : '后置拦截器', value:'', editor:  newInput}
				}},
			decision : {
				showType: 'image',
				type : 'decision',
				name : {text:'<<decision>>'},
				text : {text:'decision'},
				img : {src : '../../images/fork.png',width :48, height:48},
				props : {
					name: {name:'name',label: '名称', value:'', editor:  newInput},
					expr: {name:'expr',label: '决策表达式', value:'', editor:  newInput},
					handleClass: {name:'handleClass', label: '处理类名称', value:'', editor:  newInput},
				    preInterceptors: {name:'preInterceptors', label : '前置拦截器', value:'', editor:  newInput},
					postInterceptors: {name:'postInterceptors', label : '后置拦截器', value:'', editor:  newInput}
				}},
			fork : {
				showType: 'image',
				type : 'fork',
				name : {text:'<<fork>>'},
				text : {text:'fork'},
				img : {src : '../../images/fork.png',width :48, height:48},
				props : {
					name: {name:'name',label: '名称', value:'', editor:  newInput},
				    preInterceptors: {name:'preInterceptors', label : '前置拦截器', value:'', editor:  newInput},
					postInterceptors: {name:'postInterceptors', label : '后置拦截器', value:'', editor:  newInput}
				}},
			join : {
				showType: 'image',
				type : 'join',
				name : {text:'<<join>>'},
				text : {text:'join'},
				img : {src : '../../images/join.png',width :48, height:48},
				props : {
					name: {name:'name',label: '名称', value:'', editor:  newInput},
				    preInterceptors: {name:'preInterceptors', label : '前置拦截器', value:'', editor:  newInput},
					postInterceptors: {name:'postInterceptors', label : '后置拦截器', value:'', editor:  newInput}
				}
			}
		}
	},
	props:{// 属性编辑器
		attr:{top:328,left:10},
		props:{
			name : {name:'name', label:'名称', value:'', editor: newInput},
			displayName : {name:'displayName', label:'显示名称', value:'', editor: newInput},
			expireTime : {name:'expireTime', label:'期望完成时间', value:'', editor: newInput},
			instanceUrl : {name:'instanceUrl', label:'实例启动Url', value:'', editor: newInput},
			instanceNoClass : {name:'instanceNoClass', label:'实例编号生成类', value:'', editor: newInput}
		}
	},
	restore: "",
	activeRects:{// 当前激活状态
		rects:[],
		rectAttr:{stroke:"#ff0000","stroke-width":2}
	},
	historyRects:{
		rects:[],
        rectAttr:{stroke:"#00ff00","stroke-width":2},
		pathAttr:{
			path:{stroke:"#00ff00"},
			arrow:{stroke:"#00ff00",fill:"#00ff00"}
		}
	}
};

ajflow.util = {
	/**
	 * 三个点是否在一条直线上
	 */
    isLine(p1, p2, p3) {
        var s, p2y;

        if ((p1.x - p3.x) == 0)
            s = 1;
        else
            s = (p1.y - p3.y) / (p1.x - p3.x);

        p2y = (p2.x - p3.x) * s + p3.y;
        // $('body').append(p2.y+'-'+p2y+'='+(p2.y-p2y)+', ');
        if ((p2.y - p2y) < 10 && (p2.y - p2y) > -10) {
            p2.y = p2y;

            return true;
        }

        return false;
    },
    center(p1, p2) {// 两个点的中间点
        return {
            x: (p1.x - p2.x) / 2 + p2.x,
            y: (p1.y - p2.y) / 2 + p2.y
        };
    },
    nextId: (function() {
        var uid = 0;
        return function() {
            return ++uid;
        }
    })(),

	/**
	 * 计算矩形中心到 p 的连线与矩形的交叉点
	 */
    connPoint(rect, p) {
        var start = p, end = {
            x: rect.x + rect.width / 2,
            y: rect.y + rect.height / 2
        };

        // 计算正切角度
        var tag = (end.y - start.y) / (end.x - start.x);
        tag = isNaN(tag) ? 0 : tag;

        var rectTag = rect.height / rect.width;
        // 计算箭头位置
        var xFlag = start.y < end.y ? -1 : 1, yFlag = start.x < end.x ? -1  : 1, arrowTop, arrowLeft;

        // 按角度判断箭头位置
        if (Math.abs(tag) > rectTag && xFlag == -1) {// top边
            arrowTop = end.y - rect.height / 2;
            arrowLeft = end.x + xFlag * rect.height / 2 / tag;
        } else if (Math.abs(tag) > rectTag && xFlag == 1) {// bottom边
            arrowTop = end.y + rect.height / 2;
            arrowLeft = end.x + xFlag * rect.height / 2 / tag;
        } else if (Math.abs(tag) < rectTag && yFlag == -1) {// left边
            arrowTop = end.y + yFlag * rect.width / 2 * tag;
            arrowLeft = end.x - rect.width / 2;
        } else if (Math.abs(tag) < rectTag && yFlag == 1) {// right边
            arrowTop = end.y + rect.width / 2 * tag;
            arrowLeft = end.x + rect.width / 2;
        }

        return {
            x: arrowLeft,
            y: arrowTop
        };
    },

    arrow(p1, p2, r) {// 画箭头，p1 开始位置,p2 结束位置, r前头的边长
        var atan = Math.atan2(p1.y - p2.y, p2.x - p1.x) * (180 / Math.PI);

        var centerX = p2.x - r * Math.cos(atan * (Math.PI / 180));
        var centerY = p2.y + r * Math.sin(atan * (Math.PI / 180));

        var x2 = centerX + r * Math.cos((atan + 120) * (Math.PI / 180));
        var y2 = centerY - r * Math.sin((atan + 120) * (Math.PI / 180));

        var x3 = centerX + r * Math.cos((atan + 240) * (Math.PI / 180));
        var y3 = centerY - r * Math.sin((atan + 240) * (Math.PI / 180));

        return [p2, {
            x : x2, y : y2
        }, {
            x : x3, y : y3
        }];
    },

    tip(rect, name) { 
    	var ar = ajflow.config.activeRects, matched = false;

    	for(var u=0;u<ar.rects.length;u++){
			if(ar.rects[u].name == name)
				matched = true;
    	}

    	if(!matched) return;

        var tipDIV = document.getElementById("tipDIV");
        if (tipDIV) 
            document.body.removeChild(tipDIV);
        
        tipDIV = document.createElement("tipDIV");
        tipDIV.id = "tipDIV";
        tipDIV.style.styleFloat="left";
        tipDIV.style.overflow="hidden";
        tipDIV.style.left=document.getElementById('snakerflow').offsetLeft+rect.attr('x');
        tipDIV.style.top=document.getElementById('snakerflow').offsetTop+rect.attr('y')+52;
        tipDIV.style.width=180;
        tipDIV.style.height=60;
        tipDIV.style.position="absolute";
        tipDIV.style.backgroundColor="#FFE699";

        $.ajax({
            type:'GET',
            url:ajflow.config.ctxPath+"/snaker/task/tip",
            data:"orderId=" + ajflow.config.orderId + "&taskName=" + name,//_o.props['name'].value,
            async: false,
            error: function(){
                alert('数据处理错误！');
                return false;
            },
            success: function(data){
                //tipDIV.innerHTML="<div style='width:180px; height:20px;border: 1px solid #d2dde2;'><a href='javascript:void(0)' onclick='addTaskActor(\"" + name + "\");' class='btnAdd'></a><a href='javascript:void(0)' class='btnClock'></a><a href='javascript:void(0)' onclick='document.body.removeChild(document.getElementById(\"tipDIV\"))' class='btnDel'></a></div><div style='width:180px; height:40px;border: 1px solid #d2dde2;'><div id='currentActorDIV' style='width:180px; height:20px;'>参与者:" + data.actors + "</div><div id='arrivalDIV' style='width:180px; height:20px;'>抵达时间:" + data.createTime + "</div></div>";
            	tipDIV.innerHTML="<div style='width:180px; height:20px;border: 1px solid #d2dde2;'><a href='javascript:void(0)' onclick='document.body.removeChild(document.getElementById(\"tipDIV\"))' class='btnDel'></a></div><div style='width:180px; height:40px;border: 1px solid #d2dde2;'><div id='currentActorDIV' style='width:180px; height:20px;'>参与者:" + data.actors + "</div><div id='arrivalDIV' style='width:180px; height:20px;'>抵达时间:" + data.createTime + "</div></div>";
                document.body.appendChild(tipDIV);
            }
        });
    }
};

