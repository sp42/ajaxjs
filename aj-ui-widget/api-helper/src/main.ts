import Vue from 'vue'
import UiDesigner from './components/api-selector/index.vue';
import ViewUI from 'view-design';
import 'view-design/dist/styles/iview.css';

Vue.config.productionTip = false;
Vue.use(ViewUI);

new Vue({
  render: h => h(UiDesigner)
}).$mount('#app');