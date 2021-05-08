package com.hcmut.truyenfull.crawler.selector;


public class TruyenFullComicSelector implements ComicSelector<TruyenFullChapterSelector>{

    private TruyenFullComicSelector() {

    }

    private static class SingletonHelper {

        private static final TruyenFullComicSelector INSTANCE = new TruyenFullComicSelector();
    }

    public static TruyenFullComicSelector getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public String title() {
        return "div.col-info-desc h3.title";
    }

    @Override
    public String url() {
        return ".list>.row>div>div>.truyen-title>a";
    }

    @Override
    public String author() {
        return "div.info > div:nth-child(1) > a";
    }

    @Override
    public String categorieLinks() {
        return "div:nth-child(2) > a";
    }

    @Override
    public String status() {
        return "div.col-info-desc div.info span.text-success";
    }

    @Override
    public String chapterLinks() {
        return "ul.list-chapter > li > a";
    }

    

}
