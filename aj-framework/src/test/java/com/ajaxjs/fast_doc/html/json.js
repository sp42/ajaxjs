var DOC_JSON = 
{"EvaluteController":{"items":[{"id":"4872fd0a-e83e-42a6-90dd-1c2e61984962","name":"获取探障任务当前进度","description":"","methodName":"getTaskCurrent","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"评估任务当前进度","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"EvaluteTaskCurrent","description":null,"type":"com.toway.oba.model.ObaDTO$EvaluteTaskCurrent"},"httpMethod":"GET","url":"/evalute_task/progress/{taskId}","image":""}, {"id":"01e009e1-afa0-4fd6-a367-9fa141b218d7","name":"生成探障资源规划","description":"","methodName":"makeResourcePlan","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"评估任务资源分配结果","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"EvaluteResourcePlan","description":null,"type":"com.toway.oba.model.ObaDTO$EvaluteResourcePlan"},"httpMethod":"POST","url":"/evalute_task/resource_plan/{taskId}","image":""}, {"id":"ce9d5167-dca0-4498-8fd5-bbaa32d7e506","name":"获取已生成的探障资源规划","description":"","methodName":"getResourcePlan","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"评估任务资源分配结果","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"EvaluteResourcePlan","description":null,"type":"com.toway.oba.model.ObaDTO$EvaluteResourcePlan"},"httpMethod":"GET","url":"/evalute_task/resource_plan/{taskId}","image":""}, {"id":"16112c82-6a82-46b6-9600-0b2a110d7c26","name":"生成航路规划","description":"","methodName":"routePlan","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"评估任务航路规划结果","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"EvaluteRoutePlan","description":null,"type":"com.toway.oba.model.ObaDTO$EvaluteRoutePlan"},"httpMethod":"POST","url":"/evalute_task/route_plan/{taskId}","image":""}],"name":"EvaluteController","description":"评估任务","type":"com.toway.oba.controller.EvaluteController"},"KoController":{"items":[{"id":"7ae1a16f-044a-4008-9fc2-cb3cfcb29418","name":"获取破障任务当前进度","description":"","methodName":"getTaskCurrent","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"破障任务当前进度","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"KoTaskCurrent","description":null,"type":"com.toway.oba.model.ObaDTO$KoTaskCurrent"},"httpMethod":"GET","url":"/ko_task/progress/{taskId}","image":""}, {"id":"32cc7e16-d51c-4a20-bda6-36b08f4f3eb3","name":"生成破障资源规划","description":"","methodName":"makeResourcePlan","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"破障资源分配结果","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"KoResourcePlan","description":null,"type":"com.toway.oba.model.ObaDTO$KoResourcePlan"},"httpMethod":"POST","url":"/ko_task/resource_plan/{taskId}","image":""}, {"id":"f9b41dd1-4de5-4b01-9458-5979f914aacb","name":"获取已生成的破障资源规划","description":"","methodName":"getResourcePlan","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"破障资源分配结果","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"KoResourcePlan","description":null,"type":"com.toway.oba.model.ObaDTO$KoResourcePlan"},"httpMethod":"GET","url":"/ko_task/resource_plan/{taskId}","image":""}, {"id":"4ffaf3b0-1dc8-4ef1-bbed-3f91b156c172","name":"生成破障任务航路规划","description":"","methodName":"routePlan","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"破障航路规划结果","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"KoRoutePlan","description":null,"type":"com.toway.oba.model.ObaDTO$KoRoutePlan"},"httpMethod":"POST","url":"/ko_task/route_plan/{taskId}","image":""}],"name":"KoController","description":"破障任务","type":"com.toway.oba.controller.KoController"},"TaskController":{"items":[{"id":"080a7163-0e93-4711-8d2f-b89b46036052","name":"任务列表","description":"当指定 ?type=DO 参数时候返回探障任务列表；<br />当指定 ?type=KO 参数时候返回破障任务列表；<br />当指定 ?type=EVALUATE 参数时候返回评估任务列表；<br />当不指定任何参数时候返回所有的任务。","methodName":"list","args":[{"position":"query","isRequired":false,"defaultValue":null,"example":null,"beans":null,"name":"type","description":null,"type":"TaskType"}],"returnValue":{"comment":"任务","isMany":true,"isObject":true,"values":[{"isRequired":false,"name":"taskNo","description":"任务编号","type":"String"}, {"isRequired":false,"name":"taskDescription","description":"任务描述","type":"String"}, {"isRequired":false,"name":"homePoint","description":"Home 点","type":"MyPoint"}, {"isRequired":false,"name":"detectArea","description":"侦察目标","type":"List"}, {"isRequired":false,"name":"type","description":"任务类型：DO=探障任务；KO=破障任务；EVALUATE=评估任务","type":"TaskType"}, {"isRequired":false,"name":"status","description":"任务状态","type":"TaskStatus"}, {"isRequired":false,"name":"baseConfig","description":"基础参数","type":"BaseConfig"}, {"isRequired":false,"name":"startTime","description":"开始时间","type":"Date"}, {"isRequired":false,"name":"endTime","description":"结束时间","type":"Date"}, {"isRequired":false,"name":"id","description":"实体 Id","type":"Long"}, {"isRequired":false,"name":"creatorId","description":"创建人Id","type":"Long"}, {"isRequired":false,"name":"creatorName","description":"创建人名称","type":"String"}, {"isRequired":false,"name":"updatorId","description":"更新人Id","type":"Long"}, {"isRequired":false,"name":"updatorName","description":"更新人名称","type":"String"}, {"isRequired":false,"name":"createdOn","description":"创建时间","type":"Date"}, {"isRequired":false,"name":"updatedOn","description":"创建时间","type":"String"}, {"isRequired":false,"name":"isDeleted","description":"是否删除","type":"Boolean"}],"beans":null,"example":"[{\r\n\"baseConfig\": {\r\n\"speed\": 30,\r\n\"height\": 100,\r\n\"photoInterval\": 5\r\n},\r\n\"createdOn\": \"2022-10-24 16:29:19\",\r\n\"creatorId\": 0,\r\n\"creatorName\": null,\r\n\"endTime\": null,\r\n\"homePoint\": {\r\n\"altitude\": 41.1,\r\n\"lat\": 22.800097,\r\n\"lon\": 114.30019999999999,\r\n\"name\": null,\r\n\"seq\": null\r\n},\r\n\"id\": 2,\r\n\"isDeleted\": null,\r\n\"startTime\": null,\r\n\"status\": null,\r\n\"taskDescription\": null,\r\n\"taskNo\": \"2022081000002\",\r\n\"type\": \"DO\",\r\n\"updatedOn\": \"2022-10-24 17:40:49.0\",\r\n\"updatorId\": 0,\r\n\"updatorName\": null\r\n}]","name":"Task","description":null,"type":"java.util.List"},"httpMethod":"GET","url":"/task","image":"2"}, {"id":"794803eb-8820-4691-8079-f3b3e06c1685","name":"创建任务","description":"一进入界面时候即创建。须指定任务类型的参数 ?type=DO=探障任务/KO=破障任务/EVALUATE=评估任务","methodName":"createTask","args":[{"position":"query","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"type","description":null,"type":"TaskType"}],"returnValue":{"comment":"任务","isMany":false,"isObject":true,"values":[{"isRequired":false,"name":"taskNo","description":"任务编号","type":"String"}, {"isRequired":false,"name":"taskDescription","description":"任务描述","type":"String"}, {"isRequired":false,"name":"homePoint","description":"Home 点","type":"MyPoint"}, {"isRequired":false,"name":"detectArea","description":"侦察目标","type":"List"}, {"isRequired":false,"name":"type","description":"任务类型：DO=探障任务；KO=破障任务；EVALUATE=评估任务","type":"TaskType"}, {"isRequired":false,"name":"status","description":"任务状态","type":"TaskStatus"}, {"isRequired":false,"name":"baseConfig","description":"基础参数","type":"BaseConfig"}, {"isRequired":false,"name":"startTime","description":"开始时间","type":"Date"}, {"isRequired":false,"name":"endTime","description":"结束时间","type":"Date"}, {"isRequired":false,"name":"id","description":"实体 Id","type":"Long"}, {"isRequired":false,"name":"creatorId","description":"创建人Id","type":"Long"}, {"isRequired":false,"name":"creatorName","description":"创建人名称","type":"String"}, {"isRequired":false,"name":"updatorId","description":"更新人Id","type":"Long"}, {"isRequired":false,"name":"updatorName","description":"更新人名称","type":"String"}, {"isRequired":false,"name":"createdOn","description":"创建时间","type":"Date"}, {"isRequired":false,"name":"updatedOn","description":"创建时间","type":"String"}, {"isRequired":false,"name":"isDeleted","description":"是否删除","type":"Boolean"}],"beans":null,"example":"{\r\n    \"status\": 1,\r\n    \"message\": \"创建任务\",\r\n    \"data\": {\r\n        \"baseConfig\": {\r\n            \"speed\": 30,\r\n            \"height\": 100,\r\n            \"photoInterval\": 6\r\n        },\r\n        \"createdOn\": null,\r\n        \"creatorId\": null,\r\n        \"creatorName\": null,\r\n        \"endTime\": null,\r\n        \"homePoint\": {\r\n            \"altitude\": 50.0,\r\n            \"lat\": 22.996585845947266,\r\n            \"lon\": 114.18888854980469,\r\n            \"name\": null,\r\n            \"seq\": null\r\n        },\r\n        \"id\": 9,\r\n        \"isDeleted\": null,\r\n        \"startTime\": null,\r\n        \"status\": \"INITIAL\",\r\n        \"taskDescription\": null,\r\n        \"taskNo\": \"2022102500001\",\r\n        \"type\": \"DO\",\r\n        \"updatedOn\": null,\r\n        \"updatorId\": null,\r\n        \"updatorName\": null\r\n    }\r\n}","name":"Task","description":null,"type":"com.toway.oba.model.Task"},"httpMethod":"POST","url":"/task","image":""}, {"id":"8e009887-1755-4a81-9dce-e22140c81f9f","name":"电子围栏","description":"","methodName":"getDzwl","args":[],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":"2000","name":"Integer","description":null,"type":"java.lang.Integer"},"httpMethod":"GET","url":"/task/dzwl","image":"1"}, {"id":"d426fea3-58f7-4547-88d4-864a77f20e7b","name":"获取视频保存目录","description":"","methodName":"getVideoSavePath","args":[],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":"C:/oba/screen_save","name":"String","description":null,"type":"java.lang.String"},"httpMethod":"GET","url":"/task/getVideoSavePath","image":"4"}, {"id":"c5d4eef6-4037-48da-9261-7aebe954febd","name":"获取无人机资源统计数字","description":"","methodName":"getStatusTotal","args":[],"returnValue":{"comment":"无人机资源统计数字","isMany":false,"isObject":true,"values":[{"isRequired":false,"name":"online","description":"在线","type":"int"}, {"isRequired":false,"name":"offline","description":"离线","type":"int"}],"beans":null,"example":"{\"online\": 0,\"offline\": 3}","name":"UavStatusTotal","description":null,"type":"com.toway.oba.model.ObaDTO$UavStatusTotal"},"httpMethod":"GET","url":"/task/status_total","image":"5"}, {"id":"16dc216e-cc4f-44b4-b02e-e0d3ad3cbf06","name":"无人机列表，并返回其实时状态","description":"当指定 ?type=DETECT 参数时候返回侦察机列表；<br />当指定 ?type=JOB 参数时候返回作业机列表；<br />当指定 ?taskId=xxx 参数时候返回指定任务所属的飞机列表；<br />当不指定任何参数时候返回所有的飞机。","methodName":"uavList","args":[{"position":"query","isRequired":false,"defaultValue":null,"example":null,"beans":null,"name":"type","description":null,"type":"UavType"}, {"position":"query","isRequired":false,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"无人机","isMany":true,"isObject":true,"values":[{"isRequired":false,"name":"type","description":"飞机类型，DETECT=侦察；JOB=作业","type":"UavType"}, {"isRequired":false,"name":"isLoadBomb","description":"是否载弹，true=载弹；false=空载","type":"Boolean"}, {"isRequired":false,"name":"wingType","description":"ROTORY=旋翼；FIXED=固定翼","type":"String"}, {"isRequired":false,"name":"isMock","description":"是否仿真","type":"Boolean"}, {"isRequired":false,"name":"uavNo","description":"无人机编号","type":"String"}, {"isRequired":false,"name":"flightState","description":"飞行状态","type":"FlightState"}, {"isRequired":false,"name":"longitude","description":"经度","type":"Double"}, {"isRequired":false,"name":"latitude","description":"纬度","type":"Double"}, {"isRequired":false,"name":"altitude","description":"海拔","type":"Float"}, {"isRequired":false,"name":"gpsState","description":"GPS 信号","type":"String"}, {"isRequired":false,"name":"gpsTime","description":"GPS 时间","type":"Date"}, {"isRequired":false,"name":"airSpeed","description":"空速","type":"Float"}, {"isRequired":false,"name":"voltage","description":"电压","type":"Float"}, {"isRequired":false,"name":"isUploadFence","description":"是否已经上传围栏","type":"Boolean"}, {"isRequired":false,"name":"isUploadRoute","description":"是否已经上传航路","type":"Boolean"}, {"isRequired":false,"name":"isLock","description":"是否锁定，true=锁定，false=已解锁","type":"Boolean"}, {"isRequired":false,"name":"isPowerGood","description":"是否电压电量正常","type":"Boolean"}, {"isRequired":false,"name":"isGpsGood","description":"是否 GPS 信号正常","type":"Boolean"}, {"isRequired":false,"name":"mode","description":"模式","type":"String"}, {"isRequired":false,"name":"systemStatus","description":"系统状态","type":"String"}, {"isRequired":false,"name":"roll","description":"滚动角","type":"Float"}, {"isRequired":false,"name":"pitch","description":"俯仰角","type":"Float"}, {"isRequired":false,"name":"yaw","description":"偏航角","type":"Float"}, {"isRequired":false,"name":"satelliteNum","description":"卫星数量","type":"Integer"}, {"isRequired":false,"name":"satelliteMod","description":"卫星方式","type":"String"}, {"isRequired":false,"name":"satellitePrecision","description":"卫星精度","type":"Float"}, {"isRequired":false,"name":"power","description":"电池电量","type":"Integer"}, {"isRequired":false,"name":"id","description":"实体 Id","type":"Long"}, {"isRequired":false,"name":"creatorId","description":"创建人Id","type":"Long"}, {"isRequired":false,"name":"creatorName","description":"创建人名称","type":"String"}, {"isRequired":false,"name":"updatorId","description":"更新人Id","type":"Long"}, {"isRequired":false,"name":"updatorName","description":"更新人名称","type":"String"}, {"isRequired":false,"name":"createdOn","description":"创建时间","type":"Date"}, {"isRequired":false,"name":"updatedOn","description":"创建时间","type":"String"}, {"isRequired":false,"name":"isDeleted","description":"是否删除","type":"Boolean"}],"beans":null,"example":"[\r\n{\r\n\"airSpeed\": null,\r\n\"altitude\": 50.0,\r\n\"createdOn\": \"2022-10-24 14:03:11\",\r\n\"creatorId\": null,\r\n\"creatorName\": null,\r\n\"flightState\": null,\r\n\"gpsState\": null,\r\n\"gpsTime\": null,\r\n\"id\": 1,\r\n\"isDeleted\": false,\r\n\"isGpsGood\": null,\r\n\"isLoadBomb\": true,\r\n\"isLock\": null,\r\n\"isMock\": null,\r\n\"isPowerGood\": null,\r\n\"isUploadFence\": null,\r\n\"isUploadRoute\": null,\r\n\"latitude\": 22.996585845947266,\r\n\"longitude\": 114.18888854980469,\r\n\"mode\": null,\r\n\"pitch\": null,\r\n\"power\": null,\r\n\"roll\": null,\r\n\"satelliteMod\": null,\r\n\"satelliteNum\": null,\r\n\"satellitePrecision\": null,\r\n\"systemStatus\": null,\r\n\"type\": \"DETECT\",\r\n\"uavNo\": \"UAV1001\",\r\n\"updatedOn\": \"2022-10-24 14:03:11.0\",\r\n\"updatorId\": null,\r\n\"updatorName\": null,\r\n\"voltage\": null,\r\n\"wingType\": \"ROTORY\",\r\n\"yaw\": null\r\n}]","name":"Uav","description":null,"type":"java.util.List"},"httpMethod":"GET","url":"/task/uav_list","image":"6"}, {"id":"ddee377b-7280-469f-913c-f379cd8558e9","name":"解锁单个飞机","description":"","methodName":"unlockUav","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}, {"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"uavId","description":null,"type":"Long"}],"returnValue":{"comment":"无人机","isMany":false,"isObject":true,"values":[{"isRequired":false,"name":"type","description":"飞机类型，DETECT=侦察；JOB=作业","type":"UavType"}, {"isRequired":false,"name":"isLoadBomb","description":"是否载弹，true=载弹；false=空载","type":"Boolean"}, {"isRequired":false,"name":"wingType","description":"ROTORY=旋翼；FIXED=固定翼","type":"String"}, {"isRequired":false,"name":"isMock","description":"是否仿真","type":"Boolean"}, {"isRequired":false,"name":"uavNo","description":"无人机编号","type":"String"}, {"isRequired":false,"name":"flightState","description":"飞行状态","type":"FlightState"}, {"isRequired":false,"name":"longitude","description":"经度","type":"Double"}, {"isRequired":false,"name":"latitude","description":"纬度","type":"Double"}, {"isRequired":false,"name":"altitude","description":"海拔","type":"Float"}, {"isRequired":false,"name":"gpsState","description":"GPS 信号","type":"String"}, {"isRequired":false,"name":"gpsTime","description":"GPS 时间","type":"Date"}, {"isRequired":false,"name":"airSpeed","description":"空速","type":"Float"}, {"isRequired":false,"name":"voltage","description":"电压","type":"Float"}, {"isRequired":false,"name":"isUploadFence","description":"是否已经上传围栏","type":"Boolean"}, {"isRequired":false,"name":"isUploadRoute","description":"是否已经上传航路","type":"Boolean"}, {"isRequired":false,"name":"isLock","description":"是否锁定，true=锁定，false=已解锁","type":"Boolean"}, {"isRequired":false,"name":"isPowerGood","description":"是否电压电量正常","type":"Boolean"}, {"isRequired":false,"name":"isGpsGood","description":"是否 GPS 信号正常","type":"Boolean"}, {"isRequired":false,"name":"mode","description":"模式","type":"String"}, {"isRequired":false,"name":"systemStatus","description":"系统状态","type":"String"}, {"isRequired":false,"name":"roll","description":"滚动角","type":"Float"}, {"isRequired":false,"name":"pitch","description":"俯仰角","type":"Float"}, {"isRequired":false,"name":"yaw","description":"偏航角","type":"Float"}, {"isRequired":false,"name":"satelliteNum","description":"卫星数量","type":"Integer"}, {"isRequired":false,"name":"satelliteMod","description":"卫星方式","type":"String"}, {"isRequired":false,"name":"satellitePrecision","description":"卫星精度","type":"Float"}, {"isRequired":false,"name":"power","description":"电池电量","type":"Integer"}, {"isRequired":false,"name":"id","description":"实体 Id","type":"Long"}, {"isRequired":false,"name":"creatorId","description":"创建人Id","type":"Long"}, {"isRequired":false,"name":"creatorName","description":"创建人名称","type":"String"}, {"isRequired":false,"name":"updatorId","description":"更新人Id","type":"Long"}, {"isRequired":false,"name":"updatorName","description":"更新人名称","type":"String"}, {"isRequired":false,"name":"createdOn","description":"创建时间","type":"Date"}, {"isRequired":false,"name":"updatedOn","description":"创建时间","type":"String"}, {"isRequired":false,"name":"isDeleted","description":"是否删除","type":"Boolean"}],"beans":null,"example":null,"name":"Uav","description":null,"type":"com.toway.oba.model.Uav"},"httpMethod":"GET","url":"/task/unlock/{taskId}/{uavId}","image":""}, {"id":"b3434088-ad37-423c-8581-6fc744194437","name":"上传围栏和航路","description":"在航路规划之后，进入任务摘要时候调用该接口。","methodName":"uploadDataToUav","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}, {"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"uavId","description":null,"type":"Long"}],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":null,"name":"Boolean","description":null,"type":"java.lang.Boolean"},"httpMethod":"GET","url":"/task/upload_data_to_uav/{taskId}/{uavId}","image":""}, {"id":"8f906103-ce96-4e26-9717-93c952bbd0ae","name":"获取单个任务详情","description":"","methodName":"getTaskInfo","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"id","description":null,"type":"Long"}],"returnValue":{"comment":"任务","isMany":false,"isObject":true,"values":[{"isRequired":false,"name":"taskNo","description":"任务编号","type":"String"}, {"isRequired":false,"name":"taskDescription","description":"任务描述","type":"String"}, {"isRequired":false,"name":"homePoint","description":"Home 点","type":"MyPoint"}, {"isRequired":false,"name":"detectArea","description":"侦察目标","type":"List"}, {"isRequired":false,"name":"type","description":"任务类型：DO=探障任务；KO=破障任务；EVALUATE=评估任务","type":"TaskType"}, {"isRequired":false,"name":"status","description":"任务状态","type":"TaskStatus"}, {"isRequired":false,"name":"baseConfig","description":"基础参数","type":"BaseConfig"}, {"isRequired":false,"name":"startTime","description":"开始时间","type":"Date"}, {"isRequired":false,"name":"endTime","description":"结束时间","type":"Date"}, {"isRequired":false,"name":"id","description":"实体 Id","type":"Long"}, {"isRequired":false,"name":"creatorId","description":"创建人Id","type":"Long"}, {"isRequired":false,"name":"creatorName","description":"创建人名称","type":"String"}, {"isRequired":false,"name":"updatorId","description":"更新人Id","type":"Long"}, {"isRequired":false,"name":"updatorName","description":"更新人名称","type":"String"}, {"isRequired":false,"name":"createdOn","description":"创建时间","type":"Date"}, {"isRequired":false,"name":"updatedOn","description":"创建时间","type":"String"}, {"isRequired":false,"name":"isDeleted","description":"是否删除","type":"Boolean"}],"beans":null,"example":"{\r\n    \"status\": 1,\r\n    \"message\": \"创建任务\",\r\n    \"data\": {\r\n        \"baseConfig\": {\r\n            \"speed\": 30,\r\n            \"height\": 100,\r\n            \"photoInterval\": 6\r\n        },\r\n        \"createdOn\": null,\r\n        \"creatorId\": null,\r\n        \"creatorName\": null,\r\n        \"endTime\": null,\r\n        \"homePoint\": {\r\n            \"altitude\": 50.0,\r\n            \"lat\": 22.996585845947266,\r\n            \"lon\": 114.18888854980469,\r\n            \"name\": null,\r\n            \"seq\": null\r\n        },\r\n        \"id\": 9,\r\n        \"isDeleted\": null,\r\n        \"startTime\": null,\r\n        \"status\": \"INITIAL\",\r\n        \"taskDescription\": null,\r\n        \"taskNo\": \"2022102500001\",\r\n        \"type\": \"DO\",\r\n        \"updatedOn\": null,\r\n        \"updatorId\": null,\r\n        \"updatorName\": null\r\n    }\r\n}","name":"Task","description":null,"type":"com.toway.oba.model.Task"},"httpMethod":"GET","url":"/task/{id}","image":""}, {"id":"1bf50777-1e03-4d7e-b560-9fc30f174ab0","name":"保存基础参数设置","description":"","methodName":"saveBaseConfig","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}, {"position":"body","isRequired":true,"defaultValue":null,"example":"{\"speed\":30,\"height\":100,\"photoInterval\":6}","beans":null,"name":"cfg","description":null,"type":"BaseConfig"}],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":null,"name":"Boolean","description":null,"type":"java.lang.Boolean"},"httpMethod":"POST","url":"/task/{taskId}/base_config","image":"3"}, {"id":"20d2444f-f98c-4f19-bec9-3a1b76abb52d","name":"执行任务命令","description":"","methodName":"executeCmd","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}, {"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"command","description":null,"type":"String"}],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":null,"name":"Boolean","description":null,"type":"java.lang.Boolean"},"httpMethod":"GET","url":"/task/{taskId}/command/{command}","image":""}, {"id":"31be9f94-de45-45c6-8837-fcfd34069a64","name":"获取历史飞行轨迹","description":"","methodName":"getLiveHistory","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}, {"position":"query","isRequired":false,"defaultValue":null,"example":null,"beans":null,"name":"uavId","description":null,"type":"Long"}],"returnValue":{"comment":"飞机实时与历史轨迹","isMany":true,"isObject":true,"values":[{"isRequired":false,"name":"uav","description":"实时","type":"Uav"}, {"isRequired":false,"name":"history","description":"历史轨迹","type":"List"}],"beans":null,"example":null,"name":"UavLiveHistory","description":null,"type":"java.util.List"},"httpMethod":"GET","url":"/task/{taskId}/uav_history","image":""}],"name":"TaskController","description":"通用任务","type":"com.toway.oba.controller.TaskController"},"DoController":{"items":[{"id":"a79fc511-5f1d-4af1-b64e-df309299aba0","name":"保存探障侦察区域","description":"","methodName":"saveDetectArea","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}, {"position":"body","isRequired":true,"defaultValue":null,"example":"{\r\n    \"polygon\": [\r\n        {\r\n            \"latitude\": 22.803564,\r\n            \"longitude\": 114.30004\r\n        },\r\n        {\r\n            \"latitude\": 22.801092,\r\n            \"longitude\": 114.3015\r\n   \r\n        },\r\n        {\r\n            \"latitude\": 22.799208,\r\n            \"longitude\": 114.29733\r\n        },\r\n        {\r\n            \"latitude\": 22.800032,\r\n            \"longitude\": 114.296455\r\n        }\r\n    ]\r\n}","beans":null,"name":"params","description":null,"type":"Map"}],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":null,"name":"Boolean","description":null,"type":"java.lang.Boolean"},"httpMethod":"POST","url":"/do_task/detect_area/{taskId}","image":"7"}, {"id":"fb78f76a-cb19-4ed2-ad88-a87a041767fc","name":"获取探障任务当前进度","description":"","methodName":"getTaskCurrent","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"探障任务当前进度 vv","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"DoTaskCurrent","description":null,"type":"com.toway.oba.model.ObaDTO$DoTaskCurrent"},"httpMethod":"GET","url":"/do_task/progress/{taskId}","image":""}, {"id":"0aa9af6f-c8d6-4b57-a0ea-95512b32021b","name":"生成探障资源规划","description":"","methodName":"makeResourcePlan","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"探障资源分配结果","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"DoResourcePlan","description":null,"type":"com.toway.oba.model.ObaDTO$DoResourcePlan"},"httpMethod":"POST","url":"/do_task/resource_plan/{taskId}","image":""}, {"id":"cd91d8c2-cbe5-423f-af41-eb0c963aedb6","name":"获取已生成的探障资源规划","description":"","methodName":"getResourcePlan","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"探障资源分配结果","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"DoResourcePlan","description":null,"type":"com.toway.oba.model.ObaDTO$DoResourcePlan"},"httpMethod":"GET","url":"/do_task/resource_plan/{taskId}","image":""}, {"id":"d64d04d8-9b0c-47b9-ae17-b3a593eaef27","name":"生成航路规划","description":"","methodName":"routePlan","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"探障航路规划结果","isMany":false,"isObject":true,"values":[],"beans":null,"example":null,"name":"DoRoutePlan","description":null,"type":"com.toway.oba.model.ObaDTO$DoRoutePlan"},"httpMethod":"POST","url":"/do_task/route_plan/{taskId}","image":""}, {"id":"1065d9be-3724-43b1-a840-50262b03ce0b","name":"生成子区域列表","description":"","methodName":"makeSubArea","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":null,"name":"Map","description":null,"type":"java.util.Map"},"httpMethod":"POST","url":"/do_task/sub_area/{taskId}","image":""}, {"id":"3bc16dba-3ba3-4c13-9204-63f9908a9729","name":"获取子区域切片和飞机","description":"","methodName":"getSubAreaUav","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"beans":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"资源规划结果","isMany":true,"isObject":true,"values":[{"isRequired":false,"name":"taskId","description":"任务 id","type":"Long"}, {"isRequired":false,"name":"uavId","description":"飞机 id","type":"Long"}, {"isRequired":false,"name":"subArea","description":"","type":"SubArea"}, {"isRequired":false,"name":"id","description":"实体 Id","type":"Long"}, {"isRequired":false,"name":"creatorId","description":"创建人Id","type":"Long"}, {"isRequired":false,"name":"creatorName","description":"创建人名称","type":"String"}, {"isRequired":false,"name":"updatorId","description":"更新人Id","type":"Long"}, {"isRequired":false,"name":"updatorName","description":"更新人名称","type":"String"}, {"isRequired":false,"name":"createdOn","description":"创建时间","type":"Date"}, {"isRequired":false,"name":"updatedOn","description":"创建时间","type":"String"}, {"isRequired":false,"name":"isDeleted","description":"是否删除","type":"Boolean"}],"beans":null,"example":null,"name":"TaskExecute","description":null,"type":"java.util.List"},"httpMethod":"GET","url":"/do_task/{taskId}/subarea_uav","image":""}],"name":"DoController","description":"探障任务","type":"com.toway.oba.controller.DoController"}}
