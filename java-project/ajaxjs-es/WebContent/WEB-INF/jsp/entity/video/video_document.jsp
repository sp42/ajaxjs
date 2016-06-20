<%@page pageEncoding="UTF-8"%>
<h3>获取实时源</h3>
<p>比较完善一点的视频网站都有防盗链机制，对此我们可以播放之前才获取源（实时源）。</p>
<h4>传入对方 id 和 site 获取</h4>
<p>请求实例：<a href="../${tableName}/liveSourceById/2941132?site=17">http://{serverUrl}/service/${tableName}/liveSourceById/2941132?site=17</a>，最后一位 int 是对方的 id。参数说明如下。</p>
    <table class="aj-borderTable">
	    <tr>
	    	<th>参数名</th><th>说明</th><th>参数类型</th><th>是否必填</th>
	    </tr>
	    <tr>
	    	<td>site</td><td>门户 id（网站标识）</td><td>int</td><td>true</td>
	    </tr>
	    <tr>
	    	<td>misc</td><td>对方网站上的 id</td><td>int</td><td>ture</td>
	    </tr>
    </table>
    <p>JSON 响应内容如下：</p>
    <pre class="prettyprint">{
	result: "http://cn-zjwz2-cu.acgvideo.com/vg1/5/19/4603209.mp4?expires=1460379000&ssig=tZoKKiD2scHqBCs5UYBTHA&oi=1885250535&internal=1&rate=0"
}</pre>

<br />
<h4>传入入库实体之 id 和 site 获取</h4>
<p>与上例不同，传入的 id 是入库实体之 id。通过获取 misc 字段得出对方网站上的 id。</p>
<p>请求实例：<a href="../${tableName}/678705/liveSource?site=17">http://{serverUrl}/service/${tableName}/678705/liveSource?site=17</a>，其中 int 是实体的 id。参数说明如下。</p>
    <table class="aj-borderTable">
	    <tr>
	    	<th>参数名</th><th>说明</th><th>参数类型</th><th>是否必填</th>
	    </tr>
	    <tr>
	    	<td>id</td><td>实体 id</td><td>int</td><td>ture</td>
	    </tr>
	    <tr>
	    	<td>site</td><td>网站标识</td><td>int</td><td>true</td>
	    </tr>
    </table>
    <p>JSON 响应内容如上例一致。</p>
