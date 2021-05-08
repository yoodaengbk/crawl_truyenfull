/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo.thrift.crawl;

import com.demo.thrift.model.Chapter;
import com.demo.thrift.model.Comic;
import com.demo.thrift.repo.RedisRepository;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author TAM
 */
public class crawlerRedisData {
    @Autowired
    RedisRepository redisRepository;
    
    public Boolean crawlerAllComic() throws IOException {
        int sl =50;
        Boolean cont = true;
        String url = "https://truyenfull.vn/danh-sach/truyen-hot/";
        String nextUrl = null;
        int i = 1;
        do {

            Document doc = Jsoup.connect(url).get();
            System.out.println("Title : " + doc.title());
            Elements comics = doc.select(".list>.row>div>div>.truyen-title>a");

            for (Element comic : comics) {
                
                    crawlComic( i,comic.attr("href"));
                    
                    i++;
                

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
        } while (i <= sl);

        return true;
    }
    

    public void crawlComic(int i,String urlComic) throws IOException {
        Document doc = Jsoup.connect(urlComic).get();

        Comic comic = new Comic();
        
        comic.setTitle(doc.selectFirst(".col-info-desc h3.title").text());
        comic.setId(i);
        Elements geners = doc.select(".info a[itemprop=genre]");
        
        crawlAllChapter(comic,urlComic);
        redisRepository.add(comic);
    }
     public void crawlAllChapter(Comic comic,String url) throws IOException {

//        String url = "https://truyenfull.vn/doc-ton-tam-gioi/";
        
        String nextUrl = null;
        Boolean cont = true;
        
        int id = 1;
        do {
            Document doc = Jsoup.connect(url).get();
            Elements chaps = doc.select(".list-chapter>li>a");
            for (Element chap : chaps) {
                
                Chapter chapter =new Chapter();
                String title = doc.select(".chapter .chapter-title").attr("title");
                chapter.setTitle(title);
                chapter.setId(id);
                id++;
                
            }

            Element nextEl = doc.select(" div > ul > li.active + li >a").first();
            if(nextEl != null ){                          
                    url = nextEl.attr("href");
                    System.out.println(nextEl.attr("href"));            
            }else{
                break;
            }
            

        } while (cont); 
     }
   
}
