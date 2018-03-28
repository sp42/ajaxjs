// 画日历
function render(el) {
	// 用来保存日期列表
	var arr = getDateArr.call(this);
	// 清空原来的日期对象列表
	this.days = [];

	// 插入日期
	var frag = document.createDocumentFragment();

	while (arr.length) {
		var row = document.createElement("tr"); // 每个星期插入一个tr

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
					if (isSameDay(on, this.date)) {
						cell.classList.add('onToday');
						this.onToday && this.onToday(cell);
					}
					// 判断是否选择日期
					this.selectDay && this.onSelectDay && isSameDay(on, this.selectDay) && this.onSelectDay(cell);
				}
			}
			row.appendChild(cell);
		}
		frag.appendChild(row);
	}

	// 先清空内容再插入(ie的table不能用innerHTML)
//	while (el.hasChildNodes())
//		el.removeChild(el.firstChild);

	var tbody = el.querySelector("table tbody");
	tbody.innerHTML = '';
	tbody.appendChild(frag);
	el.querySelector(".showYear").innerHTML = this.year;
	el.querySelector(".showMonth").innerHTML = this.month;

	
//	el.querySelector(".idCalendarPre").onclick = this.PreMonth.bind(this);
//	el.querySelector(".idCalendarNext").onclick = this.NextMonth.bind(this);

//	el.querySelector(".preYear").onclick = this.PreYear.bind(this);
//	el.querySelector(".nextYear").onclick = this.NextYear.bind(this);
//	el.querySelector(".idCalendarNow").onclick = this.NowMonth.bind(this);
	
	this.onFinish && this.onFinish();
}

/**
 * 判断是否同一日
 */
function isSameDay(d1, d2) {
	return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
}

function getDateArr() {
	var arr = []
	// 用当月第一天在一周中的日期值作为 当月离第一天的天数
	for (var i = 1, firstDay = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++)
		arr.push(0);

	// 用当月最后一天在一个月中的日期值作为当月的天数
	for (var i = 1, monthDay = new Date(this.year, this.month, 0).getDate(); i <= monthDay; i++)
		arr.push(i);

	return arr;
}

function foo() {
	render.call(getDate('now'), document.querySelector(".calendar"));
	
	document.querySelector(".preYear").onclick  = function(){
		render.call(getDate('preYear'), document.querySelector(".calendar"));
	};
	document.querySelector(".nextYear").onclick = function(){
		render.call(getDate('nextYear'), document.querySelector(".calendar"));
	};
}


function getDate(dateType) {
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
}

foo();


