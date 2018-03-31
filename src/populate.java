import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class populate {
	static SummaryThread st = new SummaryThread();
	static String currentProcess = "";
	public static void main(String args[])  throws SQLException{
		String businessJSON = "C:\\Users\\sande\\Desktop\\os algos\\database\\YelpDataset\\YelpDataset-CptS451\\yelp_business.json";
		String checkinJSON = "C:\\\\Users\\\\sande\\\\Desktop\\\\os algos\\\\database\\\\YelpDataset\\\\YelpDataset-CptS451\\\\yelp_checkin.json";
		String userJSON = "C:\\\\Users\\\\sande\\\\Desktop\\\\os algos\\\\database\\\\YelpDataset\\\\YelpDataset-CptS451\\\\yelp_user.json";
		String reviewJSON = "C:\\\\Users\\\\sande\\\\Desktop\\\\os algos\\\\database\\\\YelpDataset\\\\YelpDataset-CptS451\\\\yelp_review.json";
		DB.init();
		DB.printInfo();
					
			st.start();
			currentProcess = "Business";
			System.out.println("Processing Business JSON File : " + businessJSON);
			populateBusinessJSON(businessJSON);
			//currentProcess = "User";
			//System.out.println("Processing User JSON File : " + userJSON);
			//populateUserJSON(userJSON);
			
			ReadYelpUserJSON a = new ReadYelpUserJSON();
			System.out.println("POPULATING USER NOW!!!");
			a.run_user();
			
			currentProcess = "Checkin";
			System.out.println("Processing CheckIn JSON File : " + checkinJSON);
			populateCheckInJSON(checkinJSON);
			currentProcess = "Review";
			System.out.println("Processing Review JSON File : " + reviewJSON);
			populateReviewJSON(reviewJSON);
			st.stopExecution = true;
		
	}
	
	public static void printSummary() {
		DB.printInfo();
	}
	
	public static void populateBusinessJSON(String filename){
		try {
			FileReader filereader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(filereader);
			
			//Initialize and Connect with Database
			DB.init();
			JSONParser parser = new JSONParser();
			
			String line;
			while((line = bufferedReader.readLine())!= null) {
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				//if(max_line-- < 0) break;
				Business bObj = new Business(jsonObject);
				DB.insertBusinessObject(bObj);
			}
			bufferedReader.close();
			filereader.close();
		}catch(FileNotFoundException e) {
			System.out.println("File Not found : " + filename);
			System.exit(0);
		}catch(IOException e) {
			System.out.println("IOException : " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}catch(ParseException e) {
			System.out.println("ParseException : " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void populateCheckInJSON(String filename){
		try {
			FileReader filereader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(filereader);
			
			//Initialize and Connect with Database
			DB.init();
			JSONParser parser = new JSONParser();
			
			String line;
			while((line = bufferedReader.readLine())!= null) {
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				List<Checkin> cObjList = Checkin.parseJSON(jsonObject);
				for(Checkin cObj : cObjList) {
					DB.insertCheckinObject(cObj);
				}
			}
			bufferedReader.close();
			filereader.close();
		}catch(FileNotFoundException e) {
			System.out.println("File Not found : " + filename);
			System.exit(0);
		}catch(IOException e) {
			System.out.println("IOException : " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}catch(ParseException e) {
			System.out.println("ParseException : " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void populateReviewJSON(String filename){
		try {
			FileReader filereader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(filereader);
			
			//Initialize and Connect with Database
			DB.init();
			JSONParser parser = new JSONParser();
			
			//int max_line = 2;
			String line;
			while((line = bufferedReader.readLine())!= null) {
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				Review r = new Review(jsonObject);
				
				DB.insertReviewObject(r);
			}
			bufferedReader.close();
			filereader.close();
		}catch(FileNotFoundException e) {
			System.out.println("File Not found : " + filename);
			System.exit(0);
		}catch(IOException e) {
			System.out.println("IOException : " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}catch(ParseException e) {
			System.out.println("ParseException : " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void populateUserJSON(String filename){
		try {
			FileReader filereader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(filereader);
			
			//Initialize and Connect with Database
			DB.init();
			JSONParser parser = new JSONParser();
			
			String line;
			while((line = bufferedReader.readLine())!= null) {
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				User u = new User(jsonObject);
				DB.insertUserObject(u);
			}
			bufferedReader.close();
			filereader.close();
		}catch(FileNotFoundException e) {
			System.out.println("File Not found : " + filename);
			System.exit(0);
		}catch(IOException e) {
			System.out.println("IOException : " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}catch(ParseException e) {
			System.out.println("ParseException : " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	
	/*public static void displayUsage() {
		System.out.println("Usage: java populate <path to yelp_business.json> <path to yelp_review.json> <path to yelp_checkin.json> <path to yelp_user.json>");
	}*/
	
	public static class DB {
		static Connection conn = null;
		
		//Database Credentials
		static String URL = "jdbc:oracle:thin:@localhost:1521/XE";
		static String USER = "sandesh";
		static String PASSWORD = "system";
	
		
		//Database Prepared Statement
		static PreparedStatement businessInsertStatement = null;
		static PreparedStatement mainCategoriesInsertStatement = null;
		static PreparedStatement subCategoriesInsertStatement = null;
		static PreparedStatement attributesInsertStatement = null;
		static PreparedStatement hoursInsertStatement = null;
		
		static PreparedStatement checkinInsertStatement = null;
		static PreparedStatement userInsertStatement = null;
		static PreparedStatement reviewInsertStatement = null;
		
		public static void init() {
			if(conn != null) return;
			System.out.println("Initializing Database");
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("Connecting with " + URL);
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
				System.out.println("Connection Successful");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				System.out.println("Error " + e.getMessage());
			}
		}
		
		public static void printInfo() {
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs;
				if(currentProcess.equals("Business")) {
					rs = stmt.executeQuery("SELECT count(*) from business_main_category");
					while(rs.next()) {
						System.out.println("Business Main Categories Records : " + rs.getInt(1));
						rs.next();
					}
					rs = stmt.executeQuery("SELECT count(*) from business_sub_category");
					while(rs.next()) {
						System.out.println("Business Sub Categories Records : " + rs.getInt(1));
						rs.next();
					}
					rs = stmt.executeQuery("SELECT count(*) from business_attribute");
					while(rs.next()) {
						System.out.println("Business Attribute Records : " + rs.getInt(1));
						rs.next();
					}
					rs = stmt.executeQuery("SELECT count(*) from business_hour");
					while(rs.next()) {
						System.out.println("Business Hour Records : " + rs.getInt(1));
						rs.next();
					}
				} else if(currentProcess.equalsIgnoreCase("Checkin")) {
					rs = stmt.executeQuery("SELECT count(*) from business_checkin");
					while(rs.next()) {
						System.out.println("Business Checkin Records : " + rs.getInt(1));
						rs.next();
					}
				} else if(currentProcess.equalsIgnoreCase("User")) {
					rs = stmt.executeQuery("SELECT count(*) from yelp_user");
					while(rs.next()) {
						System.out.println("User Records : " + rs.getInt(1));
						rs.next();
					}
				} else if(currentProcess.equalsIgnoreCase("Review")) {
					rs = stmt.executeQuery("SELECT count(*) from yelp_review");
					while(rs.next()) {
						System.out.println("Review Records : " + rs.getInt(1));
						rs.next();
					}
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		
		public static void insertUserObject(User u) {
			try {
				if(userInsertStatement == null) {
					String sqlQuery = "INSERT INTO YELP_USER " +
								"(name, userid) VALUES " +
								"(?,?)";
					userInsertStatement = conn.prepareStatement(sqlQuery);
				}
				
				userInsertStatement.setString(1, u.name);
				userInsertStatement.setString(2, u.userId);
				
				userInsertStatement.executeQuery();
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		
		public static void insertReviewObject(Review r) {
			try {
				if(reviewInsertStatement == null) {
					String sqlQuery = "INSERT INTO YELP_REVIEW " +
								"(reviewid, userid, bid, stars, reviewdate, reviewtext, funny_count, useful_count, cool_count) VALUES " +
								"(?,?,?,?,?,?,?,?,?)";
					reviewInsertStatement = conn.prepareStatement(sqlQuery);
				}
				
				reviewInsertStatement.setString(1, r.reviewId);
				reviewInsertStatement.setString(2, r.userId);
				reviewInsertStatement.setString(3, r.bid);
				reviewInsertStatement.setString(4, r.stars);
				reviewInsertStatement.setString(5, r.reviewdate);
				reviewInsertStatement.setString(6, r.reviewtext);
				reviewInsertStatement.setString(7, r.funny_count);
				reviewInsertStatement.setString(8, r.useful_count);
				reviewInsertStatement.setString(9, r.cool_count);
				
				reviewInsertStatement.executeQuery();
			} catch(Exception e) {
				System.out.println(e);
				
			}
		}
		
		public static void insertCheckinObject(Checkin cObj) {
			try {
				if(checkinInsertStatement == null) {
					String sqlQuery = "INSERT INTO BUSINESS_CHECKIN " +
								"(bid, day, hour, count) VALUES " +
								"(?,?,?,?)";
					checkinInsertStatement = conn.prepareStatement(sqlQuery);
				}
				
				checkinInsertStatement.setString(1, cObj.business_id);
				checkinInsertStatement.setInt(2, cObj.day);
				checkinInsertStatement.setInt(3, cObj.hour);
				checkinInsertStatement.setInt(4, cObj.count);
				
				checkinInsertStatement.executeQuery();
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		public static void insertBusinessObject(Business bObj) {
			//Insert Business
			try {
				if(businessInsertStatement == null) {
					String sqlQuery = "INSERT INTO BUSINESS " + 
							"(bid, address, isOpen, city, review_count, name, state, stars) VALUES "
							+"(?,?,?,?,?,?,?,?)";
					businessInsertStatement = conn.prepareStatement(sqlQuery);
				}
				
				businessInsertStatement.setString(1, bObj.business_id);
				businessInsertStatement.setString(2, bObj.full_address);
				businessInsertStatement.setInt(3, (bObj.open?1:0));
				businessInsertStatement.setString(4, bObj.city);
				businessInsertStatement.setString(5, bObj.review_count);
				businessInsertStatement.setString(6, bObj.name);
				businessInsertStatement.setString(7, bObj.state);
				businessInsertStatement.setString(8, bObj.stars);
				businessInsertStatement.executeQuery();
				
				//Insert Main Categories
				if(mainCategoriesInsertStatement == null) {
					String sqlQuery = "INSERT INTO business_main_category " + 
							"(bid, cname) VALUES "
							+"(?,?)";
					mainCategoriesInsertStatement = conn.prepareStatement(sqlQuery);
				}
				
				mainCategoriesInsertStatement.setString(1, bObj.business_id);
				for(String cat : bObj.mainCategories) {
					mainCategoriesInsertStatement.setString(2, cat);
					//System.out.println(mainCategoriesInsertStatement.toString());
					mainCategoriesInsertStatement.executeQuery();
				}
				
				//Insert Sub Categories
				if(subCategoriesInsertStatement == null) {
					String sqlQuery = "INSERT INTO business_sub_category " + 
							"(bid, cname) VALUES "
							+"(?,?)";
					subCategoriesInsertStatement = conn.prepareStatement(sqlQuery);
				}
				
				subCategoriesInsertStatement.setString(1, bObj.business_id);
				for(String cat : bObj.subCategories) {
					subCategoriesInsertStatement.setString(2, cat);
					//System.out.println(subCategoriesInsertStatement.toString());
					subCategoriesInsertStatement.executeQuery();
				}
				
				//Insert Attributes
				if(attributesInsertStatement == null) {
					String sqlQuery = "INSERT INTO business_attribute " + 
							"(bid, attribute) VALUES "
							+"(?,?)";
					attributesInsertStatement = conn.prepareStatement(sqlQuery);
				}
				
				attributesInsertStatement.setString(1, bObj.business_id);
				for(String att : bObj.attributes) {
					attributesInsertStatement.setString(2, att);
					//System.out.println(attributesInsertStatement.toString());
					attributesInsertStatement.executeQuery();
				}
				
				//Insert Hours
				if(hoursInsertStatement == null) {
					String sqlQuery = "INSERT INTO business_hour " + 
							"(bid, day, open, close) VALUES "
							+"(?,?,?,?)";
					hoursInsertStatement = conn.prepareStatement(sqlQuery);
				}
				
				hoursInsertStatement.setString(1, bObj.business_id);
				for(Hour h : bObj.hours) {
					hoursInsertStatement.setInt(2, h.day);
					hoursInsertStatement.setInt(3, h.open);
					hoursInsertStatement.setInt(4, h.close);
					//System.out.println(hoursInsertStatement.toString());
					hoursInsertStatement.executeQuery();
				}
			} catch(Exception e) {
				System.out.println("Exception while inserting Business entry " + e);
			}
		}
	}
	public static class Review {
		public String userId;
		public String bid;
		public String reviewId;
		public String funny_count;
		public String useful_count;
		public String cool_count;
		public String reviewdate;
		public String reviewtext;
		public String stars;
		public Review(JSONObject jsonObject) {
			if(jsonObject.containsKey("user_id")) {
				userId = (String)jsonObject.get("user_id");
			}
			if(jsonObject.containsKey("review_id")) {
				reviewId = (String) jsonObject.get("review_id");
			}
			if(jsonObject.containsKey("business_id")) {
				bid = (String) jsonObject.get("business_id");
			}
			if(jsonObject.containsKey("stars")) {
				stars = String.valueOf(jsonObject.get("stars"));
			}
			if(jsonObject.containsKey("date")) {
				reviewdate = String.valueOf(jsonObject.get("date"));
			}
			if(jsonObject.containsKey("text")) {
				reviewtext = String.valueOf(jsonObject.get("text"));
			}
			if(jsonObject.containsKey("votes")) {
				JSONObject votes = (JSONObject) jsonObject.get("votes");
				if(votes.containsKey("funny")) {
					funny_count = String.valueOf(votes.get("funny"));
				}
				if(votes.containsKey("cool")) {
					cool_count = String.valueOf(votes.get("cool"));
				}
				if(votes.containsKey("useful")) {
					useful_count = String.valueOf(votes.get("useful"));
				}
			}
		}
	}
	public static class User {
		public String userId;
		public String name;
		public User(JSONObject jsonObject) {
			userId = (String) jsonObject.get("user_id");
			name = (String) jsonObject.get("name");
		}
	}
	
	public static class Checkin {
		public String business_id;
		public Integer day;
		public Integer hour;
		public Integer count;
		public Checkin(String bid,Integer d, Integer h, Integer c) {
			business_id = bid;
			day = d;
			hour = h;
			count = c;
		}
		public static List<Checkin> parseJSON(JSONObject jsonObject) {
			List<Checkin> objList = new ArrayList<Checkin>();
			
			if(jsonObject.containsKey("business_id")) {
				String bid = (String) jsonObject.get("business_id");
				
				if(jsonObject.containsKey("checkin_info")) {
					JSONObject ci = (JSONObject) jsonObject.get("checkin_info");
					for(Object key : ci.keySet()) {
						String dayHourPair[] = ((String) key).split("-");
						Integer day = Integer.valueOf(dayHourPair[1]);
						Integer hour = Integer.valueOf(dayHourPair[0]);
						Integer count = Integer.valueOf(String.valueOf(ci.get(key)));
						objList.add(new Checkin(bid,day,hour,count));
					}
				}
			}
			
			return objList;
		}
	}
	
	public static class Business {
		public String business_id;
		public String name;
		public String state;
		public String city;
		public String review_count;
		public String full_address;
		public String stars;
		public Boolean open;
		public List<String> attributes;
		public List<String> mainCategories = new ArrayList<String>();
		public List<String> subCategories = new ArrayList<String>();
		public List<Hour> hours; 
		
		public Business(JSONObject jsonObject) {
			hours = new ArrayList<Hour>();
			if(jsonObject.containsKey("hours")) {
				JSONObject hoursObj = (JSONObject)jsonObject.get("hours");
				for(Object day : hoursObj.keySet()) {
					hours.add(new Hour((String)day, (JSONObject)hoursObj.get(day)));
				}
			}
			//System.out.println(hours);
			//neighborhoods
			//city
			if(jsonObject.containsKey("city"))
				city = (String) jsonObject.get("city");
			
			//latitude - Ignore
			//review_count
			if(jsonObject.containsKey("review_count"))
				review_count = String.valueOf(jsonObject.get("review_count"));
			
			//full_address
			if(jsonObject.containsKey("full_address"))
				full_address = (String) jsonObject.get("full_address");
			
			//stars
			if(jsonObject.containsKey("stars"))
				stars = String.valueOf(jsonObject.get("stars"));
			//type - Ignoring
			
			//name
			if(jsonObject.containsKey("name"))
				name = (String) jsonObject.get("name");

			//state
			if(jsonObject.containsKey("state"))
				state = (String) jsonObject.get("state");
			//business_id
			if(jsonObject.containsKey("business_id"))
				business_id = (String) jsonObject.get("business_id");
			
			//open
			if(jsonObject.containsKey("open"))
				open = (Boolean) jsonObject.get("open");
			//longitude - Ignore
			
			//attributes
			attributes = new ArrayList<String>();
			if(jsonObject.containsKey("attributes")) {
				JSONObject attr = (JSONObject) jsonObject.get("attributes");
				for(Object s : attr.keySet()) {
					Object val = attr.get(s);
					if(val instanceof Boolean) {
						attributes.add((String) s + "_" + String.valueOf(val));
					} else if(val instanceof String) {
						attributes.add((String)s + "_" + (String)val);
					} else if(val instanceof JSONObject) {
						for(Object sub_key : ((JSONObject) val).keySet()) {
							attributes.add((String)s + "_" + (String)sub_key + "_" + String.valueOf(((JSONObject) val).get(sub_key)));
						}
					}
				}
			}
			
			//categories
			mainCategories = new ArrayList<String>();
			subCategories = new ArrayList<String>();
			if(jsonObject.containsKey("categories")) {
				JSONArray catList = (JSONArray)jsonObject.get("categories");
				for(Object s : catList) {
					if(isMainCategory((String) s)) {
						mainCategories.add((String) s);
					} else {
						subCategories.add((String) s);
					}
				}
			}
		}
		
		public static Boolean isMainCategory(String cat) {
			Set<String> catList = new HashSet<String>();
			catList.addAll(Arrays.asList("Active Life", 
					"Arts & Entertainment",
					"Automotive",
					"Car Rental", 
					"Cafes",
					"Beauty & Spas",
					"Convenience Stores",
					"Dentists",
					"Doctors", 
					"Drugstores",
					"Department Stores",
					"Education",
					"Event Planning & Services",
					"Flowers & Gifts",
					"Food",
					"Health & Medical",
					"Home Services",
					"Home & Garden", 
					"Hospitals",
					"Hotels & Travel",
					"Hardware Stores", 
					"Grocery",
					"Medical Centers",
					"Nurseries & Gardening",
					"Nightlife",
					"Restaurants",
					"Shopping",
					"Transportation"));
			return catList.contains(cat);
		}
	}
	public static class Hour {
		public Integer day;
		public Integer open;
		public Integer close;
		
		public Hour(String day, JSONObject jsonObj) {
			this.day = Hour.convertDay(day);
			if(jsonObj.containsKey("open")) {
				this.open = convertHour((String)jsonObj.get("open"));
			}
			if(jsonObj.containsKey("close")) {
				this.close = convertHour((String)jsonObj.get("close"));
			}
			if(this.close < this.open) {
				this.close = this.close + (24*60); //Because closing time must be greater than opening time
			}
		}
		
		public static Integer convertHour(String hour) {
			String[] hourArr = hour.split(":");
			Integer h = Integer.valueOf(hourArr[0])*60;
			Integer m = Integer.valueOf(hourArr[1]);
			return h+m;
		}
		
		public static Integer convertDay(String day) {
			Integer day_i = 0;
			if(day.equalsIgnoreCase("Sunday")) day_i = 0;
			else if(day.equalsIgnoreCase("Monday")) day_i = 1;
			else if(day.equalsIgnoreCase("Tuesday")) day_i = 2;
			else if(day.equalsIgnoreCase("Wednesday")) day_i = 3;
			else if(day.equalsIgnoreCase("Thursday")) day_i = 4;
			else if(day.equalsIgnoreCase("Friday")) day_i = 5;
			else if(day.equalsIgnoreCase("Saturday")) day_i = 6;
			else day_i = -1;
			return day_i;
		}
		public String toString() {
			return day + " " + open + "-" + close;
		}
		
	}
	public static class SummaryThread extends Thread {
		public Boolean stopExecution = false;
		public void run() {
			int i = 0;
			try {
				while(!stopExecution) {
					Thread.sleep(30000);
					i++;
					System.out.println("Time " + (Float.valueOf(i)/Float.valueOf(6)) + " minute.");
					printSummary();
				}
			} catch(Exception e) {
				
			}
		}
	}
}

class ReadYelpUserJSON 
{
	public void run_user() throws SQLException
	{
		// TODO Auto-generated method stub
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;

		String sqlquery = "INSERT INTO YELP_USER" 
		 							+ "(yelping_since, review_count, name, userid, average_stars) VALUES" 
									+ "(?,?,?,?,?)";

		String sqlquery2 = "INSERT INTO FRIENDS" + "(user_id, friend_id) VALUES" + "(?,?)";

		
		JSONParser parser = new JSONParser();
		int i = 0;
		
		try
		{
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(sqlquery);
			preparedStatement2 = dbConnection.prepareStatement(sqlquery2);
			
			FileReader filereader = new FileReader("C:\\Users\\sande\\Desktop\\os algos\\database\\YelpDataset\\YelpDataset-CptS451\\yelp_user.json");
			
			BufferedReader bufferedReader = new BufferedReader(filereader);
			String line;
			while ((line = bufferedReader.readLine()) != null) 
			{   System.out.println(i++);
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				String yelp_since = (String) jsonObject.get("yelping_since");
				preparedStatement.setString(1, yelp_since);
			
				int review_count = ((Long) jsonObject.get("review_count")).intValue();
				preparedStatement.setInt(2, review_count);
			
				String name = (String) jsonObject.get("name");
				preparedStatement.setString(3, name);
			
				String user_id = (String) jsonObject.get("user_id");
				preparedStatement.setString(4, user_id);
			
				
				float avg_stars = ((Double) jsonObject.get("average_stars")).floatValue();
				preparedStatement.setFloat(5, avg_stars);
				System.out.println(i++);
				preparedStatement.executeUpdate();
				

				if(jsonObject.get("friends")!=null)
				{
					JSONArray friendarray = (JSONArray) jsonObject.get("friends");
					Iterator<String> iterator = friendarray.iterator();
					String friend_id;
			
					while(iterator.hasNext())
					{
						friend_id = iterator.next();
						preparedStatement2.setString(1, user_id);
						preparedStatement2.setString(2, friend_id);
						preparedStatement2.executeUpdate();
					}
				}

			}
			filereader.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e) 
		{

			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			if (preparedStatement != null) 
			{
				preparedStatement.close();
			}
			if (preparedStatement2 != null) 
			{
				preparedStatement2.close();
			}

			if (dbConnection != null) 
			{
				dbConnection.close();
			}
		}

	}
	
	public static Connection getDBConnection() 
	{

		Connection dbConnection = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {

			System.out.println(e.getMessage());

		}

		try {

			dbConnection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/XE", "sandesh", "system");
			return dbConnection;

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		return dbConnection;

	}		

}