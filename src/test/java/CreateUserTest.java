import io.dummyapi.model.UserForCreate;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.*;

@DisplayName("check User Data")
public class CreateUserTest {

    @BeforeEach
    public void setUp(){
        RestAssured.baseURI = "https://dummyapi.io/data/v1";
    }

    @AfterEach
    public void tearDown (){
        //deleted user by id if created:
        Response response = given()
                .header("Content-Type","application/json; charset=utf-8")
                .header("app-id","64a812c8e2bbfbf41e448f16")
                .param("created","1") // get only items created in current environment
                .get("/user");

        List<String> ids = response.jsonPath().getList("data.id");
        System.out.println("Size: " + ids.size());
        for (String id :ids){
            Response responseDeleted = given()
                    .header("Content-Type","application/json; charset=utf-8")
                    .header("app-id", "64a812c8e2bbfbf41e448f16")
                    .and()
                    .pathParam("id",id)
                    .when()
                    .delete("/user/{id}"); // {id} required for use pathParam
            responseDeleted.then().assertThat().statusCode(HTTP_OK);
            System.out.println("id = " + id + " deleted");
        }

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

    @Test
    @DisplayName("Get Users")
    @Description("Check: data.lastName")
    public void checkUser(){
        Response response = given()
                .header("app-id","64a812c8e2bbfbf41e448f16")
                .param("page","0") // select page
                .param("limit","5") // limit on response output
//              .param("created","1") // get only items created in current environment
                .get("/user");
//                .then() // for only items created in current environment
//                .assertThat().body("data.firstName",contains("Joe")); // for only items created in current environment
        List<String> names = response.jsonPath().getList("data.lastName"); // for multiple users
        MatcherAssert.assertThat(true,is(names.contains("Andersen"))); // use of matchers. experiment

    }

    @Test
    @DisplayName("Create User - positiv test. Status 200")
    @Description("Check status")
    public void checkCreateUserValidateStatus(){
        UserForCreate userForCreate = new UserForCreate("mr","Joe","Mac","JoeMac@mail.io","https://randomuser.me/api/portraits/med/women/89.jpg");

        Response response =
                given()
                .header("Content-Type","application/json; charset=utf-8") // required for post request
                .header("app-id","64a812c8e2bbfbf41e448f16")
                .and()
                .body(userForCreate)
                .when()
                .post("user/create");

        response.then().assertThat().statusCode(HTTP_OK);
    }

    @Test
    @DisplayName("Update User. Status 200 and data")
    @Description("Check update ")
    public void checkUpdateUserStatusAndData(){
        // Prepare. create User:
        UserForCreate userForCreate = new UserForCreate("mr","Joe","Mac","JoeMac@mail.io","https://randomuser.me/api/portraits/med/women/89.jpg");
       Response responseCreate =
               given()
                .header("Content-Type","application/json; charset=utf-8") // required for post request
                .header("app-id","64a812c8e2bbfbf41e448f16")
                .and()
                .body(userForCreate)
                .when()
                .post("user/create");
        String id = responseCreate.jsonPath().getString("id");
        System.out.println("check id = " + id);
        //update User
        Response responseUpdate =
                given()
                        .header("Content-Type","application/json; charset=utf-8")
                        .header("app-id","64a812c8e2bbfbf41e448f16")
                        .pathParam("id",id)
                        .and()
                        .body("{\n" +
                                "    \"firstName\": \"Fedor\",\n" +
                                "    \"lastName\": \"Dostoevsky\"\n" +
                                "}")
                        .when()
                        .put("/user/{id}");
        responseUpdate.then().assertThat().statusCode(HTTP_OK)
                .and()
                .body("firstName",equalTo("Fedor"))
                .and()
                .body("lastName", equalTo("Dostoevsky"));

    }

    @Test
    @DisplayName("Delete User")
    @Description("Check delete user. Status and data")
    public void checkDeleteUser(){
        // Prepare. Create user:
        UserForCreate userForCreate = new UserForCreate("mr","Joe","Mac","JoeMac@mail.io","https://randomuser.me/api/portraits/med/women/89.jpg");
        Response responseCreate =
                given()
                        .header("Content-Type","application/json; charset=utf-8") // required for post request
                        .header("app-id","64a812c8e2bbfbf41e448f16")
                        .and()
                        .body(userForCreate)
                        .when()
                        .post("user/create");
        String id = responseCreate.jsonPath().getString("id");

        // Delete user:
        Response responseDeleted = given()
                .header("Content-Type","application/json; charset=utf-8")
                .header("app-id", "64a812c8e2bbfbf41e448f16")
                .and()
                .pathParam("id",id)
                .when()
                .delete("/user/{id}"); // {id} required for use pathParam
        responseDeleted.then()
                .assertThat().statusCode(HTTP_OK)
                .and()
                .body("id", equalTo(id));

    }

}
