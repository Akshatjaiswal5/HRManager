import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;

class StudentTableModel extends AbstractTableModel
{
 Object data[][];
 String title[];
 
 private void populateDataStructures()
 {
  title= new String[9];
  title[0]="Employee Id";
  title[1]="Name";
  title[2]="PAN no";
  title[3]="Aadhar no";
  title[4]="Designation code";
  title[5]="Gender";
  title[6]="Is Indian";
  title[7]="Basic salary";
  title[8]="Date of birth";

  try
  {
   EmployeeDAOInterface da= new EmployeeDAO();
   Set<EmployeeDTOInterface> s=da.getAll();

   data= new Object[s.size()][9];

   int i=0;
   for(EmployeeDTOInterface d:s)
   {
    data[i][0]=d.getEmployeeId();
    data[i][1]=d.getName();
    data[i][2]=d.getPANNumber();
    data[i][3]=d.getAadharCardNumber();
    data[i][4]=d.getDesignationCode();
    data[i][5]=GenderChar.to(d.getGender());
    data[i][6]=d.getIsIndian();
    data[i][7]=d.getBasicSalary();
    data[i][8]=d.getDateOfBirth();

    i++;
   }

  }
  catch(DAOException daoe)
  {
   System.out.println(daoe.getMessage());
  } 

 }

 public Object getValueAt(int rowIndex,int columnIndex)
 {
  return data[rowIndex][columnIndex];
 }
 public int getColumnCount()
 {
  return title.length;
 }
 public int getRowCount()
 {
  return data.length;
 }
 public String getColumnName(int index)
 {
  return title[index];
 }

 StudentTableModel()
 {
  populateDataStructures();
 }
}



class getTable extends JFrame
{
 int height,width,x,y;
 Dimension dimension;
 JTable table;
 StudentTableModel studenttablemodel;
 JScrollPane jscrollpane;

 getTable()
 {
  height=500;
  width=500;

  dimension=Toolkit.getDefaultToolkit().getScreenSize();
  x=((dimension.width)/2) - (width/2);
  y=((dimension.height)/2) -(height/2);

  
  studenttablemodel= new StudentTableModel();
  table= new JTable(studenttablemodel);
  table.setRowHeight(30);
  jscrollpane= new JScrollPane(table);


  add(jscrollpane);
  setVisible(true);
  setSize(height,width);
  setLocation(x,y);
 }
}

class run
{
 public static void main(String[] args)
 {
  getTable a= new getTable();
 }
}