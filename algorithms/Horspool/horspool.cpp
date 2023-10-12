// Copyright 2022 Adam Steger
#include <fstream>
#include <iostream>
#include <chrono>

using namespace std;
using namespace std::chrono;

int* shiftTable(string pattern) {
    static int table[27];  // Allocate memory for array
    int m = pattern.length();
    for (int i = 0; i < 27; i++) {
        table[i] = m;  // Initialize all values to length of pattern
    }
    for (int i = 0; i <= m - 2; i++) {
        // Convert character to ascii int
        int index = static_cast<int>(pattern[i]);
        if (index < 95) {  // Check if lowercase
            index -= 65;
        } else if (index > 95) {  // Check if uppercase
            index -= 96;
        } else {
            index -= 95;  // Must be space
        }
        table[index] = m - 1 - i;  // Set value of character in table
    }
    return table;
}

int horspool(string pattern, string text, int m) {
    int *table = shiftTable(pattern);  // Create shift table
    int i = m - 1;  // i represents current index of text
    int n = text.length();
    while (i <= n - 1) {  // Iterate until end of text is reached
        int k = 0;  // k represents number of matched characters
        while (k <= m-1 && pattern[m-1-k] == text[i-k]) {
            k++;  // Increase k if match found
        }
        if (k == m) {  // Check if entire pattern is matched
            return i - m + 1;  // Return index of first match
        } else {
            // Convert character to ascii int
            int index = static_cast<int>(text[i]);
            if (index < 95) {  // Check if uppercase
                index -= 65;
            } else if (index > 95) {  // Check if lowercase
                index -= 96;
            } else {
                index -= 95;  // Must be space
            }
            i = i + table[index];  // Shift by value in shift table
        }
    }
    return -1;  // Return -1 if no match found
}


int main(int argc, char *argv[]) {
    int output = -1;
    if (argc != 2) {  // Check to ensure file was run correctly
        return 1;
    }
    fstream in(argv[1]);
    if (in.is_open()) {
        string line;
        getline(in, line);
        string pattern = line;
        getline(in, line);
        string text = line;
        int m = pattern.length();
        // Start timer before algorithm is called
        auto start = high_resolution_clock::now();
        output = horspool(pattern, text, m);
        // End timer directly after algorithm finishes
        auto finish = high_resolution_clock::now();
        // Calculate duration by subtracting start from finish
        auto time = duration_cast<nanoseconds>(finish - start);
        // Output execution time
        cout << time.count() << " nanoseconds" << "\n";
    }
    in.close();  // Close input stream

    ofstream out;
    out.open("output.txt");  // Create output file
    out << output;
    out.close();  // Close stream to output file
}