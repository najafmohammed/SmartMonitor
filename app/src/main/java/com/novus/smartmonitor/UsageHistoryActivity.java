package com.novus.smartmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import android.widget.TimePicker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import android.os.Vibrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class UsageHistoryActivity extends AppCompatActivity {
    ArrayList<JSONArray> DayReading = new ArrayList<>();
    ArrayList<Integer> DayReading2 = new ArrayList<Integer>();
    ArrayList[][] dayvalues = new ArrayList[24][31];
    private Button choosedate,choosetime,showGraph;
    private TextView status,selectedDate,selectedTime,selectedFilter;
    private ChipGroup dateParameter;
    private int mYear,mMonth,mDay,mMinute,mHour;
    private Calendar c;
    private boolean dateSelectedFlag=false,timeSelectedFlag=false;
    private Context ctx = this;
    private Vibrator hapticFeedback;
    private int FlagfilterID=1;
    private LineGraphSeries <DataPoint> power_measured =new LineGraphSeries<>(new DataPoint[]{new DataPoint(0,0)});
    private DataPoint[] generateData(){
        DataPoint[] values =new DataPoint[]{new DataPoint(0,0)};
        return values;
    }
    private void reset(){
        final GraphView graph = findViewById(R.id.history_graph);
        power_measured.resetData(generateData());
        graph.addSeries(power_measured);
    }
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_history);
        hapticFeedback=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        choosedate=findViewById(R.id.choosedate);
        status=findViewById(R.id.status_usgae);
        dateParameter=findViewById(R.id.Date_parameter);
        choosetime=findViewById(R.id.choosetime);
        choosetime.setBackgroundColor(Color.GRAY);
        showGraph=findViewById(R.id.showGraph);
        showGraph.setBackgroundColor(Color.GRAY);
        choosedate.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mYear= Calendar.getInstance().get(Calendar.YEAR);
        mMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;
        mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ;
        mMinute = Calendar.getInstance().get(Calendar.MINUTE);
        selectedDate=findViewById(R.id.selectedDate);
        selectedTime=findViewById(R.id.selectedTime);
        selectedFilter=findViewById(R.id.selectedFilter);
        seekBar=findViewById(R.id.seekBar);
        startup_aniamtion();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                status.setText("seekbar progress: "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        showGraph.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                hapticFeedback.vibrate(50);
                if(!dateSelectedFlag){
                    Snackbar snackbar = Snackbar
                            .make(view ,"Date has not been selected", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.BLACK)
                            .setTextColor(Color.WHITE)
                            .setAction("Choose Date", new View.OnClickListener() {
                                public void onClick(View view) {
                                    show_Datepicker();
                                }
                            });
                    snackbar.show();
                }
                else if(!timeSelectedFlag){
                    Snackbar snackbar = Snackbar
                            .make(view ,"time has not been selected", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.BLACK)
                            .setTextColor(Color.WHITE)
                            .setAction("Choose Time", new View.OnClickListener() {
                                public void onClick(View view) {
                                    show_Timepicker();
                                }
                            });
                    snackbar.show();
                }
                else {
                    snackbarMessage("Loading graph Data",view);
                    open(view);
                    seekbarControl();
                    ShowGenData();
                }
            }
        });

        choosetime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                hapticFeedback.vibrate(50);
                if(dateSelectedFlag) {
                    show_Timepicker();
                }
                else{
                    Snackbar snackbar = Snackbar
                            .make(view ,"select date before choosing time", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.BLACK)
                            .setTextColor(Color.WHITE)
                            .setAction("Choose Date", new View.OnClickListener() {
                                public void onClick(View view) {
                                    show_Datepicker();
                                }
                            });
                    snackbar.show();
                }
            }
        });
        choosedate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                hapticFeedback.vibrate(50);
                show_Datepicker();

            }
        });
        dateParameter.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                hapticFeedback.vibrate(50);
                Chip chip=dateParameter.findViewById(checkedId);
                if(chip!=null){
                    String chipname= (String) chip.getText();
                    status.setText(chipname);
                    switch (chipname){
                        case "Minute":{
                        selectedFilter.setText("Minute");
                            FlagfilterID=1;
                        break;}
                        case "Hour":{
                            selectedFilter.setText("Hour");
                            FlagfilterID=2;
                            break;}
                        case "Day":{
                            selectedFilter.setText("Day");
                            FlagfilterID=3;
                            break;}
                        case "Week":{
                            selectedFilter.setText("Week");
                            FlagfilterID=4;
                            break;}
                        case "Month":{
                            selectedFilter.setText("Month");
                            FlagfilterID=5;
                            break;}
                        case "Year":{
                            selectedFilter.setText("Year");
                            FlagfilterID=6;
                            break;}
                    }
                }
            }
        });

        final GraphView graph = findViewById(R.id.history_graph);
        graph.getViewport().setScalable(false);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalableY(false);
        graph.setTitle("Usage");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Power");
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(5);
        graph.getViewport().setMaxX(10);
        graph.getGridLabelRenderer().setNumHorizontalLabels(6);
        graph.getGridLabelRenderer().setNumVerticalLabels(4);


        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value,boolean isValueX){
                if (isValueX){
                    return super.formatLabel(value,isValueX);
                }
                else{
                    return super.formatLabel(value,isValueX)+"Kwh";
                }
            }
        });


    }

    private void startup_aniamtion() {
        int[] set1 ={R.id.minute_chip,R.id.hour_chip,R.id.day_chip,R.id.week_chip,R.id.month_chip,R.id.year_chip};
        int[] set2={R.id.history_graph,R.id.choosedate,R.id.choosetime,R.id.showGraph};
        int i = 0;
        for(int id:set1){
            Chip chip=findViewById(id);
            Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
            slide.setStartOffset(i * 100);
            chip.startAnimation(slide);
            i++;
        }
        for(int id:set2){
            if(id==R.id.history_graph){
                GraphView graphView=findViewById(id);
                Animation slide = AnimationUtils.loadAnimation(this, R.anim.slidedown);
                slide.setStartOffset(i+2 * 100);
                graphView.startAnimation(slide);
                i++;
            }
            else{
                Button button=findViewById(id);
                Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_from_bottom);
                slide.setStartOffset(i * 100);
                button.startAnimation(slide);
                i++;
            }
        }

    }

    private void show_Datepicker() {
        c = Calendar.getInstance();
        int mYearParam = mYear;
        int mMonthParam = mMonth-1;
        int mDayParam = mDay;

        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        hapticFeedback.vibrate(50);
                        mMonth = monthOfYear + 1;
                        mYear=year;
                        mDay=dayOfMonth;
                        dateSelectedFlag=true;
                        selectedDate.setText(mDay+"/"+mMonth+"/"+mYear);
                        choosetime.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }, mYearParam, mMonthParam, mDayParam);

        datePickerDialog.show();
    }

    private void show_Timepicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {

                        mHour = pHour;
                        hapticFeedback.vibrate(50);
                        mMinute = pMinute;
                        selectedTime.setText(mHour+":"+mMinute);
                        showGraph.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        timeSelectedFlag=true;
                    }
                }, mHour, mMinute, true);

        timePickerDialog.show();
    }
    public String loadJSONFromAsset() {
        String json = null;
        String fileName="username-"+mMonth+mYear+".json";

        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void notifyUser(String message) {
        hapticFeedback.vibrate(50);

        Toast.makeText(UsageHistoryActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }

    private void snackbarMessage(String message,View view){
        final Snackbar snackbar = Snackbar
                .make(view ,message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.BLACK)
                .setTextColor(Color.WHITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
        snackbar.show();
    }
    private void ShowGenData(){
        reset();
        switch (FlagfilterID){
            case 1:{
                notifyUser("minute");
                minuteFilter();
                break;
            }
            case 2:{
                notifyUser("hour");
                hourFilter();
                break;
            }
            case 3:{
                notifyUser("day");
                dayFilter();
                break;
            }
            case 4:{
                notifyUser("week");
                weekFilter();
                break;
            }
            case 5:{
                notifyUser("month");
                monthFilter();
                break;
            }
            case 6:{
                notifyUser("year");
                yearFilter();
                break;
            }
        }
    }

    private void yearFilter() {
    }

    private void monthFilter() {
    }

    private void weekFilter() {
    }

    private void dayFilter() {
        JSONArray values = null;
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            values = obj.getJSONArray("values");
            int day = 0;
            for (int i = 0; i < values.length(); i++) {
                JSONObject dayValue = values.getJSONObject(i);
                day = dayValue.getInt("dayTotal");
                DayReading2.add(day);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("ssds", String.valueOf(DayReading2.size()));

    }


    private void minuteFilter() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray values = obj.getJSONArray("values");
            JSONArray day = null;
            for (int i = 0; i < values.length(); i++) {
                JSONObject dayValue = values.getJSONObject(i);
                day = dayValue.getJSONArray("day");
                DayReading.add(day);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray finalValue=DayReading.get(mDay-1);
        power_measured.setDrawDataPoints(true);
        double graphlastXValue = 1;
        int value=0;
        int sLim=0;
        if(mHour!=0){sLim=mHour*60;}
        int flim=sLim+60;
        for(sLim=sLim;sLim<flim;sLim++){
            try {
                value= (int) finalValue.get(sLim);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            power_measured.appendData(new DataPoint(graphlastXValue,value),false,1440);
            graphlastXValue = graphlastXValue+1d;
        }
        power_measured.appendData(new DataPoint(graphlastXValue, 0), false, 1440);
        power_measured.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(UsageHistoryActivity.this, "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void hourFilter() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray values = obj.getJSONArray("values");
            JSONArray day = null;
            for (int i = 0; i < values.length(); i++) {
                JSONObject dayValue = values.getJSONObject(i);
                day = dayValue.getJSONArray("day");
                DayReading.add(day);
                Log.d("sds", String.valueOf(day));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray finalValue=DayReading.get(mDay-1);
        power_measured.setDrawDataPoints(true);
        double graphlastXValue = 1;
        int value=0;
        int sLim=0;
        int fLim=60;
        boolean exitFlag=false;
        while (exitFlag==false) {
            for (value = 0; sLim < fLim; sLim++) {
                try {
                    value+= (int) finalValue.get(sLim);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            power_measured.appendData(new DataPoint(graphlastXValue, value), false, 1440);
            graphlastXValue = graphlastXValue + 1d;
            sLim=fLim;
            fLim+=60;
            if(fLim>1440)
            {exitFlag=true;}
        }
        power_measured.appendData(new DataPoint(graphlastXValue, 0), false, 1440);
        power_measured.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(UsageHistoryActivity.this, "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

    }
    //dialog
    public void open(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Show graph with current values");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(UsageHistoryActivity.this,"ok",Toast.LENGTH_LONG).show();
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                notifyUser("Choose date and time");
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void seekbarControl(){
        seekBar = findViewById(R.id.seekBar);
        int days;
        switch (FlagfilterID){
            case 1:{seekBar.setMax(60);break;}
            case 2:{seekBar.setMax(24);break;}
            case 3:{
                Calendar calendar = Calendar.getInstance();
                calendar.set(mYear, mMonth, mDay);
                days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                seekBar.setMax(days-1);
            break;}
            case 4:{seekBar.setMax(4);break;}
            case 5:{seekBar.setMax(12);break;}
            case 6:{seekBar.setMax(5);break;}
        }


    }


}

