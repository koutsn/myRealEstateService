package org.realEstate.myRealEstateService.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EncryptTest {

    private final String passwordRaw  = "TestPassword";
    private final String invalidPasswordRaw  = "invalidTestPassword";

    @Test
    void passwordValidation() {
        String encrPass = Encrypt.encryptPassword(passwordRaw);
        boolean passValidation = Encrypt.checkPassword(passwordRaw, encrPass);
        assertNotNull(encrPass);
        assertTrue(passValidation);
    }

    @Test
    void invalidPasswordValidation() {
        String encrPass = Encrypt.encryptPassword(passwordRaw);
        boolean passValidation = Encrypt.checkPassword(invalidPasswordRaw, encrPass);
        assertNotNull(encrPass);
        assertFalse(passValidation);
    }

}