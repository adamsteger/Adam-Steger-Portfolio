# Copyright 2022 Adam Steger
cpp = g++

flags = -std=c++17
flags += -Wall
flags += -g

link = $(cpp) $(flags) -o
compile = $(cpp) $(flags) -c

quicksort: quicksort.cpp
	$(link) $@ $^

quicksort2: quicksort2.cpp
	$(link) $@ $^
clean:
	$(RM) quicksort
	$(RM) output.txt
	$(RM) quicksort2