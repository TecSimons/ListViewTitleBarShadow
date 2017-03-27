package hss.eadge.listview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;

import rx.Subscription;
import hss.eadge.listview.SmoothListView.SmoothListView;
import hss.eadge.listview.view.CardealerHeaderMsgView;

public class MainActivity extends AppCompatActivity implements SmoothListView.ISmoothListViewListener {

    private CardealerHeaderMsgView mCardealMsgView;
//
//    private CardealerHeaderFilterView mCardealFilterView;

    private SmoothListView mListView;

    private boolean isSmooth = false; // 没有吸附的前提下，是否在滑动

    private boolean isScrollIdle = true; // ListView是否在滑动

    private int msgViewTopSpace; // 车商视图距离顶部的距离

    private int filterViewTopSpace; // 筛选视图距离顶部的距离

    private int msgViewHeight = 250; // 广告视图的高度

    private View filterView; // 从ListView获取的筛选子View

    private boolean isStickyTop = false; // 是否吸附在顶部

    private View msgView;

    private int filterViewPosition;

   // private CardealFilterView fvTopFilter;

    private View rlBar;

    /**
     * 筛选的位置
     * <p/>
     * button1 是历史寻车
     * button2 是历史车源
     * button3 是联系我们
     */
    private int mFilterPosition;

    private CardealerCarSourceListAdapter c;

    private Subscription mSub;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initField();
        initListener();
    }

    private void initField() {
        mListView = (SmoothListView) findViewById(R.id.listview1);
        rlBar = findViewById(R.id.relativeLayout1);
        //fvTopFilter = (CardealFilterView) findViewById(R.id.filter);
        mCardealMsgView = new CardealerHeaderMsgView(this);
     //   mCardealFilterView = new CardealerHeaderFilterView(this);
        mCardealMsgView.fillView(new Object(), mListView);
       // mCardealFilterView.fillView(new Object(), mListView);
        mListView.setRefreshEnable(true);
        mListView.setLoadMoreEnable(true);
        mListView.setSmoothListViewListener(this);
        mListView.setAdapter(c = new CardealerCarSourceListAdapter(this, DisplayUtil.dip2px(MainActivity.this, 50 + 38.5f)));
        c.addTenToList();

        filterViewPosition = mListView.getHeaderViewsCount();
    }

    private void initListener() {

        mListView.setOnScrollListener(new SmoothListView.OnSmoothScrollListener() {
                                          @Override
                                          public void onSmoothScrolling(View view) {
                                          }

                                          @Override
                                          public void onScrollStateChanged(AbsListView view, int scrollState) {
                                              isScrollIdle = (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
                                          }

                                          @Override
                                          public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                                               int totalItemCount) {
                                              if (isScrollIdle && msgViewTopSpace < 0) return;

                                              // 获取广告头部View、自身的高度、距离顶部的高度
                                              if (msgView == null) {
                                                  msgView = mListView.getChildAt(1 - firstVisibleItem);
                                              }
                                              if (msgView != null) {
                                                  msgViewTopSpace = DisplayUtil.px2dip(MainActivity.this, msgView.getTop());
                                                  msgViewHeight = DisplayUtil.px2dip(MainActivity.this, msgView.getHeight());
                                              }

                                              // 获取筛选View、距离顶部的高度
                                              if (filterView == null) {
                                                  filterView = mListView.getChildAt(filterViewPosition - firstVisibleItem);
                                              }
                                              if (filterView != null) {
                                                  filterViewTopSpace = DisplayUtil.px2dip(MainActivity.this, filterView.getTop());
                                              }

                                              // 处理筛选是否吸附在顶部
                                              if (filterViewTopSpace > 50) {
                                                  isStickyTop = false; // 没有吸附在顶部
//                                                  fvTopFilter.setVisibility(View.GONE);
//                                                  if (mCardealFilterView.getMsub().isUnsubscribed()) {
//                                                      mCardealFilterView.setObservable();
//                                                  }
                                              } else {
                                                  isStickyTop = true; // 吸附在顶部
 //                                                 fvTopFilter.setVisibility(View.VISIBLE);
                                              }

                                              if (firstVisibleItem > filterViewPosition) {
                                                  isStickyTop = true;
//                                                  fvTopFilter.setVisibility(View.VISIBLE);
                                              }

 //                                             fvTopFilter.setmIsStickyTop(isStickyTop);

                                              if (isSmooth && isStickyTop) {
                                                  isSmooth = false;
//                                                  onFilterViewClick(mFilterPosition);
                                              }

                                              // 处理标题栏颜色渐变
                                              handleTitleBarColorEvaluate();
                                          }
                                      }

        );
    }

    @Override
    protected void onStop() {
        super.onStop();
    }




    // 处理标题栏颜色渐变
    private void handleTitleBarColorEvaluate() {
        float fraction;
        if (msgViewTopSpace > 0) {
            fraction = 1f - msgViewTopSpace * 1f / 60;
            if (fraction < 0f) fraction = 0f;
            rlBar.setAlpha(fraction);
            return;
        }

        float space = Math.abs(msgViewTopSpace) * 1f;
        fraction = space / (msgViewHeight - 50);
        if (fraction < 0f) fraction = 0f;
        if (fraction > 1f) fraction = 1f;
        rlBar.setAlpha(1f);

        if (fraction >= 1f || isStickyTop) {
            isStickyTop = true;

            rlBar.setBackgroundColor(AndroidUtils.getColor(R.color.colorPrimary));
        } else {
            rlBar.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(this, fraction, R.color.colorTransparent, R.color.colorPrimary));
        }
    }


    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.stopRefresh();
            }
        }, 3000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setLoadMoreEnable(false);
            }
        }, 3000);
    }


}
