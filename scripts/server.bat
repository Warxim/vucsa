@echo off
set JAVA_EXE=java.exe
set JAVAW_EXE=javaw.exe
set DIRNAME=%~dp0
set APP_HOME=%DIRNAME%
set CMD_LINE_ARGS=%*
set DEFAULT_JVM_OPTS=
set CLASSPATH=%APP_HOME%\lib\*
set MAIN_CLASS="com.warxim.vucsa.server.Main"

rem Start Vulnerable Server without GUI (let console open).
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% -classpath "%CLASSPATH%" %MAIN_CLASS% %CMD_LINE_ARGS%
