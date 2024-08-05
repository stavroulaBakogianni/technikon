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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        System.out.println("Please insert address");
        String address = scanner.nextLine();
        System.out.println("Please insert year of construction");
        int year = scanner.nextInt();
        System.out.println("Please insert the type of property");
        String propertyTypeInput = scanner.nextLine();
        PropertyType propertyType = Arrays.stream(PropertyType.values())
                .filter(type -> type.getCode().equals(propertyTypeInput))
                .findFirst()
                .orElseThrow(() -> new CustomException("Invalid property type"));
        System.out.println("Please insert VAT");
        String vat = scanner.nextLine();
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
            log.error("Failed to create property");
            throw new CustomException("Failed to create property");
        }
    }

    /**
     * Updates an existing Property entity based on user input.
     *
     * This method retrieves a Property entity by its ID, prompts the user to
     * input updated details for the property. It validates the input, retrieves
     * the owner by VAT, and then updates and saves the Property entity. If any
     * validation fails or an error occurs during the update, a CustomException
     * is thrown.
     *
     * @param id the ID of the Property entity to be updated
     * @return the updated Property entity, if the update is successful
     * @throws CustomException if any of the following occur: The property with
     * the specified ID is not found. The property type provided is invalid. The
     * provided VAT does not correspond to any existing owner. Failed to save
     * the updated Property entity to the database.
     */
    @Override
    public Property updateProperty(Long id) throws CustomException {
        Optional<Property> findPropertyOptional = propertyRepository.findById(id);
        if (findPropertyOptional.isEmpty()) {
            log.error("Property with ID " + id + " not found");
            throw new CustomException("Property not found based on ID " + id);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please insert e9");
        String e9 = scanner.nextLine();
        System.out.println("Please insert address");
        String address = scanner.nextLine();
        System.out.println("Please insert year of construction");
        int year = scanner.nextInt();
        System.out.println("Please insert the type of property");
        String propertyTypeInput = scanner.nextLine();
        PropertyType propertyType = Arrays.stream(PropertyType.values())
                .filter(type -> type.getCode().equals(propertyTypeInput))
                .findFirst()
                .orElseThrow(() -> new CustomException("Invalid property type"));
        System.out.println("Please insert VAT");
        String vat = scanner.nextLine();
        Optional<Owner> owner = ownerServiceInterface.searchOwnerByVat(vat);
        if (owner.isEmpty()) {
            throw new CustomException("Not valid Vat");
        }

        Property property = findPropertyOptional.get();
        property.setE9(e9);
        property.setPropertyAddress(address);
        property.setConstructionYear(year);
        property.setPropertyType(propertyType);
        property.setOwner(owner.get());

        try {
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty.get();
        } catch (Exception e) {
            log.error("Failed to update property with ID " + id);
            throw new CustomException("Failed to update property with ID " + id);
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
            log.info("Property with Ε9 " + e9 + " not found");
            throw new CustomException("Property with Ε9 " + e9 + " not found");
        }
        return property.get();
    }

    /**
     * Finds properties associated with a specific VAT identifier.
     *
     * If no properties with the given VAT are found, a CustomException is
     * thrown.
     *
     * @param vat the VAT identifier of the properties to be found
     * @return a list of properties associated with the given VAT
     * @throws CustomException if no properties with the given VAT are found
     */
    @Override
    public List<Property> findByVAT(String vat) throws CustomException {
        List<Property> properties = propertyRepository.findPropertyByVAT(vat);
        if (properties.isEmpty()) {
            log.info("Properties not found based on vat " + vat);
            throw new CustomException("Properties not found based on vat " + vat);
        }
        return properties;
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
            log.info("Property with ID: " + id + " not found");
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
            log.info("Failed to safely delete property with ID: " + id);
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
            log.info("Failed to permanently delete property with ID: " + id);
            throw new CustomException("Failed to permanently delete property with ID: " + id);
        }
        return true;
    }

}
