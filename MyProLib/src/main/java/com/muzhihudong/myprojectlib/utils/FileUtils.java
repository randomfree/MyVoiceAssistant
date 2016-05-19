package com.muzhihudong.myprojectlib.utils;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sonic on 2016/4/11.
 */
public class FileUtils {

    public static File byte2File(byte[] buf, Context context) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        String filePath = context.getCacheDir().getAbsolutePath();
        String fileName = ImageUtils.getNoRepeatFileName(context
                .getCacheDir().getAbsolutePath(), "a")
                + ".jpg";
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file;
    }

    public static File uri2file(Uri uri, Context context) {
        String img_path = getPath(context, uri);
        return new File(img_path);

    }

    public static void writeFileToCache(Context context, String fileName,
                                        String data) {
        try {
            String path = getCacheDir(context) + "\\"
                    + StringUtils.GetMD5(fileName) + ".txt";
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                boolean mk = file.mkdirs();
                LogUtils.writeLog("createsucceed11111" + mk);
            }
            if (!file.exists()) {
                boolean mf = file.createNewFile();
                LogUtils.writeLog("createsucceed2222" + mf);

            }
            FileOutputStream fos = new FileOutputStream(file);
            StringBuffer sb = new StringBuffer(data);
            fos.write(sb.toString().getBytes("utf-8"));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCacheDir(Context context) {
        return context.getExternalCacheDir().getAbsolutePath() + "/lylogs";
    }

    public static String getSDDir() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/668pics";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static long systemDownload(Context context, String url) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalFilesDir(context, null, "liuliuba.apk");
        long id = dm.enqueue(request);
        return id;
    }

    public static String getRunningActivityName(Context context) {
        String contextString = context.toString();
        String name = contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
        LogUtils.writeLog("getRunningActivityName__" + name);
        return name;
    }

    /**
     * 先将用户ID进行MD5
     * "/upload/"+MD5截取前两位+"/"+截取2-4位+"/"+截取4-6位+"/"+时间戳+文件下标+5位随机数+"."+文件后缀名
     *
     * @param file
     * @return
     */
    public static String getUploadImgName(String id, File file, int index) {
        String id_md5 = StringUtils.GetMD5(id);
        String url_2 = id_md5.substring(0, 2);
        String url_2_4 = id_md5.substring(2, 4);
        String url_4_6 = id_md5.substring(4, 6);
        String times = (System.currentTimeMillis() / 1000) + "";
        String random = ((int) ((Math.random() * 9 + 1) * 10000)) + "";
        String fileName;
        if(file != null)
        {
            fileName = file.getName();
        }
       else
        {
            fileName = "668.jpg";
        }
        String file_type = fileName.substring(fileName.lastIndexOf(".") + 1);

        String url = "upload/" + url_2 + "/" + url_2_4 + "/" + url_4_6 + "/" + times + index + random + "." + file_type;
        return url;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
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
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return uri.toString();
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
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

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
