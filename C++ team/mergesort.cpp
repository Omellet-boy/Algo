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

void merge(std::vector<Data>& arr, int left, int mid, int right) {
    std::vector<Data> leftVec(arr.begin() + left, arr.begin() + mid + 1);
    std::vector<Data> rightVec(arr.begin() + mid + 1, arr.begin() + right + 1);

    int i = 0, j = 0, k = left;
    while (i < leftVec.size() && j < rightVec.size()) {
        if (leftVec[i].number <= rightVec[j].number)
            arr[k++] = leftVec[i++];
        else
            arr[k++] = rightVec[j++];
    }

    while (i < leftVec.size())
        arr[k++] = leftVec[i++];
    while (j < rightVec.size())
        arr[k++] = rightVec[j++];
}

void mergeSort(std::vector<Data>& arr, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }
}

int main() {
    std::string filename;
    std::cout << "Enter dataset filename (e.g., generated_dataset_1000.csv): ";
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

    mergeSort(data, 0, data.size() - 1);

    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> duration = end - start;

    std::ofstream outFile("merge_sort_n.csv");
    for (const auto& d : data) {
        outFile << d.number << "," << d.text << "\n";
    }
    outFile.close();

    std::cout << "Sorted data written to merge_sort_1000.csv\n";
    std::cout << "Running time: " << duration.count() << " milliseconds\n";

    return 0;
}
