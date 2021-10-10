import java.util.*;
import com.thinking.machines.util.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.enums.*;
import java.math.*;
import java.text.*;

public class getbypan
{
 public static void main(String args[])
 {
  try
  {
   String PANNumber= Keyboard.in.getString("Enter pan:");
   EmployeeManagerInterface da= EmployeeManager.getInstance();
  
   if(da.PANNumberExists(PANNumber))
   System.out.println("Id exists");

   EmployeeInterface d= da.getEmployeeByPANNumber(PANNumber);

   System.out.println("===========================");
   System.out.print(d.getEmployeeId()+"    ");
   System.out.println(d.getName());
   System.out.print(d.getPANNumber()+"     ");
   System.out.println(d.getAadharCardNumber());
   System.out.print(d.getDesignation().getTitle()+"    ");
   System.out.println(GenderChar.to(d.getGender()));
   System.out.print(d.getIsIndian()+"    ");
   System.out.print(d.getBasicSalary()+"   ");
   System.out.println(d.getDateOfBirth());
   System.out.println("===========================");
  }
  catch(BLException ble)
  {
   System.out.println("Exception occured:");
 
   if(ble.hasGenericException())
   System.out.println("Generic: "+ble.getMessage());

   List<String> properties=ble.getProperties();
   for(String property:properties)
   {
    System.out.println(property+": "+ble.getPropertyException(property));
   }
  } 
    
 }
}