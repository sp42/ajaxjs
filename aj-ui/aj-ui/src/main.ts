import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ViewUI from 'view-design';
import 'view-design/dist/styles/iview.css';
import '@/style/reset.less';

// import '@/style/common-functions.less';

Vue.config.productionTip = false;
Vue.use(ViewUI);

Vue.filter('formatDate', function (value: string) {
  if (value)
    return formatDate(String(value));
});


new Vue({
  router,
  render: h => h(App)
}).$mount('#app');

function formatDate(value: string): string {
  // 创建一个日期对象
  const date = new Date(value);

  // 获取年、月、日、时、分、秒
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hour = date.getHours();
  const minute = date.getMinutes();

  // 拼接日期字符串
  let dateStr = year + '-';
  dateStr += month < 10 ? '0' + month : month;
  dateStr += '-';
  dateStr += day < 10 ? '0' + day : day;

  // 是否需要显示时间

  dateStr += ' ';
  dateStr += hour < 10 ? '0' + hour : hour;
  dateStr += ':';
  dateStr += minute < 10 ? '0' + minute : minute;

  return dateStr;
}
