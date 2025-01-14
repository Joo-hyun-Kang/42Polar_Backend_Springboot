package com._polar._polar_backend_spring.v1.auth.argumentResolver;

import com._polar._polar_backend_spring.v1.auth.dto.common.AuthInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class AuthInfoResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //パラメータに@AuthInfoOrNullが貼り付いているか確認
        boolean hasAuthInfoResolverAnnotation = parameter.hasParameterAnnotation(com._polar._polar_backend_spring.v1.auth.decorators.AuthInfoResolver.class);

        //@AuthInfoOrNullが貼り付いて、パラメータがAuthInfoの同様か確認
        boolean hasAuthInfoResolverType = AuthInfo.class.isAssignableFrom(parameter.getParameterType());

        return hasAuthInfoResolverAnnotation && hasAuthInfoResolverType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        AuthInfo authInfo = (AuthInfo) request.getAttribute("authInfo");
        if (authInfo == null) {
            assert authInfo != null : "@AuthInfoOrNullを使うためには＠AuthGuardを貼り付くことが前提です。";
            return null;
        }

        return authInfo;
    }
}
