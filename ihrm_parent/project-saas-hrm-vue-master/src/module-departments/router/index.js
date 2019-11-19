/*
 * @Author: itcast 
 * @Description: xxx业务模块 
 * @Date: 2018-04-13 16:13:27 
 * @Last Modified by: hans.taozhiwei
 * @Last Modified time: 2018-09-03 11:12:47
 */

import Layout from '@/module-dashboard/pages/layout'
const _import = require('@/router/import_' + process.env.NODE_ENV)

export default [
  {
    root: true,
    path: '/departments',
    component: Layout,
    redirect: 'noredirect',
    name: 'departments',
    meta: {
      title: '组织机构管理',
      icon: 'component'
    },
    children: [
      {
        path: 'index', // 请求地址
        component: _import('departments/pages/index'), // 跳转的vue视图
        name: 'organizations-index',
        meta: {title: '组织架构', icon: 'component', noCache: true}
      }
    ]
  }
]
