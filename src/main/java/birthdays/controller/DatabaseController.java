package birthdays.controller;

import birthdays.model.BDayUnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseController {

    public static DatabaseController instance = null;
    private Connection connection;

    public DatabaseController(String baseLocation) throws SQLException {
        DriverManager.registerDriver(new org.sqlite.JDBC());
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + baseLocation);
    }

    public static synchronized DatabaseController getInstance(String baseLocation) throws SQLException {
        if (instance == null)
            instance = new DatabaseController(baseLocation);
        return instance;
    }

    public ArrayList<BDayUnit> selectAll() throws SQLException {
        try (Statement statement = this.connection.createStatement()) {
            ArrayList<BDayUnit> dataList = new ArrayList<BDayUnit>();
            ResultSet resultSet = statement.executeQuery("SELECT id, first_name, last_name, b_day, phone_number,description FROM bdays");
            while (resultSet.next()) {
                dataList.add(new BDayUnit(resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("b_day"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("description")));
            }
            Collections.reverse(dataList);
            return dataList;
        } catch (SQLException e) {
            throw e;
        }
    }

    public void addUnit(BDayUnit unit) throws SQLException {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO bdays('first_name','last_name','b_day','phone_number','description')" +
                        "VALUES(?,?,?,?,?)")) {
            statement.setObject(1, unit.getFirstName());
            statement.setObject(2, unit.getLastName());
            statement.setObject(3, unit.getDate());
            statement.setObject(4, unit.getPhoneNumber());
            statement.setObject(5, unit.getDescription());
            statement.execute();

        } catch (SQLException e) {
            throw e;
        }
    }

    public ArrayList<BDayUnit> search(String value) throws SQLException {

        String query = "select distinct id, first_name, last_name, b_day, phone_number, description from bdays where " +
                    "first_name like ('%" + value + "%') or " +
                    "last_name like ('%" + value + "%') or " +
                    "b_day like ('%" + value + "%') or " +
                    "phone_number like ('%" + value + "%')";

        try (Statement statement = this.connection.createStatement()) {
            ArrayList<BDayUnit> dataList = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                dataList.add(new BDayUnit(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("b_day"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("description")));
            }
            Collections.reverse(dataList);
            return dataList;
        }
    }

    public void deleteUnit(int id) throws SQLException {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM bdays WHERE id = ?")) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw e;
        }
        try (PreparedStatement statement = this.connection.prepareStatement(
                "Vacuum;")) {
            statement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateFullUnit(BDayUnit unit) throws SQLException {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "UPDATE bdays SET first_name = ?, " +
                        "last_name = ? ," +
                        "b_day = ? ," +
                        "phone_number = ? ," +
                        "description = ?" +
                        " WHERE id = ?")) {
            statement.setObject(1, unit.getFirstName());
            statement.setObject(2, unit.getLastName());
            statement.setObject(3, unit.getDate());
            statement.setObject(4, unit.getPhoneNumber());
            statement.setObject(5, unit.getDescription());
            statement.setObject(6, unit.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void clearBase() throws SQLException {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "Vacuum;")) {
            statement.execute();
        } catch (SQLException e) {
            throw e;
        }
        try (PreparedStatement statement = this.connection.prepareStatement(
                "delete from bdays;")) {
            statement.execute();
        } catch (SQLException e) {
            throw e;
        }
        try (PreparedStatement statement = this.connection.prepareStatement(
                "UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'bdays';")) {
            statement.execute();
        } catch (SQLException e) {
            throw e;
        }

    }

}
