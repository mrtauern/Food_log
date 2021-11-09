package com.base.site.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.Resource;

public class FoodImportService {
    @Autowired
    ResourceLoader resourceLoader;

    @Value("classpath:data/data.json")
    Resource resourceFile;
}

