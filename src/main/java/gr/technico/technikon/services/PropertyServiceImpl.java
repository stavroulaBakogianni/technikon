package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import gr.technico.technikon.repositories.PropertyRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PropertyServiceImpl implements PropertyService {

    private final OwnerService ownerServiceInterface;
    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository, OwnerServiceImpl ownerService) {
        this.propertyRepository = propertyRepository;
        this.ownerServiceInterface = ownerService;
    }

    /**
     * Creates a new property with the given details.
     *
     * @param e9 The E9 identifier for the property. Must be unique.
     * @param address The address of the property.
     * @param year The construction year of the property.
     * @param propertyType The type of the property.
     * @param vat The VAT of the owner of the property.
     * @return The created Property object.
     * @throws CustomException If vat is not valid or if the property cannot be
     * saved.
     */
    @Override
    public Property createProperty(String e9, String address, int year, PropertyType propertyType, String vat) throws CustomException {
        Optional<Owner> owner = ownerServiceInterface.searchOwnerByVat(vat);
        if (owner.isEmpty()) {
            throw new CustomException("Not valid Vat");
        }

        Property property = new Property();
        property.setE9(e9);
        property.setPropertyAddress(address);
        property.setConstructionYear(year);
        property.setPropertyType(propertyType);
        property.setOwner(owner.get());
        property.setDeleted(false);

        try {
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty.get();
        } catch (Exception e) {
            throw new CustomException("Failed to create property");
        }
    }

    /**
     * Updates the E9 identifier of an existing property.
     *
     * @param property The property object to be updated.
     * @param e9 The new E9 identifier to set for the property.
     * @return The updated Property object.
     * @throws CustomException If the if the update fails.
     */
    @Override
    public Property updatePropertyE9(Property property, String e9) throws CustomException {
        property.setE9(e9);

        try {
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty.get();
        } catch (Exception e) {
            throw new CustomException("Failed to update property with E9 " + e9);
        }
    }

    /**
     * Updates the address of an existing property.
     *
     * @param property The property object to be updated.
     * @param address The new address to set for the property.
     * @return The updated Property object.
     * @throws CustomException If if the update fails.
     */
    @Override
    public Property updatePropertyAddress(Property property, String address) throws CustomException {
        property.setPropertyAddress(address);

        try {
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty.get();
        } catch (Exception e) {
            throw new CustomException("Failed to update property with Address " + address);
        }
    }

    /**
     * Updates the construction year of an existing property.
     *
     * @param property The property object to be updated.
     * @param year The new construction year to set for the property.
     * @return The updated Property object.
     * @throws CustomException If the update fails.
     */
    @Override
    public Property updatePropertyConstructionYear(Property property, int year) throws CustomException {
        property.setConstructionYear(year);

        try {
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty.get();
        } catch (Exception e) {
            throw new CustomException("Failed to update property with Construction Year " + year);
        }
    }

    /**
     * Updates the property type of an existing property.
     *
     * @param property The property object to be updated.
     * @param propertyType The new property type to set for the property.
     * @return The updated Property object.
     * @throws CustomException If the update fails.
     */
    @Override
    public Property updatePropertyType(Property property, PropertyType propertyType) throws CustomException {
        property.setPropertyType(propertyType);

        try {
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty.get();
        } catch (Exception e) {
            throw new CustomException("Failed to update property with Property Type " + propertyType);
        }
    }

    /**
     * Finds a property by its E9 identifier.
     *
     * @param e9 the E9 identifier of the property to be found
     * @return the property with the given E9
     * @throws CustomException if the property with the given E9 is not found
     */
    @Override
    public Property findByE9(String e9) throws CustomException {
        Optional<Property> property = propertyRepository.findPropertyByE9(e9);

        if (property.isEmpty()) {
            throw new CustomException("Property with E9 " + e9 + " not found");
        } else {
            if (property.get().isDeleted()) {
                System.out.println("This property is marked as deleted");
            }
            return property.get();
        }
    }

    /**
     * Searches for a property by its E9 value.
     *
     * @param e9 the E9 value to search for.
     * @return the property if found, or null if no property with the given E9
     * exists.
     * @throws CustomException if an error occurs while searching for the
     * property.
     */
    @Override
    public Property findByE9ForCreate(String e9) throws CustomException {
        Optional<Property> property = propertyRepository.findPropertyByE9(e9);

        if (property.isPresent()) {
            return property.get();
        } else {
            return null;
        }
    }

    /**
     * Finds and returns a list of properties associated with the given VAT
     * number.
     *
     * @param vat the VAT number to search for.
     * @return a list of properties associated with the given VAT number.
     * @throws CustomException if no properties are found for the given VAT
     * number.
     */
    @Override
    public List<Property> findByVAT(String vat) throws CustomException {
        List<Property> properties = propertyRepository.findPropertyByVAT(vat);
        if (properties.isEmpty()) {
            throw new CustomException("Properties not found based on vat " + vat);
        }
        return properties;
    }

    /**
     * Finds and returns a list of properties associated with the given VAT
     * number, excluding those marked as deleted.
     *
     * @param vat the VAT number to search for.
     * @return a list of properties associated with the given VAT number,
     * excluding deleted properties.
     * @throws CustomException if no properties are found for the given VAT
     * number.
     */
    @Override
    public List<Property> findByVATOwner(String vat) throws CustomException {
        List<Property> properties = propertyRepository.findPropertyByVAT(vat).stream()
                .filter(property -> !property.isDeleted())
                .collect(Collectors.toList());
        if (properties.isEmpty()) {
            throw new CustomException("Properties not found based on vat " + vat);
        }
        return properties;
    }

    /**
     * Finds a property by its ID.
     *
     * @param id the ID of the property to be found
     * @return the property with the given ID
     * @throws CustomException if the property with the given ID is not found
     */
    @Override
    public Property findByID(Long id) throws CustomException {
        Optional<Property> property = propertyRepository.findById(id);
        if (property.isEmpty()) {
            throw new CustomException("Property with ID: " + id + " not found");
        }
        return property.get();
    }

    /**
     * Retrieves all properties from the repository.
     *
     * @return A list of all Property objects in the repository.
     */
    @Override
    public List<Property> findAllProperties() {
        return propertyRepository.findAll();
    }

    /**
     * Safely deletes a Property entity by marking it as deleted.
     *
     * This method retrieves a Property entity by its ID and sets its deleted
     * flag to true. It then attempts to save the updated entity. If the save
     * operation is successful, the method returns true. If any error occurs
     * during the process, a CustomException is thrown.
     *
     * @param id the ID of the Property entity to be safely deleted
     * @return true if the Property entity was successfully marked as deleted
     * and saved
     * @throws CustomException if the Property entity could not be safely
     * deleted or saved
     */
    @Override
    public boolean safelyDeleteByID(Long id) throws CustomException {
        Property property = findByID(id);
        property.setDeleted(true);
        try {
            propertyRepository.save(property);
            return true;
        } catch (Exception e) {
            throw new CustomException("Failed to safely delete property with ID : " + id);
        }
    }

    /**
     * Permanently deletes a property by its ID.
     *
     * If the property could not be deleted, a CustomException is thrown.
     *
     * @param id the ID of the property to be permanently deleted
     * @return true if the property was successfully deleted
     * @throws CustomException if the property could not be deleted
     */
    @Override
    public boolean permenantlyDeleteByID(Long id) throws CustomException {
        boolean success = propertyRepository.deleteById(id);
        if (!success) {
            throw new CustomException("Failed to permanently delete property with ID: " + id);
        }
        return true;
    }

    /**
     * Validates the E9 value. Ensures that the E9 is not null and has exactly
     * 20 characters.
     *
     * @param e9 The E9 value to validate.
     * @throws CustomException If the E9 is null or does not have exactly 20
     * characters.
     */
    @Override
    public void validateE9(String e9) throws CustomException {
        if (e9 == null || e9.length() != 20) {
            throw new CustomException("E9 must be exactly 20 characters.");
        }
    }

    /**
     * Validates the construction year input. Ensures that the year is not null
     * or blank, and is a four-digit positive integer.
     *
     * @param yearInput The construction year input to validate.
     * @throws CustomException If the year is null, blank, zero, negative, or
     * not a valid integer.
     */
    @Override
    public void validateConstructionYear(String yearInput) throws CustomException {
        if (yearInput == null || yearInput.isBlank() || yearInput.length() != 4) {
            throw new CustomException("Year cannot be null or blank. Also the year of construction must be foy digits");
        }
        try {
            int year = Integer.parseInt(yearInput);
            if (year <= 0) {
                throw new CustomException("Year cannot be zero or negative.");
            }
        } catch (NumberFormatException e) {
            throw new CustomException("Year must be a valid integer.");
        }
    }

    /**
     * Validates the property type. Ensures that the property type is not null
     * or blank and match one of the predefined property types.
     *
     * @param type The property type to validate.
     * @throws CustomException If the property type is null or blank.
     */
    @Override
    public void validatePropertyType(String type) throws CustomException {
        if (type == null || type.isBlank()) {
            throw new CustomException("Property type cannot be null or blank.");
        }

        boolean isValid = false;
        for (PropertyType propertyType : PropertyType.values()) {
            if (propertyType.getCode().equalsIgnoreCase(type)) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new CustomException("Invalid property type: " + type);
        }
    }
}
