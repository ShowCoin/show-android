package com.samuel.mediachooser.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.samuel.mediachooser.R;
import com.samuel.mediachooser.Utilities.MediaChooserConstants;
import com.samuel.mediachooser.Utilities.MediaModel;
import com.samuel.mediachooser.adapter.GridViewAdapter;

import java.io.File;
import java.util.ArrayList;


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
public class VideoFragment extends Fragment implements OnScrollListener {

    private final Uri MEDIA_EXTERNAL_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


    private final int INDEX_ID = 0;
    private final int INDEX_URL = 1;
    private final int INDEX_DURATION = 2;

    private final String[] PROJECTION = {
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
    };

    private GridViewAdapter mVideoAdapter;
    private GridView mVideoGridView;
    private Cursor mCursor;
    private ArrayList<String> mSelectedItems = new ArrayList<String>();
    private ArrayList<MediaModel> mGalleryModelList;
    private View mView;
    private OnVideoSelectedListener mCallback;


    // Container Activity must implement this interface
    public interface OnVideoSelectedListener {
        public void onVideoSelected(int count);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnVideoSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnVideoSelectedListener");
        }
    }

    public VideoFragment() {
        setRetainInstance(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.view_grid_layout_media_chooser, container, false);

            getActivity().getWindow().setBackgroundDrawable(null);

            mVideoGridView = (GridView) mView.findViewById(R.id.gridViewFromMediaChooser);

            if (getArguments() != null) {
                initVideos(getArguments().getString("name"));
            } else {
                initVideos();
            }

        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
            if (mVideoAdapter == null || mVideoAdapter.getCount() == 0) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
            }
        }

        return mView;
    }



    private void initVideos(String bucketName) {

        try {
            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
//            String searchParams = MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME + " = \"" + bucketName + "\"";

            String searchParams = MediaStore.Video.Media.DATA + " like '" + bucketName + "%'";


            mCursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, PROJECTION, searchParams, null, orderBy + " DESC");
            setAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initVideos() {

        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            //Here we set up a string array of the thumbnail ID column we want to get back


            mCursor = getActivity().getContentResolver().query(MEDIA_EXTERNAL_CONTENT_URI, PROJECTION, null, null, orderBy + " DESC");
            setAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAdapter() {
        int count = mCursor.getCount();

        if (count > 0) {

            //move position to first element
            mCursor.moveToFirst();

            mGalleryModelList = new ArrayList<MediaModel>();
            for (int i = 0; i < count; i++) {
                mCursor.moveToPosition(i);

                long id = mCursor.getLong(INDEX_ID);
                int duration = mCursor.getInt(INDEX_DURATION);
                String path = mCursor.getString(INDEX_URL);




                if (duration < 3000||duration>10000) {
                    continue;
                }

                if (!path.toUpperCase().endsWith(".MP4")) {
                    continue;
                }

                mGalleryModelList.add(new MediaModel(id,path, false));
            }


            mVideoAdapter = new GridViewAdapter(getActivity(), mGalleryModelList, true);
            mVideoAdapter.videoFragment = this;
            mVideoGridView.setAdapter(mVideoAdapter);
            mVideoGridView.setOnScrollListener(this);
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
        }


        mVideoGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                GridViewAdapter adapter = (GridViewAdapter) parent.getAdapter();
                MediaModel galleryModel = adapter.getItem(position);
                File file = new File(galleryModel.url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "video/*");
                startActivity(intent);
                return false;
            }
        });

        mVideoGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // update the mStatus of each category in the adapter
                GridViewAdapter adapter = (GridViewAdapter) parent.getAdapter();
                MediaModel galleryModel = adapter.getItem(position);

                if (!galleryModel.status) {
                    long size = MediaChooserConstants.ChekcMediaFileSize(new File(galleryModel.url.toString()), true);
                    if (size != 0) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.file_size_exceed) + "  " + MediaChooserConstants.SELECTED_VIDEO_SIZE_IN_MB + " " + getActivity().getResources().getString(R.string.mb), Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    if ((MediaChooserConstants.MAX_MEDIA_LIMIT == MediaChooserConstants.SELECTED_MEDIA_COUNT)) {
//                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.max_limit_file) + "  " + MediaChooserConstants.SELECTED_MEDIA_COUNT + " " + getActivity().getResources().getString(R.string.file), Toast.LENGTH_SHORT).show();
//                            return;
//                    }
                }

                // inverse the status
                galleryModel.status = !galleryModel.status;
                adapter.notifyDataSetChanged();

                if (galleryModel.status) {
                    mSelectedItems.add(galleryModel.url.toString());
//                    MediaChooserConstants.SELECTED_MEDIA_COUNT++;

                } else {
                    mSelectedItems.remove(galleryModel.url.toString().trim());
//                    MediaChooserConstants.SELECTED_MEDIA_COUNT--;
                }

                if (mCallback != null) {
                    mCallback.onVideoSelected(mSelectedItems.size());
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("list", mSelectedItems);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }

            }
        });

    }

    public GridViewAdapter getAdapter() {
        if (mVideoAdapter != null) {
            return mVideoAdapter;
        }
        return null;
    }

    public ArrayList<String> getSelectedVideoList() {
        return mSelectedItems;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (view == mVideoGridView) {
            // Set scrolling to true only if the user has flinged the
            // ListView away, hence we skip downloading a series
            // of unnecessary bitmaps that the user probably
            // just want to skip anyways. If we scroll slowly it
            // will still download bitmaps - that means
            // that the application won't wait for the user
            // to lift its finger off the screen in order to
            // download.
            if (scrollState == SCROLL_STATE_FLING) {
                //chk
            } else {
                mVideoAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }

    public void addNewEntry(String url) {
        if (mVideoAdapter != null) {
            mVideoAdapter.addLatestEntry(new MediaModel(-1,url, false));
        } else {
            if (mGalleryModelList == null) {
                mGalleryModelList = new ArrayList<MediaModel>();
            }
            mGalleryModelList.add(new MediaModel(-1,url, false));
            mVideoAdapter = new GridViewAdapter(getActivity(), mGalleryModelList, true);
        }
        Intent intent = new Intent(MediaChooserConstants.BRAODCAST_VIDEO_RECEIVER);
        intent.putExtra("value", url);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

    }

}

