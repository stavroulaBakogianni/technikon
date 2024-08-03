package gr.technico.technikon.services;

import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;

public interface PropertyService {
    Property createProperty(String  e9, String propertyAddress, 
             int constructionYear, PropertyType propertyType, Owner owner  );
}
