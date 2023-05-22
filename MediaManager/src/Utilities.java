
public class Utilities {
	
	public static String getFileType(String fileName) {
		String type;
		
		String extension = "";
		int index = fileName.lastIndexOf('.');
		if (index > 0) {
		  extension = fileName.substring(index + 1);
		}
		

		switch (extension) {
			case "mp3": case "wav":
				type = "music";
				break;
			case "avi": case "mp4": case "mov":
				type = "video";
				break;
				
			case "jpg": case "jpeg": case "img":
				type = "image";
				break;
				
			case "png": case "pngf":
				type = "image";
				break;
				
				
			case "cda": case "cd":
				type = "cd audio track";
				break;
			default:
				 type = "unknown";
		}
			
		
		return type;
		
	} 
	
}
