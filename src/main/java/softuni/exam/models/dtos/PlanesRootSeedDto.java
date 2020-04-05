package softuni.exam.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "planes")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlanesRootSeedDto {

    @XmlElement(name = "plane")
    private List<PlanesSeedDto> planes;

    public PlanesRootSeedDto() {
    }

    public List<PlanesSeedDto> getPlanes() {
        return planes;
    }

    public void setPlanes(List<PlanesSeedDto> planes) {
        this.planes = planes;
    }
}
