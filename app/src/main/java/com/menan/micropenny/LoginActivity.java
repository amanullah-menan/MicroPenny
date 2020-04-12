package com.menan.micropenny;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText userEmail,password;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=findViewById(R.id.Login);
        userEmail=findViewById(R.id.loginEmail);
        password=findViewById(R.id.loginPassword);
        loadingBar =new ProgressDialog(this);

        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }

    private void userLogin(final String userEmail, final String userPassword ){
        String url="https://micropenny.rbohost.xyz/login.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success")){
                    Toast.makeText(LoginActivity.this, "Logged in Sucessfully...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Paper.book().write(Prevalent.userEmail,userEmail);
                    Paper.book().write(Prevalent.userPassword,userPassword);
                    Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(response.equals("failed")){
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                }
                else if(response.equals("error")){
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
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
        MySingleton.getmInstance(LoginActivity.this).addToRequestQueue(request);
    }

    private void LoginUser() {
        String userMail= userEmail.getText().toString();
        String userPassword=password.getText().toString();


        if(TextUtils.isEmpty(userMail)){
            Toast.makeText(this, "Enter your Email or Phone", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Login");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            userLogin(userMail,userPassword);
        }
    }

}
