package de.fhe.ai;

import org.junit.Assert;
import org.junit.Test;

import de.fhe.ai.model.PassengerTram;

public class UtilsTest {
    @Test
    public void get_short_class_name() {
        var stringType = new String();
        var tramType = new PassengerTram(0, null, 0, 0, 0, stringType);

        var expectedString = Utils.getShortClassName(stringType.getClass());
        var expectedTram = Utils.getShortClassName(tramType.getClass());

        Assert.assertEquals("The values should be the same", expectedString, "String");
        Assert.assertEquals("The values should be the same", expectedTram, "PassengerTram");
    }
}