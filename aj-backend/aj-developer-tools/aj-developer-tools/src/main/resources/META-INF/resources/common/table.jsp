    <% for(Map<String, Object> map : mapList ){%>
        <tr>
        <%for(String key: map.keySet()){%>
            <td><%=map.get(key)%></td>
        <%}%>
        </tr>
    <%}%>