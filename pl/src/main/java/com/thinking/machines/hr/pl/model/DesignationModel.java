package com.thinking.machines.hr.pl.model;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.enums.*;
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
   System.out.println(ble.getGenericException());
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
 public void exportAsPdf(File file) throws BLException
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
   DesignationInterface designation;
  
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

 public DesignationModel()
 {
  this.populateDataStructures();
 }
}

