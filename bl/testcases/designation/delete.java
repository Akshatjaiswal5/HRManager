import java.util.*;
import com.thinking.machines.util.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;

public class delete
{
 public static void main(String args[])
 {
  int code=Keyboard.in.getInt("Code:");
  try
  {
   DesignationManagerInterface da= DesignationManager.getInstance();
   da.removeDesignation(code);
   System.out.println(code+" deleted."); 
  }
  catch(BLException ble)
  {
   if(ble.hasGenericException())
   System.out.println(ble.getMessage());

   List<String> properties=ble.getProperties();
   for(String property:properties)
   {
    System.out.println(ble.getPropertyException(property));
   }
  }  
 }
}