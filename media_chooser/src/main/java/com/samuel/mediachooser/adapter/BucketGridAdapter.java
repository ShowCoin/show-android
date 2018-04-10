
package com.samuel.mediachooser.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.samuel.mediachooser.R;
import com.samuel.mediachooser.Utilities.BucketEntry;
import com.samuel.mediachooser.Utilities.GalleryCache;
import com.samuel.mediachooser.Utilities.MediaChooserConstants;
import com.samuel.mediachooser.fragment.BucketVideoFragment;

import java.io.File;
import java.util.ArrayList;

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

public class BucketGridAdapter extends ArrayAdapter<BucketEntry> {

    public BucketVideoFragment bucketVideoFragment;

    private Context mContext;
    private ArrayList<BucketEntry> mBucketEntryList;
    private boolean mIsFromVideo;
    private int imgSize;
    private LayoutInflater mViewInflater;
    private GalleryCache mCache;

    public BucketGridAdapter(Context context, ArrayList<BucketEntry> categories, boolean isFromVideo) {

        super(context, 0, categories);
        mBucketEntryList = categories;
        mContext = context;
        mIsFromVideo = isFromVideo;
        mViewInflater = LayoutInflater.from(mContext);
        imgSize = mContext.getResources().getDisplayMetrics().widthPixels/3;
        mCache = GalleryCache.newInstance(mContext,imgSize,imgSize);

    }

    public int getCount() {
        return mBucketEntryList.size();
    }

    @Override
    public BucketEntry getItem(int position) {
        return mBucketEntryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addLatestEntry(String url) {
        int count = mBucketEntryList.size();
        boolean success = false;
        for (int i = 0; i < count; i++) {
            if (mBucketEntryList.get(i).bucketName.equals(MediaChooserConstants.folderName)) {
                mBucketEntryList.get(i).bucketUrl = url;
                success = true;
                break;
            }
        }

        if (!success) {
            BucketEntry latestBucketEntry = new BucketEntry(0,"0", MediaChooserConstants.folderName, new File(url).getParent(), url);
            mBucketEntryList.add(0, latestBucketEntry);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = mViewInflater.inflate(R.layout.view_grid_bucket_item_media_chooser, parent, false);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageViewFromMediaChooserBucketRowView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextViewFromMediaChooserBucketRowView);

            FrameLayout.LayoutParams imageParams = (FrameLayout.LayoutParams) holder.imageView.getLayoutParams();
            imageParams.width = imgSize;
            imageParams.height = imgSize;

            holder.imageView.setLayoutParams(imageParams);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mIsFromVideo) {
            mCache.loadBitmap((Activity)mContext,mBucketEntryList.get(position).fileId,mBucketEntryList.get(position).bucketUrl,holder.imageView);
        } else {
            String tag = (String) holder.imageView.getTag();
            if (tag==null||!tag.equals(mBucketEntryList.get(position).bucketUrl)) {
                holder.imageView.setTag(mBucketEntryList.get(position).bucketUrl);
                ImageLoader.getInstance().displayImage("file://" +mBucketEntryList.get(position).bucketUrl,new ImageViewAware(holder.imageView,true), ImageLoaderUtils.getDisplayImageOptions2());
            }
        }

        holder.nameTextView.setText(mBucketEntryList.get(position).bucketName);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
    }
}


