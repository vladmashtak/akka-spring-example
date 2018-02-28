package com.engine.node.services;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class RandomNumberService {

    public int getRandomNumber() {
        return new Random().nextInt(10);
    }
}
