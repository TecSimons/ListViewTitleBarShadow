package hss.eadge.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 车源列表的适配器
 * <p>
 * Created by Siy on 2016/6/4.
 */
public class CardealerCarSourceListAdapter extends SmoothListViewbaseAdapter {
    private Context mContext;

    private LayoutInflater mInflater;

    private final int count = 3;

    public static final int VIEW_TYPE_1 = 0;

    public static final int VIEW_TYPE_2 = 1;

    public CardealerCarSourceListAdapter(Context context, int height) {
        super(context);
        this.mHeight = height;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public List addEmptyData(int size) {
        List list = new ArrayList();
        for (int i = 0; i < size; i++) {
            list.add(new Object());
        }
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if (o instanceof NotEmpty) {
            return VIEW_TYPE_1;
        }
        return VIEW_TYPE_2;
    }

    @Override
    public int getViewTypeCount() {
        return count;
    }

    @Override
    public View getNormalView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case VIEW_TYPE_1:
                    convertView = mInflater.inflate(R.layout.fragment_layout_carsource_item2, parent, false);
                    break;
                case VIEW_TYPE_2:
                    convertView = new View(mContext);
                    break;
            }
        }
            switch (type) {
                //这个视图有可能是空视图，也有可能是补空视图（就是有数据，但是不足以占满整个屏幕时，增加的视图将其占满整个视图）
                //当getCount()为1，就是空视图，getCount()>1就是补空视图
                case VIEW_TYPE_2:
                    AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            (AndroidUtils.getScreenSize()[1] - getItemViewHeigh(parent) * (getCount() - 1) - mHeight));
                    convertView.setLayoutParams(params);
                    if (getCount() != 1) {//标识有填充空数据
                        convertView.setVisibility(View.INVISIBLE);
                    } else {//就只有一个空视图
                        convertView.setVisibility(View.VISIBLE);
                    }
                    break;
            }

        return convertView;
    }

    /**
     * 获取ListViewItem的高度
     *
     * @param parent
     * @return
     */
    private int getItemViewHeigh(ViewGroup parent) {
        View view = getView(0, null, parent);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    public void addTenToList() {
        List tempList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            tempList.add(new NotEmpty());
        }
        setData(tempList);
    }

    public void addOneDataToList() {
        List tempList = new ArrayList();
        tempList.add(new NotEmpty());
        tempList.add(new NotEmpty());

        setData(tempList);
    }

    public class NotEmpty {
        public NotEmpty() {
        }
    }
}
