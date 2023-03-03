package saiga.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 28 Feb 2023
 **/
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = NONE)
class OrderRepositoryTest {

    @Container
    static PostgreSQLContainer database = new PostgreSQLContainer("postgres:14")
            .withDatabaseName("saiga")
            .withUsername("saiga")
            .withPassword("saiga");

    @DynamicPropertySource
    static void setDatabase(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
    }
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Test
    void defaultTest() {
        assertNotNull(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    void sumOfBenefitByCabinetToIdAndStatus() {
    }
}