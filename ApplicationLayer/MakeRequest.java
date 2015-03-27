package ApplicationLayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

public class MakeRequest {
	public MakeRequest(){
		
	}
	public String sendRequest() {
        try { 
        	  String url = "http://127.0.0.1:8888/NTUServer.php";
  
      		URL obj = new URL(url);
      		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
     		con.setRequestMethod("POST");
      		con.setDoOutput(true);
      		int responseCode = con.getResponseCode();
      		System.out.println("\nSending 'GET' request to URL : " + url);
      		System.out.println("Response Code : " + responseCode);
      		System.out.println(con.getHeaderField("link"));
      		return con.getHeaderField("link");
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }
}

