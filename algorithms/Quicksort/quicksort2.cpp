// Copyright 2022 Adam Steger
#include <fstream>
#include <iostream>
#include <vector>
#include <chrono>
#include <cstdlib>

using namespace std;
using namespace std::chrono;


int partition(vector<float> &v, int start, int end) {
    int pivot = start;
    int j = end;
    int i = start;
    while (i <= j) {
        while (v[i] < v[pivot]) {
            i++;
        }
        while (v[j] > v[pivot]) {
            j--;
        }
        if (i <= j) {
            swap(v[i], v[j]);
            i++;
            j--;
        }
    }
    return i;
}

void quicksort(vector<float> &v, int start, int end) {
    if (start < end) {
        int s = partition(v, start, end);
        quicksort(v, start, s-1);
        quicksort(v, s, end);
    }
}

void printVector(vector<float> &v) {
    ofstream out;
    out.open("output.txt");
    for (float i : v) {
        out << i << " ";
    }
}

vector<float> generateRandomList(int size) {
    vector<float> v;
    for (int i = 0; i < size; i++) {
        float x = rand() % 10000 + 1;  // Generate random number between 1 and 10000
        v.push_back(x);
    }
    return v;
}

double findAverageTime(int size) {
    vector<double> averageTime;  // Declare a vector of average times so each iteration can be recorded
    for (int i = 0; i < 100; i++) {  // Repeat for 100 inputs, adding time each iteration
        vector<float> v = generateRandomList(size);  // For each iteration, get a random float vector with a given size
        auto start = high_resolution_clock::now();
        quicksort(v, 0, v.size()-1);  // Sort random vector
        auto finish = high_resolution_clock::now();
        auto time = duration_cast<microseconds>(finish - start).count();
        averageTime.push_back(time);  // Add duration to vector
    }
    double totalTime;
    for (int i = 0; i < averageTime.size(); i++) {
        totalTime += averageTime[i];  // Add each time in vector to find total time
    }
    totalTime = totalTime/100; // Divide total time by number of iterations to find average duration
    return totalTime;
}

int main(int argc, char *argv[]) {
    double t1 = findAverageTime(10);
    double t2 = findAverageTime(100);
    double t3 = findAverageTime(1000);
    double t4 = findAverageTime(10000);
    double t5 = findAverageTime(100000);
    cout << "Average Execution time: " << t1 << " microseconds" << "\n";
    cout << "Average Execution time: " << t2 << " microseconds" << "\n";
    cout << "Average Execution time: " << t3 << " microseconds" << "\n";
    cout << "Average Execution time: " << t4 << " microseconds" << "\n";
    cout << "Average Execution time: " << t5 << " microseconds" << "\n";
}
