import java.util.ArrayList;
import java.util.List;

// Treni temsil eden sınıf
class Train {
    String name;
    List<Wagon> wagons;

    public Train(String name, List<Wagon> wagons) {
        this.name = name;
        this.wagons = wagons;
    }
}

// Vagonu temsil eden sınıf
class Wagon {
    String name;
    int capacity;
    int occupiedSeats;

    public Wagon(String name, int capacity, int occupiedSeats) {
        this.name = name;
        this.capacity = capacity;
        this.occupiedSeats = occupiedSeats;
    }
}

// Rezervasyon isteğini temsil eden sınıf
class ReservationRequest {
    Train train;
    int numberOfPassengers;
    boolean canPassengersBeSeparated;

    public ReservationRequest(Train train, int numberOfPassengers, boolean canPassengersBeSeparated) {
        this.train = train;
        this.numberOfPassengers = numberOfPassengers;
        this.canPassengersBeSeparated = canPassengersBeSeparated;
    }
}

// Rezervasyon yanıtını temsil eden sınıf
class ReservationResponse {
    boolean isReservationPossible;
    List<AllocationDetail> allocationDetails;

    public ReservationResponse(boolean isReservationPossible, List<AllocationDetail> allocationDetails) {
        this.isReservationPossible = isReservationPossible;
        this.allocationDetails = allocationDetails;
    }
}

// Yerleşim ayrıntısını temsil eden sınıf
class AllocationDetail {
    String wagonName;
    int numberOfPassengers;

    public AllocationDetail(String wagonName, int numberOfPassengers) {
        this.wagonName = wagonName;
        this.numberOfPassengers = numberOfPassengers;
    }
}

// Rezervasyon servisi
class ReservationService {

    public ReservationResponse makeReservation(ReservationRequest request) {
        List<AllocationDetail> allocationDetails = new ArrayList<>();

        if (!canAccommodateAllPassengers(request.train, request.numberOfPassengers)) {
            return new ReservationResponse(false, allocationDetails);
        }

        if (request.canPassengersBeSeparated) {
            allocatePassengersSeparately(request.train, request.numberOfPassengers, allocationDetails);
        } else {
            allocatePassengersTogether(request.train, request.numberOfPassengers, allocationDetails);
        }

        return new ReservationResponse(true, allocationDetails);
    }

    private boolean canAccommodateAllPassengers(Train train, int numberOfPassengers) {
        int totalCapacity = train.wagons.stream().mapToInt(wagon -> wagon.capacity - wagon.occupiedSeats).sum();
        return numberOfPassengers <= totalCapacity;
    }

    private void allocatePassengersSeparately(Train train, int numberOfPassengers, List<AllocationDetail> allocationDetails) {
        for (Wagon wagon : train.wagons) {
            int availableSeats = wagon.capacity - wagon.occupiedSeats;
            int passengersToAllocate = Math.min(availableSeats, numberOfPassengers);
            if (passengersToAllocate > 0) {
                allocationDetails.add(new AllocationDetail(wagon.name, passengersToAllocate));
                numberOfPassengers -= passengersToAllocate;
            }
        }
    }

    private void allocatePassengersTogether(Train train, int numberOfPassengers, List<AllocationDetail> allocationDetails) {
        for (Wagon wagon : train.wagons) {
            int availableSeats = wagon.capacity - wagon.occupiedSeats;
            if (availableSeats >= numberOfPassengers) {
                allocationDetails.add(new AllocationDetail(wagon.name, numberOfPassengers));
                return;
            }
        }
    }
}
