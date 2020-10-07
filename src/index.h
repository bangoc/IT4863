#ifndef INDEX_H_
#define INDEX_H_

#include <map>
#include <string>
#include <vector>

template<typename T>
using MemIndex = std::map<std::string, std::vector<T>>;

#endif  // INDEX_H_