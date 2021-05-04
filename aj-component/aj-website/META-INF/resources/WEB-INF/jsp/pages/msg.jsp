<%@page pageEncoding="UTF-8"%>
<tags:content>
	<fieldset class="aj-fieldset" style="margin:5% auto;max-width:500px;">
		<legend>${empty title ? '提示' : title} </legend>
		
		<p>
			${msg}
			${empty redirect ? '' : '<br />五秒钟之后自动跳转。'}
		</p>
		<script>
			setTimeout(function() {
				${empty redirect ? '' : 'location.assign("'.concat(redirect).concat('");') }
			}, 5000);
		</script>
	</fieldset>
</tags:content>
