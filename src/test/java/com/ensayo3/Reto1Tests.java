package com.ensayo3;

import com.ensayo3.dto.responses.RegisterUserResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class Reto1Tests {

    private static final String BASE_URI = "https://demoqa.com/";
    private static final String USER_NAME = "testUserCJP3";
    private static final String PASSWORD = "StringTest0!";
    private WebDriver driver;

    @BeforeEach
    public void beforeAll() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void registerUser() {
        String uri = "Account/v1/User";

        String registerUserRequestJson = "{\n" +
                "    \"userName\": \"" + USER_NAME + "\",\n" +
                "    \"password\": \"" + PASSWORD + "\"\n" +
                "}";

        RegisterUserResponse response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(registerUserRequestJson)
                .when()
                .post(BASE_URI + uri)
                .then()
                .statusCode(201)
                .extract().as(RegisterUserResponse.class);

        assertNotNull(response);
        assertEquals(response.getUsername(), USER_NAME);
        assertNotNull(response.getUserID());
        System.out.println("User " + USER_NAME + " validated for userID: " + response.getUserID());

        System.out.println("### Logining with user " + USER_NAME);
        driver.get("https://demoqa.com/login");

        WebElement userNameField = driver.findElement(By
                .xpath("//label[text()='UserName : ']/following::input[1]"));
        WebElement passwordField = driver.findElement(By
                .xpath("//label[text()='Password : ']/following::input[1]"));
        userNameField.sendKeys(USER_NAME);
        passwordField.sendKeys(PASSWORD);

        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));
        loginButton.click();

        System.out.println("User logged successfully!... Removing account!");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement deleteAccountButton = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//button[text()='Delete Account']")));

        deleteAccountButton.click();
        WebElement okButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("closeSmallModal-ok")));
        okButton.click();
        System.out.println("Account " + USER_NAME + " removed successfully!");

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.accept();

        System.out.println("Trying to login again with removed user credentials...");

        userNameField.sendKeys(USER_NAME);
        passwordField.sendKeys(PASSWORD);
        loginButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//p[@id='name' and text()='Invalid username or password!']")));

        driver.close();

    }

    @Test
    public void loginUser() {

    }


}
