package modelo;

import java.util.List;

public class Cats<T> {
	 public List<T> breeds;
	 public List<T> categories;
	 public String id;
	 public String url;
	 public int width;
	 public int height;
	 
	 
	 public List<T> getCategories() {
		return categories;
	}
	public void setCategories(List<T> categories) {
		this.categories = categories;
	}
	
	public List<T> getBreeds() {
		return breeds;
	}
	public void setBreeds(List<T> breeds) {
		this.breeds = breeds;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public Cats(List<T> breeds, List<T> categories, String id, String url, int width, int height) {
		this.breeds = breeds;
		this.categories = categories;
		this.id = id;
		this.url = url;
		this.width = width;
		this.height = height;
	}
	public Cats() {
		super();
	}
	

}
