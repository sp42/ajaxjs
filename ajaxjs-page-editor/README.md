可编辑页面
=================================== 




需求：提供一页面，放置“帮助”、“版权”文字内容，特点：静态页面，无须读数据库，只是应付字眼上频繁的修改；没有复杂的交互，无须 JavaScript；没有图片，不需要文件上传。
给出的方案：提供一页面和简易的后台管理，功能单一，只是编辑页面（只是修改字体、大小、粗体、斜体等的功能）。

不管静态页面（*.html）还是动态页面（*.jsp/servlet），只要插入了下面注释的语句，就是可编辑页面，可以进入后台被编辑。

    <!-- Editable AREA|START -->    
        <p>密山市贵宾楼建筑安装有限公司</p>
        <p>密山市贵宾家园物业管理有限公司</p>
        <p>密山市交换空间装饰限公司</p>
        <p>密山市贵宾楼服务有限责任公司</p>
        <p>北京江海金苑商务宾馆</p>
        <p>密山兴凯湖大酒店（五星级）</p>
    <!-- Editable AREA|END -->
    
请谨记必须保留上述 HTML 注释。

实现思路：纯 JSP 展示，管理界面用 HTTP Basic 登入，通过一个 JS 写成 HTML 编辑器修改页面内容。直接修改服务器磁盘文件。
界面如下，右图是后台编辑。

项目根目录下 index.jsp 和 disciaimer.htm 都是例子，进入 /pageEditor_admin 访问后台进行编辑

注意：PageEditorService.save_jsp_fileContent() 方法可以写入服务器磁盘文件，注意不要给黑客调用

另有纯 jsp 方案：http://pan.baidu.com/s/1bpkHNnp

参见 ：http://blog.csdn.net/zhangxin09/article/details/51545128