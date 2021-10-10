import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.util.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;


class delete
{
 public static void main(String args[])
 {
  String employeeId= Keyboard.in.getString("employeeId:");

  try{

  EmployeeManagerInterface eManager= EmployeeManager.getInstance();

  eManager.deleteEmployee(employeeId);
  System.out.println("removed");
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