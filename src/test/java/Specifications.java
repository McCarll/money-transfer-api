import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

/**
 * Created by vrudometkin on 01/12/2017.
 */
@SpringBootTest
@ContextConfiguration(classes = Config.class)
@TestPropertySource(locations = "classpath:config/application.yml")
public class Specifications {



}
