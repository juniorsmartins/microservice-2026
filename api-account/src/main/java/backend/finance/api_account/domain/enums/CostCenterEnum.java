package backend.finance.api_account.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CostCenterEnum {

    BANKING("BANKING"),
    BARBERSHOP("BARBERSHOP"),
    CLOTHING("CLOTHING"),
    EDUCATION("EDUCATION"),
    ELETRONIC("ELETRONIC"),
    ENTERTAINMENT("ENTERTAINMENT"),
    FGTS("FGTS"),
    FOOD("FOOD"),
    HEALTH("HEALTH"),
    HOUSE("HOUSE"),
    IRPF("IRPF"),
    OTHER("OTHER"),
    PENSION("PENSION"),
    PET("PET"),
    RENT("RENT"),
    SERVICES("SERVICES"),
    SPORT("SPORT"),
    SUPPLEMENTATION("SUPPLEMENTATION"),
    TELEPHONY("TELEPHONY"),
    TRANSPORT("TRANSPORT"),
    WAGE("WAGE");

    private final String value;
}
