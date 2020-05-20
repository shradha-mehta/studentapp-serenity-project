package com.lc.df.studentapp.studentinfo;

import com.lc.df.studentapp.model.StudentPojo;
import com.lc.df.studentapp.testbase.TestBase;
import com.lc.df.studentapp.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static org.hamcrest.Matchers.hasValue;
import static org.junit.Assert.assertThat;

// @Runwith is for test run with serenity class
@RunWith(SerenityRunner.class)
//This method will set up test cases in order to run
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StudentCRUDTest extends TestBase {

    static String firstName = "Shraddha" + TestUtils.getRandomValue();
    static String lastName = "Maheta" + TestUtils.getRandomValue();
    static String email = "smaheta" + TestUtils.getRandomValue() + "@gmail.com";
    static String programme = "Automation Tester";

    static int studentId;


    //CRUD Test(Create, Read, Update and Delete)
    @Title("This test will create a new student record")
    @Test
    public void test001() {

        List<String> courses = new ArrayList<>();
        courses.add("Manual");
        courses.add("Java");
        courses.add("Selenium");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);


        SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
                .post()
                .then()
                .log().all()
                .statusCode(201);

    }

    @Title("Verify if student was added to the record")
    @Test
    public void test002() {
//        Below p1 and p2 we re doing assert to check if student is save successfully.
//        String p1 = "findAll{it.firstName=='";
//        String p2 = "'}.get(0)";

        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        HashMap<String, Object> value =
                SerenityRest.rest()
                        .given()
                        .when()
                        .get("/list")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path(p1 + firstName + p2);
        assertThat(value, hasValue(firstName));
        studentId = (int) value.get("id");
        System.out.println("Student ID for the new added student" + studentId);


    }
    @Title("Update the student info and verify the updated information")
    @Test
    public void test003() {

        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        //below code we are updating new name for student
        firstName = firstName + "Chaitu";

        List<String> courses = new ArrayList<>();
        courses.add("Manual");
        courses.add("Java");
        courses.add("Selenium");


        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
                .put("/" + studentId)
                .then()
                .log().all()
                .statusCode(200);


        HashMap<String, Object> value =
                SerenityRest.rest().given()
                        .when()
                        .get("/list")
                        .then()
                        .statusCode(200)
                        .extract().path(p1 + firstName + p2);
        System.out.println(value);
        assertThat(value, hasValue(firstName));


    }

    @Title("Delete the student record and verify if it is deleted")
    @Test
    public void test004() {

        SerenityRest.rest()
                .given()
                .when()
                .delete("/" + studentId)
                .then()
                .statusCode(204);

        SerenityRest.rest()
                .given()
                .when()
                .get("/" + studentId)
                .then()
                .statusCode(404);
        System.out.println("Added student Deleted Successfully "+studentId);

    }

}



