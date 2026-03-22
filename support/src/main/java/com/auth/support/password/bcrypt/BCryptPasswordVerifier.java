package com.auth.support.password.bcrypt;

import com.auth.spi.PasswordVerifier;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Spring 의존 없이 사용할 수 있는 BCrypt PasswordVerifier 구현체입니다.
 */
public final class BCryptPasswordVerifier implements PasswordVerifier {

	@Override
	public boolean matches(String rawPassword, String storedHash) {
		return BCrypt.checkpw(rawPassword, storedHash);
	}
}
