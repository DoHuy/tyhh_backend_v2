package com.stadio.model.redisUtils;

/**
 * Created by Andy on 01/29/2018.
 */
public class RedisConst
{
    //select DB
    public static final long TIME_TO_LIVE_SHORT = 60*3; // 3 MIN UNIT
    public static final long TIME_TO_LIVE_LONG = 60*12; // 12 MIN UNIT
    public static final long TIME_TO_LIVE_TOO_LONG = 60*60; // 1 HOUR UNIT

    //DB_EXAM
    public static final int DB_EXAM = 0;

    //DB_USER
    public static final int DB_USER = 1;

    //DB_HOME
    public static final int DB_BANNER = 2;
    public static final int DB_NEWEST_COURSE = 2;
    public static final int DB_CATEGORY = 2;

    //DB_EXAM_ONLINE
    public static final int DB_EXAM_ONLINE = 3;

    //DB_UTILS
    public static final int DB_ULTIS = 5;

    //select key

    public static final String EXAM_STATISTICS = "exam_statistics";
    public static final String EXAM_HOT_HOME_SCREEN ="exam_hot_home_screen";
    public static final String EXAM_HOT_HOME_TAB ="exam_hot_home_tab";
    public static final String EXAM_HOT_CHAPTER ="exam_hot_chapter";
    public static final String EXAM_COUNT = "exam_count";

    public static final String BANNER_HOME = "banner_home";
    public static final String NEWEST_COURSE_HOME = "newest_course_home";
    public static final String CATEGORY = "category";

    public interface EXAM_KEY
    {
        String QUESTION = "quantity";
        String VIEWS = "views";
        String LIKE = "like";
        String AVG = "average";
    }

    public static final String USER_REQUEST_EXAM = "exam_practice";

    public static final String USER_RANK_WEEKLY_TOP_10 = "rank_w_10";
    public static final String USER_RANK_WEEKLY_TOP_50 = "rank_w_50";
    public static final String USER_RANK_WEEKLY_TOP_100 = "rank_w_100";
    public static final String USER_RANK_MONTHLY_TOP_10 = "rank_m_100";
    public static final String USER_RANK_MONTHLY_TOP_50 = "rank_m_100";
    public static final String USER_RANK_MONTHLY_TOP_100 = "rank_m_100";
}
