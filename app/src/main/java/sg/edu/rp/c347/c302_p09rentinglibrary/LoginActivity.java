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

public class LoginActivity extends AppCompatActivity {

    EditText etLogin, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLogin = (EditText)findViewById(R.id.etLogin);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    HttpRequest request = new HttpRequest("http://10.0.2.2/C302_P09mCafe/doLogin.php");
                    request.setMethod("POST");
                    request.addData("username", userName);
                    request.addData("password", password);
                    request.execute();

                    try{
                        String jsonString = request.getResponse();

                        JSONObject jsonObj = (JSONObject) new JSONTokener(jsonString).nextValue();
                        if (jsonObj.getBoolean("authenticated")){
                            String apiKey = jsonObj.getString("apikey");
                            String id = jsonObj.getString("id");

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor prefEdit = prefs.edit();
                            prefEdit.putString("loginId", id);
                            prefEdit.putString("apikey", apiKey);
                            prefEdit.commit();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }else{
                            Snackbar.make(v, "Authentication failed, please login again", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(v, "No network connection available.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            }
        });
    }
}
