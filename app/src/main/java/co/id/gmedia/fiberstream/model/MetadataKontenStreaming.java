package co.id.gmedia.fiberstream.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class MetadataKontenStreaming implements Serializable {

	private int status;

	@SerializedName("message")
	private String message;

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"MetadataKontenStreaming{" + 
			"status = '" + status + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}