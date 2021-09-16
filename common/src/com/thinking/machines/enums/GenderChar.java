package com.thinking.machines.enums;
public class GenderChar 
{
 public static char to(GENDER gender)
 {
  if(gender==GENDER.MALE)
  return 'M';
  else
  return 'F';
 }

 public static GENDER from(char ch)throws GenderException
 {
  if(ch=='m'||ch=='M')
  return GENDER.MALE;
  else if(ch=='f'||ch=='F')
  return GENDER.FEMALE;
  else
  throw new GenderException("Invalid character: "+ch);
 }

}