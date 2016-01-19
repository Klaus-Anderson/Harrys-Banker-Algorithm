
public class Resource {

	private String resourceType;
	private int number;
	
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Resource(String _resourceType, int _number) {
		resourceType = _resourceType;
		number = _number;
	}

}
