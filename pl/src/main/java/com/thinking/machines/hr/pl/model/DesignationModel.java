package com.thinking.machines.hr.pl.model;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class DesignationModel extends AbstractTableModel
{
 private java.util.List<DesignationInterface> designations;
 private String[] columnTitle;
 private DesignationManagerInterface designationManager;

 private void populateDataStructures()
 {
  this.columnTitle= new String[2];
  this.columnTitle[0]="S.no";
  this.columnTitle[1]="Designations";

  try
  {
   designationManager= DesignationManager.getInstance();
  }
  catch(BLException ble)
  {
   //do something
  } 
  Set<DesignationInterface> blDesignations= designationManager.getDesignations();

  this.designations= new LinkedList<>();
  for(DesignationInterface designation: blDesignations)
  {
   this.designations.add(designation);
  }

  Collections.sort(this.designations,new Comparator<DesignationInterface>(){
   public int compare(DesignationInterface left,DesignationInterface right)
   {
    return left.getTitle().compareTo(right.getTitle());
   }
  });
 }
 
 public int getRowCount()
 { 
  return this.designations.size();
 }
 public int getColumnCount()
 {
  return columnTitle.length;
 }
 public Object getValueAt(int rowIndex,int columnIndex)
 {
  if(columnIndex==0)
  return rowIndex+1;

  return this.designations.get(rowIndex).getTitle();
 }
 public boolean isCellEditable(int rowIndex,int columnIndex)
 {
  return false;
 }
 public Class getColumnClass(int columnIndex)
 {
  if(columnIndex==0)
  return Integer.class;
  
  return String.class;
 }
 public String getColumnName(int columnIndex)
 {
  return columnTitle[columnIndex];
 }

 //Application specific functions//
 public void add(DesignationInterface designation) throws BLException
 {
  designationManager.addDesignation(designation);
  this.designations.add(designation);

  Collections.sort(this.designations,new Comparator<DesignationInterface>(){
   public int compare(DesignationInterface left,DesignationInterface right)
   {
    return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
   }
  });

  fireTableDataChanged();
 }

 public int indexOfDesignation(DesignationInterface designation) throws BLException
 {
  Iterator<DesignationInterface> iterator=this.designations.iterator();
  DesignationInterface d;

  int index=0;
  while(iterator.hasNext())
  {
   d=iterator.next();
   if(d.equals(designation))
   return index;

   index++;
  }

  BLException ble= new BLException();
  ble.setGenericException("Invalid Designation"+designation.getTitle());
  throw ble;
 }

 public int indexOfTitle(String title,boolean partialLeftSearch) throws BLException
 {
  Iterator<DesignationInterface> iterator=this.designations.iterator();
  DesignationInterface d;
  int index=0;
  while(iterator.hasNext())
  {
   d=iterator.next();
   if(partialLeftSearch)
   {
    if(d.getTitle().toUpperCase().startsWith(title.toUpperCase()))
    return index;
   }
   else
   {
    if(d.getTitle().toUpperCase().equals(title))
    return index;   
   }
   index++;
  }
  BLException ble= new BLException();
  ble.setGenericException("Invalid Title"+title);
  throw ble; 
 }
 public void update(DesignationInterface designation) throws BLException
 {
  designationManager.updateDesignation(designation);
  this.designations.remove(indexOfDesignation(designation));
  this.designations.add(designation);

  Collections.sort(this.designations,new Comparator<DesignationInterface>(){
   public int compare(DesignationInterface left,DesignationInterface right)
   {
    return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
   }
  });

  fireTableDataChanged();
 }
 public void remove(int code) throws BLException
 {
  designationManager.removeDesignation(code);
  
  Iterator<DesignationInterface> iterator=this.designations.iterator();
  DesignationInterface d;

  int index=0;
  while(iterator.hasNext())
  {
   d=iterator.next();
   if(d.getCode()==code)
   break;

   index++;
  }

  if(index==this.designations.size())
  {
   BLException ble= new BLException();
   ble.setGenericException("Invalid Designation code"+code);
   throw ble;
  }

  this.designations.remove(index);
  fireTableDataChanged();
 }

 public DesignationInterface getDesignationAt(int index) throws BLException
 {
  if(index<0||index>=this.designations.size())
  {
   BLException ble= new BLException();
   ble.setGenericException("Invalid index"+index);
   throw ble;
  }
  return this.designations.get(index);
 }

 public DesignationModel()
 {
  this.populateDataStructures();
 }
}

