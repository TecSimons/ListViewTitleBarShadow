package hss.eadge.listview;

import android.content.Context;
import android.util.TypedValue;

/**
 * 测量相关的工具类
 *
 * Created by Siy on 2016/5/31.
 */
public class DisplayUtil {
    /**
     * 密度无关像素 装换成 像素
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context,float dipValue){
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dipValue,context.getResources().getDisplayMetrics())+0.5f);
    }

    /**
     * 缩放独立像素 转换成 像素
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context,float spValue){
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spValue,context.getResources().getDisplayMetrics())+0.5f);
    }

    /**
     * 像素 转换成 密度无关像素
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context,float pxValue){
        return (int)(pxValue/(context.getResources().getDisplayMetrics().density)+0.5f);
    }

    /**
     * 像素 转换成 缩放独立像素
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context,float pxValue){
        return (int)(pxValue/(context.getResources().getDisplayMetrics().scaledDensity)+0.5f);
    }

}
