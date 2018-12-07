package com.example.admin.nav.main_menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin.nav.CallService;
import com.example.admin.nav.ImageActivity;
import com.example.admin.nav.MainActivity;
import com.example.admin.nav.R;
import com.example.admin.nav.TextActivity;
import com.example.admin.nav.model.ListImageData;
import com.example.admin.nav.model.ReceiveImageData;
import com.example.admin.nav.utils.CustomListAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HistoryActivity extends AppCompatActivity {

    List list;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listView = findViewById(R.id.listView);

        getData();
    }

    private String getData() {
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
        Call<ResponseBody> call = service.getAllImageData();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String rs = response.body().string();
                        list = new Gson().fromJson(rs, ListImageData.class).getList();
                        setAdapter(list);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return "fault";
    }

    private void setAdapter(List<ReceiveImageData> ridList) {
        listView.setAdapter(new CustomListAdapter(HistoryActivity.this, ridList));

        //Khi người dùng click vào các ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                ReceiveImageData receiveImageData = (ReceiveImageData) o;

                Intent intent = new Intent(HistoryActivity.this, TextActivity.class);
                intent.putExtra("requestId", receiveImageData.getId());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                ReceiveImageData receiveImageData = (ReceiveImageData) o;
                showAlertDialog(receiveImageData.getName());
                return false;
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
    public void showAlertDialog(final String nameFile){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Image to Text");
        builder.setMessage("Bạn có muốn xóa file" +nameFile+" không?");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //lệnh xóa lên server
                Toast.makeText(HistoryActivity.this, "File "+nameFile+" has been deleted!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
