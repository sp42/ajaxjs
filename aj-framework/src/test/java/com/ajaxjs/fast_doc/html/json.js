var DOC_JSON = 

[{"id":"cabbc11d-7221-449c-b43c-b9820431400b","name":"电子围栏","description":"","methodName":"getDzwl","args":[],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":"2000","name":"Integer","description":null,"type":"java.lang.Integer"},"httpMethod":"GET","url":"/dzwl","image":""}, {"id":"961d6ed8-8a34-42b3-9aa6-30560a170bfe","name":"获取视频保存目录","description":"","methodName":"getVideoSavePath","args":[],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":"C:/oba/screen_save","name":"String","description":null,"type":"java.lang.String"},"httpMethod":"GET","url":"/getVideoSavePath","image":""}, {"id":"9d17a753-2b5f-42e5-97a8-39590f6776e9","name":"获取无人机资源统计数字","description":"","methodName":"getStatusTotal","args":[],"returnValue":{"comment":"无人机资源统计数字","isMany":false,"isObject":true,"values":[{"isRequired":false,"name":"online","description":"在线","type":"int"}, {"isRequired":false,"name":"offline","description":"离线","type":"int"}],"beans":null,"example":"{\"online\": 0,\"offline\": 3}","name":"UavStatusTotal","description":null,"type":"com.toway.oba.model.ObaDTO$UavStatusTotal"},"httpMethod":"GET","url":"/status_total","image":""}, {"id":"0a380628-bfae-4258-85e3-8cc0efb1764a","name":"任务列表","description":"当指定 ?type=DO 参数时候返回探障任务列表；<br />当指定 ?type=KO 参数时候返回破障任务列表；<br />当指定 ?type=EVALUATE 参数时候返回评估任务列表；<br />当不指定任何参数时候返回所有的任务。","methodName":"list","args":[{"position":"query","isRequired":false,"defaultValue":null,"example":null,"name":"type","description":null,"type":"TaskType"}],"returnValue":{"comment":"任务","isMany":true,"isObject":true,"values":[{"isRequired":false,"name":"taskNo","description":"任务编号","type":"String"}, {"isRequired":false,"name":"taskDescription","description":"任务描述","type":"String"}, {"isRequired":false,"name":"homePoint","description":"Home 点","type":"MyPoint"}, {"isRequired":false,"name":"type","description":"任务类型：DO=探障任务；KO=破障任务；EVALUATE=评估任务","type":"TaskType"}, {"isRequired":false,"name":"status","description":"任务状态","type":"TaskStatus"}, {"isRequired":false,"name":"baseConfig","description":"基础参数","type":"BaseConfig"}, {"isRequired":false,"name":"startTime","description":"开始时间","type":"Date"}, {"isRequired":false,"name":"endTime","description":"结束时间","type":"Date"}, {"isRequired":false,"name":"id","description":"实体 Id","type":"Long"}, {"isRequired":false,"name":"creatorId","description":"创建人Id","type":"Long"}, {"isRequired":false,"name":"creatorName","description":"创建人名称","type":"String"}, {"isRequired":false,"name":"updatorId","description":"更新人Id","type":"Long"}, {"isRequired":false,"name":"updatorName","description":"更新人名称","type":"String"}, {"isRequired":false,"name":"createdOn","description":"创建时间","type":"Date"}, {"isRequired":false,"name":"updatedOn","description":"创建时间","type":"String"}, {"isRequired":false,"name":"isDeleted","description":"是否删除","type":"Boolean"}],"beans":null,"example":"[{\r\n\"baseConfig\": {\r\n\"speed\": 30,\r\n\"height\": 100,\r\n\"photoInterval\": 5\r\n},\r\n\"createdOn\": \"2022-10-24 16:29:19\",\r\n\"creatorId\": 0,\r\n\"creatorName\": null,\r\n\"endTime\": null,\r\n\"homePoint\": {\r\n\"altitude\": 41.1,\r\n\"lat\": 22.800097,\r\n\"lon\": 114.30019999999999,\r\n\"name\": null,\r\n\"seq\": null\r\n},\r\n\"id\": 2,\r\n\"isDeleted\": null,\r\n\"startTime\": null,\r\n\"status\": null,\r\n\"taskDescription\": null,\r\n\"taskNo\": \"2022081000002\",\r\n\"type\": \"DO\",\r\n\"updatedOn\": \"2022-10-24 17:40:49.0\",\r\n\"updatorId\": 0,\r\n\"updatorName\": null\r\n}]","name":"Task","description":null,"type":"java.util.List"},"httpMethod":"GET","url":"/task","image":""}, {"id":"1087f42f-2563-4359-bdbb-130dc38c45d1","name":"创建任务","description":"","methodName":"createTask","args":[{"position":"query","isRequired":true,"defaultValue":null,"example":null,"name":"type","description":null,"type":"TaskType"}],"returnValue":{"comment":"任务","isMany":false,"isObject":true,"values":[{"isRequired":false,"name":"taskNo","description":"任务编号","type":"String"}, {"isRequired":false,"name":"taskDescription","description":"任务描述","type":"String"}, {"isRequired":false,"name":"homePoint","description":"Home 点","type":"MyPoint"}, {"isRequired":false,"name":"type","description":"任务类型：DO=探障任务；KO=破障任务；EVALUATE=评估任务","type":"TaskType"}, {"isRequired":false,"name":"status","description":"任务状态","type":"TaskStatus"}, {"isRequired":false,"name":"baseConfig","description":"基础参数","type":"BaseConfig"}, {"isRequired":false,"name":"startTime","description":"开始时间","type":"Date"}, {"isRequired":false,"name":"endTime","description":"结束时间","type":"Date"}, {"isRequired":false,"name":"id","description":"实体 Id","type":"Long"}, {"isRequired":false,"name":"creatorId","description":"创建人Id","type":"Long"}, {"isRequired":false,"name":"creatorName","description":"创建人名称","type":"String"}, {"isRequired":false,"name":"updatorId","description":"更新人Id","type":"Long"}, {"isRequired":false,"name":"updatorName","description":"更新人名称","type":"String"}, {"isRequired":false,"name":"createdOn","description":"创建时间","type":"Date"}, {"isRequired":false,"name":"updatedOn","description":"创建时间","type":"String"}, {"isRequired":false,"name":"isDeleted","description":"是否删除","type":"Boolean"}],"beans":null,"example":"{\"id\":\"446b6719-370d-4a4c-b894-b1df0372180e\",\"name\":\"任务列表\",\"description\":\"\",\"methodName\":\"list\"}","name":"Task","description":null,"type":"com.toway.oba.model.Task"},"httpMethod":"POST","url":"/task","image":""}, {"id":"2e79a8be-486b-4ce7-9faa-e151169d3165","name":"无人机列表，并返回其实时状态","description":"当指定 ?type=DETECT 参数时候返回侦察机列表；<br />当指定 ?type=JOB 参数时候返回作业机列表；<br />当指定 ?taskId=xxx 参数时候返回指定任务所属的飞机列表；<br />当不指定任何参数时候返回所有的飞机。","methodName":"uavList","args":[{"position":"query","isRequired":false,"defaultValue":null,"example":null,"name":"type","description":null,"type":"UavType"}, {"position":"query","isRequired":false,"defaultValue":null,"example":null,"name":"taskId","description":null,"type":"Long"}],"returnValue":{"comment":"无人机","isMany":true,"isObject":true,"values":[{"isRequired":false,"name":"type","description":"飞机类型，DETECT=侦察；JOB=作业","type":"UavType"}, {"isRequired":false,"name":"isLoadBomb","description":"是否载弹，1=载弹；0=空载","type":"Boolean"}, {"isRequired":false,"name":"wingType","description":"ROTORY=旋翼；FIXED=固定翼","type":"String"}, {"isRequired":false,"name":"isMock","description":"是否仿真","type":"Boolean"}, {"isRequired":false,"name":"uavNo","description":"无人机编号","type":"String"}, {"isRequired":false,"name":"flightState","description":"飞行状态","type":"FlightState"}, {"isRequired":false,"name":"longitude","description":"经度","type":"Double"}, {"isRequired":false,"name":"latitude","description":"纬度","type":"Double"}, {"isRequired":false,"name":"altitude","description":"海拔","type":"Float"}, {"isRequired":false,"name":"gpsState","description":"GPS 信号","type":"String"}, {"isRequired":false,"name":"gpsTime","description":"GPS 时间","type":"Date"}, {"isRequired":false,"name":"airSpeed","description":"空速","type":"Float"}, {"isRequired":false,"name":"voltage","description":"电压","type":"Float"}, {"isRequired":false,"name":"isUploadFence","description":"是否已经上传围栏","type":"Boolean"}, {"isRequired":false,"name":"isUploadRoute","description":"是否已经上传航路","type":"Boolean"}, {"isRequired":false,"name":"isLock","description":"是否锁定，true=锁定，false=已解锁","type":"Boolean"}, {"isRequired":false,"name":"isPowerGood","description":"是否电压电量正常","type":"Boolean"}, {"isRequired":false,"name":"isGpsGood","description":"是否 GPS 信号正常","type":"Boolean"}, {"isRequired":false,"name":"mode","description":"模式","type":"String"}, {"isRequired":false,"name":"systemStatus","description":"系统状态","type":"String"}, {"isRequired":false,"name":"roll","description":"滚动角","type":"Float"}, {"isRequired":false,"name":"pitch","description":"俯仰角","type":"Float"}, {"isRequired":false,"name":"yaw","description":"偏航角","type":"Float"}, {"isRequired":false,"name":"satelliteNum","description":"卫星数量","type":"Integer"}, {"isRequired":false,"name":"satelliteMod","description":"卫星方式","type":"String"}, {"isRequired":false,"name":"satellitePrecision","description":"卫星精度","type":"Float"}, {"isRequired":false,"name":"power","description":"电池电量","type":"Integer"}, {"isRequired":false,"name":"id","description":"实体 Id","type":"Long"}, {"isRequired":false,"name":"creatorId","description":"创建人Id","type":"Long"}, {"isRequired":false,"name":"creatorName","description":"创建人名称","type":"String"}, {"isRequired":false,"name":"updatorId","description":"更新人Id","type":"Long"}, {"isRequired":false,"name":"updatorName","description":"更新人名称","type":"String"}, {"isRequired":false,"name":"createdOn","description":"创建时间","type":"Date"}, {"isRequired":false,"name":"updatedOn","description":"创建时间","type":"String"}, {"isRequired":false,"name":"isDeleted","description":"是否删除","type":"Boolean"}],"beans":null,"example":"[\r\n{\r\n\"airSpeed\": null,\r\n\"altitude\": 50.0,\r\n\"createdOn\": \"2022-10-24 14:03:11\",\r\n\"creatorId\": null,\r\n\"creatorName\": null,\r\n\"flightState\": null,\r\n\"gpsState\": null,\r\n\"gpsTime\": null,\r\n\"id\": 1,\r\n\"isDeleted\": false,\r\n\"isGpsGood\": null,\r\n\"isLoadBomb\": true,\r\n\"isLock\": null,\r\n\"isMock\": null,\r\n\"isPowerGood\": null,\r\n\"isUploadFence\": null,\r\n\"isUploadRoute\": null,\r\n\"latitude\": 22.996585845947266,\r\n\"longitude\": 114.18888854980469,\r\n\"mode\": null,\r\n\"pitch\": null,\r\n\"power\": null,\r\n\"roll\": null,\r\n\"satelliteMod\": null,\r\n\"satelliteNum\": null,\r\n\"satellitePrecision\": null,\r\n\"systemStatus\": null,\r\n\"type\": \"DETECT\",\r\n\"uavNo\": \"UAV1001\",\r\n\"updatedOn\": \"2022-10-24 14:03:11.0\",\r\n\"updatorId\": null,\r\n\"updatorName\": null,\r\n\"voltage\": null,\r\n\"wingType\": \"ROTORY\",\r\n\"yaw\": null\r\n}]","name":"Uav","description":null,"type":"java.util.List"},"httpMethod":"GET","url":"/uav_list","image":""}, {"id":"a85d3eb6-ff68-413c-a3c8-1d795b59e13f","name":"解锁单个飞机","description":"","methodName":"unlockUav","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"name":"taskId","description":null,"type":"Long"}, {"position":"path","isRequired":true,"defaultValue":null,"example":null,"name":"uavId","description":null,"type":"Long"}],"returnValue":{"comment":"无人机","isMany":false,"isObject":true,"values":[{"isRequired":false,"name":"type","description":"飞机类型，DETECT=侦察；JOB=作业","type":"UavType"}, {"isRequired":false,"name":"isLoadBomb","description":"是否载弹，1=载弹；0=空载","type":"Boolean"}, {"isRequired":false,"name":"wingType","description":"ROTORY=旋翼；FIXED=固定翼","type":"String"}, {"isRequired":false,"name":"isMock","description":"是否仿真","type":"Boolean"}, {"isRequired":false,"name":"uavNo","description":"无人机编号","type":"String"}, {"isRequired":false,"name":"flightState","description":"飞行状态","type":"FlightState"}, {"isRequired":false,"name":"longitude","description":"经度","type":"Double"}, {"isRequired":false,"name":"latitude","description":"纬度","type":"Double"}, {"isRequired":false,"name":"altitude","description":"海拔","type":"Float"}, {"isRequired":false,"name":"gpsState","description":"GPS 信号","type":"String"}, {"isRequired":false,"name":"gpsTime","description":"GPS 时间","type":"Date"}, {"isRequired":false,"name":"airSpeed","description":"空速","type":"Float"}, {"isRequired":false,"name":"voltage","description":"电压","type":"Float"}, {"isRequired":false,"name":"isUploadFence","description":"是否已经上传围栏","type":"Boolean"}, {"isRequired":false,"name":"isUploadRoute","description":"是否已经上传航路","type":"Boolean"}, {"isRequired":false,"name":"isLock","description":"是否锁定，true=锁定，false=已解锁","type":"Boolean"}, {"isRequired":false,"name":"isPowerGood","description":"是否电压电量正常","type":"Boolean"}, {"isRequired":false,"name":"isGpsGood","description":"是否 GPS 信号正常","type":"Boolean"}, {"isRequired":false,"name":"mode","description":"模式","type":"String"}, {"isRequired":false,"name":"systemStatus","description":"系统状态","type":"String"}, {"isRequired":false,"name":"roll","description":"滚动角","type":"Float"}, {"isRequired":false,"name":"pitch","description":"俯仰角","type":"Float"}, {"isRequired":false,"name":"yaw","description":"偏航角","type":"Float"}, {"isRequired":false,"name":"satelliteNum","description":"卫星数量","type":"Integer"}, {"isRequired":false,"name":"satelliteMod","description":"卫星方式","type":"String"}, {"isRequired":false,"name":"satellitePrecision","description":"卫星精度","type":"Float"}, {"isRequired":false,"name":"power","description":"电池电量","type":"Integer"}, {"isRequired":false,"name":"id","description":"实体 Id","type":"Long"}, {"isRequired":false,"name":"creatorId","description":"创建人Id","type":"Long"}, {"isRequired":false,"name":"creatorName","description":"创建人名称","type":"String"}, {"isRequired":false,"name":"updatorId","description":"更新人Id","type":"Long"}, {"isRequired":false,"name":"updatorName","description":"更新人名称","type":"String"}, {"isRequired":false,"name":"createdOn","description":"创建时间","type":"Date"}, {"isRequired":false,"name":"updatedOn","description":"创建时间","type":"String"}, {"isRequired":false,"name":"isDeleted","description":"是否删除","type":"Boolean"}],"beans":null,"example":null,"name":"Uav","description":null,"type":"com.toway.oba.model.Uav"},"httpMethod":"GET","url":"/unlock/{taskId}/{uavId}","image":""}, {"id":"c5152e20-4bbe-4322-bb67-9b173014be7d","name":"上传围栏和航路","description":"","methodName":"uploadDataToUav","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"name":"taskId","description":null,"type":"Long"}, {"position":"path","isRequired":true,"defaultValue":null,"example":null,"name":"uavId","description":null,"type":"Long"}],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":null,"name":"Boolean","description":null,"type":"java.lang.Boolean"},"httpMethod":"GET","url":"/upload_data_to_uav/{taskId}/{uavId}","image":""}, {"id":"c3aa1094-4c21-4668-a7f9-f6192ee7f549","name":"保存基础参数设置","description":"","methodName":"saveBaseConfig","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"name":"taskId","description":null,"type":"Long"}, {"position":null,"isRequired":false,"defaultValue":null,"example":null,"name":"cfg","description":null,"type":"BaseConfig"}],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":null,"name":"Boolean","description":null,"type":"java.lang.Boolean"},"httpMethod":"POST","url":"/{taskId}/base_config","image":""}, {"id":"825e9f94-0ad7-426d-8b1c-cad87dd042b5","name":"执行任务命令","description":"","methodName":"executeCmd","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"name":"taskId","description":null,"type":"Long"}, {"position":"path","isRequired":true,"defaultValue":null,"example":null,"name":"command","description":null,"type":"String"}],"returnValue":{"comment":null,"isMany":false,"isObject":false,"values":null,"beans":null,"example":null,"name":"Boolean","description":null,"type":"java.lang.Boolean"},"httpMethod":"GET","url":"/{taskId}/command/{command}","image":""}, {"id":"62737580-2d7e-4dba-8320-856c013da4bd","name":"获取历史飞行轨迹","description":"","methodName":"getLiveHistory","args":[{"position":"path","isRequired":true,"defaultValue":null,"example":null,"name":"taskId","description":null,"type":"Long"}, {"position":"query","isRequired":false,"defaultValue":null,"example":null,"name":"uavId","description":null,"type":"Long"}],"returnValue":{"comment":"飞机实时与历史轨迹","isMany":true,"isObject":true,"values":[{"isRequired":false,"name":"uav","description":"实时","type":"Uav"}, {"isRequired":false,"name":"history","description":"历史轨迹","type":"List"}],"beans":null,"example":null,"name":"UavLiveHistory","description":null,"type":"java.util.List"},"httpMethod":"GET","url":"/{taskId}/uav_history","image":""}]
