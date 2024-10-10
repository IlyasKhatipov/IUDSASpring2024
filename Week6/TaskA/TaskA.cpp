// Ilyas
#include <iostream>
#include <vector>

using namespace std;

template<typename T>
void IlyasKhatipov_counting_srt(vector<pair<pair<T, T>, int>>& arr) {
    if (arr.empty()) return;
    T maxFirst = arr[0].first.first;
    T minFirst = arr[0].first.first;
    for (const auto& p : arr) {
        maxFirst = max(maxFirst, p.first.first);
        minFirst = min(minFirst, p.first.first);
    }
    vector<int> count(maxFirst - minFirst + 1, 0);
    for (const auto& p : arr) {
        count[maxFirst - p.first.first]++;
    }
    for (size_t i = 1; i < count.size(); ++i) {
        count[i] += count[i - 1];
    }
    vector<pair<pair<T, T>, int>> temp(arr.size());
    for (int i = arr.size() - 1; i >= 0; --i) {
        temp[--count[maxFirst - arr[i].first.first]] = arr[i];
    }
    arr = temp;
}

template<typename T>
void IlyasKhatipov_radix_srt(vector<pair<pair<T, T>, int>>& data) {
    if (data.empty()) return;
    T maxSecond = data[0].first.second;
    for (const auto& p : data) {
        maxSecond = max(maxSecond, p.first.second);
    }
    for (T exp = 1; maxSecond / exp > 0; exp *= 10) {
        vector<vector<pair<pair<T, T>, int>>> buckets(10);
        for (const auto& p : data) {
            int digit = (p.first.second / exp) % 10;
            buckets[digit].push_back(p);
        }
        size_t index = 0;
        for (auto& bucket : buckets) {
            for (auto& p : bucket) {
                data[index++] = p;
            }
            bucket.clear();
        }
    }
}

int main() {
    int n;
    cin >> n;
    vector<pair<pair<int, int>, int>> data(n);
    for (int i = 0; i < n; ++i) {
        cin >> data[i].first.first >> data[i].first.second;
        data[i].second = i + 1;
    }

    IlyasKhatipov_radix_srt(data);
    IlyasKhatipov_counting_srt(data);

    for (const auto& p : data) {
        cout << p.second << " ";
    }
    cout << endl;

    return 0;
}
