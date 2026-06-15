package com.henheang.collection;

import java.util.HashMap;
import java.util.Map;

public class MapParamDemo {
    public static void main(String[] args) {

        // =====================================================================
        // Map<String, Object> is the most common way to pass parameters
        // in Spring MyBatis instead of creating a DTO class every time.
        //
        // Structure:
        //   Key → String (the name you use to access the value)
        //   Value → Object (any type: String, int, boolean, List, etc.)
        //
        // In MyBatis XML, #{memberId} reads from param.get("memberId")
        // so the key name MUST match exactly.
        // =====================================================================

        // ----- 1. put(key, value) : insert or update a value -----
        // If the key already exists, the old value is REPLACED
        Map<String, Object> param = new HashMap<>();
        param.put("memberId", "U001");
        param.put("memberNm", "Heang");
        param.put("age", 30);       // int auto-boxed to Integer (Object)
        param.put("useYn", "Y");

        System.out.println("=== 1. Basic put / get ===");

        // ----- 2. get(key) : retrieve a value by key -----
        // Returns Object → cast if you need a specific type
        // Returns null if the key does not exist (no exception thrown)
        System.out.println("memberId : " + param.get("memberId"));
        System.out.println("memberNm : " + param.get("memberNm"));
        System.out.println("age      : " + param.get("age"));
        System.out.println("useYn    : " + param.get("useYn"));

        // ----- 3. getOrDefault(key, defaultValue) : safe get with fallback -----
        // Very useful in Spring when a param may or may not be passed by the frontend
        // If "dept" key is missing → returns "ALL" instead of null
        String dept = (String) param.getOrDefault("dept", "ALL");
        System.out.println("\n=== 2. getOrDefault ===");
        System.out.println("dept : " + dept);   // → ALL

        // ----- 4. containsKey(key) : check if key exists -----
        // Use this before get() to avoid NullPointerException
        System.out.println("\n=== 3. containsKey ===");
        if (param.containsKey("memberNm")) {
            System.out.println("memberNm exists : " + param.get("memberNm"));
        }

        // ----- 5. remove(key) : delete a key-value pair -----
        // Common use: remove sensitive data before logging or returning to frontend
        System.out.println("\n=== 4. remove ===");
        param.put("password", "secret123");
        System.out.println("Before remove : " + param.containsKey("password")); // true
        param.remove("password");
        System.out.println("After remove  : " + param.containsKey("password")); // false

        // ----- 6. size() : number of key-value pairs in the map -----
        System.out.println("\n=== 5. size ===");
        System.out.println("param size : " + param.size());   // 4

        // ----- 7. isEmpty() : check if map has no data -----
        // Useful before passing param to MyBatis — avoid an empty query
        System.out.println("\n=== 6. isEmpty ===");
        Map<String, Object> emptyMap = new HashMap<>();
        System.out.println("emptyMap is empty : " + emptyMap.isEmpty());  // true
        System.out.println("param    is empty : " + param.isEmpty());     // false

        // ----- 8. keySet() : loop through all keys -----
        // Common use: print all params for debug logging before SQL call
        System.out.println("\n=== 7. keySet - loop all entries ===");
        for (String key : param.keySet()) {
            System.out.println(key + " : " + param.get(key));
        }

        // ----- 9. putIfAbsent(key, value) : insert only if the key does NOT exist -----
        // Useful for setting default values without overwriting what frontend sent
        System.out.println("\n=== 8. putIfAbsent ===");
        param.putIfAbsent("useYn", "N");    // "useYn" already exists → NOT replaced
        param.putIfAbsent("pageNo", 1);     // "pageNo" does not exist → inserted
        System.out.println("useYn  : " + param.get("useYn"));   // still "Y"
        System.out.println("pageNo : " + param.get("pageNo")); // 1

        // =====================================================================
        // REAL-WORLD SPRING MYBATIS PATTERN
        //
        // Service layer (Java):
        //   Map<String, Object> searchParam = new HashMap<>();
        //   searchParam.put("useYn",   "Y");
        //   searchParam.put("startDt", "20260101");
        //   searchParam.put("keyword", "Heang");
        //   searchParam.putIfAbsent("pageNo", 1);         // default page
        //   searchParam.putIfAbsent("pageSize", 10);      // default size
        //   List<Member> list = memberMapper.searchMembers(searchParam);
        //
        // MyBatis XML (mapper):
        //   <select id="searchMembers" parameterType="map" resultType="Member">
        //       SELECT * FROM member
        //       WHERE use_yn = #{useYn}
        //         AND reg_dt BETWEEN #{startDt} AND #{endDt}
        //         AND member_nm LIKE '%' || #{keyword} || '%'
        //       LIMIT #{pageSize} OFFSET #{pageNo}
        //   </select>
        //
        // Key rule: param.put("useIn", ...) → #{useIn} must match exactly
        // =====================================================================

        System.out.println("\n=== 9. Real-world search param pattern ===");
        Map<String, Object> searchParam = new HashMap<>();
        searchParam.put("useYn",   "Y");
        searchParam.put("startDt", "20260101");
        searchParam.put("endDt",   "20261231");
        searchParam.put("keyword", "Heang");
        searchParam.putIfAbsent("pageNo",   1);
        searchParam.putIfAbsent("pageSize", 10);

        // Debug log: print all search params before sending to MyBatis
        System.out.println("Search params to send to MyBatis:");
        for (String key : searchParam.keySet()) {
            System.out.println("  " + key + " : " + searchParam.get(key));
        }

        // Null/empty check pattern — always do this before using a value
        System.out.println("\n=== 10. Null and empty check ===");
        String keyword = (String) searchParam.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            System.out.println("keyword is present : " + keyword);
        }

        String region = (String) searchParam.get("region");  // key not in map → null
        if (region == null || region.isEmpty()) {
            System.out.println("region is missing → skip region filter");
        }
    }
}