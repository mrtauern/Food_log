package com.base.site.services;

import com.base.site.models.WeightGraph;
import com.base.site.repositories.WeightGraphRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("WeightGraphService")
public class WeightGraphServiceImpl implements WeightGraphService {

    @Autowired
    WeightGraphRepo weightGraphRepo;

    @Override
    public List<WeightGraph> getWeightGraph(Long user_id, String selected_date) {
        return weightGraphRepo.getWeightGraph(user_id, selected_date);
    }

    @Override
    public List<WeightGraph> getWeightGraph(Long user_id) {
        String selected_date = " ";
        return weightGraphRepo.getWeightGraph(user_id, selected_date);
    }
}
