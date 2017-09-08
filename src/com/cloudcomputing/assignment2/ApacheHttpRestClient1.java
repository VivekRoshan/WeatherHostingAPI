package com.cloudcomputing.assignment2;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * A simple Java REST GET example using the Apache HTTP library.
 * This executes a call against the Yahoo Weather API service, which is
 * actually an RSS service (http://developer.yahoo.com/weather/).
 * 
 * Try this Twitter API URL for another example (it returns JSON results):
 * http://search.twitter.com/search.json?q=%40apple
 * (see this url for more twitter info: https://dev.twitter.com/docs/using-search)
 * 
 * Apache HttpClient: http://hc.apache.org/httpclient-3.x/
 *
 */
public class ApacheHttpRestClient1 {

  public List<Report> forecast() {
    DefaultHttpClient httpclient = new DefaultHttpClient();
    List<Report> list2 = new ArrayList<>();
    try {
      // specify the host, protocol, and port
      HttpHost target = new HttpHost("query.yahooapis.com", 80, "http");
      
      //String str = "https://query.yahooapis.com/v1/public/yql?q=select%20wind%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22chicago%2C%20il%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
      //String str = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
      String str = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22cincinnati%2C%20oh%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
      // specify the get request
      HttpGet getRequest = new HttpGet(str);

      System.out.println("executing request to " + target);

      HttpResponse httpResponse = httpclient.execute(target, getRequest);
      HttpEntity entity = httpResponse.getEntity();

      System.out.println("----------------------------------------");
      System.out.println(httpResponse.getStatusLine());
      Header[] headers = httpResponse.getAllHeaders();
      for (int i = 0; i < headers.length; i++) {
        //System.out.println(headers[i]);
      }
      System.out.println("----------------------------------------");

      if (entity != null) {
        //System.out.println(EntityUtils.toString(entity));
      }
      String s1 = EntityUtils.toString(entity);
      //System.out.println(s1);
      int i = 0;
      i = s1.indexOf("forecast");
      System.out.println(i);
      
      s1 = s1.substring(i);
      int j=0;
      j = s1.indexOf("description");
      s1 = s1.substring(0,j-2);
      
      //System.out.println(s1);
      
      List<String> list = new ArrayList<>();
      
      String[] arr = s1.split(",");
      for (String string : arr) {
    	 
    	String[] strings = string.split(":");
    	string = strings[strings.length-1];
    	list.add(string); 
		//System.out.println(string);
	}
      
      //System.out.println(Integer.parseInt(date1.toString()));
      
      
      i =0;
      while(i < arr.length)
      {
    	  
    	 DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
         DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
         String d =  list.get(i+1);
         
         d = d.substring(1, d.length()-1);
         d = d.replaceAll(" ", "-");
         //System.out.println(d);
         
         Date date = df.parse(d);
         String s = df.format(date);
         String date1 = df1.format(date);
        
        String s2= list.get(i+3);
        s2 = s2.substring(1,s2.length()-1);
    	
    	String s3= list.get(i+4);
        s3 = s3.substring(1,s3.length()-1);
    	
        String Date = date1;
    	
    	Double TMAX = Double.parseDouble(s2);
    	Double TMIN = Double.parseDouble(s3);
    	Report report = new Report(Date, TMAX, TMIN);  
    	list2.add(report);
    	i = i+6;
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // When HttpClient instance is no longer needed,
      // shut down the connection manager to ensure
      // immediate deallocation of all system resources
      httpclient.getConnectionManager().shutdown();
    }
    return list2;
  }
}

