package com.example.admin.nav;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.nav.main_menu.HistoryActivity;
import com.example.admin.nav.model.ListImageData;
import com.example.admin.nav.model.ReceiveImageData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TextActivity extends AppCompatActivity {
    TextView textView;
    ImageButton text_home, text_copy, text_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ExtendedDataHolder extras = ExtendedDataHolder.getInstance();
        String text_send_strImg = "";
        if (extras.hasExtra("extra")) {
            text_send_strImg = (String) extras.getExtra("extra");
        }

        Intent intent =  getIntent();
        String requestId = intent.getStringExtra("requestId");
        if (requestId != null){
            Long id = Long.parseLong(requestId);
            getData(id);
        } else {
            sendData(text_send_strImg);
        }
        textView = findViewById(R.id.text_result);
        textView.setMovementMethod(new ScrollingMovementMethod());
        //ImageButton Home
        text_home = findViewById(R.id.text_home);
        text_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMain();
            }
        });
        //ImageButton Copy
        text_copy = findViewById(R.id.text_copy);
        text_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copy_text", textView.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(TextActivity.this, "Text copied to clipboard ", Toast.LENGTH_SHORT).show();
            }
        });
        //ImageButton History
        text_history = findViewById(R.id.text_history);
        text_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHistory();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String sendData(String data) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://103.114.107.103:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        CallService service = retrofit.create(CallService.class);
        Call<ResponseBody> call = service.getTextFromImage(new ImageData(data));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        textView.setText(response.body().string() + "\nThanh cong!");
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TextActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                if (t.getMessage() == null){
                    showAlertDialog1();
                }
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }
        });
        return "fault";
    }
    private String getData(Long id) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://103.114.107.103:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        CallService service = retrofit.create(CallService.class);
        Call<ResponseBody> call = service.getOneImageData(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String rs = response.body().string();
                        ReceiveImageData receiveImageData = new Gson().fromJson(rs, ReceiveImageData.class);
                        textView.setText(receiveImageData.getBody() +"\nthanh cong!");
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TextActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return "fault";
    }
    public void showAlertDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Image to Text");
        builder.setMessage("Ảnh không đúng chiều hoặc định dạng!");
        builder.setCancelable(false);
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(TextActivity.this, "Continue", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(TextActivity.this, "Back", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void startMain() {
        //Khoi tao lai Activity main
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    private void startHistory() {
        //Khoi tao lai History main
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        startActivity(intent);
    }
}
