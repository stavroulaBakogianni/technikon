package gr.technico.technikon.services;

import gr.technico.technikon.model.Property;
import java.util.List;


public interface PropertyService {
    Property createProperty(Property property);
    Property updateProperty(Property property, Long id);
    Property findByE9(String e9);
    List<Property> findByVAT(String vat);
    Property findByID(Long id);
    Property safelyDeleteByID(Long id);
    boolean permenantlyDeleteByID(Long id);
}
