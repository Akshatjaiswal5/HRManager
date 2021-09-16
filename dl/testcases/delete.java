import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.util.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
public class delete
{
 public static void main(String args[])
 {
  String id=Keyboard.in.getString("Enter id:");

  try
  {
   EmployeeDAOInterface da= new EmployeeDAO();
   da.delete(id);

  }
  catch(DAOException daoe)
  {
   System.out.println(daoe.getMessage());
  }  
 }
}