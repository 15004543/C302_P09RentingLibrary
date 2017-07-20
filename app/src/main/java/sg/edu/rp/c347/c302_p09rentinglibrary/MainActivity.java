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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<BookCategory> categoryList = new ArrayList<>();

    ListView listView;
    String loginId, apikey;
    Intent intent;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            prefs = PreferenceManager.getDefaultSharedPreferences(this);

            loginId = prefs.getString("loginId", "");
            apikey = prefs.getString("apikey", "");

            if (apikey != null) {
                HttpRequest request = new HttpRequest("http://10.0.2.2/C302_P09mCafe/getBookCategories.php");
                request.setMethod("POST");
                request.addData("loginId", loginId);
                request.addData("apikey", apikey);
                request.execute();

                try {
                    String jsonString = request.getResponse();

                    JSONArray jsonArray = new JSONArray(jsonString);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        BookCategory category = new BookCategory();
                        category.setCat_id(jObj.getInt("c_id"));
                        category.setName(jObj.getString("name"));
                        categoryList.add(category);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BookCategoryAdapter arrayAdapter = new BookCategoryAdapter(this, R.layout.category_row, categoryList);
                listView = (ListView) findViewById(R.id.lvCategory);
                listView.setAdapter(arrayAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        BookCategory bookCategory = (BookCategory) parent.getItemAtPosition(position);
//                        Toast.makeText(MainActivity.this, Integer.toString(bookCategory.getCat_id()), Toast.LENGTH_LONG).show();
                        intent = new Intent(getApplicationContext(), BookActivity.class);
                        intent.putExtra("catId", Integer.toString(bookCategory.getCat_id()));

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
                        MainActivity.this.finish();
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.menu_logout){
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
