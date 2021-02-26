function dragSort(_ref) {
    let id = _ref.id, // 需拖动元素父级ID
        drag = _ref.drag, // 拖动的元素class（需  draggable="true" 其中  链接和图像默认是可拖动的）
        times = _ref.times; // 拖动动画时长

    if (!times)
        times = 1000;

    this.id = id;
    this.drag = drag;
    this.times = times; // 拖动动画时长
    this.init();
};

// 给实例对象添加公共属性和方法
dragSort.prototype = {
    init() {
        let node = document.getElementById(this.id),
            dragClass = this.drag,
            _this = this,
            draging = null;

        //使用事件委托，将li的事件委托给ul
        node.ondragstart = (event) => {
            draging = event.target;
        }

        node.ondragover = (event) => {
            // console.log("onDrop over");
            event.preventDefault();
            var target = event.target;

            if (!_this.hasClassName(target, dragClass))
                target = target.parentNode

            // 进行属性判断，是否覆盖到移动的标签上
            if (_this.hasClassName(target, dragClass)) {
                if (target && target.animated)
                    return;

                var targetIndex = _this.index(target), dragingIndex = _this.index(draging), nextNode = '', animateObj = [];
                if (targetIndex != dragingIndex) { // 拖拽框位置判断
                    if (targetIndex > dragingIndex) { // 向后拖拽
                        var preIndex = _this.index(draging),
                            nextIndex = _this.index(target),
                            existingnode = target.nextElementSibling;
                    } else { // 向前拖拽
                        var preIndex = _this.index(target),
                            nextIndex = _this.index(draging),
                            existingnode = target;
                    }
                    for (var i = 0; i < nextIndex - preIndex + 1; i++) {
                        nextNode = nextNode == "" ? draging : (targetIndex > dragingIndex ? nextNode.nextElementSibling : nextNode.previousElementSibling);
                        animateObj.push([nextNode.getBoundingClientRect(), nextNode])
                    }
                    target.parentNode.insertBefore(draging, existingnode);
                    for (var i = 0; i < animateObj.length; i++) {
                        _this.animate(animateObj[i][0], animateObj[i][1]);
                    }
                }
            }
        }
    },
    // 判断是否包含某个class
    hasClassName(obj, name) {
        return new RegExp(name, 'g').test(obj.className) ? true : false;
    },
    //获取元素在父元素中的index
    index(el) {
        let index = 0;
        if (!el || !el.parentNode)
            return -1;

        while (el && (el = el.previousElementSibling)) {
            index++;
        }

        return index;
    },
    // 元素移动
    animate(prevRect, target) {
        var ms = this.times;
        var _this = this;

        if (ms) {
            var currentRect = target.getBoundingClientRect();
            if (prevRect.nodeType === 1)
                prevRect = prevRect.getBoundingClientRect();

            _this.css(target, 'transition', 'none');
            _this.css(target, 'transform', 'translate3d(' +
                (prevRect.left - currentRect.left) + 'px,' +
                (prevRect.top - currentRect.top) + 'px,0)'
            );
            target.offsetWidth; // 触发重绘
            _this.css(target, 'transition', 'all ' + ms + 'ms');
            _this.css(target, 'transform', 'translate3d(0,0,0)');

            clearTimeout(target.animated);
            target.animated = setTimeout(function () {
                _this.css(target, 'transition', '');
                _this.css(target, 'transform', '');
                target.animated = false;
            }, ms);
        }
    },
    // 给元素添加style
    css(el, prop, val) {
        var style = el && el.style;

        if (style) {
            if (val === void 0) {
                if (document.defaultView && document.defaultView.getComputedStyle)
                    val = document.defaultView.getComputedStyle(el, '');
                else if (el.currentStyle)
                    val = el.currentStyle;

                return prop === void 0 ? val : val[prop];
            } else {
                if (!(prop in style))
                    prop = '-webkit-' + prop;

                style[prop] = val + (typeof val === 'string' ? '' : 'px');
            }
        }
    }
};