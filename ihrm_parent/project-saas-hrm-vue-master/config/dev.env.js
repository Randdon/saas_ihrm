'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

//base_url可以加在reques.js的createApi方法的第一个参数前面构成完整url
//此处采用api这一参数来代替具体的请求地址，以应对多个不同微服务请求地址时的代理配置
module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_API: '"api"'
})
