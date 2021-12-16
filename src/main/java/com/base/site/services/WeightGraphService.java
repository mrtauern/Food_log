package com.base.site.services;

import com.base.site.models.WeightGraph;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("WeightGraphService")
public interface WeightGraphService {
    List<WeightGraph> getWeightGraph(Long user_id, String selected_date);
    List<WeightGraph> getWeightGraph(Long user_id);
}
