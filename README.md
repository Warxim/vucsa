# ![Vulnerable Client-Server Application (VuCSA)](http://vucsa.warxim.com/img/logo.png)
# Vulnerable Client-Server Application (VuCSA)
Vulnerable client-server application (VuCSA) is made for learning/presenting
how to perform penetration tests of non-http client-server applications.
It is written in Java (with JavaFX graphical user interface).

Currently, the vulnerable application contains the following challenges:

1. **Buffer Over-read (simulated)**
2. **Command Execution**
3. **SQL Injection**
4. **Enumeration**
5. **XML**
6. **Horizontal Access Control**
7. **Vertical Access Control**
8. **RCE Deserialization**

If you want to know how to solve these challenges, take a look at the [PETEP website](https://petep.warxim.com/methodology/), 
which describes how to use the open-source tool PETEP to exploit them.

**Tip 1:** Before you start hacking, do not forget to check the data structure of messages bellow. 
When modifying the network traffic, you will probably have to consider the structure, 
especially payload length bytes.

**Tip 2:** Most of the challenges can be exploited through modification of network traffic.
Therefore, it is recommended to use TCP proxy or process hooks for the testing.

**Tip 3:** Some challenges have input validation and restrictions in place, which is common in thick clients, 
but it does not mean that the server uses the same validation.

### Buffer Over-read (simulated) Vulnerability
In this challenge, your goal is to manipulate the network traffic between the client and the server in a way
that leads to buffer over-read.

### Command Execution Vulnerability
Command execution challenge represents a very simple command execution vulnerability.
The goal is to execute malicious command on the server. 

### SQL Injection Vulnerability
SQL Injection challenge contains a search input vulnerable to SQL injection, 
but as you will soon notice, the input does not allow you to input the characters you need.

### Enumeration Vulnerability
Enumeration challenge is based on simulated login form that is not protected from enumeration.
Will you be able to find all 5 users and guess their passwords?

### XML Vulnerabilities
In this challenge, you can find multiple XML vulnerabilities:
- XML External Entity Attack (XXE Injection)
- XInclude Attack
- XML Bomb Attack

### Horizontal Access Control Vulnerability
Horizontal Access Control challenge represents document reader that allows the user to see 
own documents and read their content. 
The goal is to find 5 documents of other users.

### Vertical Access Control Vulnerability
Vertical Access Control challenge is based on simulated user panel, which shows basic user 
information. The goal is to find a hidden admin functionality and check if it is possible 
to use it as a Guest user.

### RCE Deserialization Vulnerability
RCE Deserialization vulnerability uses Java deserialization/serialization for transmitting data through the network.
The application contains two paths that you can use to achieve remote code execution through 
the vulnerable Java deserialization. 

You can find both paths by examining the server's JAR file 
or by looking into the [source code](vucsa-server/src/main/java/com/warxim/vucsa/server/challenge/rcedeserialization).

The goal is to create exploits for both paths and execute malicious command on the server.

**Tip:** You can use the server JAR as library to make the exploit creation easier.

## How to Run
In order to run the vulnerable server and client, you can use one of releases on GitHub
or run gradle assemble, which creates distribution packages (for both Windows and Unix).
These packages contain sh/bat scripts that will run the server and client using JVM. 

You need Java 11 or newer version to run VuCSA.

***Note:** For Mac with ARM64 architecture (M1, M2 chips), use special build for Java 17.*

## Project Structure
Project is divided into three modules:
- **vucsa-common** - common functionality for both client and server (including protocol processing utilities)
- **vucsa-client** - vulnerable client with JavaFX GUI
- **vucsa-server** - vulnerable server for terminal use

## Data Structure
Messages transmitted between server and client have the following simple format:

    [type][target][length][payload]
      32b    32b     32b     ???

These four parts have the following meaning:
- **type** - type of the message (used for serialization/deserialization)
- **target** - target handler that will receive the message (identifier)
- **length** - length of the payload
- **payload** - data serialized into bytes

In order to send custom payloads, you might have to update the payload length. 
Otherwise, it will not work properly. In the [tutorial](https://petep.warxim.com/methodology/analysis/), 
automatic script is developed to auto-fix the payload length bytes.

# Tutorial (Solutions)
Vulnerable client-server application (VuCSA) contains multiple vulnerabilities,
which can be exploited in various ways. Official guide for exploiting these vulnerabilities
uses open-source PEnetration TEsting Proxy (see [PETEP Methodology](https://petep.warxim.com/methodology/)).

In the PETEP methodology, the whole process of exploiting the challenges is explained,
including useful payloads.
