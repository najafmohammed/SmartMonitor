package com.novus.smartmonitor;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.widget.TimePicker;
import java.util.Calendar;

public class UsageHistoryActivity extends AppCompatActivity {
    private Button choosedate,choosetime,showGraph;
    private TextView status,selectedDate,selectedTime,selectedFilter;
    private ChipGroup dateParameter;
    private int mYear,mMonth,mDay,mMinute,mHour;
    private Calendar c;
    private boolean dateSelectedFlag=false,timeSelectedFlag=false;
    private Context ctx = this;
    public int[] day1 = {1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,999};
    public int[] day2 = {6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4, 5, 1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,999};
    public int[] day3 = {4, 1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,999};
    public int[] day4 = {3, 2, 3, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,999};
    public int[] day5 = {5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4, 5, 1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,999};
    public int[] day6 = {1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4, 5, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,999};
    public int[] day7 = {2, 2, 3, 1, 2, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 1, 6, 2, 1, 2, 3, 4, 5, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 1, 2, 3, 4, 5,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,1, 2, 3, 1, 2, 3, 4, 5, 3, 1, 2, 3, 4, 5, 3, 4, 5, 6, 2, 1, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 2, 1, 3, 4, 5, 5, 2, 3, 4, 5, 2, 1, 2, 3, 4,999};

    private LineGraphSeries <DataPoint> power_measurured =new LineGraphSeries<>(new DataPoint[]{new DataPoint(0,0),new DataPoint(1,2),new DataPoint(2,4)});
    private DataPoint[] generateData(){
        DataPoint[] values =new DataPoint[]{new DataPoint(0,0)};
        return values;
    }
    private double graphlastXValue =1d;
    private void reset(){
        final GraphView graph = findViewById(R.id.history_graph);
        power_measurured.resetData(generateData());
        graph.addSeries(power_measurured);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_history);
        choosedate=findViewById(R.id.choosedate);
        status=findViewById(R.id.status_usgae);
        dateParameter=findViewById(R.id.Date_parameter);
        choosetime=findViewById(R.id.choosetime);
        TimePicker timePicker = findViewById(R.id.timeSelect);
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
        startup_aniamtion();


        showGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dateSelectedFlag){
                    Snackbar snackbar = Snackbar
                            .make(view ,"Date has not been selected", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.BLACK)
                            .setTextColor(Color.WHITE)
                            .setAction("Choose Date", new View.OnClickListener() {
                                @Override
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
                                @Override
                                public void onClick(View view) {
                                    show_Timepicker();
                                }
                            });
                    snackbar.show();
                }
                else {
                    final Snackbar snackbar = Snackbar
                            .make(view ,"Graph being processed please wait", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.BLACK)
                            .setTextColor(Color.WHITE)
                            .setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    snackbar.show();
                }
            }
        });

        choosetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dateSelectedFlag) {
                    show_Timepicker();
                }
                else{
                    Snackbar snackbar = Snackbar
                            .make(view ,"select date before choosing time", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(Color.BLACK)
                            .setTextColor(Color.WHITE)
                            .setAction("Choose Date", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    show_Datepicker();
                                }
                            });
                    snackbar.show();
                }
            }
        });
        choosedate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                show_Datepicker();

            }
        });
        dateParameter.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip=dateParameter.findViewById(checkedId);
                if(chip!=null){
                    String chipname= (String) chip.getText();
                    status.setText(chipname);
                    switch (chipname){
                        case "Minute":{
                        selectedFilter.setText("Minute");
                            minuteFilter();
                        break;}
                        case "Hour":{
                            selectedFilter.setText("Hour");
                            hourFilter();
                            break;}
                        case "Day":{
                            selectedFilter.setText("Day");
                            dayFilter();
                            break;}
                        case "Week":{
                            selectedFilter.setText("Week");
                            weekFilter();
                            break;}
                        case "Month":{
                            selectedFilter.setText("Month");
                            monthFilter();
                            break;}
                        case "Year":{
                            selectedFilter.setText("Year");
                            yearFilter();
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
                        mMinute = pMinute;
                        selectedTime.setText(mHour+":"+mMinute);
                        showGraph.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        timeSelectedFlag=true;
                    }
                }, mHour, mMinute, true);

        timePickerDialog.show();
    }

    private void minuteFilter() {
    }
    private void hourFilter() {

    }
    private void dayFilter() {

    }
    private void weekFilter() {

    }
    private void monthFilter() {

    }
    private void yearFilter() {

    }

    private void notifyUser(String message) {

        Toast.makeText(UsageHistoryActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }
    //custom conversion script
    public void Plot(int[] temp){
        final GraphView graph = findViewById(R.id.history_graph);
        double graphlastXValue=1d;
        for(int i:temp){
        power_measurured.appendData(new DataPoint(graphlastXValue,i),false,1440);
        graphlastXValue = graphlastXValue+1d;
        }
        graph.addSeries(power_measurured);
        graph.setVisibility(View.VISIBLE);
    }
    public String DayToValueConverter(int day){
        int Tday=day;
       String temp="day"+day;
        return temp;
    }

    public int[] GetHourValue(int[] temp){
        final GraphView graph = findViewById(R.id.history_graph);
        LineGraphSeries <DataPoint> power_measurured =new LineGraphSeries<>(new DataPoint[]{new DataPoint(0,0)});
        double graphlastXValue=0;
        int hourValue[];
        int i,j=0,lim=60;
        hourValue= new int[60];
        for (i=1;i<25;i++){
            while(j<lim){
                hourValue[i]+=temp[j];
                j++;
            }
            power_measurured.appendData(new DataPoint(graphlastXValue,hourValue[i]),false,50);
            graphlastXValue = graphlastXValue+1d;
            j=lim;
            lim+=60;
        }
        graph.addSeries(power_measurured);
        return hourValue;
    }

    }

