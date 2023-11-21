// const proxy = {
//     "target": "http://10.201.76.185",
//     "context": [
//         "/api",
//         "/sipadmin/api",
//         "/sso",
//         "/themes",
//         "/ssoclient"
//     ],
//     "secure": false,
//     "changeOrigin": true
// }

const proxy = {};

const defaultConfig = {
  "target": 'http://api.backend.com', // 后端服务器
  "changeOrigin": true,               // 让target参数是域名
  "secure": false,                    // https忽略证书
  "xfwd":false                        // 不生成x-forword-xxx，部分nginx服务器会根据这个进行配置
};

const serverResList = [
  '^/(?![^/]*$|modules/|images/|static/|mock/)'
];

serverResList.forEach(function (item) {
  proxy[item] = Object.assign(defaultConfig);
});

module.exports = proxy;
