package com.example.admin.nav;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin.nav.main_menu.HistoryActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText textView;
    Uri imageUri;
    Bundle bundle;
    ListView listView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bundle = savedInstanceState;
        //button camera
        Button button_camera = findViewById(R.id.button_camera);
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
        //button gallery
        Button button_gallery = findViewById(R.id.button_gallery);
        button_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 1);
            }
        });
        //button show_tips
        Button button_show_tips = findViewById(R.id.show_tips);
        button_show_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView = new ListView(MainActivity.this);
                String[] items = {"1. Good lightning is most important. Use the flashlight if possible","2. Please make sure that the text is upright. Use the rotate buttons at the bottom of the screen to rotate the image","3. Blurry text is hard to read for me. Make sure that the camera has properly focused the text","4. Most of the time 5 to 7 megapixels is enough resolution to get good results. Higher values will often only increase the processing time or make error"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, R.id.textItem, items);
                listView.setAdapter(adapter);
                showAlertDialog1();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent1 = new Intent(this, ImageActivity.class);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                intent1.putExtra("requestCode", "0");
                intent1.putExtra("Image", bitmap);
                startActivity(intent1);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancel Camera", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error Camera!", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
               imageUri = data.getData();
               intent1.putExtra("requestCode", "1");
               intent1.putExtra("imageUri", imageUri.toString());
               startActivity(intent1);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancel Gallery", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error Gallery!", Toast.LENGTH_LONG).show();
            }
        }
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
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_whats_new) {

        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_exit) {
            showAlertDialog();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Image to Text");
        builder.setMessage("Bạn có muốn thoát không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Cancel exit", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Khoi tao lai Activity main
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                // Tao su kien ket thuc app
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startActivity(startMain);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showAlertDialog1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How to improve text recognition");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", null);
        builder.setView(listView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
