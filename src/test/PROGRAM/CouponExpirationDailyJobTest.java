package PROGRAM;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouponExpirationDailyJobTest {

    @Test
    void run() {
        new CouponExpirationDailyJob(600L).run();
    }

    @Test
    void stop() {
        new CouponExpirationDailyJob(600L).stop();
    }

}