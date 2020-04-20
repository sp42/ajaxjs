<%@tag pageEncoding="UTF-8" description="测试"%>

<%@attribute name="frag1" fragment="true"%>
<%@attribute name="frag2" fragment="true"%>

<table border="1">
    <tr>
        <td><b>frag1</b></td>
        <td> <jsp:invoke fragment="frag1" /> </td>
    </tr>
    <tr>
        <td><b>frag2</b></td>
        <td> <jsp:invoke fragment="frag2" /> </td>
    </tr>
</table>