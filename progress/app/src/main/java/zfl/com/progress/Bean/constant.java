package zfl.com.progress.Bean;
/*
* 常量类，存放常量
* */
public class constant {
    public static int RS = 0;//服务器返回登录结果
    //请求服务器动作
//    public static final String ACTION_LOGIN = "LOGIN";//登录动作
//    public static final String ACTION_REGISTER ="REGISTER";//注册动作
//    public static final String ACTION_PERFECT = "PERFECT";//完善信息动作
//    public static final String ACTION_MYTASK ="MYTASK";//请求个人任务
//    public static final String ACTION_ISSUETASK ="ISSUETASK";//发布个人任务
//    public static final String ACTION_OTHERTASK ="OTHERTASK";//可领任务
//    public static final String ACTION_RECEIVE ="RECEIVE";//已领任务
//    public static final String ACTION_GIVEUPTASK ="GIVEUPTASK";//放弃任务
//    public static final String ACTION_PAY ="PAY";//付款
//    public static final String ACTION_GETTASK="GETTASK";//领取任务
//    public static final String ACTION_FINISHETASK="FINISHETASK";//完成任务
//    public static final String ACTION_APPRISECOT="APPRISECOT";//评价任务
//    //个人中心页面的请求
//    public static final String ACTION_EXCEPTIONTASK="EXCEPTIONTASK";//异常任务集合
//    public static final String ACTION_APPRISETASK="APPRISETASK";//待评价任务集合
//    public static final String ACTION_FINISHEDTASK="FINISHEDTASK";//已完成任务集合
//    public static final String ACTION_GETUSERBYTASK="GETUSERBYTASK";//通过任务获取user
//    //启动界面请求
//    public static final String ACTION_PERTINFO ="PERTINFO";//启动完善信息界面
//    public static final String ACTION_EXCETASK ="EXCETASK";//启动异常任务界面
//    public static final String ACTION_APPRISE ="APPRISE";//启动待评价界面
//    public static final String ACTION_FINISHED ="FINISHED";//启动已完成任务界面
//    public static final String ACTION_PERCENTER ="PERCENTER";//启动个人中心


    public  static  boolean perfect =false;//检测是否完善信息

    //url参数
//    public static final String URL = "http://10.0.2.2:8080/";//本机地址127.0.0。1 localhost
        public static final String URL = "http://192.168.43.92:8080/";//本机地址127.0.0。1 localhost
    public static final String URL_s = "http://192.168.43.92:8080/";//共享地址
    //user地址
    public static final String URL_user = URL+"progress/getUser";
    //task地址
    public static final String URL_task = URL+"progress/getTask";



    // 请求的url
    // user
    public static final String URL_login= URL+"cLogin";// 登录
    public static final String URL_register= URL+"register";// 注册
    public static final String URL_perfectInfo= URL+"perfectInformation";// 完善
    public static final String URL_updatepassword= URL+"updateCUserPasswrod";// 修改密码

    public static final String URL_userImage= URL+"getUserImage";// 获取用户图片
    // task
    public static final String URL_myTask= URL+"getMyTask";// 我的任务
    public static final String URL_issueTask= URL+"issueTask";// 发布任务

    public static final String URL_getAllTask= URL+"getAllTask";// 获取可领任务

    public static final String URL_giveupMyTask= URL+"giveupMyTask";// 放弃我的任务
    public static final String URL_receiveTask= URL+"receiveTask";// 领取任务
    public static final String URL_havereceiveTask= URL+"haveReciveTask";// 已领取任务

    public static final String URL_finishedTask= URL+"finishTask";// 完成任务

    public static final String URL_getMyExceptionTask= URL+"getMyExceptionTask";// 我的异常任务

    public static final String URL_exceGetUser= URL+"exceGetUser";// 异常获取用户信息

    public static final String URL_finshedTasks= URL+"getFinishedTask";// 获取已完成任务

    public static final String URL_payTasks= URL+"getPayTask";// 获取待付款任务

    public static final String URL_appriseTasks= URL+"getAppriseTask";// 获取待评价任务

    public static final String URL_appriseTask= URL+"appriseTask";// 获取待评价任务

    public static final String URL_userPayImage= URL+"getUserPayImage";// 获取用户图片url

    public static final String URL_userPayInfo= URL+"getUserPayInfo";// 获取用户支付信息
    public static final String URL_userPayResult= URL+"getAlipayInfo";// 支付结果处理

}
