package app;

/**
 * Class represeting a FlagQuality from the Studio Project database
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @editor David Eccles, 2025. email: david.eccles@rmit.edu.au 
 */
public class FLAG {
   // Flag
   private String flag;

   // Description
   private String description;
      
   /**
    * Create flagQuality and set the fields
    **/


   public FLAG( String flag, String description ) {
       this.flag = flag;
       this.description = description;
   }

  
   public String getFlag() {
      return flag;
   }

   public String getDescription() {
      return description;
   }
}
