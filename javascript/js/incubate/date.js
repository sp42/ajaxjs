;(function() {
	//------------------------------------
	//--------------时钟------------------
	//------------------------------------
	function toDouble(iNum){
	    return iNum < 10 ? ' 0' + iNum : ' ' + iNum;
	}

	function getTimeArray(){
	    var oDate = new Date, iYear = oDate.getYear(), iMonth = oDate.getMonth(), iDay = oDate.getDate(), iHour = oDate.getHours(), iMin = oDate.getMinutes(), iSec = oDate.getSeconds(), iWeek = oDate.getDay();
	    
	    if (iYear < 1900)iYear += 1900;

	    var str = iYear + toDouble(iMonth + 1) + toDouble(iDay) + toDouble(iHour) + toDouble(iMin) + toDouble(iSec) + ' ' + iWeek;

	    var aChar = str.split(' '), aNumber = [];
	    for (var i = 0, j = aChar.length; i < j; i++)
	        aNumber[i] = parseInt(aChar[i]);
	   
	    return aNumber;
	}
	var aWeekName = ["日", "一", "二", "三","四", "五", "六"];
	var tpl = "{0}年{1}月{2}日 {3}:{4}:{5} 星期{6}";
	
	window.bf_date = {};
	bf_date.clock = {
		init : function(el){
			function init(){
			    var arr = getTimeArray();
			    arr[arr.length - 1] = aWeekName[arr[arr.length - 1]];// 设置最后的“星期几”
			    el.innerHTML = tpl.format(arr);
			}

			init(); // 立刻执行，要不然得一秒之后。
			window.setInterval(init, 1000);
		}
	};

	//------------------------------------
	//--------------倒计时----------------
	//------------------------------------
	bf_date.countDown = {
		init : function(){
			var fn = fresh.bind(this);
			fn();
			this.timer = window.setInterval(fn, 1000);
		},
		el : null,
		endDate  : null 	// 结束时间
	};

	function fresh(){
		var toInt = window.parseInt,
			leftsecond = this.endDate.getTime() - new Date().getTime(),
			leftsecond = leftsecond / 1000,
			leftsecond = toInt(leftsecond),
			__d = toInt(leftsecond  / 3600  / 24),
			__h = toInt((leftsecond / 3600) % 24),
			__m = toInt((leftsecond / 60)   % 60),
			__s = toInt(leftsecond % 60),
			leftsecondObj = {
				d : __d, h : __h, m : __m, s : __s
			};

		if(this.onLeft)this.onLeft(leftsecondObj);
		else this.el.innerHTML = "{d}天{h}小时{m}分{s}秒".format(leftsecondObj);

		if (leftsecond <= 0) {
			if(this.onLeftEnd)this.onLeftEnd();
			else this.el.innerHTML = "抢购已结束";
			window.clearInterval(this.timer);
		}
	}


	/*
	 * JavaScript Pretty Date
	 * Copyright (c) 2011 John Resig (ejohn.org)
	 * Licensed under the MIT and GPL licenses.
	 */

	// Takes an ISO time and returns a string representing how
	// long ago the date represents.
	function prettyDate(time){
		var date = new Date((time || "").replace(/-/g,"/").replace(/[TZ]/g," ")),
			diff = (((new Date()).getTime() - date.getTime()) / 1000),
			day_diff = Math.floor(diff / 86400);
				
		if ( isNaN(day_diff) || day_diff < 0 || day_diff >= 31 )
			return;
				
		return day_diff == 0 && (
				diff < 60 && "just now" ||
				diff < 120 && "1 minute ago" ||
				diff < 3600 && Math.floor( diff / 60 ) + " minutes ago" ||
				diff < 7200 && "1 hour ago" ||
				diff < 86400 && Math.floor( diff / 3600 ) + " hours ago") ||
			day_diff == 1 && "Yesterday" ||
			day_diff < 7 && day_diff + " days ago" ||
			day_diff < 31 && Math.ceil( day_diff / 7 ) + " weeks ago";
	}

	function addZero(n){
		switch(typeof n){
			case 'number':
				if(n > 0){
					if(n <= 9)n = "0" + n;	
					return String(n);
				}else{
					return "00";	
				}
			break;
			case 'string':
				return n.length == 1 ? "0" + n : n;
			break;
		}
	}
})();