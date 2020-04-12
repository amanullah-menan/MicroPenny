package com.menan.micropenny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.menan.micropenny.Prevalent.Prevalent;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinButton,alreadyAccountButton;
    private ProgressDialog lodingBar;

    String version="";
    String downloadLink="";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;
        joinButton=findViewById(R.id.join);
        alreadyAccountButton=findViewById(R.id.goLogin);
        lodingBar=new ProgressDialog(this);
        Paper.init(this);
        updateCheck();
}

    private void userLogin(final String userEmail, final String userPassword ){
        String url="https://micropenny.rbohost.xyz/login.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success")){
                    lodingBar.dismiss();
                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(response.equals("failed")){
                    Toast.makeText(MainActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                    lodingBar.dismiss();
                }
                else if(response.equals("error")){
                    Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    lodingBar.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",userEmail);
                param.put("password",userPassword);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(MainActivity.this).addToRequestQueue(request);
    }

    void updateCheck(){
        lodingBar.setTitle("Checking for update");
        lodingBar.setMessage("Please wait....");
        lodingBar.setCanceledOnTouchOutside(false);
        lodingBar.show();
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            version =pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String url="https://micropenny.rbohost.xyz/update.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                lodingBar.dismiss();
                if(response.equals("latest")){
                    String userMail=Paper.book().read(Prevalent.userEmail);
                    String userPassword=Paper.book().read(Prevalent.userPassword);
                    if(userMail!=null && userPassword!=null){
                        lodingBar.setTitle("Login");
                        lodingBar.setMessage("Please wait....");
                        lodingBar.setCanceledOnTouchOutside(false);
                        lodingBar.show();
                        userLogin(userMail,userPassword);
                    }
                    alreadyAccountButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    joinButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
                else if(response.contains("www")){
                    downloadLink=response.toString();
                    updateNotice();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lodingBar.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("version",version);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(MainActivity.this).addToRequestQueue(request);
    }



    void updateNotice(){
        AlertDialog alertDialog=new AlertDialog.Builder(this)
                .setTitle("New Version Available")
                .setCancelable(false)
                .setMessage("Please update to new version")
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadLink));
                        startActivity(browserIntent);
                        updateNotice();
                    }
                }).create();
        alertDialog.show();
    }
    }
