/*package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;

import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dao.*;

import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;

public class EmployeeManager implements EmployeeManagerInterface
{
 private Map<String,EmployeeInterface> employeeIdWiseEmployeesMap;
 private Map<String,EmployeeInterface> PANNumberWiseEmployeesMap;
 private Map<String,EmployeeInterface> aadharCardNumberWiseEmployeesMap;
 private Set<EmployeeInterface> employeesSet;
 private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeesMap;
 private static EmployeeManager employeeManager=null;

 private EmployeeManager() throws BLException
 {
  populateDataStructures();
 }
 private void populateDataStructures() throws BLException
 {
  this.employeeIdWiseEmployeesMap= new HashMap<>();
  this.PANNumberWiseEmployeesMap= new HashMap<>();
  this.aadharCardNumberWiseEmployeesMap= new HashMap<>();
  this.employeesSet= new TreeSet<>();
  this.designationCodeWiseEmployeesMap=new HashMap<>();

  try
  {
   Set<EmployeeDTOInterface> dlEmployees= new EmployeeDAO().getAll();
   
   DesignationManagerInterface designationManager= DesignationManager.getInstance();
   for(EmployeeDTOInterface dlEmployee:dlEmployees)
   {
    EmployeeInterface employee= new Employee();
    employee.setEmployeeId(dlEmployee.getEmployeeId());
    employee.setName(dlEmployee.getName());
    employee.setPANNumber(dlEmployee.getPANNumber());
    employee.setAadharCardNumber(dlEmployee.getAadharCardNumber());
    employee.setDesignation(designationManager.getDesignationByCode(dlEmployee.getDesignationCode()));
    employee.setGender(dlEmployee.getGender());
    employee.setIsIndian(dlEmployee.getIsIndian());
    employee.setBasicSalary(dlEmployee.getBasicSalary());
    employee.setDateOfBirth(dlEmployee.getDateOfBirth());

    this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(),employee);
    this.PANNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(),employee);
    this.aadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber().toUpperCase(),employee);
    this.employeesSet.add(employee);

    Set<EmployeeInterface> s= this.designationCodeWiseEmployeesMap.get(employee.getDesignation().getCode());
    if(s==null)
    {
     s= new TreeSet<>();
     s.add(employee);
     this.designationCodeWiseEmployeesMap.put(employee.getDesignation().getCode(),s);
    }
    else
    {
     s.add(employee);
    }
   } 
  }
  catch(DAOException daoe)
  {
   BLException ble= new BLException();
   ble.setGenericException(daoe.getMessage());
   throw ble;
  }  
 }

 public static EmployeeManagerInterface getInstance() throws BLException
 {
  if(employeeManager==null)
  employeeManager= new EmployeeManager();

  return employeeManager;
 }



 public void addEmployee(EmployeeInterface employee) throws BLException
 {
  //validation of employee object given//

  BLException ble= new BLException();
  
  if(employee==null)
  {
   ble.setGenericException("Invalid Object");
   throw ble;
  }

  String employeeId=employee.getEmployeeId();
  String name=employee.getName();
  String PANNumber=employee.getPANNumber();
  String aadharCardNumber=employee.getAadharCardNumber();
  DesignationInterface designation=employee.getDesignation();
  GENDER gender=employee.getGender();
  boolean isIndian=employee.getIsIndian();
  BigDecimal basicSalary=employee.getBasicSalary();
  Date dateOfBirth=employee.getDateOfBirth();
  
  DesignationManagerInterface designationManager= DesignationManager.getInstance();

  if(employeeId!=null)
  {
   employeeId=employeeId.trim();
   if(employeeId.length()!=0)
   ble.addException("employeeId","employeeId must be nil/empty");
  }

  if(name==null)
  {
   ble.addException("name","Name is required"); 
  }
  else
  {
   name=name.trim();
   if(name.length()==0)
   ble.addException("name","Length of name is 0");
  }


  if(PANNumber==null)
  {
   ble.addException("PANNumber","PANNumber is required"); 
  }
  else
  { 
   PANNumber=PANNumber.trim();
   if(PANNumber.length()==0)
   ble.addException("PANNumber","Length of PANNumber is 0");
  }

  if(aadharCardNumber==null)
  {
   ble.addException("aadharCardNumber","aadharCardNumber is required"); 
  }
  else
  { 
   aadharCardNumber=aadharCardNumber.trim();
   if(aadharCardNumber.length()==0)
   ble.addException("aadharCardNumber","Length of aadharCardNumber is 0");
  }

  if(designation==null)
  {
   ble.addException("designation","designation is required"); 
  }
  else
  { 
   if(!designationManager.designationCodeExists(designation.getCode()))
   ble.addException("designation","Invalid Designation");
  }

  if(dateOfBirth==null)
  {
   ble.addException("dateOfBirth","dateOfBirth is required"); 
  }

  if(basicSalary==null)
  {
   ble.addException("basicSalary","basicSalary is required"); 
  }
  else
  { 
   if(basicSalary.signum()==-1)
   ble.addException("basicSalary","basicSalary is negative");
  }
  

  //checking duplicacy of PANNumber and aadharCardNumber//

  if(PANNumber!=null&&PANNumber.length()>0)
  {
   if(this.PANNumberWiseEmployeesMap.containsKey(PANNumber.toUpperCase()))
   ble.addException("PANNumber","PANNumber already exists");
  }
 
  if(aadharCardNumber!=null&&aadharCardNumber.length()>0)
  {
   if(this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase()))
   ble.addException("aadharCardNumber","aadharCardNumber already exists");
  }

  if(ble.hasExceptions())
  throw ble;

  try
  {
   EmployeeDTOInterface employeeDTO= new EmployeeDTO();
   
   employeeDTO.setName(name);
   employeeDTO.setPANNumber(PANNumber);
   employeeDTO.setAadharCardNumber(aadharCardNumber);
   employeeDTO.setDesignationCode(designation.getCode());
   employeeDTO.setGender(gender);
   employeeDTO.setIsIndian(isIndian);
   employeeDTO.setBasicSalary(basicSalary);
   employeeDTO.setDateOfBirth(dateOfBirth);
   
   EmployeeDAOInterface employeeDAO= new EmployeeDAO();
   employeeDAO.add(employeeDTO);

   employeeId=employeeDTO.getEmployeeId();
  }
  catch(DAOException daoe)
  {
   ble.setGenericException(daoe.getMessage());
   throw ble;
  }
  
  EmployeeInterface dsEmployee= new Employee();

  dsEmployee.setEmployeeId(employeeId);
  dsEmployee.setName(name);
  dsEmployee.setPANNumber(PANNumber);
  dsEmployee.setAadharCardNumber(aadharCardNumber);
  dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
  dsEmployee.setGender(gender);
  dsEmployee.setIsIndian(isIndian);
  dsEmployee.setBasicSalary(basicSalary);
  dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());

  this.employeeIdWiseEmployeesMap.put(employeeId.toUpperCase(),dsEmployee);
  this.PANNumberWiseEmployeesMap.put(PANNumber.toUpperCase(),dsEmployee);
  this.aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
  this.employeesSet.add(dsEmployee); 

  Set<EmployeeInterface> s= this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
  if(s==null)
  {
   s= new TreeSet<>();
   s.add(dsEmployee);
   this.designationCodeWiseEmployeesMap.put(dsEmployee.getDesignation().getCode(),s);
  }
  else
  {
   s.add(dsEmployee);
  }

  employee.setEmployeeId(employeeId);  
 }

 public void updateEmployee(EmployeeInterface employee) throws BLException
 {
  //validation of employee object given//

  BLException ble= new BLException();
  
  if(employee==null)
  {
   ble.setGenericException("Invalid Object");
   throw ble;
  }

  String employeeId=employee.getEmployeeId();
  String name=employee.getName();
  String PANNumber=employee.getPANNumber();
  String aadharCardNumber=employee.getAadharCardNumber();
  DesignationInterface designation=employee.getDesignation();
  GENDER gender=employee.getGender();
  boolean isIndian=employee.getIsIndian();
  BigDecimal basicSalary=employee.getBasicSalary();
  Date dateOfBirth=employee.getDateOfBirth();
  
  DesignationManagerInterface designationManager= DesignationManager.getInstance();

  if(employeeId==null)
  {
   ble.addException("employeeId","employeeID is required"); 
  }
  else
  {
   employeeId=employeeId.trim();
   if(employeeId.length()==0)
   ble.addException("employeeId","Length of employeeID is 0");
   else if(!employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase()))
   ble.addException("employeeId","employeeId doesn't exist");
  }

  if(name==null)
  {
   ble.addException("name","Name is required"); 
  }
  else
  {
   name=name.trim();
   if(name.length()==0)
   ble.addException("name","Length of name is 0");
  }


  if(PANNumber==null)
  {
   ble.addException("PANNumber","PANNumber is required"); 
  }
  else
  { 
   PANNumber=PANNumber.trim();
   if(PANNumber.length()==0)
   ble.addException("PANNumber","Length of PANNumber is 0");
  }

  if(aadharCardNumber==null)
  {
   ble.addException("aadharCardNumber","aadharCardNumber is required"); 
  }
  else
  { 
   aadharCardNumber=aadharCardNumber.trim();
   if(aadharCardNumber.length()==0)
   ble.addException("aadharCardNumber","Length of aadharCardNumber is 0");
  }

  if(designation==null)
  {
   ble.addException("designation","designation is required"); 
  }
  else
  { 
   if(!designationManager.designationCodeExists(designation.getCode()))
   ble.addException("designation","Invalid Designation");
  }

  if(dateOfBirth==null)
  {
   ble.addException("dateOfBirth","dateOfBirth is required"); 
  }

  if(basicSalary==null)
  {
   ble.addException("basicSalary","basicSalary is required"); 
  }
  else
  { 
   if(basicSalary.signum()==-1)
   ble.addException("basicSalary","basicSalary is negative");
  }
  

  //checking duplicacy of PANNumber and aadharCardNumber//

  if(PANNumber!=null&&PANNumber.length()>0)
  {
   EmployeeInterface E= PANNumberWiseEmployeesMap.get(PANNumber.toUpperCase());
   if(E!=null&&E.getEmployeeId().equalsIgnoreCase(employeeId)==false)
   ble.addException("PANNumber","PANNumber already exists with another Employee");
  }
  
  if(aadharCardNumber!=null&&aadharCardNumber.length()>0)
  {
   EmployeeInterface E= aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
   if(E!=null&&E.getEmployeeId().equalsIgnoreCase(employeeId)==false)
   ble.addException("aadharCardNumber","aadharCardNumber already exists with another Employee");
  }
 
  
  if(ble.hasExceptions())
  throw ble;

  try
  {
   EmployeeDTOInterface employeeDTO= new EmployeeDTO();
   
   employeeDTO.setEmployeeId(employeeId);
   employeeDTO.setName(name);
   employeeDTO.setPANNumber(PANNumber);
   employeeDTO.setAadharCardNumber(aadharCardNumber);
   employeeDTO.setDesignationCode(designation.getCode());
   employeeDTO.setGender(gender);
   employeeDTO.setIsIndian(isIndian);
   employeeDTO.setBasicSalary(basicSalary);
   employeeDTO.setDateOfBirth(dateOfBirth);
   
   EmployeeDAOInterface employeeDAO= new EmployeeDAO();
   employeeDAO.update(employeeDTO);
  }
  catch(DAOException daoe)
  {
   ble.setGenericException(daoe.getMessage());
   throw ble;
  }
  
  EmployeeInterface oldEmployee= this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
    
  employeeIdWiseEmployeesMap.remove(oldEmployee.getEmployeeId().toUpperCase());
  PANNumberWiseEmployeesMap.remove(oldEmployee.getPANNumber().toUpperCase());
  aadharCardNumberWiseEmployeesMap.remove(oldEmployee.getAadharCardNumber().toUpperCase());
  employeesSet.remove(oldEmployee);
  
  Set<EmployeeInterface> s= this.designationCodeWiseEmployeesMap.get(oldEmployee.getDesignation().getCode());
  s.remove(oldEmployee);


  EmployeeInterface newEmployee= new Employee();

  newEmployee.setEmployeeId(employeeId);
  newEmployee.setName(name);
  newEmployee.setPANNumber(PANNumber);
  newEmployee.setAadharCardNumber(aadharCardNumber);
  newEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
  newEmployee.setGender(gender);
  newEmployee.setIsIndian(isIndian);
  newEmployee.setBasicSalary(basicSalary);
  newEmployee.setDateOfBirth((Date)dateOfBirth.clone());

  this.employeeIdWiseEmployeesMap.put(employeeId.toUpperCase(),newEmployee);
  this.PANNumberWiseEmployeesMap.put(PANNumber.toUpperCase(),newEmployee);
  this.aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),newEmployee);
  this.employeesSet.add(newEmployee); 

  s= this.designationCodeWiseEmployeesMap.get(newEmployee.getDesignation().getCode());
  if(s==null)
  {
   s= new TreeSet<>();
   s.add(newEmployee);
   this.designationCodeWiseEmployeesMap.put(newEmployee.getDesignation().getCode(),s);
  }
  else
  {
   s.add(newEmployee);
  }

 }
 
 public void deleteEmployee(String employeeId) throws BLException
 {
  BLException ble= new BLException();

  if(employeeId==null)
  {
   ble.addException("employeeId","employeeID is required"); 
  }
  else
  {
   employeeId=employeeId.trim();
   if(employeeId.length()==0)
   ble.addException("employeeId","Length of employeeID is 0");
   else if(!employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase()))
   ble.addException("employeeId","employeeId doesn't exist");
  }

  if(ble.hasExceptions())
  throw ble;

  try
  {
   EmployeeDAOInterface employeeDAO= new EmployeeDAO();
   employeeDAO.delete(employeeId);
  }
  catch(DAOException daoe)
  {
   ble.setGenericException(daoe.getMessage());
   throw ble;
  }

  EmployeeInterface oldEmployee= this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
    
  employeeIdWiseEmployeesMap.remove(oldEmployee.getEmployeeId().toUpperCase());
  PANNumberWiseEmployeesMap.remove(oldEmployee.getPANNumber().toUpperCase());
  aadharCardNumberWiseEmployeesMap.remove(oldEmployee.getAadharCardNumber().toUpperCase());
  employeesSet.remove(oldEmployee);
  
  Set<EmployeeInterface> s= this.designationCodeWiseEmployeesMap.get(oldEmployee.getDesignation().getCode());
  s.remove(oldEmployee);
 }

 
 public Set<EmployeeInterface> getEmployees()
 {
  Set<EmployeeInterface> cloneEmployeesSet= new TreeSet<>();    
 
  for(EmployeeInterface employee: this.employeesSet)
  {
   EmployeeInterface cloneEmployee= new Employee();
   
   cloneEmployee.setEmployeeId(employee.getEmployeeId());
   cloneEmployee.setName(employee.getName());
   cloneEmployee.setPANNumber(employee.getPANNumber());
   cloneEmployee.setAadharCardNumber(employee.getAadharCardNumber());

   DesignationInterface designation= employee.getDesignation();
   DesignationInterface cloneDesignation= new Designation();
   cloneDesignation.setCode(designation.getCode());
   cloneDesignation.setTitle(designation.getTitle());
   
   cloneEmployee.setDesignation(cloneDesignation);
   cloneEmployee.setGender(employee.getGender());
   cloneEmployee.setIsIndian(employee.getIsIndian());
   cloneEmployee.setBasicSalary(employee.getBasicSalary());
   cloneEmployee.setDateOfBirth((Date)employee.getDateOfBirth().clone());

   cloneEmployeesSet.add(cloneEmployee);
  }
  return cloneEmployeesSet;
 }

 public EmployeeInterface getEmployeeByEmployeeId(String employeeId) throws BLException
 {
  BLException ble= new BLException();

  if(employeeId==null)
  {
   ble.addException("employeeId","employeeID is required"); 
  }
  else
  {
   employeeId=employeeId.trim();
   if(employeeId.length()==0)
   ble.addException("employeeId","Length of employeeID is 0");
  }

  EmployeeInterface employee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
  
  if(employee==null)
  ble.addException("employeeId","employeeId doesn't exist");

  
  if(ble.hasExceptions())
  throw ble;

  EmployeeInterface cloneEmployee= new Employee();  
  cloneEmployee.setEmployeeId(employee.getEmployeeId());
  cloneEmployee.setName(employee.getName());
  cloneEmployee.setPANNumber(employee.getPANNumber());
  cloneEmployee.setAadharCardNumber(employee.getAadharCardNumber());
  
  DesignationInterface designation= employee.getDesignation();
  DesignationInterface cloneDesignation= new Designation();
  cloneDesignation.setCode(designation.getCode());
  cloneDesignation.setTitle(designation.getTitle());
  
  cloneEmployee.setDesignation(cloneDesignation);
  cloneEmployee.setGender(employee.getGender());
  cloneEmployee.setIsIndian(employee.getIsIndian());
  cloneEmployee.setBasicSalary(employee.getBasicSalary());
  cloneEmployee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
  
  return cloneEmployee;
 }

 public EmployeeInterface getEmployeeByPANNumber(String PANNumber) throws BLException
 {
  BLException ble= new BLException();

  if(PANNumber==null)
  {
   ble.addException("PANNumber","PANNumber is required"); 
  }
  else
  {
   PANNumber=PANNumber.trim();
   if(PANNumber.length()==0)
   ble.addException("PANNumber","Length of PANNumber is 0");
  }

  EmployeeInterface employee=this.PANNumberWiseEmployeesMap.get(PANNumber.toUpperCase());
  
  if(employee==null)
  ble.addException("PANNumber","PANNumber doesn't exist");

  
  if(ble.hasExceptions())
  throw ble;

  EmployeeInterface cloneEmployee= new Employee();  
  cloneEmployee.setEmployeeId(employee.getEmployeeId());
  cloneEmployee.setName(employee.getName());
  cloneEmployee.setPANNumber(employee.getPANNumber());
  cloneEmployee.setAadharCardNumber(employee.getAadharCardNumber());
  
  DesignationInterface designation= employee.getDesignation();
  DesignationInterface cloneDesignation= new Designation();
  cloneDesignation.setCode(designation.getCode());
  cloneDesignation.setTitle(designation.getTitle());
  
  cloneEmployee.setDesignation(cloneDesignation);
  cloneEmployee.setGender(employee.getGender());
  cloneEmployee.setIsIndian(employee.getIsIndian());
  cloneEmployee.setBasicSalary(employee.getBasicSalary());
  cloneEmployee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
  
  return cloneEmployee;
 }

 public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException
 {
  BLException ble= new BLException();

  if(aadharCardNumber==null)
  {
   ble.addException("aadharCardNumber","aadharCardNumber is required"); 
  }
  else
  {
   aadharCardNumber=aadharCardNumber.trim();
   if(aadharCardNumber.length()==0)
   ble.addException("aadharCardNumber","Length of aadharCardNumber is 0");
  }

  EmployeeInterface employee=this.aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
  
  if(employee==null)
  ble.addException("aadharCardNumber","aadharCardNumber doesn't exist");

  
  if(ble.hasExceptions())
  throw ble;

  EmployeeInterface cloneEmployee= new Employee();  
  cloneEmployee.setEmployeeId(employee.getEmployeeId());
  cloneEmployee.setName(employee.getName());
  cloneEmployee.setPANNumber(employee.getPANNumber());
  cloneEmployee.setAadharCardNumber(employee.getAadharCardNumber());
  
  DesignationInterface designation= employee.getDesignation();
  DesignationInterface cloneDesignation= new Designation();
  cloneDesignation.setCode(designation.getCode());
  cloneDesignation.setTitle(designation.getTitle());
  
  cloneEmployee.setDesignation(cloneDesignation);
  cloneEmployee.setGender(employee.getGender());
  cloneEmployee.setIsIndian(employee.getIsIndian());
  cloneEmployee.setBasicSalary(employee.getBasicSalary());
  cloneEmployee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
  
  return cloneEmployee;
 }

 public boolean employeeIdExists(String employeeId)
 {
  return this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase());
 }
 public boolean PANNumberExists(String PANNumber)
 {
  return this.PANNumberWiseEmployeesMap.containsKey(PANNumber.toUpperCase());
 }
 public boolean aadharCardNumberExists(String aadharCardNumber) 
 {
  return this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase());
 }
 public int getEmployeeCount()
 {
  return this.employeesSet.size();
 }
 public boolean isDesignationAlloted(int designationCode) throws BLException
 {
  DesignationManagerInterface designationManager= DesignationManager.getInstance();
  if(designationCode<=0||!designationManager.designationCodeExists(designationCode))
  {
   BLException ble= new BLException();
   ble.setGenericException("Invalid designation code");
   throw ble;
  }

  Set<EmployeeInterface> s= this.designationCodeWiseEmployeesMap.get(designationCode);
  if(s==null)
  return false;
  else
  return s.size()>0;  

 }
 public int getEmployeeCountByDesignation(int designationCode) throws BLException
 {
  DesignationManagerInterface designationManager= DesignationManager.getInstance();
  if(designationCode<=0||!designationManager.designationCodeExists(designationCode))
  {
   BLException ble= new BLException();
   ble.setGenericException("Invalid designation code");
   throw ble;
  }

  Set<EmployeeInterface> s= this.designationCodeWiseEmployeesMap.get(designationCode);
  if(s==null)
  return 0;
  else
  return s.size();  
 }
 public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode) throws BLException
 {
  DesignationManagerInterface designationManager= DesignationManager.getInstance();
  if(designationCode<=0||!designationManager.designationCodeExists(designationCode))
  {
   BLException ble= new BLException();
   ble.setGenericException("Invalid designation code");
   throw ble;
  }

  Set<EmployeeInterface> s= this.designationCodeWiseEmployeesMap.get(designationCode);
  Set<EmployeeInterface> cloneS= new TreeSet<>();

  if(s==null)
  return cloneS;
  
  for(EmployeeInterface employee: s)
  {
   EmployeeInterface cloneEmployee= new Employee();
   
   cloneEmployee.setEmployeeId(employee.getEmployeeId());
   cloneEmployee.setName(employee.getName());
   cloneEmployee.setPANNumber(employee.getPANNumber());
   cloneEmployee.setAadharCardNumber(employee.getAadharCardNumber());

   DesignationInterface designation= employee.getDesignation();
   DesignationInterface cloneDesignation= new Designation();
   cloneDesignation.setCode(designation.getCode());
   cloneDesignation.setTitle(designation.getTitle());
   
   cloneEmployee.setDesignation(cloneDesignation);
   cloneEmployee.setGender(employee.getGender());
   cloneEmployee.setIsIndian(employee.getIsIndian());
   cloneEmployee.setBasicSalary(employee.getBasicSalary());
   cloneEmployee.setDateOfBirth((Date)employee.getDateOfBirth().clone());

   cloneS.add(cloneEmployee);
  }
  return cloneS;
 
 } 
}
*/
