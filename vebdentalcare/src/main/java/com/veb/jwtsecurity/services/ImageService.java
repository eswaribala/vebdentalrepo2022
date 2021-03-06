package com.veb.jwtsecurity.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.veb.jwtsecurity.models.ImageModel;
import com.veb.jwtsecurity.repositories.ImageRepository;


@Service
public class ImageService {
	@Autowired
	private ImageRepository imageRepo;
	
    public void saveImage(MultipartFile file) throws IOException
    {
    	ImageModel img = new ImageModel(file.getOriginalFilename(), file.getContentType(),
				compressBytes(file.getBytes()));
	
    	this.imageRepo.save(img);
    }
    
    public ImageModel getImageByName(String imageName)
    {
    	final ImageModel retrievedImage = imageRepo.findByName(imageName).get(0);
		ImageModel img = new ImageModel(retrievedImage.getName(), retrievedImage.getType(),
				decompressBytes(retrievedImage.getPicByte()));
		return img;
    }
    
    
    public byte[] getImageByNameToArray(String imageName)
    {
    	final ImageModel retrievedImage = imageRepo.findByName(imageName).get(0);
    	return decompressBytes(retrievedImage.getPicByte());
    }
    
 // compress the image bytes before storing it in the database
 	public static byte[] compressBytes(byte[] data) {
 		Deflater deflater = new Deflater();
 		deflater.setInput(data);
 		deflater.finish();
 		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
 		byte[] buffer = new byte[1024];
 		while (!deflater.finished()) {
 			int count = deflater.deflate(buffer);
 			outputStream.write(buffer, 0, count);
 		}
 		try {
 			outputStream.close();
 		} catch (IOException e) {
 		}
 		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
 		return outputStream.toByteArray();
 	}
    
 // uncompress the image bytes before returning it to the angular application
    private static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
    
}
