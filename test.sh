#!/bin/bash

if [ "$#" -ne 4 ]; then
        echo "Usage <inputfile> <implementation> <threads> <runs>" >&2
        exit 1
fi
shortestTime=100000000000
longestTime=0
totalTime=0
for i in `seq 1 $4`
do
        result=$(bin/ndfs $1 $2 $3)
        [[ $result =~ ([0-9]{2,}) ]]
        time=${BASH_REMATCH[1]}
        echo "Run $i: $time  ms"
        totalTime=$(( $time+$totalTime))
        if [ "$time" -gt "$longestTime" ]; then
                longestTime=$time
        fi
        if [ "$time" -lt "$shortestTime" ]; then
                shortestTime=$time
        fi
done
averageTime=$(($totalTime/$4))
echo "Average time $averageTime ms"
echo "Shortest time: $shortestTime ms"
echo "Longest time: $longestTime ms"
