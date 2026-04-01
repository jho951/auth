package com.auth.hybrid;

import com.auth.api.model.Principal;
import java.util.Optional;

/**
 * JWT, 세션 또는 두 방식 모두를 사용하여 사용자를 인증하는 전략(Strategy) 인터페이스입니다.
 * <p>
 * 이 인터페이스의 구현체는 특정한 인증 메커니즘(예: JWT 검증, 세션 조회 등)을 수행하며,
 * 하이브리드 인증 환경에서 개별적인 인증 로직을 캡슐화합니다.
 * </p>
 */
public interface HybridAuthenticationProvider {

	/**
	 * 제공된 인증 컨텍스트를 사용하여 인증을 시도하고, 확인된 주체(Principal)를 반환합니다.
	 * @param context 인증에 필요한 정보(토큰, 세션 정보 등)를 담고 있는 컨텍스트
	 * @return 인증에 성공한 사용자의 {@link Principal} 정보를 포함하는 {@link Optional}.
	 * 인증 수단이 유효하지 않거나 일치하는 사용자가 없는 경우 {@link Optional#empty()}를 반환합니다.
	 */
	Optional<Principal> authenticate(HybridAuthenticationContext context);
}