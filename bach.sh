#!/bin/sh
read num
java heuristic2 input_$num.csv solution_yours_$num.csv
java calcDistance input_$num.csv solution_yours_$num.csv
