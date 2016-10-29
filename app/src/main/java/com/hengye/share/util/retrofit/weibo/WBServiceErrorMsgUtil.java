package com.hengye.share.util.retrofit.weibo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivityHelper;
import com.hengye.share.module.sso.ThirdPartyLoginActivity;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;

import java.util.HashMap;
import java.util.Map;

public class WBServiceErrorMsgUtil {

    private static final Map<String, String> errorMap = new HashMap<>();

    /**
     * 根据服务器返回的错误代码，返回错误信息
     *
     * @param errorMsg
     * @return
     */
    private static final String[][] errorMsgs = new String[][]{
            {"10002", "服务暂停"},
            {"10009", "任务过多，系统繁忙"},
            {"10010", "任务超时"},
            {"10014", "权限受限"},
            {"10017", "分组名称长度超过限制"},
            {"10022", "IP请求频次超过上限"},
            {"10023", "用户请求频次超过上限,请明天再试"},
            {"10024", "用户请求特殊接口频次超过上限"},
            {"10025", "备注长度不正确，应为0～30个字符"},
            {"20003", "用户不存在"},
            {"20006", "图片太大"},
            {"20008", "内容不能为空"},
            {"20012", "字数超过140限制"},
            {"20015", "账号、APP或者IP异常，请稍后再试。(sina有毛病)"},
            {"20016", "发布内容过于频繁"},
            {"20017", "提交相似的信息"},
            {"20018", "包含非法网址"},
            {"20019", "不能发布相同内容"},
            {"20020", "包含广告信息"},
            {"20021", "包含非法内容"},
            {"20031", "需要验证码,操作太频繁"},
            {"20101", "该微博已经删除"},
            {"20104", "不合法的微博"},
            {"20202", "不合法的评论"},
            {"20203", "该评论已被删除"},
            {"20206", "仅Ta的好友能回复Ta"},
            {"20207", "Ta设置了不允许你评论他的微博"},
            {"20506", "已经关注了"},
            {"20508", "根据对方的设置，你不能进行此操作"},
            {"20512", "你已经把此用户加入黑名单，加关注前请先解除"},
            {"20513", "你的关注人数已达上限"},
            {"20521", "hi 超人，你今天已经取消关注很多喽，接下来的时间想想如何让大家都来关注你吧！如有问题，请联系新浪客服：400 690 0000"},
            {"20522", "还没有关注该用户"},
            {"20603", "分组不存在"},
            {"20608", "分组名不能重复"},
            {"20704", "该微博已经收藏过了"},
            {"20705", "还没有收藏该微博"},
            {"21301", "授权过期，请重新授权"},
            {"21317", "授权被取消，请重新授权"},
            {"21321", "应用请求超过API限制了"},
            {"21324", "安全方面考虑，开发人员重置了应用秘钥，当前应用被视为盗版，请重新从应用市场下载!"},
            {"21327", "授权过期，请重新授权"},
            {"21332", "授权过期，请重新授权"},
            {"21335", "需要高级授权"},
            {"21602", "含有敏感词"},
            {"21923", "没有找到相关位置信息"},
            {"23201", "服务器请求超时"}
    };

    static {
        for (String[] errorArr : errorMsgs) {
            errorMap.put(errorArr[0], errorArr[1]);
        }
        errorMap.put("invalid_access_token", "无效授权，请稍后尝试重新授权");
    }

    public static String getErrorMsg(String code) {
        if (errorMap.containsKey(code))
            return errorMap.get(code);

        return null;
    }

}
