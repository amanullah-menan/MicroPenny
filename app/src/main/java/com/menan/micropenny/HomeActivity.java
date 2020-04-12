package com.menan.micropenny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.menan.micropenny.Prevalent.Prevalent;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity  {
    private Button facebookButton,instagramButton,youtubeButton,twitterButton, menuButton;
    private TextView userBalance,clickText,impressionText,clickStatusText,myIp,myCountry;
    private EditText email;
    private ImageView impressionImage,clickImage;
    private String ADMOB_AD_UNIT_ID = "";
    private UnifiedNativeAd nativeAd;
    private Context context;
    private boolean isLeagal=false;
    private String banner0="ca-app-pub-4671840559976910/8036053290";
    private String banner1="ca-app-pub-2989090493512506/2185411493";
    private String banner2="ca-app-pub-6811777494326927/6089492326";
    private String banner3="ca-app-pub-9451842569113287/6942007599";
    private String native0="ca-app-pub-4671840559976910/3520095345";
    private String native1="ca-app-pub-2989090493512506/6855284242";
    private String native2="ca-app-pub-6811777494326927/5838210885";
    private String native3="ca-app-pub-9451842569113287/3190464849";
    int randomNumber;
    private String chosenBanner ="";
    private boolean debugMode=true;
    private DrawerLayout dl;
    private ActionBarDrawerToggle adt;
    private ProgressDialog loadingBar;

     int impression=0;
     int click;
     int clickRequired=2;

    @Override
    protected void onStart() {
        super.onStart();
        checkCountry();
        checkEmail();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FileUtils.deleteQuietly(context.getCacheDir());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        getImpression();
        dl=findViewById(R.id.dl);
        adt=new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        adt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(adt);
        adt.syncState();
        final NavigationView nav_view=findViewById(R.id.nav);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                if(id==R.id.Item1){
                    Intent intent=new Intent(HomeActivity.this, ReferralActivity.class);
                    startActivity(intent);
                    dl.closeDrawer(GravityCompat.START);
                }
                else if(id==R.id.Item2){
                    Intent intent=new Intent(HomeActivity.this, StatusActivity.class);
                    startActivity(intent);
                    dl.closeDrawer(GravityCompat.START);
                }
                else if(id==R.id.Item3){
                    Intent intent=new Intent(HomeActivity.this, WithdrawActivity.class);
                    startActivity(intent);
                    dl.closeDrawer(GravityCompat.START);
                }
                else if(id==R.id.Item4){
                    Intent intent=new Intent(HomeActivity.this, SupportActivity.class);
                    startActivity(intent);
                    dl.closeDrawer(GravityCompat.START);

                }
                else if(id==R.id.Item5){
                    Paper.book().destroy();
                    Intent intent=new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        context=this;

        Paper.init(this);
        myIp=findViewById(R.id.ip);
        myCountry=findViewById(R.id.country);
        clickStatusText=findViewById(R.id.clickStatusText);
        impressionText=findViewById(R.id.impressionText);
        clickText=findViewById(R.id.clickText);
        impressionImage=findViewById(R.id.impStatus);
        clickImage=findViewById(R.id.clickStatus);
        facebookButton=findViewById(R.id.facebook);
        instagramButton=findViewById(R.id.instagram);
        youtubeButton=findViewById(R.id.youtube);
        twitterButton=findViewById(R.id.twitter);
        menuButton =findViewById(R.id.menuBtn);
        randomAdUnitGenerator();
        refreshAd();
        balance();
        userName();
        admobBanner();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(Gravity.LEFT);
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLeagal){
                    Intent intent=new Intent(HomeActivity.this, FacebookActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(context, "Use a VPN to Continue", Toast.LENGTH_SHORT).show();
                }
            }
        });
        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    void checkCountry(){
        loadingBar =new ProgressDialog(HomeActivity.this);
        loadingBar.setTitle("Checking country");
        loadingBar.setMessage("Please wait....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        String url="https://extreme-ip-lookup.com/json";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    myIp.setText(jsonObject.getString("query"));
                    myCountry.setText(jsonObject.getString("country"));
                    if(jsonObject.getString("status").equals("success")){
                        if(!jsonObject.getString("country").equals("Bangladesh")){
                            isLeagal=true;
                            loadingBar.dismiss();
                        }
                        else{
                            isLeagal=false;
                            loadingBar.dismiss();
                        }
                    }
                    else {
                        isLeagal=true;
                        loadingBar.dismiss();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(HomeActivity.this).add(request);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return adt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    void randomAdUnitGenerator(){
        Random r = new Random();
        randomNumber = r.nextInt(4);

        if(debugMode){
            ADMOB_AD_UNIT_ID ="ca-app-pub-3940256099942544/2247696110";
            chosenBanner ="ca-app-pub-3940256099942544/6300978111";
        }
        else if(randomNumber==0) {
            ADMOB_AD_UNIT_ID =native0;
            chosenBanner =banner0;
        }
        else if(randomNumber==1){
            ADMOB_AD_UNIT_ID =native1;
            chosenBanner =banner1;
        }
        else if(randomNumber==2){
            ADMOB_AD_UNIT_ID =native2;
            chosenBanner =banner2;
        }
        else if(randomNumber==3){
            ADMOB_AD_UNIT_ID =native3;
            chosenBanner =banner3;
        }
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
        FileUtils.deleteQuietly(context.getCacheDir());
        System.exit(0);
    }

    void status(){
        if(impression<100){
            clickRequired=0-click;
        }
        else if(impression>=100 && impression<200){
            clickRequired=1-click;
        }
        else if(impression>=200 && impression<300){
            clickRequired=2-click;
        }
        else if(impression>=300 && impression<400){
            clickRequired=3-click;
        }
        else if(impression>=400 ){
            clickRequired=4-click;
        }

        clickText.setText(""+Math.abs(clickRequired) );
        if(clickRequired==0){
            clickImage.setImageResource(R.drawable.ic_done_green_24dp);
        }
        else if(clickRequired==1 || clickRequired==-1){
            clickImage.setImageResource(R.drawable.ic_warning_yellow_24dp);
            if(clickRequired==-1){
                clickStatusText.setText("Over Clicked:");
                impressionImage.setImageResource(R.drawable.ic_warning_yellow_24dp);
            }
        }
        else if(clickRequired==2 || clickRequired==-2){
            clickImage.setImageResource(R.drawable.ic_warning_red_24dp);
            if(clickRequired==-2){
                clickStatusText.setText("Over Clicked:");
                impressionImage.setImageResource(R.drawable.ic_warning_red_24dp);
            }
        }
        else if(clickRequired==3 || clickRequired==-3){
            clickImage.setImageResource(R.drawable.ic_warning_red_24dp);
            if(clickRequired==-3){
                clickStatusText.setText("Over Clicked:");
                impressionImage.setImageResource(R.drawable.ic_warning_red_24dp);
            }
        }
        else if(clickRequired==4 || clickRequired==-4){
            clickImage.setImageResource(R.drawable.ic_warning_red_24dp);
            if(clickRequired==-4){
                clickStatusText.setText("Over Clicked:");
                impressionImage.setImageResource(R.drawable.ic_warning_red_24dp);
            }
        }
        else if(clickRequired>4 || clickRequired<-4){
            clickImage.setImageResource(R.drawable.ic_warning_red_24dp);
            impressionImage.setImageResource(R.drawable.ic_warning_red_24dp);
            if(clickRequired<-4){
                clickStatusText.setText("Over Clicked:");
            }
            Toast.makeText(context, "You are violating our policy", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Access wil be unlocked tomorrow", Toast.LENGTH_SHORT).show();
            facebookButton.setEnabled(false);
        }
    }

    public void balance(){
        userBalance=findViewById(R.id.userBalance);
        final String userMail=Paper.book().read(Prevalent.userEmail).toString();
        String url="https://micropenny.rbohost.xyz/balance.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                userBalance.setText("MP: "+response);
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
        MySingleton.getmInstance(HomeActivity.this).addToRequestQueue(request);

    }

    void userName(){
        final String userMail=Paper.book().read(Prevalent.userEmail).toString();
        String url="https://micropenny.rbohost.xyz/name.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavigationView nav_view=findViewById(R.id.nav);
                View header = nav_view.getHeaderView(0);
                TextView userName = header.findViewById(R.id.UserName);
                userName.setText(response);
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
        MySingleton.getmInstance(HomeActivity.this).addToRequestQueue(request);
    }
    void admobBanner(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        View adContainer = findViewById(R.id.adMobView);
        AdView mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(chosenBanner);
        ((RelativeLayout)adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    //Native Ad.....................................
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.

    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private void refreshAd() {


        AdLoader.Builder builder = new AdLoader.Builder(this, ADMOB_AD_UNIT_ID);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                FrameLayout frameLayout =
                        findViewById(R.id.fl_adplaceholder);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                        .inflate(R.layout.ad_unified, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });


        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {


            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());


    }
    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }

    void getImpression(){
        final String userMail=Paper.book().read(Prevalent.userEmail).toString();
        String url="https://micropenny.rbohost.xyz/getimpression.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                impressionText.setText(response);
                impression=Integer.parseInt(response);
                getClick();
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
        MySingleton.getmInstance(HomeActivity.this).addToRequestQueue(request);
    }

    void getClick() {
        final String userMail=Paper.book().read(Prevalent.userEmail).toString();
        String url="https://micropenny.rbohost.xyz/getclick.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                click=Integer.parseInt(response);
                status();
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
        MySingleton.getmInstance(HomeActivity.this).addToRequestQueue(request);
    }

    void checkEmail(){
        final String userMail=Paper.book().read(Prevalent.userEmail).toString();
        String url="https://micropenny.rbohost.xyz/checkEmail.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("no email")){
                    getEmail();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("phone",userMail);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(HomeActivity.this).addToRequestQueue(request);
    }
    void getEmail(){
        email=new EditText(this);
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this)
                .setTitle("Enter your email address")
                .setCancelable(false)
                .setView(email);
               alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveEmail(email.getText().toString());
                    }
                }).create();
        alertDialog.show();
    }
    void saveEmail(final String email){
        final String phone=Paper.book().read(Prevalent.userEmail).toString();
        String url="https://micropenny.rbohost.xyz/saveEmail.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("no email")){
                    getEmail();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Now you can login with this email", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("phone",phone);
                param.put("email",email);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(HomeActivity.this).addToRequestQueue(request);
    }
}
