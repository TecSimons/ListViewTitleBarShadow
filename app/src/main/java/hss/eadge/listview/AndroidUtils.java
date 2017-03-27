package hss.eadge.listview;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import hss.eadge.listview.RxBus.RxBus;

/**
 * 一些Android操作常用的工具方法
 * <p/>
 * Created by Siy on 2016/6/14.
 */
public class AndroidUtils {
    private static Handler mHandler;

    private static long lastClickTime;

    /**
     * 防止暴力点击
     *
     * @param deltaTime 时间段，在该时间段内的点击都算作双击
     * @return
     */
    public static boolean isFastDoubleClick(long deltaTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < deltaTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    /**
     * 获取上下文对象
     *
     * @return
     */
    public static MyApplication getContext() {
        return MyApplication.getInstance();
    }

    /**
     * 获取主线程
     *
     * @return
     */
    public static Thread getMainThread() {
        return Looper.getMainLooper().getThread();
    }

    /**
     * 获取主线程的Looper
     *
     * @return
     */
    public static Looper getMainThreadLooper() {
        return Looper.getMainLooper();
    }

    /**
     * 获取主线程的handler
     *
     * @return
     */
    public static Handler getMainHandler() {
        if (mHandler == null) {
            synchronized (AndroidUtils.class) {
                if (mHandler == null) {
                    mHandler = new Handler(getMainThreadLooper());
                }
            }
        }
        return mHandler;
    }

    /**
     * 在主线程执行延时Runnable
     *
     * @param runnable    延时执行的runnable
     * @param delayMillis 延时的时间
     * @return 任务执行的结果
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getMainHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 在主线程执行Runnable
     *
     * @param runnable 执行的runna
     * @return 任务执行的结果
     */
    public static boolean post(Runnable runnable) {
        return getMainHandler().post(runnable);
    }

    /**
     * 从主线程的Looper里面移除runnable
     *
     * @param runnable
     */
    public static void removeCallback(Runnable runnable) {
        getMainHandler().removeCallbacks(runnable);
    }

    /**
     * 移除主线程中的所有的runnable和message
     */
    public static void removeAllcallbackAndMessage() {
        getMainHandler().removeCallbacksAndMessages(null);
    }

    /**
     * 判断是否在主线程
     *
     * @return
     */
    public static boolean isRunInMainThread() {
        return getMainThreadLooper() == Looper.myLooper();
    }

    /**
     * 获取资源对象
     *
     * @return
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 获取字符串
     *
     * @param resId
     * @return
     */
    public static String getString(@StringRes int resId) {
        return getResource().getString(resId);
    }

    /**
     * 获取dimen
     *
     * @param resId
     * @return
     */
    public static int getDimens(@DimenRes int resId) {
        return getResource().getDimensionPixelSize(resId);
    }

    /**
     * 获取Drawable
     *
     * @param resId
     * @return
     */
    public static Drawable getDrawable(@DrawableRes int resId) {
        return getResource().getDrawable(resId);
    }

    /**
     * 获取Color
     *
     * @param resId
     * @return
     */
    public static int getColor(@ColorRes int resId) {
        return getResource().getColor(resId);
    }


    /**
     * 获取屏幕的宽高
     *
     * @return int[0] 代表屏幕的宽
     * <br/>
     * int[1] 代表屏幕的高
     */
    public static int[] getScreenSize() {
        int[] size = new int[2];

        DisplayMetrics dm = new DisplayMetrics();

        WindowManager wm = (WindowManager) AndroidUtils.getContext().getSystemService(Context.WINDOW_SERVICE);

        wm.getDefaultDisplay().getMetrics(dm);

        size[0] = dm.widthPixels;
        size[1] = dm.heightPixels;
        return size;
    }




    /**
     * 获取版本号versionCode
     * @return 当前应用的版本号
     * 错误就返回-1
     */
    public static int getVersionCode() {
        try {
            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取RxBus
     * @return
     */
    public static RxBus getRxBus(){
        return getContext().getRxBusSingleton();
    }

}
