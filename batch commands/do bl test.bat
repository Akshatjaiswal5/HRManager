cd ..\bl\testcases\employee
del *.class
javac -classpath ..\..\build\libs\bl.jar;..\..\..\dl\dist\hr-dl-1.0.jar;..\..\..\common\dist\common.jar;. *.java
pause