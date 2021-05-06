package de.fhe.ai;

import de.fhe.ai.manager.EventManager;
import de.fhe.ai.manager.TrafficManager;
import de.fhe.ai.model.PassengerTram;
import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {
    @Test
    public void get_short_class_name() {
        var stringType = "testTramType";
        var tramType = new PassengerTram(0, EventManager.getInstance(), TrafficManager.getInstance(), 0, 0, 0, stringType);

        var expectedString = Utils.getShortClassName(stringType.getClass());
        var expectedTram = Utils.getShortClassName(tramType.getClass());

        Assert.assertEquals("The values should be the same", expectedString, "String");
        Assert.assertEquals("The values should be the same", expectedTram, "PassengerTram");
    }
}