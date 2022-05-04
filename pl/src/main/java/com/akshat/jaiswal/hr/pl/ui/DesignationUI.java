package com.akshat.jaiswal.hr.pl.ui;
import com.akshat.jaiswal.hr.pl.model.*;
import com.akshat.jaiswal.hr.bl.exceptions.*;
import com.akshat.jaiswal.hr.bl.interfaces.pojo.*;
import com.akshat.jaiswal.hr.bl.pojo.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.formdev.flatlaf.*;
import java.awt.geom.AffineTransform;


public class DesignationUI extends JFrame
{
 private DesignationModel designationModel;
 private JLabel titleLabel;
 private JTextField searchTextField; 
 private JButton clearSearchFieldButton;   
 private JTable designationTable;
 private JScrollPane jScrollPane;
 private DesignationPanel bottomPanel;
 private Container container;
 private NoScalingIcon titleLabelIcon,clearIcon;

 class DesignationPanel extends JPanel
 {
  private JLabel titleCaptionLabel,titleLabel;
  private JTextField titleTextField;
  private JButton add,edit,cancel,delete,exportToPDF,clear;
  private JPanel buttonsPanel;
  private DesignationInterface designation;
  private enum MODE{VIEW,ADD,EDIT,DELETE,EXPORT};
  private MODE mode;
  private NoScalingIcon addIcon,editIcon,cancelIcon,deleteIcon,exportIcon,saveIcon,updateIcon;
  private NoScalingIcon addOffIcon,editOffIcon,cancelOffIcon,deleteOffIcon,exportOffIcon;

  private boolean addDesignation()
  {
   String title=titleTextField.getText().trim();
   if(title.length()==0)
   {
    JOptionPane.showMessageDialog(this,"Designation cant be blank");
    titleTextField.requestFocus();
    return false;
   }
   
   DesignationInterface d = new Designation();
   d.setTitle(title);

   int rowIndex=0;
   try
   {
    designationModel.add(d);
    rowIndex=designationModel.indexOfDesignation(d);
    designationTable.setRowSelectionInterval(rowIndex,rowIndex);
    Rectangle rectangle= designationTable.getCellRect(rowIndex,0,true);
    designationTable.scrollRectToVisible(rectangle);
   }
   catch(BLException ble)
   {
    if(ble.hasGenericException())
    JOptionPane.showMessageDialog(this,ble.getGenericException());
    else if(ble.hasException("title"))
    JOptionPane.showMessageDialog(this,ble.getPropertyException("title"));

    titleTextField.requestFocus();
    return false;
   }
   return true;
  }
  private boolean updateDesignation()
  {
   String title=titleTextField.getText().trim();
   if(title.length()==0)
   {
    JOptionPane.showMessageDialog(this,"Designation cant be blank");
    titleTextField.requestFocus();
    return false;
   }
   
   DesignationInterface d = new Designation();
   d.setCode(this.designation.getCode());
   d.setTitle(title);

   int rowIndex=0;
   try
   {
    designationModel.update(d);
    rowIndex=designationModel.indexOfDesignation(d);
    designationTable.setRowSelectionInterval(rowIndex,rowIndex);
    Rectangle rectangle= designationTable.getCellRect(rowIndex,0,true);
    designationTable.scrollRectToVisible(rectangle);
   }
   catch(BLException ble)
   {
    if(ble.hasGenericException())
    JOptionPane.showMessageDialog(this,ble.getGenericException());
    else if(ble.hasException("title"))
    JOptionPane.showMessageDialog(this,ble.getPropertyException("title"));

    titleTextField.requestFocus();
   return false;
   }
   return true;
  }
  private void deleteDesignation()
  {
   try
   {
    String title= this.designation.getTitle();
    int selected=JOptionPane.showConfirmDialog(this,"delete "+title+"?","Confirmation",JOptionPane.YES_NO_OPTION);
    if(selected==JOptionPane.YES_OPTION) 
    {
      designationModel.remove(this.designation.getCode());
      JOptionPane.showMessageDialog(this,title+" deleted");
    }
   }
   catch(BLException ble)
   {
    if(ble.hasGenericException())
    JOptionPane.showMessageDialog(this,ble.getGenericException());
    else if(ble.hasException("title"))
    JOptionPane.showMessageDialog(this,ble.getPropertyException("title"));
   }
  }
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


  private void setViewMode()
  {  
   this.mode=MODE.VIEW;
   DesignationUI.this.setViewMode();
   this.titleTextField.setVisible(false);
   this.titleLabel.setVisible(true);
   this.clear.setVisible(false);

   this.add.setIcon(addIcon);
   this.add.setEnabled(true);
   this.cancel.setIcon(cancelOffIcon);
   this.cancel.setEnabled(false);
   this.exportToPDF.setIcon(exportIcon);
   this.exportToPDF.setEnabled(true);
   if(designation!=null)
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
   DesignationUI.this.setAddMode();
   this.titleTextField.setText("");
   this.titleLabel.setVisible(false);
   this.titleTextField.setVisible(true);
   this.clear.setVisible(true);
   
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
   DesignationUI.this.setEditMode();
   this.titleTextField.setText(this.designation.getTitle());
   this.titleLabel.setVisible(false);
   this.titleTextField.setVisible(true);
   this.clear.setVisible(true);

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
  private void setDeleteMode()
  {
   this.mode=MODE.DELETE;
   DesignationUI.this.setDeleteMode();
   this.add.setIcon(addOffIcon);
   this.add.setEnabled(false);
   this.edit.setIcon(editOffIcon);
   this.edit.setEnabled(false);
   this.cancel.setIcon(cancelOffIcon);
   this.cancel.setEnabled(false);
   this.delete.setIcon(deleteOffIcon);
   this.delete.setEnabled(false);
   this.exportToPDF.setIcon(exportOffIcon);
   this.exportToPDF.setEnabled(false);
   deleteDesignation();
   setViewMode();
  }
  private void setExportMode()
  {
   this.mode=MODE.EXPORT;
   DesignationUI.this.setExportMode();
   this.add.setIcon(addOffIcon);
   this.add.setEnabled(false);
   this.edit.setIcon(editOffIcon);
   this.edit.setEnabled(false);
   this.cancel.setIcon(cancelOffIcon);
   this.cancel.setEnabled(false);
   this.delete.setIcon(deleteOffIcon);
   this.delete.setEnabled(false);
   this.exportToPDF.setIcon(exportOffIcon);
   this.exportToPDF.setEnabled(false);
   exportDesignation();
   setViewMode();
  }
  private void setDesignation(DesignationInterface designation)
  {
   this.designation=designation;
   titleLabel.setText(designation.getTitle());
   titleTextField.setText(designation.getTitle());
  }
  private void clearDesignation()
  {
   this.designation=null;
   titleLabel.setText("");
   titleTextField.setText("");
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
   titleCaptionLabel= new JLabel("Designation");
   titleLabel= new JLabel("");
   titleTextField= new JTextField();
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

   titleCaptionLabel.setFont(new Font("Default",Font.PLAIN,20));
   titleCaptionLabel.setBounds(12,10,120,50);
   add(titleCaptionLabel);

   titleLabel.setFont(new Font("Default",Font.PLAIN,20));
   titleLabel.setBounds(10+120,10,320,50);
   add(titleLabel);

   titleTextField.setFont(new Font("Default",Font.PLAIN,20));
   titleTextField.setBounds(9+120,19,280,35);
   add(titleTextField);


   clear.setBounds(14+360+40,19,35,35);
   clear.setIcon(clearIcon);
   add(clear);

   add.setBounds(15,70,70,40);
   add.setIcon(addIcon);
   add(add);

   edit.setBounds(15+90,70,70,40);
   edit.setIcon(editIcon);
   add(edit);

   cancel.setBounds(15+180,70,70,40);   
   cancel.setIcon(cancelIcon);
   add(cancel);

   delete.setBounds(15+270,70,70,40);  
   delete.setIcon(deleteIcon); 
   add(delete);

   exportToPDF.setBounds(15+360,70,70,40);  
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
      if(addDesignation())
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
      if(updateDesignation())
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
     setDeleteMode();
    }
   });
  
   clear.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent ev)
    {
     titleTextField.setText("");
     searchTextField.requestFocus();
    }
   });
   exportToPDF.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent ev)
    {
     setExportMode();
    }
   });
  }
  DesignationPanel()
  {
   
   prepareResources();
   initComponents();
   setAppearance();
   addListeners();
  }
 }

 private void setViewMode()
 {  
  if(designationModel.getRowCount()==0)
  {
   searchTextField.setEnabled(false);
   clearSearchFieldButton.setEnabled(false);
   designationTable.setEnabled(false);   
  }
  else
  {
   searchTextField.setEnabled(true);
   clearSearchFieldButton.setEnabled(true);
   designationTable.setEnabled(true);
  }
 }
 private void setAddMode()
 {
  searchTextField.setEnabled(false);
  clearSearchFieldButton.setEnabled(false);
  designationTable.setEnabled(false); 
 }
 private void setEditMode()
 {
  searchTextField.setEnabled(false);
  clearSearchFieldButton.setEnabled(false);
  designationTable.setEnabled(false); 
 }
 private void setDeleteMode()
 {
  searchTextField.setEnabled(false);
  clearSearchFieldButton.setEnabled(false);
  designationTable.setEnabled(false); 
 }
 private void setExportMode()
 {
  searchTextField.setEnabled(false);
  clearSearchFieldButton.setEnabled(false);
  designationTable.setEnabled(false); 
 }
 private void searchDesignation()
 {
  searchTextField.setForeground(Color.BLACK);
  String title=searchTextField.getText().trim();
  if(title.length()==0)
  return;
  int rowIndex=0;
  try
  {
   rowIndex=designationModel.indexOfTitle(title,true);
  }
  catch(BLException ble)
  {
   searchTextField.setForeground(Color.RED); 
   return;
  }
  designationTable.setRowSelectionInterval(rowIndex,rowIndex);
  Rectangle rectangle= designationTable.getCellRect(rowIndex,0,true);
  designationTable.scrollRectToVisible(rectangle);
 }

 private void prepareResources()
 {
  titleLabelIcon=new NoScalingIcon(new ImageIcon(getClass().getResource("/images/design.png")));
  clearIcon = new NoScalingIcon(new ImageIcon(getClass().getResource("/images/clear.png")));
 }
 private void initComponents()
 {
  FlatLightLaf.setup();

  designationModel=new DesignationModel();
  titleLabel= new JLabel(titleLabelIcon);
  searchTextField= new JTextField();
  clearSearchFieldButton= new JButton();
  designationTable= new JTable(designationModel);
  jScrollPane= new JScrollPane(designationTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  bottomPanel= new DesignationPanel();
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
  
  designationTable.setFont(new Font("Default",Font.PLAIN,20));
  designationTable.setRowHeight(30);
  designationTable.getColumnModel().getColumn(0).setPreferredWidth(100);
  designationTable.getColumnModel().getColumn(1).setPreferredWidth(900);
  designationTable.setRowSelectionAllowed(true);
  designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
  centerRenderer.setHorizontalAlignment( JLabel.CENTER );
  designationTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
  JTableHeader tableHeader=designationTable.getTableHeader();
  tableHeader.setFont(new Font("Default",Font.PLAIN,15));
  tableHeader.setReorderingAllowed(false);
  tableHeader.setResizingAllowed(false);
  jScrollPane.setBounds(12,10+50+10+35+20,460,300);
  container.add(jScrollPane);
  
  
  bottomPanel.setBounds(12,10+50+10+35+20+300+20,460,125);
  container.add(bottomPanel);

  Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
  int w=500,h=625;
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
    searchDesignation();
   }
   public void removeUpdate(DocumentEvent ev)
   {
    searchDesignation();
   }
   public void insertUpdate(DocumentEvent ev)
   {
    searchDesignation();
   }
  });
  
  clearSearchFieldButton.addActionListener(new ActionListener(){
   public void actionPerformed(ActionEvent ev)
   {
    searchTextField.setText("");
    searchTextField.requestFocus();
   }
  });

  designationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
   public void valueChanged(ListSelectionEvent ev)
   {
    int selectedRowIndex= designationTable.getSelectedRow();
    try
    {
     DesignationInterface designation= designationModel.getDesignationAt(selectedRowIndex);
     bottomPanel.setDesignation(designation);
    }
    catch(BLException ble)
    {
     bottomPanel.clearDesignation();
    }
    bottomPanel.setViewMode();
   }
  });
  setDefaultCloseOperation(DISPOSE_ON_CLOSE);
 }
 public DesignationUI()
 {
  prepareResources();
  initComponents();
  setAppearance();
  addListeners();
  bottomPanel.setViewMode();
 }
}
class NoScalingIcon implements Icon
{
 private Icon icon;
 public NoScalingIcon(Icon icon)
 {
  this.icon = icon;
 }
 public int getIconWidth()
 {
  return icon.getIconWidth();
 }
 public int getIconHeight()
 {
  return icon.getIconHeight();
 }
 public void paintIcon(Component c, Graphics g, int x, int y)
 {
  Graphics2D g2d = (Graphics2D)g.create();
  AffineTransform at = g2d.getTransform();

  int scaleX = (int)(x * at.getScaleX());
  int scaleY = (int)(y * at.getScaleY());

  int offsetX = (int)(icon.getIconWidth() * (at.getScaleX() - 1) / 2);
  int offsetY = (int)(icon.getIconHeight() * (at.getScaleY() - 1) / 2);

  int locationX = scaleX + offsetX;
  int locationY = scaleY + offsetY;

  //  Reset scaling to 1.0 by concatenating an inverse scale transfom

  AffineTransform scaled = AffineTransform.getScaleInstance(1.0 / at.getScaleX(), 1.0 / at.getScaleY());
  at.concatenate( scaled );
  g2d.setTransform( at );

  icon.paintIcon(c, g2d, locationX, locationY);

  g2d.dispose();
 }
}
