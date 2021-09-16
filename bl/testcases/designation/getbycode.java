import java.util.*;
import com.thinking.machines.util.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;

public class getbycode
{
 public static void main(String args[])
 {
  int code=Keyboard.in.getInt("Code:");
  try
  {
   DesignationInterface d= new Designation();
   DesignationManagerInterface da= DesignationManager.getInstance();
   
   if(da.designationCodeExists(code))
   System.out.println("Code exists");

   d=da.getDesignationByCode(code);
   System.out.println(d.getTitle()+" found with code: "+code);
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