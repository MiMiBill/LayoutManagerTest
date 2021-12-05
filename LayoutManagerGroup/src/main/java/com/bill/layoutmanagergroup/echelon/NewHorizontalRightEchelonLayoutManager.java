package com.bill.layoutmanagergroup.echelon;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Bill
 * github: https://github.com/MiMiBill
 *
 */

public class NewHorizontalRightEchelonLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = "EchelonLayoutManager";

    private Context mContext;
    private int mItemViewWidth;
    private int mItemViewHeight;
    private int mItemCount;
    private int mScrollOffset = Integer.MAX_VALUE;
    private float mScale = 0.9f;

    public NewHorizontalRightEchelonLayoutManager(Context context) {
        this.mContext = context;
        mItemViewWidth = (int) (getHorizontalSpace() * 0.67f);//item的宽
        mItemViewHeight = (int) (mItemViewWidth * 1.46f);//item的高
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        Log.d(TAG,"generateDefaultLayoutParams");
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0 || state.isPreLayout()) return;
        removeAndRecycleAllViews(recycler);


        mItemViewWidth = (int) (getHorizontalSpace() * 0.67f);
        mItemViewHeight = (int) (mItemViewWidth * 1f);
        mItemCount = getItemCount();
        mScrollOffset = Math.min(Math.max(mItemViewWidth, mScrollOffset), mItemCount * mItemViewWidth);
        Log.d(TAG,"onLayoutChildren:mScrollOffset:" + mScrollOffset + "  mItemViewWidth:" + mItemViewWidth );
        layoutChild(recycler);
    }

//    @Override
//    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        int pendingScrollOffset = mScrollOffset + dy;
//        mScrollOffset = Math.min(Math.max(mItemViewHeight, mScrollOffset + dy), mItemCount * mItemViewHeight);
//        layoutChild(recycler);
//        Log.d(TAG,"scrollVerticallyBy");
//        return mScrollOffset - pendingScrollOffset + dy;
//    }



    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int pendingScrollOffset = mScrollOffset + dx;
        mScrollOffset = Math.min(Math.max(mItemViewWidth, mScrollOffset + dx), mItemCount * mItemViewWidth);
        layoutChild(recycler);
        Log.d(TAG,"scrollVerticallyBy:mScrollOffset:" + mScrollOffset + "  mItemViewWidth:" + mItemViewWidth );
        return mScrollOffset - pendingScrollOffset + dx;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }


    //    @Override
//    public boolean canScrollVertically() {
//        return true;
//    }


    private void layoutChild(RecyclerView.Recycler recycler) {
        if (getItemCount() == 0 ) return;
        //右边item能看得见的item的位置
        int rightItemPosition = (int) Math.floor(mScrollOffset / mItemViewWidth);
        //减去一个item的高度，剩余的空间，这个空间将用于布局剩余item
        int remainSpace = getHorizontalSpace() - mItemViewWidth;
        //底部item能看得见的高度
        int rightItemVisibleHeight = mScrollOffset % mItemViewWidth;
        //相对于一个item的高度，底部item能看得见部分的高度
        final float offsetPercentRelativeToItemView = rightItemVisibleHeight * 1.0f / mItemViewWidth;

        ArrayList<ItemViewInfo> layoutInfos = new ArrayList<>();
        //重底部开始布局，依次往上排
        for (int i = rightItemPosition - 1, j = 1; i >= 0; i--, j++) {
            double pow = Math.pow(0.9, j);
            //每个item之间的偏移是以剩余空间的一半为基数的，当然也可以定位其他值
//            double maxOffset = (getVerticalSpace() - mItemViewHeight) / 2 * pow;
//            double maxOffset = 400.0 * pow;
            double maxOffset = 200 * pow;
            //当j=1时，如果没有移动过，那么top就是remainSpace，如果已经移动过了，由于bottomItemPosition计算是向下取整
            //所以，这个时候bottomItemPosition倒数第二项了，底部一项在循环后补进去
            int left = (int) (remainSpace - offsetPercentRelativeToItemView * maxOffset);
            float scaleXY = (float) (Math.pow(mScale, j - 1) * (1 - offsetPercentRelativeToItemView * (1 - mScale)));
            float positonOffset = offsetPercentRelativeToItemView;
            float layoutPercent = left * 1.0f / getHorizontalSpace();
            ItemViewInfo info = new ItemViewInfo(left, scaleXY, positonOffset, layoutPercent);
            Log.d(TAG,"layoutChild" + info.toString());
            layoutInfos.add(0, info);
            remainSpace = (int) (remainSpace - maxOffset);
            if (remainSpace <= 0) {
//                info.setTop((int) (remainSpace + maxOffset));
                //top设置为0，那么就可以整个铺满
                info.setTop((int) (0));
                info.setPositionOffset(0);
                info.setLayoutPercent(info.getTop() / getVerticalSpace());
                info.setScaleXY((float) Math.pow(mScale, j - 1)); ;
                break;
            }
        }

        if (rightItemPosition < mItemCount) {//当移动过了
            //总高度减去看得见的部分就是底部top值
            final int start = getHorizontalSpace() - rightItemVisibleHeight;
            layoutInfos.add(new ItemViewInfo(start, 1.0f, rightItemVisibleHeight * 1.0f / mItemViewHeight, start * 1.0f / getVerticalSpace())
                    .setIsBottom());
        } else {
            rightItemPosition = rightItemPosition - 1;//99
        }

        int layoutCount = layoutInfos.size();
        final int startPos = rightItemPosition - (layoutCount - 1);
        final int endPos = rightItemPosition;
        final int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View childView = getChildAt(i);
            int pos = getPosition(childView);
            if (pos > endPos || pos < startPos) {
                removeAndRecycleView(childView, recycler);
            }
        }

        detachAndScrapAttachedViews(recycler);

        for (int i = 0; i < layoutCount; i++) {
            View view = recycler.getViewForPosition(startPos + i);
            ItemViewInfo layoutInfo = layoutInfos.get(i);
            addView(view);
            measureChildWithExactlySize(view);
            int top = (getVerticalSpace() - mItemViewHeight) / 2;
            int left = layoutInfo.getTop();//这里是left
            //int left, int top, int right, int bottom
            layoutDecoratedWithMargins(view, left, top, left + mItemViewWidth, top + mItemViewHeight);
            view.setPivotX(0);
            view.setPivotY(view.getHeight()/2);
            view.setScaleX(layoutInfo.getScaleXY());
            view.setScaleY(layoutInfo.getScaleXY());
        }
    }

    /**
     * 测量itemview的确切大小
     */
    private void measureChildWithExactlySize(View child ) {
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(mItemViewWidth, View.MeasureSpec.EXACTLY);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(mItemViewHeight, View.MeasureSpec.EXACTLY);
        child.measure(widthSpec, heightSpec);
    }

    /**
     * 获取RecyclerView的显示高度
     */
    public int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    /**
     * 获取RecyclerView的显示宽度
     */
    public int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

}

