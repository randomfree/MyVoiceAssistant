package com.muzhihudong.myprojectlib.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.widget.ImageView;

import com.muzhihudong.myprojectlib.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class ImageUtils {

    public static DisplayImageOptions getDefaultImageLoaderOprations() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.logo)
                .showImageOnLoading(R.mipmap.logo)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnFail(R.mipmap.logo).cacheInMemory(true)
                .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        return options;
    }

    public static DisplayImageOptions getDefaultImageLoaderOprations(
            int default_img_resid) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(default_img_resid)
                .showImageOnLoading(default_img_resid)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnFail(default_img_resid).cacheInMemory(true)
                .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        return options;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (FileUtils.isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (FileUtils.isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (FileUtils.isMediaDocument(uri)) {
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
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
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


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqwidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqwidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqwidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;

        }
        LogUtils.writeLog("inSamleSize ===== " + inSampleSize);
        return inSampleSize;
    }


    /**
     * bitmap转base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmaptoString(Bitmap bitmap) {

        String string = null;

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();

        bitmap.compress(CompressFormat.PNG, 100, bStream);

        byte[] bytes = bStream.toByteArray();

        string = Base64.encodeToString(bytes, Base64.DEFAULT);

        return string;

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static File drawable2File(Drawable drawable, Context context) {
        return FileUtils.byte2File(Bitmap2Bytes(drawableToBitmap(drawable)), context);
    }

    public static String getNoRepeatFileName(String path, String name) {
        File file = new File(path);
        String[] indexStr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z"};
        Random random = new Random();
        String[] filenames = file.list();
        boolean b = false;
        for (int i = 0; i < filenames.length; i++) {
            if (filenames[i].equals(name)) {
                name += indexStr[random.nextInt(indexStr.length)];
                b = true;
            }
        }
        if (b) {
            return getNoRepeatFileName(path, name);
        } else {
            return name;
        }

    }


    /**
     * 获取尺寸裁剪后的图
     *
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inTempStorage = new byte[12 * 1024]; //临时存储
        options.inInputShareable = true;
//        return BitmapFactory.decodeFile(filePath, options);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(filePath);
            bitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 获取尺寸裁剪后的图
     *
     * @return
     */
    public static byte[] getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, KeyStringUtils.PIC_WIDTH, KeyStringUtils.PIC_HEIGHT);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inTempStorage = new byte[12 * 1024]; //临时存储
        options.inInputShareable = true;
//        return BitmapFactory.decodeFile(filePath, options);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(filePath);
            bitmap = BitmapFactory.decodeStream(is, null, options);
            int degree = readPictureDegree(filePath);
            bitmap = rotateBitmap(bitmap, degree);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Bitmap2Bytes(bitmap);
    }

    public static String getBase64data(Bitmap bitmap) {
        byte[] a = Bitmap2Bytes(bitmap);
        String str = Base64.encodeToString(a, Base64.DEFAULT);
        return str;
    }


    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }


    /** */
    /**
     * 文件转化为字节数组
     */
    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * @param uri
     * @param imageView
     * @param context
     * @param is_small  是否是缩略图
     */
    public static void setImageViewURIImage(Uri uri, ImageView imageView, Context context, boolean is_small) {
        File f = FileUtils.uri2file(uri, context);
        Bitmap bitmap;
        if (is_small) {
            bitmap = ImageUtils.getSmallBitmap(f.getAbsolutePath(), KeyStringUtils.PIC_WIDTH_SMALL, KeyStringUtils.PIC_WIDTH_SMALL);
        } else {
            bitmap = ImageUtils.getSmallBitmap(f.getAbsolutePath(), KeyStringUtils.PIC_WIDTH, KeyStringUtils.PIC_HEIGHT);
        }
        imageView.setImageBitmap(bitmap);
    }


    /**
     * 拍照 onactivityresult的时候调用
     *
     * @param data
     * @return
     */
    public static File getFileFromCamera(Intent data) {
        String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
        Bundle bundle = data.getExtras();
        Bitmap bitmap1 = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

        FileOutputStream b = null;
        File file = new File("/sdcard/668/");
        file.mkdirs();// 创建文件夹
        String fileName = "/sdcard/668/" + name;

        try {
            b = new FileOutputStream(fileName);
            bitmap1.compress(CompressFormat.PNG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new File(fileName);
    }


    public static void saveBitmapToFile(Context context, Bitmap bitmap, File file) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }


    public static int dip2px(Context context, float dpValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将有旋转角度的图片转回来
     *
     * @param context
     * @param mImageCaptureUri 图片uri
     * @return
     */
    public static Bitmap ratationImage(Context context, Uri mImageCaptureUri) {

        // 不管是拍照还是选择图片每张图片都有在数据中存储也存储有对应旋转角度orientation值
        // 所以我们在取出图片是把角度值取出以便能正确的显示图片,没有旋转时的效果观看

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(mImageCaptureUri, null, null, null, null);// 根据Uri从数据库中找
        if (cursor != null) {
            cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找指向第一个就是了
            String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路
            String orientation = cursor.getString(cursor
                    .getColumnIndex("orientation"));// 获取旋转的角度
            cursor.close();
            if (filePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);//根据Path读取资源图片
                int angle = 0;
                if (orientation != null && !"".equals(orientation)) {
                    angle = Integer.parseInt(orientation);
                }
                if (angle != 0) {
                    // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
                    Matrix m = new Matrix();
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    m.setRotate(angle); // 旋转angle度
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                            m, true);// 从新生成图片

                }
                return bitmap;
            }
        }
        return null;
    }

}
