#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <string>

struct Data {
    int number;
    std::string text;
};

void printStep(const std::vector<Data>& data, std::ofstream& out) {
    for (const auto& item : data) {
        out << item.number << "," << item.text << " ";
    }
    out << "\n";
}

int partition(std::vector<Data>& arr, int low, int high, std::ofstream& out) {
    int pivot = arr[high].number;
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (arr[j].number <= pivot) {
            ++i;
            std::swap(arr[i], arr[j]);
        }
    }
    std::swap(arr[i + 1], arr[high]);

    printStep(arr, out); // Log step after partitioning
    return i + 1;
}

void quickSort(std::vector<Data>& arr, int low, int high, std::ofstream& out) {
    if (low < high) {
        int pi = partition(arr, low, high, out);
        quickSort(arr, low, pi - 1, out);
        quickSort(arr, pi + 1, high, out);
    }
}

int main() {
    std::ifstream inFile("generated_dataset.csv");
    if (!inFile) {
        std::cerr << "Cannot open input file.\n";
        return 1;
    }

    int startRow, endRow;
    std::cout << "Enter start row (1-based): ";
    std::cin >> startRow;
    std::cout << "Enter end row (1-based): ";
    std::cin >> endRow;

    std::vector<Data> data;
    std::string line;
    int row = 0;

    while (std::getline(inFile, line)) {
        row++;
        if (row < startRow) continue;
        if (row > endRow) break;

        std::stringstream ss(line);
        std::string numStr, txt;
        if (std::getline(ss, numStr, ',') && std::getline(ss, txt)) {
            data.push_back({std::stoi(numStr), txt});
        }
    }

    inFile.close();

    std::string outFileName = "quick_sort_step_" + std::to_string(startRow) + "_" + std::to_string(endRow) + ".txt";
    std::ofstream outFile(outFileName);
    if (!outFile) {
        std::cerr << "Cannot open output file.\n";
        return 1;
    }

    outFile << "Initial data:\n";
    printStep(data, outFile);

    quickSort(data, 0, data.size() - 1, outFile);

    outFile << "Final sorted data:\n";
    printStep(data, outFile);

    outFile.close();
    std::cout << "Quick sort steps saved to " << outFileName << "\n";

    return 0;
}
