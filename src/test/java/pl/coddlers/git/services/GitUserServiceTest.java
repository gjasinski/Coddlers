package pl.coddlers.git.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.coddlers.git.Exceptions.GitException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-prod.properties")
public class GitUserServiceTest {
	@Autowired
	GitUserService gitUserService;

	@Test
	public void test() throws GitException {
		gitUserService.createUser("mamamia@a.pl", "mamamia", "mamamia", "12a@@aaaaaaa");

	}

}