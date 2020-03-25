package com.novus.smartmonitor;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private TextView profile,other,nav_user_name;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        auth = FirebaseAuth.getInstance();
        profile=findViewById(R.id.settings_profile);
        other=findViewById(R.id.settings_other);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        nav_user_name=headerview.findViewById(R.id.nav_username);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String savedUsername= prefs.getString("saved_username","not found");
        nav_user_name.setText(savedUsername);
        ImageView nav_profile_img = headerview.findViewById(R.id.nav_profile_img);
        Bitmap bitmap = new profile_settings.ImageSaver(this).setFileName("ProfilePic.png").setDirectoryName("images").load();
        nav_profile_img.setImageBitmap(bitmap);
        nav_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this, profile_settings.class);
                startActivity(intent);
                finish();
            }
        });
        final ActionBarDrawerToggle[] toggle = {new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)};
        drawer.addDrawerListener(toggle[0]);
        toggle[0].syncState();
        navigationView.setNavigationItemSelectedListener(this);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(settings.this, profile_settings.class);
                startActivity(intent);
            }
        });

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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bills) {

        } else if (id == R.id.nav_history) {

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
            Intent intent = new Intent(settings.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void notifyUser(String message) {

        Toast.makeText(settings.this, message,
                Toast.LENGTH_SHORT).show();
    }
}
