package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.io.*;
import java.math.*;
import java.text.*;
import java.sql.*;

public class EmployeeDAO implements EmployeeDAOInterface 
{
 private final static String FILE_NAME ="employee.data";
 
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

  File file= new File(FILE_NAME);
  if(!(file.exists()))
  throw new DAOException("Not Found");
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   throw new DAOException("Not Found");     
 
   raf.readLine();raf.readLine();  

   String fEmployeeId,fName,fPANNumber,fAadharCardNumber;
   int fDesignationCode;
   GENDER fGender;
   boolean fIsIndian;
   BigDecimal fBasicSalary;
   java.util.Date fDateOfBirth;  


   while(raf.getFilePointer()<raf.length())
   {
    fEmployeeId=raf.readLine();
    fName=raf.readLine();
    fPANNumber=raf.readLine(); 
    fAadharCardNumber=raf.readLine();
    fDesignationCode=Integer.parseInt(raf.readLine());
    fGender=GenderChar.from(raf.readLine().charAt(0));
    fIsIndian=Boolean.parseBoolean(raf.readLine());
    fBasicSalary= new BigDecimal(raf.readLine());
    fDateOfBirth=new SimpleDateFormat("dd/MM/yyyy").parse(raf.readLine()); 

    if(fEmployeeId.equalsIgnoreCase(employeeId))
    {
     EmployeeDTOInterface edto= new EmployeeDTO();
     edto.setEmployeeId(fEmployeeId);
     edto.setName(fName);
     edto.setPANNumber(fPANNumber);
     edto.setAadharCardNumber(fAadharCardNumber);
     edto.setDesignationCode(fDesignationCode);
     edto.setGender(fGender);
     edto.setIsIndian(fIsIndian);
     edto.setBasicSalary(fBasicSalary);
     edto.setDateOfBirth(fDateOfBirth);
 
     raf.close();
     return edto;
    }
   }
   throw new DAOException("Not Found"); 
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
  catch(ParseException pe)
  {
   throw new DAOException(pe.getMessage());
  }
  catch(GenderException ge)
  {
   throw new DAOException(ge.getMessage());
  }
 }
 public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
 { 
  if(aadharCardNumber==null)
  throw new DAOException("aadharCardNumber is null"); 
  aadharCardNumber=aadharCardNumber.trim();
  if(aadharCardNumber.length()==0)
  throw new DAOException("Length of aadharCardNumber is 0");  

  File file= new File(FILE_NAME);
  if(!(file.exists()))
  throw new DAOException("Not Found");
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   throw new DAOException("Not Found");     
 
   raf.readLine();raf.readLine();  

   String fEmployeeId,fName,fPANNumber,fAadharCardNumber;
   int fDesignationCode;
   GENDER fGender;
   boolean fIsIndian;
   BigDecimal fBasicSalary;
   java.util.Date fDateOfBirth;  


   while(raf.getFilePointer()<raf.length())
   {
    fEmployeeId=raf.readLine();
    fName=raf.readLine();
    fPANNumber=raf.readLine(); 
    fAadharCardNumber=raf.readLine();
    fDesignationCode=Integer.parseInt(raf.readLine());
    fGender=GenderChar.from(raf.readLine().charAt(0));
    fIsIndian=Boolean.parseBoolean(raf.readLine());
    fBasicSalary= new BigDecimal(raf.readLine());
    fDateOfBirth=new SimpleDateFormat("dd/MM/yyyy").parse(raf.readLine()); 

    if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
    {
     EmployeeDTOInterface edto= new EmployeeDTO();
     edto.setEmployeeId(fEmployeeId);
     edto.setName(fName);
     edto.setPANNumber(fPANNumber);
     edto.setAadharCardNumber(fAadharCardNumber);
     edto.setDesignationCode(fDesignationCode);
     edto.setGender(fGender);
     edto.setIsIndian(fIsIndian);
     edto.setBasicSalary(fBasicSalary);
     edto.setDateOfBirth(fDateOfBirth);
 
     raf.close();
     return edto;
    }
   }
   throw new DAOException("Not Found"); 
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
  catch(ParseException pe)
  {
   throw new DAOException(pe.getMessage());
  }
  catch(GenderException ge)
  {
   throw new DAOException(ge.getMessage());
  }
 }
 public EmployeeDTOInterface getByPANNumber(String PANNumber) throws DAOException
 { 
  if(PANNumber==null)
  throw new DAOException("PANNumber is null"); 
  PANNumber=PANNumber.trim();
  if(PANNumber.length()==0)
  throw new DAOException("Length of PANNumber is 0");  

  File file= new File(FILE_NAME);
  if(!(file.exists()))
  throw new DAOException("Not Found");
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   throw new DAOException("Not Found");     
 
   raf.readLine();raf.readLine();  

   String fEmployeeId,fName,fPANNumber,fAadharCardNumber;
   int fDesignationCode;
   GENDER fGender;
   boolean fIsIndian;
   BigDecimal fBasicSalary;
   java.util.Date fDateOfBirth;  


   while(raf.getFilePointer()<raf.length())
   {
    fEmployeeId=raf.readLine();
    fName=raf.readLine();
    fPANNumber=raf.readLine(); 
    fAadharCardNumber=raf.readLine();
    fDesignationCode=Integer.parseInt(raf.readLine());
    fGender=GenderChar.from(raf.readLine().charAt(0));
    fIsIndian=Boolean.parseBoolean(raf.readLine());
    fBasicSalary= new BigDecimal(raf.readLine());
    fDateOfBirth=new SimpleDateFormat("dd/MM/yyyy").parse(raf.readLine()); 

    if(fPANNumber.equalsIgnoreCase(PANNumber))
    {
     EmployeeDTOInterface edto= new EmployeeDTO();
     edto.setEmployeeId(fEmployeeId);
     edto.setName(fName);
     edto.setPANNumber(fPANNumber);
     edto.setAadharCardNumber(fAadharCardNumber);
     edto.setDesignationCode(fDesignationCode);
     edto.setGender(fGender);
     edto.setIsIndian(fIsIndian);
     edto.setBasicSalary(fBasicSalary);
     edto.setDateOfBirth(fDateOfBirth);
 
     raf.close();
     return edto;
    }
   }
   throw new DAOException("Not Found"); 
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
  catch(ParseException pe)
  {
   throw new DAOException(pe.getMessage());
  }
  catch(GenderException ge)
  {
   throw new DAOException(ge.getMessage());
  }
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
  return set;

  File file= new File(FILE_NAME);
  if(!(file.exists()))
  return set;

  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   return set;     
 
   raf.readLine();raf.readLine();  

   String fEmployeeId,fName,fPANNumber,fAadharCardNumber;
   int fDesignationCode;
   GENDER fGender;
   boolean fIsIndian;
   BigDecimal fBasicSalary;
   java.util.Date fDateOfBirth;  


   while(raf.getFilePointer()<raf.length())
   {
    fEmployeeId=raf.readLine();
    fName=raf.readLine();
    fPANNumber=raf.readLine(); 
    fAadharCardNumber=raf.readLine();
    fDesignationCode=Integer.parseInt(raf.readLine());
    fGender=GenderChar.from(raf.readLine().charAt(0));
    fIsIndian=Boolean.parseBoolean(raf.readLine());
    fBasicSalary= new BigDecimal(raf.readLine());
    fDateOfBirth=new SimpleDateFormat("dd/MM/yyyy").parse(raf.readLine()); 

    if(fDesignationCode==designationCode)
    {
     EmployeeDTOInterface edto= new EmployeeDTO();
     edto.setEmployeeId(fEmployeeId);
     edto.setName(fName);
     edto.setPANNumber(fPANNumber);
     edto.setAadharCardNumber(fAadharCardNumber);
     edto.setDesignationCode(fDesignationCode);
     edto.setGender(fGender);
     edto.setIsIndian(fIsIndian);
     edto.setBasicSalary(fBasicSalary);
     edto.setDateOfBirth(fDateOfBirth);
   
     set.add(edto);
    }
   }

   return set; 
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
  catch(ParseException pe)
  {
   throw new DAOException(pe.getMessage());
  }
  catch(GenderException ge)
  {
   throw new DAOException(ge.getMessage());
  }
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
  if(!(new DesignationDAO().codeExists(designationCode)))
  throw new DAOException("Invalid designation code");
 
  if(dateOfBirth==null)
  throw new DAOException("dateOfBirth is null"); 
  
  if(basicSalary==null)
  throw new DAOException("Basic salary is null");
  if(basicSalary.signum()==-1)
  throw new DAOException("Basic salary is negative");
 
  File file= new File(FILE_NAME);
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   throw new DAOException("Not found");

   raf.readLine();raf.readLine();
  
   String fEmployeeId,fPANNumber,fAadharCardNumber;
   boolean found=false;

   while(raf.getFilePointer()<raf.length())
   {
    fEmployeeId=raf.readLine();raf.readLine();
    fPANNumber=raf.readLine();fAadharCardNumber=raf.readLine();
    raf.readLine();raf.readLine();raf.readLine();
    raf.readLine();raf.readLine();  
   
    if(fEmployeeId.equalsIgnoreCase(employeeId))
    { 
     found=true;
     continue;
    }

    if(fPANNumber.equalsIgnoreCase(PANNumber))
    throw new DAOException("fPANNumber: "+PANNumber+" exists.");

    if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
    throw new DAOException("AadharCardNumber: "+aadharCardNumber+" exists.");

   }
    
   if(!found)
   throw new DAOException("employeeId does not exist");

   RandomAccessFile tmpRaf = new RandomAccessFile("TMP.dat","rw");
   
   tmpRaf.setLength(0);
   raf.seek(0);

   tmpRaf.writeBytes(raf.readLine()+"\n");
   tmpRaf.writeBytes(raf.readLine()+"\n");

   while(raf.getFilePointer()<raf.length())
   {
    fEmployeeId=raf.readLine();
    if(!fEmployeeId.equalsIgnoreCase(employeeId))
    {
     tmpRaf.writeBytes(fEmployeeId+"\n");
     for(int i=0;i<8;i++)
     tmpRaf.writeBytes(raf.readLine()+"\n");   
    }
    else
    {
     tmpRaf.writeBytes(employeeId+"\n");
     tmpRaf.writeBytes(name+"\n");
     tmpRaf.writeBytes(PANNumber+"\n");
     tmpRaf.writeBytes(aadharCardNumber+"\n");
     tmpRaf.writeBytes(designationCode+"\n");
     tmpRaf.writeBytes(GenderChar.to(gender)+"\n");
     tmpRaf.writeBytes(isIndian+"\n");
     tmpRaf.writeBytes(basicSalary.toPlainString()+"\n");
     tmpRaf.writeBytes(new SimpleDateFormat("dd/MM/yyyy").format(dateOfBirth)+"\n");
     for(int i=0;i<8;i++)
     raf.readLine();
    }      
   }
   
   raf.setLength(0);
   tmpRaf.seek(0);

   while(tmpRaf.getFilePointer()<tmpRaf.length())
   {
    raf.writeBytes(tmpRaf.readLine()+"\n");
   }
   tmpRaf.setLength(0);
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public void delete(String employeeId) throws DAOException
 { 
  if(employeeId==null)
  throw new DAOException("EmployeeId is null");
  employeeId=employeeId.trim();
  if(employeeId.length()==0)
  throw new DAOException("Length of employeeId is 0");

  File file= new File(FILE_NAME);
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   throw new DAOException("Not found");

   raf.readLine();raf.readLine();
  
   String fEmployeeId;
   boolean found=false;

   while(raf.getFilePointer()<raf.length())
   {
    fEmployeeId=raf.readLine();raf.readLine();
    raf.readLine();raf.readLine();
    raf.readLine();raf.readLine();raf.readLine();
    raf.readLine();raf.readLine();  
   
    if(fEmployeeId.equalsIgnoreCase(employeeId))
    { 
     found=true;
     break;
    }

   }
    
   if(!found)
   throw new DAOException("Not found.");

   RandomAccessFile tmpRaf = new RandomAccessFile("TMP.dat","rw");
   
   tmpRaf.setLength(0);
   raf.seek(0);

   tmpRaf.writeBytes(raf.readLine()+"\n");
   int employeeCount= Integer.parseInt(raf.readLine().trim());
   tmpRaf.writeBytes(String.format("%-10d",employeeCount-1)+"\n");

   while(raf.getFilePointer()<raf.length())
   {
    fEmployeeId=raf.readLine();
    if(!fEmployeeId.equalsIgnoreCase(employeeId))
    {
     tmpRaf.writeBytes(fEmployeeId+"\n");
     for(int i=0;i<8;i++)
     tmpRaf.writeBytes(raf.readLine()+"\n");   
    }
    else
    {
     for(int i=0;i<8;i++)
     raf.readLine();
    }      
   }
   
   raf.setLength(0);
   tmpRaf.seek(0);

   while(tmpRaf.getFilePointer()<tmpRaf.length())
   {
    raf.writeBytes(tmpRaf.readLine()+"\n");
   }
   tmpRaf.setLength(0);
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
}