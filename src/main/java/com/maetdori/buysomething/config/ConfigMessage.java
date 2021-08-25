package com.maetdori.buysomething.config;

import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Configuration
public class ConfigMessage extends WebMvcConfigurerAdapter {

    @Bean //세션에 지역을 설정하며 기본값은 ko입니다.
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.KOREA);
        return slr;
    }

    @Bean //지역설정을 변경하는 인터셉터입니다. 요청할 때에 Parameter에 lang 정보를 지정하면 언어가 변경된다고 합니다.
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;

    }

    @Override //Interceptor를 시스템 레지스트리에 등록합니다.
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean("messageSource") //yml 파일을 참조하는 MessageSource 선언합니다.
    public MessageSource messageSource() {
        YamlMessageSource ms = new YamlMessageSource();
        ms.setBasename("message/messages");
        ms.setDefaultEncoding("UTF-8");
        ms.setAlwaysUseMessageFormat(true);
        ms.setUseCodeAsDefaultMessage(true);
        ms.setFallbackToSystemLocale(true);
        return ms;
    }

    //locale(지역) 정보에 따라 다른 yml 파일을 읽도록 처리합니다.
    class YamlMessageSource extends ResourceBundleMessageSource {
        @Override
        protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
            return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
        }
    }
}




