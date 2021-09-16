cd D:\java\hr\bl\testcases
del *.class
javac -classpath D:\java\javatools\common\classes;..\build\libs\bl.jar;..\..\dl\dist\hr-dl-1.0.jar;..\..\common\classes;. *.java
pause