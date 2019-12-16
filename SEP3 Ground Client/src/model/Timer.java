package model;

import java.io.Serializable;

public class Timer implements Serializable
{

   private int Seconds;
   private int Minutes;
   private int Hour;

   public Timer(int seconds, int minutes, int hour)
   {
      setTimer(seconds, minutes, hour);
   }

   public Timer(int seconds)
   {
      this.Hour = seconds / 3600;
      this.Minutes = (seconds % 3600) / 60;
      this.Seconds = (seconds % 3600) % 60;
   }

   public void setTimer(int seconds, int minutes, int hour)
   {
      setHour(hour);
      setMinute(minutes);
      setSecond(seconds);
   }

   public void setTimer(Timer timer)
   {
      setHour(timer.getHour());
      setMinute(timer.getMinute());
      setMinute(timer.getSecond());
   }

   public void setHour(int hour)
   {
      if (hour > 23)
      {
         this.Hour = 23;
      }
      else if (hour < 0)
      {
         this.Hour = 0;
      }
      else
      {
         this.Hour = hour;
      }
   }

   public void setMinute(int minute)
   {
      if (minute > 59)
      {
         this.Minutes = 59;
      }
      else if (minute < 0)
      {
         this.Minutes = 0;
      }
      else
      {
         this.Minutes = minute;
      }
   }

   public void setSecond(int second)
   {
      if (second > 59)
      {
         this.Seconds = 59;
      }
      else if (second < 0)
      {
         this.Seconds = 0;
      }
      else
      {
         this.Seconds = second;
      }
   }

   public void increment()
   {
      this.Seconds++;
      if (this.Seconds == 60)
      {
         this.Seconds = 0;
         this.Minutes++;
      }

      if (this.Minutes == 60)
      {
         this.Minutes = 0;
         this.Hour++;
      }

      if (this.Hour == 24)
      {
         this.Hour = 0;
      }
   }

   public void decrement()
   {
      this.Seconds--;

      if (this.Seconds == -1)
      {
         this.Seconds = 59;
         this.Minutes--;
      }

      if (this.Minutes == -1)
      {
         this.Minutes = 59;
         this.Hour--;
      }

      if (this.Hour == -1)
      {
         this.Hour = 23;
      }

   }

   public int getHour()
   {
      return Hour;
   }

   public int getMinute()
   {
      return Minutes;
   }

   public int getSecond()
   {
      return Seconds;
   }

   public Timer timeNow()
   {
      return new Timer(this.Seconds, this.Minutes, this.Hour);
   }

   public int convertToSeconds()
   {
      return this.Hour * 60 * 60 + this.Minutes * 60 + this.getSecond();
   }

   public boolean isBefore(Timer timer)
   {
      return this.convertToSeconds() < timer.convertToSeconds();
   }

   public boolean isAfter(Timer timer)
   {
      return this.convertToSeconds() > timer.convertToSeconds();
   }

   public Timer calculateDifference(Timer timer)
   {
      int seconds = 0;
      if (this.isAfter(timer))
      {
         seconds = this.convertToSeconds() - timer.convertToSeconds();
      }
      else if (this.isBefore(timer))
      {
         seconds = timer.convertToSeconds() - this.convertToSeconds();
      }
      else if (this.equals(timer))
      {
         seconds = 0;
      }
      return new Timer(seconds);
   }

   public String toString()
   {
      String s = "";

      if (this.Hour < 10)
      {
         s += "0" + this.Hour + ":";
      }
      else
      {
         s += this.Hour + ":";
      }

      if (this.Minutes < 10)
      {
         s += "0" + this.Minutes + ":";
      }
      else
      {
         s += this.Minutes + ":";
      }

      if (this.Seconds < 10)
      {
         s += "0" + this.Seconds;
      }
      else
      {
         s += this.Seconds;
      }

      return s;

   }

   public boolean equals(Object obj)
   {
      if (!(obj instanceof Timer))
      {
         return false;
      }
      Timer other = (Timer) obj;
      return this.Seconds == other.getSecond()
            && this.Minutes == other.getMinute()
            && this.Hour == other.getHour();
   }

}
