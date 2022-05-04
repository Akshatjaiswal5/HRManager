package com.akshat.jaiswal.hr.dl.dao;
import com.akshat.jaiswal.hr.dl.exceptions.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class DAOConnection
{
 private DAOConnection()
 {}

 public static Connection getConnection() throws DAOException
 {
  Connection connection=null;
  try
  {
   Class.forName("com.mysql.cj.jdbc.Driver");
   connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/hrdb","hr","hr");
   return connection;
  }
  catch(Exception e)
  {
   throw new DAOException(e.getMessage());
  }
 }

}
