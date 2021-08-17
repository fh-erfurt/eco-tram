package de.ecotram.backend.event;

import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.jupiter.api.Test;

// this is less of a test and more of a representation of the usage of these events
public final class EventTests {
	@Test
	public void events_are_independent() {
		var emitter1 = new EventEmitter(1);
		var emitter2 = new EventEmitter(2);

		emitter1.onEvent().add(args ->
				Assert.isEqual(1, args.id, "Id should be equal to 1.")
		);

		emitter2.onEvent().add(args ->
				Assert.isEqual(2, args.id, "Id should be equal to 2.")
		);

		emitter1.invoke();
		emitter2.invoke();
	}

	private static final class EventEmitter {
		private final Event<TestEventArgs> event = new Event<>();
		private final int id;

		public EventEmitter(int id) {
			this.id = id;
		}

		public Event<TestEventArgs>.Accessor onEvent() {
			return event.getAccess();
		}

		public void invoke() {
			event.invoke(new TestEventArgs(this.id));
		}
	}

	private final static class TestEventArgs extends EventArgs {
		private final int id;

		public TestEventArgs(int id) {
			this.id = id;
		}
	}
}