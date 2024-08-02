package PROGRAM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import FACADE.CouponFacade;

public class CouponExpirationDailyJob  implements Runnable {
    protected static final Logger logger = LogManager.getLogger();

    public CouponExpirationDailyJob() {
    }

    @Override
    public void run() {
        logger.info("run");

/*    while (true) {
            CouponFacade.deleteExpiredCoupons();
            Thread.sleep(3 600 000);
        }
    }
  */

    }
}

