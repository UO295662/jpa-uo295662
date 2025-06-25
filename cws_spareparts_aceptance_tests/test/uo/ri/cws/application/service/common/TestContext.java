package uo.ri.cws.application.service.common;


import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestContext {

	public enum Key {
		OPTIONAL_PROVIDER
	}

	private Map<Key, Object> table = new HashMap<>();
	private Exception exception;
	private Optional<?> optional;
	private List<?> resultList;

	public TestContext put(Key key, Object value) {
		table.put(key, value);
		return this;
	}

	public TestContext clear() {
		table.clear();
		return this;
	}

	public Object get(Key key) {
		return table.get(key);
	}

	public Exception getException() {
		return exception;
	}

	/**
     * Deprecated, use ctx.tryAndKeepException() instead
     */
	@Deprecated
	public void setException(Exception ex) {
		this.exception = ex;
	}

	public void setUniqueResult(Optional<?> optional) {
		this.optional = optional;
	}

	public void setResultList(List<?> list) {
		this.resultList = list;
	}

	public Optional<?> getUniqueResult() {
		return optional;
	}

	public List<?> getResultList() {
		return resultList;
	}

	@FunctionalInterface
	public interface Action {
		void execute() throws Exception;
	}
	public void tryAndKeepException(Action action) {
		try {
			action.execute();
			fail("An exception was expected");
		} catch (Exception e) {
			this.exception = e;
		}
	}

}
