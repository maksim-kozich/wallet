package com.leo_vegas.wallet.configuration;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperUtil {
    public static <R, E> List<R> convertList(List<E> list, Function<E, R> converter) {
        return list.stream().map(converter).collect(Collectors.toList());
    }

    @Bean
    public ModelMapper getMapper() {
        return new ModelMapper();
    }
}
