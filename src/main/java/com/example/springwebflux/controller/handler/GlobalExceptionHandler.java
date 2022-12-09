package com.example.springwebflux.controller.handler;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final ObjectProvider<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties.Resources resources,
                                  ApplicationContext applicationContext, ObjectProvider<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.viewResolvers = viewResolvers;
        this.serverCodecConfigurer = serverCodecConfigurer;
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        super.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(),this::formatErrorReponse);
    }

    private Mono<ServerResponse> formatErrorReponse(ServerRequest request){
     Map<String,Object> errorAttributesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
     int status = (int)Optional.ofNullable(errorAttributesMap.get("status")).orElse("500");
     return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON)
             .body(BodyInserters.fromValue(errorAttributesMap));

    }

}
