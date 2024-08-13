package PROGRAM;
import BEANS.CouponException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import FACADE.CouponFacade;


import java.sql.SQLException;

public class CouponExpirationDailyJob  implements Runnable {
    protected static final Logger logger = LogManager.getLogger();
    private Long sleepingInMillis = 600L;
    private boolean quit =false;

    public CouponExpirationDailyJob(Long sleepingInMillis) {
        this.sleepingInMillis = sleepingInMillis;
    }

    /**
     * Runnable interface's run() method does not allow checked exceptions to be thrown.
     * avoiding this by handling the exception within the run() method
     */
    @Override
    public void run() {
        logger.info("run started");
        System.out.println("expirationJob started");
        while (!quit) {
            try {
                new CouponFacade().deleteExpiredCoupons();
                logger.info("sleep for {}", sleepingInMillis);
                Thread.sleep(sleepingInMillis);
            } catch (InterruptedException e) {
                logger.warn("Thread was interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (SQLException e) {
                logger.error("SQL error occurred", e);
            } catch (CouponException e) {
                logger.error("Coupon exception occurred", e);
            }
        }
        logger.info("run stopped");

    }

    public void stop (){
        logger.info("stop");
        this.quit =true;
        System.out.println("expirationJob stoped");
    }
/////
}


