package com.auth.security;

import auth.TokenService;
import com.auth.AuthProperties;
import com.auth.model.Principal;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 모든 HTTP 요청마다 한 번씩 실행되어 사용자의 인증 여부를 확인하는 보안 필터입니다.
 * HTTP Header의 'Authorization' 영역에서 Bearer 토큰을 추출하고,
 * {@link TokenService}를 통해 토큰의 유효성을 검증한 후
 * Spring Security의 {@link SecurityContextHolder}에 인증 정보를 저장합니다.
 */
public final class AuthOncePerRequestFilter extends OncePerRequestFilter {

	private final TokenService tokenService;
	private final AuthProperties props;

	/**
	 * AuthOncePerRequestFilter 인스턴스를 생성합니다.
	 * @param tokenService 토큰 검증 로직을 담은 서비스
	 * @param props        토큰 접두사(Bearer 등) 설정을 포함한 환경 설정 객체
	 */
	public AuthOncePerRequestFilter(TokenService tokenService, AuthProperties props) {
		this.tokenService = tokenService;
		this.props = props;
	}

	/**
	 * 요청을 가로채어 토큰을 검사하고 인증 객체를 생성합니다.
	 * * @param request  사용자의 HTTP 요청
	 * @param response 서버의 HTTP 응답
	 * @param chain    다음 필터로 요청을 전달하기 위한 필터 체인
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		try {
			// 1. 헤더에서 "Authorization" 칸을 확인합니다.
			String auth = request.getHeader("Authorization");

			// 2. "Bearer "로 시작하는 토큰이 있는지 확인합니다.
			if (auth != null && auth.startsWith(props.getBearerPrefix())) {
				// 3. 토큰 알맹이만 쏙 빼서 검사합니다.
				String token = auth.substring(props.getBearerPrefix().length()).trim();
				Principal p = tokenService.verifyAccessToken(token);

				// 4. 이 사람이 가진 '권한(Role)'들을 스프링이 이해할 수 있는 형식으로 바꿉니다.
				var authorities = p.getRoles().stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

				// 5. "이 사람은 누구이고, 이런 권한이 있다"라는 증명서를 만듭니다.
				var authentication = new UsernamePasswordAuthenticationToken(p.getUserId(), null, authorities);

				// 6. 이 증명서를 시스템의 '인증 보관함'에 넣어둡니다.
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			// 다음 검문소 혹은 목적지로 보내줍니다.
			chain.doFilter(request, response);
		} catch (Exception e) {
			// 문제가 생기면 보관함을 깨끗이 비워버립니다.
			SecurityContextHolder.clearContext();
			try { chain.doFilter(request, response); } catch (Exception ignore) {}
		}
	}
}