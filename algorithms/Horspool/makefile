# Copyright 2022 Adam Steger
cpp = g++

flags = -std=c++17
flags += -Wall
flags += -g

link = $(cpp) $(flags) -o
compile = $(cpp) $(flags) -c

horspool: horspool.cpp
	$(link) $@ $^

boyer: boyer.cpp
	$(link) $@ $^
clean:
	$(RM) horspool
	$(RM) output.txt
	$(RM) boyer
