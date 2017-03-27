package hss.eadge.listview;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则校验的工具类
 *
 * Created by Siy on 2016/6/6.
 */
public class RegularUtils {
        /**
         * 功能：判断字符串是否为数字
         *
         * @param str
         * @return
         */
        public static boolean isNumeric(String str) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if (isNum.matches()) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * 功能：判断字符串是否为日期格式
         *
         * @param strDate
         * @return
         */
        public static boolean isDate(String strDate) {
            String regxStr="^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
            Pattern pattern = Pattern.compile(regxStr);
            Matcher m = pattern.matcher(strDate);
            if (m.matches()) {
                return true;
            } else {
                return false;
            }
        }
        /*********************************** 身份证验证结束 ****************************************/

    /**
     * 手机号验证
     * @param phoneNum
     * @return
     */
    public static boolean phoneNumValidate(String phoneNum){
            String regxStr = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$";
            Pattern pattern = Pattern.compile(regxStr);
            Matcher m = pattern.matcher(phoneNum);
            return m.matches();
        }

    public static boolean passWordValidate(String password){
        String regxStr ="^[0-9A-Za-z]{6,}$";
        Pattern pattern = Pattern.compile(regxStr);
        Matcher m = pattern.matcher(password);
        return m.matches();
    }


    public static boolean isEmpty(String srcStr){
        if (null == srcStr){
            return true;
        }

        return "".equals(srcStr.trim());
    }

    /**
     * 如果传入的字符为空则返回“-”
     * @param srcStr
     * @return
     */
    public static String replaceByStrike(String srcStr){
        if (isEmpty(srcStr)){
            return "-";
        }else{
            return srcStr;
        }
    }


    /**
     * 集合是否为空
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 数组是否为空
     * @param array
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(T...array){
       return array==null || array.length<=0;
    }

    /**
     * map是否为空
     * @param map
     * @return
     */
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }


}
