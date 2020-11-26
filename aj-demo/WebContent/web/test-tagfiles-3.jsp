<%@page pageEncoding="UTF-8" %>
<%@taglib prefix="mytag" tagdir="/WEB-INF/tags/"%>
<html>
<body>
    <mytag:table>
        <jsp:attribute name="frag1">
            显示了 Fragment 1 
            
        </jsp:attribute>
        <jsp:attribute name="frag2">
            显示了 Fragment 2
        </jsp:attribute>
    </mytag:table>
    <% %>
</body>
</html> 