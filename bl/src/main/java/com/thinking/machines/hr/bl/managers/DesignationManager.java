package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import java.util.*;
public class DesignationManager implements DesignationManagerInterface
{
 private Map<Integer,DesignationInterface> codeWiseDesignationsMap;
 private Map<String,DesignationInterface> titleWiseDesignationsMap;
 private Set<DesignationInterface> designationSet;
 private static DesignationManager designationManager=null;

 private DesignationManager() throws BLException
 {
  populateDataStructures();
 }


 private void populateDataStructures() throws BLException
 {
  this.codeWiseDesignationsMap=new HashMap<>();
  this.titleWiseDesignationsMap=new HashMap<>();
  this.designationSet=new TreeSet<>();
  
  try
  {
   Set<DesignationDTOInterface> dlDesignations= new DesignationDAO().getAll();
   

   for(DesignationDTOInterface ddtoi: dlDesignations)
   {
    DesignationInterface designation= new Designation();
    designation.setCode(ddtoi.getCode());
    designation.setTitle(ddtoi.getTitle());

    this.codeWiseDesignationsMap.put(designation.getCode(),designation);
    this.titleWiseDesignationsMap.put(designation.getTitle(),designation);
    this.designationSet.add(designation);
   } 
  }
  catch(DAOException daoe)
  {
   BLException ble=new BLException();
   ble.setGenericException(daoe.getMessage());
   throw ble;
  } 
 }

 public static DesignationManagerInterface getInstance() throws BLException
 {
  if(designationManager==null)
  designationManager = new DesignationManager();

  return designationManager;
 }


 

 public void addDesignation(DesignationInterface designation) throws BLException
 {
  BLException ble= new BLException();
 
  if(designation==null)
  {
   ble.setGenericException("Designation required");
   throw ble;
  }
 
  int code=designation.getCode();
  String title=designation.getTitle();
  
  if(code!=0)
  {
   ble.addException("code","Code should be zero");
  }
 
  if(title==null)
  {
   ble.addException("title","Title is required");
   title="";
  }
  else
  {
   title=title.trim();
   if(title.length()==0)
   {
    ble.addException("title","Title is required");
   }
  } 
 
  if(title.length()>0)
  {
   if(this.titleWiseDesignationsMap.containsKey(title.toUpperCase()))
   {
    ble.addException("title","Designation "+title+" already exists");
   }
  }
  
  if(ble.hasExceptions())
  throw ble;

  try
  {
   DesignationDTOInterface designationDTO= new DesignationDTO();
   designationDTO.setTitle(title);
   DesignationDAOInterface designationDAO= new DesignationDAO();
   designationDAO.add(designationDTO);
   code=designationDTO.getCode();
  }
  catch(DAOException daoe)
  {
   ble.setGenericException(daoe.getMessage());
   throw ble;
  } 

  DesignationInterface dsDesignation= new Designation();
  dsDesignation.setCode(code);
  dsDesignation.setTitle(title);

  codeWiseDesignationsMap.put(code,dsDesignation);
  titleWiseDesignationsMap.put(title.toUpperCase(),dsDesignation);
  designationSet.add(dsDesignation); 

  designation.setCode(code);
 }


 public void updateDesignation(DesignationInterface designation) throws BLException
 {
  BLException ble= new BLException();
 
  if(designation==null)
  {
   ble.setGenericException("Designation required");
   throw ble;
  }
 
  int code=designation.getCode();
  String title=designation.getTitle();
  
  if(code<=0)
  {
   ble.addException("code","Invalid code:"+code);
   throw ble;
  }
  else
  {
   if(!this.codeWiseDesignationsMap.containsKey(code))
   {
    ble.addException("code","Invalid code:"+code);
    throw ble;
   }
  } 

  if(title==null)
  {
   ble.addException("title","Title is required");
   title="";
  }
  else
  {
   title=title.trim();
   if(title.length()==0)
   {
    ble.addException("title","Title is required");
   }
   else
   {
    if(this.titleWiseDesignationsMap.containsKey(title.toUpperCase()))
    {
     if(this.titleWiseDesignationsMap.get(title.toUpperCase()).getCode()!=code)
     ble.addException("title","Designation "+title+" already exists");
    }
   }
  }


  if(ble.hasExceptions())
  throw ble;
 
  try
  {
   DesignationDTOInterface designationDTO= new DesignationDTO();
   designationDTO.setTitle(title);
   designationDTO.setCode(code);
   DesignationDAOInterface designationDAO= new DesignationDAO();
   designationDAO.update(designationDTO);
  }
  catch(DAOException daoe)
  {
   ble.setGenericException(daoe.getMessage());
   throw ble;
  } 

  DesignationInterface oldDesignation= this.codeWiseDesignationsMap.get(code);

  codeWiseDesignationsMap.remove(oldDesignation.getCode());
  titleWiseDesignationsMap.remove(oldDesignation.getTitle().toUpperCase());
  designationSet.remove(oldDesignation); 

  DesignationInterface newDesignation= new Designation();

  newDesignation.setCode(code);
  newDesignation.setTitle(title);

  codeWiseDesignationsMap.put(code,newDesignation);
  titleWiseDesignationsMap.put(title.toUpperCase(),newDesignation);
  designationSet.add(newDesignation); 

 }

 public void removeDesignation(int code) throws BLException
 {
  BLException ble= new BLException();
  
  if(code<=0)
  {
   ble.addException("code","Invalid code:"+code);
   throw ble;
  }
  else
  {
   if(!this.codeWiseDesignationsMap.containsKey(code))
   {
    ble.addException("code","Invalid code:"+code);
    throw ble;
   }
  } 

  try
  {
   DesignationDAOInterface designationDAO= new DesignationDAO();
   designationDAO.delete(code);
  }
  catch(DAOException daoe)
  {
   ble.setGenericException(daoe.getMessage());
   throw ble;
  } 

  DesignationInterface oldDesignation= this.codeWiseDesignationsMap.get(code);

  codeWiseDesignationsMap.remove(oldDesignation.getCode());
  titleWiseDesignationsMap.remove(oldDesignation.getTitle().toUpperCase());
  designationSet.remove(oldDesignation); 

 }

 DesignationInterface getDSDesignationByCode(int code)
 {
  DesignationInterface designation;
  designation=codeWiseDesignationsMap.get(code);
  return designation;
 }


 public DesignationInterface getDesignationByCode(int code) throws BLException
 {
  BLException ble=new BLException();

  if(code<=0||!this.codeWiseDesignationsMap.containsKey(code))
  { 
   ble.addException("code","Invalid code:"+code);
   throw ble;
  }

  DesignationInterface designation;
  designation=this.codeWiseDesignationsMap.get(code);

  DesignationInterface designationClone= new Designation();

  designationClone.setCode(designation.getCode());
  designationClone.setTitle(designation.getTitle());

  return designationClone;
 } 
 public DesignationInterface getDesignationByTitle(String title) throws BLException
 {
  BLException ble=new BLException();

  if(title==null)
  {
   ble.addException("title","Title is required");
   throw ble;
  }
  else
  {
   title=title.trim();

   if(title.length()==0||!(this.titleWiseDesignationsMap.containsKey(title.toUpperCase())))
   {
    ble.addException("title","Title is invalid");
    throw ble;
   }
  }
    
  DesignationInterface designation;
  designation=this.titleWiseDesignationsMap.get(title.toUpperCase());

  DesignationInterface designationClone= new Designation();

  designationClone.setCode(designation.getCode());
  designationClone.setTitle(designation.getTitle());

  return designationClone;
 }
 public int getDesignationCount()
 {
  return this.designationSet.size();
 } 
 public boolean designationCodeExists(int code)
 {
  return this.codeWiseDesignationsMap.containsKey(code);
 }
 public boolean designationTitleExists(String title)
 {
  return this.titleWiseDesignationsMap.containsKey(title);
 }
 public Set<DesignationInterface> getDesignations() 
 {
  Set<DesignationInterface> cloneDesignationset= new TreeSet<>();

  for(DesignationInterface designation: this.designationSet)
  {
   DesignationInterface designationClone= new Designation();

   designationClone.setCode(designation.getCode());
   designationClone.setTitle(designation.getTitle());

   cloneDesignationset.add(designationClone);
  }
  return cloneDesignationset;
 }
} 
