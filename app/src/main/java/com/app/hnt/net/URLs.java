package com.app.hnt.net;

/**
 * URL路径处理类
 */
public class URLs{
    //测试地址
//    public static String IMGHOST = "http://hnt.fbixfbi.com";//图片地址
//    public static String HOST = "http://hnt.fbixfbi.com";//接口地址
    //正式地址
    public static String IMGHOST = "https://app.hnthnt.org";//图片地址
    public static String HOST = "https://app.hnthnt.org";//接口地址

    public static final String PROJECT_NAME = "";
    public static final String API = "";

    //headrs验证信息
    public static final String APIKEY = "773EDB6D2715FACF9C93354CAC5B1A3372872DC4D5AC085867C7490E9984D33E";
    public static final String HVERSION = "1.0";
    //上传图片
    public static final String FILEKEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCTkAMY1DO6";
    //更新位置信息
    public static final String UpdateLocation = HOST + "/user/updateUserLocation";
    //更新
    public static final String Upgrade = HOST + "/api/index/version";
    //引导页
    public static final String Guide = HOST + "/api/app-banner/index";
    //发送验证码
    public static final String Code = HOST + "/home/getVerificationCode";
    //登录
    public static final String Login = HOST + "/api/member/login";
    //一键登录
    public static final String Login1 = HOST + "/home/openIdCheck";
    //绑定手机号
    public static final String BindingPhone = HOST + "/user/bindingtel";

    //图片上传
    public static final String UpFile = HOST + "/home/fileupload";
    //WebURL
    public static final String WebURL = HOST + "/home/singleinfo";
    //搜索
    public static final String Search = HOST + "/api/v1/public/get_store_search_list";
    /**
     * ********************************首页*****************************************
     */
    public static final String Fragment1 = HOST + "/api/index";

    /**
     * ********************************设备*****************************************
     */
    //设备列表
    public static final String Fragment2 = HOST + "/api/package/index";

    /**
     * ********************************我的*****************************************
     */
    public static final String Fragment3 = HOST + "/api/member/center";
    //退出登录
    public static final String LoginOut = HOST + "/api/member/logout";
    //修改密码
    public static final String ChagePassWord = HOST + "/api/member/login-password";
    //交易密码
    public static final String TransactionPassword = HOST + "/api/member/trade-password";
    //币地址
    public static final String Address = HOST + "/api/member/set-wallet";
    //提现
    public static final String TakeCash = HOST + "/api/withdrawal/create";
    //提现列表
    public static final String MyTakeCash = HOST + "/api/withdrawal/index";
    //收益明细
    public static final String ShouYi = HOST + "/api/earning/index";
    /**
     * 拼接请求路径
     *
     * @PARAM URI
     * @RETURN
     */
    public static String getURL(String uri) {
        return HOST + PROJECT_NAME + API + uri;
    }
}
