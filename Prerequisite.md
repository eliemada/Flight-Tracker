# Using JavaFX in IntelliJ

Subtitle: CS-108

## Introduction

JavaFX is a Java library for creating user interfaces. Initially, JavaFX was part of the standard Java library but later became a separate project. Therefore, it's necessary to install JavaFX before being able to use it in a project. This document describes the steps to follow.

## JavaFX Installation

To install JavaFX, you must first download the Zip archive containing the version corresponding to your operating system (Windows, macOS, or Linux). To do this, go to [the OpenJFX download page](https://gluonhq.com/products/javafx/) and choose from the drop-down menus:

- version 20,
- the operating system you are using,
- if you know it, your computer's architecture,
- the SDK type.

Once the archive has been downloaded, unzip it into a folder of your choice. If you already have a folder containing all the projects related to this course (exercise series, main project, etc.), it is recommended to also place JavaFX there to keep things organized.

The unzipping of the archive should create a folder named `javafx-sdk-20` containing at least two subfolders named `legal` and `lib`. On Windows, a `bin` subfolder also exists.

## IntelliJ Configuration

Once the archive has been unzipped, IntelliJ must be configured to recognize this library, and then the library should be added to all projects that use it.

### Definition of a path variable

To simplify the following operations, it is good to start by defining what IntelliJ calls a "path variable". As its name suggests, such a variable contains a path to a file or folder, and once defined, it can be used in several places within IntelliJ.

Our goal is to define such a variable named `JFX_PATH` and containing the path to the `lib` subfolder obtained after unzipping the OpenJFX archive. To do this, open the IntelliJ settings, then in the left-hand part, unroll "Appearance & Behavior" and select "Path Variables". Next, click on the `+` button and in the "Add Variable" dialog box that opens, enter:

- `JFX_PATH` as variable name,
- the path to the `lib` subfolder obtained after unzipping the archive as value — the easiest way to get it is to click on the icon representing a folder and then navigate to the `lib` subfolder.

### Addition of the library

To use JavaFX in projects from IntelliJ, it must be added in the form of a global library in the following way:

1. In the File menu, choose New Project Setup then Structure…,
2. In the left-hand part, click on Global Libraries in the Platform Settings section,
3. In the middle part, click on the `+` button at the top, then choose Java in the New Global Library menu that opens,
4. Navigate to the `lib` folder created when unzipping the archive and select all the files it contains (this is very important),
5. Click Open (or equivalent),
6. Change the name of the library thus created to `OpenJFX 20`, by modifying the field to the right of the Name: label,
7. Click on the `+` button under the Name: label,
8. Select the `src.zip` file located in the parent folder of the `lib` folder,
9. Click Open (or equivalent),
10. Click OK in the dialog box that then opens.

![intellij-openjfx;64.png](https://cs108.epfl.ch/g/i/intellij-openjfx;64.png)

You can then close the window by clicking on *OK*,

### Using JavaFX in a project

Once the library is added to IntelliJ, it can be used in any project as follows:

1. In the **File** menu, choose **Project Structure...**
2. On the left side, click on **Modules** in the **Project Settings** section.
3. On the right side, select the **Dependencies** tab.
4. Still on the right side, click the `+` button, then choose the **Library…** entry from the opening menu.
5. In the window that opens, choose **OpenJFX 20**, then click on **Add Selected**.

Once these operations have been performed, the window should look like this:

![intellij-openjfx-proj;64.png](https://cs108.epfl.ch/g/i/intellij-openjfx-proj;64.png)

 You can then close the window by clicking OK, and JavaFX should be usable in your project. To check this, you can temporarily add the following program: 
```java
import javafx.scene.paint.Color;

public final class CheckJavaFx {
  public static void main(String[] args) {
    Color c = Color.RED;
    System.out.println(c.getRed());
  }
} 
```

If you are able to execute it and it displays `1.0`, this means that JavaFX is correctly installed.

### Problems

It is possible that when running some JavaFX programs, the following error may appear at startup:

>Error: JavaFX runtime components needed to run this application are missing.

To resolve this, you must perform the following steps:

1. In the **Run** menu, choose **Edit Configurations...**
2. Click on **Modify options** then choose **Add VM options** to make the **VM options** field appear.
3. In the field titled **VM options**, add the following line - the notation `$JFX_PATH$` allows you to insert the content of the previously created `JFX_PATH` variable:
```--module-path $JFX_PATH$ --add-modules javafx.controls```
