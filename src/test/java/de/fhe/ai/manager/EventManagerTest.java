package de.fhe.ai.manager;

import de.fhe.ai.model.Line;
import de.fhe.ai.model.PassengerTram;
import de.fhe.ai.model.Tram;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class EventManagerTest {

    @Test
    public void get_single_event_entity_instance() {
        Tram exampleTramObject = new PassengerTram(0, null, 100, 1000, 100, "passenger");
        EventManager eventManager = EventManager.getInstance();

        EventManager.EventEntity firstEventEntity = eventManager.getEventEntity(exampleTramObject);
        EventManager.EventEntity secondEventEntity = eventManager.getEventEntity(exampleTramObject);

        Assert.assertEquals("The objects should be the same", firstEventEntity, secondEventEntity);
    }

    @Test
    public void add_listener_to_event_entity() {
        Tram exampleTramObject = new PassengerTram(0, null, 100, 1000, 100, "passenger");
        EventManager eventManager = EventManager.getInstance();

        EventManager.EventEntity eventEntity = eventManager.getEventEntity(exampleTramObject);

        String demoEvent = "helloWorld";
        boolean demoData = true;

        // Listener for all events
        eventEntity.addListener((entity, event, data) -> {
            System.out.println("Event received with identifier " + event);

            Assert.assertEquals("Event entity should be the same object", exampleTramObject, entity);
            Assert.assertEquals("Event identifier should be the same", demoEvent, event);
            Assert.assertEquals("Event data should be the same", demoData, data);
        });

        eventEntity.emit(demoEvent, demoData);

        // Remove event entity from manager for other tests
        eventManager.removeEventEntity(eventEntity);
    }

    @Test
    public void test_different_listener_targets() {
        Tram exampleTramObject = new PassengerTram(0, null, 100, 1000, 100, "passenger");
        EventManager eventManager = EventManager.getInstance();

        EventManager.EventEntity eventEntity = eventManager.getEventEntity(exampleTramObject);

        String firstEvent = "create";
        String secondEvent = "destroy";

        EventManager.EventCallback firstCallback = (entity, event, data) -> {
            System.out.println("Received event with identifier " + event);
            Assert.assertEquals("Event should be create", firstEvent, event);
        };

        EventManager.EventCallback secondCallback = (entity, event, data) -> {
            System.out.println("Received event with identifier " + event);
            Assert.assertEquals("Event should be destroy", secondEvent, event);
        };

        eventEntity.addListener(firstEvent, firstCallback);
        eventEntity.addListener(secondEvent, secondCallback);

        eventEntity.emit(firstEvent);
        eventEntity.emit(secondEvent);

        // Remove event entity from manager for other tests
        eventManager.removeEventEntity(eventEntity);
    }

    @Test
    public void check_listener_existence() {
        Tram exampleTramObject = new PassengerTram(0, null, 100, 1000, 100, "passenger");
        EventManager eventManager = EventManager.getInstance();

        EventManager.EventEntity eventEntity = eventManager.getEventEntity(exampleTramObject);

        EventManager.EventCallback callback = (entity, event, data) -> {
        };

        eventEntity.addListener(callback);

        Assert.assertTrue("Callback should still be registered", eventEntity.isListenerRegistered(callback));

        eventEntity.removeListener(callback);

        Assert.assertFalse("Callback should no longer be registerd", eventEntity.isListenerRegistered(callback));

        // Remove event entity from manager for other tests
        eventManager.removeEventEntity(eventEntity);
    }

    @Test
    public void remove_entity_and_listener() {
        Tram exampleTramObject = new PassengerTram(0, null, 100, 1000, 100, "passenger");
        EventManager eventManager = EventManager.getInstance();

        EventManager.EventEntity eventEntity = eventManager.getEventEntity(exampleTramObject);

        EventManager.EventCallback callback = (entity, event, data) -> {
        };

        eventEntity.addListener(callback);

        eventManager.removeEventEntityByObject(exampleTramObject);

        Assert.assertFalse("Event should no longer be registered", eventEntity.isListenerRegistered(callback));

        EventManager.EventEntity otherEntity = eventManager.getEventEntity(exampleTramObject);

        Assert.assertNotEquals("Entites should be distinct", eventEntity, otherEntity);

        // Remove event entity from manager for other tests
        eventManager.removeEventEntity(otherEntity);
    }

    @Test
    public void test_multiple_entities_and_listener() {
        Tram firstTramObject = new PassengerTram(0, null, 100, 1000, 100, "passenger");
        Tram secondTramObject = new PassengerTram(0, null, 100, 1000, 100, "passenger");
        Line lineObject = new Line(-1, EventManager.getInstance(),"Linie 6", new ArrayList<>());

        EventManager eventManager = EventManager.getInstance();

        EventManager.EventEntity firstTramEventEntity = eventManager.getEventEntity(firstTramObject);
        EventManager.EventEntity secondTramEventEntity = eventManager.getEventEntity(secondTramObject);
        EventManager.EventEntity lineEventEntity = eventManager.getEventEntity(lineObject);

        String tramEvent = "createTram";
        String lineEvent = "createLine";

        firstTramEventEntity.addListener((entity, event, data) -> {
            System.out.println("Received event for firstTram with identifier " + event);

            Assert.assertEquals("Event identifier should be createTram", tramEvent, event);
        });

        secondTramEventEntity.addListener((entity, event, data) -> {
            System.out.println("Received event for secondTram with identifier " + event);

            Assert.assertEquals("Event identifier should be createTram", tramEvent, event);
        });

        lineEventEntity.addListener((entity, event, data) -> {
            System.out.println("Received event for line with identifier " + event);

            Assert.assertEquals("Event identifier should be createLine", lineEvent, event);
        });

        eventManager.emitToAllEntitiesOfType(PassengerTram.class, tramEvent, null);
        eventManager.emitToAllEntitiesOfType(Line.class, lineEvent, null);

        // Remove event entity from manager for other tests
        eventManager.removeEventEntity(firstTramEventEntity);
        eventManager.removeEventEntity(secondTramEventEntity);
        eventManager.removeEventEntity(lineEventEntity);
    }

}
