package gr.technico.technikon.services;

import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import gr.technico.technikon.repositories.PropertyRepository;

public class PropertyServiceImpl implements PropertyService{
    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }
    @Override
    public Property createProperty(String e9, String propertyAddress,
        int constructionYear, PropertyType propertyType, Owner owner) {
        Property property = new Property();
        property.setE9(e9);
        property.setPropertyAddress(propertyAddress);
        property.setConstructionYear(constructionYear);
        property.setPropertyType(propertyType);
        property.setOwner(owner);
        
        propertyRepository.save(property);
        return property;
    }
}
