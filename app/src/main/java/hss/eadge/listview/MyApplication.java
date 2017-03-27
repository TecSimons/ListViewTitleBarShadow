package hss.eadge.listview;

import android.app.Application;

import hss.eadge.listview.RxBus.RxBus;

/**
 * Created by Siy on 2016/8/26.
 */
public class MyApplication extends Application{
    private static MyApplication CONTEXT;

    private RxBus mRxBus;


    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }

    public static MyApplication getInstance() {
        return CONTEXT;
    }

    public RxBus getRxBusSingleton() {
        if (mRxBus == null) {
            mRxBus = new RxBus();
        }
        return mRxBus;
    }
}
