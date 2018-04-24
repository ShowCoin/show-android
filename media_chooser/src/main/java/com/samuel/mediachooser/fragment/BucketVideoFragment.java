
package com.samuel.mediachooser.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.VideoColumns;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.samuel.mediachooser.R;
import com.samuel.mediachooser.Utilities.BucketEntry;
import com.samuel.mediachooser.Utilities.MediaChooserConstants;
import com.samuel.mediachooser.activity.MediaChooserActivity;
import com.samuel.mediachooser.adapter.BucketGridAdapter;

import java.io.File;
import java.util.ArrayList;

import one.show.live.common.ui.BaseApplication;


/**
 * 视频目录
 */
public class BucketVideoFragment extends Fragment {

    // The indices should match the following projections.
    private final int INDEX_File_ID = 0;
    private final int INDEX_BUCKET_ID = 1;
    private final int INDEX_BUCKET_NAME = 2;
    private final int INDEX_File_URL = 3;
    private final int INDEX_File_DURATION = 4;

    private final String[] PROJECTION_BUCKET = {
            VideoColumns._ID,
            VideoColumns.BUCKET_ID,
            VideoColumns.BUCKET_DISPLAY_NAME,
            VideoColumns.DATA,
            VideoColumns.DURATION
    };

    private View mView;
    private BucketGridAdapter mBucketAdapter;
    private GridView mGridView;
    private Cursor mCursor;


    public BucketVideoFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setBackgroundDrawable(null);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(MediaChooserConstants.BRAODCAST_VIDEO_RECEIVER));


        if (mView == null) {
            mView = inflater.inflate(R.layout.view_grid_layout_media_chooser, container, false);
            mGridView = (GridView) mView.findViewById(R.id.gridViewFromMediaChooser);
            init();
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
            if (mBucketAdapter == null || mBucketAdapter.getCount() == 0) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
            }
        }
        return mView;
    }


    private void init() {
        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;


        mCursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, PROJECTION_BUCKET, null, null, orderBy + " DESC");
        ArrayList<BucketEntry> buffer = new ArrayList<>();
        try {

            while (mCursor.moveToNext()) {

                int duration = mCursor.getInt(INDEX_File_DURATION);
                String path = mCursor.getString(INDEX_File_URL);


                if (duration < 3000 || duration > 10000) {
                    continue;
                }

                if (!path.toUpperCase().endsWith(".MP4")) {
                    continue;
                }


                //合并所有拍摄的临时文件

                String oldLocalFilePath = BaseApplication.getInstance().getOldVideoCachePath();
                String localFilePath = BaseApplication.getInstance().getVideoCachePath();

                BucketEntry entry = null;

                if (path.startsWith(oldLocalFilePath)) {
                    entry = new BucketEntry(
                            mCursor.getLong(INDEX_File_ID),
                            mCursor.getString(INDEX_BUCKET_ID),
                            "showold", oldLocalFilePath, path);
                } else if (path.startsWith(localFilePath)) {
                    entry = new BucketEntry(
                            mCursor.getLong(INDEX_File_ID),
                            mCursor.getString(INDEX_BUCKET_ID),
                            "show", localFilePath, path);
                } else {
                    entry = new BucketEntry(
                            mCursor.getLong(INDEX_File_ID),
                            mCursor.getString(INDEX_BUCKET_ID),
                            mCursor.getString(INDEX_BUCKET_NAME), new File(path).getParent(), path);
                }


                if (!buffer.contains(entry)) {
                    buffer.add(entry);
//                    Log.i("BucketGridAdapter", "BucketVideoFragment--buffer|" + buffer.size() + "--entry|" + entry.bucketName + "---------|" + entry.bucketUrl);
                }
            }

            if (mCursor.getCount() > 0) {
                mBucketAdapter = new BucketGridAdapter(getActivity(), buffer, true);
                mBucketAdapter.bucketVideoFragment = this;
                mGridView.setAdapter(mBucketAdapter);
            } else {
                Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
            }
            mGridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapter, View view,
                                        int position, long id) {

                    BucketEntry bucketEntry = (BucketEntry) adapter.getItemAtPosition(position);
                    Intent selectImageIntent = new Intent(getActivity(), MediaChooserActivity.class);
                    selectImageIntent.putExtra("name", bucketEntry.bucketName);
                    selectImageIntent.putExtra("folderName", bucketEntry.folderName);
                    selectImageIntent.putExtra("isFromBucket", true);
                    getActivity().startActivityForResult(selectImageIntent, MediaChooserConstants.BUCKET_SELECT_VIDEO_CODE);

                }
            });

        } finally {
            mCursor.close();
        }
    }

    public BucketGridAdapter getAdapter() {
        if (mBucketAdapter != null) {
            return mBucketAdapter;
        }
        return null;
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBucketAdapter.addLatestEntry(intent.getStringExtra("value"));
            mBucketAdapter.notifyDataSetChanged();
        }
    };


    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}
