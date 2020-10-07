#include <iostream>
#include <map>
#include <string>
#include <vector>

#include "set_ops.h"
#include "utils.h"

template<typename T>
using MemIndex = std::map<std::string, std::vector<T>>;

template<typename T>
int BooleanAnd(const MemIndex<T> &I, const std::string &w1, 
      const std::string& w2, std::vector<T> &out) {
  auto it1 = I.find(w1),
       it2 = I.find(w2);
  if (it1 == I.end() || it2 == I.end()) {
    return 0;
  }
  int cc = Intersect(it1->second, it2->second, out);
  return cc;
}

template<typename T>
void Test(const MemIndex<T> &I, const std::string &w1, 
      const std::string& w2) {
  std::vector<T> out;
  int cc = BooleanAnd(I, w1, w2, out);
  std::cout << "#Results for: " << w1 << " AND " << w2 << ": " << cc << std::endl;
  if (cc > 0) {
    Print(out);
  }
}

int main() {
  MemIndex<int> I {
    {"hello", {1, 3, 5, 8, 9, 10}},
    {"world", {3, 5, 6, 8, 10, 15}}
  };
  Test(I, "hello", "world");
  return 0;
}