#ifndef UTILS_H_
#define UTILS_H_

#include <iostream>
#include <vector>

#include "index.h"

template<typename T>
void Print(const std::vector<T> &v) {
  for (auto value: v) {
    std::cout << value << " ";
  }
  std::cout << std::endl;
}

template<typename T>
void Print(const MemIndex<T> &I) {
  for (auto it = I.begin(); it != I.end(); ++it) {
    std::cout << it->first << ": ";
    for (auto &value: it->second) {
      std::cout << value << " ";
    }
    std::cout << std::endl;
  }
}

#endif  // UTILS_H_