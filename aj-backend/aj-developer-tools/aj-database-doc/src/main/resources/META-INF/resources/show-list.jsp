<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, com.ajaxjs.developertools.mpb.MVC"%>
<!DOCTYPE html>
<html>
<%@ include file="common/head.jsp" %>
<body>
    <%@ include file="common/nav.jsp" %>
    <h2>${param.title}</h2><h3>${param.note}</h3>
<%
    List<Map<String, Object>> mapList = MVC.showList(request.getParameter("id"));
    System.out.println(">>>>>>>>"+mapList);
    if(mapList != null) {
%>
    <%-- Your JSP content goes here --%>
    <table>
        <thead>
        <tr>
        <%for (String th : MVC.getTh(request)) {%>
             <th><%=th%></th>
        <%}%>
        </tr>
        </thead>
    <% for(Map<String, Object> map : mapList ){%>
        <tr>
        <%for(String key: map.keySet()){%>
            <td><%=map.get(key)%></td>
        <%}%>
        </tr>
    <%}%>
    </table>
    <div class="total" style="text-align:center;color:gray;font-size:12px;margin-top:5px;height:50px"> 共 <%=mapList.size()%> 项</div>

    <%}%>

    <%if(request.getParameter("id").equals("mysql_innodb_mutex")){%>
    <div class="note-text">
        <p>MySQL使用两步骤的方法来获取互斥锁。线程首先尝试锁定互斥锁。如果互斥锁被其他线程锁定，请求的线程将进行自旋等待（类似于 Oracle 中的 latch，以节省上下文切换）。如果这段时间内自旋等待不成功，线程将进入睡眠状态，直到互斥锁被释放（等待条件数组/变量）。
        还可以参考 InnoDB 引擎状态中的"Semaphores"。</p>
        <p>以下几个指标用于描述这个过程：</p>
        <ul>
          <li>自旋等待次数（spin waits）：线程尝试获取互斥锁但未成功，进而在自旋等待中等待的次数。</li>
          <li>自旋循环次数（rounds）：线程在自旋等待周期内循环检查互斥锁的次数。innodb_sync_spin_loops 变量（默认值为 20）控制每次自旋等待的循环次数。</li>
          <li>操作系统等待次数（OS wait）：线程放弃自旋等待并进入睡眠状态的次数。</li>
        </ul>
        <p>这种两步骤的方法结合了自旋等待和睡眠等待，既可以减少上下文切换带来的开销，又能够避免长时间占用 CPU 资源。通过调整相关参数，可以平衡自旋等待和睡眠等待的比例，以达到最佳的性能和资源利用率。</p>
    </div>
    <%}%>
</body>
</html>
