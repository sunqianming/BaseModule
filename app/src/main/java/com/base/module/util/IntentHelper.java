package com.base.module.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;

import com.base.module.base.BaseApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;


/***
 *
 * @ClassName: IntentHelper
 * @Description: 页面跳转工具类
 * @author SQM
 * @date 2014-10-15 下午2:33:51
 *
 */
public class IntentHelper {


    public static String getCurrentLauncherPackageName(Context context) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo res = context.getPackageManager()
                .resolveActivity(intent, 0);
        if (res == null || res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return "";
        }
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }
    }

    public static String getAuthorityFromPermissionDefault(Context context) {

        return getThirdAuthorityFromPermission(context,
                "com.android.launcher.permission.READ_SETTINGS");
    }

    public static String getThirdAuthorityFromPermission(Context context,
                                                         String permission) {
        if (TextUtils.isEmpty(permission)) {
            return "";
        }

        try {
            List<PackageInfo> packs = context.getPackageManager()
                    .getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs == null) {
                return "";
            }
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission)
                                || permission.equals(provider.writePermission)) {
                            String authority = provider.authority;
                            if (!TextUtils.isEmpty(authority)
                                    && (authority
                                    .contains(".launcher.settings")
                                    || authority
                                    .contains(".twlauncher.settings") || authority
                                    .contains(".launcher2.settings")))
                                return authority;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAuthorityFromPermission(Context context) {
        // 获取默认
        String authority = getAuthorityFromPermissionDefault(context);
        // 获取特殊第三方
        if (authority == null || authority.trim().equals("")) {
            String packageName = getCurrentLauncherPackageName(context);
            packageName += ".permission.READ_SETTINGS";
            authority = getThirdAuthorityFromPermission(context, packageName);
        }
        // 还是获取不到，直接写死
        if (TextUtils.isEmpty(authority)) {
            int sdkInt = android.os.Build.VERSION.SDK_INT;
            if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                authority = "com.android.launcher.settings";
            } else if (sdkInt < 19) {// Android 4.4以下
                authority = "com.android.launcher2.settings";
            } else {// 4.4以及以上
                authority = "com.android.launcher3.settings";
            }
        }
        authority = "content://" + authority + "/favorites?notify=true";
        return authority;

    }

    private static String AUTHORITY = null;

    public static boolean isShortCutExist(Context context, String title) {

        boolean isInstallShortcut = false;

        if (null == context || TextUtils.isEmpty(title))
            return isInstallShortcut;

        if (TextUtils.isEmpty(AUTHORITY))
            AUTHORITY = getAuthorityFromPermission(context);

        final ContentResolver cr = context.getContentResolver();

        if (!TextUtils.isEmpty(AUTHORITY)) {
            try {
                final Uri CONTENT_URI = Uri.parse(AUTHORITY);

                Cursor c = cr.query(CONTENT_URI, new String[]{"title",
                                "iconResource"}, "title=?", new String[]{title},
                        null);

                // XXX表示应用名称。
                if (c != null && c.getCount() > 0) {
                    isInstallShortcut = true;
                }
                if (null != c && !c.isClosed())
                    c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return isInstallShortcut;

    }


    /**
     * @param @param context
     * @param @param title
     * @param @param activity 参数
     * @return void 返回类型
     * @Title: deleteShutCut
     * @Description: 删除快捷方式(需增加相关权限)
     * @author SQM
     * @date 2014-10-15 下午2:34:22
     */
    public static void deleteShutCut(Context context, String title, Class<?> activity) {
        try {
            Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
            // 快捷方式的名称
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
            String appClass = activity.getName();
            ComponentName comp = new ComponentName(context.getPackageName(), appClass);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent().setComponent(comp));
            context.sendBroadcast(shortcut);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * @param @param context
     * @param @param title
     * @param @param shutcutIcon
     * @param @param intentClass 参数
     * @return void 返回类型
     * @Title: addShutCut
     * @Description: 创建快捷方式(需增加相关权限)
     * @author SQM
     * @date 2014-10-15 下午2:34:34
     */
    public static void addShutCut(Context context, String title, int shutcutIcon, Class<?> intentClass, Map<String, String> map) {
        // 创建添加快捷方式的Intent
        Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 加载快捷方式的图标
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context, shutcutIcon);
        // 创建点击快捷方式后操作Intent,该处当点击创建的快捷方式后，再次启动该程序
        Intent myIntent = new Intent(context, intentClass);
        // 设置快捷方式的标题
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 不允许重复创建
        addIntent.putExtra("duplicate", false);
        if (map != null) {
            Set<String> keys = map.keySet();
            for (String k : keys) {
                myIntent.putExtra(k, map.get(k));
            }
        }
//		myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);/
        // 设置快捷方式的图标
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 设置快捷方式对应的Intent
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
        // 发送广播添加快捷方式
        context.sendBroadcast(addIntent);
    }

    /**
     * @param @param context
     * @param @param clazz 参数
     * @return void 返回类型
     * @Title: startIntent2Activity
     * @Description: 启动Acitivity
     * @author SQM
     * @date 2014-10-15 下午2:35:38
     */
    public static Intent startIntent2Activity(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
        return intent;
    }

    /**
     * @param @param context
     * @param @param action 参数
     * @return void 返回类型
     * @Title: startIntent2Activity
     * @Description: 启动Activity
     * @author SQM
     * @date 2014-10-15 下午2:35:49
     */
    public static Intent startIntent2Activity(Context context, String action) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(action);
        context.startActivity(intent);
        return intent;
    }

    /**
     * @param @param  context
     * @param @param  clazz
     * @param @param  bundle
     * @param @return 参数
     * @return Intent 返回类型
     * @Title: startIntent2Activity
     * @Description: 启动Acitivity
     * @author SQM
     * @date 2014-10-15 下午2:37:03
     */
    public static Intent startIntent2Activity(Context context, Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        context.startActivity(intent);
        return intent;
    }

    /**
     * @param @param  context
     * @param @param  action
     * @param @param  bundle
     * @param @return 参数
     * @return Intent 返回类型
     * @Title: startIntent2Activity
     * @Description: 启动Acitivity
     * @author SQM
     * @date 2014-10-15 下午2:37:21
     */
    public static Intent startIntent2Activity(Context context, String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(action);
        intent.putExtras(bundle);
        context.startActivity(intent);
        return intent;
    }

    /***
     *
     * @Title: backIntent2Activity
     * @Description: 在activity栈中返回到activity
     * @param @param context
     * @param @param clazz
     * @param @param bundle
     * @param @return 参数
     * @return Intent 返回类型
     * @throws
     * @author SQM
     * @date 2014-10-15 下午2:39:54
     */
    public static Intent backIntent2Activity(Context context, Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        return intent;
    }

    /**
     * @param @param activity
     * @param @param clazz
     * @param @param bundle
     * @param @param requestCode 参数
     * @return void 返回类型
     * @Title: startIntentForReuslt
     * @Description: 启动activity，并有返回结果
     * @author SQM
     * @date 2014-10-15 下午2:49:43
     */
    public static void startIntentForReuslt(Activity activity, Class<?> clazz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /***
     *
     * @Title: startIntentForReuslt
     * @Description: 启动activity，并有返回结果
     * @param @param activity
     * @param @param action
     * @param @param bundle
     * @param @param requestCode 参数
     * @return void 返回类型
     * @author SQM
     * @date 2014-10-15 下午2:50:36
     */
    public static void startIntentForReuslt(Activity activity, String action, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /***
     *
     * @Title: backIntent2Activity
     * @Description: 在activity栈中返回到activity
     * @param @param context
     * @param @param action
     * @param @param bundle
     * @param @return 参数
     * @return Intent 返回类型
     * @author SQM
     * @date 2014-10-15 下午2:42:22
     */
    public static Intent backIntent2Activity(Context context, String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        return intent;
    }

    /**
     * 启动服务
     *
     * @param context
     * @param clazz
     * @return
     */
    public static Intent startService(ContextWrapper context, Class<?> clazz, Bundle bundle) {
        Intent service = new Intent(context, clazz);
        if (bundle == null) {
            service.putExtras(bundle);
        }
        context.startService(service);
        return service;
    }

    /**
     * 停止服务
     *
     * @param context
     * @param clazz
     */
    public static void stopService(Context context, Class<?> clazz) {
        Intent service = new Intent(context, clazz);
        context.stopService(service);
    }

    /***
     *
     * @Title: getActivityNumInTask
     * @Description: 获取activity栈中activity数量
     * @param @param context
     * @param @return 参数
     * @return int 返回类型
     * @author SQM
     * @date 2014-10-16 上午10:29:22
     */
    public static int getActivityNumInTask(Context context) {
        ActivityManager v_am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> v_list = v_am.getRunningTasks(1);
        if (v_list.size() != 1) {
            return -1;
        }
        return v_list.get(0).numActivities;
    }

    /**
     * @param @param  context
     * @param @return 参数
     * @return int 返回类型
     * @throws
     * @Title: getActivityRunningNumInTask
     * @Description: 获取activity栈中运行activity数量
     * @author SQM
     * @date 2014-10-16 上午10:30:14
     */
    public static int getActivityRunningNumInTask(Context context) {
        ActivityManager v_am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> v_list = v_am.getRunningTasks(1);
        if (v_list.size() != 1) {
            return -1;
        }
        return v_list.get(0).numRunning;
    }

    /***
     *
     * @Title: getActivityTopInTask
     * @Description: 获取activity栈顶activity信息
     * @param @param context
     * @param @return 参数
     * @return ComponentName 返回类型
     * @author SQM
     * @date 2014-10-16 上午10:31:28
     */
    public static ComponentName getActivityTopInTask(Context context) {
        ActivityManager v_am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> v_list = v_am.getRunningTasks(1);
        if (v_list.size() != 1) {
            return null;
        }
        return v_list.get(0).topActivity;
    }

    /***
     *
     * @Title: getActivityBaseInTask
     * @Description: 获取activity栈底activity信息
     * @param @param context
     * @param @return 参数
     * @return ComponentName 返回类型
     * @author SQM
     * @date 2014-10-16 上午10:32:40
     */
    public static ComponentName getActivityBaseInTask(Context context) {
        ActivityManager v_am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> v_list = v_am.getRunningTasks(1);
        if (v_list.size() != 1) {
            return null;
        }
        return v_list.get(0).baseActivity;
    }

    /***
     *
     * @Title: getActivityNumInApp
     * @Description: 获取程序activity数量
     * @param @return 参数
     * @return int 返回类型
     * @author SQM
     * @date 2014-11-5 上午9:35:56
     */
    public static int getActivityNumInApp() {
        try {
            Class v = Class.forName("android.app.ActivityThread");
            Method v_currentActivityThread = v.getMethod("currentActivityThread", new Class[]{});
            Object v_temp = v_currentActivityThread.invoke(v, new Object[]{});
            Field v_mActivities = v.getDeclaredField("mActivities");
            v_mActivities.setAccessible(true);
            Map<?, ?> o_mActivities = (Map<?, ?>) v_mActivities.get(v_temp);
            return o_mActivities.size();
        } catch (Throwable e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * @return void 返回类型
     * @Title: ShutDownAPP
     * @Description: 关闭整个程序
     * @author SQM
     * @date 2014-10-31 下午4:31:15
     */
    public static void ShutDownAPP() {
        try {
            NotificationManager notificationManager = (NotificationManager) BaseApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0x11);
            //todo
//            AsyncImageLoader.getInstance().shutdown();
//            NetHelper.getInstance().stop();
//            HandlerHelper.getInstance().stop();
//            DBHelper.getInstance().closeDB();
//            DBHelper.getInstance().close();
//            GlobalDataHelper.getInstance().clearAll();
//            GlobalDataHelper.getInstance().setIsShutDown(true);
            Thread.currentThread().sleep(300);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            Class v = Class.forName("android.app.ActivityThread");
            Method v_currentActivityThread = v.getMethod("currentActivityThread", new Class[]{});
            Object v_temp = v_currentActivityThread.invoke(v, new Object[]{});
            Field v_mActivities = v.getDeclaredField("mActivities");
            v_mActivities.setAccessible(true);
            Map<?, ?> o_mActivities = (Map<?, ?>) v_mActivities.get(v_temp);
            Set<?> v_keys = o_mActivities.keySet();
            int len = o_mActivities.size();
            for (Object k : v_keys) {
                Object v_p = o_mActivities.get(k);
                finishActivity(v_p, len);
            }
            Method v_scheduleExit = v.getDeclaredMethod("getHandler", new Class[]{});
            v_scheduleExit.setAccessible(true);
            Handler v_handler = (Handler) v_scheduleExit.invoke(v_temp, new Object[]{});
            v_handler.sendEmptyMessage(130);
        } catch (Throwable e) {
            e.printStackTrace();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * @param @param ActivityClientRecord 参数
     * @return void 返回类型
     * @Title: finishActivity
     * @Description: 关闭某个Activity
     * @author SQM
     * @date 2014-10-31 下午4:30:11
     */
    private static final void finishActivity(Object ActivityClientRecord, int len) {
        if (ActivityClientRecord == null) {
            return;
        }
        try {
            Field v_activityField = ActivityClientRecord.getClass().getDeclaredField("activity");
            v_activityField.setAccessible(true);
            Activity v_a = (Activity) v_activityField.get(ActivityClientRecord);
            // if (len == 1) {
            // Intent intent = new Intent(v_a, StopActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // v_a.startActivity(intent);
            // return;
            // }
            // System.out.println("activity=" + v_a.getClass().getSimpleName());
            // if (v_a.getClass().getSimpleName().equals("MainActivity")) {
            // int start =
            // BaseApplication.getInstance().getSharedPreferences("bb",
            // 1).getInt("start", 0);
            // BaseApplication.getInstance().getSharedPreferences("bb",
            // 1).edit().putInt("start", 1).commit();
            // if (!v_a.moveTaskToBack(true) && start == 0) {
            // Intent intent = new Intent(Intent.ACTION_MAIN);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
            // intent.addCategory(Intent.CATEGORY_HOME);
            // v_a.startActivity(intent);
            // }
            // } else {
            v_a.finish();
            // }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
