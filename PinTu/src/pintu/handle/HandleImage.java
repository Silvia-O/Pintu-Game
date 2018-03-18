package pintu.handle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HandleImage {
    
	public void deleteAll() {
		String pathName = this.getClass().getResource("/images/subImages/").getFile();
		File file = new File(pathName);
		if(file.isDirectory()) {
			String []fileArr = file.list();
			if(fileArr.length > 0) {
				for(int i=0; i<fileArr.length; i++) {
					String fileName = this.getClass().getResource("/images/subImages/").getFile() + fileArr[i];
				    File f = new File(fileName);
				    if(f.isFile()) {
				    	f.delete();
				    }
				}
			}
		}
	}
	
	
	public void cuttingImage(int size, int rowSize, int colSize, String preName, String imageName) {
		String fileName = this.getClass().getResource("/images/" + imageName).getFile();
		File file = new File(fileName);
		try {
		    FileInputStream fis = new FileInputStream(file);
		    BufferedImage bi = ImageIO.read(fis);
		    fis.close();
		    
		    for(int row=0; row<rowSize; row++) {
		    	for(int col=0; col<colSize; col++) {
		    		int imageIndex = row*colSize + col + 1;   //Ð¡Í¼Æ¬ÐòºÅ
		    		String smallFileName = preName + "_" + imageIndex + ".jpg";
		    		String pathAndName = this.getClass().getResource("/images/subImages/").getFile() + smallFileName;
		    		//getSubimage()
		    		BufferedImage smallImage = bi.getSubimage(col*size, row*size, size, size);
		    		FileOutputStream fos = new FileOutputStream(pathAndName);
		    		ImageIO.write(smallImage, "jpg", fos);
		    		fos.close();
		    	}
		    }
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
