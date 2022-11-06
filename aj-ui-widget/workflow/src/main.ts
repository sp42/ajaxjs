import Vue from 'vue';
import Workflow from './workflow/index.vue';
import ViewUI from 'view-design';
import 'view-design/dist/styles/iview.css';
import '@/assets/common-functions.less';

Vue.config.productionTip = false;
Vue.use(ViewUI);

new Vue({
  render: h => h(Workflow)
}).$mount('#app');