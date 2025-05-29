public class Event {
    private String title;
    private String category;
    private String descriptionTitle;
    private String description;
    private String location;
    private String date;
    private String imagePath;
    private double ticketPrice;
    private int availableTickets;

    // Full constructor with descriptionTitle
    public Event(String title,
                 String category,
                 String descriptionTitle,
                 String description,
                 String location,
                 String date,
                 String imagePath,
                 double ticketPrice,
                 int availableTickets) {
        this.title             = title;
        this.category          = category;
        this.descriptionTitle  = descriptionTitle;
        this.description       = description;
        this.location          = location;
        this.date              = date;
        this.imagePath         = imagePath;
        this.ticketPrice       = ticketPrice;
        this.availableTickets  = availableTickets;
    }

    // Legacy constructor without descriptionTitle (for backward compatibility)
    public Event(String title,
                 String category,
                 String description,
                 String location,
                 String date,
                 String imagePath,
                 double ticketPrice,
                 int availableTickets) {
        this(title,
             category,
             "",          // no descriptionTitle
             description,
             location,
             date,
             imagePath,
             ticketPrice,
             availableTickets);
    }

    // Getters and setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescriptionTitle() {
        return descriptionTitle;
    }

    public void setDescriptionTitle(String descriptionTitle) {
        this.descriptionTitle = descriptionTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    @Override
    public String toString() {
        return "Event{" +
               "title='" + title + '\'' +
               ", category='" + category + '\'' +
               ", descriptionTitle='" + descriptionTitle + '\'' +
               ", description='" + description + '\'' +
               ", location='" + location + '\'' +
               ", date='" + date + '\'' +
               ", imagePath='" + imagePath + '\'' +
               ", ticketPrice=" + ticketPrice +
               ", availableTickets=" + availableTickets +
               '}';
    }
}
