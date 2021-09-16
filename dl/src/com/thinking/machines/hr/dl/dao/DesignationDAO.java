package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
import java.io.*;


public class DesignationDAO implements DesignationDAOInterface 
{
 private final static String FILE_NAME ="designation.data";
 
 public void add(DesignationDTOInterface designationDTO) throws DAOException
 {
  if(designationDTO==null)
  throw new DAOException("designation is null");
 
  String title=designationDTO.getTitle();

  if(title==null)
  throw new DAOException("designation is null");
  title=title.trim();
  if(title.length()==0)
  throw new DAOException("length of designation is 0");

  File file= new File(FILE_NAME);
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   int lastCode=0,recordCount=0;
   String lastCodeStr="",recordCountStr="";


   if(raf.length()==0)
   {
    lastCodeStr=String.valueOf(lastCode);
    while(lastCodeStr.length()<10) lastCodeStr+=" "; 

    recordCountStr=String.valueOf(recordCount);
    while(recordCountStr.length()<10) recordCountStr+=" "; 

    raf.writeBytes(lastCodeStr+"\n");
    raf.writeBytes(recordCountStr+"\n");
   }
   else
   {
    lastCodeStr= raf.readLine().trim();
    recordCountStr= raf.readLine().trim();

    lastCode=Integer.parseInt(lastCodeStr);
    recordCount=Integer.parseInt(recordCountStr);
   }

  int fCode;
  String fTitle;

  while(raf.getFilePointer()<raf.length())
  {
   fCode=Integer.parseInt(raf.readLine());
   fTitle=raf.readLine();

   if(fTitle.equalsIgnoreCase(title))
   {
    throw new DAOException("Designation: "+title+" exists.");
   }
  }

  raf.writeBytes(String.valueOf(lastCode+1)+"\n");
  raf.writeBytes(title+"\n");

  designationDTO.setCode(lastCode+1);

  lastCode++;
  recordCount++;
  raf.seek(0);

  lastCodeStr=String.valueOf(lastCode);
  while(lastCodeStr.length()<10) lastCodeStr+=" "; 

  recordCountStr=String.valueOf(recordCount);
  while(recordCountStr.length()<10) recordCountStr+=" "; 

  raf.writeBytes(lastCodeStr+"\n");
  raf.writeBytes(recordCountStr+"\n");
  
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public void update(DesignationDTOInterface d) throws DAOException
 {
  if(d==null)
  throw new DAOException("Invalid object");

  String title=d.getTitle();
  int code=d.getCode();
  title=title.trim();
  
  if(code<0)
  throw new DAOException("Invalid object");

  if(title==null||title.trim().length()==0)
  throw new DAOException("Invalid object");

  File file= new File(FILE_NAME);

  if(!(file.exists()))
  throw new DAOException("Designation with same code not found");
  
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   throw new DAOException("Designation with same code not found");

   raf.readLine();
   raf.readLine();

   boolean found=false;
   while(raf.getFilePointer()<raf.length())
   {
    int fCode=Integer.parseInt(raf.readLine());
    String fTitle=raf.readLine();
   

    if(fCode==code)
    {
     found=true;
     continue;
    }
 
    if(fTitle.equalsIgnoreCase(title))
    throw new DAOException("Designation with same title exists");

   }
   
   if(!found)
   throw new DAOException("Designation with same code not found");

   RandomAccessFile tmpRaf = new RandomAccessFile("TMP.dat","rw");

    tmpRaf.setLength(0);
    raf.seek(0);
    tmpRaf.writeBytes(raf.readLine()+"\n");
    tmpRaf.writeBytes(raf.readLine()+"\n");
     
    while(raf.getFilePointer()<raf.length())
    {
     int fCode=Integer.parseInt(raf.readLine());
     String fTitle=raf.readLine();

     if(fCode!=code)
     {
      tmpRaf.writeBytes(String.valueOf(fCode)+"\n");
      tmpRaf.writeBytes(fTitle+"\n");
     }
     else
     {
      tmpRaf.writeBytes(String.valueOf(code)+"\n");
      tmpRaf.writeBytes(title+"\n");
     }
    }

    raf.setLength(0);
    tmpRaf.seek(0);

    while(tmpRaf.getFilePointer()<tmpRaf.length())
    {
     raf.writeBytes(tmpRaf.readLine()+"\n");
     raf.writeBytes(tmpRaf.readLine()+"\n");
    }
    tmpRaf.setLength(0);
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }

 }
 public void delete(int code) throws DAOException
 {
  if(code<0)
  throw new DAOException("Invalid code");

  File file= new File(FILE_NAME);

  if(!(file.exists()))
  throw new DAOException("Designation with same code not found");
  
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   throw new DAOException("Designation with same code not found");

   raf.readLine();
   raf.readLine();

   boolean found=false;
   while(raf.getFilePointer()<raf.length())
   {
    int fCode=Integer.parseInt(raf.readLine());
    String fTitle=raf.readLine();

    if(fCode==code)
    {
     found=true;
     break;
    }
   }
   
   if(!found)
   throw new DAOException("Designation with same code not found");

   RandomAccessFile tmpRaf = new RandomAccessFile("TMP.dat","rw");

    tmpRaf.setLength(0);
    raf.seek(0);
    tmpRaf.writeBytes(raf.readLine()+"\n");

    int recordCount=Integer.parseInt(raf.readLine().trim());
    String recordCountStr=String.valueOf(recordCount-1);
    while(recordCountStr.length()<10) recordCountStr+=" ";
    tmpRaf.writeBytes(recordCountStr+"\n");
     
    while(raf.getFilePointer()<raf.length())
    {
     int fCode=Integer.parseInt(raf.readLine());
     String fTitle=raf.readLine();

     if(fCode!=code)
     {
      tmpRaf.writeBytes(String.valueOf(fCode)+"\n");
      tmpRaf.writeBytes(fTitle+"\n");
     }
    }

    raf.setLength(0);
    tmpRaf.seek(0);

    while(tmpRaf.getFilePointer()<tmpRaf.length())
    {
     raf.writeBytes(tmpRaf.readLine()+"\n");
     raf.writeBytes(tmpRaf.readLine()+"\n");
    }

  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public Set<DesignationDTOInterface> getAll() throws DAOException
 {
  Set<DesignationDTOInterface> designations= new TreeSet<>();

  File file= new File(FILE_NAME);

  if(!(file.exists()))
  return designations;

  try
  {
   RandomAccessFile raf= new RandomAccessFile(file,"rw");
   if(raf.length()==0)
   {
    raf.close(); return designations;
   }
   raf.readLine();
   raf.readLine();

   while(raf.getFilePointer()<raf.length())
   {
    int fCode=Integer.parseInt(raf.readLine());
    String fTitle=raf.readLine();

    DesignationDTOInterface d= new DesignationDTO();
    d.setTitle(fTitle);
    d.setCode(fCode);
  
    designations.add(d);
   }
   raf.close();
   return designations;
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public DesignationDTOInterface getByCode(int code) throws DAOException
 {
  if(code<0)
  throw new DAOException("Invalid code");

  File file= new File(FILE_NAME);

  if(!(file.exists()))
  throw new DAOException("Invalid code");

  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   throw new DAOException("Invalid code");

   raf.readLine();
   int recordCount=Integer.parseInt(raf.readLine().trim());
   
   if(recordCount==0)
   throw new DAOException("Invalid code");

   while(raf.getFilePointer()<raf.length())
   {
    int fCode=Integer.parseInt(raf.readLine());
    String fTitle=raf.readLine();

    if(fCode==code)
    {
     DesignationDTOInterface d= new DesignationDTO();
     d.setTitle(fTitle);
     d.setCode(fCode);
  
     raf.close();
     return d;
    }
   }
  throw new DAOException("Invalid code");
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public DesignationDTOInterface getByTitle(String title) throws DAOException
 {
  if(title==null||title.trim().length()==0)
  throw new DAOException("Invalid title");

  title=title.trim();

  File file= new File(FILE_NAME);

  if(!(file.exists()))
  throw new DAOException("Invalid title");

  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   throw new DAOException("Invalid title");

   raf.readLine();
   int recordCount=Integer.parseInt(raf.readLine().trim());
   
   if(recordCount==0)
   throw new DAOException("Invalid title");

   while(raf.getFilePointer()<raf.length())
   {
    int fCode=Integer.parseInt(raf.readLine());
    String fTitle=raf.readLine();

    if(fTitle.equalsIgnoreCase(title))
    {
     DesignationDTOInterface d= new DesignationDTO();
     d.setTitle(fTitle);
     d.setCode(fCode);
  
     raf.close();
     return d;
    }
   }
  throw new DAOException("Invalid title");
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public boolean codeExists(int code) throws DAOException
 {
  if(code<0)
  return false;

  File file= new File(FILE_NAME);

  if(!(file.exists()))
  return false;

  try
  {
   RandomAccessFile raf= new RandomAccessFile(file,"rw");
   if(raf.length()==0)
   {
    raf.close(); return false;
   }

   raf.readLine();
   int recordCount=Integer.parseInt(raf.readLine().trim());
   
   if(recordCount==0)
   {
    raf.close(); return false;
   }

   while(raf.getFilePointer()<raf.length())
   {
    int fCode=Integer.parseInt(raf.readLine());
    String fTitle=raf.readLine();

    if(fCode==code)
    {
     raf.close(); return true;
    }
   }
  raf.close();return false;
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
 }
 public boolean titleExists(String title) throws DAOException
 {
  if(title==null||title.trim().length()==0)
  return false;

  title=title.trim();

  File file= new File(FILE_NAME);

  if(!(file.exists()))
  return false;

  try
  {
   RandomAccessFile raf= new RandomAccessFile(file,"rw");

   if(raf.length()==0)
   {
    raf.close(); return false;
   }

   raf.readLine();
   int recordCount=Integer.parseInt(raf.readLine().trim());
   
   if(recordCount==0)
   {
    raf.close(); return false;
   }

   while(raf.getFilePointer()<raf.length())
   {
    int fCode=Integer.parseInt(raf.readLine());
    String fTitle=raf.readLine();

    if(fTitle.equalsIgnoreCase(title))
    {
     raf.close(); return true;
    }
   }
   raf.close(); return false;
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
  int recordCount;
  try(RandomAccessFile raf= new RandomAccessFile(file,"rw");)
  {
   if(raf.length()==0)
   {
    recordCount=0;
   }
   else
   {
    raf.readLine();
    recordCount=Integer.parseInt(raf.readLine().trim());
   }
  }
  catch(IOException ioe)
  {
   throw new DAOException(ioe.getMessage());
  }
  return recordCount;
 }
}