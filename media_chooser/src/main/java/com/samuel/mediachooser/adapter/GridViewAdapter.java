package com.samuel.mediachooser.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.samuel.mediachooser.R;
import com.samuel.mediachooser.Utilities.GalleryCache;
import com.samuel.mediachooser.Utilities.MediaModel;
import com.samuel.mediachooser.fragment.VideoFragment;

import java.util.List;

import one.show.live.util.FrescoUtils;
import one.show.live.util.ImageLoaderUtils;


/*
 * Copyright 2015 - learnNcode (learnncode@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

public class GridViewAdapter extends ArrayAdapter<MediaModel> {
    public VideoFragment videoFragment;

    private Context mContext;
    private List<MediaModel> mGalleryModelList;
    private int mWidth;
    private boolean mIsFromVideo;
    private LayoutInflater mViewInflater;

    private GalleryCache mCache;


    public GridViewAdapter(Context context, List<MediaModel> categories, boolean isFromVideo) {
        super(context, 0, categories);
        mGalleryModelList = categories;
        mContext = context;
        mIsFromVideo = isFromVideo;
        mViewInflater = LayoutInflater.from(mContext);
        mWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        mCache = GalleryCache.newInstance(mContext,mWidth / 3,mWidth / 3);
    }

    public void addLatestEntry(MediaModel mediaModel) {
        if (mediaModel != null) {
            mGalleryModelList.add(0, mediaModel);
        }
        notifyDataSetChanged();
    }

    public int getCount() {
		return mGalleryModelList.size();
    }

    @Override
    public MediaModel getItem(int position) {
        return mGalleryModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = mViewInflater.inflate(R.layout.view_grid_item_media_chooser, parent, false);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageViewFromMediaChooserGridItemRowView);
            holder.checkedView = (RelativeLayout) convertView.findViewById(R.id.checkedViewFromMediaChooserGridItemRowView);

            LayoutParams imageParams = (LayoutParams) holder.imageView.getLayoutParams();
            imageParams.width = mWidth / 4;
            imageParams.height = mWidth / 4;

            holder.imageView.setLayoutParams(imageParams);
            holder.checkedView.setLayoutParams(imageParams);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set the status according to this Category item

        if (mIsFromVideo) {
                mCache.loadBitmap((Activity)mContext,mGalleryModelList.get(position).id,mGalleryModelList.get(position).url,holder.imageView);
        } else {

//            FrescoUtils.bind(holder.imageView,FrescoUtils.getFileUri(mGalleryModelList.get(position).url));

            String tag = (String) holder.imageView.getTag();
            if (tag==null||!tag.equals(mGalleryModelList.get(position).url)) {
                holder.imageView.setTag(mGalleryModelList.get(position).url);
                ImageLoader.getInstance().displayImage("file://" +mGalleryModelList.get(position).url,new ImageViewAware(holder.imageView,true), ImageLoaderUtils.getDisplayImageOptions2());
            }


//            ImageLoader.getInstance().displayImage("file://" +mGalleryModelList.get(position).url,new ImageViewAware(holder.imageView,true), ImageLoaderUtils.getDisplayImageOptions2());
        }

        holder.checkedView.bringToFront();
        if (mGalleryModelList.get(position).status) {
            holder.checkedView.setVisibility(View.VISIBLE);
        } else {
            holder.checkedView.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        RelativeLayout checkedView;
    }

}
