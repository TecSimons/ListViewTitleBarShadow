package hss.eadge.listview.RxBus;

import rx.Subscription;

/**
 * RxJava的工具类
 * Created by Siy on 2016/8/24.
 */
public class RxUtils {
    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
