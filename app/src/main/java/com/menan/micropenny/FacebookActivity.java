package com.menan.micropenny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColonyAppOptions;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.ads.mediation.adcolony.AdColonyMediationAdapter;
import com.google.ads.mediation.appfireworks.AppfireworksAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.menan.micropenny.Prevalent.Prevalent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;

public class FacebookActivity extends AppCompatActivity {
    public WebView facebookWeb;
    private int countSec=0,countMin=0;
    private int nextPennySec=1,nextPennyMin=2;
    private float updateBalance;
    private boolean loop=true;
    TextView spendTime,nextPenny,userBalance;
    private RewardedAd rewardedAd;
    int midTime=1;
    int mxTime=2;

    public ProgressDialog loadingBar;
    private InterstitialAd mInterstitialAdVideo;
    private InterstitialAd mInterstitialAdNormal;
    private InterstitialAd mInterstitialXAdNormal;

    private String referred=null;
    private float referredBalance;


    int randomNumber;

    private int microAdTime=0;
    private int avgAdTime=0;

    private boolean microAdTimer=false;
    private boolean avgAdTimer=false;
    private boolean rewarded=false;

    private int click;
    private int impression;

    RequestQueue rq;

    private boolean debugMode=true;

    private String interstitialNormal0="ca-app-pub-4671840559976910/9418562937";
    //private String interstitialNormal0="ca-app-pub-4671840559976910/5381969792";
    //private String interstitialNormal1="ca-app-pub-2989090493512506/2477055532";
   private String interstitialNormal1="ca-app-pub-2989090493512506/4135436347";
    //private String interstitialNormal2="ca-app-pub-6811777494326927/9415683187";
   private String interstitialNormal2="ca-app-pub-6811777494326927/3041846526";
   private String interstitialNormal3="ca-app-pub-9451842569113287/4315844252";
    private String interstitialVideo0="ca-app-pub-4671840559976910/9418562937";
    private String interstitialVideo1="ca-app-pub-2989090493512506/2477055532";
    private String interstitialVideo2="ca-app-pub-6811777494326927/9415683187";
    private String interstitialVideo3="ca-app-pub-9451842569113287/8063517575";
    private String reward0="ca-app-pub-4671840559976910/8670478111";
    private String reward1="ca-app-pub-2989090493512506/6224728858";
    private String reward2="ca-app-pub-6811777494326927/8750819843";
    private String reward3="ca-app-pub-9451842569113287/5816628181";


    //Main Function Start................................................................................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        //Starts from here...................................
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        rq= Volley.newRequestQueue(this);
        if(debugMode){
           // nextPennyMin=2;
        }
        AppfireworksAdapter.setHasUserConsent(false);

        AdColonyAppOptions appOptions = AdColonyMediationAdapter.getAppOptions();
        appOptions.setGDPRConsentString("1");
        appOptions.setGDPRRequired(true);

        loadingBar =new ProgressDialog(this);
        getTime();
        loadInterstitialNormal();
        loadRewardAd();
        updateBalance();
        loadFacebook();
        clock();
        loadInterstitialVideo();
        //MediationTestSuite.launch(this);


    }
    //Main Function End..................................................................................................................................


    void randomGenerator(){
        Random r = new Random();
        randomNumber = r.nextInt(4);
    }

    //Google Admob Start..................................................................................................................................
    public void loadRewardAd(){
        randomGenerator();
        if(debugMode){
            rewardedAd = new RewardedAd(this,"ca-app-pub-3940256099942544/5224354917");
        }
        else if(randomNumber==0) {
            rewardedAd = new RewardedAd(this,reward0);
        }
        else if(randomNumber==1){
            rewardedAd = new RewardedAd(this,reward1);
        }
        else if(randomNumber==2){
            rewardedAd = new RewardedAd(this,reward2);
        }
        else if(randomNumber==3){
            rewardedAd = new RewardedAd(this,reward3);
        }

        RewardedAdLoadCallback callback=new RewardedAdLoadCallback(){
            @Override
            public void onRewardedAdFailedToLoad(int i) {
                super.onRewardedAdFailedToLoad(i);
                Toast.makeText(FacebookActivity.this, "Big Penny Not Found", Toast.LENGTH_SHORT).show();
                Toast.makeText(FacebookActivity.this, "We recommend to use a standard VPN", Toast.LENGTH_SHORT).show();
                loadInterstitialVideo();
            }

            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();
            }
        };
        this.rewardedAd.loadAd(new AdRequest.Builder().build(),callback);

    }
    public void showRewardAd(){
        if(this.rewardedAd.isLoaded()){
            RewardedAdCallback callback=new RewardedAdCallback() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    rewarded=true;
                }

                @Override
                public void onRewardedAdClosed() {
                    super.onRewardedAdClosed();
                    if(rewarded){
                        addPenny("maxReward");

                        Toast.makeText(FacebookActivity.this, "Big penny added successfully", Toast.LENGTH_SHORT).show();
                        rewarded=false;
                    }
                    loadRewardAd();
                }
                @Override
                public void onRewardedAdOpened() {
                    super.onRewardedAdOpened();
                    setImpression();
                }

                @Override
                public void onRewardedAdFailedToShow(int i) {
                    super.onRewardedAdFailedToShow(i);
                }
            };
            rewardedAd.show(this, callback);
        }
    }

    @Override
    public void onBackPressed() {
        if(facebookWeb.canGoBack()){
            facebookWeb.goBack();
        }
        else{
            finish();
            loop=false;
            Intent intent=new Intent(FacebookActivity.this, HomeActivity.class);
            startActivity(intent);
        }

    }
    void loadInterstitialNormal(){
        mInterstitialAdNormal = new InterstitialAd(this);
        randomGenerator();
        if(debugMode){
            mInterstitialAdNormal.setAdUnitId("ca-app-pub-3940256099942544/8691691433");
        }
        else if(randomNumber==0) {
            mInterstitialAdNormal.setAdUnitId(interstitialNormal0);
        }
        else if(randomNumber==1){
            mInterstitialAdNormal.setAdUnitId(interstitialNormal1);
        }
        else if(randomNumber==2){
            mInterstitialAdNormal.setAdUnitId(interstitialNormal2);
        }
        else if(randomNumber==3){
            mInterstitialAdNormal.setAdUnitId(interstitialNormal3);
        }

        mInterstitialAdNormal.loadAd(new AdRequest.Builder().build());
        mInterstitialAdNormal.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Toast.makeText(FacebookActivity.this, "Micro Penny Not Found", Toast.LENGTH_SHORT).show();
                Toast.makeText(FacebookActivity.this, "We recommend to use a standard VPN", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Toast.makeText(FacebookActivity.this, "Spend 5 second to avoid invalid activity", Toast.LENGTH_SHORT).show();
                microAdTimer=true;
                setImpression();
            }

            @Override
            public void onAdClicked() {
                setClick();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                microAdTimer=false;
                if(microAdTime>=5){
                    Toast.makeText(FacebookActivity.this, "Micro penny added successfully", Toast.LENGTH_SHORT).show();
                    addPenny("minReward");
                    microAdTime=1;
                }
                else{
                    Toast.makeText(FacebookActivity.this, "Invalid activity detected", Toast.LENGTH_SHORT).show();
                    microAdTime=1;
                }
                loadInterstitialNormal();
            }
        });
    }
    void showInterstitialNormal(){
        if(mInterstitialAdNormal.isLoaded()){
            mInterstitialAdNormal.show();
        }
        else{
            loadInterstitialNormal();
        }
    }

    void loadInterstitialVideo(){
        mInterstitialAdVideo = new InterstitialAd(this);
        randomGenerator();
        if(debugMode){
            mInterstitialAdVideo.setAdUnitId("ca-app-pub-3940256099942544/8691691433");
        }
        else if(randomNumber==0) {
            mInterstitialAdVideo.setAdUnitId(interstitialVideo0);
        }
        else if(randomNumber==1){
            mInterstitialAdVideo.setAdUnitId(interstitialVideo1);
        }
        else if(randomNumber==2){
            mInterstitialAdVideo.setAdUnitId(interstitialVideo2);
        }
        else if(randomNumber==3){
            mInterstitialAdVideo.setAdUnitId(interstitialVideo3);
        }
        mInterstitialAdVideo.loadAd(new AdRequest.Builder().build());
        mInterstitialAdVideo.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Toast.makeText(FacebookActivity.this, "Average Penny Not Found", Toast.LENGTH_SHORT).show();
                Toast.makeText(FacebookActivity.this, "We recommend to use a standard VPN", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Toast.makeText(FacebookActivity.this, "Spend at least 10 second to avoid invalid activity", Toast.LENGTH_SHORT).show();
                avgAdTimer=true;
                setImpression();
            }

            @Override
            public void onAdClicked() {
                setClick();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                avgAdTimer=false;
                if(avgAdTime>=10){
                    addPenny("avgReward");
                    Toast.makeText(FacebookActivity.this, "Average penny added successfully", Toast.LENGTH_SHORT).show();
                    avgAdTime=1;
                }
                else{
                    Toast.makeText(FacebookActivity.this, "Invalid activity detected", Toast.LENGTH_SHORT).show();
                    avgAdTime=1;
                }
                loadInterstitialVideo();
            }
        });
    }
    void showInterstitialVideo(){
        if (mInterstitialAdVideo.isLoaded()) {
            mInterstitialAdVideo.show();
        }
        else{
            loadInterstitialVideo();
        }
    }
    //Google Admob End..................................................................................................................................

    //Start loading while changing Regular to Basic.....................................................................................................
    @JavascriptInterface
    public void startLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingBar.setTitle("Going to Basic Mode");
                loadingBar.setMessage("Please wait....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        });

    }
    //Force to Basic View................................................................................................................................
    void removeRegularMode(){
        facebookWeb.loadUrl(
                "javascript:(function() { " +
                        "var checkPoint=document.getElementById('checkpointSubmitButton-actual-button');\n" +
                        "if(!checkPoint){\n" +
                        "\tvar loginCheck=document.getElementById('oauth_login_button_container');\n" +
                        "if(!loginCheck){\n" +
                        "var isbasic=document.getElementById('objects_container');\n" +
                        "if(!isbasic){\n" +
                        "android.startLoading();"+
                        "location.href='https://m.facebook.com/settings/site';\n" +
                        "var btn=document.getElementsByName('basic_site_devices');\n" +
                        "    btn[1].checked=true;\n" +
                        "var saveBtn = document.getElementsByClassName(\"btn btnC largeBtn mfsl touchable\");\n" +
                        "    saveBtn[0].click();\n" +
                        "}\n" +
                        "}\n" +
                        "}"
                        +
                        "})()");
    }

    //Load Facebook Webview.............................................................................................................................
    void loadFacebook(){
        facebookWeb=findViewById(R.id.facebookWeb);
        facebookWeb.getSettings().setJavaScriptEnabled(true);
        facebookWeb.addJavascriptInterface(this,"android");
        facebookWeb.setWebViewClient(new WebViewClient());
        facebookWeb.loadUrl("https://m.facebook.com/");

        facebookWeb.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                lodeBanner();
                //if(url.contains("facebook.com")){
                    //removeRegularMode();
                //}
                //if(loadingBar.isShowing()){
                    //loadingBar.dismiss();
                //}
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (Uri.parse(url).getScheme().equals("market")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        Activity host = (Activity) view.getContext();
                        host.startActivity(intent);
                        return true;
                    } catch (ActivityNotFoundException e) {
                        // Google Play app is not installed, you may want to open the app store link
                        Uri uri = Uri.parse(url);
                        view.loadUrl("http://play.google.com/store/apps/" + uri.getHost() + "?" + uri.getQuery());
                        return false;
                    }

                }
                return false;
            }
        });
    }

    //Facebook Banner Add...................................................................................................................................
    void lodeBanner(){
        facebookWeb.loadUrl(
                "javascript:(function() { " +
                        "document.getElementById('m-top-of-feed').innerHTML='<h2 align=\"center\">Announcements</h2>\n" +
                        "<p align=\"center\">Dear User,</p>\n" +
                        "<p align=\"center\">Like our Fb Page:\n" +
                        "<a href=\"https://www.facebook.com/micropenny.info\">Like</a>\n" +
                        "</p>\n" +
                        "<p align=\"center\">Join our Fb Group:\n" +
                        "<a href=\"https://www.facebook.com/groups/2663968257020153\">Join</a>\n" +
                        "</p><br>'"
                        +
                        "})()");
        facebookWeb.loadUrl(
                "javascript:(function() { " +
                        "var myFrames=document.getElementsByName('framebla');" +
                        "for(var i = 0; i < myFrames.length; i++)" +
                        "{" +
                        "myFrames[i].parentNode.removeChild(myFrames[i]);" +
                        "}" +
                        "var story=document.getElementsByTagName('article');" +
                        "var ifrm = document.createElement(\"iframe\");" +
                        "ifrm.setAttribute(\"src\", \"https://micropenny.000webhostapp.com/Admob1.html\");" +
                        "ifrm.setAttribute(\"scrolling\",\"no\");" +
                        "ifrm.setAttribute(\"name\",\"framebla\");" +
                        "ifrm.style.width = \"100%\";" +
                        "ifrm.style.height = \"70px\";" +
                        "ifrm.style.border=\"0px #000000 solid\";" +
                        "var length=story.length;" +
                        "story[length-1].appendChild(ifrm);" +
                        "})()");
        facebookWeb.loadUrl(
                "javascript:(function() { " +
                        "var myFrames=document.getElementsByName('framebla1');" +
                        "for(var i = 0; i < myFrames.length; i++)" +
                        "{" +
                        "myFrames[i].parentNode.removeChild(myFrames[i]);" +
                        "}" +
                        "var story=document.getElementsByTagName('article');" +
                        "var ifrm = document.createElement(\"iframe\");" +
                        "ifrm.setAttribute(\"src\", \"https://micropenny.000webhostapp.com/Admob.html\");" +
                        "ifrm.setAttribute(\"scrolling\",\"no\");" +
                        "ifrm.setAttribute(\"name\",\"framebla1\");" +
                        "ifrm.style.width = \"100%\";" +
                        "ifrm.style.height = \"70px\";" +
                        "ifrm.style.border=\"0px #000000 solid\";" +
                        "var length=story.length;" +
                        "story[length-2].appendChild(ifrm);" +
                        "})()");
        facebookWeb.loadUrl(
                "javascript:(function() { " +
                        "var myFrames=document.getElementsByName('framebla2');" +
                        "for(var i = 0; i < myFrames.length; i++)" +
                        "{" +
                        "myFrames[i].parentNode.removeChild(myFrames[i]);" +
                        "}" +
                        "var story=document.getElementsByTagName('article');" +
                        "var ifrm = document.createElement(\"iframe\");" +
                        "ifrm.setAttribute(\"src\", \"https://micropenny.000webhostapp.com/Admob2.html\");" +
                        "ifrm.setAttribute(\"scrolling\",\"no\");" +
                        "ifrm.setAttribute(\"name\",\"framebla2\");" +
                        "ifrm.style.width = \"100%\";" +
                        "ifrm.style.height = \"70px\";" +
                        "ifrm.style.border=\"0px #000000 solid\";" +
                        "var length=story.length;" +
                        "story[length-3].appendChild(ifrm);" +
                        "})()");
    }


    //Time Controller...................................................................................................................................
    void clock(){
        nextPenny=findViewById(R.id.nextPenny);
        spendTime=findViewById(R.id.spendTime);
        Thread t=new Thread(){
            @Override
            public void run(){
                while (loop){
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(microAdTimer){
                                    microAdTime++;
                                    if(microAdTime==6){
                                        Toast.makeText(FacebookActivity.this, "You can close now", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if(avgAdTimer){
                                    avgAdTime++;
                                    if(avgAdTime==11){
                                        Toast.makeText(FacebookActivity.this, "You can close now", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                nextPennySec--;
                                if(nextPennySec==0){
                                    if(nextPennyMin==0){
                                        if(rewardedAd.isLoaded()){
                                            if(!vpn()){
                                                Toast.makeText(FacebookActivity.this, "VPN Disconnected", Toast.LENGTH_SHORT).show();
                                                finishAffinity();
                                                loop=false;
                                                Intent intent=new Intent(FacebookActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                showRewardAd();
                                            }
                                            nextPennyMin=mxTime;
                                        }
                                        else if(mInterstitialAdVideo.isLoaded()){
                                            if(!vpn()){
                                                Toast.makeText(FacebookActivity.this, "VPN Disconnected", Toast.LENGTH_SHORT).show();
                                                finishAffinity();
                                                loop=false;
                                                Intent intent=new Intent(FacebookActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                showInterstitialVideo();
                                                loadRewardAd();
                                            }

                                            nextPennyMin=midTime;
                                        }
                                        else{
                                            loadRewardAd();
                                            loadInterstitialVideo();
                                            nextPennyMin=2;
                                        }
                                    }
                                    nextPennySec=60;
                                    nextPennyMin-=1;
                                }
                                nextPenny.setText("Next Penny in "+nextPennyMin+":"+nextPennySec);
                                countSec++;
                                if(countSec==60){
                                    if(!vpn()){
                                        Toast.makeText(FacebookActivity.this, "VPN Disconnected", Toast.LENGTH_SHORT).show();
                                        finishAffinity();
                                        loop=false;
                                        Intent intent=new Intent(FacebookActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else if((countMin%2)==0){
                                        showInterstitialNormal();
                                    }
                                    lodeBanner();
                                    countSec=0;
                                    countMin++;
                                }
                                spendTime.setText("T "+countMin+":"+ countSec);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    //Update User Balance..........................................................................................................
    void updateBalance(){
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
        MySingleton.getmInstance(FacebookActivity.this).addToRequestQueue(request);
    }

    //Add Penny In User Balance......................................................................................................
    void addPenny(final String rd){
        final String userMail=Paper.book().read(Prevalent.userEmail).toString();
        String url="https://micropenny.rbohost.xyz/addpenny.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                userBalance.setText("MP: "+response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FacebookActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",userMail);
                param.put("reward",rd);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(FacebookActivity.this).addToRequestQueue(request);
    }

    //Get Reward Value.................................................................................................................
    void getTime(){
        String url="https://micropenny.rbohost.xyz/getTime.php";
        JsonArrayRequest jsonArray=new JsonArrayRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject person = (JSONObject) response.get(i);
                        midTime=Integer.parseInt(person.getString("avgTime"));
                        mxTime=Integer.parseInt(person.getString("maxTime"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(jsonArray);
    }

    void setClick(){
        final String userMail=Paper.book().read(Prevalent.userEmail).toString();
        String url="https://micropenny.rbohost.xyz/setClick.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
        MySingleton.getmInstance(FacebookActivity.this).addToRequestQueue(request);
    }


    void setImpression(){
        final String userMail=Paper.book().read(Prevalent.userEmail).toString();
        String url="https://micropenny.rbohost.xyz/setImpression.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("s")){
                    getImpression();
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
                param.put("email",userMail);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(FacebookActivity.this).addToRequestQueue(request);
    }

    void getImpression(){
        final String userMail=Paper.book().read(Prevalent.userEmail).toString();
        String url="https://micropenny.rbohost.xyz/getimpression.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
        MySingleton.getmInstance(FacebookActivity.this).addToRequestQueue(request);
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
        MySingleton.getmInstance(FacebookActivity.this).addToRequestQueue(request);
    }

    void updateStatus(final int status){
        final String userMail=Paper.book().read(Prevalent.userEmail).toString();
        String url="https://micropenny.rbohost.xyz/updateStatus.php";
        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                param.put("status",String.valueOf(status));
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(FacebookActivity.this).addToRequestQueue(request);
    }

    void status(){
        int clickRequired=0;
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
        if(clickRequired==0){
            updateStatus(1);
        }
        else if(clickRequired==1 || clickRequired==-1){
            updateStatus(0);
        }
        else if(clickRequired==2 || clickRequired==-2){
            updateStatus(-1);
        }
        else if(clickRequired==3 || clickRequired==-3){
            updateStatus(-1);
        }
        else if(clickRequired==4 || clickRequired==-4){
            updateStatus(-2);
        }
        else if(clickRequired>4 || clickRequired<-4){
            updateStatus(-3);
        }

    }
    public boolean vpn() {
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if ( networkInterface.toString().contains("tun")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        return false;
    }
}
