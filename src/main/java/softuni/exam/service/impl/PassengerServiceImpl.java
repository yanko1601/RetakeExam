package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.PassengersSeedDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

import static softuni.exam.constants.GlobalConstants.*;

@Service
@Transactional
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final TownService townService;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, TownService townService) {
        this.passengerRepository = passengerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.townService = townService;
    }


    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGERS_FILE_PATH));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder sb = new StringBuilder();
        PassengersSeedDto[] dtos = this.gson.fromJson(new FileReader(PASSENGERS_FILE_PATH), PassengersSeedDto[].class);
        Arrays.stream(dtos).forEach(passengersSeedDto -> {
            if(this.validationUtil.isValid(passengersSeedDto)){
                if(this.passengerRepository.findPassengerByEmail(passengersSeedDto.getEmail()) == null){
                    Passenger passenger = this.modelMapper.map(passengersSeedDto, Passenger.class);
                    Town town = this.townService.getTownByName(passengersSeedDto.getTown());
                    passenger.setTown(town);
                    sb.append(String.format("Successfully imported Passenger %s - %s", passenger.getLastName(), passenger.getEmail()));
                    this.passengerRepository.saveAndFlush(passenger);
                }else {
                    sb.append("Already in DB");
                }
            }else {
                sb.append("Invalid Passenger");
            }
            sb.append(System.lineSeparator());
        });

        return sb.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();

        this.passengerRepository.getAllPassengersOrderByTicketCountThenByEmail().forEach(pass -> {
            sb.append(String.format("Passenger %s  %s\n" +
                    "\tEmail - %s\n" +
                    "\tPhone - %s\n" +
                    "\tNumber of tickets - %d\n"
                    , pass.getFirstName()
                    , pass.getLastName()
                    , pass.getEmail()
                    , pass.getPhoneNumber()
                    , pass.getTickets().size()));
        });


        return sb.toString();
    }

    @Override
    public Passenger getPassengerByEmail(String email) {
        return this.passengerRepository.findPassengerByEmail(email);
    }
}
