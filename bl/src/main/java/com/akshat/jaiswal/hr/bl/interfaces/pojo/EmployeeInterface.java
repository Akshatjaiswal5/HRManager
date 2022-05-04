package com.akshat.jaiswal.hr.bl.interfaces.pojo;
import com.akshat.jaiswal.enums.*;
import java.math.*;
import java.util.*;
import java.io.*;


public interface EmployeeInterface extends Comparable<EmployeeInterface>,Serializable
{
 public void setEmployeeId(String employeeId);
 public String getEmployeeId();
 public void setName(String name);
 public String getName();
 public void setDesignation(DesignationInterface designation);
 public DesignationInterface getDesignation();
 public void setDateOfBirth(Date dateOfBirth);
 public Date getDateOfBirth();
 public void setGender(GENDER gender);
 public GENDER getGender();
 public void setIsIndian(boolean isIndian);
 public boolean getIsIndian();
 public void setBasicSalary(BigDecimal basicSalary);
 public BigDecimal getBasicSalary();
 public void setPANNumber(String PANNumber);
 public String getPANNumber();
 public void setAadharCardNumber(String aadharCardNumber);
 public String getAadharCardNumber();
}
