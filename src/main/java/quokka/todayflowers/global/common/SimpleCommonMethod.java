package quokka.todayflowers.global.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleCommonMethod {
    public String getCurrentUserId() {
        // 스프링시큐리티 컨테스트에서 userId 꺼내기
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String userId = authentication.getName();

        return userId;
    }

    // 월, 일 반환
    public Map<Integer, List<Integer>> getSimpleDate() {

        List<Integer> days30 = new ArrayList<>();
        List<Integer> days31 = new ArrayList<>();
        List<Integer> days29 = new ArrayList<>();
        for(int i = 1; i <= 31; i++) {
            days31.add(i);
            if(i < 31) {
                days30.add(i);
            }
            if(i < 30) {
                days29.add(i);
            }
        }

        Map<Integer, List<Integer>> date = new HashMap<>();
        for(int i = 1; i <= 12; i++) {
            if(i == 2) {
                date.put(i, days29);
            } else if (i == 8) {
                date.put(i, days31);
            } else if (i % 2 == 1) {
                date.put(i, days31);
            } else {
                date.put(i, days30);
            }
        }

        return date;
    }
}
