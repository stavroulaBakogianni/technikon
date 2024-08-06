package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Property;
import java.util.List;


public interface PropertyService {
    Property createProperty() throws CustomException;
    Property updatePropertyE9() throws CustomException;
    Property updatePropertyAddress() throws CustomException;
    Property updatePropertyConstructionYear() throws CustomException;
    Property updatePropertyType() throws CustomException;
    Property updatePropertyVAT() throws CustomException;
    Property findByE9(String e9) throws CustomException;
    List<Property> findByVAT(String vat) throws CustomException;
    Property findByID(Long id) throws CustomException;
    boolean safelyDeleteByID(Long id) throws CustomException;
    boolean permenantlyDeleteByID(Long id) throws CustomException;
    void validateE9(String e9) throws CustomException;
    void validateAddress(String address) throws CustomException;
    void validateConstructionYear(String yearInput) throws CustomException;
    void validatePropertyType(String type) throws CustomException;
    void validateVAT(String vat) throws CustomException;
}
