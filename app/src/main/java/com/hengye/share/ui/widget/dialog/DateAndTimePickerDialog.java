package com.hengye.share.ui.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.hengye.share.R;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.ResUtil;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by yuhy on 2016/9/23.
 */

public class DateAndTimePickerDialog extends AlertDialog {

    public DateAndTimePickerDialog(@NonNull Context context) {
        super(context);
        init(System.currentTimeMillis());
    }

    public DateAndTimePickerDialog(@NonNull Context context, long timeInMillis) {
        super(context);
        init(timeInMillis);
    }

    public DateAndTimePickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(System.currentTimeMillis());
    }

    public DateAndTimePickerDialog(@NonNull Context context, @StyleRes int themeResId, long timeInMillis) {
        super(context, themeResId);
        init(timeInMillis);
    }

    DatePickerDialog mDatePickerDialog;
    TimePickerDialog mTimePickerDialog;
    OnSetListener mOnSetListener;
    onTimeUpdateListener mOnTimeUpdateListener;
    TextView mDate, mTime;
    Calendar mCalendar;


    public void init(long timeInMillis) {

        mCalendar = Calendar.getInstance();
        View contentView = View.inflate(getContext(), R.layout.content_edit_time, null);
        mDate = (TextView) contentView.findViewById(R.id.tv_date);
        mTime = (TextView) contentView.findViewById(R.id.tv_time);

        View.OnClickListener timeSelectListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置日期
                if (v.getId() == R.id.tv_date) {
                    showDatePickerDialog();
                }
                // 设置时间
                else if (v.getId() == R.id.tv_time) {
                    showTimePickerDialog();
                }
            }
        };

        mDate.setOnClickListener(timeSelectListener);
        mTime.setOnClickListener(timeSelectListener);
        setTitle(R.string.label_publish_on_time);
        setView(contentView);
        setButton(DialogInterface.BUTTON_NEGATIVE, ResUtil.getString(R.string.dialog_text_cancel), (OnClickListener) null);
        setButton(DialogInterface.BUTTON_POSITIVE, ResUtil.getString(R.string.dialog_text_confirm), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOnSetListener != null) {
                    mOnSetListener.onSet(getCalendar(), getTimeInMillis());
                }
            }
        });

        setTime(timeInMillis);
    }

    private void updateDateAndTime() {
        updateDate();
        updateTime();
    }

    private void updateDate() {
        mDate.setText(DateUtil.formatDate(mCalendar.getTimeInMillis(), ResUtil.getString(R.string.date_format_md)));
    }

    private void updateTime() {
        mTime.setText(DateUtil.formatDate(mCalendar.getTimeInMillis(), ResUtil.getString(R.string.date_format_hm)));
    }

    private void showDatePickerDialog(){
        getDatePickerDialog().show(getActivity().getFragmentManager(), "DatePickerDialog");
    }

    private void showTimePickerDialog(){
        //TimerPickerDialog没有保留上一次选择的时间
        getTimePickerDialog().setStartTime(mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE));
        getTimePickerDialog().show(getActivity().getFragmentManager(), "TimePickerDialog");
    }

    private Activity getActivity(){
        if(getContext() instanceof ContextWrapper){
            return ((Activity)((ContextWrapper)getContext()).getBaseContext());
        }else{
            return ((Activity)getContext());
        }
    }

    public DatePickerDialog getDatePickerDialog() {
        if (mDatePickerDialog == null) {
            mDatePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {

                    if(mOnTimeUpdateListener != null){
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(mCalendar.getTimeInMillis());
                        calendar.set(year, monthOfYear, dayOfMonth);
                        if(!mOnTimeUpdateListener.onTimeUpdate(true, calendar.getTimeInMillis())){
                            return;
                        }
                    }

                    mCalendar.set(year, monthOfYear, dayOfMonth);
                    updateDate();
                    if (mTimePickerDialog == null) {
                        //如果没有设置过时间, 弹出时间选择器
                        showTimePickerDialog();
                    }
                }
            }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));


            mDatePickerDialog.setVibrate(false);
            mDatePickerDialog.setYearRange(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.YEAR));
            mDatePickerDialog.setCloseOnSingleTapDay(false);

//            Android提供的DatePicker
//            mDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                    mCalendar.set(year, monthOfYear, dayOfMonth);
//                    updateDate();
//                    if(mTimePickerDialog == null) {
//                        //如果没有设置过时间, 弹出时间选择器
//                        getTimePickerDialog().show();
//                    }
//                }
//            }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        }
        return mDatePickerDialog;
    }

    public TimePickerDialog getTimePickerDialog() {
        if (mTimePickerDialog == null) {
            mTimePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

                    if(mOnTimeUpdateListener != null){
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(mCalendar.getTimeInMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        if(!mOnTimeUpdateListener.onTimeUpdate(false, calendar.getTimeInMillis())){
                            return;
                        }
                    }

                    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mCalendar.set(Calendar.MINUTE, minute);
                    updateTime();
                }
            }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);

            mTimePickerDialog.setVibrate(false);
            mTimePickerDialog.setStartTime(mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE));
            mTimePickerDialog.setCloseOnSingleTapMinute(false);

//            Android提供的TimePicker
//            mTimePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                    mCalendar.set(Calendar.MINUTE, minute);
//                    updateTime();
//                }
//            }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);
        }
        return mTimePickerDialog;
    }

    public void setTime(long timeInMillis) {
        mCalendar.setTimeInMillis(timeInMillis);
        updateDateAndTime();
    }

    public long getTimeInMillis() {
        return mCalendar.getTimeInMillis();
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

    public OnSetListener getOnSetListener() {
        return mOnSetListener;
    }

    /**
     * 当确定时间后会调用;
     * @param onSetListener
     */
    public void setOnSetListener(OnSetListener onSetListener) {
        this.mOnSetListener = onSetListener;
    }

    public DateAndTimePickerDialog.onTimeUpdateListener getOnTimeUpdateListener() {
        return mOnTimeUpdateListener;
    }

    /**
     * 设置当日期和时间改变的时候, 设置选择拦截的监听器;
     * @param onTimeUpdateListener
     */
    public void setOnTimeUpdateListener(DateAndTimePickerDialog.onTimeUpdateListener onTimeUpdateListener) {
        this.mOnTimeUpdateListener = onTimeUpdateListener;
    }

    public interface OnSetListener {
        void onSet(Calendar calendar, long timeInMillis);
    }

    public interface onTimeUpdateListener{
        /**
         * 如果返回true,则不拦截所选择的时间
         * @param timeInMillis 选择的时间
         * @param isSelectDate 是否是选择日期
         * @return
         */
        boolean onTimeUpdate(boolean isSelectDate, long timeInMillis);
    }
}
