package hss.eadge.listview.view;

import android.app.Activity;
import android.widget.ListView;

import rx.Subscription;
import rx.functions.Action1;
import hss.eadge.listview.AndroidUtils;
import hss.eadge.listview.RxBus.RxBus;
import hss.eadge.listview.SmoothListView.headerviewinterface.HeaderViewInterface;


/**
 * 筛选视图
 * Created by Siy on 2016/7/15.
 */
public class CardealerHeaderFilterView extends HeaderViewInterface {

    private CardealFilterView mFilterView;

    private Subscription msub;

    public CardealerHeaderFilterView(Activity context) {
        super(context);
    }

    @Override
    protected void getView(Object list, ListView listView) {
        mFilterView = new CardealFilterView(mContext);

        setObservable();
        listView.addHeaderView(mFilterView);
    }

    public void setObservable() {
        final RxBus rxBus = AndroidUtils.getRxBus();
        msub = rxBus.toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof CardealFilterView.ClickEvent) {
                            rxBus.send(new ClickEvent(((CardealFilterView.ClickEvent) o).position));
                        }

                        if (o instanceof CardealFilterView.CheckedChangeEvent) {
                            rxBus.send(new CheckedChangeEvent(((CardealFilterView.CheckedChangeEvent) o).position));
                        }

                    }
                });
    }


    public Subscription getMsub() {
        return msub;
    }


    public CardealFilterView getmFilterView() {
        return mFilterView;
    }

    public class CheckedChangeEvent {
        public int position;

        public CheckedChangeEvent(int position) {
            this.position = position;
        }
    }

    public class ClickEvent {
        int position;

        public ClickEvent(int position) {
            this.position = position;
        }
    }
}
