package com.ConcurrentPage;

import com.ConcurrentPage.pageCommon.AsynAbstracPagination;
import com.ConcurrentPage.pageCommon.Pagination;
import com.batchInsert.dao.CostDao;
import com.batchInsert.domain.Cost;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ConcurrentPageService {

    @Resource
    private CostDao costDao;

    /**
     * 并行查询
     * @param params
     * @return
     */
    public Pagination<Cost> queryConcurrentCostPage(Map<String,Object> params){
        Pagination<Cost> pagination = new Pagination<> ();
        AsynAbstracPagination<Cost,Map<String,Object>> costPage = new AsynAbstracPagination<Cost, Map<String, Object>>() {
            @Override
            public List<Cost> getRowList(Map<String, Object> stringObjectMap) {
                return costDao.ListPage(stringObjectMap);
            }

            @Override
            public int getTotalCount(Map<String, Object> stringObjectMap) {
                return costDao.selectTotal(stringObjectMap);
            }
        };
        costPage.execute(pagination,params);
        return pagination;
    }





    /**
     * 串行查询
     * @param params
     * @return
     */
    public Pagination<Cost> querySerialCostPage(Map<String,Object> params){
        Pagination<Cost> pagination = new Pagination<> ();
        pagination.setTotal(costDao.selectTotal(params));
        pagination.setRows(costDao.ListPage(params));
        return pagination;
    }
}