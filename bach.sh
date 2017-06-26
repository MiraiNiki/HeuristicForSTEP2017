#!/bin/sh
read num
java heuristic1 ../google-step-tsp/input_$num.csv output
java calcDistance ../google-step-tsp/input_$num.csv output
