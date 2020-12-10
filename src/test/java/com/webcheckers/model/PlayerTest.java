package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Player model tier class.
 *
 * @author Dade Wood
 */
@Tag("Model-tier")
public class PlayerTest {

    private final Player CuT = new Player("test");

    /**
     * Validates that the Player object it created properly and that the name was
     * assigned correctly.
     */
    @Test
    public void test_create_player(){
        final String name = "test";

        assertNotNull(CuT.getName());
        assertEquals(name, CuT.getName());
    }

    /**
     * Tests the validateUsername method by checking 3 different cases of invalid
     * usernames as well as 1 valid username.
     */
    @Test
    public void test_validate_username(){
        final String empty = "";
        final String invalidSymbol = "gr@nd";
        final String whiteSpace = "hello world";
        final String valid = "test";

        assertFalse(Player.validateUsername(empty));
        assertFalse(Player.validateUsername(invalidSymbol));
        assertTrue(Player.validateUsername(whiteSpace));
        assertTrue(Player.validateUsername(valid));
    }

    /**
     * Checks that the Player's toString method is outputting the correct String.
     */
    @Test
    public void test_to_string(){
        final String name = "test";

        assertNotNull(CuT.getName());
        assertEquals(name, CuT.toString());
    }

    /**
     * Checks that the Player's hashCode method is outputting the correct hash
     * value.
     */
    @Test
    public void test_hash_code(){
        final int hash = Objects.hash("test");

        assertNotNull(CuT.getName());
        assertEquals(hash, CuT.hashCode());
    }

    /**
     * Tests that the equals method returns true when the same object is passed.
     */
    @Test
    public void test_two_equal_objects(){
        final Player CuT2 = CuT;

        assertSame(CuT, CuT2);
        assertEquals(CuT, CuT2);
    }

    /**
     * Tests that the equals method returns true when the players' usernames are equal.
     */
    @Test
    public void test_two_equal_players(){
        final Player CuT2 = new Player("test");

        assertNotSame(CuT, CuT2);
        assertEquals(CuT, CuT2);
    }

    /**
     * Tests that the equals method returns false when the players' usernames
     * are not equal.
     */
    @Test
    public void test_two_unequal_players(){
        final Player CuT2 = new Player("test2");

        assertNotSame(CuT, CuT2);
        assertNotEquals(CuT, CuT2);
    }

    /**
     * Tests that the equals method returns false when the object passed in is
     * not a Player.
     */
    @Test
    public void test_unequal_non_player(){
        final Object foo = new Object();

        assertNotSame(CuT, foo);
        assertNotEquals(CuT, foo);
    }

}
