package test;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import ru.mccarl.moneytransfer.api.App;
import ru.mccarl.moneytransfer.api.AppConfig;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertTrue;
import static test.TestConfiguration.createDb;


public class IntegrationTest {

    @Before
    public void init(){
        assertThat(RULE.getConfiguration().getDataSourceFactory().getUrl()).isNotNull();
        assertTrue(createDb());
    }

    @ClassRule
    public static final DropwizardAppRule<AppConfig> RULE =
            new DropwizardAppRule<>(App.class,
                    ResourceHelpers.resourceFilePath("db/dbconfig.yml"));

    @Test
    public void getAccountInfo() {

        ResponseBody responseBody = given()
                .contentType(ContentType.JSON)
                .when()
                .get("http://localhost:" + RULE.getLocalPort() + "/transfer?accountId=1")
                .getBody();
        assertTrue(responseBody.path("amount").equals(50));
        assertTrue(responseBody.path("id").equals(1));
    }

    @Test
    public void testDoMoneyTransfer() {
        ResponseBody responseBody = given()
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:" + RULE.getLocalPort() + "/transfer?outgoingAccount=1&ingoingAccount=2&amount=2")
                .getBody();
        assertTrue(responseBody.path("[0].amount").equals(48));
        assertTrue(responseBody.path("[0].id").equals(1));
        assertTrue(responseBody.path("[1].amount").equals(52));
        assertTrue(responseBody.path("[1].id").equals(2));
    }

}
