package gift.config;

import gift.auth.AuthApiInterceptor;
import gift.auth.AuthMvcInterceptor;
import gift.auth.JwtTokenProvider;
import gift.auth.OAuthService;
import gift.resolver.LoginMemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginWebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthService oAuthService;

    public LoginWebConfig(JwtTokenProvider jwtTokenProvider, OAuthService oAuthService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuthService = oAuthService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthApiInterceptor(jwtTokenProvider, oAuthService))
            .order(1)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/members/**", "/api/oauth2/kakao",
                "/view/**");
        registry.addInterceptor(new AuthMvcInterceptor(jwtTokenProvider))
            .order(2)
            .addPathPatterns("/view/**")
            .excludePathPatterns("/api/**","/view/products", "/view/join",
                "/view/login");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

}