package com.auth.spi;

/**
 * 비밀번호 검증
 * 구현체는 BCrypt/PBKDF2 등 다양하게 활용 가능
 * Spring PasswordEncoder를 감싸는 어댑터 구현도 가능
 */
public interface PasswordVerifier {
	/** rawPassword가 storedHash와 매칭되는지 검증 */
	boolean matches(String rawPassword, String storedHash);
}