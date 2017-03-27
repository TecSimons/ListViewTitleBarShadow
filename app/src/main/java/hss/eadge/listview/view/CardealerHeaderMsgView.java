package hss.eadge.listview.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import hss.eadge.listview.AndroidUtils;
import hss.eadge.listview.R;
import hss.eadge.listview.Utils;

/**
 * 车商主页的头部
 * <p/>
 * Created by Siy on 2016/7/15.
 */
public class CardealerHeaderMsgView extends HeaderViewInterface {
    private View view;

    private ViewGroup relativelayout1;

    private ImageView iv;

    public CardealerHeaderMsgView(Activity context) {
        super(context);
    }

    @Override
    protected void getView(Object list, ListView listView) {
        view = mInflate.inflate(R.layout.activity_layout_cardealer_header, listView, false);
        listView.addHeaderView(view);
        initView();
    }

    private void initView() {
        relativelayout1 = (ViewGroup) view.findViewById(R.id.relativelayout1);
        iv = (ImageView) view.findViewById(R.id.imageview1);
        Bitmap src = BitmapFactory.decodeResource(AndroidUtils.getResource(), R.drawable.delicious);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(AndroidUtils.getResource(), src);
        drawable.setCircular(true);
        iv.setImageDrawable(drawable);

        Utils.blurBitamp(R.drawable.delicious, 16.0f)
                .map(new Func1<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap call(Bitmap o) {
                        return Utils.darkBitmap(o);

                    }
                })
                .map(new Func1<Bitmap, Drawable>() {

                    @Override
                    public Drawable call(Bitmap bitmap) {
                        return Utils.bitmapToDrawable(bitmap);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<Drawable>() {
                            @Override
                            public void call(Drawable drawable) {
                                relativelayout1.setBackgroundDrawable(drawable);
                            }
                        }
                );
    }
}
