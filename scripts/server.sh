#!/bin/bash

JAVA="java"
APP_HOME="`pwd`"
DEFAULT_JVM_OPTS=
CMD_LINE_ARGS=$@
CLASSPATH=$APP_HOME/lib/*
MAIN_CLASS="com.warxim.vucsa.server.Main"
LOG_FILE=server.log

$JAVA -cp "$CLASSPATH" $MAIN_CLASS $CMD_LINE_ARGS
