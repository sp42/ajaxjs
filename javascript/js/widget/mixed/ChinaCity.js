(function() {
	var ChinaData = ChinaCity.data;

	ChinaCity = {
		selectUIs : [], // 联动控件
		selectData : [], // 已选择的数据
		levelCount : 2, // int 级数，从 0 开始计
		showAllChoice : false,
		init : function() {
			JSON.Path.tree = ChinaData;
			// console.log(JSON.Path.hashSearch('/北京市/市辖区/'));

			if (this.selectData && this.selectData.length) {
				initSelect.call(this, ChinaData, 0);
				// 各级控件要显示的数据（都是已选中的）
				var _data = JSON.Path.hashSearch(this.selectData);

				if (_data[0]) initSelect.call(this, _data[0].sub, 1);
				if (_data[1]) initSelect.call(this, _data[1].sub, 2);
				else {
					// @todo 显示下一级别
				}
			} else reset.call(this, ChinaData, 0); // 没有默认值

			this.selectUIs[0].onclick = this.selectUIs[1].onclick = this.selectUIs[2].onclick = onClk.bind(this);
		}
	};

	function initSelect(arr, levelIndex) {
		var container = this.selectUIs[levelIndex], 
		selectedValue = this.selectData[levelIndex];
	    container.innerHTML = ""; 						//删除所有已设置的option
	    
	    for(var i = 0, j = arr.length; i < j; i++){
	        if(levelIndex != this.levelCount){			// 不是最后一级，仅列出选中的
				if(this.showAllChoice)					// 显示全部选项（显示选中的，其他的也不显示）
					createItem.call(container, arr[i], selectedValue);
				else if(arr[i].name == selectedValue) { // 显示选中的，其他不显示
	            	var span;
	            	span = createItem.call(container, arr[i], true);
	            	span.innerHTML += "×";				// 不是最后一级 可以撤销，
	        		onGotoParentClk.scope = this;		// 撤销的事件
	        		span.onclick = onGotoParentClk.delegate(null, arr, levelIndex);
	        		break;								// 只显示一个，于是无须再遍历了
	            }
	    	}else if(levelIndex == this.levelCount){	// 最后一级，列出所有即可
	    		createItem.call(container, arr[i], selectedValue);
	    	}
	    }
	
	    return selectedValue;
	}

	// arr[], levelIndex:number 把列表中的所有元素都展现出来
	function reset(arr, levelIndex) {
		if (!arr)
			return;
		var container;
		container = this.selectUIs[levelIndex];
		container.innerHTML = ""; // 删除所有已设置的option

		for (var i = 0, j = arr.length; i < j; i++)
			createItem.call(container, arr[i]);

		// 打后的都隐藏
		for (var i = levelIndex + 1, j = this.selectUIs.length; i < j; i++)
			this.selectUIs[i].innerHTML = "";
	}

	// 创建元素
	function createItem(data, selectedValue) {
		var item;
		item = document.createElement("span");
		// item = elWrapper(item);
		item.innerHTML = item.title = data.name;
		// 显示选中的，如果两者名称相符，则是为选中的。
		// 如果 selectedValue === true，则强制加上 选中 状态
		if (data.name == selectedValue || selectedValue === true)
			item.addCls("selected");

		this.appendChild(item);// this = container

		return item;
	}
	function onClk(e) {
		var span = e.target;
		var container = span.parentNode, 
			sNodes = container.children, 
			sNode, 
			levelIndex = this.selectUIs.indexOf(container); // 控件在级联控件的索引，刚点击的是出于第几级的

		if (levelIndex == -1)
			return;

		var currentValue = this.selectData[levelIndex] = span.innerHTML;// 刚才选择的记录。每次单击都保存用户选择的记录

		if (this.levelCount != levelIndex) {
			var data = levelIndex == 0 ? ChinaData : JSON.Path.getNode("/"
					+ this.selectData[levelIndex - 1]).sub;
			initSelect.call(this, data, levelIndex);

			var data = levelIndex == 0 ? JSON.Path.getNode("/" + currentValue).sub // 最开始的控件
					: JSON.Path.getNode(this.selectData[levelIndex - 1], currentValue).sub // 之前的数据

			this.selectData[levelIndex + 1] = reset.call(this, data, levelIndex + 1); // 下一个控件
		} else if (this.levelCount == levelIndex) {
			// 最后一个控件，不联动，只需简单地高亮
			for (var i = 0, j = sNodes.length; i < j; i++) {
				sNode = sNodes[i];
				if (sNode == span)
					sNode.addCls('selected');// highlight it!
				else
					sNode.removeCls('selected');
			}
		}

		this.onItemClick && this.onItemClick(currentValue, levelIndex);
	}

	function onGotoParentClk(e, arr, levelIndex) {
		reset.call(this, arr, levelIndex);
		this.onGotoParent && this.onGotoParent(arr, levelIndex);

		if (e && e.stopPropagation) e.stopPropagation();
		else window.event.cancelBubble = true;
		return false;
	}
})();