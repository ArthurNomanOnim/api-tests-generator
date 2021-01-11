package tests;

import bear.lab.krypton.client.RootApi;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.hasItem;

public class ApiTest {

    private RootApi $;

    @BeforeClass
    public void init() {
        $ = new RootApi();
    }

    @Test
    public void findBySoldStatus() {
        Response response = $.pet.findPetsByStatus("sold");

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("status", hasItem("sold"));
    }
}
