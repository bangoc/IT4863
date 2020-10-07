#include <iostream>
#include <vector>

#include "utils.h"

template<typename T>
int Intersect(const std::vector<T> &a, const std::vector<T> &b, 
              std::vector<T> &out) {
  int cc = 0;
  auto i1 = a.begin(),
       i2 = b.begin();
  while (i1 != a.end() && i2 != b.end()) {
    if (*i1 == *i2) {
      ++cc;
      out.push_back(*i1);
      ++i1;
      ++i2;
    } else if (*i1 < *i2) {
      ++i1;
    } else {
      ++i2;
    }
  }
  return cc;
}

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