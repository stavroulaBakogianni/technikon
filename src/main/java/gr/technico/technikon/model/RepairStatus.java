package gr.technico.technikon.model;

import lombok.*;

@AllArgsConstructor
@Getter
public enum RepairStatus {
    PENDING("Pending"),
    DECLINED("Declined"),
    INPROGRESS("In progress"),
    COMPLETE("COMPLETE");

    private final String code;
}
