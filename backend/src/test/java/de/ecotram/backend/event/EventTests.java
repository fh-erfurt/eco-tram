package de.ecotram.backend.event;

import lombok.Getter;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.jupiter.api.Test;

// this is less of a test and more of a representation of the usage of these events
public final class EventTests {
    @Test
    public void eventsAreIndependent() {
        var emitter1 = new EventEmitter(1);
        var emitter2 = new EventEmitter(2);

        emitter1.getEventHandler().add(args ->
                Assert.isEqual(1, args.id, "Id should be equal to 1.")
        );

        emitter2.getEventHandler().add(args ->
                Assert.isEqual(2, args.id, "Id should be equal to 2.")
        );

        emitter1.invoke();
        emitter2.invoke();
    }

    private final class EventEmitter {
        private final Event<TestEventArgs> event;
        private final int id;

        @Getter
        private final EventHandler<TestEventArgs> eventHandler;

        public EventEmitter(int id) {
            this.event = new Event<>();
            this.eventHandler = new EventHandler<>(event);
            this.id = id;
        }

        public void invoke() {
            event.invoke(new TestEventArgs(this.id));
        }
    }

    private final class TestEventArgs extends EventArgs {
        private final int id;

        public TestEventArgs(int id) {
            this.id = id;
        }
    }
}