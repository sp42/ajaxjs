/**
* Thx to ScrollSpy
* 
* @param {Elment} 如果是在区域内滚动的话，则要传入滚动面板的元素，移动端会适用
*/
aj.scrollSpy = function(cfg) {
	var isScrollInElement = !!(cfg && cfg.scrollInElement);

	var handleScroll = function() {
		var currentViewPosition;

		if (isScrollInElement)
			currentViewPosition = cfg.scrollInElement.scrollTop + window.innerHeight;
		else
			currentViewPosition = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;

		for (var i in elements) {
			var element = elements[i],
				el = element.domElement,
				elementPosition = getPositionOfElement(el);

			var usableViewPosition = currentViewPosition;

			if (element.isInViewPort == false)
				usableViewPosition -= el.clientHeight;

			if (usableViewPosition < elementPosition) {
				this.onScrollSpyOutOfSight && this.onScrollSpyOutOfSight(el);
				element.isInViewPort = false;
			} else if (element.isInViewPort == false) {
				this.onScrollSpyBackInSight && this.onScrollSpyBackInSight(el);
				element.isInViewPort = true;
			}
		}
	}.bind(this);

	if (document.addEventListener) {
		(cfg && cfg.scrollInElement || document).addEventListener("touchmove", handleScroll, false);
		(cfg && cfg.scrollInElement || document).addEventListener("scroll", handleScroll, false);
	} else if (window.attachEvent)
		window.attachEvent("onscroll", handleScroll);

	var elements = {};

	this.spyOn = function(domElement) {
		var element = {};
		element['domElement'] = domElement;
		element['isInViewPort'] = true;
		elements[domElement.id] = element;
	}

	if (cfg && cfg.spyOn)
		this.spyOn(cfg.spyOn);

	// 获取元素y方向距离
	function getPositionOfElement(domElement) {
		var pos = 0;
		while (domElement != null) {
			pos += domElement.offsetTop;
			domElement = domElement.offsetParent;
		}

		return pos;
	}
}