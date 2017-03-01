package domain;

public class Car {

	private int id;
	private String registryPlate;

	public Car() {
		
	}

	public Car(int id, String registryPlate) {
		this.id = id;
		this.registryPlate = registryPlate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegistryPlate() {
		return registryPlate;
	}

	public void setRegistryPlate(String registryPlate) {
		this.registryPlate = registryPlate;
	}

	@Override
	public String toString() {
		return registryPlate;
	}

}
