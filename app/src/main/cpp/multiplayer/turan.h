//
// Created by Daler on 20.10.2025.
//
#pragma once

#include <string>

struct Turan
{
    std::string szHost = "188.127.241.74";
    int iPort = 2838;
};

inline Turan GetTuran()
{
    Turan config;
    return config;
}