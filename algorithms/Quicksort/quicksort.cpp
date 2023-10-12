// Copyright 2022 Adam Steger
#include <fstream>
#include <iostream>
#include <vector>
#include <chrono>

using namespace std;
using namespace std::chrono;


int partition(vector<float> &v, int start, int end) {
    int pivot = start;  // set pivot to start
    int j = end;
    int i = start;
    while (i <= j) {
        while (v[i] < v[pivot]) {  // advance i while v[i] is smaller than the pivot
            i++;
        }
        while (v[j] > v[pivot]) {  // advance j while v[j] is bigger than the pivot
            j--;
        }
        if (i <= j) {
            swap(v[i], v[j]);  // swap i and j when they havent passed each other
            i++;
            j--;
        }
    }
    return i;  // return pivot position
}

void quicksort(vector<float> &v, int start, int end) {
    if (start < end) {
        int s = partition(v, start, end);
        quicksort(v, start, s-1);  // Apply quicksort to left side of pivot
        quicksort(v, s, end);  // Apply quicksort to right side of pivot
    }
}

void printVector(vector<float> &v) {
    ofstream out;
    out.open("output.txt");
    for (float i : v) {
        out << i << " ";  // output sorted vector to file
    }
    out.close();
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        return 1;
    }
    fstream in(argv[1]);
    vector<float> v;
    if (in.is_open()) {
        string line;
        while (getline(in, line, ' ')) {
            float num = stof(line);  // Convert string to float
            v.push_back(num);  // Add read float to vector
        }
    }
    in.close();
    auto start = high_resolution_clock::now(); // start timer before algorithm is called
    quicksort(v, 0, v.size()-1);
    auto finish = high_resolution_clock::now(); // end timer directly after algorithm finishes
    auto time = duration_cast<microseconds>(finish - start); // calculate duration by subtracting start from finish
    cout << "Execution time: " << time.count() << " microseconds" << "\n";
    printVector(v);  // Output vector to file
}
