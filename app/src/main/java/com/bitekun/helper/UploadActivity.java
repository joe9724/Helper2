package com.bitekun.helper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.bitekun.helper.R;

public class UploadActivity extends Activity {

	EditText et_username, et_password;
	Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);

		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);

		btn_login = (Button) findViewById(R.id.btn_login);

		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				

			}
		});

	}
	
	/** 
	 * ʹ��HttpURLConnectionͨ��POST��ʽ�ύ���󣬲��ϴ��ļ��� 
	 * 
	 * @param actionUrl  ���ʵ�url 
	 * @param textParams �ı����͵�POST����(key:value) 
	 * @param filePaths  �ļ�·���ļ��� 
	 * @return ���������ص����ݣ������쳣ʱ���� null 
	 */  
	public static String postWithFiles(String actionUrl, Map<String, String> textParams, List<String> filePaths) {  
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
	                resultSb.append(line).append("\n");  
	            }  
	        }  
	  
	        br.close();  
	        outStream.close();  
	        conn.disconnect();  
	  
	        return resultSb == null ? null : resultSb.toString();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	  
	    return null;  
	}  

}
