package com.yang.assetmanage.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 通用适配器
 *
 * @param <T> Created by liangfj on 2018/6/21.
 */

public abstract class RVAdapter<T> extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private static final int ITEM_VIEW_TYPE_EMPTY = 2;
    private static final int ITEM_VIEW_TYPE_FOOTER = 3;

    private List<T> mData = new ArrayList<>();
    private Context mContext;
    private int mItemLayoutId;

    private boolean mHeadAndEmptyEnable = false;
    private boolean mEmptyEnable = false;
    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;


    public boolean hasMore = true;
    public boolean isLoading = false;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    protected RVAdapter(Context context, int itemLayoutId) {
        mContext = context;
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getItemCount() {
        int count = getCount() + getHeaderViewsCount() + getFooterViewsCount();
        mEmptyEnable = false;
        if ((mHeadAndEmptyEnable && getHeaderViewsCount() == 1 && count == 1) || count == 0) {
            mEmptyEnable = true;
            count += getEmptyViewsCount();
        }
        return count;
    }

    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return ITEM_VIEW_TYPE_HEADER;
        }
        if (mFooterView != null && (position == getCount() + getHeaderViewsCount())) {
            return ITEM_VIEW_TYPE_FOOTER;
        }
        if (mEmptyView != null && getItemCount() == (mHeadAndEmptyEnable ? 2 : 1) && mEmptyEnable) {
            return ITEM_VIEW_TYPE_EMPTY;
        }
        return getItemType(position - getHeaderViewsCount());
    }

    private int getItemType(int position) {
        return ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM_VIEW_TYPE_HEADER:
                viewHolder = new ViewHolder(mHeaderView);
                break;
            case ITEM_VIEW_TYPE_FOOTER:
                viewHolder = new ViewHolder(mFooterView);
                break;
            case ITEM_VIEW_TYPE_EMPTY:
                if (mHeadAndEmptyEnable) {
                    ViewGroup.LayoutParams layoutParams = mEmptyView.getLayoutParams();
                    int emptyHeight = parent.getHeight() - mHeaderView.getHeight();
                    layoutParams.height = emptyHeight;
                }
                viewHolder = new ViewHolder(mEmptyView);
                break;
            default:
                viewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false));
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case ITEM_VIEW_TYPE_HEADER:
                break;
            case ITEM_VIEW_TYPE_FOOTER:
                break;
            case ITEM_VIEW_TYPE_EMPTY:
                break;
            default:
                // 默认普通item
                int itemPosition = position - getHeaderViewsCount();
                if (itemPosition < mData.size()) {
                    convert(holder, mData.get(itemPosition), itemPosition);
                    initItemClickListener(holder, itemPosition);
                }
                break;
        }
    }

    private void initItemClickListener(final ViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
    }

    protected abstract void convert(ViewHolder vH, T item, int position);

    public void addHeaderView(View header) {
        if (header == null) {
            throw new RuntimeException("header is null");
        }
        mHeaderView = header;
        this.notifyDataSetChanged();
    }

    public void removeHeaderView() {
        if (mHeaderView != null) {
            mHeaderView = null;
            this.notifyDataSetChanged();
        }
    }

    public void setEmptyView(View emptyView) {
        setEmptyView(false, emptyView);
    }

    public void setEmptyView(boolean isHeadAndEmpty, View emptyView) {
        mHeadAndEmptyEnable = isHeadAndEmpty;
        mEmptyView = emptyView;
    }

    public void removeEmptyView() {
        mEmptyView = null;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void addFooterView(View footer) {
        if (footer == null) {
            throw new RuntimeException("footer is null.");
        }
        mFooterView = footer;
        this.notifyDataSetChanged();
    }

    public void removeFooterView() {
        if (mFooterView != null) {
            mFooterView = null;
            this.notifyDataSetChanged();
        }
    }

    public int getHeaderViewsCount() {
        return mHeaderView == null ? 0 : 1;
    }

    public int getEmptyViewsCount() {
        return mEmptyView == null ? 0 : 1;
    }

    public int getFooterViewsCount() {
        return mFooterView == null ? 0 : 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 用来保存条目视图里面所有的控件
         */
        private SparseArray<View> mViews = null;

        /**
         * 构造函数
         *
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArray<View>();
        }

        /**
         * 根据控件id获取控件对象
         *
         * @param viewId
         * @return
         */
        @SuppressWarnings("unchecked")
        public <T> T getView(int viewId) {

            // 从集合中根据这个id获取view视图对象
            View view = mViews.get(viewId);

            // 如果为空，说明是第一次获取，里面没有，那就在布局文件中找到这个控件，并且存进集合中
            if (view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }

            // 返回控件对象
            return (T) view;
        }

        /**
         * 为TextView设置文本,按钮也可以用这个方法,button是textView的子类
         *
         * @param textViewId
         * @param content
         */
        public void setText(int textViewId, String content) {
            ((TextView) getView(textViewId)).setText(content);
        }

        /**
         * 为ImageView设置图片
         *
         * @param iv
         * @param imageId
         */
        public void setImage(ImageView iv, int imageId) {
            iv.setImageResource(imageId);
        }

        /**
         * 为ImageView设置图片
         *
         * @param imgId
         * @param imageId
         */
        public void setImage(int imgId, int imageId) {
            ((ImageView) getView(imgId)).setImageResource(imageId);
        }

        public void setVisibility(int viewID,int visibility){
            ((View)getView(viewID)).setVisibility(visibility);
        }
    }

    public List<T> getData() {
        return mData;
    }

    public int getDataSize() {
        return mData.size();
    }

    public T getData(int index) {
        return mData.size() > index ? mData.get(index) : null;
    }

    public void add(T d) {
        int startPos = mData.size();
        mData.add(d);
        notifyItemInserted(startPos);
    }

    public void addAll(List<T> data) {
        int curSize = mData.size();
        mData.addAll(data);
        if (curSize == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(curSize, data.size());
        }
    }

    public void remove(T d) {
        if (mData.contains(d)) {
            int posIndex = mData.indexOf(d);
            mData.remove(d);
            notifyItemRemoved(posIndex);
        }
    }

    public void remove(int index) {
        if (mData.size() > index) {
            mData.remove(index);
            notifyItemRemoved(index);
        }
    }

    public boolean contains(T d) {
        return mData.contains(d);
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mHeaderView != null || mFooterView != null || mEmptyView != null) {
                        return getItemViewType(position) == ITEM_VIEW_TYPE_HEADER
                                || getItemViewType(position) == ITEM_VIEW_TYPE_FOOTER
                                || getItemViewType(position) == ITEM_VIEW_TYPE_EMPTY
                                ? gridManager.getSpanCount() : 1;
                    }
                    return 1;
                }
            });
        }
    }



}

