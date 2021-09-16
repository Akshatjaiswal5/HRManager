import java.util.*;
import com.thinking.machines.util.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;

public class add
{
 public static void main(String args[])
 {
  String title=Keyboard.in.getString("Title:");
  int code=Keyboard.in.getInt("Code:");
  try
  {
   DesignationInterface d= new Designation();
   d.setTitle(title);
   d.setCode(code);
   DesignationManagerInterface da= DesignationManager.getInstance();
   da.addDesignation(d);
   System.out.println(title+" added with code: "+d.getCode()); 
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