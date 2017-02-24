#AOP

关于异常
java.lang.ClassCastException: com.sun.proxy.$Proxy8 cannot be cast to com.ajaxjs.framework.service.IService

原先：
public class NewsService extends BaseCrudService<News, NewsDAO>
改为增加接口在最终类上（虽然已经继承类了，ide 页也可以编译）而不能在父类上
public class NewsService extends BaseCrudService<News, NewsDAO> implements IService<News>

http://blog.csdn.net/wwy543565357/article/details/45645965
http://stackoverflow.com/questions/3344299/how-do-i-get-the-underlying-type-of-a-proxy-object-in-java