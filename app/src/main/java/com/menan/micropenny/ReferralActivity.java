package com.menan.micropenny;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.menan.micropenny.Prevalent.Prevalent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class ReferralActivity extends AppCompatActivity {
    private TextView refCount;
    private RecyclerView rView;
    ReferralViewAdapter adapter;
    List<ReferralSetFire> referralSetFireList;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        Paper.init(this);

        referralSetFireList=new ArrayList<>();
        rView=findViewById(R.id.referralRecycler);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new LinearLayoutManager(this));

        refCount=findViewById(R.id.myReferral);

        referral();

    }

    public void referral(){
        loadingBar =new ProgressDialog(ReferralActivity.this);
        loadingBar.setTitle("Refreshing List");
        loadingBar.setMessage("Please wait....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        final String userMail=Paper.book().read(Prevalent.userEmail);
        String url="https://micropenny.rbohost.xyz/getReferrals.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray statusArray=new JSONArray(response);
                    refCount.setText(String.valueOf(statusArray.length()));
                    for (int i = 0; i < statusArray.length(); i++) {
                        JSONObject status = statusArray.getJSONObject(i);
                        String name=status.getString("name");
                        ReferralSetFire referralSetFire=new ReferralSetFire(name);
                        referralSetFireList.add(referralSetFire);
                        loadingBar.dismiss();
                    }
                    adapter =new ReferralViewAdapter(ReferralActivity.this,referralSetFireList);
                    rView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
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
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(ReferralActivity.this).addToRequestQueue(request);
    }
}

