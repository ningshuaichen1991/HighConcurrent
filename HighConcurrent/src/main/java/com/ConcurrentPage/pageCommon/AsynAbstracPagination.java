package com.ConcurrentPage.pageCommon;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 异步查询分页数据
 */
@Slf4j
public abstract class AsynAbstracPagination<T,V> {


    public abstract List<T> getRowList(V v);

    public abstract int getTotalCount(V v);

    public void execute(Pagination p,V v){
        CompletableFuture<Pagination> totalCountFuture = CompletableFuture.supplyAsync(()->{
            p.setTotal(this.getTotalCount(v));
            log.info("总数查询执行完毕");
            return p;
        });
        CompletableFuture<Pagination> listFutrue = CompletableFuture.supplyAsync(()->{
            p.setRows(this.getRowList(v));
            log.info("列表执行查询完毕");
            return p;
        });
        CompletableFuture.allOf(totalCountFuture,listFutrue).join();
    }
}
