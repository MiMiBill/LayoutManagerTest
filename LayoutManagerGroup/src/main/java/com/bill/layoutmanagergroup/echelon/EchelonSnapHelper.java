/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific languag`e governing permissions and
 * limitations under the License.
 */

package com.bill.layoutmanagergroup.echelon;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import com.bill.layoutmanagergroup.skidright.SkidRightLayoutManager;

/**
 * Created by Bill
 * github: https://github.com/MiMiBill
 *
 */

public class EchelonSnapHelper extends SnapHelper {
    private int mDirection;

    @Override
    public int[] calculateDistanceToFinalSnap(
            @NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {

        if (layoutManager instanceof NewHorizontalLeftEchelonLayoutManager) {
            int[] out = new int[2];
            if (layoutManager.canScrollHorizontally()) {
                out[0] = ((NewHorizontalLeftEchelonLayoutManager) layoutManager).calculateDistanceToPosition();
                out[1] = 0;
            }

//            else {
//                out[0] = 0;
//                out[1] = ((SkidRightLayoutManager) layoutManager).calculateDistanceToPosition(
//                        layoutManager.getPosition(targetView));
//            }
            return out;
        }
        return null;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX,
                                      int velocityY) {
        if (layoutManager.canScrollHorizontally()) {
            mDirection = velocityX;
        } else {
            mDirection = velocityY;
        }
        return RecyclerView.NO_POSITION;
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof NewHorizontalLeftEchelonLayoutManager) {
            int pos = ((NewHorizontalLeftEchelonLayoutManager) layoutManager).getFixedScrollPosition(
                    mDirection, mDirection != 0 ? 0.5f : 0.5f);
            mDirection = 0;
            if (pos != RecyclerView.NO_POSITION) {
                return layoutManager.findViewByPosition(pos);
            }
        }
        return null;
    }
}
