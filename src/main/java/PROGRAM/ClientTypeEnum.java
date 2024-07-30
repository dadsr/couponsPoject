package PROGRAM;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum ClientTypeEnum {
    ADMINISTRATOR(1),
    COMPANY(2),
    CUSTOMER(3);
    private int id;

    ClientTypeEnum(int i) {
        this.id = id;
    }


}
