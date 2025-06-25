package uo.ri.cws.application.service;

import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientHistoryService;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.spare.OrdersService;
import uo.ri.cws.application.service.spare.ProvidersCrudService;
import uo.ri.cws.application.service.spare.SparePartCrudService;
import uo.ri.cws.application.service.spare.SparePartReportService;
import uo.ri.cws.application.service.spare.SuppliesCrudService;
import uo.ri.cws.application.service.vehicle.VehicleCrudService;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService;
import uo.ri.cws.application.service.workorder.CloseWorkOrderService;
import uo.ri.cws.application.service.workorder.ViewAssignedWorkOrdersService;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService;

public interface ServiceFactory {

    MechanicCrudService forMechanicCrudService();

    VehicleTypeCrudService forVehicleTypeCrudService();

    SparePartCrudService forSparePartCrudService();

    ProvidersCrudService forProvidersService();

    OrdersService forOrdersService();

    SuppliesCrudService forSuppliesCrudService();

    SparePartReportService forSparePartReportService();

    InvoicingService forCreateInvoiceService();

    VehicleCrudService forVehicleCrudService();

    ClientCrudService forClientCrudService();

    ClientHistoryService forClientHistoryService();

    WorkOrderCrudService forWorkOrderCrudService();

    CloseWorkOrderService forClosingBreakdown();

    ViewAssignedWorkOrdersService forViewAssignedWorkOrdersService();

}
