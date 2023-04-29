import Vue from 'vue';
import ViewUI from 'view-design';
import ApiHelper from './api-helper/index.vue';
import 'view-design/dist/styles/iview.css';
import '@/assets/common-functions.less';

Vue.config.productionTip = false;
Vue.use(ViewUI);

new Vue({
  render: h => h(ApiHelper)
}).$mount('#app');