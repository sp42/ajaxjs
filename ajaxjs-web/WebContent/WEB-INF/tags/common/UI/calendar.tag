<%@tag pageEncoding="UTF-8" description="日历控件。需要把 input 输入框放置于 body" import="java.util.Random"%>
<%@ attribute name="datetime_control" type="java.lang.Boolean" required="false" description="是否需要时间控件（滑动杆）"%> 
<!-- 日历控件 -->
<div class="CalendarHolder">
	<jsp:doBody/>
	<%--
	OUTPUT:
	<input placeholder="" name="valid" type="text" noSelectedBeforeDate="true" />
	 --%>
    <div class="icon">◲</div>
    <%
    	// 为避免冲突，设置 id 的随机数
    	Random randomGenerator = new Random();
    	int randomInt = randomGenerator.nextInt(10000);
    %>
    <div class="Calendar id_<%=randomInt%>">
    	<div style="position:relative;padding-bottom: 40px;">
	        <div class="idCalendarPre">&lt;&lt;</div>
	        <div class="idCalendarNext">&gt;&gt;</div>
	        <span class="idCalendarYear"></span>年
	        <span class="idCalendarMonth"></span>月
	        <table cellspacing="0">
	            <thead>
	                <tr>
	                    <td>日</td><td>一</td> <td>二</td><td>三</td><td>四</td><td>五</td><td>六</td>
	                </tr>
	            </thead>
	            <tbody></tbody>
	        </table>
	        
			<div class="btns">
		        <input type="button" value="上一年" class="idCalendarPreYear" />
		        <input type="button" value="今日" class="idCalendarNow" />
		        <input type="button" value="下一年" class="idCalendarNextYear" />
			</div>
    	</div>
    	<%if(datetime_control != null && datetime_control ){ %>
    	<div class="slider-holder">
    		<ul>
    			<li>
    				<h2>时：</h2>
    				<input type="text" class="hour" />
    			</li>
    			<li>
    				<h2>分：</h2>
    				<input type="text" class="minute" />
    			</li>
    			<li>
    				<h2>秒：</h2>
    				<input type="text" class="second" />
    			</li>
    		</ul>
	        <div class="slider">
	        	<button></button>
	        </div>
	        <button>提交时间</button>
    	</div>
    	<%} %>
    </div>
</div>
<!-- 
	如果插入多个日历的时候，
	可能会重复加载，但暂时没应对的方法，希望浏览器不会重复加载 
-->
<script src="${pageContext.request.contextPath}/asset/bigfoot/js/form/calendar.js"></script>
<script>
	var calendar = Object.create(bf_calendar);
	calendar.el = document.querySelector('.Calendar.id_<%=randomInt%>');
	calendar.onSelect = function(date){
	    var input = this.el.parentNode.querySelector('input[type=text]');
	    // 时间要有限制，不能选择当前日期之前的时间
	    var noSelectedBeforeDate = input.getAttribute('noSelectedBeforeDate');
	
	    // 2012-07-08
	    // firefox中解析 new date(2012/12-23) 不兼容，提示invalid date 无效的日期
		// Chrome下可以直接将其格式化成日期格式，且时分秒默认为零
	    var arr = date.split('-'), now = new Date(arr[0], arr[1] - 1, arr[2], " ", " ", " ");
	    if (noSelectedBeforeDate == "true" && now < new Date){
	    	alert('不能选择当前日期之前的时间');
	    }else {
	    	input.value = date;
	    } 
	};
	calendar.init();
</script>
<!-- // 日历控件 -->