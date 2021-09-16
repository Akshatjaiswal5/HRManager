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


class add
{
 public static void main(String args[])
 {
  String name= Keyboard.in.getString("Name:");
  String pan= Keyboard.in.getString("pan:");
  String aadhar= Keyboard.in.getString("aadhar:");
  int dcode=Keyboard.in.getInt("designation code:");
  DesignationInterface designation= new Designation();
  designation.setCode(dcode);
  GENDER gender=GENDER.MALE;
  try
  {gender=GenderChar.from(Keyboard.in.getCharacter("gender:"));
  }catch(GenderException ge)
  {System.out.println("gender format wrong");return;}
  boolean ind=Keyboard.in.getBoolean("is indian?:");
  BigDecimal salary= new BigDecimal(Keyboard.in.getString("salary:"));
  Date dob=null;
  try{
  dob=new SimpleDateFormat("dd/MM/yyyy").parse(Keyboard.in.getString("dob:"));
  }catch(ParseException pe){System.out.println("date format wrong");return;}

  try{
  EmployeeInterface employee= new Employee();
  EmployeeManagerInterface eManager= EmployeeManager.getInstance();

  employee.setName(name);
  employee.setPANNumber(pan);
  employee.setAadharCardNumber(aadhar);
  employee.setDesignation(designation);
  employee.setGender(gender);
  employee.setIsIndian(ind);
  employee.setBasicSalary(salary);
  employee.setDateOfBirth(dob);

  eManager.addEmployee(employee);
  System.out.println("added");
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