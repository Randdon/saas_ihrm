'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

//base_url可以加在reques.js的createApi方法的第一个参数前面构成完整url
module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_API: '"api"'
})
