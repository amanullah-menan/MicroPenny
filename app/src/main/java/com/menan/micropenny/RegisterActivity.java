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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText inputName, inputPhone,inputPassword,inputReferrad,inputEmail;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton=findViewById(R.id.Register);
        inputEmail=findViewById(R.id.email);
        inputName=findViewById(R.id.name);
        inputPhone =findViewById(R.id.registerPhone);
        inputPassword=findViewById(R.id.registerPassword);
        inputReferrad=findViewById(R.id.referred);
        loadingBar =new ProgressDialog(this);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void registerAccount(final String email, final String name, final String phone, final String password, final String referred){
        String url="http://micropenny.rbohost.xyz/register.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("successful")){
                    Toast.makeText(RegisterActivity.this, "Congratulation, your account is created successfully", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else  if(response.equals("already")){
                    Toast.makeText(RegisterActivity.this, "This email is already registered", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                else  if(response.equals("failed")){
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                else  if(response.equals("referred invalid")){
                    Toast.makeText(RegisterActivity.this, "Invalid referral email", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Network Error:Please try again", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("name",name);
                param.put("phone",phone);
                param.put("password",password);
                param.put("balance","0");
                param.put("referred_by",referred);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(RegisterActivity.this).addToRequestQueue(request);
    }

    private void register() {
        String email=inputEmail.getText().toString();
        String name=inputName.getText().toString();
        String phone= inputPhone.getText().toString();
        String password=inputPassword.getText().toString();
        String referred=inputReferrad.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter your Email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter your Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Enter your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(referred)){
            Toast.makeText(this, "Enter referral code", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Register");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            registerAccount(email,name,phone,password,referred);
        }
    }

}
