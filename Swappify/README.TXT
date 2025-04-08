==============================
How to Compile and Run Swappify
==============================

1. Make sure Java JDK 8 or higher is installed.
2. Navigate to the 'Swappify' folder using terminal/command prompt.

3. Compile the source files:
   Windows:
     javac -cp "lib/itextpdf-5.5.13.3.jar" -d build/classes src/swappify/*.java

   macOS/Linux:
     javac -cp "lib/itextpdf-5.5.13.3.jar" -d build/classes src/swappify/*.java

4. Run the program:
   Windows:
     java -cp "build/classes;lib/itextpdf-5.5.13.3.jar" swappify.SwappifyMain

   macOS/Linux:
     java -cp "build/classes:lib/itextpdf-5.5.13.3.jar" swappify.SwappifyMain
