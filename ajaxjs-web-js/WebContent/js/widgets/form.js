/**
 * 表單控件
 */


// 图片验证码
Vue.component('aj-page-captcha', {
	props : {
		imgSrc : {
			type: String, // 生成图片验证码地址
			required: false,
		},
		
		fieldName : {	// 提交的字段名
			type: String,
			required: false,
			default : 'captcha'
		}
	},
	template : 
		'<table class="aj-page-captcha"><tr>\
			<td><input type="text" :name="fieldName" placeholder="输入右侧验证码" data-regexp="integer" required /></td>\
			<td style="vertical-align: top;">\
				<img :src="imgSrc || ajResources.ctx + \'/Captcha\'" @click="onClk($event);" title="点击刷新图片" />\
			</td>\
		</tr></table>',
	methods : {
		onClk : function(e) {
			var img = e.target;
			img.src = img.src.replace(/\?\d+$/, '') + '?' + new Date().valueOf();
		},
		refreshCode: function(){
			var img = this.$el.querySelector('img');
			img.src = img.src.replace(/\?\d+$/, '') + '?' + new Date().valueOf();
		}
	}
});


// 日期选择器
Vue.component('aj-form-calendar', {
	data: function() {
		return {
			year : 2017,
			month : 2,
			day : 1
		};
	},
	props : {

	},
	template : 
		'<div class="aj-form-calendar">\
			<div class="selectYearMonth">\
				<a href="#" @click="setYear(\'preYear\')" class="preYear">&lt;</a> \
				<select>\
					<option value="1">一月</option><option value="2">二月</option><option value="3">三月</option><option value="4">四月</option>\
					<option value="5">五月</option><option value="6">六月</option><option value="7">七月</option><option value="8">八月</option>\
					<option value="9">九月</option><option value="10">十月</option><option value="11">十一月</option><option value="12">十二月</option>\
				</select>\
				<a href="#" @click="setYear(\'nextYear\')" class="nextYear">&gt;</a>\
			</div>\
			<div class="showCurrentYearMonth">\
				<span class="showYear">{{year}}</span>/<span class="showMonth">{{month}}</span>\
			</div>\
			<table>\
				<thead>\
					<tr><td>日</td><td>一</td><td>二</td><td>三</td><td>四</td><td>五</td><td>六</td></tr>\
				</thead>\
				<tbody @click="pickDay($event);"></tbody>\
			</table>\
		</div>',
		
	    // 2012-07-08
	    // firefox中解析 new Date('2012/12-23') 不兼容，提示invalid date 无效的日期
		// Chrome下可以直接将其格式化成日期格式，且时分秒默认为零
// var arr = date.split('-'), now = new Date(arr[0], arr[1] - 1, arr[2], " ", "
// ", " ");
	mounted : function () {
		aj.apply(this, this.getDate('now'));
		this.render();
	},

	methods : {
		// 画日历
		render : function () {
			var el = this.$el;
			var arr = this.getDateArr();// 用来保存日期列表
			this.days = [];// 清空原来的日期对象列表

			var frag = document.createDocumentFragment();// 插入日期

			while (arr.length) {
				var row = document.createElement("tr"); // 每个星期插入一个 tr

				for (var i = 1; i <= 7; i++) { // 每个星期有7天
					var cell = document.createElement("td");
					if (arr.length) {
						var d = arr.shift();
						if (d) {
							cell.innerHTML = d;
							
							cell.className = 'day day_' + this.year + '-' + this.month + '-' + d;
							cell.title = this.year + '-' + this.month + '-' + d;
							
							this.days[d] = cell;
							var on = new Date(this.year, this.month - 1, d);

							// 判断是否今日
							if (this.isSameDay(on, this.date)) {
								cell.classList.add('onToday');
								this.onToday && this.onToday(cell);
							}
							
							// 判断是否选择日期
							this.selectDay && this.onSelectDay && this.isSameDay(on, this.selectDay) && this.onSelectDay(cell);
						}
					}
					
					row.appendChild(cell);
				}
				
				frag.appendChild(row);
			}

			// 先清空内容再插入(ie的table不能用innerHTML)
// while (el.hasChildNodes())
// el.removeChild(el.firstChild);

			var tbody = el.$("table tbody");
			tbody.innerHTML = '';
			tbody.appendChild(frag);

			
// el.querySelector(".idCalendarPre").onclick = this.PreMonth.bind(this);
// el.querySelector(".idCalendarNext").onclick = this.NextMonth.bind(this);


// el.querySelector(".idCalendarNow").onclick = this.NowMonth.bind(this);
			
			this.onFinish && this.onFinish();
		},
		setYear : function(type) {
			this.year = this.getDate(type).year;
		},
		nextYear : function() {
			
		},
		getDate: function (dateType) {
			var now = new Date(), date, nowYear = now.getFullYear(), nowMonth = now.getMonth() + 1;
			
			switch (dateType) {
				case 'now':// 当前月
					date = now;
					break;
				case 'preMonth':// 上一月
					date = new Date(nowYear, nowMonth - 2, 1);
					break;
				case 'nextMonth':// 下一月
					date = new Date(nowYear, nowMonth, 1);
					break;
				case 'preYear':// 上一年
					date = new Date(nowYear - 1, nowMonth - 1, 1);
					break;
				case 'nextYear':// 下一年
					date = new Date(nowYear + 1, nowMonth - 1, 1);
					break;
			}
			
			return {
				date : date,
				year : date.getFullYear(),
				month : date.getMonth() + 1,
			};
		},
	
		getDateArr: function () {
			var arr = [];
			// 用当月第一天在一周中的日期值作为 当月离第一天的天数
			for (var i = 1, firstDay = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++)
				arr.push(0);

			// 用当月最后一天在一个月中的日期值作为当月的天数
			for (var i = 1, monthDay = new Date(this.year, this.month, 0).getDate(); i <= monthDay; i++)
				arr.push(i);

			return arr;
		},
		
		pickDay : function(e) {
			var el = e.target, date = el.title;
			this.$emit('pick-date', date);
			return date;
		},

		/**
		 * 判断是否同一日
		 */
		isSameDay: function (d1, d2) {
			return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
		}
	}
});

// 帶有 input 輸入框的
Vue.component('aj-form-calendar-input', {
	data : function(){
		return {
			date : this.fieldValue
		}
	},
	props : {
		fieldName : { // 表单 name，字段名
			type: String,
			required: true
		},
		fieldValue : {
			type: String,// 表单值，可选的
			required: false
		},
		
		positionFixed : Boolean // 是否采用固定定位
	},
	
	template : 
		'<div class="aj-form-calendar-input" @mouseover="onMouseOver($event)">\
			<div class="icon" ><div class="menu"></div></div>\
			<input placeholder="请输入日期" :name="fieldName" :value="date" type="text" />\
			<aj-form-calendar @pick-date="recEvent"></aj-form-calendar>\
		</div>',
	mounted : function() {
		if(this.positionFixed) {
			this.$el.$('.aj-form-calendar').classList.add('positionFixed');
		}
	},
	methods : {
		recEvent: function(date) {
			this.date = date;
		},
		
		onMouseOver : function(e) {
			if(this.positionFixed) {
				var el = e.currentTarget;
				var b = el.getBoundingClientRect();
				var c = this.$el.$('.aj-form-calendar');
					c.style.top  = (b.top + el.clientHeight - 0) + 'px';
					c.style.left = ((b.left - 0) + 0) +  'px';
			}
		}
	}
});

// HTML 在綫編輯器
// 注意：必须提供一个 <slot> 包含有 <textarea class="hide"
// name="content">${info.content}</textarea>
Vue.component('aj-form-html-editor', {
	template : '',
	props : {
		fieldName : { // 表单 name，字段名
			type: String,
			required: true
		},
		content : { // 内容
			type: String,
			required: false
		},
		basePath : { // iframe 的 <base href="${param.basePath}/" />路徑
			type: String,
			required: false,
			default : ''
		}
	},
	beforeCreate : function() {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", this.ajResources.libraryUse + '/htmleditor-tag.htm', false);// 同步方式请求
		xhr.send(null);
		this.$options.template = xhr.responseText;
	},
	mounted : function() {
		var el = this.$el;
		this.iframeEl 	  = el.$('iframe');
		this.sourceEditor = el.$('textarea');
		this.iframeWin 	  = this.iframeEl.contentWindow;
		this.mode         = 'iframe'; // 当前可视化编辑 iframe|textarea
		
		this.toolbarEl = el.$('.toolbar');
		
		// 这个方法只能写在 onload 事件 不写 onload 里还不执行
		this.iframeWin.onload = function() {
			this.iframeDoc 	= this.iframeWin.document;
			this.iframeDoc.designMode = 'on';
			this.iframeBody = this.iframeDoc.body;
			// 有内容
			this.sourceEditor.value && this.setValue(this.sourceEditor.value);
		}.bind(this);
	},
	
	methods: {
		onToolBarClk : function(e) {
			var el = e.target, clsName = el.className;
			
			clsName = clsName.split(' ').shift();
			switch(clsName) {
			case 'createLink':
				var result = prompt("请输入 URL 地址");
				if(result)
					this.format("createLink", result);
				break;
			case 'insertImage':
				if(window.isCreate)
					aj.alert.show('请保存记录后再上传图片。');
				else {
					var self = this;
					App.$refs.uploadLayer.show(function(imgUrl) {
						if(imgUrl)
							self.format("insertImage", imgUrl);
					});
				}
				
				break;
			case 'switchMode':
				this.setMode();
				break;
			case 'cleanHTML':
				this.cleanHTML();
				break;
			default:
				this.format(clsName);
			}
		},
		
		format : function (type, para) {
			this.iframeWin.focus();
			
			if (!para) {
				if (document.all) 
					this.iframeDoc.execCommand(type);
				else 
					this.iframeDoc.execCommand(type, false, false);
			} else 
				this.iframeDoc.execCommand(type, false, para);
			
			this.iframeWin.focus();
		},
		
		insertEl : function (html) {// 重複？
			this.iframeDoc.body.innerHTML = html;
		},
		
		// 設置 HTML
		setValue : function(v) {
			var self = this;
			setTimeout(function() {
				self.iframeWin.document.body.innerHTML = v;
// self.iframeBody.innerHTML = v;
			}, 500);
		},
		
		// 獲取 HTML
		getValue : function(cfg) {
			var result = this.iframeBody.innerHTML;
			
			if(cfg && cfg.cleanWord)
				result = this.cleanPaste(result);
			
			if(cfg && cfg.encode)
				result = encodeURIComponent(result);
			
			return result;
		},
		
		// MSWordHtmlCleaners.js https://gist.github.com/ronanguilloux/2915995
		cleanPaste : function(html) {
		    // Remove additional MS Word content
		    html = html.replace(/<(\/)*(\\?xml:|meta|link|span|font|del|ins|st1:|[ovwxp]:)((.|\s)*?)>/gi, ''); // Unwanted
																												// tags
		    html = html.replace(/(class|style|type|start)=("(.*?)"|(\w*))/gi, ''); // Unwanted
																					// sttributes
		    html = html.replace(/<style(.*?)style>/gi, '');   // Style tags
		    html = html.replace(/<script(.*?)script>/gi, ''); // Script tags
		    html = html.replace(/<!--(.*?)-->/gi, '');        // HTML comments
		    
		    return html;
		},
		
		// 切換 HTML 編輯 or 可視化編輯
		setMode : function () {
			if (this.mode == 'iframe') {
				this.iframeEl.classList.add('hide');
				this.sourceEditor.classList.remove('hide');
				this.sourceEditor.value = this.iframeBody.innerHTML;
				this.mode = 'text';
				this.grayImg(true);
			} else {
				this.iframeEl.classList.remove('hide');
				this.sourceEditor.classList.add('hide');
				this.iframeBody.innerHTML = this.sourceEditor.value;
				this.mode = 'iframe';
				this.grayImg(false);
			}
		},
		
		// 使图片灰色
		grayImg: function (isGray) {
			this.toolbarEl.$('span', function(item) {
				if(item.className.indexOf('switchMode') != -1) {
					item.style.color = isGray ? 'red' : '';
				} else {
					item.style.filter = isGray ? 'grayscale(100%)' : '';
				}
			});
		},
		
		onFontfamilyChoserClk : function(e) {
			var el = e.target; 
			
			this.format('fontname', el.innerHTML);
			// 如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：
			var menuPanel = el.parentNode;
			menuPanel.style.display = 'none';

			setTimeout(function() {
				menuPanel.style.display = '';
			}, 300);
		},
		
		onFontsizeChoserClk : function(e) {	
			var el = e.target;
			for(var els = e.currentTarget.children, i = 0, j = els.length; i < j; i++)
				if(el == els[i]) break;
			
			this.format('fontsize', i);
		},
		
		onFontColorPicker(e) {
			var color = e.target.title;
			this.format('foreColor', color);
		},
		
		onFontBgColorPicker(e) {
			var color = e.target.title;
			this.format('backColor', color);
		},
		
		createColorPickerHTML() {
			// 定义变量
			var cl = ['00', '33', '66', '99', 'CC', 'FF'], a, b, c, d, e, f, i, j, k, T;
			// 创建head
			var h = '<div class="colorhead"><span class="colortitle">颜色选择</span></div>\
						<div class="colorbody"><table cellspaci="0" cellpadding="0"><tr>';// 创建body
																							// [6 x
																							// 6的色盘]
			
			for (var i = 0; i < 6; ++i) {
				h += '<td><table class="colorpanel" cellspacing="0" cellpadding="0">';
				for (var j = 0, a = cl[i]; j < 6; ++j) {
					h += '<tr>';
					for (var k = 0, c = cl[j]; k < 6; ++k) {
						b = cl[k];
						e = k == 5 && i != 2 && i != 5 ? ';border-right:none;' : '';
						f = j == 5 && i < 3 ? ';border-bottom:none' : '';
						d = '#' + a + b + c;
						T = document.all ? '&nbsp;' : '';
						h += '<td unselectable="on" style="background-color: ' + d + e + f + '" title="' + d + '">' + T + '</td>'; 
					}
					h += '</tr>';
				}
				h += '</table></td>';
				if (cl[i] == '66') h += '</tr><tr>';
			}
			
			h += '</tr></table></div>';
			
			return h;
		},
		
		// 清理冗余 HTML
		cleanHTML(){
		    // 类似于 白名单
		    var tagsAllowed = "|h1|h2|h3|p|div|a|b|strong|br|ol|ul|li|pre|img|br|hr|font|";

		    var attributesAllowed = {};
		    attributesAllowed["div"] = "|id|class|";
		    attributesAllowed["a"] = "|id|class|href|name|";
		    attributesAllowed["img"] = "|src|";

		    this.everyNode(this.iframeBody, node => {
		    	var isDelete = false;
		    	
		    	if (node.nodeType === 1) {
		    		var tag = node.tagName.toLowerCase();
		    		if (tagsAllowed.indexOf("|" + tag + "|") === -1)
		    			isDelete = true;
		    		
		    		if (!isDelete) { // 删除属性
		    			var attrs = node.attributes;

		    			for(var i = attrs.length - 1; i >= 0; i--) {
		    				var name = attrs[i].name;
		    				if (attributesAllowed[tag] == null || attributesAllowed[tag].indexOf("|" + name.toLowerCase() + "|") == -1){
		    					node.removeAttribute(name);
		    				}
		    			}
		    		}
		    	} else if (node.nodeType === 8) {// 删除注释
		    		isDelete = true;
		    	}
		    	
		    	return isDelete;
		    });

		},
		// 遍历节点
	    everyNode (el, fn) {
	        var objChildNode = el.firstChild;
	        while (objChildNode) {
	            if (fn(objChildNode)) { // 返回 true 则删除
	                var next = objChildNode.nextSibling;
	                el.removeChild(objChildNode);
	                objChildNode = next;
	            } else {
	                if (objChildNode.nodeType === 1) 
	                    this.everyNode(objChildNode, fn);

	                objChildNode = objChildNode.nextSibling;
	            }
	        }
	    }
	}
});
