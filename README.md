# ![Vulnerable Client-Server Application (VuCSA)](http://vucsa.warxim.com/img/logo.png)
# Vulnerable Client-Server Application (VuCSA)
Vulnerable client-server application (VuCSA) is made for learning/presenting how to perform penetration tests of non-http thick clients.
It is written in Java (with JavaFX graphical user interface).

Currently the vulnerable application contains the following challenges:
1. Buffer Over-read (simulated)
2. Command Execution
3. SQL Injection
4. Enumeration
5. XML
6. Horizontal Access Control
7. Vertical Access Control

If you want to know how to solve these challenges, take a look at the [PETEP website](http://petep.warxim.com/methodology/), 
which describes how to use the open-source tool PETEP to exploit them.

**Tip:** Before you start hacking, do not forget to check the data structure of messages bellow.

## How to Run
In order to run the vulnerable server and client, you can use one of releases on GitHub
or run gradle assemble, which creates distribution packages (for both Windows and Unix).
These packages contain sh/bat scripts that will run the server and client using JVM.

## Project Structure
Project is divided into three modules:
- **vulnerable-common** - common functionality for both client and server (including protocol processing utilities)
- **vulnerable-client** - vulnerable client with JavaFX GUI
- **vulnerable-server** - vulnerable server for terminal use

## Data Structure
Messages transmitted between server and client have the following simple format:

    [type][target][length][payload]
      32b    32b     32b     ???

These four parts have the following meaning:
- **type** - type of the message (used for serialization/deserialization)
- **target** - target handler that will receive the message 
- **length** - length of the payload
- **payload** - data serialized into bytes
