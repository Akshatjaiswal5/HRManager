import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;

public class DesignationGetByCodeTestCase
{
 public static void main(String args[])
 {
  int code=Integer.parseInt(args[0]);

  try
  {
   DesignationDTOInterface d;
   DesignationDAOInterface da= new DesignationDAO();
   d=da.getByCode(code);
   System.out.println(d.getTitle()+" found with code: "+d.getCode()); 
  }
  catch(DAOException daoe)
  {
   System.out.println(daoe.getMessage());
  }  
 }
}