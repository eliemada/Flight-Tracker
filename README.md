# Flight Tracker: A Second Semester Java Application Project

Flight Tracker is a dynamic, robust, and user-friendly application built with Java. This application was developed as part of my second semester Computer Science coursework. It is designed to enable real-time tracking and management of flight data, providing an immersive experience for users interested in aviation logistics.

## Key Features

- **Real-Time Tracking:** Flight Tracker allows users to track flights worldwide in real time. It provides up-to-date information on flight status, duration, delays, and cancellations.

- **User-Friendly Interface:** Designed with simplicity and efficiency in mind, the application's GUI aids seamless navigation. This design choice makes it easy for users to find and track flights.

- **Data Management:** The app's backend is proficient in handling, managing, and updating vast amounts of flight data. This feature ensures accuracy and reliability in delivering flight information.

- **Search & Filter Functionality:** Flight Tracker offers the ability for users to search for flights based on various parameters. These include the flight number, departure airport, arrival airport, and airline. The app also includes a filter functionality to refine the search results further.

## Implementation Details

The Flight Tracker application was built adhering to the principles of Object-Oriented Programming (OOP). It demonstrates the practical use of key Java concepts such as inheritance, polymorphism, and encapsulation. In addition, it makes efficient use of Java libraries for networking and data management.

This project serves as a demonstration of how Java can be effectively used to build complex, real-world applications. Contributions, suggestions, and critiques are all welcome as I continue my journey in the world of Computer Science.

## Preview of the Application
<p align="center">
  <img src="src/playing.gif" alt="Demo of Flight Tracker Application">
</p>


## Application Modes

Flight Tracker operates in two distinct modes, providing flexibility depending on your setup and requirements:

1. **File Mode:** In this mode, the application decodes messages from a `.bin` file. This is an excellent option if you're working offline or wish to analyze previously collected flight data.

2. **Live Mode:** For real-time aviation enthusiasts, Flight Tracker can work in live mode, provided you have an Airspy device plugged in. In this mode, the application decodes messages in real-time, providing up-to-the-minute flight data.

## Altitude Color Coding

To make it easier to visualize and understand flight data, Flight Tracker employs a color-coding system based on the altitude of the aircraft. This allows for an intuitive understanding of the altitude level of different flights at a glance, enhancing user experience and interaction with the app.
<p align="center">
  <img src="src/ColorCodes.png" alt="Color Gradient in Function of the altitude">
</p>


# Prerequisites

Before getting started with the Flight Tracker application, there are some prerequisites that need to be met to ensure smooth execution and use of the program.

## Java

You must have Java 17 installed on your system. If you don't have it installed already, you can download and install it from Oracle's official website. After installation, verify your java version by opening your terminal and typing java -version. You should see Java 17 in your terminal if it is installed correctly.

Here are some guides to install Java 17 LTS and an IDE (Eclipse Or IntelliJ) : 
* [Linux :](https://d3c33hcgiwev3.cloudfront.net/fIAixXd9QN6AIsV3fXDeSw_5b727f0806b04c128160795cacf1cce1_install-java-linux.pdf?Expires=1685577600&Signature=aDlhEu~9RUfqmrbBdgTZmspKLYM602iVSAOHx6g0DbmWfeoBloBTy6rBK9LW0ajQ6unuB1mVLyAF1MI2zdrLrUNrggYC-7atXv6D2Th2N8-2GJ5Q27ZgqdU-DPeNvdGnRncMq-DOI9RMsdeXM8f7Mdy58vrnHi1TCeseXiUmC6U_&Key-Pair-Id=APKAJLTNE6QMUY6HBC5A) 

* [Windows :](https://d3c33hcgiwev3.cloudfront.net/xVWMiCVFRcuVjIglRVXL-A_88039540b1294592bb2f60abbe5665e1_install-java-windows.pdf?Expires=1685577600&Signature=ftfFuJ0RCK92nSs3eMbMYfRiHUrvSwlBCrJT9-HN5Bz6Lw~Cc0QCeDtM~evGzDv2N1sBur~AQkQur42ud1Kd9PyqrDQfyIsDhBxA~2abaX7NVX6JrnJZhcnm2GtCvyAmcK068KCqm-xWo9pQm-AuFeZs4qU1kBTydTXVIVmZknc_&Key-Pair-Id=APKAJLTNE6QMUY6HBC5A)

* [MacOS : ](https://d3c33hcgiwev3.cloudfront.net/UZxqHJxSSvWcahycUmr1wA_0c7d8b36406441f196671690e65ce5e1_install-java-macos.pdf?Expires=1685577600&Signature=IUzn0qI42eXAfOJvAK8uDtUGfrJehaBpK5sehyfI3tHx2cZJZPhFsk1Jx9A3hZJp0wfTneYWXVWl-o9rVEIUXmuOCsa3v2EWfIZBp-TZ5LiR2ow5CI2sNjb8VY1Y9diAsRDsL6FldZxA2TJUxeapXu6FgxbN4ziwEC6w4wPTXLg_&Key-Pair-Id=APKAJLTNE6QMUY6HBC5A)

## JavaFX

This application uses JavaFX for its GUI. To install JavaFX, follow the instructions on OpenJFX's official site. Please note that JavaFX needs to be correctly linked to your IDE to run the application.

Please ensure that you have both Java 17 and JavaFX correctly installed and configured before trying to run the Flight Tracker application. In case of any difficulties or issues, refer to the installation guides provided on the official websites linked above.

[Installation Guide : ](Prerequisite.md)

