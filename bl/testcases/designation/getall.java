import java.util.*;
import com.thinking.machines.util.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;

public class getall
{
 public static void main(String args[])
 {
  try
  {

   Set<DesignationInterface> set= new TreeSet<>();

   DesignationManagerInterface da= DesignationManager.getInstance();
  
   System.out.println("Total: "+da.getDesignationCount());

   set=da.getDesignations();
   for(DesignationInterface designation:set)
   System.out.println(designation.getCode()+" "+designation.getTitle()); 
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