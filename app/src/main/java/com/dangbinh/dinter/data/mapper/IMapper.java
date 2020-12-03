package com.dangbinh.dinter.data.mapper;

public interface IMapper<From, To> {
    To map(From from);
}
