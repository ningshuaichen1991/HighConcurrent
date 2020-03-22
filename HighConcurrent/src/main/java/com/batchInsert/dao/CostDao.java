package com.batchInsert.dao;

import com.batchInsert.domain.Cost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CostDao {

    /**
     * 批量插入
     * @param list
     * @return
     */
    int batchInsert(@Param("listData") List<Cost> list);

    Cost selectByPrimaryKey(int id);

    /**
     * 总共费用数
     * @param param
     * @return
     */
    int selectTotal(Map<String,Object> param);

    /**
     * 费用信息分页
     * @param param
     * @return
     */
    List<Cost> ListPage(Map<String,Object> param);
}
