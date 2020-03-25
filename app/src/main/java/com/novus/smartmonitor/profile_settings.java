package com.novus.smartmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class profile_settings extends AppCompatActivity {
    private TextView username;
    private ImageView profilepic;
    private EditText new_username;
    private Button change,choose;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        username=findViewById(R.id.current_username);
        profilepic=findViewById(R.id.profile_pic);
        new_username=findViewById(R.id.new_username);
        change=findViewById(R.id.change_profile_activity);
        choose=findViewById(R.id.choose_profile_activity);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String savedUsername= prefs.getString("saved_username","not found");
        username.setText(savedUsername);

        Bitmap bitmap = new ImageSaver(this).setFileName("ProfilePic.png").setDirectoryName("images").load();
        if(bitmap!=null) {
            profilepic.setImageBitmap(bitmap);
        }

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_profile();
            }
        });


    }
    public static class ImageSaver {

        private String directoryName = "images";
        private String fileName = "image.png";
        private Context context;
        private boolean external;

        public ImageSaver(Context context) {
            this.context = context;
        }

        public ImageSaver setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public ImageSaver setExternal(boolean external) {
            this.external = external;
            return this;
        }

        public ImageSaver setDirectoryName(String directoryName) {
            this.directoryName = directoryName;
            return this;
        }

        public void save(Bitmap bitmapImage) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(createFile());
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @NonNull
        private File createFile() {
            File directory;
            if(external){
                directory = getAlbumStorageDir(directoryName);
            }
            else {
                directory = context.getDir(directoryName, Context.MODE_PRIVATE);
            }
            if(!directory.exists() && !directory.mkdirs()){
                Log.e("ImageSaver","Error creating directory " + directory);
            }

            return new File(directory, fileName);
        }

        private File getAlbumStorageDir(String albumName) {
            return new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName);
        }

        public static boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state);
        }

        public static boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
        }

        public Bitmap load() {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(createFile());
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
    private void change_profile() {

        String fetch_username = new_username.getText().toString();
        if (TextUtils.isEmpty(fetch_username)) {
            Toast.makeText(getApplicationContext(), "Enter a username", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(fetch_username)) {
            Toast.makeText(getApplicationContext(), "username retrived", Toast.LENGTH_SHORT).show();
            username.setText(fetch_username);
            SharedPreferences.Editor prefEditor =PreferenceManager.getDefaultSharedPreferences(this).edit();
            prefEditor.putString("saved_username",fetch_username);
            prefEditor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        /*if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setMessage("Uploaded "+"0%");
            progressDialog.show();

        }*/
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                new ImageSaver(this).setFileName("ProfilePic.png").setDirectoryName("images").save(bitmap);
                profilepic.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
