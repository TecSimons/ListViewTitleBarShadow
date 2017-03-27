package hss.eadge.listview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import hss.eadge.listview.AndroidUtils;
import hss.eadge.listview.R;
import hss.eadge.listview.RxBus.RxBus;


/**
 *
 * 筛选视图
 * Created by Siy on 2016/7/15.
 */
public class CardealFilterView extends LinearLayout implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{

    private RadioGroup mRg;

    /**
     * 是否吸附在顶部
     * <p/>
     * 默认是没有吸附的
     */
    private boolean mIsStickyTop = false;

    public CardealFilterView(Context context) {
        this(context, null);
    }

    public CardealFilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardealFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_layout_cardealer_filter, this);
        mRg = (RadioGroup) view.findViewById(R.id.radiogroup1);
        mRg.setOnCheckedChangeListener(this);
        mRg.findViewById(R.id.button1).setOnClickListener(this);
        mRg.findViewById(R.id.button2).setOnClickListener(this);
        mRg.findViewById(R.id.button3).setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RxBus rxBus = AndroidUtils.getRxBus();
        if (rxBus.hasObservers()){
            rxBus.send(new CheckedChangeEvent(checkedId));
        }
    }

    @Override
    public void onClick(View v) {
        RxBus rxBus = AndroidUtils.getRxBus();
        if (rxBus.hasObservers()){
            rxBus.send(new ClickEvent(v.getId()));
        }
    }

    /**
     * 设置某个radioButton选中
     *
     * @param position
     */
    public void setItemChecked(int position) {
        if (mRg!=null) {
            View v = mRg.findViewById(position);
            if (v != null && v instanceof RadioButton) {
                ((RadioButton) v).setChecked(true);
            }
        }
    }

    public void setmIsStickyTop(boolean mIsStickyTop) {
        this.mIsStickyTop = mIsStickyTop;
    }

    public class CheckedChangeEvent{
        public int position;
        public CheckedChangeEvent(int position) {
            this.position = position;
        }
    }

    public class ClickEvent{
        int position;

        public ClickEvent(int position) {
            this.position = position;
        }
    }
}
