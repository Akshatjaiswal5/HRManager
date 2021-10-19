package com.thinking.machines.hr.pl.components;
import java.util.*;
import java.time.*;
public class DatePicker
{
 public int[][] getDays(int month,int year)
 {
  Calendar firstDayOfMonth= Calendar.getInstance();
  firstDayOfMonth.setTime(new Date(year-1900,month-1,1));

  int firstWeekDayOfMonth=firstDayOfMonth.get(Calendar.DAY_OF_WEEK);
  YearMonth yearMonth= YearMonth.of(year,month);
  int numberOfDays= yearMonth.lengthOfMonth();

  Calendar lastDayOfMonth= Calendar.getInstance();
  lastDayOfMonth.setTime(new Date(year-1900,month-1,numberOfDays));

  int lastWeekDayOfMonth=lastDayOfMonth.get(Calendar.DAY_OF_WEEK);
  int weekNumber= lastDayOfMonth.get(Calendar.WEEK_OF_MONTH);

  int days[][]= new int[weekNumber][7];

  int c=firstWeekDayOfMonth-1;
  int r=0;
  for (int i = 1; i <=numberOfDays; i++) 
  {
   days[r][c]=i;
   c++;
   if(c==7)
   {
    c=0;
    r++;
   }
  }
  return days;
 }
 public static void main(String args[])
 {
  int month= Integer.parseInt(args[0]);
  int year= Integer.parseInt(args[1]);
  DatePicker dp= new DatePicker();
  int[][] days= dp.getDays(month,year);

  for (int i = 0; i < days.length; i++) {
   for (int j = 0; j < days[i].length; j++) {
    System.out.printf("%2d ",days[i][j]);
   }
   System.out.printf("\n");
  }
 }
}