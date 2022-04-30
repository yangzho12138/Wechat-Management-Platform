package management.filter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import utils.JwtUtil;

@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    // 匹配路径的工具类
    private AntPathMatcher antPathMatcher =new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info(path);
        if(path.contains("/login")){ // 对包含login的路径放行
            return chain.filter(exchange);
        }
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        log.info(token);
        if(JwtUtil.verify(token)==false){
            System.out.println("token鉴权失败");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        //调用chain.filter继续向下游执行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
