package com.akshat.jaiswal.hr.pl.model;
import com.akshat.jaiswal.hr.bl.exceptions.*;
import com.akshat.jaiswal.hr.bl.interfaces.pojo.*;
import com.akshat.jaiswal.hr.bl.interfaces.managers.*;
import com.akshat.jaiswal.hr.bl.managers.*;
import com.akshat.jaiswal.hr.bl.pojo.*;
import com.akshat.jaiswal.enums.*;
import java.util.*;
import java.awt.event.*;
import com.itextpdf.layout.borders.*;

import javax.lang.model.element.Element;
import javax.print.attribute.standard.PageRanges;
import javax.swing.*;
import javax.swing.table.*;
import java.io.File;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.image.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.property.*;

public class EmployeeModel extends AbstractTableModel
{
 private java.util.List<EmployeeInterface> employees;
 private String[] columnTitle;
 private EmployeeManagerInterface employeeManager;

 private void populateDataStructures()
 {
  this.columnTitle= new String[2];
  this.columnTitle[0]="S.no";
  this.columnTitle[1]="Employees";


  try
  {
   employeeManager= EmployeeManager.getInstance();
  }
  catch(BLException ble)
  {
   System.out.println(ble.getGenericException());
  } 
  Set<EmployeeInterface> blEmployees= employeeManager.getEmployees();
  this.employees= new ArrayList<>();
  for(EmployeeInterface employee: blEmployees)
  {
   this.employees.add(employee);
  }
  Collections.sort(this.employees,new Comparator<EmployeeInterface>(){
   public int compare(EmployeeInterface left,EmployeeInterface right)
   {
    return left.getEmployeeId().compareToIgnoreCase(right.getEmployeeId());
   }
  });
 }
 
 public int getRowCount()
 { 
  return this.employees.size();
 }
 public int getColumnCount()
 {
  return columnTitle.length;
 }
 public Object getValueAt(int rowIndex,int columnIndex)
 {
  if(columnIndex==0)
  return rowIndex+1;

  return this.employees.get(rowIndex).getName();
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

 public EmployeeInterface getEmployeeAt(int index) throws BLException
 {
  if(index<0||index>=this.employees.size())
  {
   BLException ble= new BLException();
   ble.setGenericException("Invalid index"+index);
   throw ble;
  }
  return this.employees.get(index);
 }
 public int indexOfName(String name,boolean partialLeftSearch) throws BLException
 {
  Iterator<EmployeeInterface> iterator=this.employees.iterator();
  EmployeeInterface d;
  int index=0;
  while(iterator.hasNext())
  {
   d=iterator.next();
   if(partialLeftSearch)
   {
    if(d.getName().toUpperCase().startsWith(name.toUpperCase()))
    return index;
   }
   else
   {
    if(d.getName().toUpperCase().equals(name))
    return index;   
   }
   index++;
  }
  BLException ble= new BLException();
  ble.setGenericException("Invalid Name "+name);
  throw ble; 
 }


 public void add(EmployeeInterface employee) throws BLException
 {
  employeeManager.addEmployee(employee);
  this.employees.add(employee);

  Collections.sort(this.employees,new Comparator<EmployeeInterface>(){
   public int compare(EmployeeInterface left,EmployeeInterface right)
   {
    return left.getEmployeeId().compareToIgnoreCase(right.getEmployeeId());
   }
  });

  fireTableDataChanged();
 }
 public int indexOfEmployee(EmployeeInterface employee) throws BLException
 {
  Iterator<EmployeeInterface> iterator=this.employees.iterator();
  EmployeeInterface d;

  int index=0;
  while(iterator.hasNext())
  {
   d=iterator.next();
   if(d.equals(employee))
   return index;

   index++;
  }

  BLException ble= new BLException();
  ble.setGenericException("Invalid Employee Name"+employee.getName());
  throw ble;
 }
 public void update(EmployeeInterface employee) throws BLException
 {
  employeeManager.updateEmployee(employee);
  this.employees.remove(indexOfEmployee(employee));
  this.employees.add(employee);

  Collections.sort(this.employees,new Comparator<EmployeeInterface>(){
   public int compare(EmployeeInterface left,EmployeeInterface right)
   {
    return left.getEmployeeId().compareToIgnoreCase(right.getEmployeeId());
   }
  });

  fireTableDataChanged();
 }
 public void remove(String employeeId) throws BLException
 {
  employeeManager.deleteEmployee(employeeId);
  
  Iterator<EmployeeInterface> iterator=this.employees.iterator();
  EmployeeInterface d;

  int index=0;
  while(iterator.hasNext())
  {
   d=iterator.next();
   if(d.getEmployeeId().equalsIgnoreCase(employeeId))
   break;

   index++;
  }

  if(index==this.employees.size())
  {
   BLException ble= new BLException();
   ble.setGenericException("Invalid Employee Id"+employeeId);
   throw ble;
  }

  this.employees.remove(index);
  fireTableDataChanged();
 }

/* public void exportAsPdf(File file) throws BLException
 {

  try
  {
   PdfWriter pdfwriter= new PdfWriter(file);
   PdfDocument pdfdocument= new PdfDocument(pdfwriter);
   Document document= new Document(pdfdocument);
   

   Image logo=new Image(ImageDataFactory.create(getClass().getResource("/images/companyLogo.png")));
   Paragraph logoPara= new Paragraph();
   logoPara.add(logo);

   Paragraph companyNamePara= new Paragraph("Thinking machines.in");
   companyNamePara.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD));
   companyNamePara.setFontSize(30);

   float a[]={600f,1000f};
   Table topTable= new Table(a);
   Cell c= new Cell();
   c.add(logoPara);
   c.setBorder(Border.NO_BORDER);
   topTable.addCell(c);
   c= new Cell();
   c.add(companyNamePara).setHeight(70).setVerticalAlignment(VerticalAlignment.MIDDLE);
   c.setBorder(Border.NO_BORDER);
   topTable.addCell(c);

   Paragraph reportTitlePara= new Paragraph("List of Designations");
   reportTitlePara.setFontSize(15);
   
   float ar[]={1000f,100f};
   Table headingTable;
   Paragraph pageNoPara;

   Paragraph footnote= new Paragraph("Created by Akshat Jaiswal.");
   footnote.setTextAlignment(TextAlignment.RIGHT);

   Cell column1= new Cell();
   column1.add(new Paragraph("S.no").setFontSize(16)).setHeight(30);
   Cell column2= new Cell();
   column2.add(new Paragraph("Designations").setFontSize(16));

   int Sno=0,maxPage=26,pageNo=0;
   float arr[]={100f,1000f};
   boolean newPage=true;
   Table table=null;
   EmployeeInterface designation;
  
   while(true)
   {
    if(newPage)
    {
     //createHeader
     pageNo++;
     document.add(topTable);

     headingTable= new Table(ar);
     headingTable.addCell(new Cell().add(reportTitlePara).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));
     pageNoPara=new Paragraph("Page no: "+pageNo);
     pageNoPara.setTextAlignment(TextAlignment.RIGHT);
     headingTable.addCell(new Cell().add(pageNoPara).setBorder(Border.NO_BORDER));
     document.add(headingTable);

     table=new Table(arr);
     table.addHeaderCell(column1);
     table.addHeaderCell(column2);
     newPage=false;
    }


    designation= designations.get(Sno);

    table.addCell(new Cell().add(new Paragraph(Sno+1+"")));
    table.addCell(new Cell().add(new Paragraph(designation.getTitle())));
    Sno++;

    if(Sno%maxPage==0||designations.size()==Sno)
    {
     document.add(table);

     //createFooter
     newPage=true;
     document.add(footnote);
     
     if(designations.size()==Sno)
     break;

     document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
    }

   }

   document.close();
  }
  catch(Exception ioe)
  {
   throw new BLException();
  }
 }
*/
 public EmployeeModel()
 {
  this.populateDataStructures();
 }
}

