package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.DetailedCompensation;

public interface CompensationService {
    Compensation create(Compensation compensation);

    Compensation read(String id);

    DetailedCompensation detailed(String id);
}
