package zfl.com.progress.Bean;
/*
* 常量类，存放常量
* */
public class constant {
    public static int RS = 0;//服务器返回登录结果
    //请求服务器动作
    public static final String ACTION_LOGIN = "LOGIN";//登录动作
    public static final String ACTION_REGISTER ="REGISTER";//注册动作
    public static final String ACTION_PERFECT = "PERFECT";//完善信息动作
    public static final String ACTION_MYTASK ="MYTASK";//请求个人任务
    public static final String ACTION_ISSUETASK ="ISSUETASK";//发布个人任务
    public static final String ACTION_OTHERTASK ="OTHERTASK";//可领任务
    public static final String ACTION_RECEIVE ="RECEIVE";//已领任务
    public static final String ACTION_GIVEUPTASK ="GIVEUPTASK";//放弃任务
    public static final String ACTION_PAY ="PAY";//付款
    public static final String ACTION_GETTASK="GETTASK";//领取任务
    public static final String ACTION_FINISHETASK="FINISHETASK";//完成任务
    public static final String ACTION_APPRISECOT="APPRISECOT";//评价任务
    //个人中心页面的请求
    public static final String ACTION_EXCEPTIONTASK="EXCEPTIONTASK";//异常任务集合
    public static final String ACTION_APPRISETASK="APPRISETASK";//待评价任务集合
    public static final String ACTION_FINISHEDTASK="FINISHEDTASK";//已完成任务集合
    public static final String ACTION_GETUSERBYTASK="GETUSERBYTASK";//通过任务获取user
    //启动界面请求
    public static final String ACTION_PERTINFO ="PERTINFO";//启动完善信息界面
    public static final String ACTION_EXCETASK ="EXCETASK";//启动异常任务界面
    public static final String ACTION_APPRISE ="APPRISE";//启动待评价界面
    public static final String ACTION_FINISHED ="FINISHED";//启动已完成任务界面
    public static final String ACTION_PERCENTER ="PERCENTER";//启动个人中心
    //url参数
    public static final String URL = "http://10.0.2.2:8080/";//本机地址127.0.0。1 localhost
    public static final String URL_s = "http://192.168.43.92:8080/";//共享地址
    //user地址
    public static final String URL_user = URL+"progress/getUser";
    //task地址
    public static final String URL_task = URL+"progress/getTask";

}
