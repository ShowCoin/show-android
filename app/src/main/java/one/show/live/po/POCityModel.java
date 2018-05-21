package one.show.live.po;

public class POCityModel {
	private String name;

	public POCityModel() {
		super();
	}

	public POCityModel(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Override
	public String toString() {
		return "POCityModel [name=" + name +  "]";
	}
	
}
