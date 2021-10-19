# HRManager
A hr management app, I built using layered programming approach. It can be used by hr managers to keep track of employees and store their information using
a GUI made by Java Swing Library. This project took a long time because I experimented and learnt many effective coding practices using Java.

## Layered programming
This application is made in three layers. I built all the layers but in practice these layers can be made by different teams and each layer is independent of each other.
One layer can be changed without affecting other ones and thus multiple teams can coordinate on a single big project.

1. **Data layer**
I made this layer two times, first version "dl" was made using Java File handling. I read and write all the information to a simple ASCII file.
this way I could customize how my data was stored in n number of ways. Second time I made datalayer "dbdl" using MySql a SQL database and by using
Java database connectivity (JDBC).

1. **Business layer**
I made this layer by utilizing Java Collection Classes. The benefit of having a business layer instead of our Gui directly accessing database is to provide faster
read speeds (when the app scales) as well as proper code management. All the data is loaded into memory beforehand in business layer and I have used Map container 
to optimize read speeds.

1. **Presentation layer**
I made this layer again in two ways, First in the testing phase using CLI, and finally using a GUI using Java Swing. It is based on Model-View-Controller
paradigm where I have separated the model from the actual UI. I have also implemented a "Print to PDF" button using itextpdf library. which can generate a 
report in pdf form.

## Things I learnt

1. **Readable code** is foremost important. I have used lengthy variable names and proper readable code structures with comments.
1. **Object oriented code** I have first created Interfaces for all the classes I've created and wrote data transfer objects that are used for interaction between layers.
1. **Exception handling** the data layer throws DAOException and the business layer throws BLException which are handled well for a bug free program.
1. **Swing Library** I have spent much time on making presentation layer look pretty and I have a gist for good looking GUI.
1. **Gradle** I have used only plain text editors, command line interface, and gradle to build this application.