package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;
import java.sql.*;

public class EmployeeDAO implements EmployeeDAOInterface 
{
 
 public void add(EmployeeDTOInterface eDTO) throws DAOException 
 {
  if(eDTO==null)
  throw new DAOException("Employee is null"); 
 
  String employeeId=eDTO.getEmployeeId();
  String name=eDTO.getName();
  String PANNumber=eDTO.getPANNumber();
  String aadharCardNumber=eDTO.getAadharCardNumber();
  int designationCode=eDTO.getDesignationCode();
  GENDER gender=eDTO.getGender();
  boolean isIndian=eDTO.getIsIndian();
  BigDecimal basicSalary=eDTO.getBasicSalary();
  java.util.Date dateOfBirth=eDTO.getDateOfBirth();
 
  if(name==null)
  throw new DAOException("Name is null"); 
  name=name.trim();
  if(name.length()==0)
  throw new DAOException("Length of name is 0");
 
  if(PANNumber==null)
  throw new DAOException("PANNumber is null"); 
  PANNumber=PANNumber.trim();
  if(PANNumber.length()==0)
  throw new DAOException("Length of PANNumber is 0");

  if(aadharCardNumber==null)
  throw new DAOException("aadharCardNumber is null"); 
  aadharCardNumber=aadharCardNumber.trim();
  if(aadharCardNumber.length()==0)
  throw new DAOException("Length of aadharCardNumber is 0");
 
  if(designationCode<=0)
  throw new DAOException("Invalid designation code");

  if(dateOfBirth==null)
  throw new DAOException("dateOfBirth is null"); 
  
  if(basicSalary==null)
  throw new DAOException("Basic salary is null");
  if(basicSalary.signum()==-1)
  throw new DAOException("Basic salary is negative");
 
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT title FROM designation WHERE code=?");
   s.setInt(1,designationCode);

   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Designation code: "+designationCode+" does not exist.");
   }
   r.close();s.close();

   s=connection.prepareStatement("SELECT gender FROM employee WHERE pan_number=?");
   s.setString(1,PANNumber);
   r= s.executeQuery();
   boolean panExists= r.next();
   r.close();s.close();

   s=connection.prepareStatement("SELECT gender FROM employee WHERE aadhar_card_number=?");
   s.setString(1,aadharCardNumber);
   r= s.executeQuery();
   boolean aadharExists= r.next();
   r.close();s.close();

   if(panExists&&aadharExists)
   throw new DAOException("Pan number:"+PANNumber+" and Aadhar card number:"+aadharCardNumber+" exists.");
   else if(panExists)
   throw new DAOException("Pan number:"+PANNumber+" exists.");
   else if(aadharExists)
   throw new DAOException("Aadhar card number:"+aadharCardNumber+" exists.");

   s=connection.prepareStatement("INSERT INTO employee (name,pan_number,aadhar_card_number,designation_code,gender,is_indian,basic_salary,date_of_birth) VALUES (?,?,?,?,?,?,?,?)",s.RETURN_GENERATED_KEYS);
   s.setString(1,name);
   s.setString(2,PANNumber);
   s.setString(3,aadharCardNumber);
   s.setInt(4,designationCode);
   s.setString(5,String.valueOf(GenderChar.to(gender)));
   s.setBoolean(6,isIndian);
   s.setBigDecimal(7,basicSalary);
   java.sql.Date sqlDate= new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
   s.setDate(8,sqlDate);

   s.executeUpdate();
   r=s.getGeneratedKeys();
   r.next();
   int generatedEmployeeId=r.getInt(1);
   r.close();s.close();

   eDTO.setEmployeeId("A"+(10000000+generatedEmployeeId));
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }  
 }
 public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
 { 
  if(employeeId==null)
  throw new DAOException("employeeId is null"); 
  employeeId=employeeId.trim();
  if(employeeId.length()==0)
  throw new DAOException("Length of employeeId is 0");  

  String name,PANNumber,aadharCardNumber;
  int designationCode;
  GENDER gender;
  boolean isIndian;
  BigDecimal basicSalary;
  java.util.Date dateOfBirth;  
  EmployeeDTOInterface edto;

  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT * FROM employee WHERE employee_id=?");
   Integer employeeIdInt=Integer.parseInt(employeeId.substring(1))-1000000;
   s.setInt(1,employeeIdInt);
   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Employee id: "+employeeId+" does not exist.");
   }

   employeeId="A"+(1000000+r.getInt("employee_id"));
   name=r.getString("name").trim();
   PANNumber=r.getString("pan_number").trim();
   aadharCardNumber=r.getString("aadhar_card_number").trim();
   designationCode=r.getInt("designation_code");
   gender=GenderChar.from(r.getString("gender").charAt(0));
   isIndian=r.getBoolean("is_indian");
   basicSalary= r.getBigDecimal("basic_salary");
   dateOfBirth=new java.util.Date(r.getDate("date_of_birth").getTime()); 

   r.close();s.close();

   edto= new EmployeeDTO();
   edto.setEmployeeId(employeeId);
   edto.setName(name);
   edto.setPANNumber(PANNumber);
   edto.setAadharCardNumber(aadharCardNumber);
   edto.setDesignationCode(designationCode);
   edto.setGender(gender);
   edto.setIsIndian(isIndian);
   edto.setBasicSalary(basicSalary);
   edto.setDateOfBirth(dateOfBirth);
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
  catch(GenderException ge)
  {
   throw new DAOException(ge.getMessage());
  }
  return edto;
 }
 public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
 { 
  if(aadharCardNumber==null)
  throw new DAOException("aadharCardNumber is null"); 
  aadharCardNumber=aadharCardNumber.trim();
  if(aadharCardNumber.length()==0)
  throw new DAOException("Length of aadharCardNumber is 0");  

  String employeeId,name,PANNumber;
  int designationCode;
  GENDER gender;
  boolean isIndian;
  BigDecimal basicSalary;
  java.util.Date dateOfBirth;  
  EmployeeDTOInterface edto;

  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT * FROM employee WHERE aadhar_card_number=?");
   s.setString(1,aadharCardNumber);
   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Aadhar card number: "+aadharCardNumber+" does not exist.");
   }

   employeeId="A"+(1000000+r.getInt("employee_id"));
   name=r.getString("name").trim();
   PANNumber=r.getString("pan_number").trim();
   aadharCardNumber=r.getString("aadhar_card_number").trim();
   designationCode=r.getInt("designation_code");
   gender=GenderChar.from(r.getString("gender").charAt(0));
   isIndian=r.getBoolean("is_indian");
   basicSalary= r.getBigDecimal("basic_salary");
   dateOfBirth=new java.util.Date(r.getDate("date_of_birth").getTime()); 

   r.close();s.close();

   edto= new EmployeeDTO();
   edto.setEmployeeId(employeeId);
   edto.setName(name);
   edto.setPANNumber(PANNumber);
   edto.setAadharCardNumber(aadharCardNumber);
   edto.setDesignationCode(designationCode);
   edto.setGender(gender);
   edto.setIsIndian(isIndian);
   edto.setBasicSalary(basicSalary);
   edto.setDateOfBirth(dateOfBirth);
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
  catch(GenderException ge)
  {
   throw new DAOException(ge.getMessage());
  }
  return edto;
 }
 public EmployeeDTOInterface getByPANNumber(String PANNumber) throws DAOException
 { 
  if(PANNumber==null)
  throw new DAOException("PANNumber is null"); 
  PANNumber=PANNumber.trim();
  if(PANNumber.length()==0)
  throw new DAOException("Length of PANNumber is 0");  

  String employeeId,name,aadharCardNumber;
  int designationCode;
  GENDER gender;
  boolean isIndian;
  BigDecimal basicSalary;
  java.util.Date dateOfBirth;  
  EmployeeDTOInterface edto;

  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT * FROM employee WHERE pan_card_number=?");
   s.setString(1,PANNumber);
   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Pan card number: "+PANNumber+" does not exist.");
   }

   employeeId="A"+(1000000+r.getInt("employee_id"));
   name=r.getString("name").trim();
   PANNumber=r.getString("pan_number").trim();
   aadharCardNumber=r.getString("aadhar_card_number").trim();
   designationCode=r.getInt("designation_code");
   gender=GenderChar.from(r.getString("gender").charAt(0));
   isIndian=r.getBoolean("is_indian");
   basicSalary= r.getBigDecimal("basic_salary");
   dateOfBirth=new java.util.Date(r.getDate("date_of_birth").getTime()); 

   r.close();s.close();

   edto= new EmployeeDTO();
   edto.setEmployeeId(employeeId);
   edto.setName(name);
   edto.setPANNumber(PANNumber);
   edto.setAadharCardNumber(aadharCardNumber);
   edto.setDesignationCode(designationCode);
   edto.setGender(gender);
   edto.setIsIndian(isIndian);
   edto.setBasicSalary(basicSalary);
   edto.setDateOfBirth(dateOfBirth);
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
  catch(GenderException ge)
  {
   throw new DAOException(ge.getMessage());
  }
  return edto;
 }
 public Set<EmployeeDTOInterface> getAll() throws DAOException
 { 
  Set<EmployeeDTOInterface> set= new TreeSet<>();
  
  String employeeId,name,PANNumber,aadharCardNumber;
  int designationCode;
  GENDER gender;
  boolean isIndian;
  BigDecimal basicSalary;
  java.util.Date dateOfBirth;  
  EmployeeDTOInterface edto;

  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s=connection.prepareStatement("SELECT * FROM employee");
   ResultSet r=s.executeQuery();
   
   while(r.next())
   {
    employeeId="A"+(1000000+r.getInt("employee_id"));
    name=r.getString("name").trim();
    PANNumber=r.getString("pan_number").trim();
    aadharCardNumber=r.getString("aadhar_card_number").trim();
    designationCode=r.getInt("designation_code");
    gender=GenderChar.from(r.getString("gender").charAt(0));
    isIndian=r.getBoolean("is_indian");
    basicSalary= r.getBigDecimal("basic_salary");
    dateOfBirth=new java.util.Date(r.getDate("date_of_birth").getTime()); 

    edto= new EmployeeDTO();
    edto.setEmployeeId(employeeId);
    edto.setName(name);
    edto.setPANNumber(PANNumber);
    edto.setAadharCardNumber(aadharCardNumber);
    edto.setDesignationCode(designationCode);
    edto.setGender(gender);
    edto.setIsIndian(isIndian);
    edto.setBasicSalary(basicSalary);
    edto.setDateOfBirth(dateOfBirth);
    
    set.add(edto);
   }
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
  catch(GenderException ge)
  {
   throw new DAOException(ge.getMessage());
  }
  return set;
 }
 public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException
 { 
  Set<EmployeeDTOInterface> set= new TreeSet<>();
  
  if(designationCode<0)
  throw new DAOException("Invalid code");

  String employeeId,name,PANNumber,aadharCardNumber;
  GENDER gender;
  boolean isIndian;
  BigDecimal basicSalary;
  java.util.Date dateOfBirth;  
  EmployeeDTOInterface edto;

  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s=connection.prepareStatement("SELECT title FROM designation WHERE code=?");
   s.setInt(1,designationCode);
   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Designation code: "+designationCode+" does not exist.");
   }
   r.close();s.close();

   s=connection.prepareStatement("SELECT * FROM employee WHERE designation_code=?");
   s.setInt(1,designationCode);
   r=s.executeQuery();
   
   while(r.next())
   {
    employeeId="A"+(1000000+r.getInt("employee_id"));
    name=r.getString("name").trim();
    PANNumber=r.getString("pan_number").trim();
    aadharCardNumber=r.getString("aadhar_card_number").trim();
    designationCode=r.getInt("designation_code");
    gender=GenderChar.from(r.getString("gender").charAt(0));
    isIndian=r.getBoolean("is_indian");
    basicSalary= r.getBigDecimal("basic_salary");
    dateOfBirth=new java.util.Date(r.getDate("date_of_birth").getTime()); 

    edto= new EmployeeDTO();
    edto.setEmployeeId(employeeId);
    edto.setName(name);
    edto.setPANNumber(PANNumber);
    edto.setAadharCardNumber(aadharCardNumber);
    edto.setDesignationCode(designationCode);
    edto.setGender(gender);
    edto.setIsIndian(isIndian);
    edto.setBasicSalary(basicSalary);
    edto.setDateOfBirth(dateOfBirth);
    
    set.add(edto);
   }
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
  catch(GenderException ge)
  {
   throw new DAOException(ge.getMessage());
  }
  return set;
 }
 public boolean isDesignationAlloted(int designationCode) throws DAOException
 { 
  if(designationCode<0)
  return false;
  
  boolean exists;
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT title FROM designation WHERE code=?");
   s.setInt(1,designationCode);
   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Designation code: "+designationCode+" does not exist.");
   }
   r.close();s.close();

   s=connection.prepareStatement("SELECT gender FROM employee WHERE designation_code=?");
   s.setInt(1,designationCode);
   r= s.executeQuery();
   exists=r.next();
   r.close(); s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
  return exists;
 }
 public int getCountByDesignation(int designationCode) throws DAOException
 { 
  if(designationCode<0)
  throw new DAOException("Invalid code");

  int recordCount;

  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT title FROM designation WHERE code=?");
   s.setInt(1,designationCode);

   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Designation code: "+designationCode+" does not exist.");
   }
   r.close();s.close();


   s=connection.prepareStatement("SELECT count(*) FROM employee WHERE designation_code=?");
   s.setInt(1,designationCode);

   r=s.executeQuery();
   r.next();
   recordCount=r.getInt("count");
   
   r.close(); s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }

  return recordCount;
 }
 public boolean employeeIdExists(String employeeId) throws DAOException
 { 
  if(employeeId==null)
  throw new DAOException("employeeId is null"); 
  employeeId=employeeId.trim();
  if(employeeId.length()==0)
  throw new DAOException("Length of employeeId is 0");  

  boolean exists;
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT gender FROM employee WHERE employee_id=?");
   Integer employeeIdInt=Integer.parseInt(employeeId.substring(1))-1000000;
   s.setInt(1,employeeIdInt);
 
   ResultSet r= s.executeQuery();
   exists=r.next();
   r.close(); s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
  return exists;
 }
 public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
 { 
  if(aadharCardNumber==null)
  throw new DAOException("aadharCardNumber is null"); 
  aadharCardNumber=aadharCardNumber.trim();
  if(aadharCardNumber.length()==0)
  throw new DAOException("Length of aadharCardNumber is 0");  

  boolean exists;
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT gender FROM employee WHERE aadhar_card_number=?");
   s.setString(1,aadharCardNumber);
 
   ResultSet r= s.executeQuery();
   exists=r.next();
   r.close(); s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
  return exists;
 }
 public boolean PANNumberExists(String PANNumber) throws DAOException
 { 
  if(PANNumber==null)
  throw new DAOException("PANNumber is null"); 
  PANNumber=PANNumber.trim();
  if(PANNumber.length()==0)
  throw new DAOException("Length of PANNumber is 0");  

  boolean exists;
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT gender FROM employee WHERE pan_card_number=?");
   s.setString(1,PANNumber);
 
   ResultSet r= s.executeQuery();
   exists=r.next();
   r.close(); s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
  return exists;
 }
 public int getCount() throws DAOException
 {   
  int recordCount;

  try(Connection connection=DAOConnection.getConnection())
  {
   Statement s=connection.createStatement();
   ResultSet r=s.executeQuery("SELECT count(*) FROM employee");

   r.next();
   recordCount=r.getInt("count");
   
   r.close(); s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }

  return recordCount;
 }
 public void update(EmployeeDTOInterface eDTO) throws DAOException 
 {
  if(eDTO==null)
  throw new DAOException("Employee is null"); 
 
  String employeeId=eDTO.getEmployeeId();
  String name=eDTO.getName();
  String PANNumber=eDTO.getPANNumber();
  String aadharCardNumber=eDTO.getAadharCardNumber();
  int designationCode=eDTO.getDesignationCode();
  GENDER gender=eDTO.getGender();
  boolean isIndian=eDTO.getIsIndian();
  BigDecimal basicSalary=eDTO.getBasicSalary();
  java.util.Date dateOfBirth=eDTO.getDateOfBirth();
 
 
  if(employeeId==null)
  throw new DAOException("EmployeeId is null");
  employeeId=employeeId.trim();
  if(employeeId.length()==0)
  throw new DAOException("Length of employeeId is 0");

  if(name==null)
  throw new DAOException("Name is null"); 
  name=name.trim();
  if(name.length()==0)
  throw new DAOException("Length of name is 0");
 
  if(PANNumber==null)
  throw new DAOException("PANNumber is null"); 
  PANNumber=PANNumber.trim();
  if(PANNumber.length()==0)
  throw new DAOException("Length of PANNumber is 0");
 
  if(aadharCardNumber==null)
  throw new DAOException("aadharCardNumber is null"); 
  aadharCardNumber=aadharCardNumber.trim();
  if(aadharCardNumber.length()==0)
  throw new DAOException("Length of aadharCardNumber is 0");
 
  if(designationCode<=0)
  throw new DAOException("Invalid designation code");
 
  if(dateOfBirth==null)
  throw new DAOException("dateOfBirth is null"); 
  
  if(basicSalary==null)
  throw new DAOException("Basic salary is null");
  if(basicSalary.signum()==-1)
  throw new DAOException("Basic salary is negative");
 
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT title FROM designation WHERE code=?");
   s.setInt(1,designationCode);

   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Designation code: "+designationCode+" does not exist.");
   }
   r.close();s.close();

   s=connection.prepareStatement("SELECT gender FROM employee WHERE pan_number=? AND employee_id<>?");
   s.setString(1,PANNumber);
   s.setString(2,employeeId);
   r= s.executeQuery();
   boolean panExists= r.next();
   r.close();s.close();

   s=connection.prepareStatement("SELECT gender FROM employee WHERE aadhar_card_number=? AND employee_id<>?");
   s.setString(1,aadharCardNumber);
   s.setString(2,employeeId);
   r= s.executeQuery();
   boolean aadharExists= r.next();
   r.close();s.close();

   if(panExists&&aadharExists)
   throw new DAOException("Pan number:"+PANNumber+" and Aadhar card number:"+aadharCardNumber+" already belong to another employee(s).");
   else if(panExists)
   throw new DAOException("Pan number:"+PANNumber+" belongs to another employee.");
   else if(aadharExists)
   throw new DAOException("Aadhar card number:"+aadharCardNumber+" belong to another employee.");

   s=connection.prepareStatement("UPDATE employee SET name=?,pan_number=?,aadhar_card_number=?,designation_code=?,gender=?,is_indian=?,basic_salary=?,date_of_birth=? WHERE employee_id=?");
   s.setString(1,name);
   s.setString(2,PANNumber);
   s.setString(3,aadharCardNumber);
   s.setInt(4,designationCode);
   s.setString(5,String.valueOf(GenderChar.to(gender)));
   s.setBoolean(6,isIndian);
   s.setBigDecimal(7,basicSalary);
   java.sql.Date sqlDate= new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
   s.setDate(8,sqlDate);
   Integer employeeIdInt=Integer.parseInt(employeeId.substring(1))-1000000;
   s.setInt(9,employeeIdInt);

   s.executeUpdate();
   r.close();s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
 }
 public void delete(String employeeId) throws DAOException
 { 
  if(employeeId==null)
  throw new DAOException("EmployeeId is null");
  employeeId=employeeId.trim();
  if(employeeId.length()==0)
  throw new DAOException("Length of employeeId is 0");

  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT gender FROM employee WHERE employee_id=?");
   Integer employeeIdInt=Integer.parseInt(employeeId.substring(1))-1000000;
   s.setInt(1,employeeIdInt);
 
   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Employee id: "+employeeId+" does not exist.");
   }

   s=connection.prepareStatement("DELETE FROM employee WHERE employee_id=?");
   s.setInt(1,employeeIdInt);

   s.executeUpdate();
   r.close();s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
 }
}