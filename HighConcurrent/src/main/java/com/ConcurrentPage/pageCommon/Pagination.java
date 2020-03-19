package com.ConcurrentPage.pageCommon;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> {
    private int pageSize = 10;  // 每页显示记录数
    private int currentPage = 1;    // 当前页码
    private int maxPage = Integer.MAX_VALUE;    // 最大页数
    private int totalPage = Integer.MAX_VALUE;
    private List<T> rows = new ArrayList<T>();//本页记录集
    private int total = pageSize;//总记录数

    public Pagination(){}

    public Pagination(int pageSize, int currentPage) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	// 获取offset
    public int getOffset() {
        return (currentPage - 1) * pageSize;
    }

    // 获取limit
    public int getLimit() {
        return getPageSize();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage < 1)
            currentPage = 1;
        if (currentPage > maxPage)
            currentPage = maxPage;
        this.currentPage = currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
    	int totalPage = 0;
        if (total > 0) {
            totalPage = (total - 1) / pageSize + 1;
        }
        setTotalPage(totalPage);
        this.total = total;
    }
}