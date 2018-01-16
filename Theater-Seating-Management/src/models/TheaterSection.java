package models;

public class TheaterSection implements Comparable<TheaterSection>{

    private int rowNumber;
    private int sectionNumber;
    private int capacity;
    private int availableSeats;

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    @Override
    public int compareTo(TheaterSection t) {
        
        int result = 0;
        
        if((this.availableSeats - t.availableSeats) == 0){
            
            if((this.rowNumber - t.rowNumber) == 0){
                
                result = this.sectionNumber - t.sectionNumber;
                
            }else{
                
                result = this.rowNumber - t.rowNumber;
                
            }
            
        }else{
            
            result = this.availableSeats - t.availableSeats;
            
        }
        
        return result;
        
    }

    @Override
    public String toString() {

        return "Row #: " + rowNumber + " " + "Section #: " + sectionNumber + " Capacity: " + capacity + " availableSeats: " + availableSeats;

    }

}
