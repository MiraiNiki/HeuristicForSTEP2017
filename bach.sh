#!/bin/sh
read num
echo "0"
java heuristic$num input_0.csv solution_yours_0.csv
echo "1"
java heuristic$num input_1.csv solution_yours_1.csv
echo "2"
java heuristic$num input_2.csv solution_yours_2.csv
echo "3"
java heuristic$num input_3.csv solution_yours_3.csv
echo "4"
java heuristic$num input_4.csv solution_yours_4.csv
echo "5"
java heuristic$num input_5.csv solution_yours_5.csv
echo "6"
java heuristic$num input_6.csv solution_yours_6.csv
