#include <iostream>
#include <vector>

#include "set_ops.h"
#include "utils.h"

template<typename T>
void Test(const std::vector<T> &a, const std::vector<T> &b) {
  std::vector<T> out;
  int cc = Intersect(a, b, out);
  std::cout << cc << std::endl;
  Print(out);
}

int main() {
  Test((std::vector<int>){1, 3, 5, 7},
       (std::vector<int>){3, 6, 7, 9, 10});
  return 0;
}