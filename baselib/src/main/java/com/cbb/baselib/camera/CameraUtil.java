package com.cbb.baselib.camera;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.cbb.baselib.util.BitmapUtil;
import com.cbb.baselib.util.ScreenUtil;

import java.io.File;
import java.io.IOException;

import static com.cbb.baselib.util.BitmapUtil.readPictureDegree;

/**
 * Created by 坎坎.
 * Date: 2019/12/26
 * Time: 14:36
 * describe:
 */
public class CameraUtil {
    private static final int REQUEST_CODE_PIC_PHOTO = 1001;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1002;
    private static final int REQUEST_CODE_PHOTO_CROP = 1003;
    private static SelectCallBack callBack;

    public static void setCallBack(SelectCallBack selectCallBack) {
        callBack = selectCallBack;
    }

    public static void destroyCallBack() {
        callBack = null;
    }

    public static void openAlbum(Activity context) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        context.startActivityForResult(intent, REQUEST_CODE_PIC_PHOTO);
    }

    static File takePhotoTempFile;

    public static void openCamera(Activity context) {
        //如果没有设置文件夹，拍照后则保存在sdk根目录的Pictures文件夹下面
        File directory = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
        //如果没有设置图片名称，则根据当前系统时间设置默认的图片名称
        String photoPathName = System.currentTimeMillis() / 1000 + ".jpg";
        takePhotoTempFile = new File(directory, photoPathName);
        String takePhotoTempFilePath = takePhotoTempFile.getPath();
        //如果保存图片的文件夹还没有创建，则创建文件夹
        File dir = new File(takePhotoTempFilePath.substring(0, takePhotoTempFilePath.lastIndexOf(File.separator)));
        if (!dir.exists())
            dir.mkdirs();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, "com.cbb.wmt_eoc.fileprovider", takePhotoTempFile));
        context.startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PHOTO);
    }


    public static void onActivityResult(Activity context, int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_CODE_PIC_PHOTO://手机相册
                Uri uri = data.getData();
                if (callBack != null) {
                    if (uri != null) {
                        String path = getPath(context, uri);
                        if (path != null) {
                            callBack.success(resizeBitmapFile(path));
                        } else {
                            callBack.fail();
                        }
                    } else {
                        callBack.fail();
                    }
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO://拍照
                if (callBack != null) {
                    if (takePhotoTempFile.length() != 0) {
                        String path = takePhotoTempFile.getAbsolutePath();
                        if (path != null) {
                            callBack.success(resizeBitmapFile( path));
                        } else {
                            callBack.fail();
                        }
                    } else {
                        callBack.fail();
                    }
                }
                break;
            case REQUEST_CODE_PHOTO_CROP://裁剪
                break;
        }
    }

    private static int width = 720;

    private static String resizeBitmapFile(String path) {
        int ii = readPictureDegree(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int sample = options.outWidth / width;
        if (sample > 1) {
            options.inSampleSize = sample;
        }
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        Bitmap bit2 = BitmapFactory.decodeFile(path, options);
        Bitmap bitmap  = bit2;
        if(ii != 0){
             bitmap = BitmapUtil.rotateBitmapByDegree(bit2, ii);
        }
        long l = System.currentTimeMillis() / 1000;
        File file = BitmapUtil.saveBitmap(bitmap, l + ".jpg");
        return file.getAbsolutePath();
    }


    public static String getPath(final Context context, final Uri uri) {
        String pathHead = "file:///";
        // 1. DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 1.1 ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return pathHead + Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // 1.2 DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.
                        withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return pathHead + getDataColumn(context,
                        contentUri, null, null);
            }
            // 1.3 MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return pathHead + getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // 2. MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getFilePath_below19(context, uri);
        }
        // 3. 判断是否是文件形式 File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return pathHead + uri.getPath();
        }
        return null;
    }

    public static String getFilePath_below19(Context context, Uri uri) {
        //这里开始的第二部分，获取图片的路径：低版本的是没问题的，但是sdk>19会获取不到
        Cursor cursor = null;
        String path = "";
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            //好像是android多媒体数据库的封装接口，具体的看Android文档
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            //获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            //最后根据索引值获取图片路径   结果类似：/mnt/sdcard/DCIM/Camera/IMG_20151124_013332.jpg
            path = cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }

    public interface SelectCallBack {
        void success(String path);

        void fail();
    }
}
