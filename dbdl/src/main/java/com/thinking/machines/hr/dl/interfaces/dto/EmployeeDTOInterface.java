package com.thinking.machines.hr.dl.interfaces.dto;
import java.math.*;
import java.util.*;
import java.io.*;
import com.thinking.machines.enums.*;

public interface EmployeeDTOInterface extends Comparable<EmployeeDTOInterface>,Serializable
{
 public void setEmployeeId(String employeeId);
 public String getEmployeeId();
 public void setName(String name);
 public String getName();
 public void setDesignationCode(int designationCode);
 public int getDesignationCode();
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
