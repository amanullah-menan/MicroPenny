package com.menan.micropenny;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.menan.micropenny.Prevalent.Prevalent;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class WithdrawActivity extends AppCompatActivity {
    private Button submit;
    private EditText bankType, bankPhone,amount;
    private ProgressDialog loadingBar;
    private float balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        submit=findViewById(R.id.withdraw);
        bankType=findViewById(R.id.type);
        bankPhone=findViewById(R.id.number);
        amount=findViewById(R.id.amount);
        loadingBar =new ProgressDialog(this);
        Paper.init(this);
        getBalance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float withdrawAmount=0;
                if(!TextUtils.isEmpty(amount.getText())){
                    withdrawAmount=Float.parseFloat(amount.getText().toString());
                }
                else {
                    Toast.makeText(WithdrawActivity.this, "Enter amount", Toast.LENGTH_SHORT).show();
                }

                if(balance>=100 ){
                    if(withdrawAmount>=100){
                        if(balance>=withdrawAmount){
                            withdraw();
                        }
                        else{
                            Toast.makeText(WithdrawActivity.this, "Not enough penny", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(WithdrawActivity.this, "Minimum withdraw is MP:100", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(WithdrawActivity.this, "Not enough penny", Toast.LENGTH_SHORT).show();
                    Toast.makeText(WithdrawActivity.this, "Minimum withdraw is MP:100", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void withdraw() {
        String withdrawType=bankType.getText().toString();
        String withdrawPhone= bankPhone.getText().toString();
        String withdrawAmount=amount.getText().toString();

        if(TextUtils.isEmpty(withdrawType)){
            Toast.makeText(this, "Enter withdraw type...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(withdrawPhone)){
            Toast.makeText(this, "Enter withdraw phone...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(withdrawAmount)){
            Toast.makeText(this, "Enter amount...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Submitting Request");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            submitWithdraw(withdrawType,withdrawPhone,withdrawAmount);
        }
    }

    private void submitWithdraw(final String withdrawType, final String withdrawPhone, final String withdrawAmount) {
        final String userMail=Paper.book().read(Prevalent.userEmail);;
        String url="https://micropenny.rbohost.xyz/withdraw.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("success")){
                    loadingBar.dismiss();
                    Toast.makeText(WithdrawActivity.this, "Withdraw request successful", Toast.LENGTH_SHORT).show();
                }
                else if(response.equals("pending")){
                    loadingBar.dismiss();
                    Toast.makeText(WithdrawActivity.this, "Pending request found", Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",userMail);
                param.put("type",withdrawType);
                param.put("phone",withdrawPhone);
                param.put("amount",withdrawAmount);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(WithdrawActivity.this).addToRequestQueue(request);

    }
    public void getBalance(){
        final String userMail=Paper.book().read(Prevalent.userEmail);;
        String url="https://micropenny.rbohost.xyz/balance.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                balance=Float.parseFloat(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",userMail);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(WithdrawActivity.this).addToRequestQueue(request);

    }
}
