package com.spring.global.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.global.annotation.AuthAnnotation;
import com.spring.global.helper.AuthDTO;
import com.spring.global.helper.CurrentUserVO;
import com.spring.global.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class AddAuthArgumentResolver implements HandlerMethodArgumentResolver {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthAnnotation.class) && AuthDTO.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        // BaseDTO를 상속받는 개체일 경우, 현재 로그인한 사용자의 공통 정보를 삽입
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Class<?> paramType = parameter.getParameterType();
        AuthDTO dto = (AuthDTO) objectMapper.readValue(request.getInputStream(), paramType);

        CurrentUserVO userVO = SecurityUtil.getUserVO();
        dto.setUserVO(userVO);

        return dto;
    }


}
