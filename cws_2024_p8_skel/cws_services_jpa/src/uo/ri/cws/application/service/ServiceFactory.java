package uo.ri.cws.application.service;

import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientHistoryService;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.spare.SparePartCrudService;
import uo.ri.cws.application.service.vehicle.VehicleCrudService;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService;
import uo.ri.cws.application.service.workorder.CloseWorkOrderService;
import uo.ri.cws.application.service.workorder.ViewAssignedWorkOrdersService;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService;

public interface ServiceFactory {

	// Manager use cases
	MechanicCrudService forMechanicCrudService();
	VehicleTypeCrudService forVehicleTypeCrudService();
	
	// Spare parts use cases
	SparePartCrudService forSparePartCrudService();

	// Cash use cases
	InvoicingService forCreateInvoiceService();

	// Foreman use cases
	VehicleCrudService forVehicleCrudService();
	ClientCrudService forClientCrudService();
	ClientHistoryService forClientHistoryService();
	WorkOrderCrudService forWorkOrderCrudService();

	// Mechanic use cases
	CloseWorkOrderService forClosingBreakdown();
	ViewAssignedWorkOrdersService forViewAssignedWorkOrdersService();

}
