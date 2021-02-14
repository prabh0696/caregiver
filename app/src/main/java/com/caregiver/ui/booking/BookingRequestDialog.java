package com.caregiver.ui.booking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.caregiver.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class BookingRequestDialog extends Dialog {

    private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private Context mContext;
    private TextInputLayout input_from_date, input_to_date,
            input_from_time, input_to_time;
    private TextInputEditText from_date, to_date,
            from_time, to_time;

    private int fromDate, fromMonth, fromYear,
            toDate, toMonth, toYear;

    public BookingRequestDialog(Context context, String loggedUserId, String userId) {
        super(context);
        mContext = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.booking_request_dialog);
        getWindow().setBackgroundDrawableResource(R.drawable.round_rect_shape1);

        initializeComp();
    }

    private void initializeComp() {

        input_from_date = findViewById(R.id.input_from_date);
        from_date = findViewById(R.id.et_from_date);
        from_date.setInputType(InputType.TYPE_NULL);
        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(false);
            }
        });
        from_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(false);
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

        String hourStr = ""+hour;
        if(hour < 10){
            hourStr = "0"+hour;
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

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // and get that as a Date
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
                //DO SOMETHING
                if (isFromDate) {
                    if (dayOfMonth == toDate && monthOfYear == toMonth && year == toYear) {
                        //this is safe case
                    } else if (!isBeforeToDate(dayOfMonth, monthOfYear, year)) {
                        input_from_date.setError(mContext.getString(R.string.error_from_date));
                        return;
                    }
                } else if (!isFromDate) {
                    if (isBeforeFromDate(dayOfMonth, monthOfYear, year)) {
                        input_from_date.setError(mContext.getString(R.string.error_to_date));
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
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
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
            //message = message + "Start Date is Before Today" + "\n";
            result = true;
        } else if (startMonth < currentMonth && startYear <= currentYear) {
            //message = message + "Start Date is Before Today" + "\n";
            result = true;
        } else if (startDay < currentDay && startMonth <= currentMonth && startYear <= currentYear) {
            //message = message + "Start Date is Before Today" + "\n";
            result = true;
        }
        return result;
    }


    private long getDate(int date, int month, int year) {
        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // reuse the calendar to set user specified date
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, date);

        return c.getTimeInMillis();
    }

    private void openTimePicker(boolean isFrom) {
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
        }, hour, minute, false);//No 24 hour time
        if (isFrom) {
            mTimePicker.setTitle(mContext.getString(R.string.from_date) + " " + mContext.getString(R.string.time));
        } else {
            mTimePicker.setTitle(mContext.getString(R.string.to_date) + " " + mContext.getString(R.string.time));
        }

        mTimePicker.show();
    }

    private void submitClick() {

        input_from_date.setError(null);
        String fromDate = from_date.getText().toString().trim();
        if (TextUtils.isEmpty(fromDate)) {
            input_from_date.setError(mContext.getString(R.string.empty_from_date));
            return;
        }
        dismiss();
    }
}
