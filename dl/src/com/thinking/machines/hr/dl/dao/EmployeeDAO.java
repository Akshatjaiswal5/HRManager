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
  Date dateOfBirth=eDTO.getDateOfBirth();
 
 
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
   int lastId=10000000,employeeCount=0;
   
   if(raf.length()==0)
   {
    raf.writeBytes(String.format("%-10d",lastId)+"\n");
    raf.writeBytes(String.format("%-10d",employeeCount)+"\n");
   }
   else
   {
    lastId=Integer.parseInt(raf.readLine().trim());
    employeeCount=Integer.parseInt(raf.readLine().trim());
   }
 
 
   String fPANNumber,fAadharCardNumber;
 
   while(raf.getFilePointer()<raf.length())
   {
    raf.readLine();raf.readLine();
    fPANNumber=raf.readLine();fAadharCardNumber=raf.readLine();
    raf.readLine();raf.readLine();raf.readLine();
    raf.readLine();raf.readLine();  
   

    if(fPANNumber.equalsIgnoreCase(PANNumber))
    {
     throw new DAOException("fPANNumber: "+PANNumber+" exists.");
    }
    if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
    {
     throw new DAOException("AadharCardNumber: "+aadharCardNumber+" exists.");
    }
   }
    
   raf.writeBytes("A"+String.valueOf(lastId+1)+"\n");
   raf.writeBytes(name+"\n");
   raf.writeBytes(PANNumber+"\n");
   raf.writeBytes(aadharCardNumber+"\n");
   raf.writeBytes(designationCode+"\n");
   raf.writeBytes(GenderChar.to(gender)+"\n");
   raf.writeBytes(isIndian+"\n");
   raf.writeBytes(basicSalary.toPlainString()+"\n");
   raf.writeBytes(new SimpleDateFormat("dd/MM/yyyy").format(dateOfBirth)+"\n");
   
   lastId++;
   employeeCount++;
   
   raf.seek(0); 

   raf.writeBytes(String.format("%-10d",lastId)+"\n");
   raf.writeBytes(String.format("%-10d",employeeCount)+"\n");
 
   eDTO.setEmployeeId("A"+lastId);
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
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
   Date fDateOfBirth;  


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
   Date fDateOfBirth;  


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
   Date fDateOfBirth;  


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
   Date fDateOfBirth;  


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
   Date fDateOfBirth;  


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

  File file= new File(FILE_NAME);
  if(!(file.exists()))
  return false;

  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   return false;     
 
   raf.readLine();raf.readLine();  

   int fDesignationCode;

   while(raf.getFilePointer()<raf.length())
   {
    raf.readLine();
    raf.readLine();
    raf.readLine(); 
    raf.readLine();
    fDesignationCode=Integer.parseInt(raf.readLine());
    raf.readLine();
    raf.readLine();
    raf.readLine();
    raf.readLine(); 

    if(fDesignationCode==designationCode)
    {
     raf.close();
     return true;
    }
   }

   return false; 
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public int getCountByDesignation(int designationCode) throws DAOException
 { 
  if(!(new DesignationDAO().codeExists(designationCode)))
  throw new DAOException("Invalid code");

  File file= new File(FILE_NAME);
  if(!(file.exists()))
  return 0;

  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   return 0;     
 
   raf.readLine();raf.readLine();  

   int fDesignationCode;
   int count=0;

   while(raf.getFilePointer()<raf.length())
   {
    raf.readLine();
    raf.readLine();
    raf.readLine(); 
    raf.readLine();
    fDesignationCode=Integer.parseInt(raf.readLine());
    raf.readLine();
    raf.readLine();
    raf.readLine();
    raf.readLine(); 

    if(fDesignationCode==designationCode)
    count++;

   }

   return count; 
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public boolean employeeIdExists(String employeeId) throws DAOException
 { 
  if(employeeId==null)
  throw new DAOException("employeeId is null"); 
  employeeId=employeeId.trim();
  if(employeeId.length()==0)
  throw new DAOException("Length of employeeId is 0");  

  File file= new File(FILE_NAME);
  if(!(file.exists()))
  return false;

  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   return false;     
 
   raf.readLine();raf.readLine();  

   String fEmployeeId; 


   while(raf.getFilePointer()<raf.length())
   {
    fEmployeeId=raf.readLine();
    raf.readLine();
    raf.readLine(); 
    raf.readLine();
    raf.readLine();
    raf.readLine();
    raf.readLine();
    raf.readLine();
    raf.readLine(); 

    if(fEmployeeId.equalsIgnoreCase(employeeId))
    {
     raf.close();
     return true;
    }
   }
   return false;
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
 { 
  if(aadharCardNumber==null)
  throw new DAOException("aadharCardNumber is null"); 
  aadharCardNumber=aadharCardNumber.trim();
  if(aadharCardNumber.length()==0)
  throw new DAOException("Length of aadharCardNumber is 0");  

  File file= new File(FILE_NAME);
  if(!(file.exists()))
  return false;
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   return false;     
 
   raf.readLine();raf.readLine();  

   String fAadharCardNumber;

   while(raf.getFilePointer()<raf.length())
   {
    raf.readLine();
    raf.readLine();
    raf.readLine(); 
    fAadharCardNumber=raf.readLine();
    raf.readLine();
    raf.readLine();
    raf.readLine();
    raf.readLine();
    raf.readLine(); 

    if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
    {
     raf.close();
     return true;
    }
   }
   return false; 
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public boolean PANNumberExists(String PANNumber) throws DAOException
 { 
  if(PANNumber==null)
  throw new DAOException("PANNumber is null"); 
  PANNumber=PANNumber.trim();
  if(PANNumber.length()==0)
  throw new DAOException("Length of PANNumber is 0");  

  File file= new File(FILE_NAME);
  if(!(file.exists()))
  return false;
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   return false;     
 
   raf.readLine();raf.readLine();  

   String fPANNumber;  


   while(raf.getFilePointer()<raf.length())
   {
    raf.readLine();
    raf.readLine();
    fPANNumber=raf.readLine(); 
    raf.readLine();
    raf.readLine();
    raf.readLine();
    raf.readLine();
    raf.readLine();
    raf.readLine(); 

    if(fPANNumber.equalsIgnoreCase(PANNumber))
    {
     raf.close();
     return true;
    }
   }
   return false; 
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public int getCount() throws DAOException
 {   
  File file= new File(FILE_NAME);
  if(!(file.exists()))
  return 0;
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   return 0;     
 
   raf.readLine();
   int recordCount=Integer.parseInt(raf.readLine().trim()); 

   raf.close();
   return recordCount; 
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
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
  Date dateOfBirth=eDTO.getDateOfBirth();
 
 
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