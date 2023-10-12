// Copyright 2022 Adam Steger
#include <fstream>
#include <iostream>
#include <sstream>
#include <vector>
#include <chrono>

using namespace std;
using namespace std::chrono;


vector<vector<float>> floyd(vector<vector<float>> matrix, int n) {
    vector<vector<float>> d = matrix;  // Initialize return 2D vector
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            // Copy original matrix into return matrix
            d[i].push_back(matrix[i][j]);
        }
    }
    for (int k = 0; k < n; k++) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Find min of current value and potential new value
                d[i][j] = min(d[i][j], d[i][k] + d[k][j]);
            }
        }
    }
    return d;
}

void printMatrix(vector<vector<float>> matrix, int n) {
    ofstream out;
    out.open("output.txt");  // Create output file
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            out << matrix[i][j] << " ";  // Output each value to file
        }
        out << "\n";  // Move to next line
    }
    out.close();
}

int main(int argc, char *argv[]) {
    if (argc != 2) {  // Check to ensure file was run correctly
        return 1;
    }
    fstream in(argv[1]);
    vector<vector<float>> matrix;
    int n;
    if (in.is_open()) {
        string line;
        while (getline(in, line)) {
            stringstream stream(line);  // Create stringstream of current line
            vector<float> lineMatrix;
            float num;
            while (stream >> num) {
                lineMatrix.push_back(num);  // Add each value to vector
            }
            matrix.push_back(lineMatrix);  // Add line vector to 2D vector
            n = lineMatrix.size();  // Initialize size variable
        }
    }
    in.close();  // Close input stream
    vector<vector<float>> finalMatrix;
    // Start timer before algorithm is called
    auto start = high_resolution_clock::now();
    finalMatrix = floyd(matrix, n);
    // End timer directly after algorithm finishes
    auto finish = high_resolution_clock::now();
    // Calculate duration by subtracting start from finish
    auto time = duration_cast<microseconds>(finish - start);
    // Output execution time
    cout << time.count() << " microseconds" << "\n";
    printMatrix(finalMatrix, n);  // Output final matrix to file
}
