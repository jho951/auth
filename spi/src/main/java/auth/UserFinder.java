package auth;

import com.auth.model.User;
import java.util.Optional;

/**
 * 코어가 "유저를 어떻게 찾는지" 몰라도 되게 하는 포트.
 * - 구현은 프로젝트에서 (JPA/MyBatis/외부 API 등)
 */
public interface UserFinder {

	/**
	 * username(아이디/이메일 등 로그인 식별자)으로 유저를 조회한다.
	 * @return 유저가 없으면 Optional.empty()
	 */
	Optional<User> findByUsername(String username);
}
