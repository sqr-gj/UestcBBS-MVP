package com.scatl.uestcbbs.module.home.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scatl.uestcbbs.R;
import com.scatl.uestcbbs.entity.CollectionListBean;
import com.scatl.uestcbbs.helper.glidehelper.GlideLoader4Common;
import com.scatl.uestcbbs.util.Constant;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Random;

/**
 * author: sca_tl
 * date: 2020/5/18 15:55
 * description:
 */
public class MyCollectionListAdapter extends BaseQuickAdapter<CollectionListBean, BaseViewHolder> {
    public MyCollectionListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CollectionListBean item) {
        helper.setText(R.id.item_collection_list_my_user_name, item.authorName)
                .setText(R.id.item_collection_list_my_title, item.collectionTitle)
                .setText(R.id.item_collection_list_my_update_time, "最后更新:" + item.latestUpdateDate)
                .setText(R.id.item_collection_list_my_latest_post, "最新主题:" + item.latestPostTitle)
                .addOnClickListener(R.id.item_collection_list_my_latest_post)
                .addOnClickListener(R.id.item_collection_list_my_user_avatar);

        Button subscribe = helper.getView(R.id.item_collection_list_my_subscribe_btn);
        subscribe.setText(new StringBuilder().append(item.subscribeCount).append("人订阅"));

        TextView dsp = helper.getView(R.id.item_collection_list_my_dsp);
        if (TextUtils.isEmpty(item.collectionDsp)) {
            dsp.setVisibility(View.GONE);
        } else {
            dsp.setVisibility(View.VISIBLE);
            dsp.setText(item.collectionDsp);
        }
        GlideLoader4Common.simpleLoad(mContext, item.authorAvatar, helper.getView(R.id.item_collection_list_my_user_avatar));

        TagFlowLayout tagFlowLayout = helper.getView(R.id.item_collection_list_my_tag);
        tagFlowLayout.setAdapter(new TagAdapter<String>(item.collectionTags) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView textView = new TextView(mContext);
                textView.setClickable(true);
                textView.setFocusable(true);
                textView.setTextSize(10);
                textView.setText(o);
                textView.setAlpha(0.9f);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.drawable.shape_collection_tag);
                textView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Constant.TAG_COLOR[new Random().nextInt(Constant.TAG_COLOR.length)])));
                return textView;
            }
        });
    }
}
