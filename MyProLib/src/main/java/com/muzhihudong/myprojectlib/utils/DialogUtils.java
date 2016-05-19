package com.muzhihudong.myprojectlib.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.muzhihudong.myprojectlib.R;


public class DialogUtils {

    private Context context;

    public DialogUtils(Context context) {
        this.context = context;
    }

    public Dialog getDefaultDialog(String title, String message, String oktext,
                                   DialogInterface.OnClickListener onclick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,
                R.style.MyDefaultDialog_style);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(context.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton(oktext, onclick);

        return builder.create();
    }

    public Dialog getDefaultDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,
                R.style.MyDefaultDialog_style);
        builder.setTitle(context.getString(R.string.prompt));
        builder.setMessage(message);
        builder.setNegativeButton(context.getString(R.string.ok),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public Dialog getDefaultDialog(String message, boolean isCancle, DialogInterface.OnClickListener okOnClickListener) {
        return getDefaultDialog(message, context.getString(R.string.ok), isCancle, okOnClickListener);
    }

    public Dialog getDefaultDialog(String message, String okText, boolean isCancle, DialogInterface.OnClickListener okOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,
                R.style.MyDefaultDialog_style);
        builder.setTitle(context.getString(R.string.prompt));
        builder.setMessage(message);
        if (isCancle) {
            builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        builder.setPositiveButton(context.getString(R.string.ok),
                okOnClickListener);
        return builder.create();
    }

    public Dialog getDefaultDialog(String message, String okText, boolean isCancle, String cancleText, DialogInterface.OnClickListener okOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,
                R.style.MyDefaultDialog_style);
        builder.setTitle(context.getString(R.string.prompt));
        builder.setMessage(message);
        if (isCancle) {
            builder.setNegativeButton(cancleText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        builder.setPositiveButton(okText,
                okOnClickListener);
        return builder.create();
    }

    public Dialog getDefaultDialog(String message,
                                   DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,
                R.style.MyDefaultDialog_style);
        builder.setTitle(context.getString(R.string.prompt));
        builder.setMessage(message);
        builder.setNegativeButton(context.getString(R.string.ok),
                onClickListener);
        return builder.create();
    }


    public Dialog getTakePhoneDialog(final Activity activity, String message, final String phoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDefaultDialog_style);
        builder.setTitle(activity.getString(R.string.prompt));
        builder.setMessage(message);
        builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri uri = Uri.parse("tel:" + phoneNumber);
                intent.setData(uri);
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    // TODO 需要测试
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                activity.startActivity(intent);
            }
        });
        builder.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        Dialog dialog = builder.create();
        return dialog;
    }


    public static Dialog initMastUpdataDialog(final Context context, final String url) {
        AlertDialog.Builder builder = getUpdataDialogBuilder(context);
        builder.setPositiveButton(context.getString(R.string.updata_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FileUtils.systemDownload(context, url);
                Updataing(context).show();
            }
        });
        Dialog MustUpdataDialog = builder.create();
        MustUpdataDialog.setCancelable(false);
        return MustUpdataDialog;
    }

    public static Dialog initChooseUpdataDialog(final Context context, final String url) {
        AlertDialog.Builder builder = getUpdataDialogBuilder(context);
        builder.setPositiveButton(context.getString(R.string.updata_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FileUtils.systemDownload(context, url);
            }
        });
        builder.setNegativeButton(context.getString(R.string.updata_not_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog notnowDialog = builder.create();
        return notnowDialog;
    }

    public static Dialog Updataing(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDefaultDialog_style);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle(context.getString(R.string.prompt));
        builder.setMessage(context.getString(R.string.updataing));
        builder.setCancelable(false);
        return builder.create();
    }

    private static AlertDialog.Builder getUpdataDialogBuilder(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDefaultDialog_style);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle(context.getString(R.string.prompt));
        builder.setMessage(context.getString(R.string.find_new_version));
        return builder;
    }


}
