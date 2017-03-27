package hss.eadge.listview.SmoothListView.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import hss.eadge.listview.AndroidUtils;
import hss.eadge.listview.R;
import hss.eadge.listview.RegularUtils;
import hss.eadge.listview.SmoothListView.SmoothListView;

/**
 * smoothlistview的基适配器
 * <p>
 * Created by Siy on 2016/7/18.
 */
public abstract class SmoothListViewbaseAdapter<T> extends BaseAdapter {
    private boolean isNoData = false;
    protected int mHeight;
    public static int ONE_SCREEN_COUNT = 6;
    protected Context mContext;
    protected List<T> mList;
    protected LayoutInflater mInflater;

    public SmoothListViewbaseAdapter(Context context) {
        this.mList = new ArrayList<>();
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return (T) mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 给适配填充数据源，用来显示数据
     *
     * @param list
     */
    public void setData(List<T> list) {
        isNoData = false;
        clearAll();
        addAll(list);

        //当可用数据少于设置的一屏数据时添加空数据将显示撑满一屏
        if (mList.size() < ONE_SCREEN_COUNT) {
            addAll(addEmptyData(1));
        }
        notifyDataSetChanged();
    }

    /**
     * 加载更多数据
     *
     * @param moreDate
     */
    public void loadMoreData(List<T> moreDate) {
        if (RegularUtils.isEmpty(moreDate)) {
            return;
        }

        addAll(moreDate);
        notifyDataSetChanged();
    }

    /**
     * 当数据少于一屏幕时，用来添加空数据作为填充，使数据占满一屏
     *
     * @param size 需要填充空数据个数
     * @return
     */
    public abstract List<T> addEmptyData(int size);

    /**
     * 设置空视图
     *
     */
    public void setEmptyView() {
        this.isNoData = true;
        clearAll();
        addAll(addEmptyData(1));
        notifyDataSetChanged();
    }

    private void clearAll() {
        mList.clear();
    }

    private void addAll(List<T> lists) {
        if (lists == null || lists.size() == 0) {
            return;
        }
        mList.addAll(lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isNoData) {
            if (parent instanceof SmoothListView) {
                ((SmoothListView) parent).setLoadMoreEnable(false);
            }
            convertView = mInflater.inflate(R.layout.xlistview_norecord, null);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidUtils.getScreenSize()[1]-mHeight);
            convertView = convertView.findViewById(R.id.xlistview_norecord_root);
            convertView.setLayoutParams(params);
            convertView.setTag(convertView);//将空数据视图设置成tag，以便于与撑满屏幕的view公用一个视图(注意：空视图一共只有一个item(空视图的item))，有
            //填充数据的视图一共有可用数据+1个（填充数据的视图）item
            return convertView;
        }

        //正常数据
        return getNormalView(position, convertView, parent);
    }


    /**
     * 加载正常视图
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public abstract View getNormalView(int position, View convertView, ViewGroup parent);

}
