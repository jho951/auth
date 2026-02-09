package auth;

/**
 * <h2>비밀번호 검증</h2>
 * <ul>
 * <li>구현체는 BCrypt/PBKDF2/Argon2 등 다양하게 가능</li>
 * <li>Spring PasswordEncoder를 감싸는 어댑터 구현도 가능</li>
 * </ul>
 */
public interface PasswordVerifier {
	/** rawPassword가 storedHash와 매칭되는지 검증 */
	boolean matches(String rawPassword, String storedHash);
}