package uo.ri.cws.application.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto.OrderLineDto;

public class Assertions {

	public static void sameWorkOrder(InvoicingWorkOrderDto expected,
			InvoicingWorkOrderDto found) {
		assertEquals(expected.id, found.id);

		assertEquals(expected.description, found.description);
		assertEquals(
				expected.date.truncatedTo(ChronoUnit.MILLIS), 
				found.date.truncatedTo(ChronoUnit.MILLIS)
		);
		assertEquals(expected.amount, found.amount, 0.01);
		assertEquals(expected.state, found.state);
	}

	public static void sameWorkOrders(List<InvoicingWorkOrderDto> expected,
			List<InvoicingWorkOrderDto> tested) {
		
		assertEquals(expected.size(), tested.size());
		for (InvoicingWorkOrderDto e : expected) {
			Optional<InvoicingWorkOrderDto> of = findInListById(e.id, tested);
			assertTrue(of.isPresent());
			Assertions.sameWorkOrder(e, of.get());
		}
	}

	private static Optional<InvoicingWorkOrderDto> findInListById(String id,
			List<InvoicingWorkOrderDto> list) {
		return list.stream()
				.filter(w -> w.id.equals(id))
				.findFirst();
	}

	public static void sameOrder(OrderDto expected, OrderDto dto) {
		assertEquals(expected.id, dto.id);
		assertEquals(expected.code, dto.code);
		assertEquals(expected.amount, dto.amount, 0.001);
		assertEquals(expected.orderedDate, dto.orderedDate);
		assertEquals(expected.receptionDate, dto.receptionDate);
		assertEquals(expected.state, dto.state);
		assertEquals(expected.provider.id, dto.provider.id);
		assertEquals(expected.lines.size(), dto.lines.size());
		for (OrderLineDto expLine: expected.lines) {
			OrderLineDto line = find(dto.lines, expLine.sparePart.code);
			assertEquals(expLine.price, line.price, 0.001);
			assertEquals(expLine.quantity, line.quantity);
		}
	}


	private static OrderLineDto find(List<OrderLineDto> lines, String spareCode) {
		return lines.stream()
				.filter(ol -> ol.sparePart.code.equals(spareCode))
				.findFirst()
				.get();
	}
}
