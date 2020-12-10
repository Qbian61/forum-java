import Vue from 'vue'
import Router from 'vue-router'
import ArticleEditor from '@/components/ArticleEditor'
import FaqEditor from '@/components/FaqEditor'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/article/editor',
      component: ArticleEditor
    }, {
      path: '/article/editor/:id',
      component: ArticleEditor
    }, {
      path: '/faq/editor',
      component: FaqEditor
    }, {
      path: '/faq/editor/:id',
      component: FaqEditor
    }
  ]
})
