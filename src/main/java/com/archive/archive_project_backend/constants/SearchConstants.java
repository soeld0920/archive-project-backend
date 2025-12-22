package com.archive.archive_project_backend.constants;

import java.util.Map;

public class SearchConstants {
    public static String SNIPPER = "snippet";
    public static String SERIES = "series";
    public static Map<String, Integer> SORT_ORDER_CODE_MAP = Map.of(
            "정확도순",1, "인기순",2, "최신순",3, "오래된순",4,
            "조회수 높은 순",5, "조회수 낮은 순",6, "좋아요 많은 순", 7,"좋아요 적은 순",8, "가나다순",9
    );
}
