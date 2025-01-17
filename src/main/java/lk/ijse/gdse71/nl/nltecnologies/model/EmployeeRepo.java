package lk.ijse.gdse71.nl.nltecnologies.model;

import lk.ijse.gdse71.nl.nltecnologies.db.DbConnection;
import lk.ijse.gdse71.nl.nltecnologies.dto.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {
    public static List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM employee";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Employee> empList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String nic = resultSet.getString(4);
            String position = resultSet.getString(5);
            String contact = resultSet.getString(6);
            java.sql.Date dob = resultSet.getDate(7);
            Date dateRegistration = resultSet.getDate(8);
            String email = resultSet.getString(9);
            double salary = resultSet.getDouble(10);
            String path = resultSet.getString(11);

            Employee employee = new Employee(id, name, address, nic, position, contact , dob, dateRegistration, email, salary, path);
            empList.add(employee);
        }
        return empList;
    }

    public static List<String> getId() throws SQLException {
        String sql = "SELECT emp_id FROM employee";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }

    public static String getLastId() throws SQLException {
        String sql = "SELECT emp_id FROM employee ORDER BY CAST(SUBSTRING(emp_id, 2) AS UNSIGNED) DESC LIMIT 1;";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String Id = resultSet.getString(1);
            return Id;
        }
        return null;
    }

    public static boolean save(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, employee.getEmpId());
        pstm.setObject(2, employee.getEmpName());
        pstm.setObject(3, employee.getEmpAddress());
        pstm.setObject(4, employee.getEmpNic());
        pstm.setObject(5, employee.getPosition());
        pstm.setObject(6, employee.getEmpTel());
        pstm.setObject(7, employee.getDob());
        pstm.setObject(8, employee.getDateRegister());
        pstm.setObject(9, employee.getEmpEmail());
        pstm.setObject(10, employee.getSalary());
        pstm.setObject(11, employee.getPath());

        return pstm.executeUpdate() > 0;
    }

    public static Employee searchById(String id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE emp_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {

            String emp_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String nic = resultSet.getString(4);
            String position =resultSet.getString(5);
            String contact = resultSet.getString(6);
            Date dateOfBirth = resultSet.getDate(7);
            Date enrollDate = resultSet.getDate(8);
            String email =resultSet.getString(9);
            double sallary = resultSet.getDouble(10);
            String path = resultSet.getString(11);

            return new Employee(emp_id, name, address, nic, position, contact, dateOfBirth, enrollDate, email, sallary, path);
        }

        return null;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM employee WHERE emp_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET name = ?, address = ?, nic = ?, position = ?, contact = ?, dob = ?, enroll_date = ?, email = ?, basic_salary = ?, path = ? WHERE emp_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, employee.getEmpName());
        pstm.setObject(2, employee.getEmpAddress());
        pstm.setObject(3, employee.getEmpNic());
        pstm.setObject(4, employee.getPosition());
        pstm.setObject(5, employee.getEmpTel());
        pstm.setObject(6, employee.getDob());
        pstm.setObject(7, employee.getDateRegister());
        pstm.setObject(8, employee.getEmpEmail());
        pstm.setObject(9, employee.getSalary());
        pstm.setObject(10, employee.getPath());
        pstm.setObject(11, employee.getEmpId());

        return pstm.executeUpdate() > 0;
    }
}
