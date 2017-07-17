package jdbcPostgres;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App 
{
	public static void main( String[] args )
	{
		Connection c = null;
		PreparedStatement ps = null;
		final String ipAddress = "192.168.99.100";
		
		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			return;
		}
		
		//creation Db tables 
		try {
			Class.forName("org.postgresql.Driver"); 
			c = DriverManager.getConnection("jdbc:postgresql://" + ipAddress + ":5432/trasporti?useUnicode=true&characterEncoding=UTF-8", "postgres", "ai-user-password");
			
			JSONParser parser = new JSONParser();

			String queryLine = "INSERT INTO BusLine (line, description) VALUES (?, ?)";
			String queryStop = "INSERT INTO BusStop (id, name, lat, lng) VALUES (?, ?, ?, ?)";
			String queryLineStop = "INSERT INTO BusLineStop (stopId, lineId, sequenceNumber) VALUES (?, ?, ?)";
			ps = c.prepareStatement(queryLine);

			Object obj = parser.parse(new BufferedReader(new InputStreamReader(new FileInputStream("linee.json"), "iso-8859-1")));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray lines = (JSONArray) jsonObject.get("lines");
			JSONArray stops = (JSONArray) jsonObject.get("stops");

			Iterator<JSONObject> iterator = lines.iterator();
			while (iterator.hasNext()) {
				JSONObject line = iterator.next();
				String lineName = (String)line.get("line");
				String desc = (String)line.get("desc");
				ps.setString(1, lineName);
				ps.setString(2, desc);
				ps.executeUpdate();
			}
			
			System.out.println("Inserted all lines into BusLine");
			
			iterator = stops.iterator();
			ps = c.prepareStatement(queryStop);
			while (iterator.hasNext()) {
				JSONObject stop = iterator.next();
				String id = (String)stop.get("id");
				String name = (String)stop.get("name");
				ps.setString(1, id);
				ps.setString(2, name);

				JSONArray latLng = (JSONArray) stop.get("latLng");
				double lat = (Double)latLng.get(0);
				
				double lng = (Double)latLng.get(1);
				ps.setDouble(3, lat);
				ps.setDouble(4, lng);
				ps.executeUpdate();

			}		

			System.out.println("Inserted all lines into BusStop");
			
			ps = c.prepareStatement(queryLineStop);
			iterator = lines.iterator();
			while (iterator.hasNext()) {
				JSONObject line = iterator.next();
				JSONArray stopsForLIne = (JSONArray)line.get("stops");
				for(int i=0; i<stopsForLIne.size(); i++){
					String stopId = (String)stopsForLIne.get(i);
					ps.setString(1, stopId);
					ps.setString(2, (String)line.get("line"));
					ps.setShort(3, (short)i);
					ps.executeUpdate();
				}
			} 
			
			System.out.println("Inserted all lines into BusLineStop");
			System.out.println("End");
		} catch (SQLException e) {
			System.out.println("Error : "+e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Error : "+e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println("Error opening the file");
		} catch (IOException e) {
			System.out.println("Error : "+e.getMessage());
		} catch (ParseException e) {
			System.out.println("Error : "+e.getMessage());
		}finally{
			try {
				if(ps != null) ps.close();
				if(c != null) c.close();
			} catch (SQLException e) {
				System.exit(1);
			}
		}
	} 	
	
}