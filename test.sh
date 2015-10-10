#!/bin/bash

if [ "$#" -ne 2 ]; then
        echo "Usage <implementation> <runs>" >&2
        exit 1
fi
shortestTime=100000000000
longestTime=0
totalTime=0
regex=" ([0-9]+) "
files=input/*.prom
for file in $files
do
        for j in 1 4 16 64
        do
                echo "Processing $file on $j threads"
                for i in `seq 1 $2`
                do
                        result=$(bin/ndfs $file $1 $j)
                        [[ $result =~ $regex ]]
                        time=${BASH_REMATCH[1]}
                        echo "Run $i: $time ms"
                        totalTime=$(( $time+$totalTime))
                        if [ "$time" -gt "$longestTime" ]; then
                                longestTime=$time
                        fi
                        if [ "$time" -lt "$shortestTime" ]; then
                                shortestTime=$time
                        fi
                done
                averageTime=$(($totalTime/$2))
                echo "File: $file on $j threads in $1"
                echo "Average time $averageTime ms"
                echo "Shortest time: $shortestTime ms"
                echo "Longest time: $longestTime ms"
        done
done