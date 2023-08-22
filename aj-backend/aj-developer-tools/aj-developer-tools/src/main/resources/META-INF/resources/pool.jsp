<%@ page contentType="text/plain; charset=UTF-8" pageEncoding="ISO-8859-1"
  session="false"
  import="java.io.*, java.util.*, java.net.*,
  javax.management.*, java.lang.management.ManagementFactory
  "
%><%!

private void dumpMBean(MBeanServer server, ObjectName objName, MBeanInfo mbi, Writer writer) throws Exception {
    writer.write(String.format("MBeanClassName=%s%n", mbi.getClassName()));
    Map<String,String> props=new HashMap<String,String>();
    int idx=0;
    for(MBeanAttributeInfo mf : mbi.getAttributes()) {
        idx++;
        try {
            Object attr = server.getAttribute(objName, mf.getName());
            if (attr!=null)
                props.put(mf.getName(), attr.toString());
        } catch(Exception ex) {
            // sun.management.RuntimeImpl: java.lang.UnsupportedOperationException(Boot class path mechanism is not supported)
            props.put("error_"+idx, ex.getClass().getName()+" "+ex.getMessage());
        }
    }
    // sort by hashmap keys
    for(String sKey : new TreeSet<String>(props.keySet()))
        writer.write(String.format("%s=%s%n", sKey, props.get(sKey)));
}

%><%
// Dump MBean management properties, all beans or named beans
// dumpMBean.jsp?name=ConnectionPool,ContainerMBean
// dumpMBean.jsp?name=

if (request.getCharacterEncoding()==null)
    request.setCharacterEncoding("UTF-8");

String val = request.getParameter("name");
String[] names = val!=null ? val.trim().split(",") : new String[0];
if (names.length==1 && names[0].isEmpty()) names=new String[0];

MBeanServer server = ManagementFactory.getPlatformMBeanServer();
for(ObjectName objName : server.queryNames(null,null)) {
    MBeanInfo mbi = server.getMBeanInfo(objName);
    boolean match = names.length<1;
    String name = mbi.getClassName();
    for(int idx=0; idx<names.length; idx++) {
        if (name.endsWith(names[idx])) {
            match=true;
            break;
        }
    }
    if (match) {
        dumpMBean(server, objName, mbi, out);
        out.println("");
    }
}
out.flush();

%>