import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;

public class DesignationUpdateTestCase
{
 public static void main(String args[])
 {
  String title=args[0];
  int code= Integer.parseInt(args[1]);

  try
  {
   DesignationDTOInterface d= new DesignationDTO();
   d.setTitle(title);
   d.setCode(code);
   DesignationDAOInterface da= new DesignationDAO();
   da.update(d);
   System.out.println(title+" updated with code: "+d.getCode()); 
  }
  catch(DAOException daoe)
  {
   System.out.println(daoe.getMessage());
  }  
 }
}