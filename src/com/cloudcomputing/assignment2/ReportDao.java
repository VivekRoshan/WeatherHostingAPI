package com.cloudcomputing.assignment2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.PathParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ReportDao {
	
	
	@SuppressWarnings("unchecked")
	public List<Report> readData(){
		List<Report> reports = new ArrayList<Report>();
		try {
		File file2 = new File("Userx.dat");
		 if (!file2.exists()) {
		//URL url = ReportDao.class.getClassLoader().getResource("daily.csv");
		//File file = new File("C:\\Users\\Indra_000\\Desktop\\study\\Cloud\\daily.csv");
		//File file = new File("daily.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("daily.csv")));
		String st = br.readLine();
		st = br.readLine();
		while(st != null)
		{
		String[] array = st.split(",");
	    	Report report = new Report(array[0], Double.parseDouble(array[1]), Double.parseDouble(array[2]));
	    	reports.add(report);
	    	st = br.readLine();
	    }
		saveUserList(reports);
		
		 }
		 else{
		 FileInputStream fis;
			fis = new FileInputStream(file2);
			ObjectInputStream ois = new ObjectInputStream(fis);
			 reports = (List<Report>) ois.readObject();
			 ois.close();
			 
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		 return reports;
			
	}
		 
	
	public Report getReport(String date)
	{
		Report report2 = null;
		List<Report> list = readData();
		for (Report report : list) {
			if(report.DATE.equals(date))
				return report;
		}
		return report2;
	}
	
	public int addReport(Report report)
	{
		try{
		DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String d = report.DATE+"";
        
        Date date = df1.parse(d);
        String date1 = df1.format(date);
		}
		catch(Exception e)
		{
			return 0;
		}
		
		boolean flag = false;
		//ReportDao.reports.add(report);
		List<Report> r = new ArrayList<>();
		r = readData();
		for (Report report2 : r) {
			if(report2.DATE.equals(report.DATE))
			{
				flag = true;
				report2.TMAX = report.TMAX;
				report2.TMIN = report.TMIN;
			}
		}
		
		if(flag){
			saveUserList(r);
			return 1;
		}
		else{
			r.add(new Report(report.DATE, report.TMAX, report.TMIN));
			saveUserList(r);
			return 1;
		}
		
	}
	public Report deleteReport(String DATE)
	{
		List<Report> list = readData();
		for (Report report : list) {
			if(report.DATE.equals(DATE))
			{
				int index = list.indexOf(report);
				Report report2 = list.get(index);
				list.remove(index);
				saveUserList(list);
				return report2;
			}
		}
		return null;
	}
	public List<Report> nextSevenDays(String DATE)
	{
		List<Report> answer = new ArrayList<>();
		List<Report> reports = new ApacheHttpRestClient1().forecast();
		int i = 0;
		while(i < 7)
		{
			String dt = DATE;  // Start date
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
				c.add(Calendar.DATE, 1);  // number of days to add
				
			}
			dt = sdf.format(c.getTime());  // dt is now the new date
			DATE = dt;
			
			answer.add(new Report(DATE,reports.get(i).TMAX,reports.get(i).TMIN));
			i = i+1;
		}
		
			
		/*List<Report> reports = readData();
		
		double sumTMAX =0.0;
		double sumTMIN = 0.0;
		for(int p=0;p<7;p++)
		{
			for(int j=1;j<20;j++)
			{
				sumTMAX += reports.get(j*(p+1)).TMAX;
				sumTMIN += reports.get(j*(p+1)).TMIN;
			}
			sumTMAX = sumTMAX/18;
			sumTMIN = sumTMIN/18;
			sumTMAX = Math.floor(sumTMAX * 100) / 100;
			sumTMIN = Math.floor(sumTMIN * 100) / 100;
			
			String dt = DATE+"";  // Start date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(sdf.parse(dt));
				System.out.println(c.getTime().toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(p != 0)
			{
				c.add(Calendar.DATE, 1);  // number of days to add
				
			}
			dt = sdf.format(c.getTime());  // dt is now the new date
			DATE = Integer.parseInt(dt);
			answer.add(new Report(DATE,sumTMAX, sumTMIN ));
		}*/
		return answer;
	}
	
	//my own logic for the forecast
	public Report nextSevenDaysOwnLogic(String DATE)
	{
		List<Report> reports = readData();
		double sumTmax = 0.0;
		double sumTmin = 0.0;
		int i=0;
		for (Report report : reports) {
			if(report.DATE.substring(4).equals(DATE.substring(4)))
			{
				sumTmax += report.TMAX;
				sumTmin += report.TMIN;
				i++;
			}
		}
		Report answer = new Report(DATE, ((sumTmax/i)*100)/100, ((sumTmin/i)*100)/100);
		return answer;
	}
	
	public List<Report> nextSevenDaysOwnLogicLoop(String DATE)
	{
		List<Report> answer = new ArrayList<>();
		String dt = DATE;  // Start date
		answer.add(nextSevenDaysOwnLogic(DATE));
		int i=1;
		while(i<7)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(sdf.parse(dt));
				//System.out.println(c.getTime().toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			c.add(Calendar.DATE, 1);  // number of days to add
			dt = sdf.format(c.getTime());  // dt is now the new date
			DATE = dt;
			answer.add(nextSevenDaysOwnLogic(DATE));
			i++;
			
		}
		return answer;
	}
	
	//for hw3
	public List<Report> getReportforfivedays(String DATE)
	{
		List<Report> reports = new ArrayList<>();
		List<Report> list = readData();
		int j=0;
		for (Report report : list) {
			if(report.DATE.equals(DATE))
			{
				
				int index = list.indexOf(report);
				int i = index;
				System.out.println(index);
				System.out.println(list.size());
				while(i<index+7 && i < list.size())
				{
					reports.add(list.get(i));
					i++;
					j++;
				}
				break;
			}
		}
		System.out.println(j);
		List<Report> reports2 = new ArrayList<>();
		if(j < 7 && j>0)
		{
			
			String date = reports.get(j-1).DATE;
			String dt = date;  // Start date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(sdf.parse(dt));
				System.out.println(c.getTime().toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
				c.add(Calendar.DATE, 1);  // number of days to add
			dt = sdf.format(c.getTime());  // dt is now the new date
			date = dt;
			reports2 = nextSevenDaysOwnLogicLoop(date);
			int i = 0;
			while(j<7)
			{
				
				reports.add(reports2.get(i));
				i++;
				j++;
			}
			
			
		}
		if(j ==0)
		{
			reports2 = nextSevenDaysOwnLogicLoop(DATE);
			int i = 0;
			while(j<7)
			{
				
				reports.add(reports2.get(i));
				i++;
				j++;
			}
		}
		
		
		return reports;
	}
	private static void saveUserList(List<Report> userList){
		 try {
		 File file2 = new File("Userx.dat");
		 if(file2.exists())
			file2.delete(); 
		 File file = new File("Userx.dat");
		 
		 FileOutputStream fos;
		 fos = new FileOutputStream(file);
		 ObjectOutputStream oos = new ObjectOutputStream(fos);
		 oos.writeObject(userList);
		 oos.close();
		 } catch (FileNotFoundException e) {
		 e.printStackTrace();
		 } catch (IOException e) {
		 e.printStackTrace();
		 }
		 }
	
    public List<Report> forcastClimate(int dat) {
		System.out.println(" "+dat);
		try{
			List<Report> lt=new ArrayList<>();
			String temp = Integer.toString(dat);
			char c[]=temp.toCharArray();
			String dt=""+c[0]+""+c[1]+""+c[2]+""+c[3]+"-"+c[4]+""+c[5]+"-"+c[6]+""+c[7];
			System.out.println(dt);
			java.sql.Date dd = java.sql.Date.valueOf( dt );
			for(int i=0;i<7;i++)
			{

				//String urrl="https://api.darksky.net/forecast/9f5783c9fabed19432e6b4f94e22dc02/39.103118,-84.512020,"+dd+"T12:00:00";
				String urrl ="https://api.darksky.net/forecast/3476ac5af82da1f43aa6e9b8fd29d0b4/39.103118,-84.512020,"+dd+"T12:00:00";
				JSONObject json=readJsonFromUrl(urrl);
				JSONObject jj=json.getJSONObject("daily");
				Report d=new Report();
				JSONArray jr4= jj.getJSONArray("data");
				JSONObject jo=jr4.getJSONObject(0);
				d.DATE = Integer.toString(dat);
		    	double tmax=jo.getLong("temperatureMax");
				d.TMAX = tmax;
		    	double tmin=jo.getLong("temperatureMin");
		    	d.TMIN = tmin;
		    	lt.add(d);
				dat++;
				dd = new java.sql.Date(dd.getTime() + (1000 * 60 * 60 * 24));

			}
			return lt;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}		
	}


	public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
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
		public String readAll(Reader rd) throws IOException {
			StringBuilder sb = new StringBuilder();
			int cp;
			while ((cp = rd.read()) != -1) {
				sb.append((char) cp);
			}
			return sb.toString();
		}

	public static void main(String[] args) {
		ReportDao dao = new ReportDao();
		List<Report> reports = dao.readData();
		for (Report report : reports) {
		//	System.out.println(report.DATE+report.TMAX+" "+report.TMIN);
		}
	
	//	deleteReport(20130103);
		//System.out.println(dao.getReport(20130103+""));
		List<Report> reports2 = dao.getReportforfivedays("20160309");
		for (Report report : reports2) {
			System.out.println(report.DATE+" "+report.TMAX+" "+report.TMIN);
		}
		
		System.out.println();
		
		//System.out.println(dao.addReport(new Report(20170303+"", 10000.1, 12.0)));
/*		Report report = dao.nextSevenDaysOwnLogic("20170201");
		System.out.println(report.DATE+" "+report.TMAX+" "+report.TMIN);
*/		
		/*List<Report> list = dao.nextSevenDaysOwnLogicLoop("20180228");
		for (Report report2 : list) {
			System.out.println(report2.DATE+" "+report2.TMAX+" "+report2.TMIN);
		}*/
		List<Report> list = dao.forcastClimate(20170331);
		for (Report report2 : list) {
			System.out.println(report2.DATE+" "+report2.TMAX+" "+report2.TMIN);
		}
		
	}
	

}
