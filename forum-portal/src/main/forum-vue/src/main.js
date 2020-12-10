import Vue from 'vue'
import App from './App'
import router from './router'
import axios from './config/http'
import mavonEditor from 'mavon-editor'
import iView from 'iview'
import 'iview/dist/styles/iview.css'
import 'mavon-editor/dist/css/index.css'

Vue.config.productionTip = false
Vue.prototype.$http = axios
Vue.use(iView)
Vue.use(mavonEditor)
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
