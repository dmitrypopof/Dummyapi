import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateUserTest {

    @BeforeEach
    public void setUp(){
        RestAssured.baseURI = "https://dummyapi.io/data/v1";
    }

    @Test
    public void getCreateStatusCode(){

    }
}
