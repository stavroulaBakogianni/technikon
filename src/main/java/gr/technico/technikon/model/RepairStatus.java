package gr.technico.technikon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RepairStatus {
    PENDING("Pending"),
    DECLINED("Declined"),
    INPROGRESS("In progress"),
    COMPLETE("Complete");

    private final String code;
}
