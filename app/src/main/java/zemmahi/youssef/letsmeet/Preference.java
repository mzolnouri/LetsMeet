package zemmahi.youssef.letsmeet;

/**
 * Created by youssef on 24/02/2016.
 */
public class Preference {
    private Integer idLocarion_;
    private String priority_=Constants.DefaultPriority;

    public Preference(int id, Position position)
    {
        idLocarion_=id;
    }
    public Preference(Integer id,String priority)
    {
        idLocarion_=id;
        priority_=priority;
    }

    public Integer getId() {
        return idLocarion_;
    }

    public void setId(Integer id) {
        this.idLocarion_ = id;
    }

    public String getPriority() {
        return priority_;
    }

    public void setPriority(String priority) {
        this.priority_ = priority;
    }

    public void setToHighPriority()
    {
        priority_=Constants.highPriority;
    }
    public void setToMediumPriority()
    {
        priority_=Constants.mediumPriority;
    }
    public void setToLowPriority()
    {
        priority_=Constants.lowPriority;
    }
    public void setToDefaultPriority()
    {
        priority_=Constants.DefaultPriority;
    }
}
