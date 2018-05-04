package one.show.live.po;

import java.util.List;

public class POProvinceModel {
	private String name;
	private List<POCityModel> cityList;
	
	public POProvinceModel() {
		super();
	}

	public POProvinceModel(String name, List<POCityModel> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<POCityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<POCityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "POProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}
	
}
