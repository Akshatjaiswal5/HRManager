import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import com.thinking.machines.hr.pl.model.*;
class DesignationModelTestCase extends JFrame
{
 DesignationModel dm;   
 JTable table;
 JScrollPane jsp;
 public static void main(String[] args)
 {
  DesignationModelTestCase a= new DesignationModelTestCase();
 }

 DesignationModelTestCase()
 {
  dm= new DesignationModel();
  table= new JTable(dm);
  jsp= new JScrollPane(table,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


  add(jsp);
  setSize(500,500);
  setLocation(100,100);   
  setVisible(true);
 }
}