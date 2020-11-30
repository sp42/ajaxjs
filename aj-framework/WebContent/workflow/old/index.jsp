<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%
	request.setAttribute("ctx", "/aj-wf-demo");
	request.setAttribute("developing_js_url", "/ajaxjs-js");
%>
<!-- 通用头部 -->
<%@include file="/WEB-INF/jsp/head.jsp"%>

<title>流程展现</title>
<link rel="stylesheet" href="css/snaker.css" type="text/css" media="all" />
<script src="js/lib/raphael-min.js"></script>
<script src="js/lib/jquery.js"></script>
<script src="js/util.js"></script>
<script src="js/model.js"></script>
<script src="js/dot.js"></script>
<script src="js/rect.js"></script>
<script src="js/path.js"></script>
<script src="js/editors.js"></script>
<script src="js/serialize.js"></script>
</head>
<body>
	<div class="aj-workflow">
		<div class="canvas"></div>

		<fieldset class="toolbox" draggable="true">
			<legend>工具箱</legend>
			<ul>
				<li><img src="js/snaker/images/select16.gif" /> 选择 Select</li>
				<li><img src="js/snaker/images/16/flow_sequence.png" /> 流转
					Transition</li>
				<li>
					<hr />
				</li>
				<li class="state" data-type="start"><img src="images/start.png" />
					开始节点 Start</li>
				<li class="state" data-type="end"><img src="images/end.png" />
					结束节点 End</li>
				<li class="state" data-type="task"><img
					src="images/process.png" /> 任务节点 Task</li>
				<li class="state" data-type="custom"><img
					src="images/process.png" /> 自定义节点 Custom</li>
				<li class="state" data-type="subprocess"><img
					src="images/process.png" /> 子流程 Subprocess</li>
				<li class="state" data-type="decision"><img
					src="images/fork.png" /> 抉择节点 Decision</li>
				<li class="state" data-type="fork"><img src="images/fork.png" />
					分支节点 Fork</li>
				<li class="state" data-type="join"><img src="images/join.png" />
					合并节点 Join</li>
				<li><hr /></li>
				<li @click="toXML"><img src="js/snaker/images/save.gif" /> 保存
					Save</li>
			</ul>
		</fieldset>

		<fieldset class="prop" draggable="true">
			<legend>属性</legend>
			<div class="property">
				<table>
					<tr v-for="(value, key, index) in property"
						v-if="value.name && value.label">
						<td class="key">{{value.label}}</td>
						<td><input type="text" v-model="value.value" /></td>
					</tr>
				</table>
			</div>
		</fieldset>
	</div>




	<div id="properties">
		<div id="properties_handle">属性</div>
		<table class="properties_all" cellpadding="0" cellspacing="0">
		</table>
		<div>&nbsp;</div>
	</div>

	<div id="snakerflow"></div>
	<script>
		CONFIG = {
			basePath : "js/snaker/",
			ctxPath : "${ctx}",
			restore : ({
				states : {
					start1 : {
						type : 'start',
						text : {
							text : 'start1'
						},
						attr : {
							x : 204,
							y : 124,
							width : 50,
							height : 50
						},
						props : {
							displayName : {
								value : 'start1'
							},
							layout : {
								value : '24,124,-1,-1'
							},
							name : {
								value : 'start1'
							}
						}
					},
					end1 : {
						type : 'end',
						text : {
							text : 'end1'
						},
						attr : {
							x : 750,
							y : 124,
							width : 50,
							height : 50
						},
						props : {
							displayName : {
								value : 'end1'
							},
							layout : {
								value : '570,124,-1,-1'
							},
							name : {
								value : 'end1'
							}
						}
					},
					apply : {
						type : 'task',
						text : {
							text : '请假申请'
						},
						attr : {
							x : 297,
							y : 122,
							width : 100,
							height : 50
						},
						props : {
							assignee : {
								value : 'apply.operator'
							},
							displayName : {
								value : '请假申请'
							},
							form : {
								value : '/flow/leave/apply'
							},
							layout : {
								value : '117,122,-1,-1'
							},
							name : {
								value : 'apply'
							},
							performType : {
								value : 'ANY'
							},
							taskType : {
								value : 'Major'
							}
						}
					},
					approveDept : {
						type : 'task',
						text : {
							text : '部门经理审批'
						},
						attr : {
							x : 452,
							y : 122,
							width : 100,
							height : 50
						},
						props : {
							assignee : {
								value : 'approveDept.operator'
							},
							displayName : {
								value : '部门经理审批'
							},
							form : {
								value : '/flow/leave/approveDept'
							},
							layout : {
								value : '272,122,-1,-1'
							},
							name : {
								value : 'approveDept'
							},
							performType : {
								value : 'ANY'
							},
							taskType : {
								value : 'Major'
							}
						}
					},
					decision1 : {
						type : 'decision',
						text : {
							text : 'decision1'
						},
						attr : {
							x : 606,
							y : 124,
							width : 50,
							height : 50
						},
						props : {
							displayName : {
								value : 'decision1'
							},
							expr : {
								value : '#day #5 2 ? #1transition5#1 : #1transition4#1'
							},
							layout : {
								value : '426,124,-1,-1'
							},
							name : {
								value : 'decision1'
							}
						}
					},
					approveBoss : {
						type : 'task',
						text : {
							text : '总经理审批'
						},
						attr : {
							x : 584,
							y : 231,
							width : 100,
							height : 50
						},
						props : {
							assignee : {
								value : 'approveBoss.operator'
							},
							displayName : {
								value : '总经理审批'
							},
							form : {
								value : '/flow/leave/approveBoss'
							},
							layout : {
								value : '404,231,-1,-1'
							},
							name : {
								value : 'approveBoss'
							},
							performType : {
								value : 'ANY'
							},
							taskType : {
								value : 'Major'
							}
						}
					}
				},
				paths : {
					transition1 : {
						from : 'start1',
						to : 'apply',
						dots : [{x:100, y:50}, {x:150, y:10}],  
						text : {
							text : ''
						},
						textPos : {
							x : 0,
							y : 0
						},
						props : {
							name : {
								value : 'transition1'
							},
							expr : {
								value : ''
							}
						}
					},
					transition2 : {
						from : 'apply',
						to : 'approveDept',
						dots : [],
						text : {
							text : ''
						},
						textPos : {
							x : 0,
							y : 0
						},
						props : {
							name : {
								value : 'transition2'
							},
							expr : {
								value : ''
							}
						}
					},
					transition3 : {
						from : 'approveDept',
						to : 'decision1',
						dots : [],
						text : {
							text : ''
						},
						textPos : {
							x : 0,
							y : 0
						},
						props : {
							name : {
								value : 'transition3'
							},
							expr : {
								value : ''
							}
						}
					},
					transition4 : {
						from : 'decision1',
						to : 'end1',
						dots : [],
						text : {
							text : '<=2天'},textPos:{x:0,y:0}, props:{name:{value:'transition4'},expr:{value:''}}},transition5:{from:'decision1',to:'approveBoss', dots:[],text:{text:'>2天'
						},
						textPos : {
							x : 0,
							y : 0
						},
						props : {
							name : {
								value : 'transition5'
							},
							expr : {
								value : ''
							}
						}
					},
					transition6 : {
						from : 'approveBoss',
						to : 'end1',
						dots : [],
						text : {
							text : ''
						},
						textPos : {
							x : 0,
							y : 0
						},
						props : {
							name : {
								value : 'transition6'
							},
							expr : {
								value : ''
							}
						}
					}
				},
				props : {
					props : {
						name : {
							name : 'name',
							value : 'leave'
						},
						displayName : {
							name : 'displayName',
							value : '请假流程测试'
						},
						expireTime : {
							name : 'expireTime',
							value : ''
						},
						instanceUrl : {
							name : 'instanceUrl',
							value : '/snaker/flow/all'
						},
						instanceNoClass : {
							name : 'instanceNoClass',
							value : ''
						}
					}
				}
			}),
			formPath : "forms/"
		};
	</script>
	<script src="js.js"></script>
</body>
</html>
