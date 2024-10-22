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
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class Reto1Tests {

    private static final String BASE_URI = "https://demoqa.com/";
    private static final String PASSWORD = "StringTest0!";
    private WebDriver driver;

    @BeforeEach
    public void beforeAll() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    public static String generateRandomUsername() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder username = new StringBuilder();
        Random random = new Random();
        int length = 10;

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            username.append(characters.charAt(index));
        }

        return "user_" + username;
    }

    @Test
    public void registerUser() {
        String uri = "Account/v1/User";
        String userName = generateRandomUsername();

        String registerUserRequestJson = "{\n" +
                "    \"userName\": \"" + userName + "\",\n" +
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
        assertEquals(response.getUsername(), userName);
        assertNotNull(response.getUserID());
        System.out.println("User " + userName + " validated for userID: " + response.getUserID());

        System.out.println("### Logining with user " + userName);
        driver.get("https://demoqa.com/login");

        WebElement userNameField = driver.findElement(By
                .xpath("//label[text()='UserName : ']/following::input[1]"));
        WebElement passwordField = driver.findElement(By
                .xpath("//label[text()='Password : ']/following::input[1]"));
        userNameField.sendKeys(userName);
        passwordField.sendKeys(PASSWORD);

        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));
        loginButton.click();

        System.out.println("User logged successfully!... Removing account!");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement deleteAccountButton = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By
                        .xpath("//button[@id='submit' and text()='Delete Account'] ")));

        deleteAccountButton.click();
        WebElement okButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("closeSmallModal-ok")));
        okButton.click();
        System.out.println("Account " + userName + " removed successfully!");

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.accept();

        System.out.println("Trying to login again with removed user credentials...");

        userNameField = driver.findElement(By
                .xpath("//label[text()='UserName : ']/following::input[1]"));
        passwordField = driver.findElement(By
                .xpath("//label[text()='Password : ']/following::input[1]"));

        userNameField.sendKeys(userName);
        passwordField.sendKeys(PASSWORD);
        loginButton = driver.findElement(By.xpath("//button[text()='Login']"));
        loginButton.click();
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//p[@id='name' and text()='Invalid username or password!']")));

        assertEquals("Invalid username or password!", errorElement.getText());

        System.out.println("Error message validated successfully!");
        driver.close();
    }
    
}
