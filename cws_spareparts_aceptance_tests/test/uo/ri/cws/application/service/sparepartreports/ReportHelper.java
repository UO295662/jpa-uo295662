package uo.ri.cws.application.service.sparepartreports;

import uo.ri.cws.application.service.util.ClientUtil;
import uo.ri.cws.application.service.util.InterventionUtil;
import uo.ri.cws.application.service.util.MechanicUtil;
import uo.ri.cws.application.service.util.VehicleTypeUtil;
import uo.ri.cws.application.service.util.VehicleUtil;
import uo.ri.cws.application.service.util.WorkOrderUtil;

public class ReportHelper {

	public String addInterventionContext() {
		String vtId = addVehicleType();
		String cId = addClient();
		String vId = addVehicle(cId, vtId);
		String wId = addWorkOrder( vId );
		String mId = addMechanic();
		return addIntervention(wId, mId);
	}

	private String addClient() {
		return new ClientUtil().randomNif().register().get().id;
	}

	private String addVehicleType() {
		return new VehicleTypeUtil().randomName().register().get().id;
	}

	private String addIntervention(String wId, String mId) {
		return new InterventionUtil()
					.forWorkOrder( wId )
					.forMechanic( mId )
					.register()
					.get()
					.id;
	}

	private String addMechanic() {
		return new MechanicUtil().register().get().id;
	}

	private String addWorkOrder(String vId) {
		return new WorkOrderUtil().forVehicle( vId ).register().get().id;
	}

	private String addVehicle(String cId, String vtId) {
		return new VehicleUtil()
				.randomPlate()
				.forClient( cId )
				.forVehicleType( vtId )
				.register()
				.get()
				.id;
	}

}
