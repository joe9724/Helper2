package com.bitekun.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitekun.helper.bean.HelpPeopleListItem;
import com.bitekun.helper.util.Urls;
import com.bitekun.helper.util.Utils;
import com.bumptech.glide.Glide;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


public class HealthPicker extends Activity implements View.OnClickListener {

    ImageView iv_livephoto,iv_recordphoto;
    int REQUEST_IMAGE =1;
    int REQUEST_IMAGE2 =2;
    ArrayList<String> defaultDataArray,getDefaultDataArray2;
    Intent intent;
    Button btn_upload;
    HelpPeopleListItem hp;
    TextView tv_name,tv_sex,tv_age,tv_level,tv_tel,tv_kfxm,tv_kfjg,tv_type;
    EditText et_record;
    RelativeLayout progressbar;

    Spinner sp_kfxm,sp_kfjg;

    String kfxm,kfjg;

    ScrollView sv;

    HashMap<String,List<String>> hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_picker);


        hp = (HelpPeopleListItem) getIntent().getSerializableExtra("item");
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_sex = (TextView)findViewById(R.id.tv_sex);
        tv_age = (TextView)findViewById(R.id.tv_age);
        tv_level = (TextView)findViewById(R.id.tv_level);
        tv_tel = (TextView)findViewById(R.id.tv_tel);
        tv_kfxm = (TextView)findViewById(R.id.tv_kfxm);
        tv_kfjg = (TextView)findViewById(R.id.tv_kfjg);
        tv_type = (TextView)findViewById(R.id.tv_type);

        tv_name.setText(hp.getName());
        tv_sex.setText(hp.getGender().equals("1")?"男":"女");
        tv_age.setText(hp.getAge()+"岁");
        tv_level.setText(hp.getDisableLevel());
        tv_tel.setText(hp.getPhone());
        tv_kfxm.setText(hp.getKfxm());
        tv_kfjg.setText(hp.getKfjg());
        tv_type.setText(hp.getDisableModeId());

        sv = (ScrollView)findViewById(R.id.scroll);
        sv.smoothScrollTo(0,20);

        progressbar = (RelativeLayout)findViewById(R.id.progressbarlayout);

        //康复项目
        sp_kfxm = (Spinner)findViewById(R.id.sp_kfxm);
        sp_kfjg = (Spinner)findViewById(R.id.sp_kfjg);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.kfxm));
        sp_kfxm.setAdapter(adapter);
        sp_kfxm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    kfxm = item.toString();
                    setkfjg(kfxm);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });


        iv_livephoto = (ImageView)findViewById(R.id.iv_livephoto);
        iv_recordphoto = (ImageView)findViewById(R.id.iv_recordphoto);

        iv_livephoto.setOnClickListener(this);
        iv_recordphoto.setOnClickListener(this);
        btn_upload = (Button)findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(this);

        defaultDataArray = new ArrayList<String>();
        getDefaultDataArray2 = new ArrayList<String>();

        ImageView iv_back;
        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        livepath = new ArrayList<String>();
        recordpath = new ArrayList<String>();

        et_record = (EditText)findViewById(R.id.et_record);
        Utils.setProhibitEmoji(et_record);
        //et_record.setFocusable(false);



    }

    private void setkfjg(String kfxm) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Urls.hash+kfxm.trim(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if(Utils.byteArrayToStr(responseBody)!=null)
                {
                    if (Utils.byteArrayToStr(responseBody).equals("error"))
                    {
                        Toasty.success(HealthPicker.this, "发生错误!", Toast.LENGTH_SHORT, true).show();
                        return;
                    }
                    try {
                        JSONArray arr = new JSONArray(Utils.byteArrayToStr(responseBody));
                        if(arr!=null&&arr.length()>0)
                        {
                            ArrayList<String> temp = new ArrayList<String>();
                            for (int i=0;i<arr.length();i++)
                            {
                                temp.add(arr.getJSONObject(i).getString("kfjg"));
                            }
                            String[] stockArr = new String[temp.size()];
                            stockArr = temp.toArray(stockArr);
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(HealthPicker.this,
                                    android.R.layout.simple_spinner_item, stockArr);
                            sp_kfjg.setAdapter(adapter2);
                            sp_kfjg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view,
                                                           int position, long id) {
                                    Object item = adapterView.getItemAtPosition(position);
                                    if (item != null) {
                                        kfjg = item.toString();
                                    }


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    // TODO Auto-generated method stub

                                }
                            });
                        }


                    }catch (Exception e)
                    {
                        //Toasty.error(HealthPicker.this, "err111!"+e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toasty.error(HealthPicker.this, "err222!", Toast.LENGTH_SHORT, true).show();
            }
        });

    }

    Map<String,String> txt;
    ArrayList<String> temp;

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_upload:
                //test is server reached
                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(8000);
                //lists.clear();
                client.get(Urls.signal, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        if(Utils.byteArrayToStr(responseBody)!=null)
                        {
                            if(Utils.byteArrayToStr(responseBody).equals("ok"))
                            {
                                Toasty.success(HealthPicker.this, "网络环境OK!", Toast.LENGTH_SHORT, true).show();
                                //
                                try
                                {
                                    if(kfxm.equals("")||kfxm==null||kfjg.equals("")||kfjg==null||kfxm.length()<4||kfjg.length()<4)
                                    {
                                        //Toast.makeText(HealthPicker.this,"请选择康复项目和康复机构",Toast.LENGTH_SHORT).show();
                                        Toasty.error(HealthPicker.this, "请选择康复项目和康复机构!", Toast.LENGTH_SHORT, true).show();
                                        return;
                                    }
                                }catch (Exception e)
                                {
                                    Toasty.error(HealthPicker.this, "请选择康复项目和康复机构!", Toast.LENGTH_SHORT, true).show();
                                    return;
                                }

                                if (et_record.getText().toString().length()<1)
                                {
                                    Toasty.error(HealthPicker.this, "请填写记录!", Toast.LENGTH_SHORT, true).show();
                                    return;
                                }
                                if (livepath.size()<1||recordpath.size()<1)
                                {
                                    Toasty.error(HealthPicker.this, "需要上传2张照片!", Toast.LENGTH_SHORT, true).show();
                                    return;
                                }
                                txt = new HashMap<String, String>();
                                //txt.put("id",hp.getId());
                                txt.put("dispeopleId",hp.getId());
                                txt.put("idcardNo",hp.getIdCard());
                                txt.put("discardNo",hp.getCardNo());
                                txt.put("serviceId",kfxm);
                                txt.put("agencyId",kfjg);
                                txt.put("accessRecord",et_record.getText().toString().trim());
                                txt.put("latitude",String.valueOf(MainActivity.lat));
                                txt.put("longitude",String.valueOf(MainActivity.lon));
                                txt.put("location",MainActivity.location);
                                txt.put("livePhoto",livepath.get(0));
                                txt.put("recordPhoto",recordpath.get(0));
                                txt.put("name",hp.getName());
                                txt.put("gender",hp.getGender());
                                txt.put("phone",hp.getPhone());
                                txt.put("level",hp.getDisableLevel());
                                txt.put("birth",hp.getBirth());
                                txt.put("pickerId",MyApplication.workerId);
                                txt.put("disableModeId",hp.getDisableModeId());

                                temp = new ArrayList<String>();
                                if(livepath.size()>0)
                                    temp.add(livepath.get(0));
                                if(recordpath.size()>0)
                                    temp.add(recordpath.get(0));
                                new AsyncTask<String, Integer, String>() {
                                    String resultstr;
                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        progressbar.setVisibility(View.VISIBLE);

                                    }

                                    @Override
                                    protected String doInBackground(String... params) {
                                        resultstr = postWithFiles(Urls.prefix+"upload", txt, temp); //此处应该上传2张图片

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(String result) {
                                        super.onPostExecute(result);
                                        progressbar.setVisibility(View.GONE);
                                        if(resultstr==null)
                                        {
                                            Toasty.error(HealthPicker.this, "服务器返回null!", Toast.LENGTH_SHORT, true).show();
                                            return;
                                        }
                                        if(resultstr.equals("notMultipartForm"))
                                        {
                                            Toasty.error(HealthPicker.this, "notMultipartForm!", Toast.LENGTH_SHORT, true).show();
                                        }else if(resultstr.equals("noUploadfile"))
                                        {
                                            Toasty.error(HealthPicker.this, "需要上传2张图片!", Toast.LENGTH_SHORT, true).show();

                                        }
                                        else if(resultstr.equals("error"))
                                        {
                                            Toasty.error(HealthPicker.this, "upsert err!", Toast.LENGTH_SHORT, true).show();

                                        }
                                        else if(resultstr.equals("ok"))
                                        {
                                            Toasty.success(HealthPicker.this, "采集成功!", Toast.LENGTH_SHORT, true).show();
                                            new Handler().postDelayed(new Runnable(){
                                                public void run() {
                                                    finish();
                                                }
                                            }, 2000);
                                        }


                                    }
                                }.execute("");
                            }
                            else
                            {
                                Toasty.success(HealthPicker.this, "连接服务器失败!", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        Toasty.success(HealthPicker.this, "连接服务器failure!", Toast.LENGTH_SHORT, true).show();
                    }
                });



                break;

            case R.id.iv_livephoto:
                //申请readextenalstorage权限
                requestLocation();
                if(canreadpic) {

                    intent = new Intent(this, MultiImageSelectorActivity.class);
// 是否显示调用相机拍照
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
// 最大图片选择数量
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
// 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
// 默认选择图片,回填选项(支持String ArrayList)
                    intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
                    startActivityForResult(intent, REQUEST_IMAGE);
                }else{
                    Toasty.error(HealthPicker.this, "应用需要授权访问存储!", Toast.LENGTH_SHORT, true).show();
                }
                break;

            case R.id.iv_recordphoto:
                requestLocation();
                if(canreadpic) {
                    intent = new Intent(this, MultiImageSelectorActivity.class);
// 是否显示调用相机拍照
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
// 最大图片选择数量
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
// 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
// 默认选择图片,回填选项(支持String ArrayList)
                    intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, getDefaultDataArray2);
                    startActivityForResult(intent, REQUEST_IMAGE2);
                }
                else{
                    Toasty.error(HealthPicker.this, "应用需要授权访问存储!", Toast.LENGTH_SHORT, true).show();
                }
                break;
        }
    }
    public boolean canreadpic = false;
    private void requestLocation() {
        if (PermissionsUtil.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //Toast.makeText(MainActivity.this, "已授权定位权限 开始定位", Toast.LENGTH_LONG).show();
            //sign();
            canreadpic = true;
        } else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    //Toast.makeText(MainActivity.this, "用户授权了定位权限", Toast.LENGTH_LONG).show();
                    //sign();
                    canreadpic = true;
                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                    canreadpic = false;
                    Toast.makeText(HealthPicker.this, "用户拒绝了访问媒体权限,无法选择图片!", Toast.LENGTH_LONG).show();
                }
            }, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
    public static ArrayList<String> livepath,recordpath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                // 获取返回的图片列表
                ArrayList<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                livepath = path;
                // 处理你自己的逻辑 ....
                //Toast.makeText(HealthPicker.this,path.get(0),Toast.LENGTH_SHORT).show();
                //显示选中的图片
                Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                Tiny.getInstance().source(path.get(0)).asFile().withOptions(options).compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile) {
                        if (isSuccess) {
                            Glide.with(HealthPicker.this).load(outfile).placeholder(R.drawable.ic_launcher).error(R.drawable.takephoto).centerCrop().crossFade().into(iv_livephoto);
                            livepath.clear();
                            livepath.add(outfile);
                        }
                    }
                });



            }
        }else if(requestCode == REQUEST_IMAGE2){
            if(resultCode == RESULT_OK){
                // 获取返回的图片列表
                ArrayList<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                recordpath = path;
                // 处理你自己的逻辑 ....
                //Toast.makeText(HealthPicker.this,path.get(0),Toast.LENGTH_SHORT).show();
                Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                Tiny.getInstance().source(path.get(0)).asFile().withOptions(options).compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile) {
                        if (isSuccess) {
                            Glide.with(HealthPicker.this).load(outfile).placeholder(R.drawable.ic_launcher).error(R.drawable.takephoto).centerCrop().crossFade().into(iv_recordphoto);
                            recordpath.clear();
                            recordpath.add(outfile);
                        }
                    }
                });

            }
        }
    }

    //
    public String postWithFiles(String actionUrl, Map<String, String> textParams, List<String> filePaths) {

        try
        {
          /*  if (android.os.Build.VERSION.SDK_INT > 9) {
                try
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                catch(Exception e)
                {
                    Toasty.error(HealthPicker.this, "err0!"+e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }

            }*/
            try {
                final String BOUNDARY = UUID.randomUUID().toString();
                final String PREFIX = "--";
                final String LINE_END = "\r\n";

                final String MULTIPART_FROM_DATA = "multipart/form-data";
                final String CHARSET = "UTF-8";

                URL uri = new URL(actionUrl);
                HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

                //�����С
                conn.setChunkedStreamingMode(1024 * 64);
                //��ʱ
                conn.setReadTimeout(5 * 1000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");

                conn.setRequestProperty("connection", "keep-alive");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

                // ƴ���ı����͵Ĳ���
                StringBuilder textSb = new StringBuilder();
                if (textParams != null) {
                    for (Map.Entry<String, String> entry : textParams.entrySet()) {
                        textSb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                        textSb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
                        textSb.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END);
                        textSb.append("Content-Transfer-Encoding: 8bit" + LINE_END);
                        textSb.append(LINE_END);
                        textSb.append(entry.getValue());
                        textSb.append(LINE_END);
                    }
                }

                DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
                outStream.write(textSb.toString().getBytes());

                //����POST��ʽ
                //outStream.write("userId=1&cityId=26".getBytes());

                // �����ļ�����
                if (filePaths != null) {
                    for (String file : filePaths) {
                        StringBuilder fileSb = new StringBuilder();
                        fileSb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                        fileSb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" +
                                file.substring(file.lastIndexOf("/") + 1) + "\"" + LINE_END);
                        fileSb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                        fileSb.append(LINE_END);
                        outStream.write(fileSb.toString().getBytes());

                        InputStream is = new FileInputStream(file);
                        byte[] buffer = new byte[1024 * 8];
                        int len;
                        while ((len = is.read(buffer)) != -1) {
                            outStream.write(buffer, 0, len);
                        }

                        is.close();
                        outStream.write(LINE_END.getBytes());
                    }
                }

                // ���������־
                outStream.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes());
                outStream.flush();

                // �õ���Ӧ��
                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));

                StringBuilder resultSb = null;
                String line;
                if (responseCode == 200) {
                    resultSb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        resultSb.append(line);
                    }
                }

                br.close();
                outStream.close();
                conn.disconnect();

                return resultSb == null ? null : resultSb.toString();
            } catch (IOException e) {
                Toasty.error(HealthPicker.this, "err!"+e.getMessage(), Toast.LENGTH_SHORT, true).show();
                return null;
                //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e)
        {
            Toasty.error(HealthPicker.this, "提交发生错误了err!"+e.getMessage(), Toast.LENGTH_SHORT, true).show();
        }

        return null;

    }



}
