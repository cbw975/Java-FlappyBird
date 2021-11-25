# AngryFlappyBird
The (variant) game Angry Flappy Bird built using JavaFX 11 and Java 11 platforms.

### Screenshot
![Angry Flappy Bird](gameScreenshot.PNG?raw=true)

### Requirements & Setup
You will need Java 11 (or newer) and JavaFX 11 installed to build and run the project.

Java 11 (or newer)

- Go to https://adoptopenjdk.net/
- Choose a version equal to or higher than 11 and continue with installation

JavaFX 11

- Go to https://gluonhq.com/products/javafx/
- Download the version of JavaFX SDK compatible with your Java version
- Unzip the folder in a location for later use
- Configure your chosen IDE (i.e. VS Code, Eclipse, IntelliJ, etc.)

#### *Example* Eclipse setup:
- Download Eclipse installer (https://www.eclipse.org/downloads/)
- Installation: choose 'Eclipse IDE for Java Developers' and the appropriate Java 11+ VM and Install

Configuring Eclipse
- Create a workspace directory
    - MAC users: Click on Eclipse > Preferences
    - Window users: Click on Window > Preferences
- Go to: Java > Installed JREs > Choose the one you installed.
- Go to: Execution Environment > JavaSE-11 & Check on compatible JRE (or higher, whichever you installed)
- Go to Java > Compiler > Set the Compiler Compliance Level to 11 (or whichever you installed)
- Apply & Close

Adding JavaFX to Eclipse
- Go to Preferences > Java > BuildPath > User Libraries
- New > Name “JavaFX11” > Select it and “Add External Jars” > Browse to the location where JavaFX sdk (slide 5)
was downloaded and select all .jar files.
    - **NOTE** for Mac users: Navigate to the folder where JavaFX was unzipped. Go to lib, for *each .dylib file*, 
Right-click > Open-with > Terminal > Open and then, close the terminal

For (each) Java project created:
- New > Create Java Project > ... > Navigate to Libraries > Add Library > User
Library > *JavaFX11* ( library created in slide 8 )
- With the project selected, Run > Run Configurations > Java Application > Select “New launch configuration” >
    - Name : Same as java file containing main method
    - Main Class : Same as java file containing main method ( without .java)
- Go to (x) = Arguments > Under VM Arguments > **Paste this:** `--module-path <path-to-javafx-sdk>/lib --add-modules=javafx.controls,javafx.fxml,javafx.media`
