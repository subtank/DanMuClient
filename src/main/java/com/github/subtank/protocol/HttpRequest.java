package com.github.subtank.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {
	
	public static String sendGet(String url, String roomID) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlcomp = url + roomID;
			URL realURL = new URL(urlcomp);
			//connection
			
			URLConnection connection = realURL.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //connected
            //connection.connect();Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
            //for (String key : map.keySet()) {
            //    System.out.println(key + "--->" + map.get(key));
            //}
            // ���� BufferedReader����������ȡURL����Ӧ
            try{
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }catch(IOException e){
            	return null;
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("����GET��������쳣��" + e);
            e.printStackTrace();
        }
        // close I/O
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
	}
}
