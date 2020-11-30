TEST_DATA = {
	states : {
		start1 : {
			type : 'start',
			text : {
				text : 'start1'
			},
			attr : {
				x : 104,
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
}