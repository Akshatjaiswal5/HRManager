package com.akshat.jaiswal.hr.dl.dto;
import com.akshat.jaiswal.enums.*;
import com.akshat.jaiswal.hr.dl.interfaces.dto.*;
import java.util.*;
import java.math.*;


public class EmployeeDTO implements EmployeeDTOInterface
{
 String employeeId,name,PANNumber,aadharCardNumber;
 int designationCode;
 GENDER gender;
 boolean isIndian;
 BigDecimal basicSalary;
 Date dateOfBirth;
 public EmployeeDTO()
 {
  this.employeeId="";
  this.name="";
  this.PANNumber="";
  this.aadharCardNumber="";
  this.designationCode=0;
  this.gender=GENDER.MALE;
  this.isIndian=false;
  this.basicSalary=null;
  this.dateOfBirth=null;
 }
 public void setEmployeeId(java.lang.String employeeId)
 {
  this.employeeId=employeeId;
 }
 public java.lang.String getEmployeeId()
 {
  return this.employeeId;
 }
 public void setName(java.lang.String name)
 {
  this.name=name;
 }
 public java.lang.String getName()
 {
  return this.name;
 } 
 public void setPANNumber(java.lang.String PANNumber)
 {
  this.PANNumber=PANNumber;
 }
 public java.lang.String getPANNumber()
 {
  return this.PANNumber;
 }
 public void setAadharCardNumber(java.lang.String aadharCardNumber)
 {
  this.aadharCardNumber=aadharCardNumber;
 }
 public java.lang.String getAadharCardNumber()
 {
  return this.aadharCardNumber;
 }
 public void setDesignationCode(int designationCode)
 {
  this.designationCode=designationCode;
 }
 public int getDesignationCode()
 {
  return this.designationCode;
 }
 public void setGender(GENDER gender)
 {
  this.gender=gender;
 }
 public GENDER getGender()
 {
  return this.gender;
 }
 public void setIsIndian(boolean isIndian)
 {
  this.isIndian=isIndian;
 }
 public boolean getIsIndian()
 {
  return this.isIndian;
 }
 public void setBasicSalary(java.math.BigDecimal basicSalary)
 {
  this.basicSalary=basicSalary;
 }
 public java.math.BigDecimal getBasicSalary()
 {
  return this.basicSalary;
 }
 public void setDateOfBirth(java.util.Date dateOfBirth)
 {
  this.dateOfBirth=dateOfBirth;
 }
 public java.util.Date getDateOfBirth()
 {
  return this.dateOfBirth;
 }
 
 public boolean equals(Object other)
 {
  if(!(other instanceof EmployeeDTOInterface))
  return false;

  EmployeeDTOInterface e=(EmployeeDTOInterface)other;
  return this.employeeId.equalsIgnoreCase(e.getEmployeeId());
 }

 public int compareTo(EmployeeDTOInterface other)
 {
  return this.employeeId.compareToIgnoreCase(other.getEmployeeId());
 }

 public int hashCode()
 {
  return this.employeeId.toUpperCase().hashCode();
 }
}