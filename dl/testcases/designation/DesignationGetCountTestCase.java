import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;

public class DesignationGetCountTestCase
{
 public static void main(String args[])
 {
  try
  {
   DesignationDAOInterface da= new DesignationDAO();
   System.out.println("Total member(s):"+da.getCount());   
  }
  catch(DAOException daoe)
  {
   System.out.println(daoe.getMessage());
  }  
 }
}