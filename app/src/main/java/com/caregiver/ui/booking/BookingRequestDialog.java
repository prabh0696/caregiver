package com.caregiver.ui.booking;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.WebApi;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class BookingRequestDialog extends Dialog {

    private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private Context mContext;
    private TextInputLayout input_from_date, input_to_date,
            input_from_time, input_to_time;
    private TextInputEditText from_date, to_date,
            from_time, to_time,
            message;

    private int fromDate, fromMonth, fromYear,
            toDate, toMonth, toYear;

    private String userId = "";

    public BookingRequestDialog(Context context, String userId) {
        super(context);
        mContext = context;
        this.userId = userId;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.booking_request_dialog);
        getWindow().setBackgroundDrawableResource(R.drawable.round_rect_shape1);

        initializeComp();
    }

    private void initializeComp() {

        message = findViewById(R.id.et_message);

        input_from_date = findViewById(R.id.input_from_date);
        from_date = findViewById(R.id.et_from_date);
        from_date.setInputType(InputType.TYPE_NULL);
        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(true);
            }
        });
        from_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(true);
                }
            }
        });

        input_to_date = findViewById(R.id.input_to_date);
        to_date = findViewById(R.id.et_to_date1);
        to_date.setInputType(InputType.TYPE_NULL);
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(false);
            }
        });
        to_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(false);
                }
            }
        });

        input_from_time = findViewById(R.id.input_from_time);
        from_time = findViewById(R.id.et_from_time);
        from_time.setInputType(InputType.TYPE_NULL);
        from_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(true);
            }
        });
        from_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openTimePicker(true);
                }
            }
        });

        input_to_time = findViewById(R.id.input_to_time);
        to_time = findViewById(R.id.et_to_time);
        to_time.setInputType(InputType.TYPE_NULL);
        to_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(false);
            }
        });
        to_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openTimePicker(false);
                }
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitClick();
            }
        });

        setDateParam(true);
        setDateParam(false);

        setTimeParm(true);
        setTimeParm(false);
    }

    private void setTimeParm(boolean isFrom) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int amPm = c.get(Calendar.AM_PM);

        String hourStr = "" + hour;
        if (hour < 10) {
            hourStr = "0" + hour;
        }
        String minStr = "" + minute;
        if (minute < 10) {
            minStr = "0" + minute;
        }
        String amPmStr = "AM";
        if (amPm == Calendar.PM) {
            amPmStr = "PM";
        }
        String timeStr = hourStr + ":" + minStr + " " + amPmStr;
        if (isFrom) {
            from_time.setText(timeStr);
        } else {
            to_time.setText(timeStr);
        }
    }

    private void setDateParam(boolean isFrom) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        if (isFrom) {
            fromDate = c.get(Calendar.DAY_OF_MONTH);
            fromMonth = c.get(Calendar.MONTH);
            fromYear = c.get(Calendar.YEAR);

            String dateStr = "" + fromDate;
            if (fromDate < 10) {
                dateStr = "0" + fromDate;
            }
            from_date.setText(months[fromMonth] + " " + dateStr + ", " + fromYear);
        } else {
            toDate = c.get(Calendar.DAY_OF_MONTH);
            toMonth = c.get(Calendar.MONTH);
            toYear = c.get(Calendar.YEAR);

            String dateStr = "" + toDate;
            if (fromDate < 10) {
                dateStr = "0" + toDate;
            }
            to_date.setText(months[toMonth] + " " + dateStr + ", " + toYear);
        }

    }


    private void showDatePicker(final boolean isFromDate) {

        input_from_date.setError(null);
        input_to_date.setError(null);

        int resourceID = R.string.from_date;
        int yyyy = fromYear, dd = fromDate, mm = fromMonth;
        if (!isFromDate) {
            resourceID = R.string.to_date;
            yyyy = toYear;
            dd = toDate;
            mm = toMonth;
        }
        DatePickerDialog dialog = new DatePickerDialog(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                if (isFromDate) {
                    if (dayOfMonth == toDate && monthOfYear == toMonth && year == toYear) {

                    } else if (!isBeforeToDate(dayOfMonth, monthOfYear, year)) {
                        input_from_date.setError(mContext.getString(R.string.error_from_date));
                        return;
                    }
                } else if (!isFromDate) {
                    if (isBeforeFromDate(dayOfMonth, monthOfYear, year)) {
                        input_to_date.setError(mContext.getString(R.string.error_to_date));
                        return;
                    }
                }

                String dateStr = "" + dayOfMonth;
                if (dayOfMonth < 10) {
                    dateStr = "0" + dayOfMonth;
                }

                if (isFromDate) {
                    from_date.setText(months[monthOfYear] + " " + dateStr + ", " + year);
                    fromDate = dayOfMonth;
                    fromMonth = monthOfYear;
                    fromYear = year;
                    long fromDateObj = getDate(fromDate, fromMonth, fromYear);
                    long toDateObj = getDate(dayOfMonth, monthOfYear, year);
                    if (toDateObj < fromDateObj) {
                        showDatePicker(false);
                    }
                } else {
                    to_date.setText(months[monthOfYear] + " " + dateStr + ", " + year);
                    toDate = dayOfMonth;
                    toMonth = monthOfYear;
                    toYear = year;
                }
            }
        }, yyyy, mm, dd);
        dialog.setTitle(mContext.getString(resourceID) + " " + mContext.getString(R.string.date));
        dialog.show();
    }

    private boolean isBeforeToDate(int startDay, int startMonth, int startYear) {
        boolean result = false;
        int currentYear = toYear;
        int currentMonth = toMonth;
        int currentDay = toDate;

        if (startYear < currentYear) {
            result = true;
        } else if (startMonth < currentMonth && startYear <= currentYear) {
            result = true;
        } else if (startDay < currentDay && startMonth <= currentMonth && startYear <= currentYear) {
            result = true;
        }
        return result;
    }

    private boolean isBeforeFromDate(int startDay, int startMonth, int startYear) {
        boolean result = false;
        int currentYear = fromYear;
        int currentMonth = fromMonth;
        int currentDay = fromDate;

        if (startYear < currentYear) {
            result = true;
        } else if (startMonth < currentMonth && startYear <= currentYear) {
            result = true;
        } else if (startDay < currentDay && startMonth <= currentMonth && startYear <= currentYear) {
            result = true;
        }
        return result;
    }


    private long getDate(int date, int month, int year) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, date);

        return c.getTimeInMillis();
    }

    private void openTimePicker(boolean isFrom) {

        input_from_time.setError(null);
        input_to_time.setError(null);

        TimePickerDialog mTimePicker;

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, selectedHour);
                c.set(Calendar.MINUTE, selectedMinute);

                Date date = new Date(c.getTimeInMillis());
                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                String formattedTime = fmtOut.format(date);
                if (isFrom) {
                    from_time.setText(formattedTime);
                } else {
                    to_time.setText(formattedTime);
                }

            }
        }, hour, minute, false);
        if (isFrom) {
            mTimePicker.setTitle(mContext.getString(R.string.from_date) + " " + mContext.getString(R.string.time));
        } else {
            mTimePicker.setTitle(mContext.getString(R.string.to_date) + " " + mContext.getString(R.string.time));
        }

        mTimePicker.show();
    }

    private void submitClick() {

        input_to_date.setError(null);
        input_from_date.setError(null);
        String fromDate1 = from_date.getText().toString().trim();
        if (TextUtils.isEmpty(fromDate1)) {
            input_from_date.setError(mContext.getString(R.string.empty_from_date));
            return;
        }
        String toDate1 = to_date.getText().toString().trim();
        if (TextUtils.isEmpty(toDate1)) {
            input_to_date.setError(mContext.getString(R.string.empty_to_date));
            return;
        }

        String fromTime = from_time.getText().toString().trim();
        if (TextUtils.isEmpty(fromTime)) {
            input_from_time.setError(mContext.getString(R.string.empty_from_time));
            return;
        }

        String toTime = to_time.getText().toString().trim();
        if (TextUtils.isEmpty(toTime)) {
            input_to_time.setError(mContext.getString(R.string.empty_to_time));
            return;
        }
        String fDate = fromYear+"-"+formatValue(fromMonth)+"-"+formatValue(fromDate);
        String tDate = toYear+"-"+formatValue(toMonth)+"-"+formatValue(toDate);


        submitData(fDate,
                tDate,
                fromTime,
                toTime,
                message.getText().toString().trim());
    }

    private String formatValue(int value){
        String str = ""+value;
        if(value < 10){
            str = "0"+value;
        }
        return str;
    }

    private void submitData(String fromDate,
                            String toDate,
                            String fromTime,
                            String toTime,
                            String bookingMessage) {

        WebApi.showLoadingDialog(mContext);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "from_user_id=" + Constants.loginUser.id
                + "&to_user_id=" + userId
                + "&from_date="+fromDate
                +"&to_date="+toDate
                +"&from_time="+fromTime
                +"&to_time="+toTime
                +"&booking_message="+bookingMessage);
        Request request = new Request.Builder()
                .url(WebApi.ADD_BOOKING)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Constants.TAG, "Login::onFailure::Exception: " + e);
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WebApi.dismissLoadingDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                WebApi.dismissLoadingDialog();
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.BOOKING_ADDED_ACTION));
                        dismiss();
                        WebApi.showLongToast(mContext, mContext.getString(R.string.booking_req_sent));
                    }
                });
            }
        });

    }
}
