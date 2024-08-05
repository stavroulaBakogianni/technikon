package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Property;
import java.util.List;


public interface PropertyService {
    Property createProperty(Property property) throws CustomException;
    Property updateProperty(Property property, Long id) throws CustomException;
    Property findByE9(String e9) throws CustomException;
    List<Property> findByVAT(String vat) throws CustomException;
    Property findByID(Long id) throws CustomException;
    Property safelyDeleteByID(Long id) throws CustomException;
    boolean permenantlyDeleteByID(Long id) throws CustomException;
}
