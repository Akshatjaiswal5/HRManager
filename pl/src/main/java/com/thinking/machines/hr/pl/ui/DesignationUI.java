package com.thinking.machines.hr.pl.ui;
import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class DesignationUI extends JFrame
{
 private DesignationModel designationModel;
 private JLabel titleLabel,searchLabel,searchErrorLabel;
 private JTextField searchTextField; 
 private JButton clearSearchFieldButton;   
 private JTable designationTable;
 private JScrollPane jScrollPane;
 private DesignationPanel bottomPanel;
 private Container container;

 class DesignationPanel extends JPanel
 {
  private JLabel titleCaptionLabel,titleLabel;
  private JTextField titleTextField;
  private JButton add,edit,cancel,delete,exportToPDF,clear;
  private JPanel buttonsPanel;
  private DesignationInterface designation;

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

  private void initComponents()
  {
   titleCaptionLabel= new JLabel("Designation");
   titleLabel= new JLabel("");
   titleTextField= new JTextField();
   add = new JButton("+");
   edit = new JButton("e");
   cancel = new JButton("x");
   delete = new JButton("-");
   exportToPDF = new JButton("p");
   clear = new JButton("C");
  }
  private void setAppearance()
  {
   titleCaptionLabel.setFont(new Font("Default",Font.PLAIN,20));
   titleLabel.setFont(new Font("Default",Font.PLAIN,20));
   titleTextField.setFont(new Font("Default",Font.PLAIN,20));
   setLayout(null);

   titleLabel.setBounds(10+120,10,320,50);
   titleTextField.setBounds(9+120,19,280,35);
   titleCaptionLabel.setBounds(12,10,120,50);
   add.setBounds(15,70,70,40);
   edit.setBounds(15+90,70,70,40);
   cancel.setBounds(15+180,70,70,40);
   delete.setBounds(15+270,70,70,40);
   exportToPDF.setBounds(15+360,70,70,40);
   clear.setBounds(14+360+40,19,35,35);

   add(titleCaptionLabel);
   add(titleLabel);
   add(add);
   add(edit);
   add(cancel);
   add(delete);
   add(exportToPDF);
   add(titleTextField);
   add(clear);
  }
  private void addListeners()
  {}

  DesignationPanel()
  {
   setBorder(BorderFactory.createLineBorder(new Color(165,165,165)));
   initComponents();
   setAppearance();
   addListeners();
  }
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

 private void initComponents()
 {
  designationModel=new DesignationModel();
  titleLabel= new JLabel("Designations");
  searchLabel=new JLabel("Search");
  searchTextField= new JTextField();
  clearSearchFieldButton= new JButton("X");
  searchErrorLabel=new JLabel("Not Found");
  designationTable= new JTable(designationModel);
  jScrollPane= new JScrollPane(designationTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  bottomPanel= new DesignationPanel();
  container=getContentPane();
 }
 private void setAppearance()
 {
  try
  {
   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  }
  catch(Exception ex)
  {
   ex.printStackTrace();
  }
  Font titleFont= new Font("",Font.PLAIN,35);
  Font captionFont= new Font("Default",Font.BOLD,20);
  Font dataFont= new Font("Default",Font.PLAIN,20);
  Font ColumnHeaderFont = new Font("Default",Font.PLAIN,15);

  titleLabel.setFont(titleFont);
  titleLabel.setForeground(new Color(17,130,132));
  searchLabel.setFont(captionFont);
  searchTextField.setFont(dataFont);
  designationTable.setFont(dataFont);
  designationTable.setRowHeight(30);
  designationTable.getColumnModel().getColumn(0).setPreferredWidth(100);
  designationTable.getColumnModel().getColumn(1).setPreferredWidth(900);
  designationTable.setRowSelectionAllowed(true);
  designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
  centerRenderer.setHorizontalAlignment( JLabel.CENTER );
  designationTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
  JTableHeader tableHeader=designationTable.getTableHeader();
  tableHeader.setFont(ColumnHeaderFont);
  tableHeader.setReorderingAllowed(false);
  tableHeader.setResizingAllowed(false);

  container.setLayout(null);
  titleLabel.setBounds(12,10,250,50);
  searchTextField.setBounds(12,10+50+10,325,35);
  clearSearchFieldButton.setBounds(12+325+5,10+50+10,35,35);
  jScrollPane.setBounds(12,10+50+10+35+20,460,300);
  bottomPanel.setBounds(12,10+50+10+35+20+300+20,460,125);

  container.add(titleLabel);
  container.add(searchTextField);
  container.add(clearSearchFieldButton);
  container.add(jScrollPane);
  container.add(bottomPanel);

  Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
  setSize(500,625);
  setLocation((d.width/2)-500/2,(d.height/2)-625/2);
  setIconImage(Toolkit.getDefaultToolkit().getImage("blank.png"));
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
   }
  });
  setDefaultCloseOperation(EXIT_ON_CLOSE);
 }
 public DesignationUI()
 {
  initComponents();
  setAppearance();
  addListeners();
 }
}