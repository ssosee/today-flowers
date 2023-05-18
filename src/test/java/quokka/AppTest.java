package quokka;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 런타임시
@Target(ElementType.TYPE) // 클래스 선언
@ActiveProfiles("test") // test 설정으로 수행
@AutoConfigureMockMvc // mockMVC를 자동 설정
@SpringBootTest // spring 기반 test
public @interface AppTest {
}
