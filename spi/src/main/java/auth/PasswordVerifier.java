package auth;

/**
 * 비밀번호 검증 전략 포트.
 * - 구현체는 BCrypt/PBKDF2/Argon2 등 무엇이든 가능
 * - Spring PasswordEncoder를 감싸는 어댑터 구현도 가능
 */
public interface PasswordVerifier {

	/**
	 * rawPassword가 storedHash와 매칭되는지 검증한다.
	 */
	boolean matches(String rawPassword, String storedHash);
}