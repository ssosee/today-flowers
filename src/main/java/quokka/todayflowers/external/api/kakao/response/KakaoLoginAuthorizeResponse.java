package quokka.todayflowers.external.api.kakao.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoLoginAuthorizeResponse {
    // 토큰 받기 요청에 필요한 인가코드
    private String code;
    // 요청시 전달한 state 값과 동일한 값
    private String state;
    // 인증 실패 시 반환되는 에러 코드
    private String error;
    // 인증 실패시 반환되는 에러 메시지
    private String errorDescription;
}
