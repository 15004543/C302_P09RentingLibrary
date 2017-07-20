package sg.edu.rp.c347.c302_p09rentinglibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class BookDetailActivity extends AppCompatActivity {

    TextView tvTitle, tvPages, tvQuantity, tvPrice;
    Intent intent;
    Books bookList;
    Button btnDelete, btnUpdate;
    SharedPreferences prefs;
    String loginId, apikey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loginId = prefs.getString("loginId", "");
        apikey = prefs.getString("apikey", "");

        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvPages = (TextView)findViewById(R.id.tvPages);
        tvQuantity = (TextView)findViewById(R.id.tvQuantity);
        tvPrice = (TextView)findViewById(R.id.tvRentPrice);
        btnDelete = (Button)findViewById(R.id.buttonDelete);
        btnUpdate = (Button)findViewById(R.id.buttonUpdate);

        intent = getIntent();
        bookList = (Books) intent.getSerializableExtra("book");

        tvTitle.setText(bookList.getTitle().toString());
        tvPages.setText(bookList.getPages().toString());
        tvQuantity.setText(bookList.getQuantity().toString());
        tvPrice.setText(bookList.getRent_price().toString());
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpRequest request = new HttpRequest("http://10.0.2.2/C302_P09mCafe/deleteBookById.php");
                request.setMethod("POST");
                request.addData("loginId", loginId);
                request.addData("apikey", apikey);
                request.addData("bookId", Integer.toString(bookList.getId()));
                request.execute();

                try{
                    String jsonString = request.getResponse();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
