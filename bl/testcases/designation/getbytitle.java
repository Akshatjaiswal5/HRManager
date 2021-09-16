import java.util.*;
import com.thinking.machines.util.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;

public class getbytitle
{
 public static void main(String args[])
 {
  String title=Keyboard.in.getString("Title:");
  try
  {
   DesignationInterface d= new Designation();
   DesignationManagerInterface da= DesignationManager.getInstance();
   
   if(da.designationTitleExists(title))
   System.out.println("Title exists");

   d=da.getDesignationByTitle(title);
   System.out.println(title+" found with code: "+d.getCode());
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