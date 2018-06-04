package kr.netty;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public enum DataBase {
    Weather();
    private Connection conn;

    DataBase(){
        try {
            conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            prepareDatabase();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareDatabase() throws SQLException {

        PreparedStatement ps = conn.prepareStatement("DELETE FROM weatherF");
        ps.execute();
        ps = conn.prepareStatement("INSERT INTO weatherF VALUES ('Moscow', '+25'), ('Piter','-10'), ('Tom', '+15'), ('Tokyo', '+20'), ('Minsk','+19')");
        ps.execute();
        ps = conn.prepareStatement("SELECT * from weatherF");

    }

    public void closeConnection() throws SQLException{
        conn.close();
    }

    public String getWeather(String city)throws SQLException{
        String Result;
        synchronized (conn) {
            PreparedStatement ps = conn.prepareStatement("SELECT Weather FROM WeatherF WHERE city ='" + city + "'");
            ResultSet rs = ps.executeQuery();
            rs.beforeFirst();
            rs.next();
            Result = rs.getString("Weather");
        }
        return Result;
    }

    public Set<String> getCities(){
        try {
            Set<String> result = new HashSet<>();
            synchronized (conn) {
                PreparedStatement ps = conn.prepareStatement("SELECT City FROM WeatherF");
                ResultSet rs = ps.executeQuery();
                rs.beforeFirst();
                while (rs.next()) {
                    result.add(rs.getString("City"));
                }
            }
            return  result;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    } }

