package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;

import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import com.thinking.machines.network.client.*;

import java.util.*;
public class DesignationManager implements DesignationManagerInterface
{
 static DesignationManagerInterface designationManager;

 private DesignationManager() throws BLException
 {

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
  
  if(ble.hasExceptions())
  throw ble;

  Request request= new Request();
  request.setManager("DesignationManager");
  request.setAction("add");
  request.setArguments(designation);

  NetworkClient client= new NetworkClient();
  
  try {
   Response response= client.send(request);
   
   if(response.hasException())
   {
    ble=(BLException)response.getException();
    throw ble;
   }

   DesignationInterface d= (DesignationInterface)response.getResult();
   designation.setCode(d.getCode());
   
  }catch(NetworkException ne) {
   
   ble.setGenericException(ne.getMessage());
   throw ble;
  }

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

  if(ble.hasExceptions())
  throw ble;
 
  Request request= new Request();
  request.setManager("DesignationManager");
  request.setAction("update");
  request.setArguments(designation);

  NetworkClient client= new NetworkClient();
  
  try {
   Response response= client.send(request);
   
   if(response.hasException())
   {
    ble=(BLException)response.getException();
    throw ble;
   }
   
  }catch(NetworkException ne) {
   
   ble.setGenericException(ne.getMessage());
   throw ble;
  }

 }
 public void removeDesignation(int code) throws BLException
 {
  BLException ble= new BLException();
  
  if(code<=0)
  {
   ble.addException("code","Invalid code:"+code);
   throw ble;
  }
  
  Request request= new Request();
  request.setManager("DesignationManager");
  request.setAction("remove");
  request.setArguments(code);

  NetworkClient client= new NetworkClient();
  
  try {
   Response response= client.send(request);
   
   if(response.hasException())
   {
    ble=(BLException)response.getException();
    throw ble;
   }
   
  }catch(NetworkException ne) {
   
   ble.setGenericException(ne.getMessage());
   throw ble;
  }

 }
 public DesignationInterface getDesignationByCode(int code) throws BLException
 {
  BLException ble=new BLException();

  if(code<=0)
  { 
   ble.addException("code","Invalid code:"+code);
   throw ble;
  }

  DesignationInterface designation;

  Request request= new Request();
  request.setManager("DesignationManager");
  request.setAction("getbycode");
  request.setArguments(code);

  NetworkClient client= new NetworkClient();
  
  try {
   Response response= client.send(request);
   
   if(response.hasException())
   {
    ble=(BLException)response.getException();
    throw ble;
   }

   designation=(DesignationInterface)response.getResult();

  }catch(NetworkException ne) {
   
   ble.setGenericException(ne.getMessage());
   throw ble;
  }

  return designation;
 }
 public DesignationInterface  getDesignationByTitle(String title) throws BLException
 {
  BLException ble= new BLException();

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

  if(ble.hasExceptions())
  throw ble;

  DesignationInterface designation;

  Request request= new Request();
  request.setManager("DesignationManager");
  request.setAction("getbytitle");
  request.setArguments(title);

  NetworkClient client= new NetworkClient();
  
  try {
   Response response= client.send(request);
   
   if(response.hasException())
   {
    ble=(BLException)response.getException();
    throw ble;
   }

   designation=(DesignationInterface)response.getResult();

  }catch(NetworkException ne) {
   
   ble.setGenericException(ne.getMessage());
   throw ble;
  }

  return designation;
 }
 public int getDesignationCount()
 {
  BLException ble= new BLException();

  Request request= new Request();
  request.setManager("DesignationManager");
  request.setAction("getcount");

  NetworkClient client= new NetworkClient();
  Integer count;

  try {
   Response response= client.send(request);

   count=(Integer)response.getResult();

  }catch(NetworkException ne) {
   
   ble.setGenericException(ne.getMessage());
   //throw ble;
   return 0;
  }
  return count;
 } 
 public boolean designationCodeExists(int code)
 {
  BLException ble=new BLException();

  if(code<=0)
  { 
   return false;
  }

  Boolean exists;

  Request request= new Request();
  request.setManager("DesignationManager");
  request.setAction("codeExists");
  request.setArguments(code);

  NetworkClient client= new NetworkClient();
  
  try {
   Response response= client.send(request);
   exists=(Boolean)response.getResult();

  }catch(NetworkException ne) {
   
   ble.setGenericException(ne.getMessage());
   //throw ble;
   return false;
  }

  return exists;
 }
 public boolean designationTitleExists(String title)
 {
  BLException ble=new BLException();

  if(title==null)
  {
   return false;
  }
  else
  {
   title=title.trim();

   if(title.length()==0)
   {
    return false;
   }
  }

  Boolean exists;

  Request request= new Request();
  request.setManager("DesignationManager");
  request.setAction("titleExists");
  request.setArguments(title);

  NetworkClient client= new NetworkClient();
  
  try {
   Response response= client.send(request);
   exists=(Boolean)response.getResult();

  }catch(NetworkException ne) {
   
   ble.setGenericException(ne.getMessage());
   //throw ble;
   return false;
  }
  
  return exists;
 }
 public Set<DesignationInterface> getDesignations() 
 {
  BLException ble=new BLException();

  Request request= new Request();
  request.setManager("DesignationManager");
  request.setAction("getdesignations");

  NetworkClient client= new NetworkClient();
  Set<DesignationInterface> designations;

  try {
   Response response= client.send(request);

   designations=(Set<DesignationInterface>)response.getResult();

  }catch(NetworkException ne) {
   
   ble.setGenericException(ne.getMessage());
   //throw ble;
   return new TreeSet<DesignationInterface>();
  }
  return designations;
 }

} 
