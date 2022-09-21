import axios from "axios"
import { message } from "antd";

let instance = axios.create();
// 文档中的统一设置post请求头。下面会说到post请求的几种'Content-Type'
instance.defaults.headers.post['Content-Type'] = 'application/json'
/** 添加响应拦截器  **/
instance.interceptors.response.use(response => {
    return response;
}, error => {
    if (error.response) {
        // token或者登陆失效情况下跳转到登录页面，根据实际情况，在这里可以根据不同的响应错误结果，做对应的事。这里我以401判断为例
        if (error.response.status===401){
            message.error("权限不足");
        }
        if (error.response.status === 403) {
            //针对框架跳转到登陆页面
            message.error("会话失效")
            setTimeout(() => {
                window.location.href = "/logout/cas"
            }, 4000);
        }
        return Promise.reject(error)
    } else {
        return Promise.reject('请求超时, 请刷新重试')
    }
})

export default instance



