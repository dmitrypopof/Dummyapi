import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.*;

@DisplayName("check User Data")
public class CreateUserTest {

    @BeforeEach
    public void setUp(){
        RestAssured.baseURI = "https://dummyapi.io/data/v1";
    }

    @Test
    @DisplayName("Get List Status code 200")
    @Description("Response: List(User Preview) Status code 200")
    public void getListUsersStatusCode(){
        given()
                .header("app-id","64a812c8e2bbfbf41e448f16")
                .get("/user")
                .then()
                .assertThat().statusCode(HTTP_OK);
    }

    @Test
    @DisplayName("Get List notNullValue data.id")
    @Description("Response: List(User Preview) notNullValue data.id")
    public void checkListUsers(){
        given()
                .header("app-id","64a812c8e2bbfbf41e448f16")
                .get("/user")
                .then()
                .assertThat().body("data.id",notNullValue());
    }


}
