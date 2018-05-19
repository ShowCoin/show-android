package one.show.live.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;


/**
 * Created by clarkM1ss1on on 2018/5/19
 */
public class UriUtils {

    public static String contentUriToPath(Context ctx, Uri uri) {
        String imagePath = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(ctx, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = contentUriToPathBeforeKitKat(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            , selection, ctx.getContentResolver());
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(docId));
                    imagePath = contentUriToPathBeforeKitKat(contentUri, null, ctx.getContentResolver());
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = contentUriToPathBeforeKitKat(uri, null, ctx.getContentResolver());
            }
            return imagePath;
        } else {
            return contentUriToPathBeforeKitKat(uri, null, ctx.getContentResolver());
        }
    }


    private static String contentUriToPathBeforeKitKat(Uri uri, String selection, ContentResolver contentResolver) {
        String path = null;
        Cursor cursor = contentResolver
                .query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
