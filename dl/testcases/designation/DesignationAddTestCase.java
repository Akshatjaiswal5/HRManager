import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;

public class DesignationAddTestCase
{
 public static void main(String args[])
 {
  String title=args[0];

  try
  {
   DesignationDTOInterface d= new DesignationDTO();
   d.setTitle(title);
   DesignationDAOInterface da= new DesignationDAO();
   da.add(d);
   System.out.println(title+" added with code: "+d.getCode()); 
  }
  catch(DAOException daoe)
  {
   System.out.println(daoe.getMessage());
  }  
 }
}