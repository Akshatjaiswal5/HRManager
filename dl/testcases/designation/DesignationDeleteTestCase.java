import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;

public class DesignationDeleteTestCase
{
 public static void main(String args[])
 {
  int code=Integer.parseInt(args[0]);

  try
  {
   DesignationDTOInterface d;
   DesignationDAOInterface da= new DesignationDAO();
   da.delete(code);
   System.out.println("deleted"); 
  }
  catch(DAOException daoe)
  {
   System.out.println(daoe.getMessage());
  }  
 }
}