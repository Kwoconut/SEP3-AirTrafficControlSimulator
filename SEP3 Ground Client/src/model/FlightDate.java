package model;

public class FlightDate extends Timer
{
   private int Day;
   private int Month;
   private int Year;

   public FlightDate(int year, int month, int day, int hour, int minute,
         int second)
   {
      super(hour, minute, second);
      this.Year = year;
      this.Month = month;
      this.Day = day;
   }

   public FlightDate(int year, int month, int day, Timer timer)
   {
      super(timer.getHour(), timer.getMinute(), timer.getSecond());
      this.Year = year;
      this.Month = month;
      this.Day = day;
   }

   public FlightDate(int year, int month, int day, int seconds)
   {
      super(seconds);
      this.Year = year;
      this.Month = month;
      this.Day = day;
   }

   public FlightDate dateNow()
   {
      return new FlightDate(this.Year, this.Month, this.Day, super.getHour(),
            super.getMinute(), super.getSecond());
   }
   

}
