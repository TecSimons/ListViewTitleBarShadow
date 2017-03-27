package hss.eadge.listview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * 其他工具方法
 * <p/>
 * Created by Siy on 2016/7/13.
 */
public class Utils {

    /**
     * 判断手机是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断手机是否安装qq
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断手机是否安装新浪微博
     *
     * @param context
     * @return
     */
    public static boolean isWeiboInstalled(@NonNull Context context) {
        PackageManager pm;
        if ((pm = context.getApplicationContext().getPackageManager()) == null) {
            return false;
        }
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String name = info.packageName.toLowerCase(Locale.ENGLISH);
            if ("com.sina.weibo".equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成新的颜色值
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public static int getNewColorByStartEndColor(float fraction, int startValue, int endValue) {
        return evaluate(fraction, AndroidUtils.getColor(startValue), AndroidUtils.getColor(endValue));
    }

    /**
     * 成新的颜色值
     *
     * @param fraction   颜色取值的级别 (0.0f ~ 1.0f)
     * @param startValue 开始显示的颜色
     * @param endValue   结束显示的颜色
     * @return 返回生成新的颜色值
     */
    public static int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

    /**
     * 图片高斯模糊的方法
     *
     * @param resId
     * @param radius
     * @return
     */
    public static Observable<Bitmap> blurBitamp(@DrawableRes final int resId, final float radius) {

       return Observable.just(resId)
               .map(new Func1<Integer, Bitmap>() {
                   @Override
                   public Bitmap call(Integer integer) {
                       return BitmapFactory.decodeResource(AndroidUtils.getResource(),resId);
                   }
               })
                .map(new Func1<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap call(Bitmap bitmap) {
                        //创建一个空的Bitmap用来输出模糊后的Bitmap
                        Bitmap outBitMap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

                        //创建一个新的RenderScript对象，这个类提供了RenderScript context，在创建其他RS类之前必须要先创建这个类，他控制RenderScript的初始化，资源管理，释放
                        RenderScript rs = RenderScript.create(AndroidUtils.getContext());

                        // 创建高斯模糊对象
                        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

                        //创建Allocations，此类是将数据传递给RenderScript内核的主要方法，并制定一个后备类型存储给定类型
                        Allocation allin = Allocation.createFromBitmap(rs, bitmap);
                        Allocation allout = Allocation.createFromBitmap(rs, outBitMap);

                        // 设定模糊度
                        blurScript.setRadius(radius);

                        //执行模糊
                        blurScript.setInput(allin);
                        blurScript.forEach(allout);

                        //创建最终模糊后的bitmap从allout到outbitmap
                        allout.copyTo(outBitMap);

                        //销毁rs对象
                        rs.destroy();
                        return outBitMap;
                    }
                });
    }

    /**
     * 图片变暗
     *
     * @param bitmap 需要变暗的图片
     * @return
     */
    public static Bitmap darkBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        // 生成色彩矩阵
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.5F, 0, 0, 0, 0,
                0, 0.5F, 0, 0, 0,
                0, 0, 0.5F, 0, 0,
                0, 0, 0, 1, 0,
        });

        //创建一个空的Bitmap用来输出变暗后的Bitmap
        Bitmap drakBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        Canvas canvas = new Canvas(drakBitmap);

        canvas.drawBitmap(bitmap, 0, 0, paint);

        return drakBitmap;
    }

    /**
     * 打电话
     *
     * @param acitivty
     * @param phoneNum 电话号码
     */
    public static void callPhone(Activity acitivty, String phoneNum) {
        //意图：想干什么事
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        //url:统一资源定位符
        //uri:统一资源标示符（更广）
        intent.setData(Uri.parse("tel:" + phoneNum));
        //开启系统拨号器
        acitivty.startActivity(intent);
    }


    /**
     * 将文字进行颜色突出处理
     *
     * @param str          整个文字
     * @param start        需要突出处理的文字的开始
     * @param end          需要突出处理的文字结束
     * @param flag         突出文字处理的标志，Spanned.SPAN_INCLUSIVE_INCLUSIVE，Spanned.SPAN_EXCLUSIVE_EXCLUSIVE等等
     * @param fontColor    整个字体的颜色
     * @param specialColor 特许处理处理字体的颜色
     * @return
     */
    public static CharSequence getSpannableString(CharSequence str, int start, int end, int flag, int fontColor, int specialColor) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(fontColor), 0, str.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(specialColor), start, end, flag);
        return spannableString;
    }

    /**
     * 解析json成map
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, String> parsJson(String jsonStr) {
        Map<String, String> map = new HashMap<>();
        try {
            JSONObject json = new JSONObject(jsonStr);
            Iterator<String> it = json.keys();

            while (it.hasNext()) {
                String myKey = it.next().toString();
                map.put(myKey, json.optString(myKey));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 比较2个对象是否相同，不相同返回不相同的字段
     *
     * @param object
     * @param comparObject
     * @return
     */
    public static List<Field> compareObject(Object object, Object comparObject) {
        if (!object.getClass().getName().equals(comparObject.getClass().getName())) {
            throw new IllegalArgumentException("比较的对象不属于同一个类型");
        }
        List<Field> list = new ArrayList<>();
        try {
            Class srcClazz = object.getClass();
            Class tarClazz = comparObject.getClass();

            Field[] srcfields = srcClazz.getDeclaredFields();
            Field[] tarfields = tarClazz.getDeclaredFields();

            for (int i = 0; i < srcfields.length; i++) {
                srcfields[i].setAccessible(true);
                tarfields[i].setAccessible(true);
                if (srcfields[i].getType().getName().equals(String.class.getName()) && srcfields[i].getType().getName().equals(String.class.getName()) &&
                        !srcfields[i].get(object).equals(tarfields[i].get(comparObject))) {
                    list.add(srcfields[i]);
                }
                srcfields[i].setAccessible(false);
                tarfields[i].setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 安装软件
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Uri uri = Uri.fromFile(file);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        // 执行意图进行安装
        context.startActivity(install);
    }

    /**
     * 将Drawable转化成bitmap
     *
     * @param drawable 要转变的Drawable
     * @return 转换后返回的Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        int w = drawable.getIntrinsicWidth();

        int h = drawable.getIntrinsicHeight();

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;

        Bitmap tempBitmap = Bitmap.createBitmap(w <= 0 ? AndroidUtils.getScreenSize()[0] : w, h <= 0 ? AndroidUtils.getScreenSize()[1] / 3 : h, config);

        Canvas c = new Canvas(tempBitmap);

        drawable.setBounds(0, 0, w, h);

        drawable.draw(c);

        return tempBitmap;
    }

    /**
     * 将biemap转换成Drawalbe
     *
     * @param bitmap 要转换的bitmap
     * @return 转换后返回的Drawable
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(AndroidUtils.getResource(), bitmap);
    }
}
