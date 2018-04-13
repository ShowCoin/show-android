package com.samuel.mediachooser.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.samuel.mediachooser.MediaChooser;
import com.samuel.mediachooser.R;
import com.samuel.mediachooser.Utilities.MediaChooserConstants;
import com.samuel.mediachooser.adapter.ViewPagerAdapter;
import com.samuel.mediachooser.fragment.BucketImageFragment;
import com.samuel.mediachooser.fragment.BucketVideoFragment;
import com.samuel.mediachooser.fragment.ImageFragment;
import com.samuel.mediachooser.fragment.VideoFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ScaleTransitionPagerTitleView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.view.ToastUtils;



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


public class MediaChooserActivity extends BaseFragmentActivity implements ImageFragment.OnImageSelectedListener, VideoFragment.OnVideoSelectedListener {
    private ImageView mCameraVideo;
    private ImageView mBack;
    private TextView mDone;
    private TextView mTitle;
    private Context mContext;
    private static Uri fileUri;
    public ViewPager viewPager;
    private MagicIndicator magicIndicator;
    public final String IMAGE = "Image";
    public final String VIDEO = "Video";
    public final String IMAGE_BUCKET = "Image Folder";
    public final String VIDEO_BUCKET = "Video Folder";

    private int [] titleIds = new int[]{R.string.images_tab,R.string.videos_tab};


    private ArrayList<String> mSelectedVideo = new ArrayList<String>();
    private ArrayList<String> mSelectedImage = new ArrayList<String>();
    public boolean isBucket;
    private final Handler handler = new Handler();
    private ViewPagerAdapter mViewPagerAdapter;

    public static void startMediaChooser(Activity context, boolean isBucket) {
        Intent intent = new Intent(context, MediaChooserActivity.class);
        intent.putExtra("isBucket", isBucket);
        context.startActivityForResult(intent,MediaChooserConstants.SELECT_MEDIA_REQUEST_CODE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(null);
        initCommonWindow();

        magicIndicator = (MagicIndicator)findViewById(R.id.magic_indicator);
        viewPager = (ViewPager) findViewById(R.id.viewpager_MediaChooser);

        initView();

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Fragment fragment = mViewPagerAdapter.getItem(position);
//                if (fragment instanceof ImageFragment) {
//                    ImageFragment imageFragment = (ImageFragment) fragment;
//                    if (imageFragment != null) {
//                        mCameraVideo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.selector_camera_button));
//                        mCameraVideo.setTag(getResources().getString(R.string.image));
//
//                    }
//                } else if (fragment instanceof VideoFragment) {
//                    VideoFragment videoFragment = (VideoFragment) fragment;
//
//                    if (videoFragment != null) {
//                        mCameraVideo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.selector_video_button));
//                        mCameraVideo.setTag(getResources().getString(R.string.video));
//
//                    }
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        isBucket = getIntent().getBooleanExtra("isBucket", false);


        if (isBucket) {
            initMagicIndicator();
            mViewPagerAdapter.addFragment(new BucketImageFragment(), IMAGE_BUCKET);
            mViewPagerAdapter.addFragment(new BucketVideoFragment(), VIDEO_BUCKET);
        } else {

            if (getIntent() != null && (getIntent().getBooleanExtra("isFromBucket", false))) {

                magicIndicator.setVisibility(View.GONE);
                mTitle.setVisibility(View.VISIBLE);

                String name = getIntent().getStringExtra("name");
                String folderName = getIntent().getStringExtra("folderName");
                mTitle.setText(name);

                if (getIntent().getBooleanExtra("image", false)) {
//                    mCameraVideo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.selector_camera_button));
//                    mCameraVideo.setTag(getResources().getString(R.string.image));

                    Bundle bundle = new Bundle();
                    bundle.putString("name", folderName);
                    ImageFragment imageFragment = new ImageFragment();
                    imageFragment.setArguments(bundle);
                    mViewPagerAdapter.addFragment(imageFragment, IMAGE);

                } else {

//                    mCameraVideo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.selector_video_button));
//                    mCameraVideo.setTag(getResources().getString(R.string.video));

                    Bundle bundle = new Bundle();
                    bundle.putString("name", folderName);
                    VideoFragment videoFragment = new VideoFragment();
                    videoFragment.setArguments(bundle);
                    mViewPagerAdapter.addFragment(videoFragment, VIDEO);
                }
            } else {

                initMagicIndicator();

                if (MediaChooserConstants.showImage) {
//                    mCameraVideo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.selector_camera_button));
//                    mCameraVideo.setTag(getResources().getString(R.string.image));
                    mViewPagerAdapter.addFragment(new ImageFragment(), IMAGE);
                }


                if (MediaChooserConstants.showVideo) {
//                    mCameraVideo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.selector_video_button));
//                    mCameraVideo.setTag(getResources().getString(R.string.video));
                    mViewPagerAdapter.addFragment(new VideoFragment(), VIDEO);
                }


            }
        }


        viewPager.setAdapter(mViewPagerAdapter);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_home_screen_media_chooser;
    }


    private void initMagicIndicator() {
        magicIndicator.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.GONE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titleIds.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(titleIds[index]);
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setNormalColor(Color.parseColor("#6e6e6e"));
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(UIUtil.dip2px(context, 14));
                indicator.setYOffset(UIUtil.dip2px(context, 9));
                indicator.setXOffset(UIUtil.dip2px(context, 3));
                indicator.setLineHeight(UIUtil.dip2px(context, 1));
                indicator.setColors(Color.WHITE);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }


    private void initView() {
        mTitle = (TextView) findViewById(R.id.title_textView_toolbar_MediaChooser);
//        mCameraVideo = (ImageView) findViewById(R.id.camera_imageView_toolbar_MediaChooser);
        mBack = (ImageView) findViewById(R.id.backArrow_imageView_toolbar_MediaChooser);
//        mDone = (TextView) findViewById(R.id.done_textView_toolbar_MediaChooser);

        mBack.setOnClickListener(clickListener);
//        mCameraVideo.setOnClickListener(clickListener);
//        mDone.setOnClickListener(clickListener);

//        if (!MediaChooserConstants.showCameraVideo) {
//            mCameraVideo.setVisibility(View.GONE);
//        }

    }

    @Override
    public void onImageSelected(int count) {
        //TODO 未来版本可能一次上传多个文件，这里是选择文件数量回调
//        if (tabLayout.getTabAt(1) != null) {
//            if (count != 0) {
//                tabLayout.getTabAt(1).setText(getResources().getString(R.string.images_tab) + "  " + count);
//
//            } else {
//                tabLayout.getTabAt(1).setText(getResources().getString(R.string.image));
//            }
//        }
    }


    @Override
    public void onVideoSelected(int count) {
        //TODO 未来版本可能一次上传多个文件，这里是选择文件数量回调

//        if (count != 0) {
//            tabLayout.getTabAt(0).setText(getResources().getString(R.string.videos_tab) + "  " + count);
//
//        } else {
//            tabLayout.getTabAt(0).setText(getResources().getString(R.string.video));
//        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (view == mCameraVideo) {

                if (view.getTag() != null) {
                    if (view.getTag().toString().equals(getResources().getString(R.string.video))) {
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_VIDEO); // create a file to save the image
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                        // start the image capture Intent
                        startActivityForResult(intent, MediaChooserConstants.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_IMAGE); // create a file to save the image
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                        // start the image capture Intent
                        startActivityForResult(intent, MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                }
            } else if (view == mDone) {

                int count = mViewPagerAdapter.getCount();
                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        Fragment fragment = mViewPagerAdapter.getItem(i);
                        if (fragment instanceof ImageFragment) {
                            ImageFragment imageFragment = (ImageFragment) fragment;
                            if (imageFragment != null) {
                                if (imageFragment.getSelectedImageList() != null && imageFragment.getSelectedImageList().size() > 0) {
                                    Intent imageIntent = new Intent();
                                    imageIntent.setAction(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
                                    imageIntent.putStringArrayListExtra("list", imageFragment.getSelectedImageList());
                                    sendBroadcast(imageIntent);
                                }
                            }
                        } else if (fragment instanceof VideoFragment) {
                            VideoFragment videoFragment = (VideoFragment) fragment;

                            if (videoFragment != null) {
                                if (videoFragment.getSelectedVideoList() != null && videoFragment.getSelectedVideoList().size() > 0) {
                                    Intent videoIntent = new Intent();
                                    videoIntent.setAction(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
                                    videoIntent.putStringArrayListExtra("list", videoFragment.getSelectedVideoList());
                                    sendBroadcast(videoIntent);
                                }
                            }
                        }
                    }

                    finish();
                } else {
                    Toast.makeText(mContext, getString(R.string.please_select_file), Toast.LENGTH_SHORT).show();

                }

            } else if (view == mBack) {
                finish();
            }
        }
    };


    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), MediaChooserConstants.folderName);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MediaChooserConstants.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MediaChooserConstants.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == MediaChooserConstants.BUCKET_SELECT_IMAGE_CODE) {
                addMedia(mSelectedImage, data.getStringArrayListExtra("list"));

            } else if (requestCode == MediaChooserConstants.BUCKET_SELECT_VIDEO_CODE) {
                addMedia(mSelectedVideo, data.getStringArrayListExtra("list"));

            } else if (requestCode == MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
                final AlertDialog alertDialog = MediaChooserConstants.getDialog(mContext).create();
                alertDialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 2000ms
                        String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
                        Fragment fragment = mViewPagerAdapter.getItem(0);

                        if (fragment instanceof ImageFragment) {
                            ImageFragment imageFragment = (ImageFragment) fragment;

                            if (imageFragment != null) {
                                imageFragment.addNewEntry(fileUriString);
                            }
                        } else {

                            ImageFragment imageFragment = (ImageFragment) mViewPagerAdapter.getItem(1);

                            if (imageFragment != null) {
                                imageFragment.addNewEntry(fileUriString);
                            }
                        }

                        alertDialog.dismiss();
                    }
                }, 5000);

            } else if (requestCode == MediaChooserConstants.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));

                final AlertDialog alertDialog = MediaChooserConstants.getDialog(mContext).create();
                alertDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 2000ms
                        String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();

                        Fragment fragment = mViewPagerAdapter.getItem(0);
                        if (fragment instanceof VideoFragment) {
                            VideoFragment videoFragment = (VideoFragment) fragment;

                            if (videoFragment != null) {
                                videoFragment.addNewEntry(fileUriString);
                            }
                        } else {
                            VideoFragment videoFragment = (VideoFragment) mViewPagerAdapter.getItem(1);

                            if (videoFragment != null) {
                                videoFragment.addNewEntry(fileUriString);
                            }
                        }
                        alertDialog.dismiss();
                    }
                }, 5000);
            }
        }
    }

    private void addMedia(ArrayList<String> list, ArrayList<String> input) {
        //目前没有多选
//        for (String string : input) {
//            list.add(string);
//        }
        Intent intent = new Intent();
        intent.putExtra("filePath", input.get(0));
        setResult(Activity.RESULT_OK, intent);
        finish();

//        ToastUtils.showToast("选择上传的文件路径：>>>>"+input.get(0));

    }
}
