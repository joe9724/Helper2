package com.bitekun.helper.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;

public class NetGet
{
    String url;
    
    // HashMap<String, String> params;
    
    public NetGet(String _url)
    {
        // params = _params;
        url = _url;
        
    }
    
    public String doGet()
    {
        String resultString = null;
        try
        {
            resultString = (String)getRequest(url, new DefaultHttpClient(new BasicHttpParams()));
            
        }
        catch (Exception e)
        {
            System.out.println("err:" + e.getMessage());
        }
        
        return resultString;
        
    }
    
    public String doGetNoPass()
    {
        String resultString = null;
        try
        {
            resultString = (String)getRequest(url, new DefaultHttpClient(new BasicHttpParams()));
            
        }
        catch (Exception e)
        {
            System.out.println("err:" + e.getMessage());
        }
        return resultString;
    }
    
    protected static String retrieveInputStream(HttpEntity httpEntity)
    {
        
        int length = (int)httpEntity.getContentLength();
        
        // the number of bytes of the content, or a negative number if unknown.
        // If the content length is known but exceeds Long.MAX_VALUE, a negative
        // number is returned.
        
        // length==-1��������䱨��println needs a message
        
        if (length < 0)
            length = 10000;
        
        StringBuffer stringBuffer = new StringBuffer(length);
        
        try
        {
            
            InputStreamReader inputStreamReader = new InputStreamReader(httpEntity.getContent(), HTTP.UTF_8);
            
            char buffer[] = new char[length];
            
            int count;
            
            while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0)
            {
                
                stringBuffer.append(buffer, 0, count);
                
            }
            
        }
        catch (UnsupportedEncodingException e)
        {
            
        }
        catch (IllegalStateException e)
        {
            
        }
        catch (IOException e)
        {
            
        }
        
        return stringBuffer.toString();
        
    }
    
    protected static String getRequest(String url, DefaultHttpClient client)
        throws Exception
    {
        
        String result = null;
        
        // int statusCode = 0;
        
        HttpGet getMethod = new HttpGet(url);
        System.out.println("ͶƱ������ܺ�url��:" + url);
        try
        {
            
            // getMethod.setHeader("User-Agent", USER_AGENT);
            
            HttpResponse httpResponse = client.execute(getMethod);
            
            // statusCode == 200 ����
            
            // statusCode = httpResponse.getStatusLine().getStatusCode();
            
            // �����ص�httpResponse��Ϣ
            
            result = retrieveInputStream(httpResponse.getEntity());
            
        }
        catch (Exception e)
        {
            
            throw new Exception(e);
            
        }
        finally
        {
            
            getMethod.abort();
            
        }
        
        return result;
        
    }
    
}
