package MAIN;

import java.sql.Date;
import java.util.Random;

public  class RandomGenerators {

    public static Date dateGenerator(String sMinDay,String sMaxDay) {
            Random random = new Random();
            // Define a range for the date generation
            long minDay = Date.valueOf(sMinDay).getTime();
            long maxDay = Date.valueOf(sMaxDay).getTime();
            // Generate a random day within the range
            long randomDay = minDay + (long) (random.nextDouble() * (maxDay - minDay));
            // Create the sql date
            return new Date(randomDay);
        }



}
