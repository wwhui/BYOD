package com.byod.utils;

import java.util.Map;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.byod.app.listener.MDMReceiver;

/**
 * 从服务器获取并存储策略sharedPreference中
 * @author ifay
 *
 */
@SuppressWarnings("static-access")
public class PolicyUtils {

    //认证策略
    public static String PREF_MAX_AUTHTIME = "max_authtime";   //最大尝试次数
    public static String PREF_PWD_LENGTH = "pwd_length";    //密码长度
    public static String PREF_PWD_STRENGTH = "pwd_strength";    //密码强度
    //TODO TObeContinued
    
    //设备策略
    public static String PREF_OS_VERSION = "os_version";    //系统版本
    public static String PREF_OS_ISROOTED = "is_rooted";   //是否root
    
    
    public static String PREF_POLICY_SYNC_TIME = "policy_sync_time";  //策略的最新同步时间
    
    
    public static int sAuthMaxTime = 3;
    
    private static DevicePolicyManager dpManager;
    private static ComponentName byodAdmin;
    private static SharedPreferences sPrefs;

    private static SharedPreferences initSharedPreferences(Context ctx) {
        if (sPrefs == null) {
            sPrefs = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        }
        return sPrefs;
    }
    
    /**
     * 向服务器查询用户所有设备数目
     * @param userAccount 用户账户唯一标识，此处用邮箱
     * @return 用户所绑定的设备数目
     */
    public static int getUserDeviceNum(String userAccount) {
        //TODO
        return 0;
    }
    
    /**
     * 对设备进行合规性检测
     * 1.更新最新的策略:若非最新 且 更新不成功的话，不可进行后续操作, 返回 “false”
     * 2 安全检查，不通过的话，返回错误提示（哪些安全策略未通过）。通过的话返回“true”
     * 
     * @param context
     * @return
     */
    public static String isDeviceComplianced(Context context) {
        if (getNewestPolicy() == CommonUtils.FAIL) {
            return String.valueOf(CommonUtils.FAIL);
        }
        SharedPreferences prefs = initSharedPreferences(context);
        
        //遍历没用诶，需要根据每条策略的具体含义进行检测。
        
//        Map<String,?> policyMap = sharedPref.getAll();
//        for(Map.Entry<String, ?> item : policyMap.entrySet()){
//            //TODO 
//        }
        return String.valueOf(CommonUtils.SUCCESS);
    }
    //device admin check
    public static boolean isAdminActive(Context context) {
        if (dpManager == null) {
            dpManager = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
        }
        if (byodAdmin == null) {
            byodAdmin = new ComponentName(context, MDMReceiver.class);
        }
        return dpManager.isAdminActive(byodAdmin);
    }


    public static void ActivateDeviceAdmin(Context context) {
        Intent i = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        if (byodAdmin == null) {
            byodAdmin = new ComponentName(context, MDMReceiver.class);
        }
        i.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, byodAdmin);
        context.startActivity(i);
    }
    
    /**
     * TODO
     * 本方法需要连接网络，不要直接使用
     * 从服务器请求策略是否有更新，若有，则同步下来，解析json，写到本地的sharedPreference中
     * @return 返回成功或失败
     */
    public static boolean getNewestPolicy() {
        if(localPolicyIsNewest()) {
            return true;
        }
        
        return true;
    }
    
    /**
     * TODO
     * 检测本地策略是否为最新
     * @return
     */
    public static boolean localPolicyIsNewest() {
        //每次同步策略时,将同步的服务器时间存储到sharedpreference中
        //从服务器请求最新的策略的时间，和本地存储的时间比较，若不同，则返回false
        return true;
    }

    public static int getPolicyInt(Context cxt, String key, int defValue) {
        SharedPreferences prefs = initSharedPreferences(cxt);
        return prefs.getInt(key, defValue);
    }

    public static String getPolicyString(Context cxt, String key,  String defValue) {
        SharedPreferences prefs = initSharedPreferences(cxt);
        return prefs.getString(key, defValue);
    }

    public static boolean getPolicyBool(Context cxt, String key, boolean defValue) {
        SharedPreferences prefs = initSharedPreferences(cxt);
        return prefs.getBoolean(key, defValue);
    }

    public static Long getPolicyLong(Context cxt, String key, Long defValue) {
        SharedPreferences prefs = initSharedPreferences(cxt);
        return prefs.getLong(key, defValue);
    }

    public static Long getLatestPolicyTime(Context cxt, Long defValue) {
        SharedPreferences prefs = initSharedPreferences(cxt);
        Log.d("null pointer", prefs==null?"null":"not null");
        return prefs.getLong(PREF_POLICY_SYNC_TIME, defValue);
    }
}
