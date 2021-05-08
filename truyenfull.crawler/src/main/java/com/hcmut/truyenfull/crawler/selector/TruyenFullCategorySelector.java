package com.hcmut.truyenfull.crawler.selector;



public class TruyenFullCategorySelector implements CategorySelector {

    private TruyenFullCategorySelector() {

    }

    private static class SingletonHelper {

        private static final TruyenFullCategorySelector INSTANCE = new TruyenFullCategorySelector();
    }

    public static TruyenFullCategorySelector getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public String name() {
        return "div.dropdown-menu > div.row > div.col-md-4 > ul > li > a";
    }
}
