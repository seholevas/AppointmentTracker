/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;
/**
 *
 * @author Steven
 */
public class Appointment 
{
    //variables
    private String appointmentID;
    private Customer client;
    private User representative;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private String start;
    private String end;
    private String startTime;
    private String endTime;
    private String date;
    private String clientName;
    
    private String month;
    private  Integer number = 0;
//    private  Integer statusUpdateMeetingCount = 0;
//    private  Integer decisionMakingMeetingCount = 0;
//    private  Integer innovationMeetingCount = 0;
    
    public Appointment(Integer _number, String _month, String _type)
    {
        month = _month;
        type = _type;
        number = _number;
    }
    
    public Appointment(String _location, String _type, Integer _number)
    {
        location = _location;
        type = _type;
        this.number = _number;
    }
    
    public Appointment(String _date, String _start, String _end, Customer _customer, String _type)
    {
        start = _start;
        end = _end;
        client = _customer;
        type = _type;  
        date = end.subSequence(0, 10).toString();
    }
    
    public Appointment(String passedAppointmentID, Customer passedClient, User passedRepresentative, String passedTitle, String passedDescription, String passedLocation, String passedContact, String passedAppointmentType, String passedURL, String _start, String _end)
    {
        appointmentID = passedAppointmentID;
        client = passedClient;
        representative = passedRepresentative;
        title = passedTitle;
        description = passedDescription;
        location = passedLocation;
        contact = passedContact;
        type = passedAppointmentType;
        url = passedURL;
        start = _start;
        end = _end;
        
        startTime = start.subSequence(11, 16).toString();
        endTime = end.subSequence(11, 16).toString();
        date = end.subSequence(0, 10).toString(); 
        clientName = client.getCustomerName();
    }
    
    public Appointment(String passedAppointmentID, Customer passedClient, User passedRepresentative, String passedTitle, String passedDescription, String passedLocation, String passedContact, String passedAppointmentType, String passedURL)
    {
        appointmentID = passedAppointmentID;
        client = passedClient;
        representative = passedRepresentative;
        title = passedTitle;
        description = passedDescription;
        location = passedLocation;
        contact = passedContact;
        type = passedAppointmentType;
        url = passedURL;
        
    }
    
    
    
    
    
    
    public String getAppointmentID() 
    {
        return appointmentID;
    }

    /**
     * @param appointmentID the appointmentID to set
     */
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * @return the client
     */
    public Customer getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Customer client) {
        this.client = client;
    }
    
    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the representive
     */
    
    public User getRepresentative() {
        return representative;
    }

    /**
     * @param representive the representive to set
     */
    public void setRepresentive(User representive) {
        this.representative = representive;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getStart()
    {
        return start;
    }
    public void setStart(String _start)
    {
        start = _start;
    }
    
    public String getEnd()
    {
        return end;
    }
    public void setEnd(String _end)
    {
        end = _end;
    }
    
    /**
     * @return the clientName
     */
    public String getClientName() 
    {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) 
    {
        this.clientName = clientName;
    }

    
//    /**
//     * @return the statusUpdateMeetingCount
//     */
//    public  Integer getStatusUpdateMeetingCount() {
//        return statusUpdateMeetingCount;
//    }
//
//    /**
//     * @param aStatusUpdateMeetingCount the statusUpdateMeetingCount to set
//     */
//    public  void setStatusUpdateMeetingCount(Integer aStatusUpdateMeetingCount) {
//        statusUpdateMeetingCount = aStatusUpdateMeetingCount;
//    }
//
//    /**
//     * @return the decisionMakingMeetingCount
//     */
//    public  Integer getDecisionMakingMeetingCount() {
//        return decisionMakingMeetingCount;
//    }
//
//    /**
//     * @param aDecisionMakingMeetingCount the decisionMakingMeetingCount to set
//     */
//    public  void setDecisionMakingMeetingCount(Integer aDecisionMakingMeetingCount) {
//        decisionMakingMeetingCount = aDecisionMakingMeetingCount;
//    }
//
//    /**
//     * @return the innovationMeetingCount
//     */
//    public  Integer getInnovationMeetingCount() {
//        return innovationMeetingCount;
//    }
//
//    /**
//     * @param aInnovationMeetingCount the innovationMeetingCount to set
//     */
//    public  void setInnovationMeetingCount(Integer aInnovationMeetingCount) {
//        innovationMeetingCount = aInnovationMeetingCount;
//    }

    /**
     * @return the startTime
     */
    public String getStartTime() 
    {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) 
    {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime()
    {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) 
    {
        this.endTime = endTime;
    }
    
    /**
     * @return the month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * @return the number
     */
    public  Integer getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public  void setNumber(Integer number) {
        this.number = number;
    }
    
    public  void incrementNumber()
    {
        this.number++;
    }
    
}
