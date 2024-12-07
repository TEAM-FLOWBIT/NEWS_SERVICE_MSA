package com.example.boardservice.global.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

public class HtmlSanitizer {

    /**
     * HTML에서 <script> 태그 및 기타 위험 요소 제거
     */
    public static String sanitizeHtml(String htmlContent) {
        // Document로 파싱
        Document document = Jsoup.parse(htmlContent);

        // Safelist 설정: <script> 태그 제거
        Safelist safelist = Safelist.relaxed()
                .addAttributes(":all", "style", "class", "id") // 모든 태그에 style, class, id 속성 허용
                .addAttributes("table", "border")             // 테이블에 border 속성 허용
                .addAttributes("a", "href", "target", "rel")  // 링크 관련 속성 허용
                .addAttributes("img", "src", "alt", "title"); // 이미지 관련 속성 허용

        // Body의 안전한 HTML만 정리
        String safeBodyHtml = Jsoup.clean(document.body().html(), safelist);

        // Head 부분은 무조건 허용
        String safeHeadHtml = document.head().html();

        // <!DOCTYPE>과 전체 HTML 조합
        return "<!DOCTYPE html>\n<html>\n<head>\n" + safeHeadHtml + "\n</head>\n<body>\n" + safeBodyHtml + "\n</body>\n</html>";
    }
}
