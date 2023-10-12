// Copyright 2022 Adam Steger
#include <math.h>
#include <fstream>
#include <iostream>
#include <chrono>

using namespace std;
using namespace std::chrono;

void heapify(int *arr, int n, int i) {
    int k = i;  // Set key to i
    int v = arr[k];  // Set v to value of current parent
    bool heap = false;
    while (!heap && 2*k <= n) {  // Repeat until heap or end of array is reached
        int j = 2*k;  // Set j to index of first child
        if (j < n) {  // There are two children
            if (arr[j] < arr[j+1]) {
                j++;  // Set j to larger child
            }
        }
        if (v >= arr[j]) {
            heap = true;  // Parent/Child relationship is good
        } else {
            arr[k] = arr[j];  // Swap parent with larger children
            k = j;  // Keep checking new key
        }
    }
    arr[k] = v;
}

void heapBottomUp(int *arr, int n) {
    for (int i = floor(n/2); i >= 1; i--) {
        heapify(arr, n, i);  // Heapify for each parent in heap
    }
}

void printHeap(int *arr, int n) {
    ofstream out;
    out.open("output.txt");  // Create output file
    for (int i = 1; i <= n; i++) {
        out << arr[i] << " ";  // Print out each value of heap in order
    }
    out.close();  // Close stream to output file
}

int main(int argc, char *argv[]) {
    if (argc != 2) {  // Check to ensure file was run correctly
        return 1;
    }
    fstream in(argv[1]);
    if (in.is_open()) {
        string line;
        getline(in, line);
        int n = stoi(line);  // Set size to number in first line
        int arr[n+1];  // Allocate memory for array
        for (int i = 1; i <= n+1; i++) {
            getline(in, line, ' ');
            // Convert each string to integer and add to array
            arr[i] = stoi(line);
        }
        // Start timer before algorithm is called
        auto start = high_resolution_clock::now();
        heapBottomUp(arr, n);
        // End timer directly after algorithm finishes
        auto finish = high_resolution_clock::now();
        // Calculate duration by subtracting start from finish
        auto time = duration_cast<nanoseconds>(finish - start);
        // Output execution time
        cout << time.count() << " nanoseconds" << "\n";
        printHeap(arr, n);  // Output heap to file
    }
    in.close();  // Close input stream
}
