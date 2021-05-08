package com.hcmut.truyenfull.crawler.selector;

public interface ComicSelector<T extends ChapterSelector> {

    String title();

    String author();

    String categorieLinks();

    String url();

    String chapterLinks();

    String status();
}
