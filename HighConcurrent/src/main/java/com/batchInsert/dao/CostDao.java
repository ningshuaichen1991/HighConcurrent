package com.batchInsert.dao;

import com.batchInsert.domain.Cost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CostDao {

    int batchInsert(@Param("listData") List<Cost> list);

    Cost selectByPrimaryKey(int id);

    int selectTotal(Map<String,Object> param);

    List<Cost> ListPage(Map<String,Object> param);
}
