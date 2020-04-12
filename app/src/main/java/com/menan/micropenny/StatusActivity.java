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

public class StatusActivity extends AppCompatActivity {
    private RecyclerView rView;
    StatusViewAdapter adapter;
    List<StatusSetFire> statusSetFireList;
    private TextView statusImage;
    private ProgressDialog loadingBar;
    String node="";
    int sum=0;
    int nodeCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        statusImage=findViewById(R.id.mystatus);

        Paper.init(this);
        statusSetFireList=new ArrayList<StatusSetFire>();
        rView =findViewById(R.id.statusRecycler);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new LinearLayoutManager(this));
        oldRemove();
        loadStatus();
        loadingBar =new ProgressDialog(this);
        loadingBar.setTitle("Refreshing list");
        loadingBar.setMessage("Please wait....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    void loadStatus(){
        final String userMail=Paper.book().read(Prevalent.userEmail);
        String url="https://micropenny.rbohost.xyz/getStatus.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray statusArray=new JSONArray(response);
                    for (int i = 0; i < statusArray.length(); i++) {
                        JSONObject status = statusArray.getJSONObject(i);
                        String date=status.getString("date");
                        int impression=status.getInt("impression");
                        int click=status.getInt("click");
                        int sta=status.getInt("status");
                        double balance=status.getDouble("balance");
                        double refBalance=status.getDouble("ref");
                        StatusSetFire statusSetFire=new StatusSetFire(date,impression,click,sta,balance,refBalance);
                        statusSetFireList.add(statusSetFire);
                    }
                    adapter =new StatusViewAdapter(StatusActivity.this,statusSetFireList);
                    rView.setAdapter(adapter);
                    loadingBar.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingBar.dismiss();
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
        MySingleton.getmInstance(StatusActivity.this).addToRequestQueue(request);
    }



    void oldRemove(){
        final String userMail=Paper.book().read(Prevalent.userEmail);;
        String url="https://micropenny.rbohost.xyz/removeOld.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sumStatus();
                sumNode();
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
        MySingleton.getmInstance(StatusActivity.this).addToRequestQueue(request);
    }

    void statusHandler(){
        if(nodeCount!=0){
            String ratio=String.valueOf((sum*100)/nodeCount);
            statusImage.setText(ratio+"%");
        }


    }

    void sumStatus() {
        final String userMail=Paper.book().read(Prevalent.userEmail);;
        String url="https://micropenny.rbohost.xyz/sumStatus.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sum=Integer.parseInt(response);
                statusHandler();
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
        MySingleton.getmInstance(StatusActivity.this).addToRequestQueue(request);
    }
    void sumNode(){
        final String userMail=Paper.book().read(Prevalent.userEmail);;
        String url="https://micropenny.rbohost.xyz/sumNode.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                nodeCount=Integer.parseInt(response);
                statusHandler();
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
        MySingleton.getmInstance(StatusActivity.this).addToRequestQueue(request);
    }
}
