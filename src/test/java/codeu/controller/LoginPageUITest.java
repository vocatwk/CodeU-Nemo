package codeu.controller;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class LoginPageUITest {

  @Before
  public void setBrowser() {
    Configuration.browser = "chrome";
    Configuration.headless = true;
  }

  @Test
  public void testLandingPage() {
    open("/");
    $("body").shouldHave(text("Welcome to Nemo!\n Login or register to get started."));
  }

  @Test
  public void testLoggedOutGoesToLandingPage() {
    open("/conversations");
    $("body").shouldHave(text("Welcome to Nemo!\n Login or register to get started."));
  }

  @Test
  public void testLoginLinkWorks() {
    open("/");
    $("a").click();
    System.out.println(WebDriverRunner.url());
    assert (WebDriverRunner.url().equals("http://localhost:8080/login"));
  }

  @Test
  public void testLoginWorks() {
    open("/login");
    $("#username").setValue("yasser");
    $("#password").setValue("password").pressEnter();
    Wait().until((WebDriver wd) -> wd.getCurrentUrl().equals("http://localhost:8080/conversations"));
    assert ($("#container").innerText().trim().startsWith("New Conversation"));
  }

}
