# Copyright 2022 Adam Steger
cpp = g++

flags = -std=c++17
flags += -Wall
flags += -g

link = $(cpp) $(flags) -o
compile = $(cpp) $(flags) -c

floyd: floyd.cpp
	$(link) $@ $^

clean:
	$(RM) floyd
	$(RM) output.txt
