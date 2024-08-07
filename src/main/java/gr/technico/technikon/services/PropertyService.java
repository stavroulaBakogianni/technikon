package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import java.util.List;


public interface PropertyService {
    Property createProperty(String e9, String address, int year, PropertyType propertyType, String vat) throws CustomException;
    Property updatePropertyE9(Long propertyId, String e9) throws CustomException;
    Property updatePropertyAddress(Long propertyId, String address) throws CustomException;
    Property updatePropertyConstructionYear(Long propertyId, int year) throws CustomException;
    Property updatePropertyType(Long propertyId, PropertyType propertyType) throws CustomException;
    Property findByE9(String e9) throws CustomException;
    List<Property> findByVAT(String vat) throws CustomException;
    List<Property> findAllProperties();
    Property findByID(Long id) throws CustomException;
    boolean safelyDeleteByID(Long id) throws CustomException;
    boolean permenantlyDeleteByID(Long id) throws CustomException;
    void validateE9(String e9) throws CustomException;    
    void validateConstructionYear(String yearInput) throws CustomException;
    void validatePropertyType(String type) throws CustomException;
    void validateVAT(String vat) throws CustomException;
}
