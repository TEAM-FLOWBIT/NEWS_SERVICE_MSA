package com.example.boardservice.domain.news.service.impl;

import com.example.boardservice.domain.news.dto.NewsResponseDto;
import com.example.boardservice.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {


    private final Environment env;



    @Override
    public List<NewsResponseDto> getNewsData(String searchWord, Long display) throws Exception {

        ArrayList<NewsResponseDto> newsResponseDtos = new ArrayList<>();

        String clientId = env.getProperty("X-Naver-Client-Id");
        String clientSecret = env.getProperty("X-Naver-Client-Secret");

        String responseData = "";
        String apiUrl = "https://openapi.naver.com/v1/search/news.json?query="+searchWord+"&display="+display+"&start=1&sort=sim";

        responseData = httpRequestToNaver(clientId, clientSecret, responseData, apiUrl);
        parsingData(newsResponseDtos, responseData);

        return newsResponseDtos;
    }

    private void parsingData(ArrayList<NewsResponseDto> newsResponseDtos, String responseData) throws Exception {
        String jsonString = responseData;
        // JSON 문자열을 JSONObject로 파싱
        JSONObject jsonObject = new JSONObject(jsonString);

        // 필요한 데이터 추출
        int total = jsonObject.getInt("total");
        JSONArray items = jsonObject.getJSONArray("items");


        // items 배열 순회
        for (int i = 0; i < items.length(); i++) {



            JSONObject item = items.getJSONObject(i);
            NewsResponseDto newsDto = NewsResponseDto.builder()
                    .title(item.getString("title"))
                    .originalling(item.getString("originallink"))
                    .link(item.getString("link"))
                    .description(item.getString("description"))
                    .pubDate(item.getString("pubDate"))
                    .img(linkPreview(item.getString("link")))
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
