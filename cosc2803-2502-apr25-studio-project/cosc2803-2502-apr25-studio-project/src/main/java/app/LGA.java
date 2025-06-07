package app;

/**
 * Class represeting a LGA from the Studio Project database
 * In the template, this only uses the code and name for 2016
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 */
public class LGA {
   // LGA Code
   private int code;

   // LGA Name
   private String name;

   // LGA Year
   private int year;

   /**
    * Create an LGA and set the fields
    */
   public LGA(int code, String name, int year) {
      this.code = code;
      this.name = name;
      this.year = year;
   }

   public int getCode() {
      return code;
   }

   public String getName() {
      return name;
   }

   public int getYear() {
      return year;
   }
}
