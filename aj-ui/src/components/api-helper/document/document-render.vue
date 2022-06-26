<template>
  <div class="document-rendered">
    <h3>{{documnetObj.name}}</h3>
    <p>{{documnetObj.description}}</p>

    <div>
      <h3 style="margin-top:0;">对应 UI：</h3>
      <img class="ui small" @click="enlagrn($event)" src="https://tcs-devops.aliyuncs.com/storage/112h785d5d59567281da7b2b5f67ee7ec6b5?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY1NjI0MTAzNywiaWF0IjoxNjU1NjM2MjM3LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMmg3ODVkNWQ1OTU2NzI4MWRhN2IyYjVmNjdlZTdlYzZiNSJ9.zKVBc_WpZmoNV2XdzQOpKRSe4Etm5w2o2HH2afZrHOg" />
    </div>

    <p style="padding-top:3px">请求 <span class=" code-font">{{documnetObj.httpMethod}}</span> <span class="code code-font">{{documnetObj.url}}</span></p>

    <h3>请求参数：</h3>
    <table class="request-arg aj-table">
      <thead>
        <th>参数说明</th>
        <th>变量</th>
        <th>类型</th>
        <th>参数位置</th>
        <th>必填</th>
        <th>描述</th>
      </thead>
      <tbody>
        <tr v-for="arg in args" :key="arg.name">
          <td>{{arg.desc}}</td>
          <td>{{arg.name}}</td>
          <td>{{arg.dataType}}</td>
          <td>{{arg.type}}</td>
          <td>{{arg.isRequired?'Yes': 'No'}}</td>
          <td>{{arg.comment}}

            <ul v-if="arg.values && arg.values.length">
              <li v-for="enu in arg.values" :key="enu.value">
                {{enu.value}} : {{enu.comment}}
              </li>
            </ul>
          </td>
        </tr>
      </tbody>
    </table>

    <h3>请求示例：</h3>
    <div class="note">
      <a :href="documnetObj.demoUrl" target="_blank">{{documnetObj.demoUrl}}</a>
    </div>
    <h3>返回结果：</h3>

    <pre class="note">
{
	"status": int,       // 调用接口是否成功，0：失败，1：成功
	"errorCode": string, // 错误码，当 status=0 时，必须返回错误码
	"message": string,   // 错误具体信息
	"data": {            // 飞机状态信息
		"airSpeed": 52.0,
		"altitude": 55.5,
		"ammeterState": "OK",
		"compassState": "OK",
		"flightState": "UNLOCKED", 
		"gpsState": "OK",
		"gpsTime": "2022-06-07 14:36:14",
		"groundSpeed": 5.0,
		"heading": 55.0,
		"ip": "192.168.0.22",
		"latitude": 39.9352,
		"longitude": 116.354,
		"netSignal": 98.0,
		"netState": "OK",
		"power": 8.0,
		"satelliteMod": "98",
		"satelliteNum": 98,
		"satellitePrecision": 98.0,
		"uavId": "10001",
		"voltage": 5.0
	}
}</pre>
  </div>
</template>

<script lang="ts">
export default {
  data() {
    return {
      documnetObj: {
        name: "解锁任务中所有的飞机",
        description: "把 空闲、解锁失败的进行解锁",
        httpMethod: "GET",
        url: "/uav/{uav_id}/check_status",
        demoUrl: "http://test.ajaxjs.com:8081/drone/uav/10001/check_status",
      } as API_HELPER_DOCUMENT,
      args: [
        {
          desc: "任务 id",
          name: "task_id",
          type: "FORM",
          dataType: "string",
          comment: "执行任务 execute | 立即返航 return | 结束任务 close 任选一",
          isRequired: true,
          values: [
            { value: "execute", comment: "执行任务" },
            { value: "return", comment: "立即返航" },
            { value: "close", comment: "结束任务" },
          ],
        },
      ] as API_HELPER_ARGUMENT[],
    };
  },
  methods: {
    enlagrn(e: Event): void {
      let img: HTMLElement = <HTMLElement>e.target;
      img.classList.toggle("small");
    },
  },
};
</script>

<style lang="less" scoped>
.document-rendered {
  h3 {
    font-weight: bold;
    font-size: 1.2rem;
    margin: 8px 0;
  }

  img.ui {
    cursor: pointer;
    max-width: 360px;
    &.small {
      max-width: 260px;
    }
  }
}

.code {
  padding: 3px 4px;
  //   border: 1px solid rgb(250, 167, 59);
  background-color: rgb(253, 222, 222);
  color: rgb(255, 35, 35);
}

.code-font {
  font-family: "Courier New";
}

.note {
  padding-left: 10px;
  border-left: 4px solid lightgray;
}

.request-arg {
  width: 98%;
  td {
    text-align: center;
  }
}

// 快速制作1px 表格边框，为需要设置的 table 元素加上 border 的class即可
.aj-table {
  border: 1px lightgray solid;
  border-collapse: collapse;
  border-spacing: 0;

  th {
    // background-color: #efefef;
    letter-spacing: 3px;
  }

  td,
  th {
    border: 1px lightgray solid;
    line-height: 160%;
    height: 120%;
    padding: 6px;
  }

  tr {
    // .transition (background-color 400ms ease-out);

    &:nth-child(odd) {
      //   background: #f5f5f5;
      box-shadow: 0 1px 0 rgba(255, 255, 255, 0.8) inset;
    }

    &:hover {
      background-color: #fbf8e9;
    }
  }
}
</style>