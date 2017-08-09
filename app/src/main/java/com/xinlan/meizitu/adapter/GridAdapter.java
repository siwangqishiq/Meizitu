package com.xinlan.meizitu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xinlan.meizitu.ImageUtil;
import com.xinlan.meizitu.R;
import com.xinlan.meizitu.data.Node;
import com.xinlan.meizitu.data.Resource;
import com.xinlan.meizitu.holder.GridViewHolder;

/**
 * Created by panyi on 2017/8/9.
 */

public class GridAdapter extends RecyclerView.Adapter<GridViewHolder> {

    public static interface IItemClick {
        void onItemClick(final int pos);
    }

    private IItemClick mItemClick;

    public void setItemClick(IItemClick mItemClick) {
        this.mItemClick = mItemClick;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_image, parent, false);
        return new GridViewHolder(item);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, final int position) {
        // setViewHeight(holder.mImage, position);

        Node node = Resource.getInstance().getRootList().get(position);
        if (node == null)
            return;

        holder.mTitle.setText(node.getTitle());
        ImageUtil.loadImage(holder.mImage.getContext(), node.getRefer(), node.getImage(), holder.mImage);

        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClick != null) {
                    mItemClick.onItemClick(position);
                }
            }
        });
    }

    private void setViewHeight(ImageView view, int pos) {
        int width = view.getWidth();
        if (width != 0) {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
            params.height = width;
            view.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return Resource.getInstance().getRootList().size();
    }
}
