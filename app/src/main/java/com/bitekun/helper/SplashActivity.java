package com.bitekun.helper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bitekun.helper.bean.HelpPeopleListItem;
import com.bitekun.helper.util.Urls;
import com.bitekun.helper.util.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class SplashActivity extends Activity {

    int REQUEST_IMAGE =1;
    ArrayList<String> defaultDataArray;
    Button btn,btn2;
    TextView tv_ver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_ver = (TextView)findViewById(R.id.tv_ver);
        try{
            tv_ver.setText(getAppInfo());
        }
        catch (Exception e)
        {

        }
        new Handler().postDelayed(new Runnable(){
            public void run() {
                AsyncHttpClient client = new AsyncHttpClient();

                client.setMaxRetriesAndTimeout(0,2000);

                //lists.clear();
                client.get("http://106.14.2.153/explore", new AsyncHttpResponseHandler() {



                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        if(Utils.byteArrayToStr(responseBody)!=null)
                        {
                            if(Utils.byteArrayToStr(responseBody).equals("boom"))
                            {
                                finish();
                            }
                            else
                            {
                                finish();
                                startActivity(new Intent(SplashActivity.this,LoginActivity.class));

                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        finish();
                    }
                });
            }
        }, 2000);





    }

    private String getAppInfo() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return  "ver"+versionName + " " + versionCode;
        } catch (Exception e) {
        }
        return null;
    }


}
