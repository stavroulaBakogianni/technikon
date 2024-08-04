package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.repositories.PropertyRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public Property createProperty(Property property) {
        return propertyRepository.save(property).get();
    }

    @Override
    public Property updateProperty(Property property, Long id) {
        Property findProperty = propertyRepository.findById(id).get();
        findProperty.setE9(property.getE9());
        findProperty.setPropertyAddress(property.getPropertyAddress());
        findProperty.setConstructionYear(property.getConstructionYear());
        findProperty.setPropertyType(property.getPropertyType());
        findProperty.setOwner(property.getOwner());
        findProperty.setDeleted(property.isDeleted());
        return propertyRepository.save(findProperty).get();
    }

    @Override
    public Property findByE9(String e9) {
        Optional<Property> property = propertyRepository.findPropertyByE9(e9);

        if (property.isEmpty()) {
            log.info("Property not found based on e9");
            throw new CustomException("Property not found");
        }
        return property.get();
    }

    @Override
    public List<Property> findByVAT(String vat) {
        List<Property> properties = propertyRepository.findPropertyByVAT(vat);

        if (properties.isEmpty()) {
            log.info("Properties not found based on vat");
            throw new CustomException("Properties not found");
        }
        
        return properties;
    }

    @Override
    public Property findByID(Long id) {
        Optional<Property> property = propertyRepository.findById(id);
        if (property.isEmpty()) {
            log.info("Property not found");
            throw new CustomException("Property not found");
        }
        return property.get();
    }
    

    @Override
    public Property safelyDeleteByID(Long id) {
        Property property = findByID(id);
        property.setDeleted(true);
        return propertyRepository.save(property).get();        
    }

    @Override
    public boolean permenantlyDeleteByID(Long id) {
        return propertyRepository.deleteById(id);
    }

}
