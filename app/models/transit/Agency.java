package models.transit;

import java.util.Set;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import play.db.jpa.Model;

import models.transit.*;
import models.gis.*;

@JsonIgnoreProperties({"entityId", "persistent"})
@Entity
public class Agency extends Model {
	
	public String gtfsAgencyId;
    public String name;
    public String url;
    public String timezone;
    public String lang;
    public String phone;
    
    public String color;
    
    public Boolean systemMap;

    public Double defaultLat;
    public Double defaultLon;
    
    @ManyToOne
    public RouteType defaultRouteType;

    @JsonCreator
    public static Agency factory(long id) {
      return Agency.findById(id);
    }

    @JsonCreator
    public static Agency factory(String id) {
      return Agency.findById(Long.parseLong(id));
    }
    
    public Agency(org.onebusaway.gtfs.model.Agency agency) {
        this.gtfsAgencyId = agency.getId();
        this.name = agency.getName();
        this.url = agency.getUrl();
        this.timezone = agency.getTimezone();
        this.lang = agency.getLang();
        this.phone = agency.getPhone();
    }
    
    public Agency(String gtfsAgencyId, String name, String url, String timezone, String lang, String phone) {
        this.gtfsAgencyId = gtfsAgencyId;
        this.name = name;
        this.url = url;
        this.timezone = timezone;
        this.lang = lang;
        this.phone = phone;
    }

    public Agency delete() {
        List<ServiceCalendar> calendars = ServiceCalendar.find("agency = ?", this).fetch();
        for(ServiceCalendar calendar : calendars)
        {
            calendar.delete();
        }

        List<Route> routes = Route.find("agency = ?", this).fetch();
        for(Route route : routes)
        {
            route.delete();
        }

        // List<GisExport>
        // Query query = JPA.em().createQuery("select * from gisexport_agency");
        // List<GisExport> gisexports = GisExport.find("agencies_id = ?", this).fetch();
        // for(GisExport export : gisexports)
        // {
        //     export.delete();
        // }
        return super.delete();
    }

}
