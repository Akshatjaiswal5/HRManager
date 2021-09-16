cd D:\java\hr\dl\src
javac -classpath D:\java\hr\common\classes;. -d ..\classes com\thinking\machines\hr\dl\dto\*.java
javac -classpath D:\java\hr\common\classes;. -d ..\classes com\thinking\machines\hr\dl\dao\*.java
javac -classpath D:\java\hr\common\classes;. -d ..\classes com\thinking\machines\hr\dl\interfaces\dto\*.java
javac -classpath D:\java\hr\common\classes;. -d ..\classes com\thinking\machines\hr\dl\interfaces\dao\*.java
cd D:\java\hr\dl\testcases
javac -classpath D:\java\hr\dl\classes;D:\java\javatools\common\classes;D:\java\hr\common\classes;. *.java
pause