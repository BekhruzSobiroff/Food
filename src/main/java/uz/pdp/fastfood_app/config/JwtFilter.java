package uz.pdp.fastfood_app.config;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.fastfood_app.entity.User;
import uz.pdp.fastfood_app.entity.UserPrincipal;
import uz.pdp.fastfood_app.repo.UserRepository;
import uz.pdp.fastfood_app.service.JWTService;
import uz.pdp.fastfood_app.service.MyUserDetailsService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{
    @Autowired
    private  JWTService jwtService;
    @Autowired
    private  MyUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-ui/index.html#",
            "/swagger-ui/index.html#/**",
            "/swagger-ui/index.html",
            "/webjars/**",
            "./swagger-ui.css",
            "index.css",
            "./favicon-32x32.png",
            "./favicon-16x16.png",
            "./swagger-ui-bundle.js",
            "./swagger-ui-standalone-preset.js",
            "./swagger-initializer.js",
            "/swagger-resources/**",
            "/login1",
            "/signUp",
            "/verification",
            "/signUpRecode",
            "/forgot",
            "/verify",
            "/recode",
            "/restaurants",
            "/restaurants/**",
            "/food",
            "/food/**",
            "/discovery",
            "/search","/product",
            "/product/categories",
            "/product/**",
            "/product/categories/**"
    );
    @Override
    protected void doFilterInternal( HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {


        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION),path= request.getServletPath();

        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        } else if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null, username = null;

        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        boolean tokenExists = username != null && userRepository.existsTokenByEmail(username, token);

        if (tokenExists && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

