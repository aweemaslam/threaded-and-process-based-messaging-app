#!/bin/bash
# runPlayerMessaging.sh
# Usage:
#   ./runPlayerMessaging.sh single     -> Run single-process mode
#   ./runPlayerMessaging.sh multi      -> Run multi-process mode
#   ./runPlayerMessaging.sh            -> Default: single-process mode

# Exit immediately on errors
set -e

# Optional: set JAVA_HOME if needed
# export JAVA_HOME=/path/to/jdk
# export PATH=$JAVA_HOME/bin:$PATH

MODE=${1:-single}

echo "Cleaning, compiling, and running tests..."
mvn clean compile test

if [ "$MODE" == "single" ]; then
    echo "Running in single-process mode..."
    mvn exec:java -Dexec.mainClass="org.messaging.Main"
elif [ "$MODE" == "multi" ]; then
    echo "Running in multi-process mode..."
    mvn exec:java -Dexec.mainClass="org.messaging.Main" -Dexec.args="multi"
else
    echo "Unknown mode: $MODE"
    echo "Usage: $0 [single|multi]"
    exit 1
fi
chmod +x run.sh