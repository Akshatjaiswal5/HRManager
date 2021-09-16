import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;

public class DesignationGetByTitleTestCase
{
 public static void main(String args[])
 {
  String title=args[0];

  try
  {
   DesignationDTOInterface d;
   DesignationDAOInterface da= new DesignationDAO();
   d=da.getByTitle(title);
   System.out.println(d.getTitle()+" found with code: "+d.getCode()); 
  }
  catch(DAOException daoe)
  {
   System.out.println(daoe.getMessage());
  }  
 }
}