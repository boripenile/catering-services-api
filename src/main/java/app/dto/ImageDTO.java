package app.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageDTO {

	@Expose
    @SerializedName("id")
	private String id;
	
	@Expose
    @SerializedName("image")
	private Object image;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getImage() {
		return image;
	}

	public void setImage(Object image) {
		this.image = image;
	}

	public ImageDTO() {
	}

	public ImageDTO(String id, Object image) {
		this.id = id;
		this.image = image;
	}
	
}
