package com.akshat.jaiswal.hr.pl.ui;
import com.akshat.jaiswal.hr.pl.model.*;
import com.akshat.jaiswal.hr.bl.exceptions.*;
import com.akshat.jaiswal.hr.bl.interfaces.pojo.*;
import com.akshat.jaiswal.hr.bl.pojo.*;
import com.akshat.jaiswal.hr.bl.interfaces.managers.*;
import com.akshat.jaiswal.hr.bl.managers.*;
import com.akshat.jaiswal.enums.*;

import java.util.*;
import java.math.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.formdev.flatlaf.*;
import java.awt.geom.AffineTransform;



public class EmployeeUI extends JFrame
{
 private EmployeeModel employeeModel;
 private JLabel titleLabel;
 private JTextField searchTextField; 
 private JButton clearSearchFieldButton,manageDesignationButton;   
 private JTable employeeTable;
 private JScrollPane jScrollPane;
 private Container container;
 private NoScalingIcon titleLabelIcon,clearIcon,mdIcon,mdOffIcon;
 private EmployeePanel sidePanel;

 class EmployeePanel extends JPanel
 {
  private JLabel nameCaptionLabel,nameLabel;
  private JTextField nameTextField;
  private JLabel idCaptionLabel,idLabel;
  private JLabel designationCaptionLabel,designationLabel;
  private JTextField designationTextField;
  private JLabel dobCaptionLabel,dobLabel;
  private JTextField dobTextField;
  private JLabel genderCaptionLabel,genderLabel;
  private JComboBox<String> genderSelector;
  private JLabel nationalityCaptionLabel,nationalityLabel;
  private JComboBox<String> nationalitySelector;
  private JLabel salaryCaptionLabel,salaryLabel;
  private JTextField salaryTextField;
  private JLabel panCaptionLabel,panLabel;
  private JTextField panTextField;
  private JLabel aadharCaptionLabel,aadharLabel;
  private JTextField aadharTextField;
  private JButton add,edit,cancel,delete,exportToPDF,clear;
  private JPanel buttonsPanel;
  private EmployeeInterface employee;
  private enum MODE{VIEW,ADD,EDIT};
  private MODE mode;
  private NoScalingIcon addIcon,editIcon,cancelIcon,deleteIcon,exportIcon,saveIcon,updateIcon;
  private NoScalingIcon addOffIcon,editOffIcon,cancelOffIcon,deleteOffIcon,exportOffIcon;


  private void setViewMode()
  {
    this.mode=MODE.VIEW;
    EmployeeUI.this.enableComponents();

    this.nameTextField.setVisible(false);
    this.designationTextField.setVisible(false);
    this.dobTextField.setVisible(false);
    this.genderSelector.setVisible(false);
    this.nationalitySelector.setVisible(false);
    this.salaryTextField.setVisible(false);
    this.panTextField.setVisible(false);
    this.aadharTextField.setVisible(false);

    this.nameLabel.setVisible(true);
    this.idLabel.setVisible(true);
    this.designationLabel.setVisible(true);
    this.dobLabel.setVisible(true);
    this.genderLabel.setVisible(true);
    this.nationalityLabel.setVisible(true);
    this.salaryLabel.setVisible(true);
    this.panLabel.setVisible(true);
    this.aadharLabel.setVisible(true);

    this.add.setIcon(addIcon);
    this.add.setEnabled(true);
    this.cancel.setIcon(cancelOffIcon);
    this.cancel.setEnabled(false);
    this.exportToPDF.setIcon(exportIcon);
    this.exportToPDF.setEnabled(true);
    if(employee!=null)
    {
     this.edit.setIcon(editIcon);
     this.edit.setEnabled(true);
     this.delete.setIcon(deleteIcon);
     this.delete.setEnabled(true);
    }
    else
    {
     this.edit.setIcon(editOffIcon);
     this.edit.setEnabled(false);
     this.delete.setIcon(deleteOffIcon);
     this.delete.setEnabled(false);  
    }
  }

  private void setAddMode()
  {
   this.mode=MODE.ADD;
   EmployeeUI.this.disableComponents();

   this.nameTextField.setVisible(true);
   this.designationTextField.setVisible(true);
   this.dobTextField.setVisible(true);
   this.genderSelector.setVisible(true);
   this.nationalitySelector.setVisible(true);
   this.salaryTextField.setVisible(true);
   this.panTextField.setVisible(true);
   this.aadharTextField.setVisible(true);

   this.nameLabel.setVisible(false);
   this.idLabel.setVisible(false);
   this.designationLabel.setVisible(false);
   this.dobLabel.setVisible(false);
   this.genderLabel.setVisible(false);
   this.nationalityLabel.setVisible(false);
   this.salaryLabel.setVisible(false);
   this.panLabel.setVisible(false);
   this.aadharLabel.setVisible(false);

   this.nameTextField.setText("");
   this.designationTextField.setText("");
   this.dobTextField.setText("");
   this.genderSelector.setSelectedIndex(0);
   this.nationalitySelector.setSelectedIndex(0);
   this.salaryTextField.setText("");
   this.panTextField.setText("");
   this.aadharTextField.setText("");


   this.add.setIcon(saveIcon);
   this.edit.setIcon(editOffIcon);
   this.edit.setEnabled(false);
   this.cancel.setIcon(cancelIcon);
   this.cancel.setEnabled(true);
   this.delete.setIcon(deleteOffIcon);
   this.delete.setEnabled(false);
   this.exportToPDF.setIcon(exportOffIcon);
   this.exportToPDF.setEnabled(false);
  }

  private void setEditMode()
  {
   this.mode=MODE.EDIT;
   EmployeeUI.this.disableComponents();

   this.nameTextField.setVisible(true);
   this.designationTextField.setVisible(true);
   this.dobTextField.setVisible(true);
   this.genderSelector.setVisible(true);
   this.nationalitySelector.setVisible(true);
   this.salaryTextField.setVisible(true);
   this.panTextField.setVisible(true);
   this.aadharTextField.setVisible(true);

   this.nameLabel.setVisible(false);
   this.idLabel.setVisible(false);
   this.designationLabel.setVisible(false);
   this.dobLabel.setVisible(false);
   this.genderLabel.setVisible(false);
   this.nationalityLabel.setVisible(false);
   this.salaryLabel.setVisible(false);
   this.panLabel.setVisible(false);
   this.aadharLabel.setVisible(false);

   this.nameTextField.setText(employee.getName());
   this.designationTextField.setText(employee.getDesignation().getTitle());
   this.dobTextField.setText(new SimpleDateFormat("dd/MM/yyyy").format(employee.getDateOfBirth()));
   this.genderSelector.setSelectedIndex(employee.getGender()==GENDER.MALE?1:2);
   this.nationalitySelector.setSelectedIndex(employee.getIsIndian()?1:2);
   this.salaryTextField.setText(employee.getBasicSalary().toString());
   this.panTextField.setText(employee.getPANNumber());
   this.aadharTextField.setText(employee.getAadharCardNumber());

   this.add.setIcon(addOffIcon);
   this.add.setEnabled(false);
   this.edit.setIcon(updateIcon);
   this.cancel.setIcon(cancelIcon);
   this.cancel.setEnabled(true);
   this.delete.setIcon(deleteOffIcon);
   this.delete.setEnabled(false);
   this.exportToPDF.setIcon(exportOffIcon);
   this.exportToPDF.setEnabled(false);
  }
  
  /* 

  private void exportDesignation()
  {
    JFileChooser jfc= new JFileChooser();
    int selected=jfc.showSaveDialog(DesignationUI.this);
  
    if(selected!=JFileChooser.APPROVE_OPTION)
    return;
  
    File file=jfc.getSelectedFile();

    try
    {
     designationModel.exportAsPdf(file);
     JOptionPane.showMessageDialog(this,"Exported as "+file.getName());
    }
    catch(BLException ble)
    {
     JOptionPane.showMessageDialog(this,"An error occured");
    }    
  } 
  */

  private boolean addEmployee()
  {
   try
   {
     BLException ble= new BLException();
     String name=nameTextField.getText().trim();

     DesignationManagerInterface designationManager= DesignationManager.getInstance();
     if(!designationManager.designationTitleExists(designationTextField.getText().trim()))
      ble.addException("designation","Invalid Designation");

     if(ble.hasExceptions())
      throw ble;
     DesignationInterface designation=designationManager.getDesignationByTitle(designationTextField.getText().trim());
     
     System.out.println(designation.getCode());

     if(genderSelector.getSelectedIndex()==0)
      ble.addException("gender","select a gender");
     
     GENDER gender=genderSelector.getSelectedIndex()==1?GENDER.MALE:GENDER.FEMALE;

     if(nationalitySelector.getSelectedIndex()==0)
      ble.addException("nationality","select a nationality");

     boolean isIndian=nationalitySelector.getSelectedIndex()==1?true:false;

     BigDecimal salary= null;
     try
     {
       salary=new BigDecimal(Double.parseDouble(salaryTextField.getText().trim()));
     }
     catch(Exception e)
     {
      ble.addException("salary","Salary format wrong");
     }
     Date dob=null;
     try
     {
      dob=new SimpleDateFormat("dd/MM/yyyy").parse(dobTextField.getText().trim());
     }catch(ParseException pe)
     {
      ble.addException("date","Date format wrong");
     }
     String pan=panTextField.getText().trim();
     String aadhar=aadharTextField.getText().trim();

     if(ble.hasExceptions())
     throw ble;

     EmployeeInterface employee= new Employee();
     employee.setName(name);
     employee.setPANNumber(pan);
     employee.setAadharCardNumber(aadhar);
     employee.setDesignation(designation);
     employee.setGender(gender);
     employee.setIsIndian(isIndian);
     employee.setBasicSalary(salary);
     employee.setDateOfBirth(dob);
     int rowIndex=0;
     employeeModel.add(employee);
     rowIndex=employeeModel.indexOfEmployee(employee);
     employeeTable.setRowSelectionInterval(rowIndex,rowIndex);
     Rectangle rectangle= employeeTable.getCellRect(rowIndex,0,true);
     employeeTable.scrollRectToVisible(rectangle);
     
     return true;
   }
   catch(BLException ble)
   {
    String msg="";
    if(ble.hasGenericException())
    msg+=ble.getGenericException()+"\n";

    java.util.List<String> properties=ble.getProperties();
    for(String property:properties)
    {
      msg+=property+": "+ble.getPropertyException(property)+"\n";;
    }

    JOptionPane.showMessageDialog(this,msg);

    return false;
   }
  }

  private boolean updateEmployee()
  {
    try
   {
     BLException ble= new BLException();
     String name=nameTextField.getText().trim();

     DesignationManagerInterface designationManager= DesignationManager.getInstance();
     if(!designationManager.designationTitleExists(designationTextField.getText().trim()))
      ble.addException("designation","Invalid Designation");

     DesignationInterface designation=designationManager.getDesignationByTitle(designationTextField.getText().trim());
     
     if(genderSelector.getSelectedIndex()==0)
      ble.addException("gender","select a gender");
     
     GENDER gender=genderSelector.getSelectedIndex()==1?GENDER.MALE:GENDER.FEMALE;

     if(nationalitySelector.getSelectedIndex()==0)
      ble.addException("nationality","select a nationality");

     boolean isIndian=nationalitySelector.getSelectedIndex()==1?true:false;

     BigDecimal salary= null;
     try
     {
       salary=new BigDecimal(Double.parseDouble(salaryTextField.getText().trim()));
     }
     catch(Exception e)
     {
      ble.addException("salary","Salary format wrong");
     }
     Date dob=null;
     try
     {
      dob=new SimpleDateFormat("dd/MM/yyyy").parse(dobTextField.getText().trim());
     }catch(ParseException pe)
     {
      ble.addException("date","Date format wrong");
     }
     String pan=panTextField.getText().trim();
     String aadhar=aadharTextField.getText().trim();

     if(ble.hasExceptions())
     throw ble;

     EmployeeInterface employee= new Employee();
     employee.setName(name);
     employee.setEmployeeId(this.employee.getEmployeeId());
     employee.setPANNumber(pan);
     employee.setAadharCardNumber(aadhar);
     employee.setDesignation(designation);
     employee.setGender(gender);
     employee.setIsIndian(isIndian);
     employee.setBasicSalary(salary);
     employee.setDateOfBirth(dob);
     int rowIndex=0;
     employeeModel.update(employee);
     rowIndex=employeeModel.indexOfEmployee(employee);
     employeeTable.setRowSelectionInterval(rowIndex,rowIndex);
     Rectangle rectangle= employeeTable.getCellRect(rowIndex,0,true);
     employeeTable.scrollRectToVisible(rectangle);
     
     return true;
   }
   catch(BLException ble)
   {
    String msg="";
    if(ble.hasGenericException())
    msg+=ble.getGenericException()+"\n";

    java.util.List<String> properties=ble.getProperties();
    for(String property:properties)
    {
      msg+=property+": "+ble.getPropertyException(property)+"\n";;
    }

    JOptionPane.showMessageDialog(this,msg);

    return false;
   }
  }

  private void deleteEmployee()
  {
    try
    {
     String name= this.employee.getName();
     int selected=JOptionPane.showConfirmDialog(this,"delete "+name+"?","Confirmation",JOptionPane.YES_NO_OPTION);
     if(selected==JOptionPane.YES_OPTION) 
     {
       employeeModel.remove(this.employee.getEmployeeId());
       JOptionPane.showMessageDialog(this,name+" deleted");
     }
    }
    catch(BLException ble)
    {
     String msg="";
     if(ble.hasGenericException())
      msg+=ble.getGenericException()+"\n";

     java.util.List<String> properties=ble.getProperties();
     for(String property:properties)
     {
      msg+=property+": "+ble.getPropertyException(property)+"\n";;
     }

     JOptionPane.showMessageDialog(this,msg);
    }
  }

  private void prepareResources()
  {
   addIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/add.png")));
   editIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/edit.png")));
   cancelIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/cancel.png")));
   deleteIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/delete.png")));
   exportIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/export.png")));
   saveIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/save.png")));
   updateIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/update.png")));
   addOffIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/addOff.png")));
   editOffIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/editOff.png")));
   cancelOffIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/cancelOff.png")));
   deleteOffIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/deleteOff.png")));
   exportOffIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/exportOff.png")));
  }
  private void initComponents()
  {
   nameCaptionLabel= new JLabel("Employee Name");
   nameLabel= new JLabel("");
   nameTextField= new JTextField();
   
   idCaptionLabel= new JLabel("Employee ID");
   idLabel= new JLabel("");

   designationCaptionLabel= new JLabel("Designation");
   designationLabel= new JLabel("");
   designationTextField= new JTextField();

   dobCaptionLabel= new JLabel("Date of Birth");
   dobLabel= new JLabel("");
   dobTextField= new JTextField();

   genderCaptionLabel= new JLabel("Gender");
   genderLabel= new JLabel("");
   String genders[]={"","Male","Female"};
   genderSelector= new JComboBox<>(genders);

   nationalityCaptionLabel= new JLabel("Nationality");
   nationalityLabel= new JLabel("");
   String nationalities[]={"","Indian","Non-Indian"};
   nationalitySelector= new JComboBox<>(nationalities);;

   salaryCaptionLabel= new JLabel("Basic Pay");
   salaryLabel= new JLabel("");
   salaryTextField= new JTextField();

   panCaptionLabel= new JLabel("PAN Number");
   panLabel= new JLabel("");
   panTextField= new JTextField();

   aadharCaptionLabel= new JLabel("Aadhar Number");
   aadharLabel= new JLabel("");
   aadharTextField= new JTextField();


   clear = new JButton();
   add = new JButton();
   edit = new JButton();
   cancel = new JButton();
   delete = new JButton();
   exportToPDF = new JButton();
  }
  private void setAppearance()
  {
   setBorder(BorderFactory.createLineBorder(new Color(165,165,165)));
   setLayout(null);

   nameCaptionLabel.setFont(new Font("Default",Font.PLAIN,20));
   nameCaptionLabel.setBounds(12,10,180,50);
   add(nameCaptionLabel);

   nameLabel.setFont(new Font("Default",Font.PLAIN,20));
   nameLabel.setBounds(10+180,10,320,50);
   add(nameLabel);

   nameTextField.setFont(new Font("Default",Font.PLAIN,20));
   nameTextField.setBounds(9+180,19,240,35);
   add(nameTextField);

   idCaptionLabel.setFont(new Font("Default",Font.PLAIN,20));
   idCaptionLabel.setBounds(12,10+40+5,180,50);
   add(idCaptionLabel);

   idLabel.setFont(new Font("Default",Font.PLAIN,20));
   idLabel.setBounds(10+180,10+40+5,320,50);
   add(idLabel);

   designationCaptionLabel.setFont(new Font("Default",Font.PLAIN,20));
   designationCaptionLabel.setBounds(12,10+90,180,50);
   add(designationCaptionLabel);

   designationLabel.setFont(new Font("Default",Font.PLAIN,20));
   designationLabel.setBounds(10+180,10+90,320,50);
   add(designationLabel);

   designationTextField.setFont(new Font("Default",Font.PLAIN,20));
   designationTextField.setBounds(9+180,19+90,240,35);
   add(designationTextField);

   dobCaptionLabel.setFont(new Font("Default",Font.PLAIN,20));
   dobCaptionLabel.setBounds(12,10+90+45,180,50);
   add(dobCaptionLabel);

   dobLabel.setFont(new Font("Default",Font.PLAIN,20));
   dobLabel.setBounds(10+180,10+90+45,320,50);
   add(dobLabel);

   dobTextField.setFont(new Font("Default",Font.PLAIN,20));
   dobTextField.setBounds(9+180,19+90+45,240,35);
   add(dobTextField);
   
   genderCaptionLabel.setFont(new Font("Default",Font.PLAIN,20));
   genderCaptionLabel.setBounds(12,10+90+45+45,180,50);
   add(genderCaptionLabel);

   genderLabel.setFont(new Font("Default",Font.PLAIN,20));
   genderLabel.setBounds(10+180,10+90+45+45,320,50);
   add(genderLabel);

   genderSelector.setFont(new Font("Default",Font.PLAIN,20));
   genderSelector.setBounds(9+180,19+90+45+45,180,35);
   add(genderSelector);

   nationalityCaptionLabel.setFont(new Font("Default",Font.PLAIN,20));
   nationalityCaptionLabel.setBounds(12,10+180+45,180,50);
   add(nationalityCaptionLabel);

   nationalityLabel.setFont(new Font("Default",Font.PLAIN,20));
   nationalityLabel.setBounds(10+180,10+180+45,320,50);
   add(nationalityLabel);

   nationalitySelector.setFont(new Font("Default",Font.PLAIN,20));
   nationalitySelector.setBounds(9+180,19+180+45,180,35);
   add(nationalitySelector);

   salaryCaptionLabel.setFont(new Font("Default",Font.PLAIN,20));
   salaryCaptionLabel.setBounds(12,10+180+90,180,50);
   add(salaryCaptionLabel);

   salaryLabel.setFont(new Font("Default",Font.PLAIN,20));
   salaryLabel.setBounds(10+180,10+180+90,320,50);
   add(salaryLabel);

   salaryTextField.setFont(new Font("Default",Font.PLAIN,20));
   salaryTextField.setBounds(9+180,19+180+90,180,35);
   add(salaryTextField);

   panCaptionLabel.setFont(new Font("Default",Font.PLAIN,20));
   panCaptionLabel.setBounds(12,10+180+90+45,180,50);
   add(panCaptionLabel);

   panLabel.setFont(new Font("Default",Font.PLAIN,20));
   panLabel.setBounds(10+180,10+180+90+45,320,50);
   add(panLabel);

   panTextField.setFont(new Font("Default",Font.PLAIN,20));
   panTextField.setBounds(9+180,19+180+90+45,240,35);
   add(panTextField);

   aadharCaptionLabel.setFont(new Font("Default",Font.PLAIN,20));
   aadharCaptionLabel.setBounds(12,10+180+180,180,50);
   add(aadharCaptionLabel);

   aadharLabel.setFont(new Font("Default",Font.PLAIN,20));
   aadharLabel.setBounds(10+180,10+180+180,320,50);
   add(aadharLabel);

   aadharTextField.setFont(new Font("Default",Font.PLAIN,20));
   aadharTextField.setBounds(9+180,19+180+180,240,35);
   add(aadharTextField);

   add.setBounds(15,70+340+55,70,40);
   add.setIcon(addIcon);
   add(add);

   edit.setBounds(15+90,70+340+55,70,40);
   edit.setIcon(editIcon);
   add(edit);

   cancel.setBounds(15+180,70+340+55,70,40);   
   cancel.setIcon(cancelIcon);
   add(cancel);

   delete.setBounds(15+270,70+340+55,70,40);  
   delete.setIcon(deleteIcon); 
   add(delete);

   exportToPDF.setBounds(15+360,70+340+55,70,40);  
   exportToPDF.setIcon(exportIcon); 
   add(exportToPDF);
   
  }
  private void addListeners()
  {
    add.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent ev)
        {
         if(mode==MODE.VIEW)
         {
          setAddMode();
         }
         else
         {
          if(addEmployee())
          setViewMode();
         }
        }
       });

    edit.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent ev)
     {
      if(mode==MODE.VIEW)
      {
       setEditMode();
      }
      else
      {
       if(updateEmployee())
       setViewMode();
      }
     }
    });

    cancel.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ev)
      {
        setViewMode();
      }
     });

    delete.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent ev)
        {
         deleteEmployee();
        }
       });

    clear.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ev)
      {
        
      }
      });

    exportToPDF.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent ev)
        {
          
        }
        });
  }
  private void setEmployee(EmployeeInterface employee)
  {
    this.employee=employee;
    nameLabel.setText(employee.getName());
    idLabel.setText(employee.getEmployeeId());
    designationLabel.setText(employee.getDesignation().getTitle());
    dobLabel.setText(new SimpleDateFormat("dd/MM/yyyy").format(employee.getDateOfBirth()));
    genderLabel.setText(employee.getGender()==GENDER.MALE?"Male":"Female");
    nationalityLabel.setText(employee.getIsIndian()?"Indian":"Non Indian");
    salaryLabel.setText(employee.getBasicSalary().toString()+ " Rs");
    panLabel.setText(employee.getPANNumber());
    aadharLabel.setText(employee.getAadharCardNumber());
  }
  private void clearEmployee()
  {
    this.employee=null;
    nameLabel.setText("");
    idLabel.setText("");
    designationLabel.setText("");
    dobLabel.setText("");
    genderLabel.setText("");
    nationalityLabel.setText("");
    salaryLabel.setText("");
    panLabel.setText("");
    aadharLabel.setText("");
  }
  EmployeePanel()
  {
   prepareResources();
   initComponents();
   setAppearance();
   addListeners();
  }
 }

 private void enableComponents()
 {
  searchTextField.setEnabled(true);
  clearSearchFieldButton.setEnabled(true);
  employeeTable.setEnabled(true);
  manageDesignationButton.setEnabled(true);
  manageDesignationButton.setIcon(mdIcon);
 }
 private void disableComponents()
 {
  searchTextField.setEnabled(false);
  clearSearchFieldButton.setEnabled(false);
  employeeTable.setEnabled(false);
  manageDesignationButton.setEnabled(false); 
  manageDesignationButton.setIcon(mdOffIcon);
 }
 private void searchEmployee()
 {
  searchTextField.setForeground(Color.BLACK);
  String name=searchTextField.getText().trim();
  if(name.length()==0)
  return;
  int rowIndex=0;
  try
  {
   rowIndex=employeeModel.indexOfName(name,true);
  }
  catch(BLException ble)
  {
   searchTextField.setForeground(Color.RED); 
   return;
  }
  employeeTable.setRowSelectionInterval(rowIndex,rowIndex);
  Rectangle rectangle= employeeTable.getCellRect(rowIndex,0,true);
  employeeTable.scrollRectToVisible(rectangle);
 }
 private void prepareResources()
 {
  titleLabelIcon=new NoScalingIcon(new ImageIcon(getClass().getResource("/images/employ.png")));
  clearIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/clear.png")));
  mdIcon= new NoScalingIcon(new ImageIcon(getClass().getResource("/images/md.png")));
  mdOffIcon=new NoScalingIcon(new ImageIcon(getClass().getResource("/images/mdOff.png")));
 }
 private void initComponents()
 {
  FlatLightLaf.setup();

  employeeModel=new EmployeeModel();
  titleLabel= new JLabel(titleLabelIcon);
  searchTextField= new JTextField();
  clearSearchFieldButton= new JButton();
  employeeTable= new JTable(employeeModel);
  jScrollPane= new JScrollPane(employeeTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  sidePanel= new EmployeePanel();
  manageDesignationButton= new JButton();
  container=getContentPane();
 }
 private void setAppearance()
 {
  container.setLayout(null);

  titleLabel.setBounds(105,10,270,60);
  container.add(titleLabel);

  searchTextField.setFont(new Font("Default",Font.PLAIN,20));
  searchTextField.setBounds(70,10+50+10,325,35);
  container.add(searchTextField);

  clearSearchFieldButton.setBounds(70+325+5,10+50+10,35,35);
  clearSearchFieldButton.setIcon(clearIcon);
  container.add(clearSearchFieldButton); 
  
  employeeTable.setFont(new Font("Default",Font.PLAIN,20));
  employeeTable.setRowHeight(30);
  employeeTable.getColumnModel().getColumn(0).setPreferredWidth(100);
  employeeTable.getColumnModel().getColumn(1).setPreferredWidth(900);
  employeeTable.setRowSelectionAllowed(true);
  employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
  centerRenderer.setHorizontalAlignment( JLabel.CENTER );
  employeeTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
  JTableHeader tableHeader=employeeTable.getTableHeader();
  tableHeader.setFont(new Font("Default",Font.PLAIN,15));
  tableHeader.setReorderingAllowed(false);
  tableHeader.setResizingAllowed(false);
  jScrollPane.setBounds(12,10+50+10+35+20,460,400);
  container.add(jScrollPane);
  
  sidePanel.setBounds(510,10+50+10,460,515);
  container.add(sidePanel);

  manageDesignationButton.setBounds(12,535,240,40);
  manageDesignationButton.setIcon(mdIcon);
  container.add(manageDesignationButton);
  

  Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
  int w=1000,h=635;
  setSize(w,h);
  setLocation((d.width/2)-w/2,(d.height/2)-h/2);
  setIconImage(new ImageIcon(getClass().getResource("/images/appIcon.png")).getImage());
  setResizable(false);
 }
 private void addListeners()
 {
  searchTextField.getDocument().addDocumentListener(new DocumentListener(){
   public void changedUpdate(DocumentEvent ev)
   {
    searchEmployee();
   }
   public void removeUpdate(DocumentEvent ev)
   {
    searchEmployee();
   }
   public void insertUpdate(DocumentEvent ev)
   {
    searchEmployee();
   }
  });
  
  clearSearchFieldButton.addActionListener(new ActionListener(){
   public void actionPerformed(ActionEvent ev)
   {
    searchTextField.setText("");
    searchTextField.requestFocus();
   }
  });

  employeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
   public void valueChanged(ListSelectionEvent ev)
   {
    int selectedRowIndex= employeeTable.getSelectedRow();
    try
    {
     EmployeeInterface employee= employeeModel.getEmployeeAt(selectedRowIndex);
     sidePanel.setEmployee(employee);
    }
    catch(BLException ble)
    {
     sidePanel.clearEmployee();
    }
    sidePanel.setViewMode();
   }
  });
  manageDesignationButton.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent ev)
    {
     DesignationUI dui=new DesignationUI();
     dui.setVisible(true);
    }
   });

  setDefaultCloseOperation(EXIT_ON_CLOSE);
 }
 public EmployeeUI()
 {
  prepareResources();
  initComponents();
  setAppearance();
  addListeners();
  sidePanel.setViewMode();
 }
}
