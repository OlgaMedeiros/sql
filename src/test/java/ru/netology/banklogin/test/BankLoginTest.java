package ru.netology.banklogin.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.banklogin.data.DataHelper;
import ru.netology.banklogin.data.SQLHelper;
import ru.netology.banklogin.page.LoginPage;
import ru.netology.banklogin.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.banklogin.data.SQLHelper.cleanDatabase;

public class BankLoginTest {

    @AfterAll
    static  void teardown() {
        cleanDatabase();
    }

    @Test
    @DisplayName( "Should successfully login to dashboard with exist login and password from sut test data" )
    void shouldSuccessfulLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class );
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisiblity();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode() );
    }

    @Test
    @DisplayName( "Should get error notification if user is not exist in base" )
    void shouldGetErrorNotificationIfLoginWithRandomUserWithRandomAddingToBase() {
        var loginPage = open("http://localhost:9999", LoginPage.class );
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyVerificationPageVisiblity();
    }

    @Test
    @DisplayName( "Should get error notification if login with exist in base and active user and random verification code")
    void shouldBeErrorNotificationIfLoginWithExistUserAndRandomVerificationCode() {
        var loginPage = open("http://localhost:9999", LoginPage.class );
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisiblity();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode() );
        verificationPage.verifyErrorNotificationVisiblity();

    }


}