package de.ecotram.backend.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

/**
 * Handler for counting entities for controller
 */
@Component("statisticsHandler")
public final class StatisticsHandler {

	private final EntityManager entityManager;

	@Autowired
	public StatisticsHandler(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private long countEntries(String className) {
		return (long) entityManager
				.createQuery("select count(*) from " + className + " t")
				.getSingleResult();
	}

	public StatisticsCountResult getConnectionCount() {
		return new StatisticsCountResult("connection", this.countEntries("Connection"));
	}

	public StatisticsCountResult getLineCount() {
		return new StatisticsCountResult("line", this.countEntries("Line"));
	}

	public StatisticsCountResult getStationCount() {
		return new StatisticsCountResult("station", this.countEntries("Station"));
	}

	@AllArgsConstructor
	public static class StatisticsCountResult {
		@Getter
		private final String type;

		@Getter
		private final long results;
	}

	@AllArgsConstructor
	public static class StatisticsOverallCountResult {
		@Getter
		private final StatisticsCountResult connections;

		@Getter
		private final StatisticsCountResult lines;

		@Getter
		private final StatisticsCountResult stations;
	}
}