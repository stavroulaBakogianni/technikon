package gr.technico.technikon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PropertyType {
    DETACHEDHOUSE("Detached house"),
    MAISONETTE("Maisonette"),
    APARTMENTBUILDING ("Apartment building");

    private final String code;
}
