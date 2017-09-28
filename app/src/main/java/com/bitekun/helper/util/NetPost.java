package com.bitekun.helper.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class NetPost
{

	String url;

	Map<String, String> params;

	HttpClient httpclient;// = new DefaultHttpClient();

	String result = "su";

	public NetPost(Map<String, String> _params, String _url)
	{
		params = _params;
		
		url = _url;
		httpclient = new DefaultHttpClient();
	}

	@SuppressWarnings("rawtypes")
	public String doPost()
	{

		HttpPost httppost = new HttpPost(url);
		try
		{
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			Set set = params.entrySet();
			Iterator it = set.iterator();
			while (it.hasNext())
			{
				Map.Entry me = (Map.Entry) it.next();
				nameValuePairs.add(new BasicNameValuePair(me.getKey()
						.toString(), me.getValue().toString()));

				System.out.println(me.getKey() + ":" + me.getValue() + ":"
						+ me.hashCode());

			}

			/*
			 * nameValuePairs .add(new BasicNameValuePair("u_username",
			 * "88@88.com")); nameValuePairs.add(new
			 * BasicNameValuePair("u_password", "88"));
			 */

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httppost);
			result = retrieveInputStream(response.getEntity());
			return result;

		} catch (ClientProtocolException e)
		{
			return "error2" + e.getMessage();
		} catch (IOException e)
		{
			return "error3" + e.getMessage();
		}

	}

	public static String retrieveInputStream(HttpEntity httpEntity)
	{

		int length = (int) httpEntity.getContentLength();

		// the number of bytes of the content, or a negative number if unknown.
		// If the content length is known but exceeds Long.MAX_VALUE, a negative
		// number is returned.

		// length==-1��������䱨��println needs a message

		if (length < 0)
			length = 10000;

		StringBuffer stringBuffer = new StringBuffer(length);

		try
		{

			InputStreamReader inputStreamReader = new InputStreamReader(
					httpEntity.getContent(), HTTP.UTF_8);

			char buffer[] = new char[length];

			int count;

			while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0)
			{

				stringBuffer.append(buffer, 0, count);

			}

		} catch (UnsupportedEncodingException e)
		{

		} catch (IllegalStateException e)
		{

		} catch (IOException e)
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

		} catch (Exception e)
		{

			throw new Exception(e);

		} finally
		{

			getMethod.abort();

		}

		return result;

	}

}
