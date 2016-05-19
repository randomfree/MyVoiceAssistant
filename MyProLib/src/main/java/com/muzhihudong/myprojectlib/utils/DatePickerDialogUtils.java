package com.muzhihudong.myprojectlib.utils;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.widget.DatePicker;


import com.muzhihudong.myprojectlib.R;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDialogUtils {

    private Context context;

    public DatePickerDialogUtils(Context context) {
        this.context = context;

    }

    public DatePickerDialog getNowTimeDatePickerDialog(
            OnDateSetListener onDateSetListener) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(context,
                R.style.MyDatePickerDialog_style, onDateSetListener, year,
                monthOfYear, dayOfMonth);
        return dialog;
    }

    public DatePickerDialog getDatePickerDialog(int year, int month, int day,
                                                OnDateSetListener onDateSetListener) {
        DatePickerDialog dialog = new DatePickerDialog(context,
                R.style.MyDatePickerDialog_style, onDateSetListener, year,
                month, day);
        return dialog;
    }

    public DatePickerDialog getDatePickerDialog(long startdate, long enddate,
                                                OnDateSetListener onDateSetListener) {
        DatePickerDialog dialog = getNowTimeDatePickerDialog(onDateSetListener);
        DatePicker datePicker = dialog.getDatePicker();
        if (startdate != 0) {
            datePicker.setMinDate(startdate - 1000);
        }
        if (enddate != 0) {
            datePicker.setMaxDate(enddate - 1000);
        }
        return dialog;
    }

}
