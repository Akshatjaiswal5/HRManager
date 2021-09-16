import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.util.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
public class get
{
 public static void main(String args[])
 {
  String id=Keyboard.in.getString("Enter id:");

  try
  {
   EmployeeDTOInterface d;
   EmployeeDAOInterface da= new EmployeeDAO();
   d=da.getByEmployeeId(id);

   System.out.println("found:");
       System.out.println(d.getEmployeeId());
       System.out.println(d.getName());
       System.out.println(d.getPANNumber());
       System.out.println(d.getAadharCardNumber());
       System.out.println(d.getDesignationCode());
       System.out.println(d.getGender());
       System.out.println(d.getIsIndian());
       System.out.println(d.getBasicSalary());
       System.out.println(d.getDateOfBirth());
  
  }
  catch(DAOException daoe)
  {
   System.out.println(daoe.getMessage());
  }  
 }
}