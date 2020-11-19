package com.dangbinh.dinter.mapper;

public interface IMapper<From, To> {
    To map(From from);
}
