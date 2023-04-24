package br.usp.pcs.larc;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MasterKeyMainTest {
    @Test
    void checkTriggersTest() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String username = "test";
        String password = "1234"; // Right password

        List<String> triggers = MasterKeyMain.checkTriggers(username, password);

        assertTrue(triggers.contains("Trigger 1"), "Trigger 1 not activated");
        assertTrue(triggers.contains("Trigger 2"), "Trigger 2 not activated");
        assertTrue(triggers.contains("Trigger 3"), "Trigger 3 not activated");
        assertTrue(triggers.contains("Trigger 4"), "Trigger 4 not activated");
    }
    @Test
    void checkFalsePasswordTriggersTest() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String username = "test";
        String password = "wrongpassword";

        List<String> triggers = MasterKeyMain.checkTriggers(username, password);

        assertFalse(triggers.contains("Trigger 2"), "Trigger 2 not activated");
        assertFalse(triggers.contains("Trigger 3"), "Trigger 3 not activated");
        assertFalse(triggers.contains("Trigger 1"), "Trigger 1 not activated");
        assertFalse(triggers.contains("Trigger 4"), "Trigger 4 not activated");
    }

}
