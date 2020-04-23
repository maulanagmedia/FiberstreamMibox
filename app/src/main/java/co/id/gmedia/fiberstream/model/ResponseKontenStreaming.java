package co.id.gmedia.fiberstream.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ResponseKontenStreaming implements Serializable {

	@SerializedName("response")
	private List<KontenStreaming> response;

	@SerializedName("metadata")
	private MetadataKontenStreaming metadata;

	public void setResponse(List<KontenStreaming> response){
		this.response = response;
	}

	public List<KontenStreaming> getResponse(){
		return response;
	}

	public void setMetadata(MetadataKontenStreaming metadata){
		this.metadata = metadata;
	}

	public MetadataKontenStreaming getMetadata(){
		return metadata;
	}

	@Override
 	public String toString(){
		return 
			"KontenStreamingResponseKontenStreaming{" + 
			"response = '" + response + '\'' + 
			",metadata = '" + metadata + '\'' + 
			"}";
		}
}