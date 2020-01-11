'use strict'
// Template version: 1.3.1
// see http://vuejs-templates.github.io/webpack for documentation.

const path = require('path')
//多个不同微服务地址请求时的代理配置
module.exports = {
  dev: {
    // Paths
    assetsSubDirectory: 'static',
    assetsPublicPath: '',
    proxyTable: {
      //企业信息请求的远程服务
      '/api/company': {
        target: 'http://localhost:8188/company/',
        changeOrigin: true,
        pathRewrite: {
          '^/api/company': ''
        }
      },
      //用户信息请求的远程服务
      /**
       * 当请求的地址和此处冒号前面的配置匹配到时，如'/api/sys/user'就会匹配到，则将原请求地址增添到target后，即形成
       * 新的目的请求地址：'http://localhost:8186/sys/api/sys/user'，然而pathRewrite配置项，此处又会将路径根据正则重写，
       * 即将匹配到^/api/sys'的替换为空，所以最终路径就是:http://localhost:8186/sys/user
       */
      '/api/sys': {
        target: 'http://localhost:8187/sys/',
        changeOrigin: true,
        pathRewrite: {
          '^/api/sys': ''
        },
        '/api/employees': {
          target: 'http://localhost:8186/employees/',
          changeOrigin: true,
          pathRewrite: {
            '^/api/employees': ''
          }
        }
      }
    },

    // Various Dev Server settings
    host: 'localhost', // can be overwritten by process.env.HOST
    port: 8080, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
    autoOpenBrowser: false,
    errorOverlay: true,
    notifyOnErrors: true,
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-

    // Use Eslint Loader?
    // If true, your code will be linted during bundling and
    // linting errors and warnings will be shown in the console.
    useEslint: false,
    // If true, eslint errors and warnings will also be shown in the error overlay
    // in the browser.
    showEslintErrorsInOverlay: false,

    /**
     * Source Maps
     */

    // https://webpack.js.org/configuration/devtool/#development
    devtool: 'cheap-module-eval-source-map',

    // If you have problems debugging vue-files in devtools,
    // set this to false - it *may* help
    // https://vue-loader.vuejs.org/en/options.html#cachebusting
    cacheBusting: true,

    cssSourceMap: true
  },

  build: {
    // Template for index.html
    index: path.resolve(__dirname, '../dist/index.html'),

    // Paths
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: '',

    /**
     * Source Maps
     */

    productionSourceMap: true,
    // https://webpack.js.org/configuration/devtool/#production
    devtool: '#source-map',

    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],

    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
