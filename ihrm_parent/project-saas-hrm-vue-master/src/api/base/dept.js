import {createAPI, createFormAPI} from '@/utils/request'

// 查询部门列表
export const list = data => createAPI('/company/department', 'get', data)
// 保存部门
export const save = data => createAPI(`/company/department`, 'post', data)
// 根据ID查询部门
export const findById = data => createAPI('/company/department/' + data.id, 'get', data)
// 更新部门，注意这里如果用${}方法传参的话必须用右撇``，不能用引号''
export const update = data => createAPI(`/company/department/${data.id}`, 'put', data)
// 删除部门
export const deleteById = data => createAPI(`/company/department/${data.id}`, 'delete', data)
// 保存或更新
export const saveOrUpdate = data => { return data.id ? update(data) : save(data) }
