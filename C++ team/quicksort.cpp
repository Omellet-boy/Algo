#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <string>
#include <chrono>

struct Data {
    int number;
    std::string text;
};

// Partition function for quicksort (last element as pivot)
int partition(std::vector<Data>& arr, int low, int high) {
    int pivot = arr[high].number;
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (arr[j].number <= pivot) {
            ++i;
            std::swap(arr[i], arr[j]);
        }
    }
    std::swap(arr[i + 1], arr[high]);
    return i + 1;
}

// Recursive quicksort
void quickSort(std::vector<Data>& arr, int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}

int main() {
    std::string filename;
    std::cout << "Enter dataset filename (e.g., dataset_sample_1000.csv): ";
    std::cin >> filename;

    std::ifstream inFile(filename);
    if (!inFile) {
        std::cerr << "Error opening file: " << filename << "\n";
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

    auto start = std::chrono::high_resolution_clock::now();

    quickSort(data, 0, data.size() - 1);

    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> duration = end - start;

    std::ofstream outFile("quick_sort_n.csv");
    for (const auto& d : data) {
        outFile << d.number << "," << d.text << "\n";
    }
    outFile.close();

    std::cout << "Sorted data written to quick_sort_n.csv\n";
    std::cout << "Running time: " << duration.count() << " milliseconds\n";

    return 0;
}
