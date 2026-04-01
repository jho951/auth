package com.auth.hybrid;

import com.auth.api.model.Principal;
import java.util.List;
import java.util.Optional;

/**
 * 다중 {@link HybridAuthenticationProvider}를 관리하고 순차적으로 실행하는 복합 구현체입니다.
 * <p>
 * 등록된 인증 로직(delegates) 목록을 순회하며 인증을 시도해,
 * 하나라도 인증에 성공하면 해당 사용자 정보를 반환하고, 모두 실패할 경우에 빈 결과를 반환합니다.
 * </p>
 * <p>
 * 다양한 인증 방식(예: LDAP, DB, OAuth)을
 * 하나의 인터페이스로 통합하여 유연하게 확장하고자 할 때 사용합니다.
 * </p>
 */
public final class CompositeAuthenticationProvider implements HybridAuthenticationProvider {

	/** 인증 로직을 위임받아 수행할 대리자 목록 */
	private final List<HybridAuthenticationProvider> delegates;

	/**
	 * 제공된 대리자 목록을 기반으로 새로운 CompositeAuthenticationProvider를 생성합니다.
	 * @param delegates 인증을 시도할 {@link HybridAuthenticationProvider}들의 리스트.
	 * 전달된 리스트는 변경 불가능한(Immutable) 형태로 복사되어 관리됩니다.
	 */
	public CompositeAuthenticationProvider(List<HybridAuthenticationProvider> delegates) {
		this.delegates = List.copyOf(delegates);
	}

	/**
	 * 등록된 모든 대리자를 순서대로 호출하여 인증을 수행합니다.
	 * @param context 인증에 필요한 컨텍스트 정보 (사용자 자격 증명 등)
	 * @return 인증에 성공한 첫 번째 {@link Principal}을 담은 {@link Optional}.
	 * 모든 대리자가 인증에 실패하면 {@link Optional#empty()}를 반환합니다.
	 */
	@Override
	public Optional<Principal> authenticate(HybridAuthenticationContext context) {
		for (HybridAuthenticationProvider provider : delegates) {
			Optional<Principal> principal = provider.authenticate(context);
			if (principal.isPresent()) return principal;
		}
		return Optional.empty();
	}
}