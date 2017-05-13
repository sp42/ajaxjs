<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html><%request.setAttribute("title", "lightbox"); %>
	<%@include file="../public/common.jsp" %>
    <body>
    	<%@include file="../public/nav.jsp" %>
	 	<div class="imgList_1">
	    	<ul class="imgList"></ul>
	    	<center>
		    	<button class="loadMoreBtn">加载更多</button>
	    	</center>
	 	</div>
    	
    	<br /> <br />
    	
    	<div class="imgList_2">
	    	<ul class="imgList"></ul>
	    	<center>
		    	<button class="loadMoreBtn">加载更多</button>
	    	</center>
    	</div>
    	
    	<script src="${bigfoot}/js/libs/touch.js"></script>
    	<script src="${bigfoot}/js/widget/tab.js"></script>
    	<script src="${bigfoot}/js/widget/list.js"></script>
    	<script src="${bigfoot}/js/widget/lightbox.js"></script>
    	<script src="call_lightbox.js"></script>
    	
    	<textarea style="display:none;" class="indexTab_ranking_tpl">
			<li>
				<div class="box">
					<div class="imgHolder">
							<img data-src="{filePath}?w=150" onload="this.addCls('tran')" />
					</div>
				</div>
			</li>
		</textarea>
    	<script>
    	call_lightbox(4804, 'imgList_1');
    	call_lightbox(4805, 'imgList_2');
    	</script>
    </body>
</html>