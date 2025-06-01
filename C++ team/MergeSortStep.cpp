#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <string>
#include <iomanip>

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

void merge(std::vector<Data>& arr, int left, int mid, int right, std::ofstream& out) {
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

    printStep(arr, out);  // Print step after each merge
}

void mergeSort(std::vector<Data>& arr, int left, int right, std::ofstream& out) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid, out);
        mergeSort(arr, mid + 1, right, out);
        merge(arr, left, mid, right, out);
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

    std::string outFileName = "merge_sort_step_" + std::to_string(startRow) + "_" + std::to_string(endRow) + ".txt";
    std::ofstream outFile(outFileName);
    if (!outFile) {
        std::cerr << "Cannot open output file.\n";
        return 1;
    }

    outFile << "Initial data:\n";
    printStep(data, outFile);

    mergeSort(data, 0, data.size() - 1, outFile);

    outFile << "Final sorted data:\n";
    printStep(data, outFile);

    outFile.close();
    std::cout << "Merge sort steps saved to " << outFileName << "\n";

    return 0;
}
