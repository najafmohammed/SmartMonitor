package com.novus.smartmonitor;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.View;
import android.graphics.Color;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import android.os.Vibrator;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String CHANNEL_ID = "001";
    int power_value=0;
    int cost_value=0;
    double unit_rate=3.5;
    Button button,reset,current_usage,power_button,status_button,cost_button;
    private Vibrator hapticFeedback;
    ImageView image,nav_profile_img;
    TextView power,cost,status,ai_suggestions,nav_user_name;
    private LineGraphSeries <DataPoint> power_measurured =new LineGraphSeries<>(new DataPoint[]{new DataPoint(0,0)});
    private LineGraphSeries <DataPoint> power_measurured1 =new LineGraphSeries<>(new DataPoint[]{new DataPoint(0,0)});
    String low[]={"Limit use of TV","Turn off lights","Keep up the savings","Unplug phone from charger"};
    String mid[]={"turn off TV after some time","Turn off lights and fans","average usage","Approaching high usage"};
    String high[]={"Turn use of TV","Turn off AC","High usage of devices","huge Bill incoming"};
    private double graphlastXValue =1d;
    protected DrawerLayout mDrawer;

    private FirebaseAuth auth;
    private CardView DataView;

    private DataPoint[] generateData(){
        DataPoint[] values =new DataPoint[]{new DataPoint(0,0)};
        return values;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        hapticFeedback=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        image = findViewById(R.id.day_night_image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        power=findViewById(R.id.power);

        DataView=findViewById(R.id.DataCardview);

        power_button=findViewById(R.id.power_button);
        cost=findViewById(R.id.cost);
        cost_button=findViewById(R.id.cost_button);
        status=findViewById(R.id.status);
        status_button=findViewById(R.id.status_button);
        ai_suggestions=findViewById(R.id.ai_suggestions);
        current_usage=findViewById(R.id.current_usage);
        reset=findViewById(R.id.reset);

        startup_aniamtion();

        power_measurured.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                status.setText(""+dataPoint);
            }
        });

        final GraphView graph =findViewById(R.id.history_graph);
        //graph part
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

        //Guesture support template
        /*DataView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });*/

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapticFeedback.vibrate(50);
                Animation refresh = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.refresh_menu);
                graph.startAnimation(refresh);
                graph.takeSnapshotAndShare(MainActivity.this, "exampleGraph", "GraphViewSnapshot");
                power_measurured.resetData(generateData());
                graphlastXValue=0;
                power_value=0;
                cost_value=0;
                power.setText(power_value +"KWH");
                status.setText("Offline");
                status.setBackgroundResource(R.drawable.status_low);
                cost.setBackgroundResource(R.drawable.status_low);
                power.setBackgroundResource(R.drawable.status_low);
                cost.setText(cost_value+"rs");
                current_usage.setText("0 Kwh");
                ai_suggestions.setText("System Reset");
                power_button.setBackgroundResource(R.drawable.rnd_btn_transp_low);
                cost_button.setBackgroundResource(R.drawable.rnd_btn_transp_low);
                status_button.setBackgroundResource(R.drawable.rnd_btn_transp_low);
                ai_suggestions.setBackgroundResource(R.drawable.rnd_btn_transp_low);

            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                hapticFeedback.vibrate(50);
                Random random = new Random();
                int randomFactor = random.nextInt(7-3)+3;
                power_value += randomFactor;
                power.setText(power_value +"KWH");
                cost_value= (int) (power_value*unit_rate);

                current_usage.setText(randomFactor+"kwh");


                cost.setText(cost_value+"rs");
                if (power_value<20) {

                    status.setText("Low");
                    power_measurured.setColor(Color.GREEN);
                    status_button.setBackgroundResource(R.drawable.rnd_btn_transp_low);
                    power_button.setBackgroundResource(R.drawable.rnd_btn_transp_low);
                    cost_button.setBackgroundResource(R.drawable.rnd_btn_transp_low);
                    ai_suggestions.setBackgroundResource(R.drawable.rnd_btn_transp_low);
                    status.setBackgroundResource(R.drawable.status_low);
                    power.setBackgroundResource(R.drawable.status_low);
                    cost.setBackgroundResource(R.drawable.status_low);

                    int ai_select= random.nextInt(4-0)+0;
                    ai_suggestions.setText(low[ai_select]);
                }
                if(power_value>20 && power_value<50){

                    status.setText("Moderate");
                    power_measurured.setColor(Color.YELLOW);
                    status_button.setBackgroundResource(R.drawable.rnd_btn_transp_mid);
                    power_button.setBackgroundResource(R.drawable.rnd_btn_transp_mid);
                    cost_button.setBackgroundResource(R.drawable.rnd_btn_transp_mid);
                    ai_suggestions.setBackgroundResource(R.drawable.rnd_btn_transp_mid);
                    cost.setBackgroundResource(R.drawable.status_moderate);
                    status.setBackgroundResource(R.drawable.status_moderate);
                    power.setBackgroundResource(R.drawable.status_moderate);
                    int ai_select= random.nextInt(4-0)+0;
                    ai_suggestions.setText(mid[ai_select]);
                }
                if(power_value>50){
                    highpowerAlert();
                    status.setText("High");
                    power_measurured.setColor(Color.RED);
                    status_button.setBackgroundResource(R.drawable.rnd_btn_transp_high);
                    power_button.setBackgroundResource(R.drawable.rnd_btn_transp_high);
                    cost_button.setBackgroundResource(R.drawable.rnd_btn_transp_high);
                    ai_suggestions.setBackgroundResource(R.drawable.rnd_btn_transp_high);
                    cost.setBackgroundResource(R.drawable.status_high);
                    status.setBackgroundResource(R.drawable.status_high);
                    power.setBackgroundResource(R.drawable.status_high);
                    int ai_select= random.nextInt(4-0)+0;
                    ai_suggestions.setText(high[ai_select]);
                }
                graph.setVisibility(View.VISIBLE);

                power_measurured.setDrawDataPoints(true);
                power_measurured.appendData(new DataPoint(graphlastXValue,randomFactor),true,50);
                graphlastXValue = graphlastXValue+1d;
                graph.addSeries(power_measurured);
            }
        });
        mDrawer= findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        nav_user_name=headerview.findViewById(R.id.nav_username);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String savedUsername= prefs.getString("saved_username","not found");
        nav_user_name.setText(savedUsername);
        nav_profile_img=headerview.findViewById(R.id.nav_profile_img);
        Bitmap bitmap = new profile_settings.ImageSaver(this).setFileName("ProfilePic.png").setDirectoryName("images").load();
        nav_profile_img.setImageBitmap(bitmap);
        nav_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapticFeedback.vibrate(50);
                Intent intent = new Intent(MainActivity.this, profile_settings.class);
                startActivity(intent);
            }
        });
        final ActionBarDrawerToggle[] toggle = {new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)};
        mDrawer.addDrawerListener(toggle[0]);
        toggle[0].syncState();

        navigationView.setNavigationItemSelectedListener(this);
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                hapticFeedback.vibrate(50);
                image.setImageResource(R.drawable.home_night);
            }
        });
        button = findViewById(R.id.change);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                hapticFeedback.vibrate(50);
                image.setImageResource(R.drawable.home_day);

            }
        });

    }
    public void startup_aniamtion(){
        int[] set1 = {R.id.day_night_image,R.id.ai_suggestions,R.id.status_button,R.id.power_button,R.id.cost_button,R.id.history_graph,};
        int[] set2 = {R.id.current_usage,R.id.temperature,R.id.change};
        int i = 0;
        for (int viewId : set1) {
            if(i==0) {
                ImageView imageview = findViewById(viewId);
                Animation slide = AnimationUtils.loadAnimation(this, R.anim.slidedown);
                slide.setStartOffset(i * 200);
                imageview.startAnimation(slide);
                for (int  id:set2){
                    if(i==2){
                        Button button = findViewById(id);
                        Animation slide_right = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);
                        slide_right.setStartOffset(i * 900);
                        button.startAnimation(slide_right);
                    }
                    else {
                        Button button = findViewById(id);
                        Animation slide_left = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
                        slide_left.setStartOffset(i * 200);
                        button.startAnimation(slide_left);
                    }
                }
            }
            else if(i==1){
                TextView textView = findViewById(viewId);
                Animation slide = AnimationUtils.loadAnimation(this, R.anim.slidedown);
                slide.setStartOffset(i * 200);
                textView.startAnimation(slide);
            }
            else if(i==5){
                GraphView graphView=findViewById(viewId);
                Animation slide = AnimationUtils.loadAnimation(this, R.anim.slide_from_bottom);
                slide.setStartOffset(i * 200);
                graphView.startAnimation(slide);
            }
            else {
                Button button=findViewById(viewId);
                Animation slide = AnimationUtils.loadAnimation(this, R.anim.slidedown);
                slide.setStartOffset(i * 200);
                button.startAnimation(slide);
            }
            i++;
        }
    }

    private void highpowerAlert() {
        Intent intent = new Intent(this, MainActivity.class);
        Random random = new Random();
        int ai_select= random.nextInt(4-0)+0;
        String ai_suggestion=high[ai_select];
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("High Usage Alert")
                .setContentText("System has measured High usage")
                .setStyle(new NotificationCompat.BigTextStyle()

                        .bigText(ai_suggestion))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId=1;
        notificationManager.notify(notificationId, builder.build());
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Connect) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        hapticFeedback.vibrate(50);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_profile_img){
        }

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bills) {

        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(this, UsageHistoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, settings.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.smartmonitor.com");
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Check Out Github page");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        }
        else if (id == R.id.nav_logout) {
            notifyUser("Singed out");
            auth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void notifyUser(String message) {

        Toast.makeText(MainActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }



}
