#include<stdlib.h>
#include<iostream>
using namespace std;
int main()
{
 int x,y=1;

 while(y)
 {
  fflush(stdin);
  getchar();
  system("cls");
  cout<<"Select an option:"<<endl;
  cout<<"1.add employee"<<endl;
  cout<<"2.update employee"<<endl;
  cout<<"3.get employee by id"<<endl;
  cout<<"4.get employee by aadhar"<<endl;
  cout<<"5.get employee by pan"<<endl;  
  cout<<"6.remove employee"<<endl;
  cout<<"7.get all employees"<<endl;
  cout<<"8.get all employee by designations"<<endl;
  cout<<"9.exit"<<endl;

  cin>>x;

  switch(x)
  {
   case 1: system("java -classpath ..\\..\\..\\mysql\\mysql.jar;..\\..\\build\\libs\\bl.jar;..\\..\\..\\dbdl\\build\\libs\\dbdl-1.0.jar;..\\..\\..\\common\\dist\\common.jar;. add"); continue;

   case 2: system("java -classpath ..\\..\\..\\mysql\\mysql.jar;..\\..\\build\\libs\\bl.jar;..\\..\\..\\dbdl\\build\\libs\\dbdl-1.0.jar;..\\..\\..\\common\\dist\\common.jar;. update"); continue;

   case 3: system("java -classpath ..\\..\\..\\mysql\\mysql.jar;..\\..\\build\\libs\\bl.jar;..\\..\\..\\dbdl\\build\\libs\\dbdl-1.0.jar;..\\..\\..\\common\\dist\\common.jar;. getbyid"); continue;

   case 4: system("java -classpath ..\\..\\..\\mysql\\mysql.jar;..\\..\\build\\libs\\bl.jar;..\\..\\..\\dbdl\\build\\libs\\dbdl-1.0.jar;..\\..\\..\\common\\dist\\common.jar;. getbyad"); continue;

   case 5: system("java -classpath ..\\..\\..\\mysql\\mysql.jar;..\\..\\build\\libs\\bl.jar;..\\..\\..\\dbdl\\build\\libs\\dbdl-1.0.jar;..\\..\\..\\common\\dist\\common.jar;. getbypan"); continue;

   case 6: system("java -classpath ..\\..\\..\\mysql\\mysql.jar;..\\..\\build\\libs\\bl.jar;..\\..\\..\\dbdl\\build\\libs\\dbdl-1.0.jar;..\\..\\..\\common\\dist\\common.jar;. delete"); continue;

   case 7: system("java -classpath ..\\..\\..\\mysql\\mysql.jar;..\\..\\build\\libs\\bl.jar;..\\..\\..\\dbdl\\build\\libs\\dbdl-1.0.jar;..\\..\\..\\common\\dist\\common.jar;. getall");continue;

   case 8: system("java -classpath ..\\..\\..\\mysql\\mysql.jar;..\\..\\build\\libs\\bl.jar;..\\..\\..\\dbdl\\build\\libs\\dbdl-1.0.jar;..\\..\\..\\common\\dist\\common.jar;. getallbydesignation");continue;

   case 9: y=0;continue;
   default:cout<<"Enter a valid response"<<endl; continue;
  }
 }
 return 0;
}