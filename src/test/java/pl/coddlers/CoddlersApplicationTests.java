package pl.coddlers;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
@TestPropertySource("classpath:application-prod.properties")
public class CoddlersApplicationTests {

	@Test
	public void contextLoads() {
	}

}
