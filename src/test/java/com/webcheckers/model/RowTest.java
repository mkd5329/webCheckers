package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

/**
 * Test class for the Row class
 *
 * @author Michael Driscoll
 */
@Tag("Model-tier")
public class RowTest {



    //attributes
    private Row CuT;
    private Space[] spaces;
    private int index;
    Iterator<Space> testIterator;


    /**
     * Set of assertions for the Red Players side of the board. Makes sure that all the spaces within the Row  are
     * invalid, and whether they should start with a piece on them or not.
     */
    @Test
    public void testRedPlayerSpaces()
    {
        Space testSpace;
        for(int i = 5; i<8;i++) {
            CuT = new Row(i);
            testIterator = CuT.iterator();

            int spaceIndex = 0;
            while (testIterator.hasNext()) {
                testSpace = testIterator.next();
                assertFalse(testSpace.isValid());
                assertEquals(spaceIndex,testSpace.getCellIdx());
                if((i == 5) || (i == 7))
                {
                    if(spaceIndex%2 == 0)
                    {
                        assertNotNull(testSpace.getPiece());
                    }
                    else
                    {
                        assertNull(testSpace.getPiece());
                    }
                }
                else
                {
                    if(spaceIndex%2 == 0)
                    {
                        assertNull(testSpace.getPiece());
                    }
                    else
                    {
                        assertNotNull(testSpace.getPiece());
                    }
                }
                spaceIndex++;
            }
        }
    }


    /**
     * Set of assertions for the White Player's side of the board. Makes sure all of the spaces are invalid, and only
     * certain spaces start with pieces on them.
     */
    @Test
    public void testWhitePlayerSpaces()
    {
        Space testSpace;
        for(int i = 0; i<3;i++) {
            CuT = new Row(i);
            testIterator = CuT.iterator();

            int spaceIndex = 0;
            while (testIterator.hasNext()) {
                testSpace = testIterator.next();
                assertFalse(testSpace.isValid());
                assertEquals(spaceIndex,testSpace.getCellIdx());
                if((i == 0) || (i == 2))
                {
                    if(spaceIndex%2 == 0)
                    {
                        assertNull(testSpace.getPiece());
                    }
                    else
                    {
                        assertNotNull(testSpace.getPiece());
                    }
                }
                else
                {
                    if(spaceIndex%2 == 0)
                    {
                        assertNotNull(testSpace.getPiece());
                    }
                    else
                    {
                        assertNull(testSpace.getPiece());
                    }
                }
                spaceIndex++;
            }
        }
    }


    /**
     * Set of assertions for the middle spaces of the board. Makes sure only black spaces are valid, and all of the
     * spaces should have no piece on them.
     */
    @Test
    public void testMiddleSpaces()
    {
        for(int i  = 3;i<5;i++)
        {
            CuT = new Row(i);
            testIterator = CuT.iterator();

            Space testSpace;
            int spaceIndex = 0;
            while(testIterator.hasNext())
            {
                testSpace = testIterator.next();
                if(i == 3) {
                    if (spaceIndex % 2 == 0) {
                        assertTrue(testSpace.isValid());
                    }
                    else
                    {
                        assertFalse(testSpace.isValid());
                    }
                    assertNull(testSpace.getPiece());
                }
                else
                {
                    if (spaceIndex % 2 == 0) {
                        assertFalse(testSpace.isValid());
                    }
                    else
                    {
                        assertTrue(testSpace.isValid());
                    }
                    assertNull(testSpace.getPiece());
                }
                spaceIndex++;
            }
        }
    }

    /**
     * Makes sure that the getIndex method returns the proper index that was passed in through the constructor.
     */
    @Test
    public void testGetIndex()
    {
        for(int i = 0;i<8;i++){
        CuT = new Row(i);
        assertEquals(i,CuT.getIndex());
    }
    }

    /**
     * Makes sure the iterator() method returns an object every time.
     */
    @Test
    public void testIterator()
    {
        CuT = new Row(0);

        testIterator = CuT.iterator();

        assertNotNull(testIterator);

    }


}
