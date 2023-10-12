# Copyright 2022 Adam Steger
cpp = g++

flags = -std=c++17
flags += -Wall
flags += -g

link = $(cpp) $(flags) -o
compile = $(cpp) $(flags) -c

heap: heap.cpp
	$(link) $@ $^

heapsort: heapsort.cpp
	$(link) $@ $^
clean:
	$(RM) heap
	$(RM) output.txt
	$(RM) heapsort
