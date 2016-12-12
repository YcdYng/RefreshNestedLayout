/*
 * Copyright 2016 EastWood Yang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ycdyng.refreshnestedlayouttrial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ycdyng.refreshnestedlayout.RefreshNestedListViewLayout;
import com.ycdyng.refreshnestedlayout.kernel.RefreshNestedLayout;
import com.ycdyng.refreshnestedlayout.widget.adapter.BaseAdapterHelper;
import com.ycdyng.refreshnestedlayout.widget.adapter.QuickAdapter;

import java.util.ArrayList;

public class SampleListViewCustomAutoLoad extends AppCompatActivity {

    private RefreshNestedListViewLayout mRefresher;

    private QuickAdapter<SampleModel> mQuickAdapter;
    private ArrayList<SampleModel> mDataList = new ArrayList<SampleModel>();
    private int mAutoLoadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 20; i++) {
            SampleModel sampleMode = new SampleModel();
            sampleMode.setValues("ListView item_" + i);
            mDataList.add(sampleMode);
        }
        mQuickAdapter = new QuickAdapter<SampleModel>(this, R.layout.list_item, mDataList) {

            @Override
            protected void convert(int position, BaseAdapterHelper helper, SampleModel item) {
                helper.setText(R.id.textView1, item.getValues());
            }
        };

        setContentView(R.layout.sample_list_view_custom_auto_load); // set auto load layout
        // as well as below
        // mQuickAdapter.setAutoLoadResId(R.layout.custom_auto_load_layout);
        // mQuickAdapter.setClickableResId(R.layout.custom_clickable_layout);
        // mQuickAdapter.setLoadEndResId(R.layout.custom_load_end_layout);
        // mQuickAdapter.setLoadErrorResId(R.layout.custom_load_error_layout);

        mRefresher = (RefreshNestedListViewLayout) findViewById(R.id.refresh_layout);
        mRefresher.setAdapter(mQuickAdapter);
        mRefresher.setOnAutoLoadListener(new RefreshNestedLayout.OnAutoLoadListener() {
            @Override
            public void onLoading() {
                handleAutoLoadEvent();
            }
        });

        mRefresher.setAutoLoadUsable(true);
    }

    private void handleAutoLoadEvent() {
        mRefresher.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAutoLoadCount == 0) {
                    mAutoLoadCount++;
                    mRefresher.onAutoLoadingError();
                    return;
                }

                for (int i = 0; i < 20; i++) {
                    SampleModel sampleMode = new SampleModel();
                    sampleMode.setValues("ListView add item_" + (20 * (mAutoLoadCount + 1) + i) + " by Auto-Load");
                    mDataList.add(sampleMode);
                }
                mQuickAdapter.notifyDataSetChanged();

                if (mAutoLoadCount < 2) {
                    mAutoLoadCount++;
                    mRefresher.onAutoLoadingComplete(true);
                } else {
                    mRefresher.setShowLoadEnd(true);
                    mRefresher.setAutoLoadUsable(false);
                    mRefresher.onAutoLoadingComplete(false);
                }
            }
        }, 1500);
    }

}