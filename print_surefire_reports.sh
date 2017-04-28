#!/usr/bin/env sh
echo "Current directory is $(pwd)"
echo "\n=== SUREFIRE REPORTS ===\n"
for F in build/test-results/test/*.xml
do
    echo $F:
    cat $F
    echo
done
for F in build/integrationTest-results/*.xml
do
    echo $F:
    cat $F
    echo
done