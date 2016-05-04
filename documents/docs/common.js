var html = [
		' <h1>AJAJXJS 特色</h1>',
		'    <p>AJAXJS Framework 是一款轻量级的 Java Web 框架，其代码精炼、逻辑清晰和良好的文档支持。它特色如下：内置 IOC、AOP、ORM、DAO、MVC 等特性；充分做到“前后端分离“，客户端可使用 HTML/JSP 作为视图模板，服务端可发布 RESTful 服务。</p>',
		'    <p>AJAXJS Framework 基于 <a  target="_blank"  href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache License Version 2.0</a> 开源协议发布。</p>',
		'    <h1>框架选用</h1>',
		'    <ul>',
		'      <li>Java JDK：要求 JDK 1.7 或以上。请注意不能是 OpenJDK。</li>',
		'      <li>Tomcat 7 应用服务器，基于 Servlet 3.0 规范</li>',
		'      <li>Spring MVC 4 框架，可选使用 Spring IOC/AOP 服务</li>',
		'      <li>Mybatis + Sqlite/MySQL 数据层服务</li>',
		'      <li>缺省 HTML/JSP 视图层，亦可以自由搭配。我们更提供提自己的供配套前端库</li>',
		'    </ul>',
		'    <p><img  src="http://img.blog.csdn.net/20150615184008222"></p>',
		'    <ul>',
		'    </ul>',
		'    <h2>fooo</h2>',
		'    <h2>bar</h2>',
		'    <h1>工具选用</h1>',
		'    <h2>日志服务</h2>',
		'    <p> AJAXJS 本身并不强制依赖第三方日志，而是采用 JDK 自带的 logger。如果你执意要加入其他 log，建议使用&nbsp; slf4j 或和 apache common logging。</p>',
		'    <h3>dfsfsf</h3>',
		'    <img  src="http://a.hiphotos.baidu.com/exp/w=480/sign=814868bad31373f0f53f6e97940e4b8b/a08b87d6277f9e2f5703edc91d30e924b899f339.jpg"><br>',
		'    <p>相关教程请参阅<a  href="http://blog.csdn.net/zhangxin09/article/details/50611373">《使用 slf4j + Java.util.logger》</a>。代码模板如下。</p>',
		'    <pre  class="prettyprint">private static final LogHelper log = LogHelper.getLog(TestLogHelper.class);',
		'log.warning("fooo");',
		'log.info("bar");</pre>',
		'    <p>',
		'      对于 WARNING 级别信息会以磁盘文件方式保存在 /META-INF/logger/log.txt 下。</p>',
		'    <p> </p>',
		'    <h2> 基于 Map 的简易缓存服务</h2>',
		'    <p>TODO</p>',
		'    <h2>单元测试基于 JUnit 4 + Mock</h2>',
		'    <h1>ffdg</h1>',
		'    <p>相关教程请参阅<a  href="http://blog.csdn.net/zhangxin09/article/details/42418441">《 JUnit + Mockito 单元测试》</a>系列文章。代码模板如下。</p>',
		'    <pre  class="prettyprint">import static org.junit.Assert.*;',
		'import org.junit.Test;', 'import com.ajaxjs.util.LogHelper;',
		'public class TestLogHelper {', '	@Test', '	public void testLog() {',
		'		LogHelper log = new LogHelper(TestLogHelper.class);',
		'		assertNotNull(log);', '		log.warning("dsada");',
		'		log.info("dsada");', '	}', '	@org.junit.Test',
		'	public void testCatchEx() {',
		'		LogHelper log = LogHelper.getLog(TestLogHelper.class);', '		try {',
		'			throw new Throwable("h");', '		} catch (Throwable e) {',
		'			log.warning(e);', '		}', '	}', '}</pre>', '    <p><br>',
		'    </p>', '    <p> </p>' ].join("");
var MAX = 20;
var reg = /\d/;
var result = {
};
function foo(html) {
	var lastIndex = 1, parentNode;
	for (var i = 0, j = html.length; i < j; i++) {
		var l = html[i + 2], i_int = parseInt(l);
		if (html[i] == '<' && html[i + 1] == 'h' && reg.test(l)) {
			var sub = html.substr(i, MAX).replace(/<[^>]*>?/g, '').trim();
			// if(sub.indexOf('工具选用') != -1)debugger;
			// if(sub.indexOf('日志服务') != -1)debugger;
			if (i_int > lastIndex) {

			} else if (i_int == lastIndex) {
				if (parentNode && i_int == parentNode.level) {
					// 同级
					parentNode = parentNode.parentNode;
				} else {
					parentNode = result[l];
					if (!parentNode) {
						parentNode = result[l] = {
							name : 'root',
							children : [],
							level : 0
						};
					}
				}

			} else if (i_int < lastIndex) {
				while (parentNode && parentNode.parentNode) {
					if (parentNode.level == (i_int - 1))
						break;
					parentNode = parentNode.parentNode;
				}
			}
			var obj = {
				name : sub,
				children : [],
				parentNode : parentNode,
				level : i_int
			};
			parentNode.children.push(obj);
			parentNode = obj;
			lastIndex = i_int;
		}
	}
}

foo(html);
console.log(result);
function xx(obj, x){
	if(!obj.name)return;
	x++;
	for(var i = 0; i < obj.children.length; i++){
		var item = obj.children[i];
		document.write(x + new Array(item.level).join('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;') + item.name + '<br/>');
		xx(item, x);
	}	
}
xx(result['1'], 0);