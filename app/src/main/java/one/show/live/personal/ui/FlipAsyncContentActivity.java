package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.api.BaseRequest;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.AppUtil;
import one.show.live.widget.TitleView;


public class FlipAsyncContentActivity extends BaseFragmentActivity {


    private FlipViewController flipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.activity_title);

        flipView = new FlipViewController(this);
        flipView.setAdapter(new MyBaseAdapter(this, flipView));

        setContentView(flipView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flipView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flipView.onPause();
    }

    private static class MyBaseAdapter extends BaseAdapter {

        private FlipViewController controller;

        private Context context;

        private LayoutInflater inflater;

        private Bitmap placeholderBitmap;

        private MyBaseAdapter(Context context, FlipViewController controller) {
            inflater = LayoutInflater.from(context);
            this.context = context;
            this.controller = controller;

            //Use a system resource as the placeholder
            placeholderBitmap =
                    BitmapFactory.decodeResource(context.getResources(), android.R.drawable.dark_header);
        }

        @Override
        public int getCount() {
            return Travels.IMG_DESCRIPTIONS.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View layout = convertView;
            if (convertView == null) {
                layout = inflater.inflate(R.layout.complex1, null);
            }

            final Travels.Data data = Travels.IMG_DESCRIPTIONS.get(position);

            UI
                    .<TextView>findViewById(layout, R.id.title)
                    .setText(AphidLog.format("%d. %s", position, data.title));

            UI
                    .<TextView>findViewById(layout, R.id.description)
                    .setText(Html.fromHtml(data.description));

            UI
                    .<Button>findViewById(layout, R.id.wikipedia)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(data.link)
                            );
                            inflater.getContext().startActivity(intent);
                        }
                    });

            ImageView photoView = UI.findViewById(layout, R.id.photo);
            //Use an async task to load the bitmap
            boolean needReload = true;
            AsyncImageTask previousTask = AsyncDrawable.getTask(photoView);
            if (previousTask != null) {
                if (previousTask.getPageIndex() == position && previousTask.getImageName()
                        .equals(data.imageFilename)) //check if the convertView happens to be previously used
                {
                    needReload = false;
                } else {
                    previousTask.cancel(true);
                }
            }

            if (needReload) {
                AsyncImageTask
                        task =
                        new AsyncImageTask(layout.getContext().getAssets(), photoView, controller, position,
                                data.imageFilename);
                photoView
                        .setImageDrawable(new AsyncDrawable(context.getResources(), placeholderBitmap, task));

                task.execute();
            }

            return layout;
        }
    }

    /**
     * Borrowed from the official BitmapFun tutorial: http://developer.android.com/training/displaying-bitmaps/index.html
     */
    private static final class AsyncDrawable extends BitmapDrawable {

        private final WeakReference<AsyncImageTask> taskRef;

        public AsyncDrawable(Resources res, Bitmap bitmap, AsyncImageTask task) {
            super(res, bitmap);
            this.taskRef = new WeakReference<AsyncImageTask>(task);
        }

        public static AsyncImageTask getTask(ImageView imageView) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable) drawable).taskRef.get();
            }

            return null;
        }
    }

    private static final class AsyncImageTask extends AsyncTask<Void, Void, Bitmap> {

        private static final Random RANDOM = new Random();

        private final AssetManager assetManager;

        private final WeakReference<ImageView> imageViewRef;
        private final WeakReference<FlipViewController> controllerRef;
        private final int pageIndex;
        private final String imageName;

        public AsyncImageTask(AssetManager assetManager, ImageView imageView,
                              FlipViewController controller, int pageIndex, String imageName) {
            this.assetManager = assetManager;
            imageViewRef = new WeakReference<ImageView>(imageView);
            controllerRef = new WeakReference<FlipViewController>(controller);
            this.pageIndex = pageIndex;
            this.imageName = imageName;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public String getImageName() {
            return imageName;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                Thread.sleep(500 + RANDOM.nextInt(2000)); //wait for a random time
            } catch (InterruptedException e) {
            }

            return IO.readBitmap(assetManager, imageName);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                return;
            }

            ImageView imageView = imageViewRef.get();
            if (imageView != null && AsyncDrawable.getTask(imageView)
                    == this) { //the imageView can be reused for another page, so it's necessary to check its consistence
                imageView.setImageBitmap(bitmap);
                FlipViewController controller = controllerRef.get();
                if (controller != null) {
                    controller.refreshPage(pageIndex);
                }
            }
        }
}
}
