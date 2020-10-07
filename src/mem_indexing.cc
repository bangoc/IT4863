#include <algorithm>
#include <filesystem>
#include <fstream>
#include <iostream>

#include "index.h"
#include "utils.h"

struct Posting {
  std::string token;
  int file_id;
  bool operator<(const Posting& other) {
    return token < other.token || 
           (token == other.token && file_id <= other.file_id);
  }
};

int ParseFile(const std::string &path, std::vector<std::string> &tokens) {
  std::ifstream inp{path};
  std::string token;
  int cc = 0;
  while (inp >> token) {
    tokens.push_back(token);
    ++cc;
  }
  return cc;
}

void ParseDirectory(const std::string&d, 
      std::vector<Posting> &postings,
      std::vector<std::string> &paths) {
  int id = 0;  
  for (auto &p: std::filesystem::recursive_directory_iterator(d)) {
    paths.push_back(p.path());    
    std::vector<std::string> tokens;
    ParseFile(p.path(), tokens);
    for (auto &tk: tokens) {
      postings.push_back({tk, id});
    }
    ++id;
  }
}

void SimpleInvert(const std::vector<Posting> &postings, MemIndex<int> &out) {
  Posting prev = postings[0];
  std::vector<int> docs;
  docs.push_back(prev.file_id);
  auto it = postings.begin() + 1;
  for (; it != postings.end(); ++it) {
    if (it->token == prev.token) {
      if (it->file_id == prev.file_id) {
        // count tf if required
      } else {
        docs.push_back(it->file_id);
      }
    } else {
      out[prev.token] = docs;
      docs.clear();
      docs.push_back(it->file_id);
    }
    prev = *it;
  }
  out[prev.token] = docs;
}

void CreateSimpleMemIndexFromDir(const std::string& d,
    MemIndex<int> &out,
    std::vector<std::string> &paths) {
  std::vector<Posting> postings;
  ParseDirectory(d, postings, paths);
  std::sort(postings.begin(), postings.end());
  SimpleInvert(postings, out);
}

int main(int argc, char *argv[]) {
  if (argc != 2) {
    std::cout << "Usage: memindex directory_to_be_indexed" << std::endl;
    return 1;
  }
  MemIndex<int> I;
  std::vector<std::string> paths;
  CreateSimpleMemIndexFromDir(argv[1], I, paths);
  Print(I);
  Print(paths);
  return 0;
}