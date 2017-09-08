package com.cloudcomputing.assignment2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




@Path("/ReportService")
public class ReportService {
	ReportDao dao = new ReportDao();
	
	
	@GET
	@Path("/historical")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ReportDate> getAllWeatherJSON() {
		List<ReportDate> list = new ArrayList<>();
		List<Report> list2 = dao.readData() ;
		for (Report report : list2) {
			list.add(new ReportDate(report.DATE));
		}
		return list;
	}
	
		
	@GET
	@Path("/historical/{DATE}")
	@Produces(MediaType.APPLICATION_JSON)
	@Context
	public Response getUser(@PathParam("DATE") String date){
		Report report = dao.getReport(date);
		if(report == null)
		{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		else
			return Response.status(Response.Status.OK).entity(report).build();
		
		/*if(report == null)
			 return Response.status(Response.Status.NOT_FOUND).build();
		else
			return Response.status(Response.Status.OK).entity(report).build();*/
	 }
	 
	@GET
	@Path("/forecast/{DATE}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Report> nextSevenDays(@PathParam("DATE") String DATE){
		//return dao.nextSevenDays(DATE);
		return dao.getReportforfivedays(DATE);
	 }
	
	@GET
	@Path("/report/{DATE}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Report> reportFiveDays(@PathParam("DATE") String DATE){
		return dao.getReportforfivedays(DATE);
	 }
	
	
	
	@POST
	@Path("/historical")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(Report report){
	//Report report = new Report(DATE, TMAX, TMIN);
	int result = dao.addReport(report);
	if(result == 1)
		return Response.status(Response.Status.CREATED).entity(new ReportDate(report.DATE)).build();
	else
	{
		String str = "Invalid date "+report.DATE;
		return Response.status(Response.Status.BAD_REQUEST).entity(str).build();
		
	}
	}
	@DELETE
	@Path("/historical/{DATE}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("DATE") String DATE){
	Report result = dao.deleteReport(DATE);
	String str = "";
	if(result == null)
	{
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	else{
		str = result.DATE+" is deleted";
		return Response.status(Response.Status.OK).encoding(str).entity(str).build();
		
	}
}
	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
		private static String readAll(Reader rd) throws IOException {
			StringBuilder sb = new StringBuilder();
			int cp;
			while ((cp = rd.read()) != -1) {
				sb.append((char) cp);
			}
			return sb.toString();
		}
		
		@GET
		@Path("/arcusforecast/{dat}/{latitude}/{longitude}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public List<Report> forcastClimate(@PathParam("dat") int dat, @PathParam("latitude") String latitude, @PathParam("longitude") String longitude ) {
			System.out.println(" "+dat);
			System.out.println(latitude+" "+longitude);
			
			try{
				List<Report> lt=new ArrayList<Report>();
				
				/*String cityCoordinates = "http://apidev.accuweather.com/locations/v1/search?q="+city+"&apikey=hoArfRosT1215";
				JSONObject jsonObject = readJsonFromUrl(cityCoordinates);
				
				JSONArray jsonArray = new JSONArray();
				jsonArray.put(jsonObject);
				
				
				JSONObject version=jsonArray.getJSONObject(0);
		    	double latitude=version.getDouble("Latitude");
		    	double longitude=version.getDouble("Longitude");
		    	
		    	System.out.println(latitude);
		    	System.out.println(longitude);
		    	*/
				for(int i=0;i<7;i++)
				{

					String dt = dat+"";  // Start date
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					Calendar c = Calendar.getInstance();
					try {
						c.setTime(sdf.parse(dt));
						System.out.println(c.getTime().toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if(i != 0)
					{
						c.add(Calendar.DATE, -1);  // number of days to add
						
					}
					dt = sdf.format(c.getTime());  // dt is now the new date
					dat = Integer.parseInt(dt);
					
					char c1[]= dt.toCharArray();
					String darksky=""+c1[0]+""+c1[1]+""+c1[2]+""+c1[3]+"-"+c1[4]+""+c1[5]+"-"+c1[6]+""+c1[7];
					
					
//					String urrl="https://api.darksky.net/forecast/9f5783c9fabed19432e6b4f94e22dc02/39.103118,-84.512020,"+darksky+"T12:00:00";
					String urrl="https://api.darksky.net/forecast/9f5783c9fabed19432e6b4f94e22dc02/"+latitude+","+longitude+","+darksky+"T12:00:00";

					JSONObject json=readJsonFromUrl(urrl);
					JSONObject jj=json.getJSONObject("daily");
					Report d=new Report();
					JSONArray jr4= jj.getJSONArray("data");
					JSONObject jo=jr4.getJSONObject(0);
					d.DATE = Integer.toString(dat);
			    	double tmax=jo.getDouble("temperatureMax");
					d.TMAX = tmax;
			    	double tmin=jo.getDouble("temperatureMin");
			    	d.TMIN = (tmin);
			    	lt.add(d);
					//dat++;

				}
				return lt;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}		
		}	
	}

