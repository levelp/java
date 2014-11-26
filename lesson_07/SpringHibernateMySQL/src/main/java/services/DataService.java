package services;

import model.Employee;

import java.util.List;


/**
 *
 */
public interface DataService {
    public int insertRow(Employee employee);

    public List<Employee> getList();

    public Employee getRowById(int id);

    public int updateRow(Employee employee);

    public int deleteRow(int id);

}
