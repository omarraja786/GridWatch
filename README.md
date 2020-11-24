--------------
|  Gridwatch  |
--------------
An application that was created in Java that could load a CSV file and manipulate the data into various graphical forms.

------------------------------
What Platform/OS To uase?
------------------------------
The program works best on Windows 10 64 bit but is able to function on other operating Systems such as Windows 8/8.1/7 PROVIDING YOU HAVE **JAVA 8** INSTALLED, no other java versions will work.

---------------------------------------------------
Instructions on how to compile and run the source:
---------------------------------------------------

1: Open IntelliJ and copy all the files/source folders to a project folder (You can create your own)
2: If you wish to test the program through intelliJ then open the MainFrame class file and run it. This is the main file
3: If you wish to compile the source into a JAR file; File -> Project Structure -> Project Settings -> Artifacts -> Click green plus sign -> Jar -> From modules with dependencies... and select the main class as MainFrame
4: Click yes to all option boxes that open, then click Apply and Ok
5: Click Build (in the top menu) -> Build Artifacts -> Build
6: The file will be in the same project folder as all the files -> out -> Artifacts -> jarFolder -> fileName.jar

**The ClassPath will be the main folder of the project you put it within as long as you keep the internal folders the same such as src**

-----------------------------------------------------------
**IMPORTANT INFORMATION IF YOU ARE RUNNING FROM INTELLIJ**
-----------------------------------------------------------

The program uses external libraries for the PDF export so if you are running from IntelliJ and not the compiled JAR file; do the following:
1: Right click on the "lib" folder in the project menu on the left
2: Click on Add as Library; leave the settings as default and press Ok to all. This will allow the external libraries to be used whilst compiling to prevent any errors.

**The CSV file has to be one directory below the src (So the folder before opening the src folder) if using IntelliJ to run, but has to be in the same directory if using a compiled jar file**
------------------------------
How to Run the Java File
------------------------------
1: Open up CMD as admin
2: Change to the directory of the jar file (Make sure the CSV file is in the same directory as the jar file)
3: Type in java -jar filename.jar
