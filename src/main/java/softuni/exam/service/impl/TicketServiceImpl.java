package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.TicketsRootSeedDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Ticket;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.constants.GlobalConstants.*;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final TownService townService;
    private final PassengerService passengerService;
    private final PlaneService planeService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, TownService townService, PassengerService passengerService, PlaneService planeService) {
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.townService = townService;
        this.passengerService = passengerService;
        this.planeService = planeService;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKETS_FILE_PATH));
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        TicketsRootSeedDto ticketsRootSeedDto = this.xmlParser.convertFromFile(TICKETS_FILE_PATH, TicketsRootSeedDto.class);
        ticketsRootSeedDto.getTickets().forEach(ticketsSeedDto -> {
            if(this.validationUtil.isValid(ticketsSeedDto)){
                if (this.ticketRepository.findTicketBySerialNumber(ticketsSeedDto.getSerialNumber()) == null){
                    Ticket ticket = this.modelMapper.map(ticketsSeedDto, Ticket.class);
                    Town toTown = this.townService.getTownByName(ticket.getToTown().getName());
                    Town fromTown = this.townService.getTownByName(ticket.getFromTown().getName());
                    Passenger passenger = this.passengerService.getPassengerByEmail(ticket.getPassenger().getEmail());
                    Plane plane = this.planeService.getPlaneByNumber(ticket.getPlane().getRegisterNumber());

                    ticket.setFromTown(fromTown);
                    ticket.setToTown(toTown);
                    ticket.setPassenger(passenger);
                    ticket.setPlane(plane);

                    sb.append(String.format("Successfully imported Ticket %s - %s", ticket.getFromTown().getName(), ticket.getToTown().getName()));

                    this.ticketRepository.saveAndFlush(ticket);
                }else {
                    sb.append("Already in DB");
                }
            }else {
                sb.append("Invalid Ticket");
            }
            sb.append(System.lineSeparator());
        });

        return sb.toString();
    }
}
