#include <vector>

template<typename T>
void Print(const std::vector<T> &v) {
  for (auto value: v) {
    std::cout << value << " ";
  }
  std::cout << std::endl;
}