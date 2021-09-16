import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.util.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;


class update
{
 public static void main(String args[])
 {
  String employeeId=Keyboard.in.getString("EmployeeID:");
  String name= Keyboard.in.getString("Name:");
  String pan= Keyboard.in.getString("pan:");
  String aadhar= Keyboard.in.getString("aadhar:");
  int dcode=Keyboard.in.getInt("designation code:");
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
  EmployeeDTOInterface edto= new EmployeeDTO();
  EmployeeDAOInterface edao= new EmployeeDAO();

  edto.setEmployeeId(employeeId);
  edto.setName(name);
  edto.setPANNumber(pan);
  edto.setAadharCardNumber(aadhar);
  edto.setDesignationCode(dcode);
  edto.setGender(gender);
  edto.setIsIndian(ind);
  edto.setBasicSalary(salary);
  edto.setDateOfBirth(dob);

  edao.update(edto);
  System.out.println("added");
  }
  catch(DAOException e)
  {System.out.println(e.getMessage());}
  
  
 }


}		