package com.bill.layoutmanagergroup.slide;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Bill
 * github: https://github.com/MiMiBill
 *
 */


public interface OnSlideListener<T> {

    void onSliding(RecyclerView.ViewHolder viewHolder, float ratio, int direction);

    void onSlided(RecyclerView.ViewHolder viewHolder, T t, int direction);

    void onClear();

}
