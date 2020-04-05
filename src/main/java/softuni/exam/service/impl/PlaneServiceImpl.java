package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.constants.GlobalConstants;
import softuni.exam.models.dtos.PlanesRootSeedDto;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.constants.GlobalConstants.*;

@Service
public class PlaneServiceImpl implements PlaneService {
    private final PlaneRepository planeRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public PlaneServiceImpl(PlaneRepository planeRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.planeRepository = planeRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANES_FILE_PATH));
    }

    @Override
    public String importPlanes() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        PlanesRootSeedDto planesRootSeedDto = this.xmlParser.convertFromFile(PLANES_FILE_PATH, PlanesRootSeedDto.class);
        planesRootSeedDto.getPlanes().forEach(planesSeedDto -> {
            if(this.validationUtil.isValid(planesSeedDto)){
                if(this.planeRepository.findPlaneByRegisterNumber(planesSeedDto.getRegisterNumber()) == null) {
                    Plane plane = this.modelMapper.map(planesSeedDto, Plane.class);
                    sb.append(String.format("Successfully imported Plane %s", plane.getRegisterNumber()));
                    this.planeRepository.saveAndFlush(plane);
                }else {
                    sb.append("Already in DB");
                }
            }else {
                sb.append("Invalid Plane");
            }
            sb.append(System.lineSeparator());
        });


        return sb.toString();
    }

    @Override
    public Plane getPlaneByNumber(String number) {
        return this.planeRepository.findPlaneByRegisterNumber(number);
    }
}
