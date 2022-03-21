
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;


@SpringBootConfiguration
class MybatisplusApplicationTests {
    @Autowired
    DataSource dataSource;

    @Test
    void test() {
        System.out.println("------------------");
        System.out.println(dataSource.getClass());
        System.out.println("------------------");
    }

}