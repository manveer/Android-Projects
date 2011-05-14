/**
 * 
 */
package com.playground.farecalculator.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Manveer Chawla (manveer.chawla@gmail.com)
 *
 */
public class URLUtils
{
	public static String getUrl(String urlString) throws IOException
	{
		URL url = new URL(urlString);
		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setReadTimeout(60000);
		connection.setRequestMethod("GET");
		connection.connect();
		
		int responseCode = connection.getResponseCode();
		if(responseCode == -1)
			return null;
		
		InputStream is = connection.getInputStream();
		boolean done = false;
		byte[] data = new byte[256];
		int totalBytesRead = 0;
		do
		{
			int result = is.read(data, totalBytesRead, data.length-totalBytesRead);
			if(result != -1) // still more to go
			{
				totalBytesRead += result;
				if(totalBytesRead == data.length)
				{
					// grow array
					byte[] temp = new byte[2*data.length];
					System.arraycopy(data, 0, temp, 0, data.length);
					data = temp;
				}
			}
			else
			{
				byte[] temp = new byte[totalBytesRead];
				System.arraycopy(data, 0, temp, 0, totalBytesRead);
				data = temp;
				done = true;
			}
		}while(!done);
		if(is != null)
			is.close();
		return new String(data);
	}
}
