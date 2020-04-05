package softuni.exam.models.dtos;

import org.hibernate.validator.constraints.Length;
import softuni.exam.adapters.LocalDateTimeAdapter;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketsSeedDto {

    @XmlElement(name = "serial-number")
    private String serialNumber;
    @XmlElement(name = "price")
    private BigDecimal price;
    @XmlElement(name = "take-off")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime takeoff;
    @XmlElement(name = "from-town")
    private FromTownSeedDto fromTownSeedDto;
    @XmlElement(name = "to-town")
    private ToTownSeedDto toTownSeedDto;
    @XmlElement(name = "passenger")
    private PassengerXmlSeedDto passengerXmlSeedDto;
    @XmlElement(name = "plane")
    private PlaneXmlSeedDto planeXmlSeedDto;

    public TicketsSeedDto() {
    }

    @Length(min = 2)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Min(value = 0)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(LocalDateTime takeoff) {
        this.takeoff = takeoff;
    }

    public FromTownSeedDto getFromTownSeedDto() {
        return fromTownSeedDto;
    }

    public void setFromTownSeedDto(FromTownSeedDto fromTownSeedDto) {
        this.fromTownSeedDto = fromTownSeedDto;
    }

    public ToTownSeedDto getToTownSeedDto() {
        return toTownSeedDto;
    }

    public void setToTownSeedDto(ToTownSeedDto toTownSeedDto) {
        this.toTownSeedDto = toTownSeedDto;
    }

    public PassengerXmlSeedDto getPassengerXmlSeedDto() {
        return passengerXmlSeedDto;
    }

    public void setPassengerXmlSeedDto(PassengerXmlSeedDto passengerXmlSeedDto) {
        this.passengerXmlSeedDto = passengerXmlSeedDto;
    }

    public PlaneXmlSeedDto getPlaneXmlSeedDto() {
        return planeXmlSeedDto;
    }

    public void setPlaneXmlSeedDto(PlaneXmlSeedDto planeXmlSeedDto) {
        this.planeXmlSeedDto = planeXmlSeedDto;
    }
}
