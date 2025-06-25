package uo.ri.cws.domain;

public class Associations {

    public static class Own {

        public static void link(Client client, Vehicle vehicle) {
            vehicle._setClient(client);
            client._getVehicles().add(vehicle);
        }

        public static void unlink(Client cliente, Vehicle vehicle) {
            cliente._getVehicles().remove(vehicle);
            vehicle._setClient(null);
        }

    }

    public static class Classify {

        public static void link(VehicleType vehicleType, Vehicle vehicle) {
            vehicle._setVehicleType(vehicleType);
            vehicleType._getVehicles().add(vehicle);
        }

        public static void unlink(VehicleType tipoVehicle, Vehicle vehicle) {
            tipoVehicle._getVehicles().remove(vehicle);
            vehicle._setVehicleType(null);
        }
    }

    public static class Hold {

        public static void link(PaymentMean mean, Client client) {
            mean._setClient(client);
            client._getPaymentMeans().add(mean);
        }

        public static void unlink(Client client, PaymentMean mean) {
            client._getPaymentMeans().remove(mean);
            mean._setClient(null);
        }
    }

    public static class Fix {

        public static void link(Vehicle vehicle, WorkOrder workOrder) {
            workOrder._setVehicle(vehicle);
            vehicle._getWorkOrders().add(workOrder);
        }

        public static void unlink(Vehicle vehicle, WorkOrder workOrder) {
            vehicle._getWorkOrders().remove(workOrder);
            workOrder._setVehicle(null);
        }
    }

    public static class Bill {

        public static void link(Invoice invoice, WorkOrder workOrder) {
            workOrder._setInvoice(invoice);
            invoice._getWorkOrders().add(workOrder);
        }

        public static void unlink(Invoice invoice, WorkOrder workOrder) {
            invoice._getWorkOrders().remove(workOrder);
            workOrder._setInvoice(null);
        }
    }

    public static class Settle {

        public static void link(Invoice invoice, Charge cargo, PaymentMean mp) {
            cargo._setInvoice(invoice);
            cargo._setPaymentMean(mp);
            invoice._getCharges().add(cargo);
            mp._getCharges().add(cargo);
        }

        public static void unlink(Charge cargo) {
            cargo._getInvoice()._getCharges().remove(cargo);
            cargo._getPaymentMean()._getCharges().remove(cargo);
            cargo._setInvoice(null);
            cargo._setPaymentMean(null);
        }
    }

    public static class Assign {

        public static void link(Mechanic mechanic, WorkOrder workOrder) {
            workOrder._setMechanic(mechanic);
            mechanic._getAssigned().add(workOrder);
        }

        public static void unlink(Mechanic mechanic, WorkOrder workOrder) {
            mechanic._getAssigned().remove(workOrder);
            workOrder._setMechanic(null);
        }
    }

    public static class Intervene {

        public static void link(WorkOrder workOrder, Intervention intervention,
            Mechanic mechanic) {
            intervention._setWorkOrder(workOrder);
            intervention._setMechanic(mechanic);
            mechanic._getInterventions().add(intervention);
            workOrder._getInterventions().add(intervention);
        }

        public static void unlink(Intervention intervention) {
            intervention.getMechanic()._getInterventions().remove(intervention);
            intervention.getWorkOrder()._getInterventions()
                .remove(intervention);
            intervention._setWorkOrder(null);
            intervention._setMechanic(null);
        }
    }

    public static class Substitute {

        static void link(SparePart sparePart, Substitution substitution,
            Intervention intervention) {
            substitution._setIntervention(intervention);
            substitution._setSparePart(sparePart);
            sparePart._getSubstitutions().add(substitution);
            intervention._getSubstitutions().add(substitution);
        }

        public static void unlink(Substitution substitution) {
            substitution.getIntervention()._getSubstitutions()
                .remove(substitution);
            substitution.getSparePart()._getSubstitutions()
                .remove(substitution);
            substitution._setIntervention(null);
            substitution._setSparePart(null);
        }
    }

    public static class Deliver {

        public static void link(Provider provider, Order order) {
            order._setProvider(provider);
            provider._getOrders().add(order);
        }

        public static void unlink(Provider provider, Order order) {
            provider._getOrders().remove(order);
            order._setProvider(null);
        }
    }

}
