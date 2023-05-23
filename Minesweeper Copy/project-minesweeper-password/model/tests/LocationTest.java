package model.tests;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

import model.Location;

import static org.junit.Assert.assertEquals;


@Testable
public class LocationTest {
    /**
     * test the location constructor
     */
    @Test
    public void locationConstructor(){
        Location expLoc=new Location(6, 9);
        assertEquals(6, expLoc.getRow());
        assertEquals(9, expLoc.getCol());
    }

    /**
     * test the equals of the location class
     */
    @Test
    public void locEqualsTest(){
        Location loc1=new Location(1, 1);
        Location loc2=new Location(1, 1);

        assertEquals(loc1, loc2);

    }

    /**
     * tests the equals of the location class
     */
    @Test
    public void locHashTest(){
        Location loc1=new Location(1, 1);
        Location loc2=new Location(1, 1);

        assertEquals(loc1.hashCode(), loc2.hashCode());
    }

    /**
     * this is test for get row of location
     */
    @Test
    public void getRowsTest(){
        Location loc1=new Location(1, 1);
        
        assertEquals(1,loc1.getRow());
    }    

    /**
     * this is the test fot the getcol of location
     */
    @Test
    public void getColTest(){
        Location loc1=new Location(1, 1);
        
        assertEquals(1,loc1.getCol());
    } 

    /**
     * this tests tostring of the location
     */
    @Test
    public void locationToString(){
        Location loc1=new Location(1, 1);
        loc1.toString();
        assertEquals("1 1",loc1.toString());
    }   
}
