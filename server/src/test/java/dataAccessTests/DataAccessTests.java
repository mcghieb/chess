package dataAccessTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class DataAccessTests {
    @Test
    @Order(0)
    void testTrue(){
        Assertions.assertTrue(true, "This should absolutely not fail." );
    }
}
