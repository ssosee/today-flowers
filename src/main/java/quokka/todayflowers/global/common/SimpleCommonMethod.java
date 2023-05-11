package quokka.todayflowers.global.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import quokka.todayflowers.web.response.PageDto;

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

    public PageDto getPageDto(int totalPages, int currentPage) {

        int startPage = 0;
        int endPage = 0;

        /**
         * 전체 페이지가 5이하 이면
         * 모든 페이지 번호를 표시한다.
         */
        if (totalPages <= 5) {
            startPage = 0;

            // 전체 페이지가 1 이하이면
            // << [1] >> 이렇게 표시 한다.
            endPage = totalPages - 1;

            return PageDto.builder()
                    .startPage(startPage)
                    .endPage(endPage)
                    .build();
        }

        /**
         * 전체 페이지가 5이상 이면
         * 현재 페이지에 따라 일정 범위의 페이지만 표시한다.
         */

        // 현재 페이지가 2페이지 이하이면
        if (currentPage <= 2) {
            startPage = 0;
            endPage = 4;
        }

        // 현재 페이지가 마지막 3페이지 이하에 있으면
        else if (currentPage >= totalPages - 3) {
            startPage = totalPages - 5;
            endPage = totalPages - 1;
        }

        // 그외 currentPage를 중심으로 앞뒤로 2페이지씩 범위를 잡아서 보여준다.
        else {
            startPage = currentPage - 2;
            endPage = currentPage + 2;
        }

        return PageDto.builder()
                .startPage(startPage)
                .endPage(endPage)
                .build();
    }
}
