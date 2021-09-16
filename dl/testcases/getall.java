import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;
public class getall
{
 public static void main(String args[])
 {
   try{

   EmployeeDAOInterface da= new EmployeeDAO();
  
   Set<EmployeeDTOInterface> s=da.getAll();

   for(EmployeeDTOInterface d:s)
   {
   System.out.println("===========================");
   System.out.println(d.getEmployeeId());
   System.out.println(d.getName());
   System.out.println(d.getPANNumber());
   System.out.println(d.getAadharCardNumber());
   System.out.println(d.getDesignationCode());
   System.out.println(GenderChar.to(d.getGender()));
   System.out.println(d.getIsIndian());
   System.out.println(d.getBasicSalary());
   System.out.println(d.getDateOfBirth());
   }

  }

  catch(DAOException daoe)
  {
   System.out.println(daoe.getMessage());
  }  
  
 }
}