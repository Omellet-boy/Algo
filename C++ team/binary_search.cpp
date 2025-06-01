#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <string>
#include <chrono>
#include <random>
#include <algorithm>

struct Data {
    int number;
    std::string text;
};

// Binary Search without logging
int binarySearch(const std::vector<Data>& data, int target) {
    int low = 0, high = data.size() - 1;

    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (data[mid].number == target)
            return mid;
        else if (data[mid].number < target)
            low = mid + 1;
        else
            high = mid - 1;
    }
    return -1;
}

int main() {
    std::string filename;
    std::cout << "Enter dataset filename (e.g., merge_sort_n.csv): ";
    std::cin >> filename;

    std::ifstream inFile(filename);
    if (!inFile) {
        std::cerr << "Error opening file.\n";
        return 1;
    }

    std::vector<Data> data;
    std::string line;
    while (std::getline(inFile, line)) {
        std::stringstream ss(line);
        std::string numStr, txt;
        if (std::getline(ss, numStr, ',') && std::getline(ss, txt)) {
            data.push_back({std::stoi(numStr), txt});
        }
    }
    inFile.close();

    size_t n = data.size();
    std::ofstream outFile("binary_search_n.txt");

    // Best case: search for middle element
    int midIndex = n / 2;
    int bestCaseTarget = data[midIndex].number;
    auto start = std::chrono::high_resolution_clock::now();
    binarySearch(data, bestCaseTarget);
    auto end = std::chrono::high_resolution_clock::now();
    double bestTime = std::chrono::duration<double, std::micro>(end - start).count();

    // Average case: search for n/2 random targets
    std::mt19937 rng(std::random_device{}());
    std::uniform_int_distribution<size_t> dist(0, n - 1);

    double totalAvgTime = 0;
    int trials = n / 2;
    for (int i = 0; i < trials; ++i) {
        int target = data[dist(rng)].number;
        auto startAvg = std::chrono::high_resolution_clock::now();
        binarySearch(data, target);
        auto endAvg = std::chrono::high_resolution_clock::now();
        totalAvgTime += std::chrono::duration<double, std::micro>(endAvg - startAvg).count();
    }
    double averageTime = totalAvgTime / trials;

    // Worst case: search for an element not in the dataset
    int worstTarget = 2000000000; // larger than max 32-bit values in typical data
    start = std::chrono::high_resolution_clock::now();
    binarySearch(data, worstTarget);
    end = std::chrono::high_resolution_clock::now();
    double worstTime = std::chrono::duration<double, std::micro>(end - start).count();

    // Write results
    outFile << "Binary Search Performance (average of microseconds):\n";
    outFile << "Best Case: " << bestTime << " µs\n";
    outFile << "Average Case (" << trials << " searches): " << averageTime << " µs\n";
    outFile << "Worst Case: " << worstTime << " µs\n";
    outFile.close();

    std::cout << "Results written to binary_search_n.txt\n";
    return 0;
}
