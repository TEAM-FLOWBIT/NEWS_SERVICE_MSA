package com.example.boardservice.domain.news.service.impl;

import com.example.boardservice.domain.news.dto.NewsSearchCondition;
import com.example.boardservice.domain.news.dto.response.NaverNewsResponseDto;
import com.example.boardservice.domain.news.dto.response.NewsResponseDto;
import com.example.boardservice.domain.news.entity.News;
import com.example.boardservice.domain.news.entity.NewsViewCount;
import com.example.boardservice.domain.news.exception.error.NotFoundNewsException;
import com.example.boardservice.domain.news.repository.NewsRepository;
import com.example.boardservice.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.boardservice.global.tranlator.Translator.getNewsViewCount;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NewsServiceImpl implements NewsService {


    private final Environment env;
    private final NewsRepository newsRepository;



    @Override
    public List<NaverNewsResponseDto> readNaverNewsData(String searchWord, Long display) throws Exception {

        ArrayList<NaverNewsResponseDto> newsResponseDtos = new ArrayList<>();

        String clientId = env.getProperty("X-Naver-Client-Id");
        String clientSecret = env.getProperty("X-Naver-Client-Secret");

        String responseData = "";
        String apiUrl = "https://openapi.naver.com/v1/search/news.json?query="+searchWord+"&display="+display+"&start=1&sort=sim";

        responseData = httpRequestToNaver(clientId, clientSecret, responseData, apiUrl);
        parsingData(newsResponseDtos, responseData,searchWord);

        return newsResponseDtos;
    }

    @Override
    @Transactional
    public void saveNewsApiData(List<NaverNewsResponseDto> newsData) {
        for (NaverNewsResponseDto data : newsData) {
            News news = News.builder()
                    .originalLink(data.getOriginalLink())
                    .tag(data.getTag())
                    .link(data.getPreview_link())
                    .pubDate(data.getPubDate())
                    .title(data.getTitle())
                    .description(data.getDescription())
                    .img(data.getImg())
                    .newsViewCount(new NewsViewCount(0L))
                    .build();

            // link 값이 데이터베이스에 이미 존재하는지 확인
            News existingNews = newsRepository.findByLink(news.getLink()).orElse(null);
            if (existingNews == null) {
                // link 값이 존재하지 않으면 새로운 뉴스를 저장
                newsRepository.save(news);
            } else {
                // link 값이 이미 존재하면 기존 뉴스의 태그를 업데이트
                if (!existingNews.isDuplicateTag(news.getTag())) {
                    // 기존 뉴스의 태그에 새로운 태그를 추가합니다.
                    String updatedTags = existingNews.getTag() + "," + news.getTag();
                    existingNews.updateTag(updatedTags);
                    newsRepository.save(existingNews);
                }
            }
        }
    }

    @Override
    public Page<News> readNewsList(NewsSearchCondition newsSearchCondition, Pageable pageable) {
        Page<News> newsList = newsRepository.readBoardList(newsSearchCondition, pageable);
        return newsList;
//        List<NewsResponseDto> dtoList = newsList.stream().map(NewsResponseDto::new).collect(Collectors.toList());
//        return newsList
//        return new PageImpl<>(dtoList, newsList.getPageable(), dtoList.size());
    }

    @Override
    @Transactional
    public Long updateNewsViewCount(String link) {
        News news = newsRepository.findByLink(link).orElseThrow(NotFoundNewsException::new);
        news.plusNewsViewCount(getNewsViewCount(news.getNewsViewCount().getNewsViewCount()).getNewsViewCount());
        return news.getNewsViewCount().getNewsViewCount();
    }


    private void parsingData(ArrayList<NaverNewsResponseDto> newsResponseDtos, String responseData, String searchWord) throws Exception {
        String jsonString = responseData;
        // JSON 문자열을 JSONObject로 파싱
        JSONObject jsonObject = new JSONObject(jsonString);

        // 필요한 데이터 추출
        int total = jsonObject.getInt("total");
        JSONArray items = jsonObject.getJSONArray("items");


        // items 배열 순회
        for (int i = 0; i < items.length(); i++) {



            JSONObject item = items.getJSONObject(i);
            NaverNewsResponseDto newsDto = NaverNewsResponseDto.builder()
                    .title(item.getString("title"))
                    .originalLink(item.getString("originallink"))
                    .preview_link(item.getString("link"))
                    .description(item.getString("description"))
                    .pubDate(item.getString("pubDate"))
                    .img(linkPreview(item.getString("link")))
                    .tag(searchWord)
                    .build();
            newsResponseDtos.add(newsDto);
        }
    }

    private static String httpRequestToNaver(String clientId, String clientSecret, String responseData, String apiUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("X-Naver-Client-Id", clientId)
                .addHeader("X-Naver-Client-Secret", clientSecret)
                .build();
        try {
            Response response = client.newCall(request).execute();
            responseData = response.body().string();

        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }


    private static String getMetaTagContent(Document document, String metaTagName) {
        Elements metaTags = document.select(metaTagName);
        for (Element metaTag : metaTags) {
            String content = metaTag.attr("content");
            if (!content.isEmpty()) {
                return content;
            }
        }
        return "";
    }

    private static String getMetaTagSrc(Document document, String tagName) {
        Elements elements = document.select(tagName);
        for (Element element : elements) {
            String src = element.attr("src");
            if (!src.isEmpty()) {
                return src;
            }
        }
        return "";
    }

    private String linkPreview(String url) throws Exception {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }

        Document document = Jsoup.connect(url).get();

        URI uri = new URI(url);
        String domain = uri.getHost();

        String link = url;
        String description = "";
        String thumbnail = "";
        String favicon = "";

        //System.out.println(document.toString());

        if (!domain.startsWith("www")) {
            domain = domain;
        }

        description = getMetaTagContent(document, "meta[name=description]");
        if (description.equals("")) {
            description = getMetaTagContent(document, "meta[property=og:description]");
        }

        thumbnail = getMetaTagContent(document, "meta[property=og:image]");
        if (thumbnail.equals("")) {
            thumbnail = getMetaTagContent(document, "meta[property=twitter:image]");
            if (thumbnail.equals("")) {
                thumbnail = getMetaTagSrc(document, "img");
            }
        }
        if (!thumbnail.startsWith("http")) {
            thumbnail = "http://" + domain + thumbnail;
        }

        Element faviconElem = document.head().select("link[href~=.*\\.(ico|png)]").first();
        if (faviconElem != null) {
            favicon = faviconElem.attr("href");
        } else if (document.head().select("meta[itemprop=image]").first() != null) {
            favicon = document.head().select("meta[itemprop=image]").first().attr("content");
        }

        if (!favicon.startsWith("http")) {
            if (domain.startsWith("www")) {
                favicon = "http://" + domain + favicon;
            } else {
                favicon = "http://www." + domain + favicon;
            }
        }

        JSONObject result = new JSONObject();
        result.put("domain", domain);
        result.put("link", link);
        result.put("description", description);
        result.put("thumbnail", thumbnail);
        result.put("favicon", favicon);


        return result.get("thumbnail").toString();
    }

}
