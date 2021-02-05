## 智能修改窗口的位置
调整浏览器尺寸后，窗体消失

出现这问题的原因也很简单，因为窗口的top、left的值是写死的，当浏览器的宽高小于窗口top、left时，必然会被隐藏掉。解决办法就是在调整浏览器尺寸的同时，把窗口的top、left也一起修改，但是具体要修改成多少呢？

　　经过自己的考虑和他人的指点，最终的解决办法就是按比例修改，也就是按当时窗口离左/右、上/下的距离与整个窗口大小的比例进行缩放，说的可能有点迷糊，不如看下修改公式吧：

　　top = 浏览器缩放前窗口上边距 / ( 浏览器缩放前高度 - 浏览器缩放前窗口高度 ) * ( 浏览器缩放后高度 - 浏览器缩放后窗口高度 );
　　left = 浏览器缩放前窗口左边距 / ( 浏览器缩放前宽度 - 浏览器缩放前窗口宽度 ) * ( 浏览器缩放后宽度 - 浏览器缩放后窗口宽度 );


##窗口超级预览
复制一份蓝色窗口到预览区，transform:scale()
https://www.cnblogs.com/hooray/archive/2011/12/29/2305654.html