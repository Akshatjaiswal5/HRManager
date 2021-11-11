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

 

 }


 public void updateDesignation(DesignationInterface designation) throws BLException
 {

 }

 public void removeDesignation(int code) throws BLException
 {
 
 }

 DesignationInterface getDSDesignationByCode(int code)
 {
  return null;
 }

 public DesignationInterface getDesignationByCode(int code) throws BLException
 {
  return null;
 }
 public DesignationInterface  getDesignationByTitle(String s) throws BLException
 {
  return null;
 }
 
 public int getDesignationCount()
 {
  return 0;
 } 
 public boolean designationCodeExists(int code)
 {
  return false;
 }
 public boolean designationTitleExists(String title)
 {
  return false;
 }
 public Set<DesignationInterface> getDesignations() 
 {
  return null;
 }

} 
