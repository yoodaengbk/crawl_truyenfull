package com.hcmut.truyenfull.crawler.selector;




public class TruyenFullChapterSelector implements ChapterSelector {

    private TruyenFullChapterSelector() {

    }

    

    private static class SingletonHelper {

        private static final TruyenFullChapterSelector INSTANCE = new TruyenFullChapterSelector();
    }

    public static TruyenFullChapterSelector getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public String title() {
        return "a.chapter-title";
    }

    @Override
    public String content() {
        return "#chapter-c";
    }
    @Override
    public String url() {
        return ".list-chapter>li>a";
    }
    
    
    
}
