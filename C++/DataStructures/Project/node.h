#pragma once
#include <string>

using std::string;

struct node_info
{
    string value;
    long long int times;
};

class node
{
public:
    node_info data;
    long long int height;
    node* left;
    node* right;
    node* parent;
    node(const string &);
    node();
};

node::node(const string& word)
{
    data.value = word;
    data.times = 1;
    left = nullptr;
    right = nullptr;
    parent = nullptr;
    height = 1;
}

node::node()
{
    left = nullptr;
    right = nullptr;
    parent = nullptr;
    height = 0;
}
