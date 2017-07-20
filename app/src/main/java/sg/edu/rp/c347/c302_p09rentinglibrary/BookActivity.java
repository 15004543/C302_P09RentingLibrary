package sg.edu.rp.c347.c302_p09rentinglibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    ArrayList<Books> bookList = new ArrayList<>();
    ListView listView;
    String loginId, apikey, catId;
    Intent intent;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            loginId = prefs.getString("loginId", "");
            apikey = prefs.getString("apikey", "");

            intent = getIntent();
            catId = intent.getStringExtra("catId");

            if (apikey != null) {
                HttpRequest request = new HttpRequest("http://10.0.2.2/C302_P09mCafe/getBookByCategory.php");
                request.setMethod("POST");
                request.addData("loginId", loginId);
                request.addData("apikey", apikey);
                request.addData("cat_id", catId);
                request.execute();

                try {
                    String jsonString = request.getResponse();

                    JSONArray jsonArray = new JSONArray(jsonString);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        Books books = new Books();
                        books.setId(jObj.getInt("id"));
                        books.setCat_id(jObj.getInt("c_id"));
                        books.setTitle(jObj.getString("title"));
                        books.setPages(jObj.getString("pages"));
                        books.setQuantity(jObj.getString("qty"));
                        books.setRent_price(jObj.getString("rent_price"));
                        bookList.add(books);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                BookAdapter arrayAdapter = new BookAdapter(this, R.layout.book_row, bookList);
                listView = (ListView) findViewById(R.id.lvBook);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Books book = (Books) parent.getItemAtPosition(position);
                        intent = new Intent(getApplicationContext(), BookDetailActivity.class);
                        intent.putExtra("book", book);
                        startActivity(intent);
                    }
                });
            }else {
                // AlertBox
                showAlert("Login Failed");
            }
        } else {
            // AlertBox
            showAlert("No network connection!");
        }
    }

    private void showAlert(String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        BookActivity.this.finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = builder.create();

        // show it
        alertDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.menu_add){
            intent = new Intent(getApplicationContext(), AddBookActivity.class);
            intent.putExtra("catId", catId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
