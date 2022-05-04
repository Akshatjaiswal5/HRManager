package com.akshat.jaiswal.hr.bl.interfaces.managers;
import com.akshat.jaiswal.hr.bl.exceptions.*;
import com.akshat.jaiswal.hr.bl.interfaces.pojo.*;
import java.util.*;

public interface EmployeeManagerInterface
{
 public void addEmployee(EmployeeInterface employee) throws BLException;
 public Set<EmployeeInterface> getEmployees();
 public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode) throws BLException;
 public EmployeeInterface getEmployeeByEmployeeId(String employeeId) throws BLException;
 public EmployeeInterface getEmployeeByPANNumber(String PANNumber) throws BLException;
 public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException;
 public boolean employeeIdExists(String employeeId);
 public boolean PANNumberExists(String PANNumber);
 public boolean aadharCardNumberExists(String aadharCardNumber);
 public boolean isDesignationAlloted(int designationCode) throws BLException;
 public int getEmployeeCount();
 public int getEmployeeCountByDesignation(int designationCode) throws BLException; 
 public void updateEmployee(EmployeeInterface employee) throws BLException;
 public void deleteEmployee(String employeeId) throws BLException;
}