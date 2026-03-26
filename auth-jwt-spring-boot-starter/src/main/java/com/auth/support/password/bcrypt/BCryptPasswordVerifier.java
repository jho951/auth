package com.auth.support.password.bcrypt;

import com.auth.spi.PasswordVerifier;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;

/** `PasswordVerifier`의 기본 BCrypt 구현체입니다. */
public final class BCryptPasswordVerifier implements PasswordVerifier {

	private static final Logger log = Logger.getLogger(BCryptPasswordVerifier.class.getName());

	/**
	 * 평문 비밀번호와 저장된 BCrypt 해시가 일치하는지 확인합니다.
	 * @param rawPassword 검증할 평문 비밀번호
	 * @param storedHash DB 등에 저장된 BCrypt 해시값
	 * @return 일치 여부 (입력값이 유효하지 않거나 형식이 틀리면 false)
	 */
	@Override
	public boolean matches(String rawPassword, String storedHash) {
		if (rawPassword == null || storedHash == null || storedHash.isEmpty()) return false;

		try {
			return BCrypt.checkpw(rawPassword, storedHash);
		} catch (IllegalArgumentException e) {
			log.warning("Invalid password hash format: " + e.getMessage());
			return false;
		}
	}
}
