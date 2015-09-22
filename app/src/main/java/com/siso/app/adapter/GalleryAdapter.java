package com.siso.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.siso.app.entity.ImageInfo;
import com.siso.app.ui.R;
import com.squareup.picasso.Picasso;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ItemViewHolder> {
	
	private final LayoutInflater inflater;
	private ArrayList<ImageInfo> mData;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_photo_img);
        }
    }
    
    public GalleryAdapter(Context context,ArrayList<ImageInfo> mData){
    	this.inflater = LayoutInflater.from(context);
    	this.mData = mData;
    }

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ItemViewHolder itemViewHolder, int position) {
		Picasso picasso = Picasso.with(itemViewHolder.imageView.getContext());
        picasso.load(mData.get(position).path).placeholder(R.drawable.ic_img_loading).into(itemViewHolder.imageView);
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photopick_gridlist,parent,false);
		ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
	}

}
