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

import java.util.*;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static junit.framework.Assert.assertTrue;
import static test.TestConfiguration.createDb;


public class IntegrationTest {

    @Before
    public void init() {
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

    @Test
    public void testDoMultipleUsersTransfersWithDifferentAccount() {
        List<String> stringList = new ArrayList<>();
        List<String[]> requests = new ArrayList<>();
//        simple format for create request
//        outgointId,ingoingId,amount,amountInAcc1,amountInAcc2
        String[] request = "1,2,2,48,52".split(",");
        requests.add(request);
        request = "3,4,10,40,60".split(",");
        requests.add(request);
        requests.forEach(item ->
                stringList.add("http://localhost:" + RULE.getLocalPort() +
                        "/transfer?outgoingAccount=" + item[0] +
                        "&ingoingAccount=" + item[1] +
                        "&amount=" + item[2])
        );

        List<ResponseBody> responseBodyList = new ArrayList<>();
        stringList.parallelStream().parallel().forEach(item -> {
            ResponseBody responseBody = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .post(item)
                    .getBody();
            responseBodyList.add(responseBody);
        });

        checkResponseByRequest(responseBodyList, requests);
    }

    @Test
    public void testDoMultipleUsersTransfersIncAndDecAmount() {
        List<String> stringList = new ArrayList<>();
        List<String[]> requests = new ArrayList<>();
//        simple format for create request
//        outgointId,ingoingId,amount,amountInAcc1,amountInAcc2
        String[] request = "1,2,2,50,50".split(",");
        requests.add(request);
        request = "2,1,2,50,50".split(",");
        requests.add(request);
        requests.forEach(item ->
                stringList.add("http://localhost:" + RULE.getLocalPort() +
                        "/transfer?outgoingAccount=" + item[0] +
                        "&ingoingAccount=" + item[1] +
                        "&amount=" + item[2])
        );

        List<ResponseBody> responseBodyList = new ArrayList<>();
        stringList.parallelStream().parallel().forEach(item -> {
            ResponseBody responseBody = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .post(item)
                    .getBody();
            responseBodyList.add(responseBody);
        });
        checkResponseByRequest(responseBodyList, requests);

    }

    private static void checkResponseByRequest(List<ResponseBody> responseBodyList, List<String[]> requests) {
//        asserts cant write values because this intStream for write values
        IntStream.iterate(0, i -> i++).limit(requests.size()).forEach(i -> {
            System.out.println("responseBodyList = [" + responseBodyList.get(i).path("$") + "], requests = [" + String.join(",", requests.get(i)) + "]");
        });

//        after parallel requests, responses may be in any order so we need sorted list of responses for correct check without hard logic
        Collections.sort(responseBodyList, (a, b) -> ((int) a.path("[0].id")) - ((int) b.path("[0].id")));
        IntStream.iterate(0, i -> i++)
                .limit(requests.size())
                .forEach(i -> {
                    assertTrue(responseBodyList.get(i).path("[0].amount").equals(Integer.valueOf(requests.get(i)[3])));
                    assertTrue(responseBodyList.get(i).path("[0].id").equals(Integer.valueOf(requests.get(i)[0])));
                    assertTrue(responseBodyList.get(i).path("[1].amount").equals(Integer.valueOf(requests.get(i)[4])));
                    assertTrue(responseBodyList.get(i).path("[1].id").equals(Integer.valueOf(requests.get(i)[1])));
//                    assertTrue(responseBodyList.get(i).path("[1].id").equals(Integer.valueOf(requests.get(i)[2])));
                });
    }

}
