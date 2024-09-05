package com.example.beans;

import java.util.List;

public class ResultPage<T> {
    private int pages;
    private List<T> results;

    public ResultPage(int pages, List<T> results) {
        this.pages = pages;
        this.results = results;
    }

    public int getPages() {
        return pages;
    }

    public List<T> getResults() {
        return results;
    }
}
