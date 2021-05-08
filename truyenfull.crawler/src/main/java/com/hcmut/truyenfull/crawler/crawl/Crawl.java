/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.crawler.crawl;

import com.hcmut.truyenfull.crawler.model.*;
import com.hcmut.truyenfull.crawler.repository.*;


import com.hcmut.truyenfull.crawler.selector.TruyenFullCategorySelector;
import com.hcmut.truyenfull.crawler.selector.TruyenFullChapterSelector;
import com.hcmut.truyenfull.crawler.selector.TruyenFullComicSelector;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


/**
 *
 * @author TAM
 */
@Controller
public class Crawl {

    @Autowired
    ComicRepository comicRepository;

    @Autowired
    ChapterRepository chapterRepository;        

    @Autowired
    CategoryRepository categoryRepository;

    
    public Set<Category> getListCategory() throws IOException {
        TruyenFullCategorySelector categorySelector = TruyenFullCategorySelector.getInstance();
        Set<Category> categories = categoryRepository.findAll().stream().collect(Collectors.toSet());
        Document document = Jsoup.connect("https://truyenfull.vn/").get();
        Elements elements = document.select(categorySelector.name());
        for (Element element : elements) {
            Category newCategory = new Category();
            String[] strings = element.absUrl("href").split("/");
            String title = element.text();
            newCategory.setTitle(title);
            String urlName = strings[strings.length - 1];
            newCategory.setUrlName(urlName);
            categories.add(newCategory);
        }

        categoryRepository.saveAll(categories);
        return categories;
    }

    public boolean isExistComic(String urlComic) {
        String[] strings = urlComic.split("/");
        String urlName = strings[strings.length - 1];
        if (comicRepository.findByUrlName(urlName) != null) {
            return true;
        }
        return false;
    }

    
    public Boolean crawlerAllComic(int sl) throws IOException {
        
        TruyenFullComicSelector comicSelector = TruyenFullComicSelector.getInstance();
        Set<Category> categories = categoryRepository.findAll().stream().collect(Collectors.toSet());
        if (categories.isEmpty()) {
            categories = getListCategory();
        }
        Boolean cont = true;
        String url = "https://truyenfull.vn/danh-sach/dam-my-hai/";
        String nextUrl = null;
        int i = 1;
        do {

            Document doc = Jsoup.connect(url).get();
            System.out.println("Title : " + doc.title());
            Elements comics = doc.select(comicSelector.url());

            for (Element comic : comics) {
                if (isExistComic(comic.attr("href"))) {
                    crawlAllChapter(comic.attr("href"));
                    continue;

                } else {
                    crawlComic(comic.attr("href"));
                }

            }
            Element nextEl = doc.select(" div > ul > li.active + li >a").first();
            if (nextEl != null) {

                if (!nextEl.attr("href").equals("javascript:void(0)")) {

                    url = nextEl.attr("href");
                    System.out.println(nextEl.attr("href"));

                } else {
                    break;
                }

            }
            i++;

        } while (i <= sl);

        return true;
    }

    public void crawlComic(String urlComic) throws IOException {
        TruyenFullComicSelector comicSelector = TruyenFullComicSelector.getInstance();
        Document doc = Jsoup.connect(urlComic).get();
        Comic comic = new Comic();
        comic.setAuthor(doc.selectFirst(comicSelector.author()).attr("title"));
        String[] strings = urlComic.split("/");
        String urlName = strings[strings.length - 1];
        comic.setUrlName(urlName);
        comic.setTitle(doc.selectFirst(comicSelector.title()).text());
        if (doc.selectFirst(comicSelector.status()) == null) {
            comic.setStatus("Đang ra");
        } else {
            comic.setStatus(doc.selectFirst(comicSelector.status()).text());
        }
        Elements geners = doc.select(comicSelector.categorieLinks());
        for (Element gener : geners) {
            Category category = categoryRepository.findByTitle(gener.attr("title"));
            System.out.println(category.getUrlName());
            if (category != null) {
                comic.getCategorys().add(category);
            }
        }
        comicRepository.save(comic);
        crawlAllChapter(urlComic);
    }

    public void crawlAllChapter(String url) throws IOException {
        TruyenFullChapterSelector chapterSelector = TruyenFullChapterSelector.getInstance();
//        String url = "https://truyenfull.vn/doc-ton-tam-gioi/";
        String[] strings = url.split("/");
        String urlName = strings[strings.length - 1];
        Comic comic = comicRepository.findByUrlName(urlName);
        String nextUrl = null;
        Boolean cont = true;
        int indexChapter = 1;
        int i = 0;
        do {
            Document doc = Jsoup.connect(url).get();
            Elements chapters = doc.select(chapterSelector.url());
            for (Element chapter : chapters) {
                System.out.println(chapter.text());
                System.out.println(chapter.attr("href"));
                crawlChapter(chapter.attr("href"), comic, indexChapter);
                indexChapter++;
            }

            Element nextEl = doc.select(" div > ul > li.active + li >a").first();
            if (nextEl != null) {
                url = nextEl.attr("href");
                System.out.println(nextEl.attr("href"));
            } else {
                break;
            }
            i++;

        } while (cont);

    }

    public void crawlChapter(String urlChapter, Comic comic, int indexChapter) throws IOException {
        TruyenFullChapterSelector chapterSelector = TruyenFullChapterSelector.getInstance();
        Document doc = Jsoup.connect(urlChapter).get();
        Chapter chapter = new Chapter();
        String title = doc.select(chapterSelector.title()).attr("title");
        String[] strings = urlChapter.split("/");
        String urlName = strings[strings.length - 1];
        chapter.setUrlName(urlName);
        chapter.setTitle(title);
        chapter.setIndex(indexChapter);
        comic.addChapter(chapter);
        chapterRepository.save(chapter);
    }
    
    public Boolean updateAllComics(int sl) throws IOException {
        TruyenFullComicSelector comicSelector = TruyenFullComicSelector.getInstance();
        
        String url = "https://truyenfull.vn/danh-sach/truyen-moi/";
        String nextUrl = null;
        int i = 0;
        do {

            Document doc = Jsoup.connect(url).get();
            System.out.println("Title : " + doc.title());
            Elements comics = doc.select(".list>.row>div>div>.truyen-title>a");

            for (Element comic : comics) {
                if (isExistComic(comic.attr("href"))) {
                    updateChapterOfComic(comic.attr("href"));
                    i++;
                    continue;

                } else {
                    crawlComic(comic.attr("href"));
                    i++;
                }
            }
            Element nextEl = doc.select(" div > ul > li.active + li >a").first();
            if(nextEl != null ){
                
                if (!nextEl.attr("href").equals("javascript:void(0)")) {

                    url = nextEl.attr("href");
                    System.out.println(nextEl.attr("href"));
                }
                else {
                    break;
                }                                 
            }
        } while (i < sl);

        return true;
    }
    
    public void updateChapterOfComic(String urlComic) throws IOException {
//        String url = "https://truyenfull.vn/hoa-ra-chung-ta-van-con-yeu/";
        String[] strings = urlComic.split("/");
        String urlName = strings[strings.length - 1];
        Comic comic = comicRepository.findByUrlName(urlName);
        List<Chapter> chapters = comic.getChapters();
        Chapter chapter = chapters.get(chapters.size() - 1);
        String urlChapter = chapter.getUrlName();
        int indexChapter = chapter.getIndex() + 1;
        String linkChapter = urlComic + urlChapter;
        do {
            Document doc = Jsoup.connect(linkChapter).get();
            Element nextPage = doc.selectFirst("#next_chap");
            if (!nextPage.attr("href").equals("javascript:void(0)")) {
                crawlChapter(nextPage.attr("href"), comic, indexChapter);
                indexChapter++;
                linkChapter = nextPage.attr("href");
                System.out.println("Da update chap moi");
            } else {
                break;
            }
        } while (true);

    }

    
}
