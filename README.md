# bkSQL

bkSQL will auto-backup your remote DataBases.

## Getting Started

This is a **NetBeans IDE 8.2** - **Java project**, for **Windows** users. <br />

bkSQL will create backup configurations for you to manually or automatically backup your remote DataBases. <br />
It uses [MySQLDump](https://dev.mysql.com/doc/refman/8.0/en/mysqldump.html), .bat files (converted to .exe, for security reasons), .xml, and more. <br />

You need to enter some info, like **Host**, **User**, **Password**, **DB** to backup, the **Path** to save backup files and **Loggedin User Password** (Optional).
You can easily configure a **Schedule** to auto-backup, setting up the time/hour and "repeat rate".

You can visualize and delete specific backup configuration or delete all.

### Contribute

If you want to contribute with bkSQL, you can do that by developing your own idea, or a task from the [Tasks](https://github.com/brhaka/bkSQL/projects/1) project. We suggest you to read the [CONTRIBUTING](CONTRIBUTING.md) file and the [CODE_OF_CONDUCT](CODE_OF_CONDUCT.md) file.

Thank you!

## Prerequisites

What things you need to install the software and how to install them

```
 NetBeans 8.2 IDE
```
https://netbeans.org/downloads/8.2/

```
 JDK 1.8
```
https://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html

```
 JRE 1.8^
```
https://www.oracle.com/technetwork/pt/java/javase/downloads/jre8-downloads-2133155.html

```
 Launch4j
```
http://launch4j.sourceforge.net/

```
 Inno Setup Compiler
```
http://www.jrsoftware.org/isdl.php

### Installing

How to open bkSQL as NetBeans project and code it.

Installing Java

```
Download JDK 1.8 and JRE 1.8^, than open the installer and follow the steps.
```

Installing NetBeans

```
Download NetBeans than install it by opening installer and following the steps.
```

NetBeans project

```
After installing JDK, JRE, NetBeans and after clone bkSQL, open up NetBeans, click on File -> Open Project -> (Go to the folder were bkSQL is located and click on it) -> Open Project.
```

### Compiling

How to compile bkSQL.

We recommend you to read the [LICENSE](LICENSE.md) before compiling.

Building .jar

```
Open bkSQL on NetBeans than click on "Clean and Build Project".
```

Building .exe

```
Open Launch4j and select the buildConf_bkSQL.xml file on /bin/. After that, click on "Build wrapper".
```

Building Installer / Setup

```
Open Inno Setup Compiler and select the bkSQL.iss file on /bin/. After that, click on "Compile".
```

## Error codes

If you're getting any errors, take a look at the [Error Codes](https://github.com/brhaka/bkSQL/wiki/Error-Codes) Wiki page for details

## Authors

* **Brhaka** - *Company* - [brhaka-Github](https://github.com/brhaka), [brhaka-WebSite](https://brhaka.com)
* **Eduardo Julião Martinez** - *Developer* - [WebSite](https://brhaka.com)

## License

This project is licensed under the **RECEX SHARED SOURCE LICENSE** (1.0) - see the [LICENSE](LICENSE.md) file for details

## Contact

E-Mail: support@brhaka.com <br />
WebSite: https://brhaka.com
