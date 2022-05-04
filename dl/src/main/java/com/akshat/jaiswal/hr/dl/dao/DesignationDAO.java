package com.akshat.jaiswal.hr.dl.dao;
import com.akshat.jaiswal.hr.dl.dto.*;
import com.akshat.jaiswal.hr.dl.interfaces.dto.*;
import com.akshat.jaiswal.hr.dl.interfaces.dao.*;
import com.akshat.jaiswal.hr.dl.exceptions.*;
import java.sql.*;
import java.util.*;

public class DesignationDAO implements DesignationDAOInterface 
{

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


  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT code FROM designation WHERE title=?");
   s.setString(1,title);

   ResultSet r= s.executeQuery();
   if(r.next())
   {
    r.close();s.close();
    throw new DAOException("Designation: "+title+" exists.");
   }

   r.close();s.close();

   s=connection.prepareStatement("INSERT INTO designation (title) VALUES (?)",Statement.RETURN_GENERATED_KEYS);
   s.setString(1,title);
   s.executeUpdate();
   r=s.getGeneratedKeys();
   r.next();

   int code=r.getInt(1);
   r.close();s.close();
   designationDTO.setCode(code);
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
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

  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT code FROM designation WHERE code=?");
   s.setInt(1,code);

   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Code: "+code+" does not exist.");
   }

   s=connection.prepareStatement("SELECT code FROM designation WHERE title=? AND code<>?");
   s.setString(1,title);
   s.setInt(2,code);
 
   r= s.executeQuery();
   if(r.next())
   {
    r.close();s.close();
    throw new DAOException("Designation: "+title+" exists.");
   }

   s=connection.prepareStatement("UPDATE designation SET title=? WHERE code=?");
   s.setString(1,title);
   s.setInt(2,code);

   s.executeUpdate();
   r.close();s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }

 }
 public void delete(int code) throws DAOException
 {
  if(code<0)
  throw new DAOException("Invalid code");
 
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT title FROM designation WHERE code=?");
   s.setInt(1,code);
   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Code: "+code+" does not exist.");
   }
   String title=r.getString("title");
   r.close();s.close();

   s=connection.prepareStatement("SELECT gender FROM employee WHERE designation_code=?");
   s.setInt(1,code);
   r=s.executeQuery();
   if(r.next())
   {
    r.close();s.close();
    throw new DAOException("Could not delete designation:"+title+" because it has been alloted to employee(s)");
   }
   r.close();s.close();

   s=connection.prepareStatement("DELETE FROM designation WHERE code=?");
   s.setInt(1,code);

   s.executeUpdate();
   r.close();s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
 }
 public Set<DesignationDTOInterface> getAll() throws DAOException
 {
  Set<DesignationDTOInterface> designations= new TreeSet<>();
  DesignationDTOInterface designationDTO;
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s=connection.prepareStatement("SELECT * FROM designation");
   ResultSet r=s.executeQuery();

   while(r.next())
   {
    designationDTO= new DesignationDTO();
    designationDTO.setCode(r.getInt("code"));
    designationDTO.setTitle(r.getString("title").trim());

    designations.add(designationDTO);
   }
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }
  return designations;
 }
 public DesignationDTOInterface getByCode(int code) throws DAOException
 {
  if(code<0)
  throw new DAOException("Invalid code");

  DesignationDTOInterface designationDTO;
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT * FROM designation WHERE code=?");
   s.setInt(1,code);
 
   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Code: "+code+" does not exist.");
   }

   designationDTO= new DesignationDTO();
   designationDTO.setCode(r.getInt("code"));
   designationDTO.setTitle(r.getString("title").trim());
   
   r.close();s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }

  return designationDTO;
 }
 public DesignationDTOInterface getByTitle(String title) throws DAOException
 {
  if(title==null||title.trim().length()==0)
  throw new DAOException("Invalid title");

  title=title.trim();
  DesignationDTOInterface designationDTO;
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT * FROM designation WHERE title=?");
   s.setString(1,title);
 
   ResultSet r= s.executeQuery();
   if(!r.next())
   {
    r.close();s.close();
    throw new DAOException("Title: "+title+" does not exist.");
   }

   designationDTO= new DesignationDTO();
   designationDTO.setCode(r.getInt("code"));
   designationDTO.setTitle(r.getString("title").trim());
   
   r.close();s.close();
  }
  catch(SQLException se)
  {
   throw new DAOException(se.getMessage());
  }

  return designationDTO;
 }
 public boolean codeExists(int code) throws DAOException
 {
  if(code<0)
  return false;

  boolean exists;
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT code FROM designation WHERE code=?");
   s.setInt(1,code);
 
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
 public boolean titleExists(String title) throws DAOException
 {
  if(title==null||title.trim().length()==0)
  return false;

  title=title.trim();

  boolean exists;
  try(Connection connection=DAOConnection.getConnection())
  {
   PreparedStatement s;
   s=connection.prepareStatement("SELECT code FROM designation WHERE title=?");
   s.setString(1,title);
 
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
   ResultSet r=s.executeQuery("SELECT count(*) FROM designation");

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
}