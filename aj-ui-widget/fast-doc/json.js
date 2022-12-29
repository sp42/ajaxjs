var DOC_JSON = {"com.ajaxjs.fast_doc.FooController":{"items":[{"id":"f4a794f87fc02a00f7274ab9fe76e7b3","name":"保存所有侦查点2","description":"","methodName":"saveDetectPoint","args":[{"position":"body","isRequired":true,"defaultValue":null,"example":null,"bean":null,"fields":[{"name":"name","commentText":"名称","type":"String","typeFullName":"java.lang.String"}, {"name":"numbers","commentText":"数量","type":"List","typeFullName":"java.util.List"}],"name":"bean","description":"实体一个 提交的实体","type":"FooBean"}],"returnValue":{"comment":null,"isMany":false,"isObject":true,"values":null,"beans":null,"example":null,"fields":[{"name":"name","commentText":"名称","type":"String","typeFullName":"java.lang.String"}],"name":"BarBean","description":"实体 Bean#2","type":"com.ajaxjs.fast_doc.BarBean"},"httpMethod":"PUT","url":"/close/bean","image":""}, {"id":"88f8a2bcac034e46924cf7f15d120bb6","name":"保存所有侦查点","description":null,"methodName":"saveAllDetectPoint","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":"{\"a\":20220232}","bean":null,"fields":null,"name":"taskId","description":null,"type":"String"}, {"position":"body","isRequired":true,"defaultValue":null,"example":null,"bean":null,"fields":null,"name":"detectPoints","description":null,"type":"List"}],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":null,"fields":null,"name":"Boolean","description":null,"type":"java.lang.Boolean"},"httpMethod":"POST","url":"/close/detect_point/{taskId}","image":null}, {"id":"363d0fdb2792c9fbbad18e2f3e79c610","name":"航路规划","description":"航路规划 ABC","methodName":"routePlan","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"bean":null,"fields":null,"name":"taskId","description":"任务 id","type":"String"}],"returnValue":{"comment":null,"isMany":true,"isObject":false,"values":null,"beans":null,"example":"[\"task-20220232\", \"task-20220233\"]","fields":null,"name":"List","description":null,"type":"java.util.List"},"httpMethod":"GET","url":"/close/route_plan/{taskId}","image":""}, {"id":"596cfdb4e5c8fea34892b41160280d73","name":"资源规划","description":"资源规划","methodName":"resPlan","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"bean":null,"fields":null,"name":"taskId","description":"任务 id","type":"String"}],"returnValue":{"comment":null,"isMany":true,"isObject":true,"values":null,"beans":null,"example":"[\"task-20220232\", \"task-20220233\"]","fields":[{"name":"name","commentText":"名称","type":"String","typeFullName":"java.lang.String"}, {"name":"numbers","commentText":"数量","type":"List","typeFullName":"java.util.List"}],"name":"List","description":"实体一个","type":"java.util.List"},"httpMethod":"GET","url":"/close/route_plan/{taskId}/foo","image":""}],"name":"FooController","description":"抵近侦察区域控制器","type":"com.ajaxjs.fast_doc.FooController"}};