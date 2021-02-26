
/*
在我们写移动端的时候，
有时要根据相应的动作，
写出相应的效果。
这里写的是一个判断向上向下滑动，
是否滑动到底的效果。
*/
function pull_more(obj) {
    var startX, startY;
    document.addEventListener('touchstart', function (ev) {
        startX = ev.touches[0].pageX;
        startY = ev.touches[0].pageY;
    }, false);

    document.addEventListener('touchend', function (ev) {
        var endX, endY;
        endX = ev.changedTouches[0].pageX;
        endY = ev.changedTouches[0].pageY;
        // var direction = GetSlideDirection(startX, startY, endX, endY);
        var dy = startY - endY;
        if (dy > 0) {  //向上滑动
            console.log("向上滑动")
        } else if (dy < 0) {  //向下滑动
            console.log("向下滑动")
        } else {
            // alert("呵呵呵");
        }
    }, false);
    $(window).on('scroll', function () {
        if ($(window).scrollTop() >= $(document).height() - $(window).height()) {
            console.log("已滑动到底")
        }
    })
}

// 移动端双指缩放图片JS事件的实践心得
// https://www.zhangxinxu.com/wordpress/2020/06/mobile-event-touches-zoom-sacle/