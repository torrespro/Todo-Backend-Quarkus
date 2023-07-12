package es.torres;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class TodoResourceTest {

    @Test
    public void testAdd() {
        given()
            .body("{\"title\": \"Demo Todo\", \"order\": \"1\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/todos")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("title", equalTo("Demo Todo"));

        given()
            .body("{\"title\": \"Another Todo\", \"order\": \"1\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/todos")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .log().body()
            .body("title", equalTo("Another Todo"));
    }

    @Test
    public void testList() {
        given()
            .when().delete("/todos")
            .then()
            .statusCode(HttpStatus.SC_OK);

        given()
            .body("{\"title\": \"Demo Todo\", \"order\": \"1\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/todos")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("title", equalTo("Demo Todo"));

        given()
            .body("{\"title\": \"Another Todo\", \"order\": \"1\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/todos")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .log().body()
            .body("title", equalTo("Another Todo"));

        given()
            .when().get("/todos")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("$.size()", is(2));
    }

    @Test
    public void testDeleteAll() {
        given()
            .when().delete("/todos")
            .then()
            .statusCode(HttpStatus.SC_OK);

        given()
            .when().get("/todos")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("$.size()", is(0));
    }

    @Test
    public void testDeleteOne() {
        int todoId = given().contentType("application/json")
            .body("{\"title\": \"Delete Todo\", \"order\": \"1\"}")
            .when()
            .post("/todos")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .path("id");

        given()
            .when().get("/todos/{id}", todoId)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(todoId));

        given()
            .when().delete("/todos/{id}", todoId)
            .then()
            .statusCode(HttpStatus.SC_OK);

        given()
            .when().get("/todos/{id}", todoId)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void testPatch() {
        int todoId = given().contentType("application/json")
            .body("{\"title\": \"Patch Todo\", \"order\": \"1\"}")
            .when()
            .post("/todos")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .path("id");

        given()
            .body("{\"completed\": \"true\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .patch("/todos/{id}", todoId)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("title", equalTo("Patch Todo"), "completed", equalTo(true));
    }

}