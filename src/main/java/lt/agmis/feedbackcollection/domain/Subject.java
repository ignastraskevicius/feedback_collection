package lt.agmis.feedbackcollection.domain;


import javax.persistence.*;

@Entity
@Table(name="subject")
@NamedQuery(name = "qq", query = "from Subject where id = :id")
public class Subject {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable=false)
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
