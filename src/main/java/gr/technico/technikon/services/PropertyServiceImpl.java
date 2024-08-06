package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import gr.technico.technikon.repositories.PropertyRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PropertyServiceImpl implements PropertyService {

    private final OwnerServiceInterface ownerServiceInterface;
    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository, OwnerService ownerService) {
        this.propertyRepository = propertyRepository;
        this.ownerServiceInterface = ownerService;
    }

    /**
     * Creates a new Property entity based on user input.
     *
     * This method prompts the user to input various details required to create
     * a Property entity. It validates the input, retrieves the owner by VAT,
     * and then creates and saves a new Property entity. If any validation fails
     * or an error occurs during creation, a CustomException is thrown.
     *
     * @return the created Property entity, if the creation is successful
     * @throws CustomException if any of the following occur: The property type
     * is invalid. The provided VAT does not correspond to any existing owner.
     * Failed to save the Property entity to the database.
     */
    @Override
    public Property createProperty() throws CustomException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please insert e9");
        String e9 = scanner.nextLine();
        validateE9(e9);
        System.out.println("Please insert address");
        String address = scanner.nextLine();
        validateAddress(address);
        System.out.println("Please insert year of construction");
        String yearInput = scanner.nextLine();
        validateConstructionYear(yearInput);
        int year = Integer.parseInt(yearInput);
        System.out.println("Please insert the type of property");
        String propertyTypeInput = scanner.nextLine();
        validatePropertyType(propertyTypeInput);
        PropertyType propertyType = Arrays.stream(PropertyType.values())
                .filter(type -> type.getCode().equals(propertyTypeInput))
                .findFirst()
                .orElseThrow(() -> new CustomException("Invalid property type"));
        System.out.println("Please insert VAT");
        String vat = scanner.nextLine();
        validateVAT(vat);
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
            System.out.println("Failed to create property");
            throw new CustomException("Failed to create property");
        }
    }

    /**
     * Updates the E9 of a property identified by its current E9.
     *
     * Prompts the user to input the current E9 and the new E9. Validates the
     * current E9, retrieves the property, and validates the new E9. Updates the
     * property with the new E9 and saves the changes to the repository.
     *
     * @return The updated property with the new E9.
     * @throws CustomException If the property with the given E9 is not found,
     * if the new E9 is invalid, or if the update operation fails.
     */
    @Override
    public Property updatePropertyE9() throws CustomException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please insert e9: ");
        String e9 = scanner.nextLine().trim();
        validateE9(e9);
        Optional<Property> updateOptionalProperty = propertyRepository.findPropertyByE9(e9);

        if (updateOptionalProperty.isEmpty()) {
            throw new CustomException("Property with E9 " + e9 + " not found.");
        }

        Property property = updateOptionalProperty.get();

        System.out.println("Please insert the new E9:");
        String newE9 = scanner.nextLine().trim();
        validateE9(newE9);

        property.setE9(newE9);
        try {
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty.orElseThrow(() -> new CustomException("Failed to save the updated property with E9 " + newE9));
        } catch (Exception e) {
            System.out.println("Failed to update property with E9 " + newE9);
            throw new CustomException("Failed to update property with E9 " + newE9);
        }
    }

    /**
     * Updates the address of a property identified by its E9.
     *
     * Prompts the user to input the current E9 and the new address. Validates
     * the current E9, retrieves the property, and validates the new address.
     * Updates the property with the new address and saves the changes to the
     * repository.
     *
     * @return The updated property with the new address.
     * @throws CustomException If the property with the given E9 is not found,
     * if the new address is invalid, or if the update operation fails.
     */
    @Override
    public Property updatePropertyAddress() throws CustomException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please insert current E9: ");
        String e9 = scanner.nextLine().trim();
        validateE9(e9);
        Optional<Property> updateOptionalProperty = propertyRepository.findPropertyByE9(e9);
        if (updateOptionalProperty.isEmpty()) {
            throw new CustomException("Property with E9 " + e9 + " not found.");
        }

        Property property = updateOptionalProperty.get();

        System.out.println("Please insert the new address:");
        String newAddress = scanner.nextLine().trim();
        validateAddress(newAddress);

        property.setPropertyAddress(newAddress);
        try {
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty.orElseThrow(() -> new CustomException("Failed to save the updated property with address " + newAddress));
        } catch (Exception e) {
            System.out.println("Failed to update property with address " + newAddress);
            throw new CustomException("Failed to update property with address " + newAddress);
        }
    }

    /**
     * Updates the construction year of a property identified by its E9.
     *
     * Prompts the user to input the current E9 and the new construction year.
     * Validates the current E9, retrieves the property, and validates the new
     * construction year. Updates the property with the new construction year
     * and saves the changes to the repository.
     *
     * @return The updated property with the new construction year.
     * @throws CustomException If the property with the given E9 is not found,
     * if the new construction year is invalid, or if the update operation
     * fails.
     */
    @Override
    public Property updatePropertyConstructionYear() throws CustomException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please insert current E9: ");
        String e9 = scanner.nextLine().trim();
        validateE9(e9);
        Optional<Property> updateOptionalProperty = propertyRepository.findPropertyByE9(e9);
        if (updateOptionalProperty.isEmpty()) {
            throw new CustomException("Property with E9 " + e9 + " not found.");
        }

        Property property = updateOptionalProperty.get();

        System.out.println("Please insert the new construction year:");
        String yearInput = scanner.nextLine().trim();
        validateConstructionYear(yearInput);

        int newYear = Integer.parseInt(yearInput);
        property.setConstructionYear(newYear);
        try {
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty.orElseThrow(() -> new CustomException("Failed to save the updated property with construction year " + newYear));
        } catch (Exception e) {
            System.out.println("Failed to update property with construction year " + newYear);
            throw new CustomException("Failed to update property with construction year " + newYear);
        }
    }

    /**
     * Updates the property type of a property identified by its E9.
     *
     * Prompts the user to input the current E9 and the new property type.
     * Validates the current E9, retrieves the property, and validates the new
     * property type. Updates the property with the new type and saves the
     * changes to the repository.
     *
     * @return The updated property with the new property type.
     * @throws CustomException If the property with the given E9 is not found,
     * if the new property type is invalid, or if the update operation fails.
     */
    @Override
    public Property updatePropertyType() throws CustomException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please insert current E9: ");
        String e9 = scanner.nextLine().trim();
        validateE9(e9);
        Optional<Property> updateOptionalProperty = propertyRepository.findPropertyByE9(e9);
        if (updateOptionalProperty.isEmpty()) {
            throw new CustomException("Property with E9 " + e9 + " not found.");
        }

        Property property = updateOptionalProperty.get();

        System.out.println("Please insert the new property type:");
        String propertyTypeInput = scanner.nextLine().trim();
        validatePropertyType(propertyTypeInput);
        PropertyType newType = Arrays.stream(PropertyType.values())
                .filter(t -> t.getCode().equals(propertyTypeInput))
                .findFirst()
                .orElseThrow(() -> new CustomException("Invalid property type"));

        property.setPropertyType(newType);
        try {
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty.orElseThrow(() -> new CustomException("Failed to save the updated property with property type " + newType));
        } catch (Exception e) {
            System.out.println("Failed to update property with property type " + newType);
            throw new CustomException("Failed to update property with property type " + newType);
        }
    }

    /**
     * Updates the VAT number associated with a property identified by its E9.
     *
     * Prompts the user for the current E9, the new VAT number, and verifies
     * both. If the new VAT number is valid and associated with an existing
     * owner, updates the property with the new VAT and saves the changes to the
     * repository.
     *
     *
     * @return The updated property with the new VAT number.
     * @throws CustomException If the property with the given E9 is not found,
     * if the new VAT number is not associated with any existing owner, or if
     * the update operation fails.
     */
    @Override
    public Property updatePropertyVAT() throws CustomException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please insert current E9: ");
        String e9 = scanner.nextLine().trim();
        validateE9(e9);
        Optional<Property> updateOptionalProperty = propertyRepository.findPropertyByE9(e9);
        if (updateOptionalProperty.isEmpty()) {
            throw new CustomException("Property with E9 " + e9 + " not found.");
        }

        Property property = updateOptionalProperty.get();

        System.out.println("Please insert the new VAT:");
        String newVat = scanner.nextLine().trim();
        validateVAT(newVat);

        Optional<Owner> owner = ownerServiceInterface.searchOwnerByVat(newVat);
        if (owner.isEmpty()) {
            throw new CustomException("Owner with VAT " + newVat + " not found.");
        }

        property.setOwner(owner.get());
        try {
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty.orElseThrow(() -> new CustomException("Failed to save the updated property with VAT " + newVat));
        } catch (Exception e) {
            System.out.println("Failed to update property with VAT " + newVat);
            throw new CustomException("Failed to update property with VAT " + newVat);
        }
    }

    /**
     * Finds a property by its E9 identifier.
     *
     * If no property with the given E9 is found, a CustomException is thrown.
     *
     * @param e9 the E9 identifier of the property to be found
     * @return the property with the given E9
     * @throws CustomException if the property with the given E9 is not found
     */
    @Override
    public Property findByE9(String e9) throws CustomException {
        Optional<Property> property = propertyRepository.findPropertyByE9(e9);

        if (property.isEmpty()) {
            System.out.println("Property with Ε9 " + e9 + " not found");
            throw new CustomException("Property with Ε9 " + e9 + " not found");
        } else {
            if (property.get().isDeleted()) {
                System.out.println("This property is marked as deleted");
            }
            return property.get();
        }
    }

    /**
     * Finds properties associated with a specific VAT identifier.
     *
     * It filters these properties to identify those that are marked as deleted.
     * If no properties are found, either because no properties exist with the
     * given VAT or all properties are marked as deleted, a CustomException is
     * thrown.
     *
     * @param vat the VAT identifier of the properties to be found
     * @return a list of properties associated with the given VAT
     * @throws CustomException if no properties with the given VAT are found
     */
    @Override
    public List<Property> findByVAT(String vat) throws CustomException {

        List<Property> properties = propertyRepository.findPropertyByVAT(vat);
        List<Property> foundProperties = properties.stream()
                .filter(Property::isDeleted)
                .collect(Collectors.toList());
        if (properties.isEmpty()) {
            System.out.println("Properties not found based on vat " + vat);
            throw new CustomException("Properties not found based on vat " + vat);
        } else {
            if (properties == foundProperties) {
                System.out.println("This property is marked as deleted");
            }
            return properties;
        }
    }

    /**
     * Finds a property by its ID.
     *
     * If no property with the given ID is found, a CustomException is thrown.
     *
     * @param id the ID of the property to be found
     * @return the property with the given ID
     * @throws CustomException if the property with the given ID is not found
     */
    @Override
    public Property findByID(Long id) throws CustomException {
        Optional<Property> property = propertyRepository.findById(id);
        if (property.isEmpty()) {
            System.out.println("Property with ID: " + id + " not found");
            throw new CustomException("Property with ID: " + id + " not found");
        }
        return property.get();
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
            System.out.println("Failed to safely delete property with ID: " + id);
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
            System.out.println("Failed to permanently delete property with ID: " + id);
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
     * Validates the address value. Ensures that the address is not null or
     * blank.
     *
     * @param address The address value to validate.
     * @throws CustomException If the address is null or blank.
     */
    @Override
    public void validateAddress(String address) throws CustomException {
        if (address == null || address.isBlank()) {
            throw new CustomException("Address cannot be null or blank.");
        }
    }

    /**
     * Validates the construction year input. Ensures that the year is not null
     * or blank, and is a positive integer.
     *
     * @param yearInput The construction year input to validate.
     * @throws CustomException If the year is null, blank, zero, negative, or
     * not a valid integer.
     */
    @Override
    public void validateConstructionYear(String yearInput) throws CustomException {
        if (yearInput == null || yearInput.isBlank()) {
            throw new CustomException("Year cannot be null or blank.");
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
     * or blank.
     *
     * @param type The property type to validate.
     * @throws CustomException If the property type is null or blank.
     */
    @Override
    public void validatePropertyType(String type) throws CustomException {
        if (type == null || type.isBlank()) {
            throw new CustomException("Property type cannot be null or blank.");
        }
    }

    /**
     * Validates the VAT value. Ensures that the VAT is not null or blank.
     *
     * @param vat The VAT value to validate.
     * @throws CustomException If the VAT is null or blank.
     */
    @Override
    public void validateVAT(String vat) throws CustomException {
        if (vat == null || vat.isBlank()) {
            throw new CustomException("VAT cannot be null or blank.");
        }
    }
}
