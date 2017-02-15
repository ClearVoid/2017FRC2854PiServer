package VisualProcessing;

import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class VisualData implements Serializable {

		private static final long serialVersionUID = 876758800936278607L;
		private Rectangle rect;
		private int distance;
		private double angle;
		private double error;

		public VisualData(Rectangle rect, int distance, double angle,
				double error) {
			this.rect = rect;
			this.distance = distance;
			this.angle = angle;
			this.error = error;
		}

		public static String encode(VisualData[] data) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos;
				oos = new ObjectOutputStream(baos);
				oos.writeObject(data);
				oos.close();
				return Base64.getEncoder().encodeToString(baos.toByteArray());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		public static VisualData[] decode(String s) throws IOException,
				ClassNotFoundException {
			byte[] data = Base64.getDecoder().decode(s);
			ObjectInputStream ois = new ObjectInputStream(
					new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			return (VisualData[]) o;
		}

	}
