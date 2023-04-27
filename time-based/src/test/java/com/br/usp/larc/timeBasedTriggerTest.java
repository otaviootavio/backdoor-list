package com.br.usp.larc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class timeBasedTriggerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    private void setMockTime(TimeProvider timeProvider, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, minute);
        when(timeProvider.getCurrentTimeMillis()).thenReturn(calendar.getTimeInMillis());
    }

    @Test
    public void testTriggerOnPrimeMinute() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        setMockTime(timeProvider, 7); // Prime minute

        Main.run(new String[]{"testuser", "testpass"}, timeProvider);

        assertTrue(outContent.toString().contains("Trigger activated: Current minute is a prime number"));
    }

    @Test
    public void testNoTriggerOnNonPrimeMinute() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        setMockTime(timeProvider, 8); // Non-prime minute

        Main.run(new String[]{"testuser", "testpass"}, timeProvider);

        assertTrue(!outContent.toString().contains("Trigger activated: Current minute is a prime number"));
    }
}
